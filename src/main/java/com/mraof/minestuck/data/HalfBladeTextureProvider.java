package com.mraof.minestuck.data;

import com.google.common.hash.Hashing;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Generates the textures of the half bladekind weapons by cutting the textures of the full weapons, see {@link HalfBladeCutter}.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HalfBladeTextureProvider implements DataProvider
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final String TEXTURE_FOLDER = "textures/item";
	private static final String HALF_PREFIX = "half_";
	
	private final PackOutput output;
	private final ExistingFileHelper fileHelper;
	private final List<Entry> toGenerate = new ArrayList<>();
	private final List<Entry> toKeep = new ArrayList<>();
	
	private record Entry(String name, String baseTexture)
	{
	}
	
	public HalfBladeTextureProvider(PackOutput output, ExistingFileHelper fileHelper)
	{
		this.output = output;
		this.fileHelper = fileHelper;
		
		registerTextures();
	}
	
	private void registerTextures()
	{
		MSItems.REGISTER.getEntries().forEach(holder -> {
			String path = holder.getId().getPath();
			if(path.startsWith(HALF_PREFIX)) addHalf(path.substring(HALF_PREFIX.length()));
		});
	}
	
	private void addHalf(String name)
	{
		ResourceLocation halfTexture = Minestuck.id(HALF_PREFIX + name);
		if(EvolutionBlacklistDetector.isUnsupported(fileHelper, Minestuck.id(name)))
		{
			LOGGER.warn("{} has an animated texture or a 3D model, so it can't evolve and no texture is made for {}. It should not have a half item", Minestuck.id(name), halfTexture);
			return;
		}
		
		String baseTexture = EvolutionBlacklistDetector.BASE_TEXTURE_OVERRIDES.getOrDefault(name, name);
		if(Files.exists(texturePath(HALF_PREFIX + name)))
		{
			//already generated or replaced
			toKeep.add(new Entry(name, baseTexture));
			fileHelper.trackGenerated(halfTexture, PackType.CLIENT_RESOURCES, ".png", TEXTURE_FOLDER);
			return;
		}
		
		if(!canBeCut(baseTexture))
		{
			LOGGER.warn("The texture {} can't be generated from {} (missing, not square or animated), so it has to be drawn by hand and placed in {}.", halfTexture, baseTexture, texturePath(HALF_PREFIX + name).getParent());
			return;
		}
		
		toGenerate.add(new Entry(name, baseTexture));
		//this has to happen now cuz the item models are checked for missing textures
		fileHelper.trackGenerated(halfTexture, PackType.CLIENT_RESOURCES, ".png", TEXTURE_FOLDER);
	}
	
	private Path texturePath(String textureName)
	{
		return output.getOutputFolder().resolve("assets/" + Minestuck.MOD_ID + "/" + TEXTURE_FOLDER + "/" + textureName + ".png");
	}
	
	private boolean canBeCut(String baseTexture)
	{
		ResourceLocation texture = Minestuck.id(baseTexture);
		if(!fileHelper.exists(texture, PackType.CLIENT_RESOURCES, ".png", TEXTURE_FOLDER) || fileHelper.exists(texture, PackType.CLIENT_RESOURCES, ".png.mcmeta", TEXTURE_FOLDER))
			return false; //missing or animated
		
		try(InputStream input = fileHelper.getResource(texture, PackType.CLIENT_RESOURCES, ".png", TEXTURE_FOLDER).open())
		{
			BufferedImage image = ImageIO.read(input);
			return HalfBladeCutter.isSupportedSize(image.getWidth(), image.getHeight());
		} catch(IOException e)
		{
			throw new UncheckedIOException("Unable to read the texture " + texture, e);
		}
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		return CompletableFuture.runAsync(() -> {
			for(Entry entry : toKeep)
				keep(cache, entry);
			for(Entry entry : toGenerate)
				generate(cache, entry);
		});
	}
	
	private void generate(CachedOutput cache, Entry entry)
	{
		try
		{
			BufferedImage base;
			try(InputStream input = fileHelper.getResource(Minestuck.id(entry.baseTexture()), PackType.CLIENT_RESOURCES, ".png", TEXTURE_FOLDER).open())
			{
				base = ImageIO.read(input);
			}
			
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			ImageIO.write(HalfBladeCutter.cut(base), "png", bytes);
			byte[] data = bytes.toByteArray();
			
			cache.writeIfNeeded(texturePath(HALF_PREFIX + entry.name()), data, Hashing.sha1().hashBytes(data));
		} catch(IOException e)
		{
			throw new UncheckedIOException("Unable to generate " + HALF_PREFIX + entry.name() + " from the texture " + entry.baseTexture(), e);
		}
	}
	
	/**
	 * Registers an existing texture in the cache without changing it, so the cache doesn't remove it as unused.
	 */
	private void keep(CachedOutput cache, Entry entry)
	{
		Path path = texturePath(HALF_PREFIX + entry.name());
		try
		{
			byte[] data = Files.readAllBytes(path);
			cache.writeIfNeeded(path, data, Hashing.sha1().hashBytes(data));
		} catch(IOException e)
		{
			throw new UncheckedIOException("Unable to read the texture " + path, e);
		}
	}
	
	@Override
	public String getName()
	{
		return "Half bladekind textures";
	}
}