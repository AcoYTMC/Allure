package net.acoyt.allure.mixin.access;

import net.acoyt.allure.impl.util.AllureEntry;
import net.acoyt.allure.impl.util.interfaces.ItemStackAccess;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author AcoYT
 */
@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackAccess, ItemInstance {
    @Shadow public abstract boolean isEnchanted();

    @Override
    public boolean supportsAllure(Holder<AllureEntry> entryHolder) {
        return this.is(entryHolder.value().supportedItems()) && !this.isEnchanted();
    }

    @Override
    public boolean hasAllure() {
        return ItemStackAccess.super.hasAllure();
    }
}
