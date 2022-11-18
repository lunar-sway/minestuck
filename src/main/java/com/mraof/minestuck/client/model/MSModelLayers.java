package com.mraof.minestuck.client.model;

import com.mraof.minestuck.Minestuck;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Minestuck.MOD_ID, value = Dist.CLIENT)
public final class MSModelLayers
{
	public static final ModelLayerLocation FROG = new ModelLayerLocation(new ResourceLocation("minestuck:frog"), "main");
	
	public static final ModelLayerLocation BISHOP = new ModelLayerLocation(new ResourceLocation("minestuck:bishop"), "main");
	public static final ModelLayerLocation ROOK = new ModelLayerLocation(new ResourceLocation("minestuck:rook"), "main");
	
	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(FROG, FrogModel::createBodyLayer);
		
		event.registerLayerDefinition(BISHOP, BishopModel::createBodyLayer);
		event.registerLayerDefinition(ROOK, RookModel::createBodyLayer);
	}
}