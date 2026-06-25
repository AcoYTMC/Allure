package net.acoyt.allure.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;

public class AllureDatagen implements DataGeneratorEntrypoint {
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
	}

	public void buildRegistry(RegistrySetBuilder builder) {
		//
	}
}
