package com.feintha.dpu.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ExecuteCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class StringCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access){
        dispatcher.register(CommandManager.literal("string").then(CommandManager.literal("equals")
                .then(CommandManager.literal("storage").then(CommandManager.argument("location", IdentifierArgumentType.identifier()).then(CommandManager.argument("path", NbtPathArgumentType.nbtPath()).then(CommandManager.argument("str",StringArgumentType.string()).executes(StringCommand::string_equals_storage)))))
                .then(CommandManager.literal("entity").then(CommandManager.argument("entity", EntityArgumentType.entity()).then(CommandManager.argument("path", NbtPathArgumentType.nbtPath()).then(CommandManager.argument("str",StringArgumentType.string()).executes(StringCommand::string_equals_entity)))))
        ));
    }
    private static int string_equals_storage(CommandContext<ServerCommandSource> context) {
        Identifier location = IdentifierArgumentType.getIdentifier(context, "location");
        NbtPathArgumentType.NbtPath path = NbtPathArgumentType.getNbtPath(context, "path");
        String chk = StringArgumentType.getString(context, "str");
        var loc_data = context.getSource().getServer().getDataCommandStorage().get(location);
        if (loc_data == null) {
            context.getSource().sendError(Text.literal("Location " + location + " was not found."));
            return 0;
        }
        if (loc_data.isEmpty()) {return 0;}
        try {
            var p = path.get(loc_data);
            if (p.size() > 0) {
                String s = p.stream().findFirst().get().asString();
                System.out.println(s +" == " + chk);
                return s.contentEquals(chk) ? 1 : 0;
            }
        } catch (CommandSyntaxException e) {
            context.getSource().sendError(Text.literal("Something went wrong when trying to read!"));
        }


        return 0;

    }
    private static int string_equals_entity(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Entity location = EntityArgumentType.getEntity(context, "entity");
        NbtPathArgumentType.NbtPath path = NbtPathArgumentType.getNbtPath(context, "path");
        String chk = StringArgumentType.getString(context, "str");
        NbtCompound loc_data = new NbtCompound();
        location.writeNbt(loc_data);
        if (loc_data.isEmpty()) {return 0;}
        try {
            var p = path.get(loc_data);
            if (p.size() > 0) {
                String s = p.stream().findFirst().get().asString();
                System.out.println(s +" == " + chk);
                return s.contentEquals(chk) ? 1 : 0;
            }
        } catch (CommandSyntaxException e) {
            context.getSource().sendError(Text.literal("Something went wrong when trying to read!"));
        }


        return 0;

    }
}
