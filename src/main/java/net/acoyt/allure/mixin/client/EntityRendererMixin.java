package net.acoyt.allure.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.acoyt.allure.impl.cca.entity.ChainingComponent;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author AcoYT
 */
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    @WrapMethod(method = "affectedByCulling")
    private boolean allure$renderIfChained(T entity, Operation<Boolean> original) {
        if (entity instanceof LivingEntity living) {
            return original.call(entity) && !ChainingComponent.KEY.get(living).isChained();
        }

        return original.call(entity);
    }

//    @WrapMethod(method = "shouldRender")
//    private boolean allure$renderIfChained(T entity, Frustum culler, double camX, double camY, double camZ, Operation<Boolean> original) {
//        if (entity instanceof LivingEntity living) {
//            return original.call(entity, culler, camX, camY, camZ) || ChainingComponent.KEY.get(living).isChained();
//        }
//
//        return original.call(entity, culler, camX, camY, camZ);
//    }
}
