package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.model.ModelPlayer;
import net.minecraft.util.ResourceLocation;

import com.mraof.minestuck.entity.DecoyEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderDecoy extends RenderLivingBase<DecoyEntity>
{
	
	public RenderDecoy(RenderManager manager)
	{
		super(manager, new ModelPlayer(0F, false), 0F);
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerArrow(this));
	}
	
	@Override
	protected ResourceLocation getEntityTexture(DecoyEntity entity)
	{
		return entity.getLocationSkin();
	}
}