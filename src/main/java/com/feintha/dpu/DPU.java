package com.feintha.dpu;

import com.feintha.dpu.Events.DPUPlayerEvent;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.impl.client.rendering.EntityRendererRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

public class DPU {

    public static final int VERSION_MAJOR = 1;
    public static final int VERSION_PROP = 0;
    public static final int VERSION_MINOR = 2;
    public static final int VERSION_ID = (100*VERSION_MAJOR) + (10*VERSION_PROP) + (VERSION_MINOR);
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
    public static boolean InvokeClientEventFor(DPUEventType type, Identifier idToRun) {
        Identifier id = new Identifier("cl_"+idToRun.getNamespace(), idToRun.getPath());
        Identifier idStar = new Identifier("cl_"+idToRun.getNamespace(), "--");
        DPUEvent action = type.getSubEvent(id);
        DPUEvent actionS = type.getSubEvent(idStar);
        boolean cancel = false;
        if (action != null) {
            cancel = action.doActionClient(false);
        }
        if (actionS != null) {
            cancel |= actionS.doActionClient(false);
        }
        return cancel;
    }
    public static <T> boolean InvokeClientEventFor(DPUEventType type, Identifier idToRun, T data) {
        Identifier id = new Identifier("cl_"+idToRun.getNamespace(), idToRun.getPath());
        Identifier idStar = new Identifier("cl_"+idToRun.getNamespace(), "--");
        DPUEvent action = type.getSubEvent(id);
        DPUEvent actionS = type.getSubEvent(idStar);
        boolean cancel = false;
        if (action != null) {
            cancel = action.doActionClient(false, data);
        }
        if (actionS != null) {
            cancel |= actionS.doActionClient(false, data);
        }
        return cancel;
    }
    public static boolean InvokeServerEventFor(DPUEventType type, Identifier idToRun, ServerWorld world, @NotNull Entity owner, boolean immediate) {
        Identifier id = new Identifier("sv_"+idToRun.getNamespace(), idToRun.getPath());
        Identifier idStar = new Identifier("sv_"+idToRun.getNamespace(), "--");
        DPUEvent action = type.getSubEvent(id);
        DPUEvent actionS = type.getSubEvent(idStar);
        boolean cancel = false;
        if (action != null) {
            action.putEventData(owner,world,world.getServer(), id);
            cancel = action.doActionServer(world, owner, immediate);
        }
        if (actionS != null) {
            actionS.putEventData(owner,world,world.getServer(), id);
            cancel |= actionS.doActionServer(world,owner, immediate);
        }
        return cancel;
    }
    public static boolean InvokeClientEventForAt(DPUEventType type, Identifier idToRun, Vec3d pos) {
        Identifier id = new Identifier("cl_"+idToRun.getNamespace(), idToRun.getPath());
        Identifier idStar = new Identifier("cl_"+idToRun.getNamespace(), "--");
        DPUEvent action = type.getSubEvent(id);
        DPUEvent actionS = type.getSubEvent(idStar);
        boolean cancel = false;
        if (action != null) {
            cancel = action.doActionClientAt(pos, false);
        }
        if (actionS != null) {
            cancel |= actionS.doActionClientAt(pos, false);
        }
        return cancel;
    }
    public static boolean InvokeServerEventForAt(DPUEventType type, Identifier idToRun, Vec3d pos, ServerWorld world, Entity owner) {
        Identifier id = new Identifier("sv_"+idToRun.getNamespace(), idToRun.getPath());
        Identifier idStar = new Identifier("sv_"+idToRun.getNamespace(), "--");
        DPUEvent action = type.getSubEvent(id);
        DPUEvent actionS = type.getSubEvent(idStar);
        boolean cancel = false;
        if (action != null) {
            action.putEventData(owner,world,world.getServer(), id);
            cancel = action.doActionServerAt(world, owner, pos, false);
        }
        if (actionS != null) {
            actionS.putEventData(owner,world,world.getServer(), id);
            cancel |= actionS.doActionServerAt(world,owner, pos, false);
        }
        return cancel;
    }



    public static <T>boolean InvokeServerEventFor(DPUEventType type, Identifier idToRun, ServerWorld world, @NotNull Entity owner, boolean immediate, T data) {
        Identifier id = new Identifier("sv_"+idToRun.getNamespace(), idToRun.getPath());
        Identifier idStar = new Identifier("sv_"+idToRun.getNamespace(), "--");
        DPUEvent action = type.getSubEvent(id);
        DPUEvent actionS = type.getSubEvent(idStar);
        boolean cancel = false;
        if (action != null) {
            action.putEventData(owner,world,world.getServer(), id);
            cancel = action.doActionServer(world, owner, immediate, data);
        }
        if (actionS != null) {
            actionS.putEventData(owner,world,world.getServer(), id);
            cancel |= actionS.doActionServer(world,owner, immediate, data);
        }
        return cancel;
    }
    public static <T>boolean InvokeClientEventForAt(DPUEventType type, Identifier idToRun, Vec3d pos, T data) {
        Identifier id = new Identifier("cl_"+idToRun.getNamespace(), idToRun.getPath());
        Identifier idStar = new Identifier("cl_"+idToRun.getNamespace(), "--");
        DPUEvent action = type.getSubEvent(id);
        DPUEvent actionS = type.getSubEvent(idStar);
        boolean cancel = false;
        if (action != null) {
            cancel = action.doActionClientAt(pos, true, data);
        }
        if (actionS != null) {
            cancel |= actionS.doActionClientAt(pos, true, data);
        }
        return cancel;
    }
    public static <T>boolean InvokeServerEventForAt(DPUEventType type, Identifier idToRun, Vec3d pos, ServerWorld world, Entity owner, T data) {
        Identifier id = new Identifier("sv_"+idToRun.getNamespace(), idToRun.getPath());
        Identifier idStar = new Identifier("sv_"+idToRun.getNamespace(), "--");
        DPUEvent action = type.getSubEvent(id);
        DPUEvent actionS = type.getSubEvent(idStar);
        boolean cancel = false;
        if (action != null) {
            action.putEventData(owner,world,world.getServer(), id);
            cancel = action.doActionServerAt(world, owner, pos, false, data);
        }
        if (actionS != null) {
            actionS.putEventData(owner,world,world.getServer(), id);
            cancel |= actionS.doActionServerAt(world,owner, pos, false, data);
        }
        return cancel;
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
//                var entityRendererFactories = alib.<Map<EntityType<?>, EntityRendererFactory<?>>,EntityRenderers>getPrivateMixinField(null, "RENDERER_FACTORIES");
//                entityRendererFactories.forEach((entityType, entityRendererFactory) -> {
////                    entityRendererFactory.create()
//                });

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
                            var j = new JsonParser().parse(input);
                            entry.getValue().addSubEvent(registryID, entry.getValue().createEventType(new JsonParser().parse(input)));
                            l++;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                System.out.println("Loaded " + l + " server-side events.");
            }
        });
    }
    public static void InitCustomBlocks() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public void reload(ResourceManager manager) {
                String _path = "custom/blocks/";
                var custom_block_files = manager.findResources(_path, path -> path.getPath().endsWith(".json"));
                custom_block_files.forEach((identifier, resource) -> {

                });
            }

            @Override
            public Identifier getFabricId() {
                return new Identifier("dpu", "blocks");
            }
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
                            entry.getValue().addSubEvent(registryID, entry.getValue().createEventType(new JsonParser().parse(input)));
                            l++;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                System.out.println("Loaded " + l + " server-side events.");
            }
        });
    }



    public static void InvokeAllClientEventsFor(DPUEventType type) {
        for (var ev_id : type.Events.keySet()) {
            if (ev_id.toString().startsWith("cl_")) {
                DPUEvent action = type.getSubEvent(ev_id);
                if (action != null) {
                    action.doActionClient(false);
                }
            }
        }
    }
    public static void InvokeAllServerEventsFor(DPUEventType type, ServerWorld world, @NotNull PlayerEntity owner, boolean immediate) {
        for (var ev_id : type.Events.keySet()) {
            if (ev_id.toString().startsWith("sv_")) {
                DPUEvent action = type.getSubEvent(ev_id);
                if (action != null) {
                    action.doActionServer(world, owner, immediate);
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
                    action.doActionClientAt(pos, false);
                }
            }
        }
    }
    public static void InvokeAllServerEventsForAt(DPUEventType type, ServerWorld world, @NotNull PlayerEntity owner, Vec3d pos) {
        for (var ev_id : type.Events.keySet()) {
            if (ev_id.toString().startsWith("sv_")) {
                DPUEvent action = type.getSubEvent(ev_id);
                if (action != null) {
                    action.doActionServerAt(world, owner, pos, false);
                }
            }
        }
    }


}
