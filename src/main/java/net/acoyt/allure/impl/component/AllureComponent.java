package net.acoyt.allure.impl.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.acoyt.allure.impl.index.AllureDataComponents;
import net.acoyt.allure.impl.ramifications.Ramification;
import net.acoyt.allure.impl.util.AllureEntry;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

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

    public static HolderSet<AllureEntry> getAllures(ItemStack stack) {
        return stack.getOrDefault(AllureDataComponents.ALLURES, DEFAULT).entries();
    }

    public static List<Ramification> getRamifications(ItemStack stack) {
        return new ArrayList<>(getAllures(stack).stream().map(entry -> entry.value().ramification()).toList());
    }
}
