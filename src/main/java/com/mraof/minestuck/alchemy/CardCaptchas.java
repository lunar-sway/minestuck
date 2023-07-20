package com.mraof.minestuck.alchemy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.data.ShareCaptchasPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Handles the creation of, and look up of, captcha codes for each registered item in the given world.
 * The captcha is arrived at by hashing the registry name string, which gets some additional randomization via world seed.
 * Two worlds with the same seed will produce the same captcha for a given registered item, expect in some rare instances where there was hash collision.
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CardCaptchas
{
	//TODO use of the character "#" not canonically accurate and suggests there are more punch holes in a captchalogue card then there actually are
	
	public static final String AVAILABLE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?";
	public static final String EMPTY_CARD_CAPTCHA = "00000000";
	
	private static final BiMap<Item, String> REGISTRY_MAP = HashBiMap.create();
	
	private final Set<String> backupCaptchas = new HashSet<>();
	private final Set<String> backupCaptchasMaster = new HashSet<>(); //entries are not removed from here
	
	private final RandomSource seedRandomizer;
	
	private CardCaptchas(ServerLevel level)
	{
		this.seedRandomizer = RandomSource.create(level.getSeed());
		generateBackupCaptchas();
	}
	
	@SubscribeEvent
	public static void serverStarted(ServerStartedEvent event)
	{
		//TODO the second time a world loads, and every subsequent time after that, the captchas are different from the first time the world loads. Both before and after the change, server and client side are synced correctly
		iterateThroughRegistry(event.getServer());
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		Player player = event.getEntity();
		MinecraftServer server = player.getServer();
		if(server != null)
			MSPacketHandler.sendToPlayer(createShareCaptchasPacket(), (ServerPlayer) player);
	}
	
	private static ShareCaptchasPacket createShareCaptchasPacket()
	{
		ImmutableMap.Builder<Item, String> builder = new ImmutableMap.Builder<>();
		
		builder.putAll(REGISTRY_MAP);
		
		return new ShareCaptchasPacket(builder.build());
	}
	
	private String createHash(String registryName)
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
	 * Gets the registry name of the item and then returns its captcha or else returns null
	 */
	public static String getCaptcha(Item item)
	{
		return REGISTRY_MAP.get(item);
	}
	
	@Deprecated
	public static String getRegistryNameFromItem(Item item)
	{
		Optional<ResourceKey<Item>> resourceKey = item.builtInRegistryHolder().unwrapKey();
		
		return resourceKey.map(itemResourceKey -> itemResourceKey.location().toString()).orElseThrow();
	}
	
	@Nullable
	public static Item getItemFromCaptcha(String captcha)
	{
		return REGISTRY_MAP.inverse().get(captcha);
	}
	
	private static void iterateThroughRegistry(MinecraftServer server)
	{
		REGISTRY_MAP.clear();
		
		CardCaptchas cardCaptchas = new CardCaptchas(server.overworld());
		cardCaptchas.predetermineCaptcha(MSItems.GENERIC_OBJECT.get(), EMPTY_CARD_CAPTCHA);
		cardCaptchas.predetermineCaptcha(MSItems.SORD.get(), "SUPRePIC");
		
		//TODO consider a way of creating the captcha using the world seed and item id more directly to reduce resource requirements
		for(Map.Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries())
		{
			cardCaptchas.createItemsCaptcha(entry.getValue(), entry.getKey());
		}
	}
	
	/**
	 * Creates a captcha from the registry name of the item and then adds it to a BiMap.
	 * There is some simple collision detection and backup captchas for redundancy
	 */
	private void createItemsCaptcha(Item item, ResourceKey<Item> key)
	{
		//prevents reassignment of captcha to given item
		if(REGISTRY_MAP.containsKey(item))
			return;
		
		String fullHash = createHash(key.location().toString());
		
		String shuffledHash = shuffleHash(fullHash);
		String cutHash = shuffledHash.substring(shuffledHash.length() - 16); //last 16 characters of hash
		String captcha = captchaFromHash(cutHash);
		
		if(REGISTRY_MAP.containsValue(captcha))
			captcha = getBackupCaptcha();
		
		REGISTRY_MAP.put(item, captcha); //adding an entry
	}
	
	private void predetermineCaptcha(Item item, String captcha)
	{
		REGISTRY_MAP.put(item, captcha);
	}
	
	/**
	 * Takes 2 hexidecimal characters from the hash string to create an integer value between 0-255 and then performs modulo.
	 * The result will be equivalent to one of the characters in AVAILABLE_CHARACTERS
	 */
	private String captchaFromHash(String cutHash)
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
	private String shuffleHash(String fullHash)
	{
		char[] characters = fullHash.toCharArray();
		
		for(int currentIndex = 0; currentIndex < characters.length; currentIndex++)
		{
			int randIndex = seedRandomizer.nextInt(currentIndex + 1);
			
			//swap characters at indices currentIndex and randIndex
			char temp = characters[currentIndex];
			characters[currentIndex] = characters[randIndex];
			characters[randIndex] = temp;
		}
		
		return new String(characters);
	}
	
	/**
	 * Creates a Set of captchas that contain the character "#" which would not be possible to get naturally
	 */
	private void generateBackupCaptchas()
	{
		//theres a chance that two iterations of the for loop could produce the same captcha. Meaning the actual amount of entries is possibly lower than 50.
		//however if they run out, generateBackupCaptchas() gets run again
		for(int captchaIterate = 0; captchaIterate < 50; captchaIterate++)
		{
			StringBuilder captcha = new StringBuilder(8);
			
			//randomly picks the first 7 characters
			for(int i = 0; i < 7; i++)
			{
				int index = seedRandomizer.nextInt(AVAILABLE_CHARACTERS.length());
				char randomChar = AVAILABLE_CHARACTERS.charAt(index);
				captcha.append(randomChar);
			}
			
			captcha.append("#"); //adds "#" as the last character
			
			String captchaString = captcha.toString();
			
			//checks to make sure the captcha has not been created before and was deleted
			if(!backupCaptchasMaster.contains(captchaString))
			{
				backupCaptchasMaster.add(captchaString);
				backupCaptchas.add(captchaString);
			}
		}
	}
	
	/**
	 * Returns a backup captcha from the Set and also removes it from the Set to prevent reuse
	 */
	private String getBackupCaptcha()
	{
		//shouldnt cause issues unless the number of registered items is in the trillions
		if(backupCaptchas.isEmpty())
			generateBackupCaptchas();
		
		String pickedCaptcha = backupCaptchas.stream().iterator().next();
		
		backupCaptchas.remove(pickedCaptcha);
		return pickedCaptcha;
	}
}