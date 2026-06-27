package net.acoyt.allure.impl.ramifications;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * @author AcoYT
 */
public interface SoulHolder {
    default boolean shouldSpawnSoul(Player player, Entity target) {
        return false;
    }

    default void logic(Player player, Entity target) {
        Vec3 pos = target.position();
        if (player.level() instanceof ServerLevel serverLevel) {
            //serverLevel.sendParticles(
            //        new SoulParticleOptions(player.getId(), player.position()),
            //        pos.x, pos.y, pos.z,
            //        1,
            //        0.0F, 0.0F, 0.0F,
            //        0.1F
            //);
        }
    }

    default float maxAmountMultiplier() {
        return 1.0F;
    }
}
