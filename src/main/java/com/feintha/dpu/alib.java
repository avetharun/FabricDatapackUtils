package com.feintha.dpu;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.zip.Adler32;
import java.util.zip.CRC32;

@SuppressWarnings("unused")
public class alib {
    public static <F,T> F getMixinField(T mixinType, String fieldName) {
        try {
            Field f = mixinType.getClass().getField(fieldName);
            //noinspection unchecked
            return (F) f.get(mixinType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static <F,T> F setMixinField(T mixinType, String fieldName, F value) {
        try {
            Field f = mixinType.getClass().getField(fieldName);
            f.set(mixinType, value);
            //noinspection unchecked
            return (F)f.get(mixinType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static <F,T> F setPrivateMixinField(T mixinType, String fieldName, F value) {
        try {
            Field f = mixinType.getClass().getField(fieldName);
            f.setAccessible(true);
            f.set(mixinType, value);
            //noinspection unchecked
            return (F)f.get(mixinType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static <F,T> F getPrivateMixinField(T mixinType, String fieldName) {
        try {
            Field f = mixinType.getClass().getDeclaredField(fieldName);
            //noinspection unchecked
            return (F) f.get(mixinType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> void runMixinMethod(T mixinType, String methodName, Object args) {
        try {
            Method f = mixinType.getClass().getMethod(methodName);
            f.invoke(mixinType);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> void runPrivateMixinMethod(T mixinType, String methodName, Object args) {
        try {
            Method f = mixinType.getClass().getDeclaredMethod(methodName);
            f.invoke(mixinType);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    public static float getRandomFloat(Random random, float minValue, float maxValue) {
        return minValue + random.nextFloat() * (maxValue - minValue);
    }
    public static <T extends Entity> List<T> getEntitiesOfTypeInRange(World world, BlockPos pos, double range, EntityType<T> type) {
        Predicate<Entity> filter = entity -> entity.getType() == type && entity.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) <= range * range;
        return world.getEntitiesByType(type, Box.of(Vec3d.of(pos),range, range, range), filter);
    }
    public static boolean isEntityNearBlock(Entity e, int radius, Block... blocks) {
        // Get the entity's position
        BlockPos entityPos = e.getBlockPos();

        // Check if any nearby block is a target block type
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = entityPos.add(x, y, z);
                    Block block = e.world.getBlockState(pos).getBlock();
                    for (Block b : blocks) {
                        System.out.println("check");
                        if (block == b) {
                            return true;
                        }
                    }
                }
            }
        }

        // No nearby block is a target block type
        return false;
    }
    /**
     * Calculate a 64 bits hash by combining CRC32 with Adler32.
     *
     * @param bytes a byte array
     * @return a hash number
     */
    public static long getHash64(byte[] bytes) {

        CRC32 crc32 = new CRC32();
        Adler32 adl32 = new Adler32();

        crc32.update(bytes);
        adl32.update(bytes);

        long crc = crc32.getValue();
        long adl = adl32.getValue();
        return ((crc << 32) | adl) + crc << 8;
    }
    public static boolean stackCustomModelDataEquals(@NotNull ItemStack stack, int data) {
        if (!stack.getOrCreateNbt().contains("CustomModelData")) {return false;}
        return stack.getOrCreateNbt().getInt("CustomModelData") == data;
    }
    public static long getHash64(String s) {
        return getHash64(s.getBytes(StandardCharsets.UTF_8));
    }
    public static long bitenable(long var, long nbit) {
        return (var) |= (1L <<(nbit));
    }
    public static long bitdisable(long var, long nbit) {
        return (var) &= (1L <<(nbit));
    }
    public static long bitflip(long var, long nbit) {
        return (var) ^= (1L <<(nbit));
    }
    public static boolean getbit(long var, long nbit) {
        return ((var) & (1L <<(nbit))) == 1;
    }
    public static long setbit(long var, long nbit, boolean value) {
        if (value) {
            return bitenable(var, nbit);
        } else {
            return bitdisable(var,nbit);
        }
    }
    public static boolean isBlockIn(BlockState source, TagKey<Block> tag) {
        return source.isIn(tag);
    }
    public static List<Pair<Identifier, Block>> GetAllBlocksInTag(TagKey<Block> tag) {
        List<Pair<Identifier, Block>> data = new ArrayList<>();
        Optional<RegistryEntryList.Named<Block>> init_BLOCKS = Registries.BLOCK.getEntryList(tag);
        init_BLOCKS.ifPresent(registryEntries -> registryEntries.forEach(entry -> {
            Identifier id = entry.getKey().get().getValue();
            Block block = entry.value();
            data.add(Pair.of(id,block));
        }));
        return data;
    }
    public static List<Pair<Identifier, Block>> GetAllBlocksInTagAnd(TagKey<Block> tag, Consumer<Pair<Identifier, Block>> onFound) {
        List<Pair<Identifier, Block>> data = new ArrayList<>();
        Optional<RegistryEntryList.Named<Block>> init_BLOCKS = Registries.BLOCK.getEntryList(tag);
        init_BLOCKS.ifPresent(registryEntries -> registryEntries.forEach(entry -> {
            Identifier id = entry.getKey().get().getValue();
            Block block = entry.value();
            data.add(Pair.of(id,block));
            onFound.accept(Pair.of(id,block));
        }));
        return data;
    }
    public static BlockPos getBlockPosFromArray(long[] a) {
        if (a.length < 3) {return BlockPos.ORIGIN;}
        return new BlockPos((int)a[0], (int)a[1], (int)a[2]);
    }
    public static long[] getBlockPosAsArray(BlockPos d) {
        if (d == null) {return new long[]{0, 0, 0};}
        return new long[]{d.getX(), d.getY(), d.getZ()};
    }
    public static Pair<Integer, Integer> XYPosFromOffset(int w, int offset) {
        assert w != 0;
        int x;
        int y;
        x = offset % w;    // % is the "modulo operator", the remainder of i / width;
        assert offset != 0;
        y = offset / w;    // where "/" is an integer division
        return Pair.of(x,y);
    }
    public static double lerp(double a, double b, float f) {
        return a + f * (b - a);
    }
    public static int mixRGB (int a, int b, float ratio) {
        if (ratio > 1f) {
            ratio = 1f;
        } else if (ratio < 0f) {
            ratio = 0f;
        }
        float iRatio = 1.0f - ratio;

        int aA = (a >> 24 & 0xff);
        int aR = ((a & 0xff0000) >> 16);
        int aG = ((a & 0xff00) >> 8);
        int aB = (a & 0xff);

        int bA = (b >> 24 & 0xff);
        int bR = ((b & 0xff0000) >> 16);
        int bG = ((b & 0xff00) >> 8);
        int bB = (b & 0xff);

        int A = (int)((aA * iRatio) + (bA * ratio));
        int R = (int)((aR * iRatio) + (bR * ratio));
        int G = (int)((aG * iRatio) + (bG * ratio));
        int B = (int)((aB * iRatio) + (bB * ratio));

        return A << 24 | R << 16 | G << 8 | B;
    }
}
