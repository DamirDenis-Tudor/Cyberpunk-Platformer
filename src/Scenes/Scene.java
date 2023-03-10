package Scenes;

import States.StatesHandler;

public abstract class Scene {
    public abstract void draw() throws Exception;

    public abstract void update() throws Exception;

    public void requestSceneChange(String newScene) throws Exception {
        StatesHandler.getInstance().handleSceneChangeRequest(newScene);
    }

    /*
        public abstract void saveState();
        public abstract void loadState();
        public abstract void resetState();
    */
}
