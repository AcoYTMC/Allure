package net.acoyt.allure.impl.index;

import net.acoyt.allure.impl.Allure;
import net.acoyt.allure.impl.ramifications.Ramification;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/**
 * @author AcoYT
 */
public interface AllureRegistries {
    ResourceKey<Registry<Ramification>> ramificationKey = ResourceKey.createRegistryKey(Allure.id("ramification"));
    Registry<Ramification> RAMIFICATIONS = FabricRegistryBuilder.create(ramificationKey)
            .attribute(RegistryAttribute.MODDED)
            .buildAndRegister();

    static void init() {}
}
