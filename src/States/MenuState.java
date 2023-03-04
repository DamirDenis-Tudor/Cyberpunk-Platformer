package States;

import Scenes.InMenu.InitGameScene;
import Scenes.InMenu.LogoStartScene;
import Scenes.InMenu.MainMenuScene;
import Scenes.InMenu.SettingsScene;

public class MenuState extends State {

    public MenuState() throws Exception {
        super();

        sceneHandler.addScene("InitGameScene" , new InitGameScene());
        sceneHandler.addScene("LogoStartScene" , new LogoStartScene());
        sceneHandler.addScene("MainMenuScene" , new MainMenuScene());
        sceneHandler.addScene("SettingsScene" , new SettingsScene());

        sceneHandler.setActiveScene("InitGameScene");
    }
    @Override
    public void updateState() throws Exception {

        //System.out.println("-----------MenuState update-----------");

        sceneHandler.getActiveScene().update();
    }

    @Override
    public void drawState() throws Exception {
        //System.out.println("-----------MenuState draw--------------");

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
