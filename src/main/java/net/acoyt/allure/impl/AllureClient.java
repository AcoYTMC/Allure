package net.acoyt.allure.impl;

import net.acoyt.acornlib.api.event.BetterItemTooltipEvent;
import net.acoyt.allure.impl.event.client.ItemTooltipsEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * @author AcoYT
 */
@Environment(EnvType.CLIENT)
public class AllureClient implements ClientModInitializer {
    public void onInitializeClient() {
        BetterItemTooltipEvent.EVENT.register(new ItemTooltipsEvent());
    }
}
