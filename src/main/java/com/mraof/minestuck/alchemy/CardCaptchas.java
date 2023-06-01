package com.mraof.minestuck.alchemy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CardCaptchas
{
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
	
	//TODO backupCaptchas are susceptible to changes on different world loads and modpack lists
	//TODO use of the character "#" not canonically accurate
	
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
	
	/**
	 * Creates a captcha from the registry name of the item and then adds it to a BiMap.
	 * There is some simple collision detection and backup captchas for redundancy
	 */
	public void captchaFromItem(String registryName)
	{
		//no need to run through the rest of the code if the registered item is already handled
		if(registryMap.containsKey(registryName))
			return;
		
		String fullHash = createHash(registryName);
		String cutHash = fullHash.substring(fullHash.length() - 16); //TODO check if using the end of the hash instead of the beginning improves variablity
		StringBuilder captchaBuilder = new StringBuilder();
		
		//takes 4 characters as 4 different single digit hex values and adds them together. The result will be equivalent to one of the characters in AVAILABLE_CHARACTERS
		for(int captchaElement = 0; captchaElement < 8; captchaElement++)
		{
			String hashSegment = cutHash.substring(captchaElement * 2, captchaElement * 2 + 2);
			int decimalValue = Integer.parseInt(hashSegment, 16);
			int charValue = decimalValue % 64;
			//int decimalValue = Integer.parseInt(hashSegment, 64);
			//int decimalValue = ByteBuffer.wrap(Base64.getDecoder().decode(hashSegment.getBytes())).getInt();
			//int decimalValue = 0;
			
			/*for(int iterateHex = 0; iterateHex < 4; iterateHex++)
			{
				//int parsedInt = Integer.parseInt(hashSegment.substring(iterateHex), 16);
				char hexChar = hashSegment.charAt(iterateHex);
				int parsedInt = Character.digit(hexChar, 64);
				
				decimalValue = decimalValue + parsedInt;
			}*/
			LOGGER.debug("{}, {}", hashSegment, charValue);
			
			captchaBuilder.append(AVAILABLE_CHARACTERS.charAt(charValue));
		}
		
		String captcha = captchaBuilder.toString();
		
		if(registryMap.containsValue(captcha))
			captcha = getBackupCaptcha();
		
		registryMap.put(registryName, captcha); //adding an entry
		
		LOGGER.debug("\nRegistry name: {}\nCaptcha: {}\nHash: {}\nMap: {}", registryName, captcha, fullHash, registryMap);
		
		/*byte[] bytes = registryName.getBytes(StandardCharsets.UTF_8);
		String encodedName = Base64.getEncoder().encodeToString(bytes).replaceAll("=", "");
		String captcha;
		
		if(encodedName.length() <= 8)
			captcha = getBackupCaptcha(); //shouldnt be needed, but is a useful precaution
		else
			captcha = checkForCollision(encodedName, encodedName.substring(encodedName.length() - 8));
		
		registryMap.put(registryName, captcha); //adding an entry
		
		String registryNameFromHash = registryMap.inverse().get(captcha);
		
		LOGGER.debug("Registry name: {}\nEncoded name: {}\nCaptcha: {}\nMap: {}\nName inversed: {}", registryName, encodedName, captcha, registryMap, registryNameFromHash);*/
	}
	
	private String checkForCollision(String encodedName, String captcha)
	{
		//if the original captcha is fine, return that
		if(!registryMap.containsValue(captcha)) //TODO was .inverse().containsKey(). Make sure the switch to .containsValue() was okay
			return captcha;
		
		
		//if the captcha is already being used, it looks at random different substrings of encodedName for unused captchas
		for(int i = 0; i < encodedName.length() - 8; i++)
		{
			if(registryMap.containsValue(captcha))
			{
				Random rand = new Random();
				int randSublocation = rand.nextInt(encodedName.length() - 1);
				captcha = encodedName.substring(randSublocation - 8, randSublocation);
				
				//int iteratedEnd = encodedName.length() - i;
				//captcha = encodedName.substring(iteratedEnd - 8, iteratedEnd);
			} else
				return captcha;
		}
		
		//if the final for loop did not result in a usable captcha, pulls a backup captcha
		if(registryMap.inverse().containsKey(captcha))
			return getBackupCaptcha();
		else
			return captcha;
	}
}
