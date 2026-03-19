package homk.terraria1accessories.mixin;

import com.mojang.datafixers.util.Pair;
import homk.terraria1accessories.Terraria1accessories;
import homk.terraria1accessories.VisualArmorHolder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class PacketSyncMixin {

    @Unique
    private ServerPlayer getPlayer() {
        if ((Object) this instanceof net.minecraft.server.network.ServerGamePacketListenerImpl gameListener) {
            return gameListener.player;
        }
        return null;
    }

    @ModifyVariable(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), argsOnly = true)
    private Packet<?> modifyOutgoingPacket(Packet<?> packet) {
        ServerPlayer receiver = getPlayer();
        if (receiver == null) return packet;

        if (packet instanceof ClientboundSetEquipmentPacket equipPacket) {
            Entity wearer = receiver.level().getEntity(equipPacket.getEntity());

            if (wearer != null && wearer != receiver && wearer instanceof VisualArmorHolder holder) {
                List<Pair<EquipmentSlot, ItemStack>> slots = new ArrayList<>(equipPacket.getSlots());
                boolean changed = false;

                for (int i = 0; i < slots.size(); i++) {
                    Pair<EquipmentSlot, ItemStack> pair = slots.get(i);
                    EquipmentSlot slot = pair.getFirst();

                    if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                        int idx = switch (slot) {
                            case HEAD -> 0; case CHEST -> 1; case LEGS -> 2; case FEET -> 3;
                            default -> -1;
                        };

                        ItemStack visual = holder.getVisualArmorContainer().getItem(idx);
                        if (!visual.isEmpty()) {
                            slots.set(i, Pair.of(slot, visual.copy()));
                            changed = true;
                        }
                    }
                }
                if (changed) {
                    return new ClientboundSetEquipmentPacket(equipPacket.getEntity(), slots);
                }
            }
        }

        if (!Terraria1accessories.MODDED_PLAYERS.contains(receiver.getUUID())) {
            if (packet instanceof ClientboundContainerSetContentPacket contentPacket) {
                ClientboundContainerSetContentPacketAccessor acc = (ClientboundContainerSetContentPacketAccessor) (Object) contentPacket;
                if (acc.callGetContainerId() == 0 && acc.callGetItems().size() > 46) {
                    NonNullList<ItemStack> trimmed = NonNullList.create();
                    trimmed.addAll(acc.callGetItems().subList(0, 46));
                    return new ClientboundContainerSetContentPacket(0, acc.callGetStateId(), trimmed, acc.callGetCarriedItem());
                }
            }
        }
        return packet;
    }

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void cancelSlotPacket(Packet<?> packet, CallbackInfo ci) {
        ServerPlayer receiver = getPlayer();
        if (receiver != null && !Terraria1accessories.MODDED_PLAYERS.contains(receiver.getUUID())) {
            if (packet instanceof ClientboundContainerSetSlotPacket slotPacket) {
                ClientboundContainerSetSlotPacketAccessor acc = (ClientboundContainerSetSlotPacketAccessor) (Object) slotPacket;
                if (acc.callGetContainerId() == 0 && acc.callGetSlot() >= 46) {
                    ci.cancel();
                }
            }
        }
    }
}