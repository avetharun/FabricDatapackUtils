package com.feintha.dpu.mixin.Client;

import com.feintha.dpu.alib;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(EntityRenderer.class)
public abstract class LivingEntityRendererMixin{
    @Mixin(EntityRendererFactory.Context.class)
    public static class LivingEntityRendererFactory_ContextMixin{
        public NbtCompound data = new NbtCompound();
        public boolean hasCustomData = false;
    }
    public LinkedHashMap<Identifier, NbtCompound> requiredCompound;
    private static <T extends EntityRenderer> Identifier  getIDForEntity(T _this, Entity e, Identifier _default) {
        LinkedHashMap<Identifier, NbtCompound> requiredCompound = alib.getMixinField(_this, "requiredCompound");
        if (requiredCompound != null&& (boolean) alib.getMixinField(e, "hasOverride")) {
            AtomicBoolean brk = new AtomicBoolean(false);
            AtomicReference<Identifier> out = new AtomicReference<>(null);
            requiredCompound.forEach((identifier, compound) -> {
                if (!brk.get() && alib.checkNBTEquals(compound, e.writeNbt(new NbtCompound()))) {
                    out.set(identifier);
                    brk.set(true);
                }
            });
            if (out.get() == null) {return _default; } // fallback
            return out.get();
        }
        return _default;
    }
//    @Mixin(targets = {
//            "net.minecraft.client.render.entity.PigEntityRenderer",
//            "net.minecraft.client.render.entity.WardenEntityRenderer",
//            "net.minecraft.client.render.entity.ArmorStandEntityRenderer",
//            "net.minecraft.client.render.entity.ArrowEntityRenderer",
//            "net.minecraft.client.render.entity.BatEntityRenderer",
//            "net.minecraft.client.render.entity.BlazeEntityRenderer",
//            "net.minecraft.client.render.entity.CamelEntityRenderer",
//            "net.minecraft.client.render.entity.CaveSpiderEntityRenderer",
//            "net.minecraft.client.render.entity.ChickenEntityRenderer",
//            "net.minecraft.client.render.entity.CodEntityRenderer",
//            "net.minecraft.client.render.entity.CowEntityRenderer",
//            "net.minecraft.client.render.entity.CreeperEntityRenderer",
//            "net.minecraft.client.render.entity.DolphinEntityRenderer",
//            "net.minecraft.client.render.entity.DragonFireballEntityRenderer",
//            "net.minecraft.client.render.entity.DrownedEntityRenderer",
//            "net.minecraft.client.render.entity.ElderGuardianEntityRenderer",
//            "net.minecraft.client.render.entity.EndCrystalEntityRenderer",
//            "net.minecraft.client.render.entity.EndermanEntityRenderer",
//            "net.minecraft.client.render.entity.EndermiteEntityRenderer",
//            "net.minecraft.client.render.entity.EvokerEntityRenderer",
//            "net.minecraft.client.render.entity.EvokerFangsEntityRenderer",
//            "net.minecraft.client.render.entity.ExperienceOrbEntityRenderer",
//            "net.minecraft.client.render.entity.FishingBobberEntityRenderer",
//            "net.minecraft.client.render.entity.GiantEntityRenderer",
//            "net.minecraft.client.render.entity.GlowSquidEntityRenderer",
//            "net.minecraft.client.render.entity.GoatEntityRenderer",
//            "net.minecraft.client.render.entity.GuardianEntityRenderer",
//            "net.minecraft.client.render.entity.HoglinEntityRenderer",
//            "net.minecraft.client.render.entity.HuskEntityRenderer",
//            "net.minecraft.client.render.entity.IllusionerEntityRenderer",
//            "net.minecraft.client.render.entity.IronGolemEntityRenderer",
//            "net.minecraft.client.render.entity.LeashKnotEntityRenderer",
//            "net.minecraft.client.render.entity.MagmaCubeEntityRenderer",
//            "net.minecraft.client.render.entity.MinecartEntityRenderer",
//            "net.minecraft.client.render.entity.OcelotEntityRenderer",
//            "net.minecraft.client.render.entity.PhantomEntityRenderer",
//            "net.minecraft.client.render.entity.PillagerEntityRenderer",
//            "net.minecraft.client.render.entity.PolarBearEntityRenderer",
//            "net.minecraft.client.render.entity.PufferfishEntityRenderer",
//            "net.minecraft.client.render.entity.RavagerEntityRenderer",
//            "net.minecraft.client.render.entity.SalmonEntityRenderer",
//            "net.minecraft.client.render.entity.SheepEntityRenderer",
//            "net.minecraft.client.render.entity.ShulkerBulletEntityRenderer",
//            "net.minecraft.client.render.entity.SilverfishEntityRenderer",
//            "net.minecraft.client.render.entity.SkeletonEntityRenderer",
//            "net.minecraft.client.render.entity.SlimeEntityRenderer",
//            "net.minecraft.client.render.entity.SnifferEntityRenderer",
//            "net.minecraft.client.render.entity.SnowGolemEntityRenderer",
//            "net.minecraft.client.render.entity.SpectralArrowEntityRenderer",
//            "net.minecraft.client.render.entity.SpiderEntityRenderer",
//            "net.minecraft.client.render.entity.SquidEntityRenderer",
//            "net.minecraft.client.render.entity.StrayEntityRenderer",
//            "net.minecraft.client.render.entity.StriderEntityRenderer",
//            "net.minecraft.client.render.entity.TadpoleEntityRenderer",
//            "net.minecraft.client.render.entity.TridentEntityRenderer",
//            "net.minecraft.client.render.entity.TurtleEntityRenderer",
//            "net.minecraft.client.render.entity.VindicatorEntityRenderer",
//            "net.minecraft.client.render.entity.WanderingTraderEntityRenderer",
//            "net.minecraft.client.render.entity.WitchEntityRenderer",
//            "net.minecraft.client.render.entity.WitherSkeletonEntityRenderer",
//            "net.minecraft.client.render.entity.ZoglinEntityRenderer",
//            "net.minecraft.client.render.entity.ZombieBaseEntityRenderer",
////            "net.minecraft.client.render.entity.ItemFrameEntityRenderer",
////            "net.minecraft.client.render.entity.TropicalFishEntityRenderer",
////            "net.minecraft.client.render.entity.ZombieHorseEntityRenderer",
////            "net.minecraft.client.render.entity.ZombieVillagerEntityRenderer",
////            "net.minecraft.client.render.entity.WolfEntityRenderer",
////            "net.minecraft.client.render.entity.WitherSkullEntityRenderer",
////            "net.minecraft.client.render.entity.WitherEntityRenderer",
////            "net.minecraft.client.render.entity.VillagerEntityRenderer",
////            "net.minecraft.client.render.entity.VexEntityRenderer",
////            "net.minecraft.client.render.entity.TntEntityRenderer",
////            "net.minecraft.client.render.entity.TntMinecartEntityRenderer",
////            "net.minecraft.client.render.entity.ShulkerEntityRenderer",
////            "net.minecraft.client.render.entity.PiglinEntityRenderer",
////            "net.minecraft.client.render.entity.ParrotEntityRenderer",
////            "net.minecraft.client.render.entity.MooshroomEntityRenderer",
////            "net.minecraft.client.render.entity.LlamaEntityRenderer",
////            "net.minecraft.client.render.entity.LlamaSpitEntityRenderer",
////            "net.minecraft.client.render.entity.IllagerEntityRenderer",
////            "net.minecraft.client.render.entity.HorseEntityRenderer",
////            "net.minecraft.client.render.entity.GhastEntityRenderer",
////            "net.minecraft.client.render.entity.FoxEntityRenderer",
////            "net.minecraft.client.render.entity.EnderDragonEntityRenderer",
////            "net.minecraft.client.render.entity.DonkeyEntityRenderer",
////            "net.minecraft.client.render.entity.BoatEntityRenderer",
////            "net.minecraft.client.render.entity.CatEntityRenderer",
////            "net.minecraft.client.render.entity.BeeEntityRenderer",
////            "net.minecraft.client.render.entity.AxolotlEntityRenderer",
//
//    })
//    public static class NormalEntityRendererMixin {
////        @Shadow @Final private static Identifier TEXTURE;
////        @SuppressWarnings("unchecked")
////        @Redirect(method= "getTexture*", at=@At("HEAD"))
////        Identifier getTexture(Entity pigEntity) {
////            EntityRenderer<Entity> r = (EntityRenderer<Entity>) (Object)this;
////            return LivingEntityRendererMixin.getIDForEntity(r, pigEntity, TEXTURE);
////        }
//    }
}
