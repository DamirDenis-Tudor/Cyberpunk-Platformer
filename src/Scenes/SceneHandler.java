package Scenes;

import Scenes.InGame.LevelCompletedScene;
import Scenes.InGame.LevelFailedScene;
import Scenes.InGame.LevelPauseScene;
import Scenes.InGame.PlayScene;
import Scenes.InMenu.InitGameScene;
import Scenes.InMenu.LogoStartScene;
import Scenes.InMenu.MainMenuScene;
import Scenes.InMenu.SettingsScene;
import Enums.SceneNames;

import static Enums.SceneNames.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for handling of multiple scenes.
 */
public class SceneHandler {
    static SceneHandler instance;
    private final Map<SceneNames, Scene> scenes;
    private Scene activeScene;

    private SceneHandler() throws Exception {
        scenes = new HashMap<>();
        scenes.put(LogoStartScene          , new LogoStartScene());
        scenes.put(MainMenuScene           , new MainMenuScene());
        scenes.put(InitGameScene           , new InitGameScene());
        scenes.put(SettingsScene           , new SettingsScene());
        scenes.put(PlayScene               , new PlayScene());
        scenes.put(LevelPausedScene        , new LevelPauseScene());
        scenes.put(LevelFailedScene        , new LevelFailedScene());
        scenes.put(LevelCompletedScene     , new LevelCompletedScene());

        activeScene = scenes.get(PlayScene);
    }

    public static SceneHandler getInstance() throws Exception {
        if(instance == null){
            instance = new SceneHandler();
        }
        return instance;
    }

    /**
     * @return actual active scene
     * @throws Exception message
     */
    public Scene getActiveScene() throws Exception {
        if(activeScene == null) {
            throw new Exception("Error : active scene is null!");
        }
        return activeScene;
    }

    /**
     * This method iterates the map until a scene is equal
     * (has the same address with the active one.
     * @return identifier of active scene
     * @throws Exception message
     */
    public SceneNames getActiveSceneID() throws Exception {
        if (activeScene == null){
            throw new NullPointerException("Error - active scene is null.");
        }

        for (Map.Entry<SceneNames, Scene> entry: scenes.entrySet()) {
            if (entry.getValue() == activeScene){
                return entry.getKey();
            }
        }
        throw new Exception("Error - identifier not found for active scene.");
    }

    /**
     * This method handles the scene change request .
     * It can change the active state if the new scene not
     * belong to the current active state.
     * @param newScene
     */
    public void handleSceneChangeRequest(SceneNames newScene) throws Exception {
        if(scenes.containsKey(newScene)){
            activeScene = scenes.get(newScene);
        }else {
            throw new Exception("ID SCENA INVALOD");
        }

    }
}
