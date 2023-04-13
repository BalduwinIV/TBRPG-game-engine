package game.objects;

import java.util.UUID;

/**
 * Item base class.
 */
public class Item {
    private String id;
    private String name;

    public Item(String name) {
        this.name = name;
        id = UUID.randomUUID().toString();
    }

    /**
     * Return a unique id of this item that was created with object.
     * @return  A unique id of this item.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns items name.
     * @return  Items name.
     */
    public String getName() {
        return name;
    }
}
