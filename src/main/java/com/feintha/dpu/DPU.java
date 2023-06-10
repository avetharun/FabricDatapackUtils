package com.feintha.dpu;

import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Function3;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.command.DataCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

public class DPU {
    public static class DPUWorkerThread_T extends Thread{
        public DPUWorkerThread_T(int id) {
            super("DPUWorkerThread" + id);
        }
        public Function<Object, Object> currentMethod;
        public Object data;
        private boolean completed = false;
        public boolean isCompleted() { return completed; }
        public boolean isBusy() { return !completed; }

        @Override
        public void run() {
            super.run();
            completed = false;
            currentMethod.apply(data);
            completed = true;
        }
    }
    public static ThreadPoolExecutor DPUWorkerThreads;
    public static final RegistryKey<Registry<DPUEventType>> EVENT_TYPE_KEYS = RegistryKey.ofRegistry(new Identifier("dpu", "eventtypes"));
    public static final Registry<DPUEventType> EVENT_TYPE = FabricRegistryBuilder.createSimple(EVENT_TYPE_KEYS).buildAndRegister();
    public static void InitEverything() {
        DPUEventType._MINIT();
        for (var entry : EVENT_TYPE.getEntrySet()) {
            entry.getValue().Events.clear();
        }
        DPUWorkerThreads = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
    }
    public static void InvokeClientEventFor(DPUEventType type, Identifier idToRun) {
        Identifier id = new Identifier("cl_"+idToRun.getNamespace(), idToRun.getPath());
        Identifier idStar = new Identifier("cl_"+idToRun.getNamespace(), "--");
        DPUEvent action = type.getSubEvent(id);
        DPUEvent actionS = type.getSubEvent(idStar);
        if (action != null) {
            action.doActionClient();
        }
        if (actionS != null) {
            actionS.doActionClient();
        }
    }
    public static void InvokeServerEventFor(DPUEventType type, Identifier idToRun, ServerWorld world, @NotNull Entity owner) {
        Identifier id = new Identifier("sv_"+idToRun.getNamespace(), idToRun.getPath());
        Identifier idStar = new Identifier("sv_"+idToRun.getNamespace(), "--");
        DPUEvent action = type.getSubEvent(id);
        DPUEvent actionS = type.getSubEvent(idStar);
        if (action != null) {
            action.doActionServer(world, owner);
        }
        if (actionS != null) {
            actionS.doActionServer(world,owner);
        }
    }
    public static void InvokeClientEventForAt(DPUEventType type, Identifier idToRun, Vec3d pos) {
        Identifier id = new Identifier("cl_"+idToRun.getNamespace(), idToRun.getPath());
        Identifier idStar = new Identifier("cl_"+idToRun.getNamespace(), "--");
        DPUEvent action = type.getSubEvent(id);
        DPUEvent actionS = type.getSubEvent(idStar);
        if (action != null) {
            action.doActionClientAt(pos);
        }
        if (actionS != null) {
            actionS.doActionClientAt(pos);
        }
    }
    public static void InvokeServerEventForAt(DPUEventType type, Identifier idToRun, Vec3d pos, ServerWorld world, Entity owner) {
        Identifier id = new Identifier("sv_"+idToRun.getNamespace(), idToRun.getPath());
        Identifier idStar = new Identifier("sv_"+idToRun.getNamespace(), "--");
        DPUEvent action = type.getSubEvent(id);
        DPUEvent actionS = type.getSubEvent(idStar);
        if (action != null) {
            action.doActionServerAt(world, owner, pos);
        }
        if (actionS != null) {
            actionS.doActionServerAt(world,owner, pos);
        }
    }
    public static void InitClientEvents() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier("feintha", "dpu_resource_pack_listener");
            }
            final String SIDE = "cl_";
            final String r_M = "\\/([A-Za-z0-9_.-]+)\\.json";
            @Override
            public void reload(ResourceManager manager) {
                int l = 0;
                for (var entry : EVENT_TYPE.getEntrySet()) {
                    Identifier __id = entry.getKey().getValue();
                    String _path = "events/" + __id.getPath();
                    var on_use_files = manager.findResources(_path, path -> path.getPath().endsWith(".json"));
                    for (var id : on_use_files.keySet()) {
                        var file = on_use_files.get(id);
                        String itemName = id.getPath().replaceAll(".*/(.*?)\\.json", "$1");
                        Identifier itemID = new Identifier(id.getNamespace(), itemName);
                        Identifier registryID = new Identifier(SIDE+itemID.getNamespace(), itemID.getPath());
                        try {
                            String input = new String(file.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                            entry.getValue().addSubEvent(registryID, entry.getValue().createEventType(new JsonParser().parse(input).getAsJsonObject()));
                            l++;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                System.out.println("Loaded " + l + " server-side events.");
            }
        });
        EVENT_TYPE.getKeys().forEach(k -> {
            System.out.println(k.getValue());
        });

    }
    public static void InitServerEvents() {
        ServerTickEvents.START_SERVER_TICK.register(world -> {
            if (world.getTicks() <= 1) {
                DPUDataStorage.InitServer(world);
            }
        });
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier("feintha", "dpu_data_pack_listener");
            }

            final String r_M = "\\/([A-Za-z0-9_.-]+)\\.json";
            final String SIDE = "sv_";
            @Override
            public void reload(ResourceManager manager) {
                int l = 0;
                for (var entry : EVENT_TYPE.getEntrySet()) {
                    Identifier __id = entry.getKey().getValue();
                    String _path = "events/" + __id.getPath();
                    var on_use_files = manager.findResources(_path, path -> path.getPath().endsWith(".json"));
                    for (var id : on_use_files.keySet()) {
                        var file = on_use_files.get(id);
                        String itemName = id.getPath().replaceAll(".*/(.*?)\\.json", "$1");
                        Identifier itemID = new Identifier(id.getNamespace(), itemName);
                        Identifier registryID = new Identifier(SIDE+itemID.getNamespace(), itemID.getPath());

                        try {
                            String input = new String(file.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                            entry.getValue().addSubEvent(registryID, entry.getValue().createEventType(new JsonParser().parse(input).getAsJsonObject()));
                            l++;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                System.out.println("Loaded " + l + " server-side events.");
            }
        });
        EVENT_TYPE.getKeys().forEach(k -> {
            System.out.println(k.getValue());
        });
    }



    public static void InvokeAllClientEventsFor(DPUEventType type) {
        for (var ev_id : type.Events.keySet()) {
            if (ev_id.toString().startsWith("cl_")) {
                DPUEvent action = type.getSubEvent(ev_id);
                if (action != null) {
                    action.doActionClient();
                }
            }
        }
    }
    public static void InvokeAllServerEventsFor(DPUEventType type, ServerWorld world, @NotNull PlayerEntity owner) {
        for (var ev_id : type.Events.keySet()) {
            if (ev_id.toString().startsWith("sv_")) {
                DPUEvent action = type.getSubEvent(ev_id);
                if (action != null) {
                    action.doActionServer(world, owner);
                }
            }
        }
    }
    public static Collection<DPUEvent> getAllClientEventsFor(DPUEventType type) {
        ArrayList<DPUEvent> ev = new ArrayList<>();
        for (var ev_id : type.Events.keySet()) {
            if (ev_id.toString().startsWith("cl_")) {
                DPUEvent action = type.getSubEvent(ev_id);
                if (action != null) {
                    ev.add(action);
                }
            }
        }
        return ev;
    }
    public static Collection<DPUEvent> getAllServerEventsFor(DPUEventType type) {
        ArrayList<DPUEvent> ev = new ArrayList<>();
        for (var ev_id : type.Events.keySet()) {
            if (ev_id.toString().startsWith("sv_")) {
                DPUEvent action = type.getSubEvent(ev_id);
                if (action != null) {
                    ev.add(action);
                }
            }
        }
        return ev;
    }
    public static void InvokeAllClientEventsForAt(DPUEventType type, Vec3d pos) {
        for (var ev_id : type.Events.keySet()) {
            if (ev_id.toString().startsWith("cl_")) {
                DPUEvent action = type.getSubEvent(ev_id);
                if (action != null) {
                    action.doActionClientAt(pos);
                }
            }
        }
    }
    public static void InvokeAllServerEventsForAt(DPUEventType type, ServerWorld world, @NotNull PlayerEntity owner, Vec3d pos) {
        for (var ev_id : type.Events.keySet()) {
            if (ev_id.toString().startsWith("sv_")) {
                DPUEvent action = type.getSubEvent(ev_id);
                if (action != null) {
                    action.doActionServerAt(world, owner, pos);
                }
            }
        }
    }


}
