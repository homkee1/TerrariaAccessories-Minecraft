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
        if (tab.getType() == CreativeModeTab.Type.INVENTORY) {
            for (int i = 0; i < 4; i++) {
                int slotIndex = 46 + i;
                if (slotIndex < this.menu.slots.size()) {
                    Slot slot = this.menu.slots.get(slotIndex);
                    SlotAccessor acc = (SlotAccessor) slot;
                    acc.setX((i < 2) ? 126 : 144);
                    acc.setY((i % 2 == 0) ? 6 : 33);
                }
            }
        }
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void drawSlots(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
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
        g.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_TEXTURE, this.leftPos + x - 1, this.topPos + y - 1, 18, 18);
    }
}