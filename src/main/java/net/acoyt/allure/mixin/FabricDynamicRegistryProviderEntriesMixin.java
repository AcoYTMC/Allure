package net.acoyt.allure.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.acoyt.allure.impl.Allure;
import net.acoyt.allure.impl.util.AllureEntry;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryValidator;
import org.jetbrains.annotations.Unmodifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KaboomRoads
 */
@Mixin(value = FabricDynamicRegistryProvider.Entries.class, remap = false)
public abstract class FabricDynamicRegistryProviderEntriesMixin {
    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/fabricmc/fabric/api/event/registry/DynamicRegistries;getWorldRegistries()Ljava/util/List;",
                    remap = false
            ),
            remap = false
    )
    private @Unmodifiable List<RegistryDataLoader.RegistryData<?>> allure$wrapDynamicRegistries(Operation<List<RegistryDataLoader.RegistryData<?>>> original) {
        List<RegistryDataLoader.RegistryData<?>> dynamicRegistries = new ArrayList<>(original.call());
        dynamicRegistries.add(new RegistryDataLoader.RegistryData<>(Allure.ALLURE_KEY, AllureEntry.DIRECT_CODEC, RegistryValidator.none()));
        return dynamicRegistries;
    }
}
