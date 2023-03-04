package States;

import java.util.HashMap;

/**
 * This class implements the Singleton design pattern and
 * handles all the states in the game by saving them into a HashMap.
 */
public class StatesHandler {

    static private StatesHandler instance = null;
    private final HashMap<String , State> states;
    private State activeState;
    private StatesHandler() throws Exception {
        states = new HashMap<>();
        activeState = null;

        addState("play" , new PlayState());
        addState("menu" , new MenuState());

        setActiveState("menu");
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
}
