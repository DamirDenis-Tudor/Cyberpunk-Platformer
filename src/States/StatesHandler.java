package States; // This package implements State Design Pattern.

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the <b>Singleton Design Pattern</b> and
 * handles all the states in the game by saving them into a HashMap.
 */
public class StatesHandler {

    static private StatesHandler instance = null;
    private final HashMap<String , State> states;
    private State activeState;
    private StatesHandler() throws Exception {
        states = new HashMap<>();
        activeState = null;

        /*
            add a couple of states
         */
        addState("play" , new PlayState());
        addState("menu" , new MenuState());


    }

    /**
     * @return The only possible instance of this class.
     */
    public static StatesHandler getInstance() throws Exception {
        if(instance == null) {
            instance = new StatesHandler();
        }
        return instance;
    }

    /**
     *
     * @param state to be added
     * @param ID state identifier
     * @throws Exception message
     */
    public void addState(String ID , State state) throws Exception {
        if(states.containsKey(ID)) {
            throw new Exception("Error - trying to add an existing state!");
        }
        if(states.isEmpty()){
            activeState = state;
        }
        states.put(ID , state);
    }

    /**
     * @param ID active state identifier
     */
    public void setActiveState(String ID) throws Exception {
        if(!states.containsKey(ID)) {
            throw new Exception("Error - trying to add an existing state!");
        }
        this.activeState = states.get(ID);
        System.out.println("Active state has been changed to " + ID +
                " , scene : " + activeState.getSceneHandler().getActiveSceneID());
    }

    /**
     * @return active state
     * @throws Exception message
     */
    public State getActiveState() throws Exception {
        if(activeState == null ){
            throw new Exception("Error - trying to get an invalid active state!");
        }
        return activeState;
    }

    /**
     * This method handles the scene change request .
     * It can change the active state if the new scene not
     * belong to the current active state.
     * @param newScene
     */
    public void handleSceneChangeRequest(String newScene) throws Exception {
        boolean isValidScene = false;
        for (Map.Entry<String, State> state:states.entrySet()) {
            if(state.getValue().getSceneHandler().checkSceneBelongsToGroup(newScene)){
                isValidScene = true;
                if (activeState != state.getValue()) {
                    activeState = state.getValue();
                }
                activeState.getSceneHandler().setActiveScene(newScene);
            }
        }

        if (!isValidScene){
            throw (new Exception("EROR : handleSceneChangeRequest -> scene name is invalid : \"" + newScene + "\""));
        }
    }
}
