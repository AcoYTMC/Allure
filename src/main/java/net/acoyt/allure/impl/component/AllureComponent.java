package net.acoyt.allure.impl.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.acoyt.allure.impl.util.AllureEntry;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * @author AcoYT
 */
public record AllureComponent(HolderSet<AllureEntry> entries) {
    public static final AllureComponent DEFAULT = new AllureComponent(HolderSet.empty());

    public static final Codec<AllureComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AllureEntry.LIST_CODEC.fieldOf("entries").forGetter(AllureComponent::entries)
    ).apply(instance, AllureComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AllureComponent> STREAM_CODEC = StreamCodec.composite(
            AllureEntry.LIST_STREAM_CODEC, AllureComponent::entries,
            AllureComponent::new
    );
}
