package net.acoyt.allure.impl.event.client;

import net.acoyt.acornlib.api.event.BetterItemTooltipEvent;
import net.acoyt.allure.impl.component.AllureComponent;
import net.acoyt.allure.impl.index.AllureDataComponents;
import net.acoyt.allure.impl.util.AllureEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

/**
 * @author AcoYT
 */
public class ItemTooltipsEvent implements BetterItemTooltipEvent {
    public void getTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> builder) {
        if (stack.has(AllureDataComponents.ALLURES)) {
            HolderSet<AllureEntry> holderSet = stack.getOrDefault(AllureDataComponents.ALLURES, AllureComponent.DEFAULT).entries();

            if (holderSet.isBound()) {
                for (Holder<AllureEntry> entryHolder : holderSet) {
                    AllureEntry entry = entryHolder.value();
                    builder.accept(entry.name().copy().withColor(0xFF6050ae));
                    builder.accept(Component.literal("- ").append(entry.description()).withStyle(ChatFormatting.DARK_GRAY));
                    //if (entryHolder != holderSet.get(holderSet.size())) builder.accept(Component.literal(""));
                }
            }
        }
    }
}
