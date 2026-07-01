package net.acoyt.allure.impl.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.acoyt.allure.impl.Allure;
import net.acoyt.allure.impl.ramifications.Ramification;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

import java.util.List;

/**
 * @author AcoYT
 */
public record AllureEntry(Component name, List<Component> description, HolderSet<Item> supportedItems, List<Ramification> ramifications, int color) {
    public static final Codec<AllureEntry> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("name").forGetter(AllureEntry::name),
            Codec.list(ComponentSerialization.CODEC).fieldOf("description").forGetter(AllureEntry::description),
            RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("supportedItems").forGetter(AllureEntry::supportedItems),
            Codec.list(Ramification.CODEC).fieldOf("ramifications").forGetter(AllureEntry::ramifications),
            ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color", 0xFFFFFF).forGetter(AllureEntry::color)
    ).apply(instance, AllureEntry::new));

    public AllureEntry(Component name, List<Component> description, HolderSet<Item> supportedItems, Ramification... ramifications) {
        this(name, description, supportedItems, List.of(ramifications), 0xFFFFFF);
    }

    public static final Codec<Holder<AllureEntry>> CODEC = RegistryFileCodec.create(Allure.ALLURE_KEY, DIRECT_CODEC);
    public static final Codec<HolderSet<AllureEntry>> LIST_CODEC = RegistryCodecs.homogeneousList(Allure.ALLURE_KEY);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<AllureEntry>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Allure.ALLURE_KEY);
    public static final StreamCodec<RegistryFriendlyByteBuf, HolderSet<AllureEntry>> LIST_STREAM_CODEC = ByteBufCodecs.holderSet(Allure.ALLURE_KEY);
}
