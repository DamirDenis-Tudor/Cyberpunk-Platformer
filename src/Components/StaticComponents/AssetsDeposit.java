package Components.StaticComponents;

import Components.StaticComponents.Components.Animation;
import Components.StaticComponents.Components.GameMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import java.util.Map;

/**
 * Here will load predifined maps , animations , and so on
 */
public class AssetsDeposit {
    private static AssetsDeposit instance = null;

    private final Map< String , GameMap> gameMaps;

    private final Map <String , Animation> animations;
    private AssetsDeposit() throws Exception {

        // load game maps
        gameMaps = new HashMap<>();
        gameMaps.put("GreenCity" , new GameMap("src/ResourcesFiles/maps/green_map.tmx"));
        gameMaps.put("IndustrialCity" , new GameMap("src/ResourcesFiles/maps/industrial_map.tmx"));

        // load game animations
        animations = new HashMap<>();

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        Document document = builder.parse(new File("src/ResourcesFiles/in_game_assets/animations.tsx"));

        Element root = document.getDocumentElement();

        NodeList elements = root.getElementsByTagName("tile");

        for (int index = 0; index < elements.getLength(); ++index) {
            Element element = (Element) elements.item(index);
            Element imageElement = (Element) element.getFirstChild().getNextSibling();

            String source = imageElement.getAttribute("source").replace("..", "src/ResourcesFiles");
            String id = element.getAttribute("class");
            int width = Integer.parseInt(imageElement.getAttribute("width"));
            int height = Integer.parseInt(imageElement.getAttribute("height"));

            animations.put(id , new Animation(source , width , height));
        }
        System.out.println();
    }

    /**
     * @return
     */
    public static AssetsDeposit getInstance() throws Exception {
        if (instance == null) {
            instance = new AssetsDeposit();
        }
        return instance;
    }
    public GameMap getGameMap(String name){
        return gameMaps.get(name);
    }

    public Animation getAnimation(String name){
        return animations.get(name);
    }
}
