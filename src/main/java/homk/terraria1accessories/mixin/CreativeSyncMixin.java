package homk.terraria1accessories.mixin;

import homk.terraria1accessories.VisualArmorHolder;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.level.ServerPlayer;

@Mixin(ServerGamePacketListenerImpl.class)
public class CreativeSyncMixin {
    @Shadow public ServerPlayer player;

    @Inject(method = "handleSetCreativeModeSlot", at = @At("HEAD"), cancellable = true)
    private void syncVisualSlots(ServerboundSetCreativeModeSlotPacket packet, CallbackInfo ci) {
        if (!this.player.isCreative()) return;

        int slotNum = packet.slotNum();
        if (slotNum >= 46 && slotNum <= 49) {
            Slot slot = this.player.inventoryMenu.getSlot(slotNum);
            slot.set(packet.itemStack());
            this.player.inventoryMenu.broadcastChanges();

            ci.cancel();
        }
    }
}