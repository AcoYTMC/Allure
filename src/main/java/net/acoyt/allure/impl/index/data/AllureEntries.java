package net.acoyt.allure.impl.index.data;

import net.acoyt.acornlib.api.builder.KeyedBuilder;
import net.acoyt.allure.impl.Allure;
import net.acoyt.allure.impl.util.AllureEntry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

/**
 * @author AcoYT
 */
public interface AllureEntries {
    KeyedBuilder<AllureEntry> BUILDER = new KeyedBuilder<>(Allure.MOD_ID, Allure.ALLURE_KEY);

    ResourceKey<AllureEntry> CHAINING = BUILDER.register("chaining", context -> {
        HolderGetter<Item> itemHolder = context.lookup(Registries.ITEM);

        return new AllureEntry(
                Component.translatable("allure.allure.chaining"),
                Component.translatable("allure.allure.chaining.description"),
                itemHolder.getOrThrow(ItemTags.SWORDS)
        );
    });
}
