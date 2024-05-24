package com.mraof.minestuck.data;

import com.google.gson.*;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.weapon.MSToolType;
import com.mraof.minestuck.item.weapon.WeaponItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.mraof.minestuck.item.MSItemTypes.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BetterCombatProvider implements DataProvider
{
	private final PackOutput output;
	
	public BetterCombatProvider(PackOutput output)
	{
		this.output = output;
	}
	
	/**
	 * First gets all the weapons from MSItems and adds it to a Map where the key is the registry name and the value is the data.
	 * Then it checks for custom defined weapons to use either in addition to the existing entries or to replace
	 */
	@Override
	public final CompletableFuture<?> run(CachedOutput cache)
	{
		List<CompletableFuture<?>> futures = new ArrayList<>();
		Path basePath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(Minestuck.MOD_ID).resolve("weapon_attributes");
		
		Map<String, String> weaponWithParent = new HashMap<>();
		
		MSItems.REGISTER.getEntries().stream().filter(itemHolder ->
		{
			if(itemHolder.asOptional().isEmpty())
				return false;
			
			return itemHolder.get() instanceof WeaponItem weaponItem && weaponItem.getToolType() != null;
		}).forEach(weaponHolder ->
		{
			String parent = getWeaponParent(((WeaponItem) weaponHolder.get().asItem()).getToolType());
			
			if(!parent.isEmpty())
				weaponWithParent.put(weaponHolder.getKey().location().getPath(), parent);
		});
		
		//TODO custom defined weapons method call here
		
		for(Map.Entry<String, String> entry : weaponWithParent.entrySet())
		{
			JsonObject object = new JsonObject();
			
			object.addProperty("parent", entry.getValue());
			
			futures.add(DataProvider.saveStable(cache, object, basePath.resolve(entry.getKey() + ".json")));
		}
		
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	public String getWeaponParent(MSToolType toolType)
	{
		if(toolType == SICKLE_TOOL)
			return "bettercombat:sickle";
		else if(toolType == CLAWS_TOOL)
			return "bettercombat:claw";
		else if(toolType == HAMMER_TOOL)
			return "bettercombat:hammer";
		else if(toolType == AXE_TOOL)
			return "bettercombat:heavy_axe";
		else if(toolType == SHOVEL_TOOL)
			return "bettercombat:mace";
		else if(toolType == SWORD_TOOL)
			return "bettercombat:sword";
		else if(toolType == AXE_HAMMER_TOOL)
			return "bettercombat:double_axe";
		return "";
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Better Combat Weapon Files";
	}
}