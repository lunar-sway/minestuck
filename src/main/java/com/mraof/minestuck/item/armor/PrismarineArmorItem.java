package com.mraof.minestuck.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PrismarineArmorItem extends ArmorItem implements GeoItem
{
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	
	public PrismarineArmorItem(Holder<ArmorMaterial> pMaterial, Type pType, Properties pProperties)
	{
		super(pMaterial, pType, pProperties);
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar)
	{
		controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "idle", 0,  state -> state.setAndContinue(DefaultAnimations.IDLE)));
	}
	
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}
}
