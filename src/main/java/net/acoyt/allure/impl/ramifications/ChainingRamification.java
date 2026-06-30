package net.acoyt.allure.impl.ramifications;

import net.acoyt.allure.impl.cca.entity.ChainingComponent;
import net.acoyt.allure.impl.cca.entity.ChainingComponent.ChainedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

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
        if (player.getRandom().nextFloat() > chance && target instanceof LivingEntity living && !player.getCooldowns().isOnCooldown(stack)) {
            List<LivingEntity> entities = player.level().getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(player.getOnPos().atY(player.getBlockY())).inflate(range),
                    this.getNormalPredicate().and(e -> e != player && e != living && ChainingComponent.isValidEntity(e) && !ChainingComponent.KEY.get(e).isChained())
            );

            if (!entities.isEmpty()) {
                int color = DyeColor.values()[player.getRandom().nextInt(DyeColor.values().length)].getTextColor();

                entities.forEach(entity -> ChainingComponent.KEY.get(entity).addChainedEntries(new ChainedEntry(color, living, true, ChainingComponent.DEFAULT_TIME)));
                ChainingComponent.KEY.get(living).addChainedEntries(entities.stream().map(entity -> new ChainedEntry(color, entity, false, ChainingComponent.DEFAULT_TIME)).toArray(ChainedEntry[]::new));

                //player.sendOverlayMessage(Component.literal("Added " + entities.size() + " entries to entity " + living.getDisplayName().getString()));
                player.getCooldowns().addCooldown(stack, 20);
            }
        }
    }

    public Predicate<LivingEntity> getNormalPredicate() {
        return entity -> !(entity instanceof Player player && (entity.isSpectator() || player.isCreative()));
    }
}
