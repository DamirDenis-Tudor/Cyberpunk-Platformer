package States; // This package implements State Design Pattern.

import Scenes.InMenu.InitGameScene;
import Scenes.InMenu.LogoStartScene;
import Scenes.InMenu.MainMenuScene;
import Scenes.InMenu.SettingsScene;

/**
 * This class implements the menuState that is
 * responsible with the menu interaction.
 */
public class MenuState extends State {

    public MenuState() throws Exception {
        super();

        /*
            add a couple of scenes
         */
        sceneHandler.addScene("LogoStartScene" , new LogoStartScene());
        sceneHandler.addScene("MainMenuScene" , new MainMenuScene());
        sceneHandler.addScene("InitGameScene" , new InitGameScene());
        sceneHandler.addScene("SettingsScene" , new SettingsScene());

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
}
