package net.acoyt.allure.impl.ramifications;

import net.acoyt.allure.impl.cca.entity.ChainingComponent;
import net.acoyt.allure.impl.util.AllureUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * @author AcoYT
 */
public class ChainingRamification implements Ramification {
    private final float chance;
    private final double range;

    public ChainingRamification(float chance, double range) {
        this.chance = chance;
        this.range = range;
    }

    public void onAttack(Player player, Entity target, ItemStack stack) {
        if (player.getRandom().nextFloat() > chance && target instanceof LivingEntity living && AllureUtil.isCritting(player, target)) {
            List<LivingEntity> entities = player.level().getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(player.getOnPos().atY(player.getBlockY())).inflate(range),
                    EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(e -> e != player)
            );

            entities.forEach(entity -> ChainingComponent.KEY.get(entity).addChainedEntries(living));
            ChainingComponent.KEY.get(living).addChainedEntries(entities.toArray(LivingEntity[]::new));
        }
    }
}
