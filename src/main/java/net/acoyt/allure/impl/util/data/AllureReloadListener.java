package net.acoyt.allure.impl.util.data;

import com.mojang.serialization.JsonOps;
import net.acoyt.allure.impl.util.AllureEntry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.SimpleReloadListener;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author AcoYT
 */
public class AllureReloadListener extends SimpleReloadListener<Map<Identifier, AllureEntry>> {
    public static final Map<Identifier, AllureEntry> ALLURES = new HashMap<>();
    public static final Map<AllureEntry, Identifier> BACKWARDS = new HashMap<>();

    public Map<Identifier, AllureEntry> prepare(SharedState state) {
        Map<Identifier, AllureEntry> raw = new HashMap<>();
        SimpleJsonResourceReloadListener.scanDirectory(state.resourceManager(), FileToIdConverter.json("allures"),
                state.get(ResourceLoader.REGISTRY_LOOKUP_KEY).createSerializationContext(JsonOps.INSTANCE),
                AllureEntry.DIRECT_CODEC, raw
        );

        return raw;
    }

    public void apply(Map<Identifier, AllureEntry> prepared, SharedState state) {
        ALLURES.clear();
        ALLURES.putAll(prepared);
        ALLURES.forEach((id, entry) -> BACKWARDS.put(entry, id));
    }
}
