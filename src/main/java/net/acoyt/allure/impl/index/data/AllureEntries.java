package net.acoyt.allure.impl.index.data;

import net.acoyt.acornlib.api.builder.KeyedBuilder;
import net.acoyt.allure.impl.Allure;
import net.acoyt.allure.impl.util.AllureEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;

/**
 * @author AcoYT
 */
public interface AllureEntries {
    KeyedBuilder<AllureEntry> BUILDER = new KeyedBuilder<>(Allure.MOD_ID, Allure.ALLURE_KEY);

    ResourceKey<AllureEntry> CHAINING = BUILDER.register("chaining", new AllureEntry(
            Component.translatable("allure.allure.chaining"),
            Component.translatable("allure.allure.chaining.description")
    ));
}
