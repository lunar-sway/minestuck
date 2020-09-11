package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LandSkySpriteUploader extends SpriteUploader
{
	public static final int VARIANT_COUNT = 3;
	
	private static final ResourceLocation SKAIA = new ResourceLocation(Minestuck.MOD_ID, "skaia");
	
	private static LandSkySpriteUploader INSTANCE;
	
	public static LandSkySpriteUploader getInstance()
	{
		return INSTANCE;
	}
	
	@SubscribeEvent
	public static void initUploader(ParticleFactoryRegisterEvent event)
	{
		System.out.println("Creating land planet resource");
		Minecraft mc = Minecraft.getInstance();
		((IReloadableResourceManager) mc.getResourceManager()).addReloadListener(INSTANCE = new LandSkySpriteUploader(mc.getTextureManager()));
	}
	
	public LandSkySpriteUploader(TextureManager textureManagerIn)
	{
		super(textureManagerIn, new ResourceLocation(Minestuck.MOD_ID, "textures/atlas/land_sky.png"), "environment");
	}
	
	@Override
	protected Stream<ResourceLocation> getResourceLocations()
	{
		return Stream.concat(extras(), addVariantIndex(Stream.concat(planetLocations(), overlayLocations())));
	}
	
	private Stream<ResourceLocation> planetLocations()
	{
		return LandTypes.TERRAIN_REGISTRY.getKeys().stream().map(name -> new ResourceLocation(name.getNamespace(), "planets/planet_"+name.getPath()));
	}
	
	private Stream<ResourceLocation> overlayLocations()
	{
		return LandTypes.TITLE_REGISTRY.getKeys().stream().map(name -> new ResourceLocation(name.getNamespace(), "overlays/overlay_"+name.getPath()));
	}
	
	private Stream<ResourceLocation> extras()
	{
		return Stream.of(SKAIA);
	}
	
	private Stream<ResourceLocation> addVariantIndex(Stream<ResourceLocation> stream)
	{
		return stream.flatMap(name -> IntStream.range(0, VARIANT_COUNT).mapToObj(value -> new ResourceLocation(name.getNamespace(), name.getPath()+"_"+value)));
	}
	
	public TextureAtlasSprite getSkaiaSprite()
	{
		return getSprite(SKAIA);
	}
	
	public TextureAtlasSprite getPlanetSprite(TerrainLandType type, int index)
	{
		return getSprite(new ResourceLocation(type.getRegistryName().getNamespace(), "planets/planet_"+type.getRegistryName().getPath()+"_"+index));
	}
	
	public TextureAtlasSprite getOverlaySprite(TitleLandType type, int index)
	{
		return getSprite(new ResourceLocation(type.getRegistryName().getNamespace(), "overlays/overlay_"+type.getRegistryName().getPath()+"_"+index));
	}
}
