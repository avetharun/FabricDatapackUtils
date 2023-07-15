package com.feintha.dpu;

import com.feintha.dpu.client.DatapackUtilsClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class DPUEvent {
    public boolean cancelEvent = false;
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
    List<DPUEvent> nestedEvents = new ArrayList<>();
    public SoundEvent sound;

    @SuppressWarnings("rawtypes")
    public ParticleType particle;
    // Particle offset. Uses camera rotation.
    public Vec3d ParticleOffset = new Vec3d(0,0,0);
    public float SoundVolume = 1f;
    public float SoundPitch = 1f;

    public DPUEvent(JsonElement o, Function<JsonElement,DPUEvent> constructor){
        this.constructor = constructor;
        if (o.isJsonObject()) {
            nestedEvents.add(Deserialize(o.getAsJsonObject()));
        } else if (o.isJsonArray()) {
            nestedEvents = DeserializeNested(o.getAsJsonArray());
        } else {
            System.err.println("Incorrect type of event object, expected array or object, got primitive.");
        }
    }
    public DPUEvent(String input, Function<JsonElement,DPUEvent> constructor){
        this(new JsonParser().parse(input).getAsJsonObject(), constructor);
    }
    public void putEventData(@Nullable Entity owner, World world, MinecraftServer server, Identifier id) {}

    protected final void handleClientParticles(){
        assert MinecraftClient.getInstance().world != null;
        if (particle != null) {
            assert MinecraftClient.getInstance().player != null;
            Vec3d pos = MinecraftClient.getInstance().player.getEyePos();
            Vec3d rot = MinecraftClient.getInstance().player.getRotationVecClient();
            Vec3d pOff = ParticleOffset.multiply(rot).add(pos);
            MinecraftClient.getInstance().world.addParticle((ParticleEffect) particle, pOff.x, pOff.y, pOff.z, 0, 0, 0);
        }
    }
    protected final void handleClientParticlesAt(Vec3d pos){
        assert MinecraftClient.getInstance().world != null;
        if (particle != null) {
            Vec3d pOff = ParticleOffset.add(pos);
            MinecraftClient.getInstance().world.addParticle((ParticleEffect) particle, pOff.x, pOff.y, pOff.z, 0, 0, 0);
        }
    }
    protected final void handleServerParticles(ServerWorld world, Entity owner){
        if (particle != null) {
            Vec3d pos = owner.getEyePos();
            Vec3d rot = owner.getRotationVector();
            Vec3d pOff = ParticleOffset.multiply(rot).add(pos);
            world.addParticle((ParticleEffect) particle, true, pOff.x, pOff.y, pOff.z, 0, 0, 0);
        }
    }
    protected final void handleServerParticlesAt(ServerWorld world, Entity owner, Vec3d pos){
        if (particle != null) {
            Vec3d pOff = ParticleOffset.add(pos);
            world.addParticle((ParticleEffect) particle, true, pOff.x, pOff.y, pOff.z, 0, 0, 0);
        }
    }
    protected final void handleFunction(ServerWorld world, Entity owner) {
        if (function != null) {
            var func = world.getServer().getCommandFunctionManager().getFunction(function);
            if (func.isPresent()) {
                world.getServer().getCommandFunctionManager().execute(func.get(), world.getServer().getCommandSource().withEntity(owner));
            } else {
                System.out.println("Unable to execute function " + function + " when using event.");
            }
        }
    }
    protected final void handleFunctionAt(ServerWorld world, Entity owner, Vec3d pos) {
        if (function != null) {
            var func = world.getServer().getCommandFunctionManager().getFunction(function);
            if (func.isPresent()) {
                world.getServer().getCommandFunctionManager().execute(func.get(), world.getServer().getCommandSource().withPosition(pos).withEntity(owner));
            } else {
                System.out.println("Unable to execute function " + function + " when using event.");
            }
        }
    }
    protected final void handleClientSound(){
        assert MinecraftClient.getInstance().world != null;
        if (sound != null) {
            // hacky solutions are sometimes the only solution :despair:
            assert MinecraftClient.getInstance().player != null;
            Vec3d pos = MinecraftClient.getInstance().player.getEyePos();
            MinecraftClient.getInstance().world.playSound(pos.x, pos.y - 0.25f, pos.z, sound, SoundCategory.MASTER, SoundVolume, SoundPitch, true);
        }
    }
    protected final void handleClientSoundAt(Vec3d pos){
        assert MinecraftClient.getInstance().world != null;
        if (sound != null) {
            MinecraftClient.getInstance().world.playSound(null, pos.x, pos.y - 0.25f, pos.z, sound, SoundCategory.MASTER, SoundVolume, SoundPitch);
        }
    }
    protected final void handleServerSound(ServerWorld w, Entity owner){
        if (sound != null) {
            Vec3d pos = owner.getEyePos();
            w.playSound(null, pos.x, pos.y - 0.25f, pos.z, sound, SoundCategory.MASTER, SoundVolume, SoundPitch);
        }
    }
    protected final void handleServerSoundAt(ServerWorld w, Entity owner, Vec3d pos){
        if (sound != null) {
            w.playSound(null, pos.x, pos.y - 0.25f, pos.z, sound, SoundCategory.MASTER, SoundVolume, SoundPitch);
        }
    }
    public boolean doActionClient(boolean immediate){
        boolean c = false;
        for (DPUEvent e: nestedEvents) {
            c |= e.cancelEvent;
            if (!immediate) {
                DatapackUtilsClient.ScheduleForNextWorldTick(clientWorld -> {
                    e.handleClientSound();
                    e.handleClientParticles();
                });
            } else {
                e.handleClientSound();
                e.handleClientParticles();
            }
        }
        return c;
    }
    public boolean doActionClientAt(Vec3d pos, boolean immediate){
        boolean c = false;
        for (DPUEvent e: nestedEvents) {
            c |= e.cancelEvent;
            if (!immediate) {
                DatapackUtilsClient.ScheduleForNextWorldTick(clientWorld -> {
                    e.handleClientSoundAt(pos);
                    e.handleClientParticlesAt(pos);
                });
            } else {
                e.handleClientSoundAt(pos);
                e.handleClientParticlesAt(pos);
            }
        }
        return c;
    }
    public <T>boolean doActionClient(boolean immediate, T data){
        boolean c = false;
        for (DPUEvent e: nestedEvents) {
            if (!e.preProcessEvent(data)) {
                continue;
            }
            c |= e.cancelEvent;
            if (!immediate) {
                DatapackUtilsClient.ScheduleForNextWorldTick(clientWorld -> {
                    e.handleClientSound();
                    e.handleClientParticles();
                });
            } else {
                e.handleClientSound();
                e.handleClientParticles();
            }
        }
        return c;
    }
    public <T>boolean doActionClientAt(Vec3d pos, boolean immediate, T data){
        boolean c = false;
        for (DPUEvent e: nestedEvents) {
            if (!e.preProcessEvent(data)) {
                continue;
            }
            c |= e.cancelEvent;
            if (!immediate) {
                DatapackUtilsClient.ScheduleForNextWorldTick(clientWorld -> {
                    e.handleClientSoundAt(pos);
                    e.handleClientParticlesAt(pos);
                });
                continue;
            }
            e.handleClientSoundAt(pos);
            e.handleClientParticlesAt(pos);
        }
        return c;
    }







    public <T>boolean doActionServerAt(ServerWorld world, Vec3d pos, boolean immediate, T data) {
        boolean c = false;
        for (DPUEvent e: nestedEvents) {
            if (!e.preProcessEvent(data)) {
                continue;
            }
            c |= e.cancelEvent;
            DatapackUtils.hasHadEvent = true;
            if (!immediate) {
                DatapackUtils.ScheduleForNextTick((s) -> {
                    e.handleFunctionAt(world, null, pos);
                    e.handleServerSoundAt(world, null, pos);
                    e.handleServerParticlesAt(world, null, pos);
                });
                continue;
            }
            e.handleFunctionAt(world, null, pos);
            e.handleServerSoundAt(world, null, pos);
            e.handleServerParticlesAt(world, null, pos);
        }

        return c;
    }
    public boolean doActionServerAt(ServerWorld world, Vec3d pos, boolean immediate) {
        boolean c = false;
        for (DPUEvent e: nestedEvents) {
            c |= e.cancelEvent;
            DatapackUtils.hasHadEvent = true;
            if (!immediate) {
                DatapackUtils.ScheduleForNextTick((s) -> {
                    e.handleFunctionAt(world, null, pos);
                    e.handleServerSoundAt(world, null, pos);
                    e.handleServerParticlesAt(world, null, pos);
                });
            }
            e.handleFunctionAt(world, null, pos);
            e.handleServerSoundAt(world, null, pos);
            e.handleServerParticlesAt(world, null, pos);
        }
        return c;
    }
    public <T> boolean doActionServer(ServerWorld world, Entity owner, boolean immediate, T data) {
        boolean c = false;
        for (DPUEvent e: nestedEvents) {
            if (!e.preProcessEvent(data)) {
                continue;
            }
            c |= e.cancelEvent;
            DatapackUtils.hasHadEvent = true;
            if (!immediate) {
                DatapackUtils.ScheduleForNextTick((s) -> {
                    e.handleFunction(world, owner);
                    e.handleServerSound(world, owner);
                    e.handleServerParticles(world, owner);
                });
            }
            e.handleFunction(world, owner);
            e.handleServerSound(world, owner);
            e.handleServerParticles(world, owner);
        }
        return c;

    }
    public boolean doActionServer(ServerWorld world, Entity owner, boolean immediate) {
        DatapackUtils.hasHadEvent = true;
        boolean c = false;
        for (DPUEvent e: nestedEvents) {
            c |= e.cancelEvent;
            if (!immediate) {
                DatapackUtils.ScheduleForNextTick((s) -> {
                    e.handleFunction(world, owner);
                    e.handleServerSound(world, owner);
                    e.handleServerParticles(world, owner);
                });
                continue;
            }
            e.handleFunction(world, owner);
            e.handleServerSound(world, owner);
            e.handleServerParticles(world, owner);
        }
        return c;
    }
    public <T>boolean doActionServerAt(ServerWorld world, Entity owner, Vec3d pos, boolean immediate, T data) {
        boolean c = false;
        for (DPUEvent e: nestedEvents) {
            c |= e.cancelEvent;
            if (!e.preProcessEvent(data)) {
                continue;
            }
            DatapackUtils.hasHadEvent = true;
            if (!immediate) {
                DatapackUtils.ScheduleForNextTick((s) -> {
                    e.handleFunctionAt(world, owner, pos);
                    e.handleServerSoundAt(world, owner, pos);
                    e.handleServerParticlesAt(world, owner, pos);
                });
                continue;
            }
            e.handleFunctionAt(world, owner, pos);
            e.handleServerSoundAt(world, owner, pos);
            e.handleServerParticlesAt(world, owner, pos);
        }

        return c;
    }
    public boolean doActionServerAt(ServerWorld world, Entity owner, Vec3d pos, boolean immediate) {
        boolean c = false;
        for (DPUEvent e: nestedEvents) {
            c |= e.cancelEvent;
            DatapackUtils.hasHadEvent = true;
            if (!immediate) {
                DatapackUtils.ScheduleForNextTick((s) -> {
                    e.handleFunctionAt(world, owner, pos);
                    e.handleServerSoundAt(world, owner, pos);
                    e.handleServerParticlesAt(world, owner, pos);
                });
            }
            e.handleFunctionAt(world, owner, pos);
            e.handleServerSoundAt(world, owner, pos);
            e.handleServerParticlesAt(world, owner, pos);
        }
        return c;
    }
    public NbtCompound requiredNbt;
    public enum DPUDayType{
        MIDNIGHT, DAY, NIGHT
    }
    public <T> boolean preProcessEvent(T data) {
        System.out.println("default event type used");
        return true;
    }

    public final List<DPUEvent> DeserializeNested(JsonArray array) {
        List<DPUEvent> events = new ArrayList<>();
        for (JsonElement obj : array) {
            var elem = constructor.apply(obj);
            events.add(elem);
        }
        return events;
    }
    final Function<JsonElement, DPUEvent> constructor;
    @SuppressWarnings("UnusedReturnValue")
    public DPUEvent Deserialize(JsonObject object){
        if (object.has("predicate")) {
            JsonObject p_O = object.getAsJsonObject("predicate");
            if (p_O.has("nbt")) {
                JsonObject p_N = p_O.getAsJsonObject("nbt");
                requiredNbt = alib.json2NBT(p_N);
            }
        }
        if (object.has("cancels")) {
            this.cancelEvent = object.get("cancels").getAsBoolean();
        }
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
