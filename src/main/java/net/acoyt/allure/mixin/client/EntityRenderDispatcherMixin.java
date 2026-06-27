package net.acoyt.allure.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.acoyt.allure.impl.Allure;
import net.acoyt.allure.impl.AllureClient;
import net.acoyt.allure.impl.cca.entity.ChainingComponent;
import net.acoyt.allure.impl.client.ChainRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AcoYT
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Unique private static final Identifier CHAINING_TEXTURE = Allure.id("textures/entity/chaining.png");

    @WrapOperation(
            method = "submit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;" +
                            "Lnet/minecraft/client/renderer/SubmitNodeCollector;" +
                            "Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"
            )
    )
    private <T extends Entity, S extends EntityRenderState> void allure$renderChains(EntityRenderer<T, S> instance, S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, Operation<Void> original) {
        allure$renderChainsImpl(state, poseStack, submitNodeCollector);
        original.call(instance, state, poseStack, submitNodeCollector, camera);
    }

    @Unique
    private <S extends EntityRenderState> void allure$renderChainsImpl(S state, PoseStack poseStack, SubmitNodeCollector collector) {
        Minecraft mc = Minecraft.getInstance();
        float tickProgress = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);

        List<ChainingComponent.ChainedEntry> entries = state.getDataOrDefault(AllureClient.CHAINED, new ArrayList<>());
        if (!entries.isEmpty()) {
            LivingEntity self = state.getData(AllureClient.ENTITY);
            if (self == null) return;

            Vec3 start = self.getPosition(tickProgress).add(0.0F, self.getBoundingBox().getYsize() / 2.0F, 0.0F);

            for (ChainingComponent.ChainedEntry entry : entries) {
                LivingEntity living = entry.getChainedToFromLevel(Minecraft.getInstance().level);
                if (living == null || !entry.isSource()) continue;

                Vec3 end = living.getPosition(tickProgress).add(0.0F, living.getBoundingBox().getYsize() / 2.0F, 0.0F);

                poseStack.pushPose();

                poseStack.scale(-1.0F, -1.0F, -1.0F);
                poseStack.translate(0.0F, -1.0F, 0.0F);

                collector.submitCustomGeometry(poseStack,
                        RenderTypes.entityTranslucent(CHAINING_TEXTURE, true),
                        new ChainRenderer(
                                start.add(0, self.getBoundingBox().getYsize() / 2.0F, 0),
                                end.add(0, living.getBoundingBox().getYsize() / 2.0F, 0),
                                Math.sin(entry.getTimeLeft())
                        )
                );

                collector.submitCustomGeometry(poseStack,
                        RenderTypes.eyes(CHAINING_TEXTURE),
                        new ChainRenderer(
                                start.add(0, self.getBoundingBox().getYsize() / 2.0F, 0),
                                end.add(0, living.getBoundingBox().getYsize() / 2.0F, 0),
                                Math.sin(entry.getTimeLeft())
                        )
                );

                poseStack.popPose();
            }
        }
    }
}
