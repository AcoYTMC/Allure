package net.acoyt.allure.impl.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.acoyt.allure.impl.util.AllureEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

/**
 * @author AcoYT
 */
public record AllureComponent(HolderSet<AllureEntry> entries) implements TooltipProvider {
    public static final Codec<AllureComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AllureEntry.LIST_CODEC.fieldOf("entries").forGetter(AllureComponent::entries)
    ).apply(instance, AllureComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AllureComponent> STREAM_CODEC = StreamCodec.composite(
            AllureEntry.LIST_STREAM_CODEC, AllureComponent::entries,
            AllureComponent::new
    );

    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        HolderLookup.Provider provider = context.registries();

        for (Holder<AllureEntry> entry : entries) {
            consumer.accept(entry.value().name().copy().withColor(0xFF6050ae));
            consumer.accept(Component.literal("- ").append(entry.value().description()).withStyle(ChatFormatting.DARK_GRAY));
            if (entry != entries.get(entries.size())) consumer.accept(Component.literal(""));
        }
    }
}
