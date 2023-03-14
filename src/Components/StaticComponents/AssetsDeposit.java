package Components.StaticComponents;

import Components.StaticComponents.Components.GameMap;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;

/**
 * Here will load predifined maps , animations , and so on
 */
public class AssetsDeposit {
    private static AssetsDeposit instance = null;

    private final Map< String , GameMap> gameMaps;
    private AssetsDeposit() throws IOException {
        gameMaps = new HashMap<>();
        gameMaps.put("GreenCity" , new GameMap("src/ResourcesFiles/maps/green_map.tmx"));
        gameMaps.put("IndustrialCity" , new GameMap("src/ResourcesFiles/maps/industrial_map.tmx"));
        System.out.println();
    }

    /**
     * @return
     */
    public static AssetsDeposit getInstance() throws IOException {
        if (instance == null) {
            instance = new AssetsDeposit();
        }
        return instance;
    }
    public GameMap getGameMap(String name){
        return gameMaps.get(name);
    }
}
