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
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LandSkySpriteUploader extends TextureAtlasHolder
{
	public static final int VARIANT_COUNT = 3;
	
	private static final ResourceLocation SKAIA = new ResourceLocation(Minestuck.MOD_ID, "skaia");
	
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
		super(textureManagerIn, new ResourceLocation(Minestuck.MOD_ID, "textures/atlas/land_sky.png"), new ResourceLocation(Minestuck.MOD_ID, "land_sky"));
	}
	
	public TextureAtlasSprite getSkaiaSprite()
	{
		return getSprite(SKAIA);
	}
	
	public TextureAtlasSprite getPlanetSprite(TerrainLandType type, int index)
	{
		ResourceLocation typeName = LandTypes.TERRAIN_REGISTRY.getKey(type);
		Objects.requireNonNull(typeName);
		return getSprite(new ResourceLocation(typeName.getNamespace(), "planets/planet_"+typeName.getPath()+"_"+index));
	}
	
	public TextureAtlasSprite getOverlaySprite(TitleLandType type, int index)
	{
		ResourceLocation typeName = LandTypes.TITLE_REGISTRY.getKey(type);
		Objects.requireNonNull(typeName);
		return getSprite(new ResourceLocation(typeName.getNamespace(), "overlays/overlay_"+typeName.getPath()+"_"+index));
	}
}
