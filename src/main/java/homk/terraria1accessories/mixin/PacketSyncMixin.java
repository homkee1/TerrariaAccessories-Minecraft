package homk.terraria1accessories.mixin;

import homk.terraria1accessories.Terraria1accessories;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class PacketSyncMixin {

    private ServerPlayer getPlayer() {
        if ((Object) this instanceof net.minecraft.server.network.ServerGamePacketListenerImpl gameListener) {
            return gameListener.player;
        }
        return null;
    }

    @ModifyVariable(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), argsOnly = true)
    private Packet<?> modifyContainerContentPacket(Packet<?> packet) {
        ServerPlayer player = getPlayer();

        // Если игрок еще НЕ подтвердил наличие мода (его нет в списке)
        if (player != null && !Terraria1accessories.MODDED_PLAYERS.contains(player.getUUID())) {
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
        ServerPlayer player = getPlayer();
        // Если игрока нет в списке модовых - блокируем пакеты для слотов 46+
        if (player != null && !Terraria1accessories.MODDED_PLAYERS.contains(player.getUUID())) {
            if (packet instanceof ClientboundContainerSetSlotPacket slotPacket) {
                ClientboundContainerSetSlotPacketAccessor acc = (ClientboundContainerSetSlotPacketAccessor) (Object) slotPacket;
                if (acc.callGetContainerId() == 0 && acc.callGetSlot() >= 46) {
                    ci.cancel();
                }
            }
        }
    }
}