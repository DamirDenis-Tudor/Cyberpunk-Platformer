package Scenes.InMenu;

import Components.DynamicComponents.MenuItems.Button;
import Components.DynamicComponents.MenuItems.Text;
import Enums.ComponentType;
import Enums.SceneType;
import Input.MouseInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Scenes.SceneHandler;
import Utils.Coordinate;
import Utils.Rectangle;
import Window.Camera;


final public class MainMenuScene extends Scene {

    public MainMenuScene(SceneHandler sceneHandler) throws Exception {
        super(sceneHandler);
        components.add(new Text(ComponentType.GameTitle1 , new Coordinate<>(1300,400 ) , 150));
        components.add(new Text(ComponentType.GameTitle2 , new Coordinate<>(1350,550 ) , 150));
        components.add(new Button(this, ComponentType.NewGameButton,
                new Rectangle(new Coordinate<>(200,200),400,100)));
        components.add(new Button(this, ComponentType.LoadButton,
                new Rectangle(new Coordinate<>(200,400),400,100)  ));
        components.add(new Button(this, ComponentType.SettingsButton,
                new Rectangle(new Coordinate<>(200,600),400,100)  ));
        components.add(new Button(this, ComponentType.ExitButton,
                new Rectangle(new Coordinate<>(200,800),400,100)  ));
    }

    @Override
    public void notify(Message message) throws Exception {
        switch (message.type()){
            case SceneHasBeenActivated -> {
                Camera.getInstance().disableCameraOffset();
                MouseInput.getInstance().reset();
            }
            case ButtonClicked -> {
                switch (message.source()){
                    case NewGameButton -> sceneHandler.handleSceneChangeRequest(SceneType.PlayScene);
                    case LoadButton -> sceneHandler.handleSceneChangeRequest(SceneType.LoadScene);
                    case SettingsButton -> sceneHandler.handleSceneChangeRequest(SceneType.SettingsScene);
                    case ExitButton -> sceneHandler.handleSceneChangeRequest(SceneType.NoScene);
                }
            }
        }
    }
}
