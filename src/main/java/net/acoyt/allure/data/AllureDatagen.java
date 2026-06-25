package net.acoyt.allure.data;

import net.acoyt.allure.data.provider.AllureDynamicRegistryProvider;
import net.acoyt.allure.data.provider.lang.AllureLanguageProvider;
import net.acoyt.allure.impl.Allure;
import net.acoyt.allure.impl.index.data.AllureEntries;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;

/**
 * @author AcoYT
 */
public class AllureDatagen implements DataGeneratorEntrypoint {
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(AllureLanguageProvider::new);

        pack.addProvider(AllureDynamicRegistryProvider::new);
    }

    public void buildRegistry(RegistrySetBuilder builder) {
        builder.add(Allure.ALLURE_KEY, AllureEntries.BUILDER::bootstrap);
    }
}
