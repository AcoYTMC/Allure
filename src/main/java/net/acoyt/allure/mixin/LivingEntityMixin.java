package net.acoyt.allure.mixin;

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
}
