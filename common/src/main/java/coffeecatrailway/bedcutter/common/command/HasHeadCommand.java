package coffeecatrailway.bedcutter.common.command;

import coffeecatrailway.bedcutter.HasHeadHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.Collections;

/**
 * @author CoffeeCatRailway
 * Created: 23/06/2021
 */
public class HasHeadCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("hasHead").requires(source -> source.hasPermission(4));

        builder.then(Commands.literal("set").then(Commands.argument("value", BoolArgumentType.bool())
                .executes(source -> setValue(source, Collections.singleton(source.getSource().getEntity()), BoolArgumentType.getBool(source, "value")))
                .then(Commands.argument("target", EntityArgument.entities())
                        .executes((source) -> setValue(source, EntityArgument.getEntities(source, "target"), BoolArgumentType.getBool(source, "value"))))
        )).then(Commands.literal("get").executes(source -> getValue(source, source.getSource().getEntity()))
                .then(Commands.argument("target", EntityArgument.entity())
                        .executes((source) -> getValue(source, EntityArgument.getEntity(source, "target")))));

        dispatcher.register(builder);
    }

    private static int getValue(CommandContext<CommandSourceStack> source, Entity entity)
    {
        if (entity instanceof LivingEntity)
        {
            if (HasHeadHandler.hasHead((LivingEntity) entity))
                source.getSource().sendSuccess(new TranslatableComponent("commands.has_head.get.has", entity.getDisplayName()), true);
            else
                source.getSource().sendSuccess(new TranslatableComponent("commands.has_head.get.hasnt", entity.getDisplayName()), true);

            return 1;
        }
        return 0;
    }

    private static int setValue(CommandContext<CommandSourceStack> source, Collection<? extends Entity> entities, boolean hasHead)
    {
        boolean canContinue = entities.stream().anyMatch(entity -> entity instanceof LivingEntity);
        if (canContinue)
        {
            int i = 0;

            for (Entity entity : entities)
            {
                HasHeadHandler.setHasHead((LivingEntity) entity, hasHead);
                if (hasHead)
                    source.getSource().sendSuccess(new TranslatableComponent("commands.has_head.set.has", entity.getDisplayName()), true);
                else
                    source.getSource().sendSuccess(new TranslatableComponent("commands.has_head.set.hasnt", entity.getDisplayName()), true);
                i++;
            }

            return i;
        }
        return 0;
    }
}
