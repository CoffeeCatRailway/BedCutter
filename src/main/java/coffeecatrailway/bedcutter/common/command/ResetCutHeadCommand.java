package coffeecatrailway.bedcutter.common.command;

import coffeecatrailway.bedcutter.common.block.CutterBedBlock;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.util.Constants;

import java.util.Collection;
import java.util.Collections;

/**
 * @author CoffeeCatRailway
 * Created: 22/10/2020
 */
public class ResetCutHeadCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("resetCutHead").requires(source -> source.hasPermissionLevel(4));

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
        CompoundNBT playerData = player.getPersistentData();
        if (!playerData.contains(CutterBedBlock.HEAD_CUT_TAG, Constants.NBT.TAG_BYTE))
            playerData.putBoolean(CutterBedBlock.HEAD_CUT_TAG, false);

        if (playerData.getBoolean(CutterBedBlock.HEAD_CUT_TAG))
            source.getSource().sendFeedback(new TranslationTextComponent("commands.resetcuthead.get.has", player.getDisplayName()), true);
        else
            source.getSource().sendFeedback(new TranslationTextComponent("commands.resetcuthead.get.hasnt", player.getDisplayName()), true);

        return 1;
    }

    private static int setValue(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players, boolean value)
    {
        int i = 0;

        for (ServerPlayerEntity player : players)
        {
            CompoundNBT playerData = player.getPersistentData();
            if (!playerData.contains(CutterBedBlock.HEAD_CUT_TAG, Constants.NBT.TAG_BYTE))
                playerData.putBoolean(CutterBedBlock.HEAD_CUT_TAG, false);

            if (playerData.getBoolean(CutterBedBlock.HEAD_CUT_TAG) != value)
            {
                playerData.putBoolean(CutterBedBlock.HEAD_CUT_TAG, value);
                if (value)
                    source.getSource().sendFeedback(new TranslationTextComponent("commands.resetcuthead.set.has", player.getDisplayName()), true);
                else
                    source.getSource().sendFeedback(new TranslationTextComponent("commands.resetcuthead.set.hasnt", player.getDisplayName()), true);
                i++;
            }
        }

        return i;
    }
}