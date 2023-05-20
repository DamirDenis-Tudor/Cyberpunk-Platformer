package Components.MenuComponents;

import Components.BaseComponents.AssetsDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.Notifiable;
import Components.StaticComponent;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Utils.Coordinate;
import Utils.Rectangle;

import java.awt.*;
import java.util.*;
import java.util.List;

import static Utils.Constants.*;

/**
 *
 */

public class Inventory implements StaticComponent, Notifiable {
    private final Notifiable scene;
    private final List<ComponentType> gunsTypes;
    private final Map<Integer, StaticComponent> items;
    private Integer itemsNumber;
    private Integer selectedWeaponId = INVALID_ID;
    private Integer selectedWeaponIdFromDatabase = INVALID_ID;

    public Inventory(Notifiable scene) {
        this.scene = scene;
        gunsTypes = new LinkedList<>();
        items = new LinkedHashMap<>();

        // add boxes
        items.put(0, new BoxItem(this, new Rectangle(new Coordinate<>(900, 750), 150, 100)));
        items.put(1, new BoxItem(this, new Rectangle(new Coordinate<>(1075, 750), 150, 100)));
        items.put(2, new BoxItem(this, new Rectangle(new Coordinate<>(1250, 750), 150, 100)));
        items.put(3, new BoxItem(this, new Rectangle(new Coordinate<>(1425, 750), 150, 100)));
        items.put(4, new BoxItem(this, new Rectangle(new Coordinate<>(1600, 750), 150, 100)));
    }

    @Override
    public void notify(Message message) {
        switch (message.type()) {
            case WEAPON_IS_SELECTED -> selectedWeaponIdFromDatabase = message.componentId();
            case IS_PICKED_UP -> {
                if (itemsNumber < MAX_ITEMS_NUMBER) {
                    ImageWrapper item = new ImageWrapper(AssetsDeposit.get().getGun(message.source()).getImageWrapper());
                    Rectangle rectangle = ((BoxItem) items.get(itemsNumber++)).getRectangle();
                    item.setScale(3).setPosition(new Coordinate<>(rectangle.getCenterX() - item.getRectangle().getWidth() / 2,
                            rectangle.getCenterY() - item.getRectangle().getHeight() / 2));
                    gunsTypes.add(message.source());
                    items.put(message.componentId(), item);
                    if ((itemsNumber == FIRST_ITEM && selectedWeaponIdFromDatabase == INVALID_ID) || message.componentId() == selectedWeaponIdFromDatabase) {
                        selectedWeaponId = message.componentId();
                        scene.notify(new Message(MessageType.WEAPON_IS_SELECTED, message.source(), message.componentId()));
                    }
                } else {
                    scene.notify(new Message(MessageType.WEAPON_IS_DROPPED, ComponentType.INVENTORY, message.componentId()));
                }
            }
            case BUTTON_CLICKED -> {
                int counter = 0;
                for (Integer key : items.keySet()) {
                    if (counter++ == MAX_ITEMS_NUMBER + message.componentId()) {
                        scene.notify(new Message(MessageType.DISABLE_GUN, ComponentType.INVENTORY, selectedWeaponId));
                        selectedWeaponId = key;
                        scene.notify(new Message(MessageType.WEAPON_IS_SELECTED, gunsTypes.get(message.componentId()), key));
                        break;
                    }
                }
            }
            case WEAPON_IS_DROPPED -> {
                if (gunsTypes.size() == 0) break;

                int counter = 0;
                for (Map.Entry<Integer, StaticComponent> item : items.entrySet()) {
                    if (item.getKey() == message.componentId()) break;
                    counter++;
                }
                gunsTypes.remove(counter - MAX_ITEMS_NUMBER);
                items.remove(message.componentId());
                scene.notify(new Message(MessageType.WEAPON_IS_DROPPED, ComponentType.INVENTORY, message.componentId()));
                itemsNumber--;

                List<ImageWrapper> tobeRepositioned = new LinkedList<>();
                counter = 0 ;
                for (Map.Entry<Integer, StaticComponent> item : items.entrySet()) {
                    if (counter++ >= MAX_ITEMS_NUMBER) {
                        selectedWeaponId = item.getKey();
                        tobeRepositioned.add((ImageWrapper) item.getValue());
                    }
                }
                counter = 0 ;
                for (ImageWrapper item : tobeRepositioned){
                    Rectangle rectangle = ((BoxItem) items.get(counter++)).getRectangle();
                    item.setPosition(new Coordinate<>(rectangle.getCenterX() - item.getRectangle().getWidth() / 2,
                            rectangle.getCenterY() - item.getRectangle().getHeight() / 2));
                }
                if(tobeRepositioned.size() != 0) {
                    scene.notify(new Message(MessageType.WEAPON_IS_SELECTED, gunsTypes.get(tobeRepositioned.size() - 1), selectedWeaponId));
                }else {
                    selectedWeaponId = INVALID_ID;
                    selectedWeaponIdFromDatabase = INVALID_ID;
                    scene.notify(new Message(MessageType.HAS_NO_WEAPON , ComponentType.INVENTORY , -1));
                }
            }
            case CLEAR_INVENTORY -> {
                int counter = 0;
                List<Integer> toBeRemoved = new ArrayList<>();
                for (Map.Entry<Integer, StaticComponent> item : items.entrySet()) {
                    if (counter >= MAX_ITEMS_NUMBER) {
                        toBeRemoved.add(item.getKey());
                    }
                    counter++;
                }
                for (Integer id : toBeRemoved) {
                    items.remove(id);
                }
                gunsTypes.clear();
                itemsNumber = 0;
                selectedWeaponId = INVALID_ID;
                selectedWeaponIdFromDatabase = INVALID_ID;
                scene.notify(new Message(MessageType.CLEAR_INVENTORY , ComponentType.INVENTORY , INVALID_ID));
            }
        }
    }

    @Override
    public void update() {
        for (Map.Entry<Integer, StaticComponent> item : items.entrySet()) {
            item.getValue().update();
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        for (Map.Entry<Integer, StaticComponent> item : items.entrySet()) {
            item.getValue().draw(graphics2D);
        }
    }
}
