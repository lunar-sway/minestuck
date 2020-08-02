package com.mraof.minestuck.item;

import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Similar to {@link MusicDiscItem} but with a public constructor and instead works with a supplier (and as such works with mod sound events)
 */
public class ModMusicDiscItem extends MusicDiscItem
{
	private final Supplier<SoundEvent> soundEvent;
	
	public ModMusicDiscItem(int comparatorValueIn, Supplier<SoundEvent> soundIn, Properties builder)
	{
		super(comparatorValueIn, soundIn, builder);
		this.soundEvent = soundIn;
	}
	
	@Override
	public SoundEvent getSound()
	{
		return soundEvent.get();
	}
	
	private void addSoundEventToMap(Map<SoundEvent, MusicDiscItem> map)
	{
		map.put(soundEvent.get(), this);
	}
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	// A slightly hacky way to properly populate the private map in MusicDiscItem once sound events has been loaded
	public static void setup()
	{
		try
		{
			Map<SoundEvent, MusicDiscItem> map = ObfuscationReflectionHelper.getPrivateValue(MusicDiscItem.class, null, "field_150928_b");
			Objects.requireNonNull(map);
			
			for(Item item : ForgeRegistries.ITEMS)
			{
				if(item instanceof ModMusicDiscItem)
					((ModMusicDiscItem) item).addSoundEventToMap(map);
			}
		} catch(Exception e)
		{
			LOGGER.warn("Got exception while getting the records map through reflection. Minestuck music discs might not have the right title when playing.", e);
		}
	}
}