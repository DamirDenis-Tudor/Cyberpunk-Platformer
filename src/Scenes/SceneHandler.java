package Scenes;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for handling of multiple scenes.
 * Usually is an object of State class.
 */
public class SceneHandler {
    private final Map<String, Scene> scenes;
    private Scene activeScene;

    public SceneHandler(){
        scenes = new HashMap<>();
    }

    /**
     * @param ID identifier for scene
     * @param scene to be added
     * @throws Exception message
     */
    public void addScene(String ID , Scene scene) throws Exception {
        if(scenes.containsKey(ID)){
            throw new Exception("Error : trying to add an element that have same ID with an existing one!");
        }
        if(scenes.isEmpty()){
            activeScene = scene;
        }
        scenes.put(ID , scene);
    }

    /**
     *
     * @param ID identifier for the next possible active scene
     * @throws Exception message
     */
    public void setActiveScene(String ID) throws Exception {
        if(!scenes.containsKey(ID)){
            throw new Exception("Error : invalid ID : " + ID + " in scene handler." );
        }
        System.out.println("Scene has been changed to " + ID  + "." );
        activeScene = scenes.get(ID);
    }

    /**
     *
     * @param sceneName to be checked
     * @return true/false if the scene belongs or not to this scene handler
     */
    public boolean checkSceneBelongsToGroup(String sceneName){
        return scenes.containsKey(sceneName);
    }

    /**
     * @return actual active scene
     * @throws Exception message
     */
    public Scene getActiveScene() throws Exception {
        if(activeScene == null) {
            throw new Exception("Error : active scene is null!");
        }
        return activeScene;
    }

    /**
     * This method iterates the map until a scene is equal
     * (has the same address with the active one.
     * @return identifier of active scene
     * @throws Exception message
     */
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
