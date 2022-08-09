package name.uwu.feytox.spwallet.counter;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public enum ScreenType {
    INVENTORY,
    INVENTORY_WITH_RECIPE,
    CHEST,
    DOUBLE_CHEST;

    public static ScreenType getScreenType(Screen screen) {
        Text screenTitle = screen.getTitle();

        if (screenTitle instanceof TranslatableTextContent) {
            String screenName = ((TranslatableTextContent) screenTitle).getKey();

            if ("container.chestDouble".equals(screenName)) {
                return ScreenType.DOUBLE_CHEST;
            }
        }

        return ScreenType.CHEST;
    }
}
