package Components.GameComponents.Map;

import Components.BaseComponents.AssetsDeposit;
import Components.GameComponents.DynamicComponent;
import Components.Notifiable;
import Database.Database;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.InGame.PlayScene;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;
import Utils.Rectangle;
import Window.Camera;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

import static Utils.Constants.*;

/**
 * This class contains all the information about the game map:
 * matrix of objects|tiles, dimensions, predefined object positions.
 */
public class GameMap extends DynamicComponent {
    private int width; // lines
    private int height; // columns
    transient private Map<String, MapAsset> tiles; // String - id , Tile
    transient private Map<String, MapAsset> objects; // String - id , Object
    private String[][] tilesIndexes; // indexes for tiles
    private String[][] objectsIndexes; // indexes for objects
    private String[][] decorsIndexes; // indexes for objects
    transient private ParallaxWallpaper background; // parallax ba    private String[][] tilesIndexes; // indexes for tilesckground
    private Map<String, List<Rectangle>> entitiesCoordinates; // map of the preloaded entities
    ComponentType mapType;

    public GameMap(Scene scene, ComponentType mapType) {
        this.scene = scene;
        this.mapType = mapType;
        String path = "";
        switch (mapType) {
            case GREEN_CITY ->
                    path = Objects.requireNonNull(Database.class.getClassLoader().getResource("Resources/maps/green_map.tmx")).getPath();
            case INDUSTRIAL_CITY ->
                    path = Objects.requireNonNull(Database.class.getClassLoader().getResource("Resources/maps/industrial_map.tmx")).getPath();
            case POWER_STATION ->
                    path = Objects.requireNonNull(Database.class.getClassLoader().getResource("Resources/maps/power_map.tmx")).getPath();
        }
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
            String source1 = source.getAttribute("source").replace("..", "Resources");
            source1 = Objects.requireNonNull(Database.class.getClassLoader().getResource(source1)).getPath();
            Document tilesDocument = builder.parse(new File(source1));
            Element tilesRoot = tilesDocument.getDocumentElement();
            NodeList elements = tilesRoot.getElementsByTagName("tile");

            //   then for each element map asset(tile) is created
            //   and added on the tiles map
            for (int index = 0; index < elements.getLength(); ++index) {
                Element tileElement = (Element) elements.item(index);
                Element imageElement = (Element) tileElement.getFirstChild().getNextSibling();

                String tileSource = imageElement.getAttribute("source").replace("..", "Resources");
                tileSource = Objects.requireNonNull(Database.class.getClassLoader().getResource(tileSource)).getPath().replace("%20", " ");
                String tileId = Integer.toString(Integer.parseInt(tileElement.getAttribute("id")) + 1);
                int tileWidth = (int) (Integer.parseInt(imageElement.getAttribute("width")) * MAP_SCALE);
                int tileHeight = (int) (Integer.parseInt(imageElement.getAttribute("height")) * MAP_SCALE);

                tiles.put(tileId, new MapAsset(tileSource, tileWidth, tileHeight));
            }

            // -------------------------------------
            // load the parallax background
            // and all the necessary information
            background = new ParallaxWallpaper();

            Element backGroundSource = (Element) root.getElementsByTagName("tileset").item(1);
            source1 = backGroundSource.getAttribute("source").replace("..", "Resources");
            source1 = Objects.requireNonNull(Database.class.getClassLoader().getResource(source1)).getPath();
            Document backgroundDocument = builder.parse(new File(source1));
            document.getDocumentElement().normalize();

            Element backgroundRoot = backgroundDocument.getDocumentElement();

            NodeList backgrounds = backgroundRoot.getElementsByTagName("tile");

            //   then for each element an image
            //   is added in the background object
            for (int index = 0; index < backgrounds.getLength(); ++index) {
                Element tileElement = (Element) backgrounds.item(index);
                Element imageElement = (Element) tileElement.getFirstChild().getNextSibling();
                source1 = imageElement.getAttribute("source").replace("..", "Resources");
                source1 = Objects.requireNonNull(Database.class.getClassLoader().getResource(source1)).getPath().replace("%20", " ");
                background.addImage(ImageIO.read(new File(source1)));
            }

            // -------------------------------------
            // load the objects by reading all
            // the necessary information

            objects = new HashMap<>();

            Element objSource = (Element) root.getElementsByTagName("tileset").item(2);
            String lastMapId = objSource.getAttribute("firstgid");
            source1 = objSource.getAttribute("source").replace("..", "Resources");
            source1 = Objects.requireNonNull(Database.class.getClassLoader().getResource(source1)).getPath();
            Document objectsDocument = builder.parse(new File(source1));
            Element objectsRoot = objectsDocument.getDocumentElement();
            NodeList objectsElements = objectsRoot.getElementsByTagName("tile");

            // for each object element read the information
            // necessary and add the object in the dedicated map
            for (int index = 0; index < objectsElements.getLength(); ++index) {
                Element objectElement = (Element) objectsElements.item(index);
                Element imageElement = (Element) objectElement.getFirstChild().getNextSibling();

                String objectSource = imageElement.getAttribute("source").replace("..", "Resources");
                objectSource = Objects.requireNonNull(Database.class.getClassLoader().getResource(objectSource)).getPath().replace("%20", " ");
                String objectId = Integer.toString(Integer.parseInt(objectElement.getAttribute("id")) + Integer.parseInt(lastMapId));
                int objectWidth = (int) (Integer.parseInt(imageElement.getAttribute("width")) * MAP_SCALE);
                int objectHeight = (int) (Integer.parseInt(imageElement.getAttribute("height")) * MAP_SCALE);

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
            // second layer(object layer) matrix of indexes
            String objectsBuffer = root.getElementsByTagName("data").item(1).
                    getFirstChild().getTextContent();

            String[] objectsRows = objectsBuffer.split("\n"); // split the string into rows

            objectsIndexes = new String[height][width];

            for (int i = 0; i < height; i++) {
                String[] objectsCols = objectsRows[i + 1].split(",");
                System.arraycopy(objectsCols, 0, objectsIndexes[i], 0, width);
            }

            // -------------------------------------
            // it's the time to load the
            // third layer(object layer) matrix of indexes
            // ->>>>hereeeee
            String decorBuffer = root.getElementsByTagName("data").item(2).
                    getFirstChild().getTextContent();

            String[] decorsRows = decorBuffer.split("\n"); // split the string into rows

            decorsIndexes = new String[height][width];

            for (int i = 0; i < height; i++) {
                String[] decorsCols = decorsRows[i + 1].split(",");
                System.arraycopy(decorsCols, 0, decorsIndexes[i], 0, width);
            }

            // -------------------------------------
            // few more code and the map is finally added
            // now load the predefined positions

            entitiesCoordinates = new HashMap<>();
            NodeList positions = root.getElementsByTagName("objectgroup");

            for (int index = 0; index < positions.getLength(); ++index) {
                Node objectgroupNode = positions.item(index);

                Element objectgroupElement = (Element) objectgroupNode;
                String objectgroupName = objectgroupElement.getAttribute("name");
                NodeList objectList = objectgroupElement.getElementsByTagName("object");
                List<Rectangle> list = new ArrayList<>();

                for (int objectIndex = 0; objectIndex < objectList.getLength(); ++objectIndex) {
                    Node objectNode = objectList.item(objectIndex);
                    Element objectElement = (Element) objectNode;
                    int x = (int) (Float.parseFloat(objectElement.getAttributeNode("x").getValue()) * MAP_SCALE);
                    int y = (int) (Float.parseFloat(objectElement.getAttributeNode("y").getValue()) * MAP_SCALE);
                    int w = (int) (Float.parseFloat(objectElement.getAttributeNode("width").getValue()) * MAP_SCALE);
                    int h = (int) (Float.parseFloat(objectElement.getAttributeNode("height").getValue()) * MAP_SCALE);
                    list.add(new Rectangle(new Coordinate<>(x, y), w, h));

                    entitiesCoordinates.put(objectgroupName, list);
                }

                // for an efficient drawing we must construct the mapAssets objects
                // TODO
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void setScene(Scene scene) {
        super.setScene(scene);
        Camera.get().setGameMapPixelWidthDimension(this.width * MAP_DIM);
        Camera.get().setGameMapPixelHeightDimension(this.height * MAP_DIM);
    }

    @Override
    public void notify(Message message) {
    }

    @Override
    public void update() {
        background.update();
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        /*
         * drawing the parallax background
         */
        background.draw(graphics2D);

        /*
         * drawing the tiles
         */
        drawLayer(graphics2D, tilesIndexes, tiles);

        /*
         * drawing the objects
         */
        drawLayer(graphics2D, objectsIndexes, objects);

        /*
         * drawing decorations
         */
        drawLayer(graphics2D, decorsIndexes, objects);

    }

    @Override
    public ComponentType getCurrentType() {
        return mapType;
    }

    private void drawLayer(Graphics2D graphics2D, String[][] decorsIndexes, Map<String, MapAsset> types) {
        int xStart = Math.max(0, -Camera.get().getCurrentHorizontalOffset() / MAP_DIM - 4);
        int xEnd = Math.min(width - 1, (-Camera.get().getCurrentHorizontalOffset() + WINDOW_WIDTH) / MAP_DIM + 4);
        int yStart = Math.max(0, -Camera.get().getCurrentVerticalOffset() / MAP_DIM - 4);
        int yEnd = Math.min(height - 1, (-Camera.get().getCurrentVerticalOffset() + WINDOW_HEIGHT) / MAP_DIM + 4);

        for (int i = yStart; i <= yEnd; i++) {
            for (int j = xStart; j <= xEnd; j++) {
                if (!Objects.equals(decorsIndexes[i][j], "0")) {
                    MapAsset asset = types.get(decorsIndexes[i][j]);
                    int dimW = asset.getWidth();
                    int dimH = asset.getHeight();
                    int xPos = j * MAP_DIM + Camera.get().getCurrentHorizontalOffset();
                    int yPos = i * MAP_DIM - dimH + MAP_DIM + Camera.get().getCurrentVerticalOffset();

                    graphics2D.drawImage(asset.getImage(), xPos, yPos, dimW, dimH, null);
                }
            }
        }
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.MAP;
    }

    @Override
    public void addMissingPartsAfterDeserialization(Notifiable scene) {
        super.addMissingPartsAfterDeserialization(scene);
        this.tiles = AssetsDeposit.get().getGameMap(mapType).tiles;
        this.objects = AssetsDeposit.get().getGameMap(mapType).objects;
        Camera.get().setGameMapPixelWidthDimension(width * MAP_DIM);
        Camera.get().setGameMapPixelHeightDimension(height * MAP_DIM);
        this.background = AssetsDeposit.get().getGameMap(mapType).background;
    }

    /**
     * In this context, a map handles the interaction with any movable object
     *
     * @param object interacts with
     */
    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        Rectangle rectangle = component.getCollideBox();
        Rectangle rectangle1 = new Rectangle(rectangle);
        rectangle1.moveByY(1);

        // if the message is from player
        if (component.getGeneralType() == ComponentType.PLAYER) {
            // => check if is on a ladder
            try {
                for (Rectangle ladder : entitiesCoordinates.get("ladders")) {
                    if (rectangle.intersects(ladder)) {
                        component.notify(new Message(MessageType.IS_ON_LADDER, ComponentType.MAP, getId()));
                        return;
                    }
                }
                component.notify(new Message(MessageType.IS_NO_LONGER_ON_LADDER, ComponentType.MAP, getId()));
                // => check if is on a platform
                                /*
                        the explanation is quite funny
                     */
                for (DynamicComponent platform : ((PlayScene) scene).getAllComponentsWithName(ComponentType.PLATFORM)) {
                    if (rectangle1.intersects(platform.getCollideBox())) {
                        if (rectangle1.getDy() < 0) {
                            component.notify(new Message(MessageType.ACTIVATE_BOTTOM_COLLISION, ComponentType.MAP, getId()));
                            platform.interactionWith(component);
                        } else if (rectangle.getDy() > 0) {
                            component.notify(new Message(MessageType.ACTIVATE_TOP_COLLISION, ComponentType.MAP, getId()));
                        }
                        rectangle.solveCollision(platform.getCollideBox());
                        return;
                    }
                }
            }catch (NullPointerException ignored){}
        }

        // if the message is from bullet => check if it has a collision
        if (component.getGeneralType() == ComponentType.BULLET) {
            int x = component.getCollideBox().getMinX() / MAP_DIM;
            int y = component.getCollideBox().getCenterY() / MAP_DIM;
            if (x <= 0 || x > width - 1 || !Objects.equals(tilesIndexes[y][x], "0")) {
                component.notify(new Message(MessageType.HAS_COLLISION, ComponentType.MAP, getId()));
            }
            return;
        }

        // the message is from a component that has map collision and needs recalibraion
        int xStart = Math.max(0, rectangle.getPosition().getPosX() / MAP_DIM - 5);
        int xEnd = Math.min(width, (rectangle.getPosition().getPosX() + rectangle.getWidth()) / MAP_DIM + 2);
        int yStart = Math.max(0, rectangle.getPosition().getPosY() / MAP_DIM - 1);
        int yEnd = Math.min(height, (rectangle.getPosition().getPosY() + rectangle.getHeight()) / MAP_DIM + 1);

        // variables for special cases collision detection
        boolean wasGroundCollision = false;
        boolean wasTopCollision = false;
        boolean wasLeftCollision = false;
        boolean wasRightCollision = false;

        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                if (!Objects.equals(tilesIndexes[y][x], "0")) {
                    Rectangle tileRect = getRectangle(x, y);

                    /*
                        the explanation is quite funny
                     */
                    rectangle1.intersects(tileRect);
                    if (rectangle1.getDy() < 0) {
                        wasGroundCollision = true;
                    }
                    // end of the magic code

                    // solve the collision and save the offsets
                    rectangle.solveCollision(tileRect);
                    if (rectangle.getDy() > 0) {
                        wasTopCollision = true;
                    }

                    // determine left_right wall collision
                    if (rectangle.getDx() < 0) {
                        wasRightCollision = true;
                    } else if (rectangle.getDx() > 0) {
                        wasLeftCollision = true;
                    }
                }
            }
        }

        // now let's verify the borders with the world
        if (rectangle.getMinY() < 0) {
            wasTopCollision = true;
        } else if (rectangle.getMinX() < 0) {
            wasLeftCollision = true;
        }

        if (component.getGeneralType() != ComponentType.PLATFORM && component.getCurrentType() != ComponentType.AIRPLANE) {
            // notify the component
            if (wasGroundCollision) {
                component.notify(new Message(MessageType.ACTIVATE_BOTTOM_COLLISION, ComponentType.MAP, getId()));
            } else {
                component.notify(new Message(MessageType.DEACTIVATE_BOTTOM_COLLISION, ComponentType.MAP, getId()));
            }
            if (wasTopCollision) {
                component.notify(new Message(MessageType.ACTIVATE_TOP_COLLISION, ComponentType.MAP, getId()));
            }
        }

        // particular behavior for some components
        if (component.getGeneralType() != ComponentType.PLAYER) {
            if (component.getGeneralType() == ComponentType.GROUND_ENEMY) {
                // collision verification is necessary to prevent components from falling off the platform
                if (Objects.equals(tilesIndexes[rectangle.getCenterY() / MAP_DIM + 1][rectangle.getMaxX() / MAP_DIM - 1], "0")) {
                    wasLeftCollision = true;
                } else if (Objects.equals(tilesIndexes[rectangle.getCenterY() / MAP_DIM + 1][rectangle.getMinX() / MAP_DIM + 1], "0")) {
                    wasRightCollision = true;
                }
            }

            // notify the component
            if (component.getCurrentType() != ComponentType.DRONE_ENEMY) {
                if (wasLeftCollision) {
                    component.notify(new Message(MessageType.LEFT_COLLISION, ComponentType.MAP, getId()));
                } else if (wasRightCollision) {
                    component.notify(new Message(MessageType.RIGHT_COLLISION, ComponentType.MAP, getId()));
                }
            }
        }
    }

    /**
     * @param x map relative X coordinate
     * @param y map relative Y coordinate
     * @return rectangle object
     */
    public Rectangle getRectangle(int x, int y) {
        Coordinate<Integer> pos = new Coordinate<>(x * MAP_DIM, y * MAP_DIM);
        return new Rectangle(pos, MAP_DIM, MAP_DIM);
    }

    public List<Coordinate<Integer>> getPositionForEntities(ComponentType componentType) {
        String componentName = "";
        switch (componentType) {
            case PLAYER -> componentName = "player";
            case GROUND_ENEMY -> componentName = "enemies";
            case ANIMAL -> componentName = "animals";
            case CHEST -> componentName = "chests";
            case PLATFORM -> componentName = "platforms";
            case HELICOPTER -> componentName = "helicopters";
            case AIRPLANE -> componentName = "airplanes";
            case DRONE_ENEMY -> componentName = "drones";
        }
        List<Coordinate<Integer>> coordinates = new ArrayList<>();
        try {
            for (Rectangle rectangle : entitiesCoordinates.get(componentName)) {
                coordinates.add(rectangle.getPosition());
            }
        }catch (NullPointerException e){
            System.out.println("Entity : " + componentName + " not found");
        }
        return coordinates;
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
