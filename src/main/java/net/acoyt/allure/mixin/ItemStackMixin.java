package net.acoyt.allure.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author AcoYT
 */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @SuppressWarnings("ConstantValue")
    @WrapMethod(method = "isEnchantable")
    private boolean allure$noEnchantments(Operation<Boolean> original) {
        return original.call() && !((ItemStack)(Object)this).hasAllure();
    }
}
