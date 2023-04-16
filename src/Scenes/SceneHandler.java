package Scenes;

import Components.Notifiable;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.InGame.LevelCompletedScene;
import Scenes.InGame.LevelFailedScene;
import Scenes.InGame.LevelPauseScene;
import Scenes.InGame.PlayScene;
import Scenes.InMenu.InitGameScene;
import Scenes.InMenu.LogoStartScene;
import Scenes.InMenu.MainMenuScene;
import Scenes.InMenu.SettingsScene;
import Scenes.InMenu.LoadScene;
import Enums.SceneType;
import Scenes.Messages.Message;

import static Enums.SceneType.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for the handling of multiple scenes.
 */
public class SceneHandler{
    static SceneHandler instance;
    private final Map<SceneType, Scene> scenes;
    private Scene activeScene;
    private SceneHandler() throws Exception {
        scenes = new HashMap<>();
        scenes.put(LogoStartScene          , new LogoStartScene(this));
        scenes.put(MainMenuScene           , new MainMenuScene(this));
        scenes.put(InitGameScene           , new InitGameScene(this));
        scenes.put(SettingsScene           , new SettingsScene(this));
        scenes.put(PlayScene               , new PlayScene(this));
        scenes.put(LoadScene               , new LoadScene(this));
        scenes.put(LevelPausedScene        , new LevelPauseScene(this));
        scenes.put(LevelFailedScene        , new LevelFailedScene(this));
        scenes.put(LevelCompletedScene     , new LevelCompletedScene(this));

        handleSceneChangeRequest(MainMenuScene);
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
        return activeScene;
    }

    /**
     * This method handles the scene change request.
     * It can change the active state if the new scene does not belong
     * to the current active state.
     * @param newScene scene to be activated
     */
    public void handleSceneChangeRequest(SceneType newScene) throws Exception {
        if(scenes.containsKey(newScene)){
            activeScene = scenes.get(newScene);
            activeScene.notify(new Message(MessageType.SceneHasBeenActivated , ComponentType.SceneHandler , -1));
        }else {
            activeScene = null;
        }
    }
}
