package com.feintha.dpu;

import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class DPUDataStorage {
    public static NbtCompound EventCompound = new NbtCompound();
    public static NbtCompound CommandReturnsCompound = new NbtCompound();
    public static NbtCompound CommandArgumentsCompound = new NbtCompound();
    public static void InitServer(MinecraftServer s) {
        s.getDataCommandStorage().set(new Identifier("dpu", "events"), EventCompound);
    }
    public static void PopEvent(MinecraftServer s){
        for (var key : EventCompound.getKeys()) {
            EventCompound.remove(key);
        }
    }
    public static void PopReturns(MinecraftServer s){
        for (var key : CommandReturnsCompound.getKeys()) {
            CommandReturnsCompound.remove(key);
        }
    }
    public static void PopArgs(MinecraftServer s){
        for (var key : CommandArgumentsCompound.getKeys()) {
            CommandArgumentsCompound.remove(key);
        }
    }
    public static void PushEvent(MinecraftServer s, String id, NbtCompound compound) {
        PopEvent(s);
        EventCompound.put(id, compound);
        s.getDataCommandStorage().set(new Identifier("dpu", "events"), EventCompound);
    }
    public static void PushCommandReturnVals(MinecraftServer s, String id, NbtCompound compound) {
        PopReturns(s);
        CommandReturnsCompound.put(id, compound);
        s.getDataCommandStorage().set(new Identifier("dpu", "returns"), CommandReturnsCompound);
    }
    public static NbtCompound putValueF(NbtCompound compound, float value) {
        compound.putFloat("float", value);
        return compound;
    }
    public static NbtCompound putValueB(NbtCompound compound, boolean value) {
        compound.putBoolean("boolean", value);
        return compound;
    }
    public static NbtCompound putValueL(NbtCompound compound, long value) {
        compound.putLong("long", value);
        return compound;
    }
    public static NbtCompound putValueS(NbtCompound compound, short value) {
        compound.putShort("short", value);
        return compound;
    }
    public static NbtCompound putValueD(NbtCompound compound, double value) {
        compound.putDouble("double", value);
        return compound;
    }
    public static NbtCompound putValueStr(NbtCompound compound, String value) {
        compound.putString("string", value);
        return compound;
    }
    public static NbtCompound putVec3d(NbtCompound compound, Vec3d vec3d) {
        NbtList l = new NbtList();
        NbtElement e0 = NbtDouble.of(vec3d.x);
        NbtElement e1 = NbtDouble.of(vec3d.y);
        NbtElement e2 = NbtDouble.of(vec3d.z);
        l.add(e0);
        l.add(e1);
        l.add(e2);
        compound.put("position", l);
        return compound;
    }
    public static NbtCompound putFloats(NbtCompound compound, float... floats) {
        NbtList l = new NbtList();
        for (float _flt : floats) {
            l.add(NbtFloat.of(_flt));
        }
        compound.put("floats", l);
        return compound;
    }
    public static NbtCompound putInts(NbtCompound compound, int... floats) {
        NbtList l = new NbtList();
        for (int _flt : floats) {
            l.add(NbtInt.of(_flt));
        }
        compound.put("ints", l);
        return compound;
    }
    public static NbtCompound putBools(NbtCompound compound, boolean... floats) {
        NbtList l = new NbtList();
        for (boolean _flt : floats) {
            l.add(NbtByte.of(_flt));
        }
        compound.put("bools", l);
        return compound;
    }
    public static NbtCompound putStrings(NbtCompound compound, String... floats) {
        NbtList l = new NbtList();
        for (String _flt : floats) {
            l.add(NbtString.of(_flt));
        }
        compound.put("strings", l);
        return compound;
    }
    public static NbtCompound putBlockPos(NbtCompound compound, BlockPos pos) {
        compound.putLongArray("blockpos", alib.getBlockPosAsArray(pos));
        return compound;
    }
    public static NbtCompound putCompound(NbtCompound compound, NbtCompound compoundToAdd) {
        compound.put("nbt", compoundToAdd);
        return compound;
    }
    public static void PushCommandInputVals(MinecraftServer s, String id, NbtCompound compound) {
        PopArgs(s);
        CommandArgumentsCompound.put(id, compound);
        s.getDataCommandStorage().set(new Identifier("dpu", "arguments"), CommandArgumentsCompound);
    }
}
