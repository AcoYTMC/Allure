package net.acoyt.allure.data.provider;

import net.acoyt.allure.impl.Allure;
import net.acoyt.allure.impl.index.data.AllureEntries;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

/**
 * @author AcoYT
 */
public class AllureDynamicRegistryProvider extends FabricDynamicRegistryProvider {
    public AllureDynamicRegistryProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public void configure(HolderLookup.Provider provider, Entries entries) {
        AllureEntries.BUILDER.addEntries(provider, entries);
    }

    public String getName() {
        return Allure.MOD_ID + "_dynamic";
    }
}
