package engine.tools.itemsManager;

public class InventoryItem {
    private final Item item;
    private int amount;

    public InventoryItem(Item item) {
        this(item, 1);
    }

    public InventoryItem(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int newCount) {
        amount = newCount;
    }

    public void changeCount(int addedValue) {
        amount += addedValue;
        if (amount < 0) {
            amount = 0;
        }
    }
}
