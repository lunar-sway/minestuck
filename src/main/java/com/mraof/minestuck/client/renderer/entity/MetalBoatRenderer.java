package com.mraof.minestuck.client.renderer.entity;

import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.entity.item.MetalBoatEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MetalBoatRenderer extends BoatRenderer
{
	private final BoatModel model;
	
	public MetalBoatRenderer(EntityRendererProvider.Context context, boolean hasChest)
	{
		super(context, hasChest);
		
		this.model = new BoatModel(context.bakeLayer(hasChest
				? ModelLayers.createChestBoatModelName(Boat.Type.OAK)
				: ModelLayers.createBoatModelName(Boat.Type.OAK)), hasChest);
	}
	
	@Override
	public Pair<ResourceLocation, BoatModel> getModelWithLocation(Boat boat)
	{
		MetalBoatEntity metalBoat = (MetalBoatEntity) boat;
		
		return new Pair<>(metalBoat.boatType().getBoatTexture(), this.model);
	}
}