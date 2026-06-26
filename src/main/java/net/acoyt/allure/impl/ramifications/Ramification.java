package net.acoyt.allure.impl.ramifications;

import com.mojang.serialization.Codec;
import net.acoyt.allure.impl.index.AllureRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * @author AcoYT
 */
public interface Ramification {
    Codec<Ramification> CODEC = AllureRegistries.RAMIFICATIONS.byNameCodec();

    /**
     * Runs when attacking another entity, for the main hand item (or if the item is a spear, or kinetic weapon, it'll use that)
     * @param player The player attacking
     * @param target The entity being attacked
     * @param stack The stack the player is attacking with
     */
    default void onAttack(Player player, Entity target, ItemStack stack) {}

    /**
     * Runs when taking damage, for all player armor items
     * @param player The player being damaged
     * @param source The source of the damage
     * @param stack The equipped item being affected
     */
    default void onDamaged(Player player, DamageSource source, ItemStack stack) {}
}
