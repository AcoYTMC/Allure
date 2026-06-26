package net.acoyt.allure.mixin;

import net.acoyt.allure.impl.component.AllureComponent;
import net.acoyt.allure.impl.ramifications.Ramification;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author AcoYT
 */
@Mixin(Player.class)
public abstract class PlayerMixin extends Avatar {
    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;setLastHurtMob(Lnet/minecraft/world/entity/Entity;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void allure$onAttackRamifications(Entity entity, CallbackInfo ci) {
        ItemStack stack = this.getWeaponItem();
        for (Ramification ramification : AllureComponent.getRamifications(stack)) {
            ramification.onAttack((Player)(Object)this, entity, stack);
        }
    }

    @Inject(
            method = "stabAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;setLastHurtMob(Lnet/minecraft/world/entity/Entity;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void allure$onStabAttackRamifications(EquipmentSlot slot, Entity target, float baseDamage, boolean dealsDamage, boolean dealsKnockback, boolean dismounts, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = this.getItemBySlot(slot);
        for (Ramification ramification : AllureComponent.getRamifications(stack)) {
            ramification.onAttack((Player)(Object)this, target, stack);
        }
    }
}
