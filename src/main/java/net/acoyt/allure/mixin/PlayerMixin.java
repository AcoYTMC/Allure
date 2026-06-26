package net.acoyt.allure.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.acoyt.allure.impl.component.AllureComponent;
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
            method = {"attack","stabAttack"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;setLastHurtMob(Lnet/minecraft/world/entity/Entity;)V"
            )
    )
    private void allure$onAttackRamifications(Player instance, Entity entity, Operation<Void> original) {
        original.call(instance, entity);
        AllureComponent.getRamifications(instance.getMainHandItem()).forEach(ramification -> ramification.onAttack(instance, entity, instance.getMainHandItem()));
    }
}
