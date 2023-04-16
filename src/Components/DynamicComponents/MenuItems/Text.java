package Components.DynamicComponents.MenuItems;

import Components.StaticComponent;
import Enums.ColorType;
import Enums.ComponentType;
import Utils.Coordinate;
import Window.GameWindow;

public class Text implements StaticComponent {
    private Coordinate<Integer> position;
    private ColorType textColor = ColorType.DefaultText;
    private String text = "";
    private int size= 0;
    public Text(ComponentType type, Coordinate<Integer> position , int size){
        switch (type) {
            case NewGameButton -> text = "NEW GAME";
            case LoadButton -> text = "LOAD GAME";
            case SettingsButton-> text = "SETTINGS";
            case ExitButton -> text = "EXIT";
            case LoadSave -> text = "Load";
            case DeleteSave -> text = "Clean this";
            case DeleteAllSaves -> text = "Clean all";
            case Continue -> text = "CONTINUE";
            case SaveButton -> text = "Save";
            case BackToMenu -> text = "Back to menu";
            case Back -> text = "Back";
            case GameTitle1 -> text = "CYBERPUNK";
            case GameTitle2 -> text = "2030";
        }
        this.position = position;
        this.size = size;
    }
    @Override
    public void update() throws Exception {}

    @Override
    public void draw() {
        GameWindow.getInstance().drawText(text, position.getPosX(),
                position.getPosY(), textColor.getColor(), size);
    }

    public void setTextColor(ColorType color){
        textColor = color;
    }
}