package States;

import Scenes.InGame.LevelCompletedScene;
import Scenes.InGame.LevelFailedScene;
import Scenes.InGame.LevelPauseScene;
import Scenes.InGame.PlayScene;

public class PlayState extends State{

    public PlayState() throws Exception {
        super();

        sceneHandler.addScene("PlayScene" , new PlayScene());
        sceneHandler.addScene("LevelPauseScene" , new LevelPauseScene());
        sceneHandler.addScene("LevelFailedScene" , new LevelFailedScene());
        sceneHandler.addScene("LevelCompletedScene" , new LevelCompletedScene());

        sceneHandler.setActiveScene("PlayScene");
    }
    @Override
    public void updateState() throws Exception {

        //System.out.println("-----------PlayState update-----------");
        sceneHandler.getActiveScene().update();
    }

    @Override
    public void drawState() throws Exception {
        //System.out.println("-----------PlayState draw-----------");
        sceneHandler.getActiveScene().draw();
    }

    @Override
    public void saveState() {

    }

    @Override
    public void loadState() {

    }

    @Override
    public void resetState() {

    }
}

