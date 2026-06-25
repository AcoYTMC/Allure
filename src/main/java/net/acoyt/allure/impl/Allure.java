package net.acoyt.allure.impl;

import com.mojang.logging.LogUtils;
import net.acoyt.allure.impl.index.AllureDataComponents;
import net.acoyt.allure.impl.util.AllureEntry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.slf4j.Logger;

/**
 * @author AcoYT
 */
public class Allure implements ModInitializer {
    public static final String MOD_ID = "allure";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceKey<Registry<AllureEntry>> ALLURE_KEY = ResourceKey.createRegistryKey(id("allures"));

    public void onInitialize() {
        AllureDataComponents.init();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
