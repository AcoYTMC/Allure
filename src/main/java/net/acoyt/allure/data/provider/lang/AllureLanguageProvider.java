package net.acoyt.allure.data.provider.lang;

import net.acoyt.acornlib.api.template.OrganizedLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;

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
        builder.add("allure.allure.chaining.description", "Has a 50% chance to chain a cluster of mobs together when critting.");
    }
}
