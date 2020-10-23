package coffeecatrailway.bedcutter.common.command;

import coffeecatrailway.bedcutter.common.capability.HasHeadCapability;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author CoffeeCatRailway
 * Created: 22/10/2020
 */
public class HasHeadCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("hasHead").requires(source -> source.hasPermissionLevel(4));

        builder.then(Commands.literal("set").then(Commands.argument("value", BoolArgumentType.bool())
                .executes(source -> setValue(source, Collections.singleton(source.getSource().asPlayer()), BoolArgumentType.getBool(source, "value")))
                .then(Commands.argument("target", EntityArgument.players())
                        .executes((source) -> setValue(source, EntityArgument.getPlayers(source, "target"), BoolArgumentType.getBool(source, "value"))))
        )).then(Commands.literal("get").executes(source -> getValue(source, source.getSource().asPlayer()))
                .then(Commands.argument("target", EntityArgument.player())
                        .executes((source) -> getValue(source, EntityArgument.getPlayer(source, "target")))));

        dispatcher.register(builder);
    }

    private static int getValue(CommandContext<CommandSource> source, ServerPlayerEntity player)
    {
        player.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> {
            if (handler.hasHead())
                source.getSource().sendFeedback(new TranslationTextComponent("commands.has_head.get.has", player.getDisplayName()), true);
            else
                source.getSource().sendFeedback(new TranslationTextComponent("commands.has_head.get.hasnt", player.getDisplayName()), true);
        });

        return 1;
    }

    private static int setValue(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players, boolean value)
    {
        AtomicInteger i = new AtomicInteger();

        for (ServerPlayerEntity player : players)
        {
            player.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> {
                handler.setHasHead(value);
                if (value)
                    source.getSource().sendFeedback(new TranslationTextComponent("commands.has_head.set.has", player.getDisplayName()), true);
                else
                    source.getSource().sendFeedback(new TranslationTextComponent("commands.has_head.set.hasnt", player.getDisplayName()), true);
                i.getAndIncrement();
            });
        }

        return i.get();
    }
}