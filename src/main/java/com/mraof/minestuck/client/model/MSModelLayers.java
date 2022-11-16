package com.mraof.minestuck.client.model;

import com.mraof.minestuck.Minestuck;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Minestuck.MOD_ID, value = Dist.CLIENT)
public final class MSModelLayers
{
	public static final ModelLayerLocation FROG = new ModelLayerLocation(new ResourceLocation("minestuck:frog"), "main");
	public static final ModelLayerLocation NAKAGATOR = new ModelLayerLocation(new ResourceLocation("minestuck:nakagator"), "main");
	public static final ModelLayerLocation SALAMANDER = new ModelLayerLocation(new ResourceLocation("minestuck:salamander"), "main");
	public static final ModelLayerLocation IGUANA = new ModelLayerLocation(new ResourceLocation("minestuck:iguana"), "main");
	public static final ModelLayerLocation TURTLE = new ModelLayerLocation(new ResourceLocation("minestuck:turtle"), "main");
	
	public static final ModelLayerLocation IMP = new ModelLayerLocation(new ResourceLocation("minestuck:imp"), "main");
	public static final ModelLayerLocation OGRE = new ModelLayerLocation(new ResourceLocation("minestuck:ogre"), "main");
	public static final ModelLayerLocation BASILISK = new ModelLayerLocation(new ResourceLocation("minestuck:basilisk"), "main");
	public static final ModelLayerLocation LICH = new ModelLayerLocation(new ResourceLocation("minestuck:lich"), "main");
	public static final ModelLayerLocation GICLOPS = new ModelLayerLocation(new ResourceLocation("minestuck:giclops"), "main");
	
	public static final ModelLayerLocation BISHOP = new ModelLayerLocation(new ResourceLocation("minestuck:bishop"), "main");
	public static final ModelLayerLocation ROOK = new ModelLayerLocation(new ResourceLocation("minestuck:rook"), "main");
	public static final ModelLayerLocation PAWN = new ModelLayerLocation(new ResourceLocation("minestuck:pawn"), "main");
	
	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(FROG, FrogModel::createBodyLayer);
		event.registerLayerDefinition(NAKAGATOR, NakagatorModel::createBodyLayer);
		event.registerLayerDefinition(SALAMANDER, SalamanderModel::createBodyLayer);
		event.registerLayerDefinition(IGUANA, IguanaModel::createBodyLayer);
		event.registerLayerDefinition(TURTLE, TurtleModel::createBodyLayer);
		
		event.registerLayerDefinition(IMP, ImpModel::createBodyLayer);
		event.registerLayerDefinition(OGRE, OgreModel::createBodyLayer);
		event.registerLayerDefinition(BASILISK, BasiliskModel::createBodyLayer);
		event.registerLayerDefinition(LICH, LichModel::createBodyLayer);
		event.registerLayerDefinition(GICLOPS, GiclopsModel::createBodyLayer);
		
		event.registerLayerDefinition(BISHOP, BishopModel::createBodyLayer);
		event.registerLayerDefinition(ROOK, RookModel::createBodyLayer);
		event.registerLayerDefinition(PAWN, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1), 0), 64, 32));
	}
}