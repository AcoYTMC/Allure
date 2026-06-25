package net.acoyt.allure.impl.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;

/**
 * @author AcoYT
 */
public record AllureEntry(Component name, Component description) {
    public static final Codec<AllureEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("name").forGetter(AllureEntry::name),
            ComponentSerialization.CODEC.fieldOf("description").forGetter(AllureEntry::description)
    ).apply(instance, AllureEntry::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AllureEntry> STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.STREAM_CODEC, AllureEntry::name,
            ComponentSerialization.STREAM_CODEC, AllureEntry::description,
            AllureEntry::new
    );
}
