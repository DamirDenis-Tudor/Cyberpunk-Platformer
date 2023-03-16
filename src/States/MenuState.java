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
        sceneHandler.addScene("LogoStartScene" , new LogoStartScene(this));
        sceneHandler.addScene("MainMenuScene" , new MainMenuScene(this));
        sceneHandler.addScene("InitGameScene" , new InitGameScene(this));
        sceneHandler.addScene("SettingsScene" , new SettingsScene(this));

    }
}
