package net.acoyt.allure.data.provider.lang;

import net.acoyt.acornlib.api.template.OrganizedLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author AcoYT
 */
public class AllureLanguageProvider extends OrganizedLanguageProvider {
    public AllureLanguageProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, registryLookup);
    }

    public void generateTexts(HolderLookup.Provider provider, TranslationBuilder builder) {
        builder.add("allure.allure.chaining", "Chaining");
        generateMultilineTexts(builder, "allure.allure.chaining.description",
                "Has a 50% chance to chain a cluster", "of mobs together when critting."
        );
    }

    private static void generateMultilineTexts(TranslationBuilder builder, String root, String... values) {
        List<String> strings = Arrays.asList(values);
        for (int i = 0; i < strings.size(); i++) {
            builder.add(root + "." + i, values[i]);
        }
    }
}
