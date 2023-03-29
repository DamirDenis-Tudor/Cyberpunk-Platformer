package Components.StaticComponents;

import Components.StaticComponents.Components.Animation;
import Components.DinamicComponents.Map.GameMap;
import Enums.AnimationNames;
import Enums.MapNames;
import Utils.Coordinate;
import Utils.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

import static Enums.MapNames.*;
import static Utils.Constants.mapScale;

import java.util.Map;

/**
 * Here will load predifined maps , animations , and so on
 */
public class AssetsDeposit {
    private static AssetsDeposit instance = null;
    private final Map<MapNames, GameMap> gameMaps;
    private final Map <AnimationNames, Animation> animations;
    private AssetsDeposit() throws Exception {

        // -----------------------load game maps
        gameMaps = new HashMap<>();
        gameMaps.put(GreenCityMap , new GameMap("src/Resources/maps/green_map.tmx"));
        gameMaps.put(IndustrialCity , new GameMap("src/Resources/maps/industrial_map.tmx"));

        // -----------------------load game animations
        animations = new HashMap<>();

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        Document document = builder.parse(new File("src/Resources/in_game_assets/animations.tsx"));

        Element root = document.getDocumentElement();

        NodeList elements = root.getElementsByTagName("tile");

        for (int index = 0; index < elements.getLength(); ++index) {
            Element element = (Element) elements.item(index);
            Element property = (Element) element.getElementsByTagName("property").item(0);
            Element imageElement = (Element) element.getElementsByTagName("image").item(0);

            String source = imageElement.getAttribute("source").replace("..", "src/Resources");
            String id = element.getAttribute("class");
            int spriteSheetWidth = Integer.parseInt(imageElement.getAttribute("width"));
            int height = Integer.parseInt(imageElement.getAttribute("height"));
            int width = Integer.parseInt(property.getAttribute("value"));

            Element box = (Element) element.getElementsByTagName("objectgroup").item(0).getFirstChild().getNextSibling();
            int x = (int)Float.parseFloat(box.getAttributeNode("x").getValue());
            int y = (int)Float.parseFloat(box.getAttributeNode("y").getValue());
            int boxHeight = (int) (Float.parseFloat(box.getAttributeNode("height").getValue())*mapScale);
            int boxWidth = (int)(Float.parseFloat(box.getAttributeNode("width").getValue())*mapScale);
            Rectangle boxBounding = new Rectangle(new Coordinate<>(x,y) , boxWidth, boxHeight);
            animations.put(AnimationNames.valueOf(id), new Animation(source , spriteSheetWidth , width ,height , boxBounding,AnimationNames.valueOf(id) ));
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
    public GameMap getGameMap(MapNames name){
        return gameMaps.get(name);
    }

    public Animation getAnimation(AnimationNames name){
        return animations.get(name);
    }
}
