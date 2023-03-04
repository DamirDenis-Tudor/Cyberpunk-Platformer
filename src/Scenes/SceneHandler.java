package Scenes;

import java.util.HashMap;
import java.util.Map;

public class SceneHandler {
    private final Map<String, Scene> scenes;
    private Scene activeScene;

    public SceneHandler(){
        scenes = new HashMap<>();
    }
    public void addScene(String ID , Scene scene) throws Exception {
        if(scenes.containsKey(ID)){
            throw new Exception("Error : trying to add an element that have same ID with an existing one!");
        }
        scenes.put(ID , scene);
    }

    public void setActiveScene(String ID) throws Exception {
        if(!scenes.containsKey(ID)){
            throw new Exception("Error : invalid ID : " + ID + " in scene handler." );
        }
        System.out.println("Scene has been changed to " + ID  + "." );
        activeScene = scenes.get(ID);
    }

    public Scene getActiveScene() throws Exception {
        if(activeScene == null) {
            throw new Exception("Error : active scene is null!");
        }
        return activeScene;
    }

    public String getActiveSceneID() throws Exception {
        if (activeScene == null){
            throw new NullPointerException("Error - active scene is null.");
        }

        for (Map.Entry<String, Scene> entry: scenes.entrySet()) {
            if (entry.getValue() == activeScene){
                return entry.getKey();
            }
        }
        throw new Exception("Error - identifier not found for active scene.");
    }
}
