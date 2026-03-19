package homk.terraria1accessories.mixin;

import homk.terraria1accessories.VisualArmorHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.EnumMap;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "collectEquipmentChanges", at = @At("RETURN"), cancellable = true)
    private void injectVisualArmorChanges(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir) {
        if (!((Object) this instanceof VisualArmorHolder holder)) return;

        Map<EquipmentSlot, ItemStack> changes = cir.getReturnValue();

        // Проверяем все 4 слота брони
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                int idx = switch (slot) {
                    case HEAD -> 0;
                    case CHEST -> 1;
                    case LEGS -> 2;
                    case FEET -> 3;
                    default -> -1;
                };

                if (idx != -1) {
                    ItemStack visualItem = holder.getVisualArmorContainer().getItem(idx);
                    // Если в визуальном слоте что-то есть, подменяем данные в пакете
                    if (!visualItem.isEmpty()) {
                        if (changes == null) {
                            changes = new EnumMap<>(EquipmentSlot.class);
                        }
                        changes.put(slot, visualItem.copy());
                    }
                }
            }
        }
        cir.setReturnValue(changes);
    }

    // Для тех, кто только зашел на сервер и видит нас в первый раз
    @Inject(method = "getItemBySlot", at = @At("HEAD"), cancellable = true)
    private void spoofInitialEquipment(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir) {
        if ((Object) this instanceof VisualArmorHolder holder && slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
            int idx = switch (slot) {
                case HEAD -> 0;
                case CHEST -> 1;
                case LEGS -> 2;
                case FEET -> 3;
                default -> -1;
            };
            if (idx != -1) {
                ItemStack visual = holder.getVisualArmorContainer().getItem(idx);
                if (!visual.isEmpty()) {
                    cir.setReturnValue(visual);
                }
            }
        }
    }
}