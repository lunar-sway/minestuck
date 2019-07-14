package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.ResourceLocation;

import com.mraof.minestuck.entity.DecoyEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DecoyRenderer extends MobRenderer<DecoyEntity, PlayerModel<DecoyEntity>>
{
	
	public DecoyRenderer(EntityRendererManager manager)
	{
		super(manager, new PlayerModel<>(0F, false), 0F);
		this.addLayer(new BipedArmorLayer<>(this, new BipedModel(0.5F), new BipedModel(1.0F)));
		this.addLayer(new HeldItemLayer<>(this));
		this.addLayer(new ArrowLayer<>(this));
	}
	
	@Override
	protected ResourceLocation getEntityTexture(DecoyEntity entity)
	{
		return entity.getLocationSkin();
	}
}