package net.acoyt.allure.impl;

import com.mojang.logging.LogUtils;
import net.acoyt.allure.impl.command.ListAlluresCommand;
import net.acoyt.allure.impl.index.AllureDataComponents;
import net.acoyt.allure.impl.index.AllureEntities;
import net.acoyt.allure.impl.index.AllureRamifications;
import net.acoyt.allure.impl.index.AllureRegistries;
import net.acoyt.allure.impl.util.AllureEntry;
import net.acoyt.allure.impl.util.data.AllureReloadListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;

/**
 * @author AcoYT
 */
public class Allure implements ModInitializer {
    public static final String MOD_ID = "allure";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceKey<Registry<AllureEntry>> ALLURE_KEY = ResourceKey.createRegistryKey(id("allures"));

    public void onInitialize() {
        /* Initialization */
        AllureDataComponents.init();
        AllureEntities.init();
        AllureRamifications.init();
        AllureRegistries.init();

        /* Reload Listeners */
        ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(id("allures"), new AllureReloadListener());

        DynamicRegistries.registerSynced(ALLURE_KEY, AllureEntry.DIRECT_CODEC);

        /* Commands */
        CommandRegistrationCallback.EVENT.register(ListAlluresCommand::register);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
