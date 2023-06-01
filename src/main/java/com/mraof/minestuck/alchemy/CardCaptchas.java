package com.mraof.minestuck.alchemy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CardCaptchas
{
	//TODO backupCaptchas are susceptible to changes on different world loads and modpack lists
	//TODO use of the character "#" not canonically accurate and suggests there are more punch holes in a captchalogue card then there actually are
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final String AVAILABLE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?";
	
	private final Set<String> backupCaptchas = new HashSet<>();
	private final Set<String> backupCaptchasMaster = new HashSet<>(); //entries are not removed from here
	
	private final BiMap<String, String> registryMap;
	
	public CardCaptchas()
	{
		registryMap = HashBiMap.create();
		generateBackupCaptchas();
	}
	
	public String createHash(String registryName)
	{
		StringBuilder hexString = new StringBuilder();
		
		try
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-256"); //SHA-256 hashing function
			
			byte[] inputBytes = registryName.getBytes(); //Convert the input string to bytes
			byte[] hashBytes = digest.digest(inputBytes); //Compute the hash
			
			// Convert the hash to a hexadecimal string
			for(byte b : hashBytes)
			{
				String hex = String.format("%02x", b);
				hexString.append(hex);
			}
		} catch(NoSuchAlgorithmException exception)
		{
			exception.printStackTrace();
		}
		
		return hexString.toString();
	}
	//world a ver 1, load 1-2
	//minecraft:air=xVo5arco, minestuck:eightball=qZ4DnIGU, minestuck:telescopic_sassacrusher=JELSeFdI, minestuck:democratic_demolitioner=qWPmBvv4, minestuck:wrinklefucker=zU?Tci2r, minestuck:pogo_hammer=4NrROKEV, minestuck:blacksmith_hammer=RJm06mlh, minestuck:mailbox=deFnOSOI, minestuck:sledge_hammer=IxDjt1uX, minestuck:claw_hammer=M?R8DHVf, minestuck:horizontal_green_stone_bricks=BGcfLuiQ, minestuck:jar_of_bugs=an5JF2h7, minestuck:green_stone_bricks=mSqqKab!, minestuck:onion=LX!hbm6h, minestuck:green_stone_column=yEtYmPzD, minestuck:black_crown_stained_glass=ji?URhAC, minestuck:checkered_stained_glass=yrP11Wd?, minestuck:black_pawn_stained_glass=29U9H9nR, minestuck:white_crown_stained_glass=8y8fH57?, minestuck:wireless_redstone_receiver=wuJe3iRQ, geckolib3:geckoarmor_chest=ny92q4uK, geckolib3:geckoarmor_head=299O?bDk, minestuck:bright_dense_cloud=3LP!XDFe, minestuck:chocolate_beetle=xRS8q!66, minestuck:cone_of_flies=SymczBJF, minestuck:grasshopper=?YjJtH7l, minestuck:cicada=Ty?dkKwo, minestuck:bug_mac=uzazwoRg, minestuck:salad=dmXkszuR, geckolib3:fertilizer=?MgdrIoP, geckolib3:habitat=jFwWg1Pc, geckolib3:geckoarmor_leggings=kKtETPVb, minestuck:netherrack_coal_ore=stvfB1jo, minestuck:end_stone_redstone_ore=VJSNifAA, minestuck:coarse_stone_bricks=Dyt234QJ, minestuck:shade_column=dsmt0yib, minestuck:frost_bricks=GYqbrUZb
	
	//world b ver 1, load 1-2
	
	/**
	 * Creates a captcha from the registry name of the item and then adds it to a BiMap.
	 * There is some simple collision detection and backup captchas for redundancy
	 */
	public void captchaFromItem(String registryName, Level level)
	{
		if(level.getServer() == null)
			return;
		
		long worldSeed = level.getServer().overworld().getSeed();
		Random seedRandom = new Random(worldSeed);
		
		//no need to run through the rest of the code if the registered item is already handled
		if(registryMap.containsKey(registryName))
			return;
		
		String fullHash = createHash(registryName);
		
		String shuffledHash = shuffleHash(seedRandom, fullHash);
		String cutHash = shuffledHash.substring(shuffledHash.length() - 16); //last 16 characters of hash
		String captcha = captchaFromHash(cutHash);
		
		if(registryMap.containsValue(captcha))
			captcha = getBackupCaptcha();
		
		registryMap.put(registryName, captcha); //adding an entry
		
		LOGGER.debug("\nRegistry name: {}\nCaptcha: {}\nHash: {}\nMap: {}", registryName, captcha, fullHash, registryMap);
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
			
			LOGGER.debug("{}, {}", hashSegment, charValue);
			
			captchaBuilder.append(AVAILABLE_CHARACTERS.charAt(charValue));
		}
		
		return captchaBuilder.toString();
	}
	
	/**
	 * Turns the hash string into a char array in order to reorganize the contents based on the world seed
	 */
	private String shuffleHash(Random random, String fullHash)
	{
		char[] characters = fullHash.toCharArray();
		
		for(int currentIndex = 0; currentIndex < characters.length; currentIndex++)
		{
			int randIndex = random.nextInt(currentIndex + 1);
			// Swap characters at indices currentIndex and randIndex
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
			Random random = new Random();
			
			//randomly picks the first 7 characters
			for(int i = 0; i < 7; i++)
			{
				int index = random.nextInt(AVAILABLE_CHARACTERS.length());
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