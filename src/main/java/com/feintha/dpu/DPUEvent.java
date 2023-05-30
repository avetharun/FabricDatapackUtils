package com.feintha.dpu;

import com.feintha.dpu.client.DatapackUtilsClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class DPUEvent {
    public Identifier function = null;
    public boolean serverOnly = false;
    public int actionLastTick = 0;
    public int requiredTickDelay = 0;

    public static JsonArray DESERIALIZE_OFFSET_ZERO = new JsonArray();
    static {
        DESERIALIZE_OFFSET_ZERO.add(0);
        DESERIALIZE_OFFSET_ZERO.add(0);
        DESERIALIZE_OFFSET_ZERO.add(0);
    }
    public SoundEvent sound;

    @SuppressWarnings("rawtypes")
    public ParticleType particle;
    // Particle offset. Uses camera rotation.
    public Vec3d ParticleOffset = new Vec3d(0,0,0);
    public float SoundVolume = 1f;
    public float SoundPitch = 1f;

    public DPUEvent(JsonObject o){
        Deserialize(o);
    }
    public DPUEvent(String input){
        this(new JsonParser().parse(input).getAsJsonObject());
    }


    protected final void handleParticles(){
        assert MinecraftClient.getInstance().world != null;
        if (particle != null) {
            assert MinecraftClient.getInstance().player != null;
            Vec3d pos = MinecraftClient.getInstance().player.getEyePos();
            Vec3d rot = MinecraftClient.getInstance().player.getRotationVecClient();
            Vec3d pOff = ParticleOffset.multiply(rot).add(pos);
            MinecraftClient.getInstance().world.addParticle((ParticleEffect) particle, pOff.x, pOff.y, pOff.z, 0, 0, 0);
        }
    }
    protected final void handleParticlesAt(Vec3d pos){
        assert MinecraftClient.getInstance().world != null;
        if (particle != null) {
            Vec3d pOff = ParticleOffset.add(pos);
            MinecraftClient.getInstance().world.addParticle((ParticleEffect) particle, pOff.x, pOff.y, pOff.z, 0, 0, 0);
        }
    }
    protected final void handleServerParticles(ServerWorld world, PlayerEntity owner){
        if (particle != null) {
            Vec3d pos = owner.getEyePos();
            Vec3d rot = owner.getRotationVector();
            Vec3d pOff = ParticleOffset.multiply(rot).add(pos);
            world.addParticle((ParticleEffect) particle, true, pOff.x, pOff.y, pOff.z, 0, 0, 0);
        }
    }
    protected final void handleServerParticlesAt(ServerWorld world, PlayerEntity owner, Vec3d pos){
        if (particle != null) {
            Vec3d pOff = ParticleOffset.add(pos);
            world.addParticle((ParticleEffect) particle, true, pOff.x, pOff.y, pOff.z, 0, 0, 0);
        }
    }
    protected final void handleFunction(ServerWorld world, ServerPlayerEntity owner) {
        if (function != null) {
            var func = world.getServer().getCommandFunctionManager().getFunction(function);
            if (func.isPresent()) {
                world.getServer().getCommandFunctionManager().execute(func.get(), owner.getCommandSource());
            } else {
                System.out.println("Unable to execute function " + function + " when using item event.");
            }
        }
    }
    protected final void handleFunctionAt(ServerWorld world, ServerPlayerEntity owner, Vec3d pos) {
        if (function != null) {
            var func = world.getServer().getCommandFunctionManager().getFunction(function);
            if (func.isPresent()) {
                if (owner == null) {
                    world.getServer().getCommandFunctionManager().execute(func.get(), world.getServer().getCommandSource().withPosition(pos));
                } else {
                    world.getServer().getCommandFunctionManager().execute(func.get(), owner.getCommandSource().withPosition(pos));
                }
            } else {
                System.out.println("Unable to execute function " + function + " when using item event.");
            }
        }
    }
    protected final void handleSound(){
        assert MinecraftClient.getInstance().world != null;
        if (sound != null) {
            // hacky solutions are sometimes the only solution :despair:
            assert MinecraftClient.getInstance().player != null;
            Vec3d pos = MinecraftClient.getInstance().player.getEyePos();
            MinecraftClient.getInstance().world.playSound(pos.x, pos.y - 0.25f, pos.z, sound, SoundCategory.MASTER, SoundVolume, SoundPitch, true);
        }
    }
    protected final void handleServerSound(ServerWorld w, PlayerEntity owner){
        if (sound != null) {
            Vec3d pos = owner.getEyePos();
            w.playSound(null, pos.x, pos.y - 0.25f, pos.z, sound, SoundCategory.MASTER, SoundVolume, SoundPitch);
        }
    }
    protected final void handleServerSoundAt(ServerWorld w, PlayerEntity owner, Vec3d pos){
        if (sound != null) {
            w.playSound(null, pos.x, pos.y - 0.25f, pos.z, sound, SoundCategory.MASTER, SoundVolume, SoundPitch);
        }
    }
    public void doActionClient(){
        handleSound();
        handleParticles();
    }
    public void doActionClientAt(Vec3d pos){
        handleSound();
        handleParticles();
    }
    public void doActionServer(ServerWorld world, PlayerEntity owner) {
        handleFunction(world, (ServerPlayerEntity) owner);
        handleServerSound(world,owner);
        handleServerParticles(world,owner);
    }
    public void doActionServerAt(ServerWorld world, PlayerEntity owner, Vec3d pos) {
        handleFunctionAt(world, (ServerPlayerEntity) owner, pos);
        handleServerSoundAt(world,owner, pos);
        handleServerParticlesAt(world,owner, pos);

    }
    @SuppressWarnings("UnusedReturnValue")
    public DPUEvent Deserialize(JsonObject object){
        if (object.has("function")) {
            this.function = new Identifier(object.get("function").getAsString());
        }
        this.serverOnly = JsonHelper.getBoolean(object, "onlyExecuteOnServer", false);
        this.requiredTickDelay = JsonHelper.getInt(object, "delayBetweenEvents", 10);
        if (object.has("sound")) {
            JsonObject soundObjectPart = object.getAsJsonObject("sound");
            this.sound = SoundEvent.of(Identifier.tryParse(soundObjectPart.get("id").getAsString()));
            this.SoundPitch = JsonHelper.getFloat(soundObjectPart, "pitch", 1f);
            this.SoundVolume = JsonHelper.getFloat(soundObjectPart, "volume", 1f);
        }
        if (object.has("particle")) {
            JsonObject pObjectPart = object.getAsJsonObject("particle");
            this.particle = Registries.PARTICLE_TYPE.get(new Identifier(pObjectPart.get("id").getAsString()));
            JsonArray pOffsetPart = JsonHelper.getArray(pObjectPart, "offset", DESERIALIZE_OFFSET_ZERO);
            this.ParticleOffset = new Vec3d(pOffsetPart.get(0).getAsDouble(), pOffsetPart.get(1).getAsDouble(), pOffsetPart.get(2).getAsDouble());
        }

        return this;
    }
}
