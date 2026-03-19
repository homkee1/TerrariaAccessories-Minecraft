package homk.terraria1accessories.mixin;

import homk.terraria1accessories.VisualArmorHolder;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import com.mojang.datafixers.util.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {
    @Shadow @Final private Entity entity;

    @ModifyArg(method = "sendPairingData", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 3))
    private Object spoofInitialPacket(Object packetObj) {
        if (packetObj instanceof ClientboundSetEquipmentPacket packet && this.entity instanceof VisualArmorHolder holder) {
            List<Pair<EquipmentSlot, ItemStack>> slots = new ArrayList<>(packet.getSlots());
            boolean changed = false;

            for (int i = 0; i < slots.size(); i++) {
                Pair<EquipmentSlot, ItemStack> pair = slots.get(i);
                EquipmentSlot slot = pair.getFirst();

                if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                    int idx = switch (slot) {
                        case HEAD -> 0;
                        case CHEST -> 1;
                        case LEGS -> 2;
                        case FEET -> 3;
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
                return new ClientboundSetEquipmentPacket(this.entity.getId(), slots);
            }
        }
        return packetObj;
    }
}