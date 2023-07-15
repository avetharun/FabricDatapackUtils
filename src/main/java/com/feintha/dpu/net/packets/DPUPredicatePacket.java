package com.feintha.dpu.net.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DPUPredicatePacket implements Packet<ClientPlayPacketListener> {
    List<Identifier> requiredResourcePacks = new ArrayList<>();
    List<Identifier> requiredDataPacks = new ArrayList<>();
    
    @Override
    public void write(PacketByteBuf buf) {

    }

    @Override
    public void apply(ClientPlayPacketListener listener) {

    }
}
