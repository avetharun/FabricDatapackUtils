package com.feintha.dpu;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
//
//public class DPUBlockHolderClass extends BlockWithEntity{
//    public static class DPUBlockHolderEntity extends BlockEntity {
//        public DPUBlockHolderEntity(BlockPos pos, BlockState state) {
//            super(DatapackUtils.DPUBlockHolderEntity, pos, state);
//        }
//        public float strength;
//        public boolean collidable;
//        public float resistance;
//        public boolean randomTicks;
//        public BlockSoundGroup soundGroup;
//        public float slipperiness;
//        public float velocityMultiplier;
//        public float jumpVelocityMultiplier;
//        public Identifier blockID;
//        public Identifier lootTableId;
//
//        @Override
//        public NbtCompound toInitialChunkDataNbt() {
//            var c = super.toInitialChunkDataNbt();
//            c.putString("block_id", blockID.toString());
//            c.putFloat("jump_velocity_multiplier", jumpVelocityMultiplier);
//            c.putFloat("velocity_multiplier", velocityMultiplier);
//            c.putFloat("slipperiness", slipperiness);
//            c.putString("sound_group", soundGroup.toString());
//            c.putBoolean("random_ticks", randomTicks);
//            c.putFloat("blast_resistance", resistance);
//            c.putBoolean("collidable", collidable);
//            c.putFloat("hardness", strength);
//            c.putString("loot_table_id", lootTableId.toString());
//            return c;
//        }
//    }
//    public DPUBlockHolderClass(Settings settings) {
//        super(settings);
//    }
//
//
//    @Nullable
//    @Override
//    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
//        return new DPUBlockHolderEntity(pos, state);
//    }
//
//}
