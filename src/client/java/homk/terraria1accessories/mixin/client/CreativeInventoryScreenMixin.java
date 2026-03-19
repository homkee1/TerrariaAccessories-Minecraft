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

    @Unique private static final int RIGHT_COLUMN_X = 172;
    @Unique private static final int START_Y = 6;
    @Unique private static final int SLOT_SPACING = 18;

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

                    acc.setX(RIGHT_COLUMN_X);
                    acc.setY(START_Y + (i * SLOT_SPACING));
                }
            }
        }
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void drawSlots(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        CreativeModeInventoryScreen screen = (CreativeModeInventoryScreen) (Object) this;


        if (screen.isInventoryOpen()) {
            for (int k = 0; k < 4; k++) {
                drawBox(guiGraphics, RIGHT_COLUMN_X, START_Y + (k * SLOT_SPACING));
            }
        }
    }

    @Unique
    private void drawBox(GuiGraphics g, int x, int y) {

        g.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_TEXTURE, this.leftPos + x - 1, this.topPos + y - 1, 18, 18);
    }
}