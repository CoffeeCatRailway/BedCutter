package coffeecatrailway.bedcutter.common.command;

import coffeecatrailway.bedcutter.common.capability.HasHeadCapability;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
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
                .executes(source -> setValue(source, Collections.singleton(source.getSource().getEntity()), BoolArgumentType.getBool(source, "value")))
                .then(Commands.argument("target", EntityArgument.entities())
                        .executes((source) -> setValue(source, EntityArgument.getEntities(source, "target"), BoolArgumentType.getBool(source, "value"))))
        )).then(Commands.literal("get").executes(source -> getValue(source, source.getSource().getEntity()))
                .then(Commands.argument("target", EntityArgument.entity())
                        .executes((source) -> getValue(source, EntityArgument.getEntity(source, "target")))));

        dispatcher.register(builder);
    }

    private static int getValue(CommandContext<CommandSource> source, Entity entity)
    {
        if (entity instanceof LivingEntity)
        {
            entity.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> {
                if (handler.hasHead())
                    source.getSource().sendFeedback(new TranslationTextComponent("commands.has_head.get.has", entity.getDisplayName()), true);
                else
                    source.getSource().sendFeedback(new TranslationTextComponent("commands.has_head.get.hasnt", entity.getDisplayName()), true);
            });

            return 1;
        }
        return 0;
    }

    private static int setValue(CommandContext<CommandSource> source, Collection<? extends Entity> entities, boolean value)
    {
        boolean canContinue = entities.stream().anyMatch(entity -> entity instanceof LivingEntity);
        if (canContinue)
        {
            AtomicInteger i = new AtomicInteger();

            for (Entity entity : entities)
            {
                entity.getCapability(HasHeadCapability.HAS_HEAD_CAP).ifPresent(handler -> {
                    handler.setHasHead(value);
                    if (value)
                        source.getSource().sendFeedback(new TranslationTextComponent("commands.has_head.set.has", entity.getDisplayName()), true);
                    else
                        source.getSource().sendFeedback(new TranslationTextComponent("commands.has_head.set.hasnt", entity.getDisplayName()), true);
                    i.getAndIncrement();
                });
            }

            return i.get();
        }
        return 0;
    }
}