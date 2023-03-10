package GameAssets;

import GameAssets.Maps.GameMap;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;

/**
 * Here will load predifined maps , animations , and so on
 */
public class Assets {
    private static Assets instance = null;

    private final Map< String , GameMap> gameMaps;

    private Assets() throws IOException {
        gameMaps = new HashMap<>();
        // gameMaps.put("GreenCity" , new GameMap("src/ResourcesFiles/green_city_map.tmx"));
       // gameMaps.put("IndustrialCity" , new GameMap("src/ResourcesFiles/industrial_city_map.tmx"));
        System.out.println();
    }

    /**
     * @return
     */
    public static Assets getInstance() throws IOException {
        if (instance == null) {
            instance = new Assets();
        }
        return instance;
    }
    public GameMap getGameMap(String name){
        return gameMaps.get(name);
    }
}
