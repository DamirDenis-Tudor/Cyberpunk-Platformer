package Scenes.InMenu;

import Components.BaseComponents.AssetsDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.MenuComponents.Button;
import Database.Database;
import Enums.ComponentType;
import Enums.MessageType;
import Enums.SceneType;
import Input.MouseInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Scenes.SceneHandler;
import Utils.Constants;
import Utils.Coordinate;
import Utils.Rectangle;
import Window.Camera;

import java.util.Objects;

public class LoadScene extends Scene {
    private int savesStartingPosition = 0;

    public LoadScene(SceneHandler sceneHandler) {
        super(sceneHandler);
        ImageWrapper menuWallpaper = AssetsDeposit.get().getMenuWallpaper();
        menuWallpaper.setRectangle(new Rectangle(new Coordinate<>(0,0) , Constants.windowWidth , Constants.windowHeight));
        components.add(menuWallpaper);
        components.add(new Button(this, ComponentType.LoadSave, "LOAD",
                new Rectangle(new Coordinate<>(350, 300), 400, 150), 56));
        components.add(new Button(this, ComponentType.DeleteLatestSave, "DELETE LAST",
                new Rectangle(new Coordinate<>(350, 500), 400, 150), 50));
        components.add(new Button(this, ComponentType.Back, "BACK",
                new Rectangle(new Coordinate<>(350, 700), 400, 150), 56));

        savesStartingPosition = components.size();

        loadSaves();
    }

    private void loadSaves() {
        // delete the previous saves if exists
        if (components.size() > savesStartingPosition) {
            components.subList(savesStartingPosition, components.size()).clear();
        }
        // add saves from database
        int counter = 0;
        Database database = Database.get();
        for (String save : database.getAllSavesInfo()) {
            components.add(new Button(this, ComponentType.SaveInfo, save,
                    new Rectangle(new Coordinate<>(800, 300 + 80 * counter++), 900, 75), 40));
        }
    }

    @Override
    public void notify(Message message) {
        switch (message.type()) {
            case SceneHasBeenActivated -> MouseInput.get().reset();
            case SaveGame, SaveHasBeenAdded -> loadSaves();
            case ButtonClicked -> {
                switch (message.source()) {
                    case LoadSave -> {
                        if (!Objects.equals(Database.get().getSaveToBeLoaded(), "")) {
                            sceneHandler.notify(new Message(MessageType.LoadGame, ComponentType.Scene, -1));
                            sceneHandler.handleSceneChangeRequest(SceneType.PlayScene);
                        }
                    }
                    case DeleteLatestSave -> {
                        if (components.size() == savesStartingPosition) return;

                        Database.get().deleteLastSave();
                        loadSaves();
                    }
                    case Back -> sceneHandler.handleSceneChangeRequest(SceneType.MainMenuScene);
                }
            }
        }
    }
}
