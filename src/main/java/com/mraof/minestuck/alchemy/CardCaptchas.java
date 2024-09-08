package com.mraof.minestuck.alchemy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;

/**
 * Handles the creation of, and look up of, captcha codes for each registered item in the given world.
 * The captcha is arrived at by hashing the registry name string, which gets some additional randomization via world seed.
 * Two worlds with the same seed will produce the same captcha for a given registered item, expect in some rare instances where there was hash collision.
 */
public final class CardCaptchas
{
	//TODO use of the character "#" not canonically accurate and suggests there are more punch holes in a captchalogue card then there actually are
	
	public static final String AVAILABLE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?";
	public static final String EMPTY_CARD_CAPTCHA = "00000000";
	private final BiMap<Item, String> captchasMap = HashBiMap.create();
	private static final Logger LOGGER = LogManager.getLogger();
	
	public CardCaptchas()
	{
	
	}
	
	/**
	 * Gets the registry name of the item and then returns its captcha or else returns null
	 */
	@Nonnull
	public static String getCaptcha(Item item, MinecraftServer server)
	{
		MSExtraData data = MSExtraData.get(server);
		CardCaptchas captchas = data.getCardCaptchas();
		
		if(PredeterminedCardCaptchas.getData().containsKey(item))
		{
			if(captchas.captchasMap.containsKey(item))
			{
				LOGGER.warn("Conflict: Item {} already has Code '{}', removing generated Code '{}' from data.", item, PredeterminedCardCaptchas.getData().get(item), captchas.captchasMap.get(item));
				captchas.captchasMap.remove(item);
				data.setDirty();
			}
			return PredeterminedCardCaptchas.getData().get(item);
		}
		else if(captchas.captchasMap.containsKey(item))
		{
			return captchas.captchasMap.get(item);
		}
		else
		{
			String captcha = captchas.createCaptchaForItem(item, server.overworld().getSeed());
			captchas.captchasMap.put(item, captcha);
			data.setDirty();	//Make sure that the extra data gets saved when a new captcha has been generated
			return captcha;
		}
	}
	
	@Nullable
	public static Item getItemFromCaptcha(String captcha, MinecraftServer server)
	{
		MSExtraData data = MSExtraData.get(server);
		CardCaptchas captchas = data.getCardCaptchas();
		
		if(PredeterminedCardCaptchas.getData().inverse().containsKey(captcha))
		{
			if(captchas.captchasMap.inverse().containsKey(captcha))
			{
				LOGGER.warn("Conflict: Code '{}' already has assigned to Item {}, removing code '{}' reference to Item {}.", captcha, PredeterminedCardCaptchas.getData().inverse().get(captcha), captcha, captchas.captchasMap.inverse().get(captcha));
				captchas.captchasMap.inverse().remove(captcha);
				data.setDirty();
			}
			return PredeterminedCardCaptchas.getData().inverse().get(captcha);
		}
		else
		{
			return captchas.captchasMap.inverse().get(captcha);
		}
	}
	
	public CompoundTag serialize()
	{
		CompoundTag tag = new CompoundTag();
		for(Map.Entry<Item, String> entry : captchasMap.entrySet())
		{
			String itemName = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(entry.getKey())).toString();
			tag.putString(itemName, entry.getValue());
		}
		return tag;
	}
	
	public void deserialize(CompoundTag tag)
	{
		captchasMap.clear();
		for(String itemName : tag.getAllKeys())
		{
			ResourceLocation itemId = ResourceLocation.tryParse(itemName);
			if(itemId == null)
				continue;
			Item item = BuiltInRegistries.ITEM.get(itemId);
			if(item == null)
				continue;
			captchasMap.put(item, tag.getString(itemName));
		}
	}
	
	/**
	 * Creates a captcha from the registry name of the item and then adds it to a BiMap.
	 * There is some simple collision detection and backup captchas for redundancy
	 */
	private String createCaptchaForItem(Item item, long seed)
	{
		
		ResourceLocation itemId = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
		
		RandomSource itemRandom = RandomSource.create(seed)
				.forkPositional().fromHashOf("minestuck:item_captchas")
				.forkPositional().fromHashOf(itemId);
		String fullHash = createHash(itemId.toString());
		
		String shuffledHash = shuffleHash(fullHash, itemRandom);
		String cutHash = shuffledHash.substring(shuffledHash.length() - 16); //last 16 characters of hash
		String captcha = captchaFromHash(cutHash);
		
		if(captchasMap.containsValue(captcha) || PredeterminedCardCaptchas.getData().containsValue(captcha))
			return generateBackupCaptcha(itemRandom);
		
		return captcha;
	}
	
	private static String createHash(String registryName)
	{
		StringBuilder hexString = new StringBuilder();
		
		try
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-256"); //SHA-256 hashing function
			
			byte[] inputBytes = registryName.getBytes(); //Convert the input string to bytes
			byte[] hashBytes = digest.digest(inputBytes); //Compute the hash
			
			// Convert the hash to a hexadecimal string
			for(byte bytes : hashBytes)
			{
				String hex = String.format("%02x", bytes);
				hexString.append(hex);
			}
		} catch(NoSuchAlgorithmException exception)
		{
			exception.printStackTrace();
		}
		
		return hexString.toString();
	}
	
	/**
	 * Takes 2 hexidecimal characters from the hash string to create an integer value between 0-255 and then performs modulo.
	 * The result will be equivalent to one of the characters in AVAILABLE_CHARACTERS
	 */
	private static String captchaFromHash(String cutHash)
	{
		StringBuilder captchaBuilder = new StringBuilder();
		
		for(int captchaElement = 0; captchaElement < 8; captchaElement++)
		{
			String hashSegment = cutHash.substring(captchaElement * 2, captchaElement * 2 + 2);
			int decimalValue = Integer.parseInt(hashSegment, 16);
			int charValue = decimalValue % 64;
			
			captchaBuilder.append(AVAILABLE_CHARACTERS.charAt(charValue));
		}
		
		return captchaBuilder.toString();
	}
	
	/**
	 * Turns the hash string into a char array in order to reorganize the contents based on the world seed
	 */
	private static String shuffleHash(String fullHash, RandomSource itemRandom)
	{
		char[] characters = fullHash.toCharArray();
		
		for(int currentIndex = 0; currentIndex < characters.length; currentIndex++)
		{
			int randIndex = itemRandom.nextInt(currentIndex + 1);
			
			//swap characters at indices currentIndex and randIndex
			char temp = characters[currentIndex];
			characters[currentIndex] = characters[randIndex];
			characters[randIndex] = temp;
		}
		
		return new String(characters);
	}
	
	/**
	 * Creates a captcha that contains the character "#" which would not be possible to get naturally
	 */
	private String generateBackupCaptcha(RandomSource random)
	{
		//shouldn't cause issues unless the number of registered items is in the trillions
		while(true)
		{
			StringBuilder captcha = new StringBuilder(8);
			
			//randomly picks the first 7 characters
			for(int i = 0; i < 7; i++)
			{
				int index = random.nextInt(AVAILABLE_CHARACTERS.length());
				char randomChar = AVAILABLE_CHARACTERS.charAt(index);
				captcha.append(randomChar);
			}
			
			captcha.append("#"); //adds "#" as the last character
			
			String captchaString = captcha.toString();
			
			//checks to make sure the captcha has not been created before
			if(!captchasMap.containsValue(captchaString) && !PredeterminedCardCaptchas.getData().containsValue(captchaString))
			{
				return captchaString;
			}
		}
	}
}
