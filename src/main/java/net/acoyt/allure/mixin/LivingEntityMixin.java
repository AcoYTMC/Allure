package net.acoyt.allure.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.acoyt.allure.impl.cca.entity.ChainingComponent;
import net.acoyt.allure.impl.component.AllureComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * @author AcoYT
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "actuallyHurt", at = @At("HEAD"))
    private void allure$onDamagedRamifications(ServerLevel level, DamageSource source, float dmg, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof Player player) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.isArmor() && !player.getItemBySlot(slot).isEmpty()) {
                    ItemStack stack = player.getItemBySlot(slot);
                    AllureComponent.getRamifications(stack).forEach(ramification -> ramification.onDamaged(player, source, stack));
                }
            }
        }
    }

    @WrapMethod(method = "actuallyHurt")
    private void allure$splitDamageForChaining(ServerLevel level, DamageSource source, float dmg, Operation<Void> original) {
        ChainingComponent component = ChainingComponent.KEY.get(this);
        List<LivingEntity> entities = component.getAllChainedTo();
        if (!entities.isEmpty()) {
            dmg /= (entities.size() + 1);
            for (LivingEntity entity : entities) {
                entity.actuallyHurt(level, source, dmg);
            }
        }

        original.call(level, source, dmg);
    }

    @WrapOperation(
            method = "hurtServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V"
            )
    )
    private void allure$splitDamageForChaining(LivingEntity instance, ServerLevel level, DamageSource source, float dmg, Operation<Void> original) {
        ChainingComponent component = ChainingComponent.KEY.get(instance);
        List<LivingEntity> entities = component.getAllChainedTo();
        if (!entities.isEmpty()) {
            dmg /= (entities.size() + 1);
            for (LivingEntity entity : entities) {
                List<LivingEntity> livingEntities = ChainingComponent.KEY.get(entity).getAllChainedTo();
                if (!livingEntities.isEmpty()) {
                    dmg /= (livingEntities.size() + 1);
                    for (LivingEntity livingEntity : livingEntities) {
                        original.call(livingEntity, level, source, dmg);
                    }
                }

                original.call(entity, level, source, dmg);
            }
        }

        original.call(instance, level, source, dmg);
    }

    @Inject(
            method = "checkTotemDeathProtection",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/DeathProtection;applyEffects(Lnet/minecraft/world/item/ItemStack;" +
                            "Lnet/minecraft/world/entity/LivingEntity;)V"
            )
    )
    private void allure$unchainIfTotemPop(DamageSource killingDamage, CallbackInfoReturnable<Boolean> cir) {
        ChainingComponent component = ChainingComponent.KEY.get(this);
        if (component.isChained()) {
            component.clearChains();
        }
    }
}
