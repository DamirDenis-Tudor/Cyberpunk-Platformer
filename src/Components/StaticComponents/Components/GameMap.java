package Components.StaticComponents.Components;

import Components.StaticComponents.StaticComponent;
import GameWindow.Camera;
import GameWindow.GameWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class GameMap implements StaticComponent {
    private final GameWindow gameWindow = GameWindow.getInstance();
    private static final float mapScale = 1.6875f; // scale factor
    private static final int mapDim = 32; // default map dim
    private int width; // lines
    private int height; // columns
    private Map<String, MapAsset> tiles; // String - id , Tile
    private Map<String, MapAsset> objects; // String - id , Object
    private String[][] tilesIndexes; // indexes for tiles
    private String[][] objectsIndexes; // indexes for objects
    private ParallaxWallpaper background; // parallax background

    // enemy positions
    // player position
    // chest positions
    // bosses positions
    // animals position
    // explosives barrals possitions
    // ladder possitions

    public GameMap(String path) {
        try {

            /*
                first initialize the document element
             */
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document document = builder.parse(new File(path));
            document.getDocumentElement().normalize();

            Element root = document.getDocumentElement();

            /*
              load the tiles
             */

            Element source = (Element) root.getElementsByTagName("tileset").item(0);

            tiles = new HashMap<>();

            Document tilesDocument = builder.parse(new File(source.getAttribute("source").replace("..", "src/ResourcesFiles")));
            document.getDocumentElement().normalize();

            Element tilesRoot = tilesDocument.getDocumentElement();

            NodeList elements = tilesRoot.getElementsByTagName("tile");

            for (int index = 0; index < elements.getLength(); ++index) {
                Element tileElement = (Element) elements.item(index);
                Element imageElement = (Element) tileElement.getFirstChild().getNextSibling();

                String tileSource = imageElement.getAttribute("source").replace("..", "src/ResourcesFiles");
                String tileId = Integer.toString(Integer.parseInt(tileElement.getAttribute("id")) + 1);
                int tileWidth = Integer.parseInt(imageElement.getAttribute("width"));
                int tileHeight = Integer.parseInt(imageElement.getAttribute("height"));

                tiles.put(tileId, new MapAsset(tileSource, tileWidth, tileHeight));
            }

            /*
             * load the background
             */
            background = new ParallaxWallpaper();

            Element backGroundSource = (Element) root.getElementsByTagName("tileset").item(1);
            Document backgroundDocument = builder.parse(new File(backGroundSource.getAttribute("source").replace("..", "src/ResourcesFiles")));
            document.getDocumentElement().normalize();

            Element backgroundRoot = backgroundDocument.getDocumentElement();

            NodeList backgrounds = backgroundRoot.getElementsByTagName("tile");

            for (int index = 0; index < backgrounds.getLength(); ++index) {
                Element tileElement = (Element) backgrounds.item(index);
                Element imageElement = (Element) tileElement.getFirstChild().getNextSibling();
                background.addImage(ImageIO.read(new File(imageElement.getAttribute("source").replace("../", "src/ResourcesFiles/"))));
            }

            /*
                load the objects index
             */

            objects = new HashMap<>();

            Element objSource = (Element) root.getElementsByTagName("tileset").item(2);

            String lastMapId = objSource.getAttribute("firstgid");

            Document objectsDocument = builder.parse(new File(objSource.getAttribute("source").replace("..", "src/ResourcesFiles")));

            Element objectsRoot = objectsDocument.getDocumentElement();

            NodeList objectsElements = objectsRoot.getElementsByTagName("tile");

            for (int index = 0; index < objectsElements.getLength(); ++index) {
                Element objectElement = (Element) objectsElements.item(index);
                Element imageElement = (Element) objectElement.getFirstChild().getNextSibling();

                String objectSource = imageElement.getAttribute("source").replace("..", "src/ResourcesFiles");
                String objectId = Integer.toString(Integer.parseInt(objectElement.getAttribute("id")) + Integer.parseInt(lastMapId));
                int objectWidth = Integer.parseInt(imageElement.getAttribute("width"));
                int objectHeight = Integer.parseInt(imageElement.getAttribute("height"));

                objects.put(objectId,  new MapAsset(objectSource, objectWidth, objectHeight));
            }

            /*
             * load the matrix tiles matrix
             */

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

            /*
                load the objects indexes
             */
            String objectsBuffer = root.getElementsByTagName("data").item(1).
                    getFirstChild().getTextContent();

            String[] objectsRows = objectsBuffer.split("\n"); // split the string into rows

            objectsIndexes = new String[height][width];

            for (int i = 0; i < height; i++) {
                String[] objectsCols = objectsRows[i + 1].split(",");
                System.arraycopy(objectsCols, 0, objectsIndexes[i], 0, width);
            }
            System.out.println();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void update() throws Exception {
        background.update();

        /**
         * update the map objects
         * -> save the camera offset
         */
    }

    @Override
    public void draw() {
        /**
         * drawing the parralax background
         */
        background.draw();

        /**
         * drawing the tiles
         */
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; j++) {
                if (!Objects.equals(tilesIndexes[i][j], "0")) {
                    MapAsset asset = tiles.get(tilesIndexes[i][j]);
                    int xPos = (int) (j * (mapDim * mapScale) + Camera.getInstance().getCurrentXoffset());
                    int yPos = (int) (i * mapDim * mapScale);
                    int dimW = (int)(asset.getWidth()*mapScale);
                    int dimH = (int)(asset.getHeight()*mapScale);

                    gameWindow.getGraphics().drawImage(asset.getImage(), xPos, yPos, dimW, dimH, null);
                }
            }
        }

        /**
         * drawing the objects
         */
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; j++) {
                if (!Objects.equals(objectsIndexes[i][j], "0")) {
                    MapAsset asset = objects.get(objectsIndexes[i][j]);
                    int dimW = (int)(asset.getWidth()*mapScale);
                    int dimH = (int)(asset.getHeight()*mapScale);
                    int xPos = (int) (j * (mapDim * mapScale) + Camera.getInstance().getCurrentXoffset());
                    int yPos = (int) (i * mapDim * mapScale) - dimH +  (int)(mapDim * mapScale);

                    gameWindow.getGraphics().drawImage(asset.getImage(), xPos, yPos, dimW, dimH, null);
                }
            }
        }
    }
}
