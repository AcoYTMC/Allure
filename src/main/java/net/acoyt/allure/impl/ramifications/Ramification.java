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

    default void onAttack(Player player, Entity target, ItemStack stack) {}
    default void onDamaged(Player player, DamageSource source, ItemStack stack) {}
}
