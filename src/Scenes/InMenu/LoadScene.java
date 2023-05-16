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

import java.util.Objects;

public class LoadScene extends Scene {
    private int savesStartingPosition = 0;

    public LoadScene(SceneHandler sceneHandler) {
        super(sceneHandler);
        ImageWrapper menuWallpaper = AssetsDeposit.get().getMenuWallpaper();
        menuWallpaper.setRectangle(new Rectangle(new Coordinate<>(0,0) , Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        components.add(menuWallpaper);
        components.add(new Button(this, ComponentType.LOAD_SAVE, "LOAD",
                new Rectangle(new Coordinate<>(350, 300), 400, 150), 56));
        components.add(new Button(this, ComponentType.DELETE_LATEST_SAVE, "DELETE LAST",
                new Rectangle(new Coordinate<>(350, 500), 400, 150), 50));
        components.add(new Button(this, ComponentType.BACK, "BACK",
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
            components.add(new Button(this, ComponentType.SAVE_INFO, save,
                    new Rectangle(new Coordinate<>(800, 300 + 80 * counter++), 900, 75), 40));
        }
    }

    @Override
    public void notify(Message message) {
        switch (message.type()) {
            case SCENE_HAS_BEEN_ACTIVATED -> MouseInput.get().reset();
            case SAVE_GAME, SAVE_HAS_BEEN_ADDED -> loadSaves();
            case BUTTON_CLICKED -> {
                switch (message.source()) {
                    case LOAD_SAVE -> {
                        if (!Objects.equals(Database.get().getSaveToBeLoaded(), "")) {
                            sceneHandler.notify(new Message(MessageType.LOAD_GAME, ComponentType.SCENE, -1));
                            sceneHandler.handleSceneChangeRequest(SceneType.PLAY_SCENE);
                        }
                    }
                    case DELETE_LATEST_SAVE -> {
                        if (components.size() == savesStartingPosition) return;

                        Database.get().deleteLastSave();
                        loadSaves();
                    }
                    case BACK -> sceneHandler.handleSceneChangeRequest(SceneType.MAIN_MENU_SCENE);
                }
            }
        }
    }
}
