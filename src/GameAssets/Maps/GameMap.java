package GameAssets.Maps;

import GameAssets.Asset;
import GameAssets.Types.Sprite;
import GameWindow.GameWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class GameMap implements Asset {
    private static final int tileDimension = 50;
    private static final int tileScale = 1;
    private final BufferedImage test;
    private Map<String, Sprite> tiles;
    private String[][] indexMap;
    private int width;
    private int height;


    public GameMap(String path) throws IOException {
        test = ImageIO.read(new File("src/Background.png"));
        loadMapIndexes(path);
    }

    private void loadTiles(String path) {
        try {
            tiles = new HashMap<>();

            DocumentBuilder builder = DocumentBuilderFactory
                    .newInstance().newDocumentBuilder();

            Document document = builder.parse(new File(path));
            document.getDocumentElement().normalize();

            Element root = document.getDocumentElement();

            NodeList elements = root.getElementsByTagName("tile");

            for (int index = 0; index < elements.getLength(); ++index) {
                Element tileElement = (Element) elements.item(index);
                Element imageElement = (Element) tileElement.getFirstChild().getNextSibling();

                tiles.put(
                        Integer.toString(Integer.parseInt(tileElement.getAttribute("id")) + 1),
                        new Sprite("src/ResourcesFiles/" + imageElement.getAttribute("source"))
                );
            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.exit(-1);
        }
        System.out.println();
    }

    private void createIndexesMatrix(String buffer) {
        ;

        String[] rows = buffer.split("\n"); // split the string into rows

        height = rows.length - 1;
        width = rows[1].split(",").length;

        indexMap = new String[height][width];

        for (int i = 0; i < height; i++) {
            String[] cols = rows[i + 1].split(",");
            for (int j = 0; j < width; j++) {
                indexMap[i][j] = cols[j];
            }
        }
    }

    private void loadMapIndexes(String path) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory
                    .newInstance().newDocumentBuilder();

            Document document = builder.parse(new File(path));
            document.getDocumentElement().normalize();

            Element root = document.getDocumentElement();

            Element source = (Element) root.getElementsByTagName("tileset").item(0);
            loadTiles("src/ResourcesFiles/" + source.getAttribute("source"));

            Element layer = (Element) root.getElementsByTagName("layer").item(0);
            height = Integer.parseInt(layer.getAttribute("height"));
            width = Integer.parseInt(layer.getAttribute("width"));

            String matrix = root.getElementsByTagName("data").item(0).
                    getFirstChild().getTextContent();

            createIndexesMatrix(matrix);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {
        GameWindow.getInstance().getGraphics().
                drawImage(test, 0, 0,
                        GameWindow.getInstance().GetWndWidth(),
                        GameWindow.getInstance().GetWndHeight(),
                        null);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; j++) {
                if (!Objects.equals(indexMap[i][j], "0")) {
                    GameWindow.getInstance().
                            getGraphics().
                            drawImage(
                                    tiles.get(indexMap[i][j]).getTile(),
                                    j * tileDimension * tileScale,
                                    i * tileDimension * tileScale,
                                    tileDimension * tileScale,
                                    tileDimension * tileScale,
                                    null);
                }
            }
        }
    }
}
