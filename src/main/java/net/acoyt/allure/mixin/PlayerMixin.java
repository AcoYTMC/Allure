package net.acoyt.allure.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.acoyt.allure.impl.component.AllureComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author AcoYT
 */
@Mixin(Player.class)
public abstract class PlayerMixin extends Avatar {
    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurtOrSimulate(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )
    )
    private boolean allure$onAttackRamifications(Entity instance, DamageSource source, float damage, Operation<Boolean> original) {
        boolean value = original.call(instance, source, damage);
        if (value) {
            AllureComponent.getRamifications(this.getMainHandItem()).forEach(ramification -> ramification.onAttack((Player)(Object)this, instance, this.getMainHandItem()));
        }

        return value;
    }

    @WrapOperation(
            method = "stabAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurtOrSimulate(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )
    )
    private boolean allure$onStabAttackRamifications(Entity instance, DamageSource source, float damage, Operation<Boolean> original) {
        boolean value = original.call(instance, source, damage);
        if (value) {
            AllureComponent.getRamifications(this.getMainHandItem()).forEach(ramification -> ramification.onAttack((Player)(Object)this, instance, this.getMainHandItem()));
        }

        return value;
    }
}
