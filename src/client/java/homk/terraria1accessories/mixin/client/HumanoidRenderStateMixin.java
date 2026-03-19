package homk.terraria1accessories.mixin.client;

import homk.terraria1accessories.VisualStateAccessor;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HumanoidRenderState.class)
public class HumanoidRenderStateMixin implements VisualStateAccessor {
	@Unique
	private final ItemStack[] visualArmor = {ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};

	@Override
	public void setVisualArmor(int slot, ItemStack stack) {
		this.visualArmor[slot] = stack.copy();
	}

	@Override
	public ItemStack getVisualArmor(int slot) {
		return this.visualArmor[slot];
	}
}