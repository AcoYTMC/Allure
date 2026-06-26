package net.acoyt.allure.impl.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/**
 * @author AcoYT
 */
public class AllureUtil {
    public static boolean isCritting(Entity entity, Entity target) {
        if (!(entity instanceof Player player)) return false;
        return player.getAttackStrengthScale(0.5F) > 0.9F && player.canCriticalAttack(target);
    }
}
