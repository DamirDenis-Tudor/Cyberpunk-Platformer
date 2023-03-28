package Components.DinamicComponents.Map;

import Components.DinamicComponents.DinamicComponent;
import Components.StaticComponents.Components.ParallaxWallpaper;
import Enums.ComponentNames;
import Scenes.Messages.Message;
import Utils.Coordinate;
import Utils.Rectancle;
import Window.GameWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

import static Utils.Constants.mapDim;
import static Utils.Constants.mapScale;

/**
 * This class contains all the information about the game map:
 * matrix of objects|tiles, dimensions, predefined object positions.
 */
public class GameMap extends DinamicComponent {
    private final GameWindow gameWindow = GameWindow.getInstance();
    private int width; // lines
    private int height; // columns
    private Map<String, MapAsset> tiles; // String - id , Tile
    private Map<String, MapAsset> objects; // String - id , Object
    private String[][] tilesIndexes; // indexes for tiles
    private String[][] objectsIndexes; // indexes for objects
    private ParallaxWallpaper background; // parallax background
    private Map<String, List<Coordinate<Integer>>> entitiesCoordonates; // map of the preloaded entities

    public GameMap(String path) {
        try {
            //   first initialize the document element
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            //  then create the parsed document
            Document document = builder.parse(new File(path));
            document.getDocumentElement().normalize();
            //  create the root element
            Element root = document.getDocumentElement();

            // -------------------------------------
            // first load the tiles by reading all
            // the necessary information
            tiles = new HashMap<>();
            Element source = (Element) root.getElementsByTagName("tileset").item(0);

            Document tilesDocument = builder.parse(new File(source.getAttribute("source").replace("..", "src/Resources")));
            Element tilesRoot = tilesDocument.getDocumentElement();
            NodeList elements = tilesRoot.getElementsByTagName("tile");

            //   then for each element map asset(tile) is created
            //   and added on the tiles map
            for (int index = 0; index < elements.getLength(); ++index) {
                Element tileElement = (Element) elements.item(index);
                Element imageElement = (Element) tileElement.getFirstChild().getNextSibling();

                String tileSource = imageElement.getAttribute("source").replace("..", "src/Resources");
                String tileId = Integer.toString(Integer.parseInt(tileElement.getAttribute("id")) + 1);
                int tileWidth = (int) (Integer.parseInt(imageElement.getAttribute("width")) * mapScale);
                int tileHeight = (int) (Integer.parseInt(imageElement.getAttribute("height")) * mapScale);

                tiles.put(tileId, new MapAsset(tileSource, tileWidth, tileHeight));
            }

            // -------------------------------------
            // load the parallax background
            // and all the necessary information
            background = new ParallaxWallpaper();

            Element backGroundSource = (Element) root.getElementsByTagName("tileset").item(1);
            Document backgroundDocument = builder.parse(new File(backGroundSource.getAttribute("source").replace("..", "src/Resources")));
            document.getDocumentElement().normalize();

            Element backgroundRoot = backgroundDocument.getDocumentElement();

            NodeList backgrounds = backgroundRoot.getElementsByTagName("tile");

            //   then for each element a image
            //   is added in the background object
            for (int index = 0; index < backgrounds.getLength(); ++index) {
                Element tileElement = (Element) backgrounds.item(index);
                Element imageElement = (Element) tileElement.getFirstChild().getNextSibling();
                background.addImage(ImageIO.read(new File(imageElement.getAttribute("source").replace("../", "src/Resources/"))));
            }

            // -------------------------------------
            // load the objects by reading all
            // the necessary information

            objects = new HashMap<>();

            Element objSource = (Element) root.getElementsByTagName("tileset").item(2);
            String lastMapId = objSource.getAttribute("firstgid");
            Document objectsDocument = builder.parse(new File(objSource.getAttribute("source").replace("..", "src/Resources")));
            Element objectsRoot = objectsDocument.getDocumentElement();
            NodeList objectsElements = objectsRoot.getElementsByTagName("tile");

            // for each object element read the information
            // necessary and add the object in the dedicated map
            for (int index = 0; index < objectsElements.getLength(); ++index) {
                Element objectElement = (Element) objectsElements.item(index);
                Element imageElement = (Element) objectElement.getFirstChild().getNextSibling();

                String objectSource = imageElement.getAttribute("source").replace("..", "src/Resources");
                String objectId = Integer.toString(Integer.parseInt(objectElement.getAttribute("id")) + Integer.parseInt(lastMapId));
                int objectWidth = (int) (Integer.parseInt(imageElement.getAttribute("width")) * mapScale);
                int objectHeight = (int) (Integer.parseInt(imageElement.getAttribute("height")) * mapScale);

                objects.put(objectId, new MapAsset(objectSource, objectWidth, objectHeight));
            }


            // -------------------------------------
            // it's the time to load the
            // first layer(tiles layer) matrix of indexes
            Element layer = (Element) root.getElementsByTagName("layer").item(0);
            height = Integer.parseInt(layer.getAttribute("height"));
            width = Integer.parseInt(layer.getAttribute("width"));

            String buffer = root.getElementsByTagName("data").item(0).
                    getFirstChild().getTextContent();

            String[] rows = buffer.split("\n"); // split the string into rows

            height = rows.length - 1;
            width = rows[1].split(",").length;

            tilesIndexes = new String[height][width];

            for (int i = 0; i < height; i++) {
                String[] cols = rows[i + 1].split(",");
                System.arraycopy(cols, 0, tilesIndexes[i], 0, width);
            }

            // -------------------------------------
            // it's the time to load the
            // second layer(objects layer) matrix of indexes
            String objectsBuffer = root.getElementsByTagName("data").item(1).
                    getFirstChild().getTextContent();

            String[] objectsRows = objectsBuffer.split("\n"); // split the string into rows

            objectsIndexes = new String[height][width];

            for (int i = 0; i < height; i++) {
                String[] objectsCols = objectsRows[i + 1].split(",");
                System.arraycopy(objectsCols, 0, objectsIndexes[i], 0, width);
            }

            // -------------------------------------
            // few more code and the map is finally added
            // now load the predefined positions

            entitiesCoordonates = new HashMap<>();
            NodeList positions = root.getElementsByTagName("objectgroup");

            for (int index = 0; index < positions.getLength(); ++index) {
                Node objectgroupNode = positions.item(index);

                Element objectgroupElement = (Element) objectgroupNode;
                String objectgroupName = objectgroupElement.getAttribute("name");
                NodeList objectList = objectgroupElement.getElementsByTagName("object");
                List<Coordinate<Integer>> list = new ArrayList<>();

                for (int objectIndex = 0; objectIndex < objectList.getLength(); ++objectIndex) {
                    Node objectNode = objectList.item(objectIndex);
                    Element objectElement = (Element) objectNode;
                    int x = (int) (Float.parseFloat(objectElement.getAttributeNode("x").getValue()) * mapScale);
                    int y = (int) (Float.parseFloat(objectElement.getAttributeNode("y").getValue()) * mapScale);

                    list.add(new Coordinate<>(x, y));

                    entitiesCoordonates.put(objectgroupName, list);
                }
            }
            System.out.println();

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void notify(Message message) {
        /*
            I don't know yet if the map will 
            make request, but it's nice that it can.
         */
    }

    @Override
    public void update() throws Exception {
        background.update();
    }

    @Override
    public void draw() {
        /*
         * drawing the parallax background
         */
        background.draw();

        /*
         * drawing the tiles
         */
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; j++) {
                if (!Objects.equals(tilesIndexes[i][j], "0")) {
                    MapAsset asset = tiles.get(tilesIndexes[i][j]);
                    int xPos = j * mapDim;
                    int yPos = i * mapDim;
                    int dimW = asset.getWidth();
                    int dimH = asset.getHeight();

                    gameWindow.getGraphics().drawImage(asset.getImage(), xPos, yPos, dimW, dimH, null);
                    gameWindow.getGraphics().drawRect(xPos, yPos, dimW, dimH);
                }
            }
        }

        /*
         * drawing the objects
         */
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; j++) {
                if (!Objects.equals(objectsIndexes[i][j], "0")) {
                    MapAsset asset = objects.get(objectsIndexes[i][j]);
                    int dimW = asset.getWidth();
                    int dimH = asset.getHeight();
                    int xPos = j * mapDim;
                    int yPos = i * mapDim - dimH + mapDim;

                    gameWindow.getGraphics().drawImage(asset.getImage(), xPos, yPos, dimW, dimH, null);
                }
            }
        }
    }
    @Override
    public ComponentNames getType() {
        return ComponentNames.Map;
    }

    /**
     * In this context, map handles the interaction with any movable object.
     * @param component interacts with
     */
    @Override
    public void handleInteractionWith(DinamicComponent component) {
        Rectancle rectancle = component.getCollideBox();

        // first of all wee need to place the component
        // into a specific area of map => clamping
        int xStart = Math.max(0, rectancle.getPosition().getPosX() / mapDim - 1);
        int xEnd   = Math.min(width, (rectancle.getPosition().getPosX() + rectancle.getWidth()) / mapDim + 1);
        int yStart = Math.max(0, rectancle.getPosition().getPosY() / mapDim - 1);
        int yEnd   = Math.min(height, (rectancle.getPosition().getPosY() + rectancle.getHeight()) / mapDim + 1);

        for (int y = yStart; y <= yEnd; y++) {
            for (int x = xStart; x <= xEnd; x++) {
                if (!Objects.equals(tilesIndexes[y][x], "0")) {
                    rectancle.solveCollision(getRectangle(x, y));
                }
            }
        }
    }

    /**
     * 
     * @param x map relative X coordinate
     * @param y map relative Y coordinate
     * @return rectangle object
     */
    public Rectancle getRectangle(int x, int y) {
        Coordinate<Integer> pos = new Coordinate<>(x * mapDim, y * mapDim);
        return new Rectancle(pos, mapDim, mapDim);
    }

    public Coordinate<Integer> getPlayerPosition() {
        return entitiesCoordonates.get("player").get(0);
    }

    public List<Coordinate<Integer>> getEnemiesPositions() {
        return entitiesCoordonates.get("enemies");
    }

    public List<Coordinate<Integer>> getAnimalsPositions() {
        return entitiesCoordonates.get("animals");
    }

    public List<Coordinate<Integer>> getChestsPositions() {
        return entitiesCoordonates.get("chests");
    }

}
