package homk.terraria1accessories.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends net.minecraft.client.gui.screens.inventory.AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    @Unique
    private static final ResourceLocation SLOT_TEXTURE = ResourceLocation.withDefaultNamespace("container/slot");

    public CreativeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, net.minecraft.world.entity.player.Inventory inventory, net.minecraft.network.chat.Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = "selectTab", at = @At("TAIL"))
    private void fixSlots(CreativeModeTab tab, CallbackInfo ci) {
        // Проверяем, является ли выбранная вкладка инвентарем выживания
        boolean isInventoryTab = tab.getType() == CreativeModeTab.Type.INVENTORY;

        for (Slot slot : this.menu.slots) {
            // В креативном меню (вкладка выживания) Minecraft создает SlotWrapper
            // для каждого слота из InventoryMenu игрока.
            // Наши визуальные слоты в InventoryMenu имеют индексы 46, 47, 48, 49.
            int index = slot.getContainerSlot();

            if (index >= 46 && index <= 49) {
                SlotAccessor acc = (SlotAccessor) slot;
                if (isInventoryTab) {
                    // Перемещаем слоты в нужные координаты справа от персонажа
                    int i = index - 46;
                    acc.setX((i < 2) ? 126 : 144);
                    acc.setY((i % 2 == 0) ? 6 : 33);
                } else {
                    // Если это любая другая вкладка (Блоки, Поиск и т.д.) -
                    // прячем слоты далеко за экран, чтобы они не перекрывали хотбар.
                    acc.setX(-2000);
                    acc.setY(-2000);
                }
            }
        }
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void drawSlots(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        // Исправляем ошибку static context: кастуем this к классу экрана
        CreativeModeInventoryScreen screen = (CreativeModeInventoryScreen) (Object) this;

        if (screen.isInventoryOpen()) {
            drawBox(guiGraphics, 126, 6);
            drawBox(guiGraphics, 126, 33);
            drawBox(guiGraphics, 144, 6);
            drawBox(guiGraphics, 144, 33);
        }
    }

    @Unique
    private void drawBox(GuiGraphics g, int x, int y) {
        // Используем this.leftPos и this.topPos (они доступны в AbstractContainerScreen)
        g.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_TEXTURE, this.leftPos + x - 1, this.topPos + y - 1, 18, 18);
    }
}