package net.acoyt.allure.data.provider;

import net.acoyt.allure.impl.Allure;
import net.acoyt.allure.impl.index.data.AllureEntries;
import net.acoyt.allure.impl.util.AllureEntry;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryValidator;

import java.util.concurrent.CompletableFuture;

/**
 * @author AcoYT
 */
public class AllureDynamicRegistryProvider extends FabricDynamicRegistryProvider {
    public AllureDynamicRegistryProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public void configure(HolderLookup.Provider provider, Entries entries) {
        entries.queuedEntries.put(Allure.ALLURE_KEY.identifier(), RegistryEntries.create(provider, new RegistryDataLoader.RegistryData<>(Allure.ALLURE_KEY, AllureEntry.CODEC, RegistryValidator.none())));
        AllureEntries.BUILDER.addEntries(provider, entries);
    }

    public String getName() {
        return Allure.MOD_ID + "_dynamic";
    }
}
