package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

import java.util.Objects;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LandSkySpriteUploader extends TextureAtlasHolder
{
	public static final int VARIANT_COUNT = 3;
	
	private static final ResourceLocation SKAIA = Minestuck.id("skaia");
	private static final ResourceLocation PROSPIT = Minestuck.id("prospit");
	private static final ResourceLocation DERSE = Minestuck.id("derse");
	private static final ResourceLocation METEOR = Minestuck.id("meteor");
	
	private static LandSkySpriteUploader INSTANCE;
	
	public static LandSkySpriteUploader getInstance()
	{
		return INSTANCE;
	}
	
	@SubscribeEvent
	public static void initUploader(RegisterClientReloadListenersEvent event)
	{
		event.registerReloadListener(INSTANCE = new LandSkySpriteUploader(Minecraft.getInstance().getTextureManager()));
	}
	
	public LandSkySpriteUploader(TextureManager textureManagerIn)
	{
		super(textureManagerIn, ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/atlas/land_sky.png"), ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "land_sky"));
	}
	
	public TextureAtlasSprite getSkaiaSprite()
	{
		return getSprite(SKAIA);
	}
	
	public TextureAtlasSprite getProspitSprite()
	{
		return getSprite(PROSPIT);
	}
	
	public TextureAtlasSprite getDerseSprite()
	{
		return getSprite(DERSE);
	}
	
	public TextureAtlasSprite getMeteorSprite()
	{
		return getSprite(METEOR);
	}
	
	public TextureAtlasSprite getPlanetSprite(TerrainLandType type, int index)
	{
		ResourceLocation typeName = LandTypes.TERRAIN_REGISTRY.getKey(type);
		Objects.requireNonNull(typeName);
		return getSprite(ResourceLocation.fromNamespaceAndPath(typeName.getNamespace(), "planets/planet_"+typeName.getPath()+"_"+index));
	}
	
	public TextureAtlasSprite getOverlaySprite(TitleLandType type, int index)
	{
		ResourceLocation typeName = LandTypes.TITLE_REGISTRY.getKey(type);
		Objects.requireNonNull(typeName);
		return getSprite(ResourceLocation.fromNamespaceAndPath(typeName.getNamespace(), "overlays/overlay_"+typeName.getPath()+"_"+index));
	}
}
