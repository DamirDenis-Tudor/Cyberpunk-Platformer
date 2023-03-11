package Components.StaticComponents;

import GameWindow.GameWindow;
import Input.KeyboardInput;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ParallaxWallpaper implements StaticComponent {

    private final GameWindow gameWindow;
    private final KeyboardInput keyboardInput;
    private final List<BufferedImage> images;

    private final List<Integer> velocities;

    private final List<Integer> background1Position;

    private final List<Integer> background2Position;
    public ParallaxWallpaper(){
        gameWindow = GameWindow.getInstance();
        keyboardInput = KeyboardInput.getInstance();
        images = new ArrayList<>();

        background1Position = new ArrayList<>();
        background2Position = new ArrayList<>();

        velocities = new ArrayList<>();


    }

    public void addImage(BufferedImage image){
        images.add(image);
        background1Position.add(0);
        background2Position.add(gameWindow.GetWndWidth());
        velocities.add((int) (images.size()));
    }

    private int scrollingDirection(){
        if(keyboardInput.getKeyA()){
            return 1;
        }else if(keyboardInput.getKeyD()){
            return -1;
        }
        return 0;
    }
    @Override
    public void update() throws Exception {
        for (int index = 0 ; index < images.size() ; index++){


            if(background1Position.get(index) <= -gameWindow.GetWndWidth()){
                background1Position.set(index , gameWindow.GetWndWidth());
            }else if(background1Position.get(index) >= gameWindow.GetWndWidth()){
                background1Position.set(index , -gameWindow.GetWndWidth());
            }
            if(background2Position.get(index) <= -gameWindow.GetWndWidth()){
                background2Position.set(index , gameWindow.GetWndWidth());
            }else if(background2Position.get(index) >= gameWindow.GetWndWidth()){
                background2Position.set(index , -gameWindow.GetWndWidth());
            }

            background1Position.set(index , background1Position.get(index) + scrollingDirection() * velocities.get(index));
            background2Position.set(index , background2Position.get(index) + scrollingDirection() * velocities.get(index));
        }
    }

    @Override
    public void draw() {
        for (int index = 0 ; index < images.size() ; index++){
            gameWindow.getGraphics().drawImage(images.get(index) , background1Position.get(index) , 0 , gameWindow.GetWndWidth() , gameWindow.GetWndHeight(), null);
            gameWindow.getGraphics().drawImage(images.get(index) , background2Position.get(index) , 0 , gameWindow.GetWndWidth() , gameWindow.GetWndHeight(), null);
        }
    }
}
