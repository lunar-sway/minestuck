package com.mraof.minestuck.world;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.LandAspects;
import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.lands.LandInfoContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class MinestuckDimensionHandler
{
	public static int biomeIdStart;
	public static final ResourceLocation SKAIA_ID = new ResourceLocation(Minestuck.MOD_ID, "skaia");
	
	private static Exception unregisterTrace;
	private static Hashtable<IdentifierHandler.PlayerIdentifier, LandInfoContainer> landInfo = new Hashtable<>();
	private static Hashtable<IdentifierHandler.PlayerIdentifier, DimensionType> lands = new Hashtable<>();
	public static DimensionType skaia;
	
	public static ModDimension landDimensionType;
	public static ModDimension skaiaDimensionType;
	
	public static void registerModDimensions(IForgeRegistry<ModDimension> registry)
	{
		landDimensionType = new LandDimension.Type();
		registry.register(landDimensionType.setRegistryName("lands"));
		skaiaDimensionType = new SkaiaDimension.Type();
		registry.register(skaiaDimensionType.setRegistryName("skaia"));
	}
	
	/**
	 * On server init, this function is called to register dimensions.
	 * The dimensions registered will then be sent and registered by forge client-side.
	 */
	@SubscribeEvent
	public static void registerDimensionTypes(final RegisterDimensionsEvent event)
	{
		lands.clear();
		
		//register dimensions
		skaia = DimensionType.byName(SKAIA_ID);
		if(skaia == null)
			skaia = DimensionManager.registerDimension(SKAIA_ID, skaiaDimensionType, null);
		
		for(LandInfoContainer container : landInfo.values())
		{
			DimensionType type = DimensionManager.registerDimension(container.name, landDimensionType, null);
			lands.put(container.identifier, type);
		}
	}
	
	/*public static void saveData(NBTTagCompound nbt)
	{
		if(unregisterTrace != null)
		{
			throw new IllegalStateException("Saving minestuck dimension data after unregistering dimensions. This is bad!", unregisterTrace);
		}
		
		NBTTagList list = new NBTTagList();
		for(Map.Entry<Integer, LandAspectRegistry.AspectCombination> entry : lands.entrySet())
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setInteger("dimID", entry.getKey());
			tagCompound.setString("type", "land");
			tagCompound.setString("aspect1", entry.getValue().aspectTerrain.getPrimaryName());
			tagCompound.setString("aspect2", entry.getValue().aspectTitle.getPrimaryName());
			BlockPos spawn = spawnpoints.get(entry.getKey());
			tagCompound.setInteger("spawnX", spawn.getX());
			tagCompound.setInteger("spawnY", spawn.getY());
			tagCompound.setInteger("spawnZ", spawn.getZ());
			list.appendTag(tagCompound);
		}
		GateHandler.saveData(list);
		nbt.setTag("dimensionData", list);
	}
	
	public static void loadData(NBTTagCompound nbt)
	{
		unregisterTrace = null;
		if(nbt == null)
		{
			return;
		}
		
		NBTTagList list = nbt.getTagList("dimensionData", new NBTTagCompound().getId());
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound tagCompound = list.getCompoundTagAt(i);
			int dim = tagCompound.getInteger("dimID");
			String type = tagCompound.getString("type");
			if(type.equals("land"))
			{
				String name1 = tagCompound.getString("aspect1");
				String name2 = tagCompound.getString("aspect2");
				LandAspectRegistry.AspectCombination aspects = new LandAspectRegistry.AspectCombination(LandAspectRegistry.fromNameTerrain(name1), LandAspectRegistry.fromNameTitle(name2));
				BlockPos spawn = new BlockPos(tagCompound.getInteger("spawnX"), tagCompound.getInteger("spawnY"), tagCompound.getInteger("spawnZ"));
				
				lands.put(dim, aspects);
				spawnpoints.put(dim, spawn);
				DimensionManager.registerDimension(dim, landDimensionType);
				Debug.debugf("Loaded minestuck info on land dimension %d", dim);
			} else Debug.warnf("Found data on a non-land dimension in the minestuck data (%d). Are you running a newer world on an older version?", dim);
		}
		Debug.debugf("Loaded minestuck data for %d land dimensions out of %d entries.", lands.size(), list.tagCount());
		GateHandler.loadData(list);
	}
	
	public static void registerLandDimension(int dimensionId, LandAspects landAspects)
	{
		if(landAspects == null)
			throw new IllegalArgumentException("May not register a land aspect combination that is null");
		if(!lands.containsKey(dimensionId) && !DimensionManager.isDimensionRegistered(dimensionId))
		{
			lands.put(dimensionId, landAspects);
			DimensionManager.registerDimension(dimensionId, landDimensionType);
		}
		else Debug.warnf("Did not register land dimension with id %d. Appears to already be registered.", dimensionId);
	}*/
	
	public static LandAspects getAspects(DimensionType dimension)
	{
		return null;
		/*LandAspects aspects = lands.get(dimensionId);
		
		if(aspects == null)
		{
			Debug.warnf("Tried to access land aspect for dimension %d, but didn't find any!", dimensionId);
		}
		
		return aspects;*/
	}
	
	public static boolean isLandDimension(DimensionType dimension)
	{
		return dimension != null && dimension.getModType() == landDimensionType;
	}
	
	public static boolean isSkaia(DimensionType dimension)
	{
		return dimension == skaia;
	}
	
	/*public static Set<Map.Entry<Integer, LandAspects>> getLandSet()
	{
		return lands.entrySet();
	}
	
	public static void onLandPacket(LandRegisterPacket packet)
	{
		if(Minestuck.isServerRunning)
			return;
		unregisterTrace = null;
		lands.clear();
		spawnpoints.clear();
		
		lands.putAll(packet.aspectMap);
		spawnpoints.putAll(packet.spawnMap);
		
		for(int dim : lands.keySet())
		{
			if(!DimensionManager.isDimensionRegistered(dim))
				DimensionManager.registerDimension(dim, landDimensionType);
		}
	}*/
}