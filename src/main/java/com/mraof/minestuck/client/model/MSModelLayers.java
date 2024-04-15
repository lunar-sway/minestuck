package com.mraof.minestuck.client.model;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.model.entity.BishopModel;
import com.mraof.minestuck.client.model.entity.FrogModel;
import com.mraof.minestuck.client.model.entity.RookModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

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