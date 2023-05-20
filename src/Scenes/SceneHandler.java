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

import java.util.HashMap;
import java.util.Map;

import static Enums.SceneType.*;
import static Utils.Constants.*;

/**
 * This class is responsible for the handling of multiple scenes.
 * @see Notifiable
 */
public class SceneHandler implements Notifiable {
    /**Shared instance.*/
    static SceneHandler instance;

    /**Map of scenes.*/
    private final Map<SceneType, Scene> scenes;

    /**Current active scene.*/
    private Scene activeScene;

    /**
     * This constructor loads a series of predefined scenes.
     */
    private SceneHandler()  {
        scenes = new HashMap<>();
        scenes.put(MAIN_MENU_SCENE, new MainMenuScene(this));
        scenes.put(CHOOSE_PLAYER_SCENE, new ChoosePlayerScene(this));
        scenes.put(CHOOSE_LEVEL_SCENE, new ChooseLevelScene(this));
        scenes.put(PLAY_SCENE, new PlayScene(this));
        scenes.put(LOAD_SCENE, new LoadScene(this));
        scenes.put(LEVEL_PAUSED_SCENE, new LevelPauseScene(this));
        scenes.put(LEVEL_FAILED_SCENE, new LevelFailedScene(this));
        scenes.put(LEVEL_COMPLETED_SCENE, new LevelCompletedScene(this));

        handleSceneChangeRequest(MAIN_MENU_SCENE);
    }

    /**
     * Getter for shared instance.
     * @return shared instance
     */
    public static SceneHandler getInstance() {
        if (instance == null) {
            instance = new SceneHandler();
        }
        return instance;
    }

    /**
     * Getter for a current active scene.
     * @return current scene
     */
    public Scene getActiveScene(){
        return activeScene;
    }

    /**
     * This method handles the scene change request.
     * It can change the active state if the new scene does not belong to the current active state.
     * @param newScene scene to be activated
     */
    public void handleSceneChangeRequest(SceneType newScene) {
        if (scenes.containsKey(newScene)) {
            activeScene = scenes.get(newScene);
            activeScene.notify(new Message(MessageType.SCENE_HAS_BEEN_ACTIVATED, ComponentType.SCENE_HANDLER, INVALID_ID));
        } else {
            activeScene = null;
        }
    }

    @Override
    public void notify(Message message) {
        switch (message.source()) {

            case SCENE -> {
                switch (message.type()) {
                    case SAVE_GAME -> {
                        scenes.get(PLAY_SCENE).notify(message);
                        scenes.get(LOAD_SCENE).notify(message);
                    }
                    case BIKER_SELECTED, PUNK_SELECTED, CYBORG_SELECTED -> {
                        scenes.get(PLAY_SCENE).notify(message);
                        scenes.get(LEVEL_PAUSED_SCENE).notify(message);
                    }
                    case NEW_GAME, LOAD_GAME,GREEN_MAP_SELECTED,INDUSTRIAL_MAP_SELECTED -> scenes.get(PLAY_SCENE).notify(message);
                    case WEAPON_IS_DROPPED -> scenes.get(LEVEL_PAUSED_SCENE).notify(message);
                }
            }

            case INVENTORY -> {
                switch (message.type()) {
                    case WEAPON_IS_SELECTED, DISABLE_GUN, WEAPON_IS_DROPPED,HAS_NO_WEAPON -> scenes.get(PLAY_SCENE).notify(message);
                }
            }

            case GUN_1, GUN_2, GUN_3, GUN_4, GUN_5, GUN_6, GUN_7, GUN_8,
                    GUN_9, GUN_10 -> scenes.get(LEVEL_PAUSED_SCENE).notify(message);

            case PLAYER -> {
                if(message.type() ==MessageType.WEAPON_IS_SELECTED) scenes.get(LEVEL_PAUSED_SCENE).notify(message);
            }
        }
    }
}
