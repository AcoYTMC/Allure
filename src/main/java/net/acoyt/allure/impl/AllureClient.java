package net.acoyt.allure.impl;

import net.acoyt.acornlib.api.event.BetterItemTooltipEvent;
import net.acoyt.allure.impl.cca.entity.ChainingComponent;
import net.acoyt.allure.impl.event.client.ItemTooltipsEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

/**
 * @author AcoYT
 */
@Environment(EnvType.CLIENT)
public class AllureClient implements ClientModInitializer {
    public static final RenderStateDataKey<List<ChainingComponent.ChainedEntry>> CHAINED = RenderStateDataKey.create();
    public static final RenderStateDataKey<LivingEntity> ENTITY = RenderStateDataKey.create();

    public void onInitializeClient() {
        BetterItemTooltipEvent.EVENT.register(new ItemTooltipsEvent());
    }
}
