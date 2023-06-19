package com.feintha.dpu.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

import static com.feintha.dpu.commands.StringCommand.DATA_TYPE.*;

@SuppressWarnings("rawtypes")
public class StringCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access){
        LiteralArgumentBuilder<ServerCommandSource> _equals;
        LiteralArgumentBuilder<ServerCommandSource> _split;
        LiteralArgumentBuilder<ServerCommandSource> _concat;
        dispatcher.register(CommandManager.literal("string")
                .then((_equals = CommandManager.literal("equals"))
                        .then(addLocationTypes(_equals, (compound, context) -> {
                            var bl = compound.getString("left").contentEquals(compound.getString("right")) ? 1 : 0;
                            return bl;
                        }))
                )
                .then(_split = CommandManager.literal("split"))
                .then(_concat = CommandManager.literal("concat"))
        );
//            .then(CommandManager.literal("split")
//                .then(CommandManager.literal("storage").then(CommandManager.argument("location", IdentifierArgumentType.identifier()).then(CommandManager.argument("path", NbtPathArgumentType.nbtPath()).then(CommandManager.argument("by",StringArgumentType.string()).executes(StringCommand::string_split_storage)))))
//                .then(CommandManager.literal("entity").then(CommandManager.argument("entity", EntityArgumentType.entity()).then(CommandManager.argument("path", NbtPathArgumentType.nbtPath()).then(CommandManager.argument("by",StringArgumentType.string()).executes(StringCommand::string_split_entity)))))
//            )
//            .then(CommandManager.literal("concat")
//                    .then(CommandManager.literal("storage").then(CommandManager.argument("location", IdentifierArgumentType.identifier()).then(CommandManager.argument("path", NbtPathArgumentType.nbtPath()).then(CommandManager.argument("with",StringArgumentType.string()).executes(StringCommand::string_split_storage)))))
//                    .then(CommandManager.literal("entity").then(CommandManager.argument("entity", EntityArgumentType.entity()).then(CommandManager.argument("path", NbtPathArgumentType.nbtPath()).then(
//                            CommandManager.literal("const").then(CommandManager.argument("with", StringArgumentType.string()))).executes(StringCommand::string_split_entity)))))
//            )
//        );
    }
    static class DATA_TYPE<T extends ArgumentType> {
        public static DATA_TYPE<IdentifierArgumentType> STORAGE = new DATA_TYPE<>(IdentifierArgumentType.identifier(), "storage");
        public static DATA_TYPE<StringArgumentType> CONST = new DATA_TYPE<>(StringArgumentType.string(), "const");
        public static DATA_TYPE<EntityArgumentType> ENTITY = new DATA_TYPE<>(EntityArgumentType.entity(), "entity");
        public static DATA_TYPE<BlockPosArgumentType> BLOCK = new DATA_TYPE<>(BlockPosArgumentType.blockPos(), "block");

        DATA_TYPE(T var, String name) {
            this.var = var;
            this.name = name;
        }
        public String name;
        public T var;
    }

    public static ArgumentBuilder<ServerCommandSource, ?> addLocationTypes(ArgumentBuilder<ServerCommandSource, ?> root, final BiFunction<NbtCompound, CommandContext<ServerCommandSource>, Integer> command) {
        ArgumentBuilder<ServerCommandSource, ?> root_e_L;
        ArgumentBuilder<ServerCommandSource, ?> root_b_L;
        ArgumentBuilder<ServerCommandSource, ?> root_c_L;
        ArgumentBuilder<ServerCommandSource, ?> root_s_L;
        var out =
                root
                        .then(CommandManager.literal("block").then(CommandManager.argument("location", BlockPosArgumentType.blockPos()).then((ArgumentBuilder<ServerCommandSource, ?>) (root_b_L = CommandManager.argument("path", NbtPathArgumentType.nbtPath())).then(addLocationTypesRight(root_b_L, BLOCK, command)))))
                        .then(CommandManager.literal("entity").then(CommandManager.argument("location", EntityArgumentType.entity()).then((ArgumentBuilder<ServerCommandSource, ?>) (root_e_L = CommandManager.argument("path", NbtPathArgumentType.nbtPath())).then(addLocationTypesRight(root_e_L, ENTITY, command)))))
                        .then(CommandManager.literal("const").then((root_c_L = CommandManager.argument("source", StringArgumentType.string()))).then(addLocationTypesRight(root_c_L, CONST, command)))
                        .then(CommandManager.literal("storage").then(CommandManager.argument("location", IdentifierArgumentType.identifier()).then((ArgumentBuilder<ServerCommandSource, ?>) (root_s_L = CommandManager.argument("path", NbtPathArgumentType.nbtPath())).then(addLocationTypesRight(root_s_L, STORAGE, command)))))
                ;
        return out;
    }
    @SuppressWarnings("unchecked")
    public static <T> ArgumentBuilder<ServerCommandSource, ?> addLocationTypesRight(ArgumentBuilder<ServerCommandSource, ?> root, DATA_TYPE type, final BiFunction<NbtCompound, CommandContext<ServerCommandSource>, Integer> command) {

        ArgumentBuilder<ServerCommandSource, ?> root_e_L;
        ArgumentBuilder<ServerCommandSource, ?> root_b_L;
        ArgumentBuilder<ServerCommandSource, ?> root_c_L;
        ArgumentBuilder<ServerCommandSource, ?> root_s_L;
        var out =
                root
                        .then(CommandManager.literal("block").then(CommandManager.argument("location2", BlockPosArgumentType.blockPos()).then((ArgumentBuilder<ServerCommandSource, ?>) (root_b_L = CommandManager.argument("path2", NbtPathArgumentType.nbtPath())).executes(context -> string_util_loc(type, DATA_TYPE.BLOCK, context, command)))))
                        .then(CommandManager.literal("entity").then(CommandManager.argument("location2", EntityArgumentType.entity()).then((ArgumentBuilder<ServerCommandSource, ?>) (root_e_L = CommandManager.argument("path2", NbtPathArgumentType.nbtPath())).executes(context -> string_util_loc(type, DATA_TYPE.ENTITY, context, command)))))
                        .then(CommandManager.literal("const").then((root_c_L = CommandManager.argument("dest", StringArgumentType.string()))).executes(context -> string_util_loc(type, DATA_TYPE.CONST, context, command)))
                        .then(CommandManager.literal("storage").then(CommandManager.argument("location2", IdentifierArgumentType.identifier()).then((ArgumentBuilder<ServerCommandSource, ?>) (root_s_L = CommandManager.argument("path2", NbtPathArgumentType.nbtPath())).executes(context -> string_util_loc(type, STORAGE, context, command)))))
                ;
        return out;
    }
    private static NbtElement getDataFromLocation(CommandContext<ServerCommandSource> context, BiFunction<CommandContext<ServerCommandSource>, Identifier, NbtCompound> consumer) throws CommandSyntaxException {
        Identifier location = IdentifierArgumentType.getIdentifier(context, "location");
        NbtPathArgumentType.NbtPath path = NbtPathArgumentType.getNbtPath(context, "path");
        var loc_data = consumer.apply(context, location);
        var p = path.get(loc_data);
        if (p.size() > 0) {
            String s = p.stream().findFirst().get().asString();
            if (loc_data == null) {
                context.getSource().sendError(Text.literal("Location " + location + " was not found."));
                throw new RuntimeException("Location " + path.toString() + " was not found in " +location + ". Ensure this is correct!");
            }
        }
        assert p.size() > 0;
        return p.stream().findFirst().get();
    }
    private static NbtElement getDataFromEntity(CommandContext<ServerCommandSource> context, BiFunction<CommandContext<ServerCommandSource>, Entity, NbtCompound> consumer) throws CommandSyntaxException {
        Entity location = EntityArgumentType.getEntity(context, "location");
        NbtPathArgumentType.NbtPath path = NbtPathArgumentType.getNbtPath(context, "path");
        var loc_data = consumer.apply(context, location);
        var p = path.get(loc_data);
        if (p.size() > 0) {
            String s = p.stream().findFirst().get().asString();
            if (loc_data == null) {
                context.getSource().sendError(Text.literal("Entity " + location + " was not found."));
                throw new RuntimeException("Data at " + path.toString() + " was not found in " +location + ". Ensure this is correct!");
            }
        }
        assert p.size() > 0;
        return p.stream().findFirst().get();
    }
    private static NbtElement getDataFromBlock(CommandContext<ServerCommandSource> context, BiFunction<CommandContext<ServerCommandSource>, BlockEntity, NbtCompound> consumer) throws CommandSyntaxException {
        BlockPos pos = BlockPosArgumentType.getBlockPos(context, "location");
        NbtPathArgumentType.NbtPath path = NbtPathArgumentType.getNbtPath(context, "path");
        var location =context.getSource().getWorld().getBlockEntity(pos);
        var loc_data = consumer.apply(context, location);
        var p = path.get(loc_data);
        if (p.size() > 0) {
            String s = p.stream().findFirst().get().asString();
            if (loc_data == null) {
                context.getSource().sendError(Text.literal("Location " + location + " was not found."));
                throw new RuntimeException("Block at "+pos.toShortString()+" did not have any data at " + path.toString() + " was not found in " +location + ". Ensure this is correct!");
            }
        }
        assert p.size() > 0;
        return p.stream().findFirst().get();
    }

    private static NbtElement getDataFromLocationR(CommandContext<ServerCommandSource> context, BiFunction<CommandContext<ServerCommandSource>, Identifier, NbtCompound> consumer) throws CommandSyntaxException {
        Identifier location = IdentifierArgumentType.getIdentifier(context, "location2");
        NbtPathArgumentType.NbtPath path = NbtPathArgumentType.getNbtPath(context, "path2");
        var loc_data = consumer.apply(context, location);
        var p = path.get(loc_data);
        if (p.size() > 0) {
            String s = p.stream().findFirst().get().asString();
            if (loc_data == null) {
                context.getSource().sendError(Text.literal("Location " + location + " was not found."));
                throw new RuntimeException("Location " + path.toString() + " was not found in " +location + ". Ensure this is correct!");
            }
        }
        assert p.size() > 0;
        return p.stream().findFirst().get();
    }
    private static NbtElement getDataFromEntityR(CommandContext<ServerCommandSource> context, BiFunction<CommandContext<ServerCommandSource>, Entity, NbtCompound> consumer) throws CommandSyntaxException {
        Entity location = EntityArgumentType.getEntity(context, "location2");
        NbtPathArgumentType.NbtPath path = NbtPathArgumentType.getNbtPath(context, "path2");
        var loc_data = consumer.apply(context, location);
        var p = path.get(loc_data);
        if (p.size() > 0) {
            String s = p.stream().findFirst().get().asString();
            if (loc_data == null) {
                context.getSource().sendError(Text.literal("Entity " + location + " was not found."));
                throw new RuntimeException("Data at " + path.toString() + " was not found in " +location + ". Ensure this is correct!");
            }
        }
        assert p.size() > 0;
        return p.stream().findFirst().get();
    }
    private static NbtElement getDataFromBlockR(CommandContext<ServerCommandSource> context, BiFunction<CommandContext<ServerCommandSource>, BlockEntity, NbtCompound> consumer) throws CommandSyntaxException {
        BlockPos pos = BlockPosArgumentType.getBlockPos(context, "location2");
        NbtPathArgumentType.NbtPath path = NbtPathArgumentType.getNbtPath(context, "path2");
        var location =context.getSource().getWorld().getBlockEntity(pos);
        var loc_data = consumer.apply(context, location);
        var p = path.get(loc_data);
        if (p.size() > 0) {
            String s = p.stream().findFirst().get().asString();
            if (loc_data == null) {
                context.getSource().sendError(Text.literal("Location " + location + " was not found."));
                throw new RuntimeException("Block at "+pos.toShortString()+" did not have any data at " + path.toString() + " was not found in " +location + ". Ensure this is correct!");
            }
        }
        assert p.size() > 0;
        return p.stream().findFirst().get();
    }
    private static int string_util_loc(DATA_TYPE origin, DATA_TYPE second, CommandContext<ServerCommandSource> context, BiFunction<NbtCompound, CommandContext<ServerCommandSource>, Integer> consumer) throws CommandSyntaxException {
        NbtCompound c = new NbtCompound();
        handleCasesLeft(origin,context,c);
        handleCasesRight(second,context,c);
        return consumer.apply(c, context);
    }
    private static void handleCasesLeft(DATA_TYPE origin, CommandContext<ServerCommandSource> context, NbtCompound c) throws CommandSyntaxException {
        if (DATA_TYPE.CONST.equals(origin)) {
            c.putString("left", StringArgumentType.getString(context, "source"));
        } else if (STORAGE.equals(origin)) {
            var e = getDataFromLocation(context, (context1, identifier) -> context1.getSource().getServer().getDataCommandStorage().get(identifier));
            c.putString("left", e.asString());
        } else if (DATA_TYPE.ENTITY.equals(origin)) {
            var e = getDataFromEntity(context, (context1, entity) -> {
                NbtCompound e_C = new NbtCompound();
                entity.writeNbt(e_C);
                return e_C;
            });
            c.putString("left", e.asString());
        } else if (DATA_TYPE.BLOCK.equals(origin)) {
            var e = getDataFromBlock(context, (context1, block) -> {
                NbtCompound e_C = new NbtCompound();
                block.readNbt(e_C);
                return e_C;
            });
            c.putString("left", e.asString());
        }
    }
    private static void handleCasesRight(DATA_TYPE origin, CommandContext<ServerCommandSource> context, NbtCompound c) throws CommandSyntaxException {
        if (DATA_TYPE.CONST.equals(origin)) {
            c.putString("right", StringArgumentType.getString(context, "dest"));
        } else if (STORAGE.equals(origin)) {
            var e = getDataFromLocationR(context, (context1, identifier) -> context1.getSource().getServer().getDataCommandStorage().get(identifier));
            c.putString("right", e.asString());
        } else if (DATA_TYPE.ENTITY.equals(origin)) {
            var e = getDataFromEntityR(context, (context1, entity) -> {
                NbtCompound e_C = new NbtCompound();
                entity.writeNbt(e_C);
                return e_C;
            });
            c.putString("right", e.asString());
        } else if (DATA_TYPE.BLOCK.equals(origin)) {
            var e = getDataFromBlockR(context, (context1, block) -> {
                NbtCompound e_C = new NbtCompound();
                block.readNbt(e_C);
                return e_C;
            });
            c.putString("right", e.asString());
        }
    }

}
