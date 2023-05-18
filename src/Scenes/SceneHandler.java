package Scenes;

import Components.Notifiable;
import Enums.ComponentType;
import Enums.MessageType;
import Enums.SceneType;
import Scenes.InGame.LevelCompletedScene;
import Scenes.InGame.LevelFailedScene;
import Scenes.InGame.LevelPauseScene;
import Scenes.InGame.PlayScene;
import Scenes.InMenu.*;
import Scenes.Messages.Message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static Enums.SceneType.*;

/**
 * This class is responsible for the handling of multiple scenes.
 */
public class SceneHandler implements Notifiable, Serializable {
    static SceneHandler instance;
    private final Map<SceneType, Scene> scenes;
    private Scene activeScene;

    private SceneHandler() throws Exception {
        scenes = new HashMap<>();
        scenes.put(LOGO_START_SCENE, new LogoStartScene(this));
        scenes.put(MAIN_MENU_SCENE, new MainMenuScene(this));
        scenes.put(CHOOSE_PLAYER_SCENE, new ChoosePlayerScene(this));
        scenes.put(CHOOSE_LEVEL_SCENE, new ChooseLevelScene(this));
        scenes.put(SETTINGS_SCENE, new SettingsScene(this));
        scenes.put(PLAY_SCENE, new PlayScene(this));
        scenes.put(LOAD_SCENE, new LoadScene(this));
        scenes.put(LEVEL_PAUSED_SCENE, new LevelPauseScene(this));
        scenes.put(LEVEL_FAILED_SCENE, new LevelFailedScene(this));
        scenes.put(LEVEL_COMPLETED_SCENE, new LevelCompletedScene(this));

        handleSceneChangeRequest(MAIN_MENU_SCENE);
    }

    public static SceneHandler getInstance() throws Exception {
        if (instance == null) {
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
     *
     * @param newScene scene to be activated
     */
    public void handleSceneChangeRequest(SceneType newScene) {
        if (scenes.containsKey(newScene)) {
            activeScene = scenes.get(newScene);
            activeScene.notify(new Message(MessageType.SCENE_HAS_BEEN_ACTIVATED, ComponentType.SCENE_HANDLER, -1));
        } else {
            activeScene = null;
        }
    }

    @Override
    public void notify(Message message) {
        switch (message.type()){
            case NEW_GAME,LOAD_GAME, GREEN_MAP_SELECTED,INDUSTRIAL_MAP_SELECTED,WEAPON_IS_SELECTED -> scenes.get(PLAY_SCENE).notify(message);
            case BIKER_SELECTED, PUNK_SELECTED, CYBORG_SELECTED -> {
                scenes.get(PLAY_SCENE).notify(message);
                scenes.get(LEVEL_PAUSED_SCENE).notify(message);
            }
            case SAVE_GAME -> {
                scenes.get(PLAY_SCENE).notify(message);
                scenes.get(LOAD_SCENE).notify(message);
            }
            case IS_PICKED_UP -> scenes.get(LEVEL_PAUSED_SCENE).notify(message);
        }
    }
}
