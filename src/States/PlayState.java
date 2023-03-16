package States; // This package implements State Design Pattern.

import Scenes.InGame.LevelCompletedScene;
import Scenes.InGame.LevelFailedScene;
import Scenes.InGame.LevelPauseScene;
import Scenes.InGame.PlayScene;

/**
 * This class implements the playState that is
 * responsible for the actual action of the game.
 */
public class PlayState extends State{

    public PlayState() throws Exception {

        super();
        /*
            add a couple of scenes
         */
        sceneHandler.addScene("PlayScene" , new PlayScene(this));
        sceneHandler.addScene("LevelPauseScene" , new LevelPauseScene(this));
        sceneHandler.addScene("LevelFailedScene" , new LevelFailedScene(this));
        sceneHandler.addScene("LevelCompletedScene" , new LevelCompletedScene(this));
    }


}

