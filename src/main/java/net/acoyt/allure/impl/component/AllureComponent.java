package net.acoyt.allure.impl.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.acoyt.allure.impl.util.AllureEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author AcoYT
 */
public record AllureComponent(List<Holder<AllureEntry>> entries) implements TooltipProvider {
    public static final Codec<AllureComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AllureEntry.CODEC.listOf().fieldOf("entries").forGetter(AllureComponent::entries)
    ).apply(instance, AllureComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AllureComponent> STREAM_CODEC = StreamCodec.composite(
            AllureEntry.STREAM_CODEC.apply(ByteBufCodecs.list()), AllureComponent::entries,
            AllureComponent::new
    );

    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        HolderLookup.Provider provider = context.registries();

        for (Holder<AllureEntry> entry : entries) {
            consumer.accept(entry.value().name().copy().withColor(0xFF6050ae));
            consumer.accept(Component.literal("- ").append(entry.value().description()).withStyle(ChatFormatting.DARK_GRAY));
            if (entry != entries.getLast()) consumer.accept(Component.literal(""));
        }
    }
}
