package Components.BaseComponents;

import Components.GameItems.GameItems.Bullet;
import Components.GameItems.GameItems.Gun;
import Components.GameItems.Map.GameMap;
import Enums.AnimationType;
import Enums.ComponentType;
import Utils.Coordinate;
import Utils.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static Utils.Constants.mapScale;

/**
 * This class has predefined maps, animations and so on.
 */
public class AssetsDeposit {
    private static AssetsDeposit instance = null;
    private final Map<ComponentType, GameMap> gameMaps;
    private final Map <AnimationType, Animation> animations;
    private final Map<ComponentType , Gun> guns;
    private final Map<ComponentType , Bullet> bullets;
    private final Map<ComponentType , ComponentType> gunsBulletsRelation;

    /**
     * this constructor loads all the assets.
     */
    private AssetsDeposit() {
        gameMaps = new HashMap<>();
        animations = new HashMap<>();
        gunsBulletsRelation = new HashMap<>();
        guns = new HashMap<>();
        bullets = new HashMap<>();
        try {
            // -----------------------load game maps
            gameMaps.put(ComponentType.GreenCity , new GameMap(null,ComponentType.GreenCity));

            // -----------------------load game animations
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
                int x = (int) Float.parseFloat(box.getAttributeNode("x").getValue());
                int y = (int) Float.parseFloat(box.getAttributeNode("y").getValue());
                int boxHeight = (int) (Float.parseFloat(box.getAttributeNode("height").getValue()) * mapScale);
                int boxWidth = (int) (Float.parseFloat(box.getAttributeNode("width").getValue()) * mapScale);
                Rectangle boxBounding = new Rectangle(new Coordinate<>(x, y), boxWidth, boxHeight);
                animations.put(AnimationType.valueOf(id), new Animation(source, spriteSheetWidth, width, height, boxBounding, AnimationType.valueOf(id)));
            }

            // -----------------------load game ComponentTypes
            // first of all let's make a mapping that describes
            // the bullet-gun relationship

            gunsBulletsRelation.put(ComponentType.Gun1, ComponentType.Bullet1);
            gunsBulletsRelation.put(ComponentType.Gun2, ComponentType.Bullet2);
            gunsBulletsRelation.put(ComponentType.Gun3, ComponentType.Bullet3);
            gunsBulletsRelation.put(ComponentType.Gun4, ComponentType.Bullet4);
            gunsBulletsRelation.put(ComponentType.Gun5, ComponentType.Bullet5);
            gunsBulletsRelation.put(ComponentType.Gun6, ComponentType.Bullet6);
            gunsBulletsRelation.put(ComponentType.Gun7, ComponentType.Bullet7);
            gunsBulletsRelation.put(ComponentType.Gun8, ComponentType.Bullet8);
            gunsBulletsRelation.put(ComponentType.Gun9, ComponentType.Bullet9);
            gunsBulletsRelation.put(ComponentType.Gun10, ComponentType.Bullet10);



            document = builder.parse(new File("src/Resources/in_game_assets/weapons&buletts.tsx"));
            root = document.getDocumentElement();
            elements = root.getElementsByTagName("tile");

            for (int index = 0; index < elements.getLength(); ++index) {
                Element element = (Element) elements.item(index);
                Element imageElement = (Element) element.getElementsByTagName("image").item(0);

                String source = imageElement.getAttribute("source").replace("..", "src/Resources");
                String id = element.getAttribute("class");
                int width = Integer.parseInt(imageElement.getAttribute("width"));
                int height = Integer.parseInt(imageElement.getAttribute("height"));

                if (id.contains("Gun")) {
                    Rectangle boxBounding = new Rectangle(new Coordinate<>(0, 0), (int) (width), (int) (height));
                    guns.put(ComponentType.valueOf(id), new Gun(ImageIO.read(new File(source)), boxBounding));
                } else {
                    Rectangle boxBounding = new Rectangle(new Coordinate<>(0, 0), (int) (width * 2), (int) (height * 2));
                    bullets.put(ComponentType.valueOf(id), new Bullet(ImageIO.read(new File(source)), boxBounding));
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage() + "->" + e.getLocalizedMessage());
        }
    }

    public static AssetsDeposit get(){
        if (instance == null) {
            instance = new AssetsDeposit();
        }
        return instance;
    }

    public void addGameMap(GameMap map){
        if(!gameMaps.containsKey(map.getCurrentType())){
           gameMaps.put(map.getCurrentType() , map);
        }
    }
    public GameMap getGameMap(ComponentType name){
        return gameMaps.get(name);
    }

    public Animation getAnimation(AnimationType name){
        return animations.get(name);
    }

    /**
     * @param name bun type
     * @return Gun object
     */
    public Gun getGun(ComponentType name){
        return guns.get(name);
    }

    /**
     * @param name bullet type
     * @return Bullet object
     */
    public Bullet getBulletByGunName(ComponentType name){return bullets.get(gunsBulletsRelation.get(name));}

}
