package Components.MenuComponents;

import Components.BaseComponents.AssetsDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.GameComponents.GameItems.Gun;
import Components.Notifiable;
import Components.StaticComponent;
import Enums.ComponentType;
import Scenes.Messages.Message;
import Utils.Coordinate;
import Utils.Rectangle;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory implements StaticComponent, Notifiable {
    private Map<Integer, StaticComponent> items;

    private Integer itemsNumber = 0;
    private int selectedWeaponId;

    public Inventory() {
        items = new HashMap<>();
        // add boxes
        items.put(0 , new BoxItem(new Rectangle(new Coordinate<>(900,750) , 150 , 100)));
        items.put(1 , new BoxItem(new Rectangle(new Coordinate<>(1075,750) , 150 , 100)));
        items.put(2 , new BoxItem(new Rectangle(new Coordinate<>(1250,750) , 150 , 100)));
        items.put(3 , new BoxItem(new Rectangle(new Coordinate<>(1425,750) , 150 , 100)));
        items.put(4 , new BoxItem(new Rectangle(new Coordinate<>(1600,750) , 150 , 100)));
        // add player preview
    }

    @Override
    public void notify(Message message) {
        switch (message.type()) {
            case IS_PICKED_UP -> {
                ImageWrapper item= AssetsDeposit.get().getGun(message.source()).getImageWrapper();
                Rectangle rectangle = ((BoxItem)items.get(itemsNumber++)).getRectangle();

                item.setScale(3).setPosition(new Coordinate<>(rectangle.getCenterX()-item.getRectangle().getWidth()/2,
                        rectangle.getCenterY() - item.getRectangle().getHeight()/2 ));
                items.put(message.componentId(), item);
            }
        }
    }

    @Override
    public void update() {
        for (Map.Entry<Integer , StaticComponent> item : items.entrySet()) {
            item.getValue().update();
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        for (Map.Entry<Integer , StaticComponent> item : items.entrySet()) {
            item.getValue().draw(graphics2D);
        }
    }
}
