package net.acoyt.allure.impl.util.interfaces;

import net.acoyt.allure.impl.index.AllureDataComponents;
import net.acoyt.allure.impl.util.AllureEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;

/**
 * @author AcoYT
 */
public interface ItemStackAccess extends DataComponentHolder {
    default boolean supportsAllure(Holder<AllureEntry> entryHolder) {
        return false;
    }

    default boolean hasAllure() {
        return this.has(AllureDataComponents.ALLURES);
    }
}
