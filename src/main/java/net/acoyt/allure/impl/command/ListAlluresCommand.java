package net.acoyt.allure.impl.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.acoyt.allure.impl.util.data.AllureReloadListener;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import static net.minecraft.commands.Commands.literal;

/**
 * @author AcoYT
 */
public class ListAlluresCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ignoredContext, Commands.CommandSelection ignoredSelection) {
        dispatcher.register(literal("list_allures")
                .executes(context -> {
                    Component entries = Component.literal(AllureReloadListener.ALLURES.keySet()
                            .stream()
                            .map(id -> id.toString() + " ")
                            .toList()
                            .toString());

                    context.getSource().sendSuccess(() -> entries, false);
                    return Command.SINGLE_SUCCESS;
                })
        );
    }
}
