package engine;

/**
 *  Defines program colors.
 */
public class GUIColors {
    public enum Colors {
        BACKGROUND,
        CHARACTER_ALLY,
        CHARACTER_ENEMY,
        INVENTORY_CHARACTER,
        INVENTORY_KNOWN_ITEMS
    }

    /**
     *  Return color.
     * @param color Color.
     * @return Color as value.
     */
    public static int color(Colors color) {
        switch (color) {
            case BACKGROUND -> {
                return 0xe6e6e6;
            }
            case CHARACTER_ALLY -> {
                return 0xcfecec;
            }
            case CHARACTER_ENEMY -> {
                return 0xff9c9c;
            }
            case INVENTORY_CHARACTER -> {
                return 0xffaa00;
            }
            case INVENTORY_KNOWN_ITEMS -> {
                return 0x00c8ff;
            }
        }
        return 0x000000;
    }
}
