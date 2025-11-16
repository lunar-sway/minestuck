package com.mraof.minestuck.item.armor;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.mraof.minestuck.client.model.armor.PrismarineArmorModel;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
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
		controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "idle", 0, state -> state.setAndContinue(DefaultAnimations.IDLE)));
	}
	
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}
	
	@Override
	public void createGeoRenderer(Consumer<GeoRenderProvider> consumer)
	{
		consumer.accept(new GeoRenderProvider()
		{
			private GeoArmorRenderer<?> renderer;
			
			@Override
			public <T extends LivingEntity> @Nullable HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity,
																						   ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original)
			{
				if(this.renderer == null)
					this.renderer = new GeoArmorRenderer<>(new PrismarineArmorModel());
				
				return this.renderer;
			}
		});
	}
}
