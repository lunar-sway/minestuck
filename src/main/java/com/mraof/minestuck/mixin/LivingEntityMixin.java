package com.mraof.minestuck.mixin;

import com.mraof.minestuck.fluid.MSFluidType;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	/**
	 * After entity movement, and before applying fall damage, vanilla does a fluid check to reset fall distance if the entity moved into water.
	 * However, the forge patch for resetting fall distance when in mod fluids does not cover this specific circumstance, which is why this mixin exists.
	 * Because the call to {@code Entity#updateInWaterStateAndDoWaterCurrentPushing()} also updates the fluid height of all touched fluids after movement,
	 * this function is injected immediately after to be able to use these updated fluid heights ourselves.
	 * This mixin will not be needed once a fix to fall distance resetting exists in forge/neoforge.
	 */
	@Inject(method = "checkFallDamage", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updateInWaterStateAndDoWaterCurrentPushing()V", ordinal = 0, shift = At.Shift.AFTER))
	protected void onCheckFallDamage(CallbackInfo ci)
	{
		MSFluidType.handleExtraFallReset((LivingEntity) (Object) this);
	}
}
