package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.entry.PostEntryTask;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Stores any extra data that's not worth putting in their own data file. (Such as editmode recovery data and post entry tasks, which most of the time will be empty)
 * @author kirderf1
 */
public class MSExtraData extends WorldSavedData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final String DATA_NAME = Minestuck.MOD_ID+"_extra";
	
	private final List<EditData> activeEditData = new ArrayList<>();
	
	private final Map<UUID, EditData.PlayerRecovery> editPlayerRecovery = new HashMap<>();
	private final List<EditData.ConnectionRecovery> editConnectionRecovery = new ArrayList<>();
	
	
	private final List<PostEntryTask> postEntryTasks = new ArrayList<>();
	
	private MSExtraData()
	{
		super(DATA_NAME);
	}
	
	@Override
	public void load(CompoundNBT nbt)
	{
		activeEditData.clear();
		editPlayerRecovery.clear();
		editConnectionRecovery.clear();
		
		postEntryTasks.clear();
		
		ListNBT editRecoveryList = nbt.getList("editmode_recovery", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < editRecoveryList.size(); i++)
		{
			CompoundNBT dataTag = editRecoveryList.getCompound(i);
			UUID playerID = dataTag.getUUID("player");
			editPlayerRecovery.put(playerID, EditData.readRecovery(dataTag));
			EditData.ConnectionRecovery recovery = EditData.readExtraRecovery(dataTag);
			if(recovery != null)
				editConnectionRecovery.add(recovery);
		}
		
		ListNBT entryTaskList = nbt.getList("entry_tasks", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < entryTaskList.size(); i++)
		{
			CompoundNBT tag = entryTaskList.getCompound(i);
			postEntryTasks.add(new PostEntryTask(tag));
		}
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		ListNBT editRecoveryList = new ListNBT();
		editRecoveryList.addAll(editPlayerRecovery.entrySet().stream().map(MSExtraData::writeRecovery).collect(Collectors.toList()));
		editRecoveryList.addAll(activeEditData.stream().map(MSExtraData::writeRecovery).collect(Collectors.toList()));
		
		compound.put("editmode_recovery", editRecoveryList);
		
		ListNBT entryTaskList = new ListNBT();
		entryTaskList.addAll(postEntryTasks.stream().map(PostEntryTask::write).collect(Collectors.toList()));
		
		compound.put("entry_tasks", entryTaskList);
		
		return compound;
	}
	
	private static CompoundNBT writeRecovery(EditData data)
	{
		CompoundNBT nbt = data.writeRecoveryData();
		nbt.putUUID("player", data.getEditor().getGameProfile().getId());
		return nbt;
	}
	
	private static CompoundNBT writeRecovery(Map.Entry<UUID, EditData.PlayerRecovery> data)
	{
		CompoundNBT nbt = data.getValue().write(new CompoundNBT());
		nbt.putUUID("player", data.getKey());
		return nbt;
	}
	
	public static MSExtraData get(World world)
	{
		MinecraftServer server = world.getServer();
		if(server == null)
			throw new IllegalArgumentException("Can't get extra data instance on client side! (Got null server from world)");
		return get(server);
	}
	
	public static MSExtraData get(MinecraftServer mcServer)
	{
		ServerWorld world = mcServer.getLevel(World.OVERWORLD);
		
		DimensionSavedDataManager storage = world.getDataStorage();
		MSExtraData instance = storage.get(MSExtraData::new, DATA_NAME);
		
		if(instance == null)	//There is no save data, so insert a new instance
		{
			instance = new MSExtraData();
			storage.set(instance);
		}
		
		return instance;
	}
	
	public EditData findEditData(Predicate<EditData> condition)
	{
		for(EditData data : activeEditData)
		{
			if(condition.test(data))
				return data;
		}
		return null;
	}
	
	public void addEditData(EditData data)
	{
		activeEditData.add(data);
		setDirty();
	}
	
	public void removeEditData(EditData data)
	{
		if(activeEditData.remove(data))
			setDirty();
	}
	
	public void forEach(Consumer<EditData> consumer)
	{
		activeEditData.forEach(consumer);
	}
	
	public void forEachAndClear(Consumer<EditData> consumer)
	{
		if(!activeEditData.isEmpty())
		{
			forEach(consumer);
			activeEditData.clear();
			setDirty();
		}
	}
	
	public EditData.PlayerRecovery removePlayerRecovery(UUID playerID)
	{
		EditData.PlayerRecovery recovery = editPlayerRecovery.remove(playerID);
		if(recovery != null)
			setDirty();
		return recovery;
	}
	
	public void recoverConnections(Consumer<EditData.ConnectionRecovery> recover)
	{
		if(!editConnectionRecovery.isEmpty())
		{
			LOGGER.warn("Recovering extra connection data for {} players that were in editmode when the server shut down abruptly last session. An attempt to recover players will be made when they rejoin the server.", editConnectionRecovery.size());
			editConnectionRecovery.forEach(recover);
			editConnectionRecovery.clear();
			setDirty();
		}
	}
	
	public void addPostEntryTask(PostEntryTask task)
	{
		postEntryTasks.add(task);
		setDirty();
	}
	
	public void executeEntryTasks(MinecraftServer server)
	{
		for(PostEntryTask task : postEntryTasks)
		{
			if(task.onTick(server))
				setDirty();
		}
		
		if(postEntryTasks.removeIf(PostEntryTask::isDone))
			setDirty();
	}
	
	@Override
	public boolean isDirty()
	{
		return super.isDirty() || !activeEditData.isEmpty(); //Always save at every opportunity if editmode is active for someone
	}
}