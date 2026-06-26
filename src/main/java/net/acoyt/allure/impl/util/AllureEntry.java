package net.acoyt.allure.impl.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.acoyt.allure.impl.Allure;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;

/**
 * @author AcoYT
 */
public record AllureEntry(Component name, Component description) {
    public static final Codec<AllureEntry> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("name").forGetter(AllureEntry::name),
            ComponentSerialization.CODEC.fieldOf("description").forGetter(AllureEntry::description)
    ).apply(instance, AllureEntry::new));

    public static final Codec<Holder<AllureEntry>> CODEC = RegistryFileCodec.create(Allure.ALLURE_KEY, DIRECT_CODEC);
    public static final Codec<HolderSet<AllureEntry>> LIST_CODEC = RegistryCodecs.homogeneousList(Allure.ALLURE_KEY);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<AllureEntry>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Allure.ALLURE_KEY);
    public static final StreamCodec<RegistryFriendlyByteBuf, HolderSet<AllureEntry>> LIST_STREAM_CODEC = ByteBufCodecs.holderSet(Allure.ALLURE_KEY);
}
