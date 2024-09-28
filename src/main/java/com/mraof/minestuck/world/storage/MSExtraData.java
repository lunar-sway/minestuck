package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.CardCaptchas;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.entry.PostEntryTask;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Stores any extra data that's not worth putting in their own data file. (Such as editmode recovery data and post entry tasks, which most of the time will be empty)
 * @author kirderf1
 */
public class MSExtraData extends SavedData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final String DATA_NAME = Minestuck.MOD_ID+"_extra";
	
	private final List<EditData> activeEditData = new ArrayList<>();
	
	private final Map<UUID, EditData.PlayerRecovery> editPlayerRecovery = new HashMap<>();
	private final List<EditData.ConnectionRecovery> editConnectionRecovery = new ArrayList<>();
	
	private final CardCaptchas cardCaptchas = new CardCaptchas();
	private final List<PostEntryTask> postEntryTasks = new ArrayList<>();
	
	private MSExtraData()
	{
	}
	
	public static MSExtraData load(CompoundTag nbt)
	{
		MSExtraData data = new MSExtraData();
		
		data.activeEditData.clear();
		data.editPlayerRecovery.clear();
		data.editConnectionRecovery.clear();
		
		data.postEntryTasks.clear();
		
		ListTag editRecoveryList = nbt.getList("editmode_recovery", Tag.TAG_COMPOUND);
		for(int i = 0; i < editRecoveryList.size(); i++)
		{
			CompoundTag dataTag = editRecoveryList.getCompound(i);
			UUID playerID = dataTag.getUUID("player");
			data.editPlayerRecovery.put(playerID, EditData.readRecovery(dataTag));
			EditData.ConnectionRecovery recovery = EditData.readExtraRecovery(dataTag);
			if(recovery != null)
				data.editConnectionRecovery.add(recovery);
		}
		
		ListTag entryTaskList = nbt.getList("entry_tasks", Tag.TAG_COMPOUND);
		for(int i = 0; i < entryTaskList.size(); i++)
		{
			PostEntryTask.CODEC.parse(NbtOps.INSTANCE, entryTaskList.getCompound(i))
					.resultOrPartial(LOGGER::error).ifPresent(data.postEntryTasks::add);
		}
		
		if(nbt.contains("card_captchas", Tag.TAG_COMPOUND))
			data.cardCaptchas.deserialize(nbt.getCompound("card_captchas"));
		
		return data;
	}
	
	@Override
	public CompoundTag save(CompoundTag compound)
	{
		ListTag editRecoveryList = new ListTag();
		editRecoveryList.addAll(editPlayerRecovery.entrySet().stream().map(MSExtraData::writeRecovery).toList());
		editRecoveryList.addAll(activeEditData.stream().map(MSExtraData::writeRecovery).toList());
		
		compound.put("editmode_recovery", editRecoveryList);
		
		ListTag entryTaskList = new ListTag();
		postEntryTasks.stream()
				.flatMap(task -> PostEntryTask.CODEC.encodeStart(NbtOps.INSTANCE, task).resultOrPartial(LOGGER::error).stream())
				.forEach(entryTaskList::add);
		
		compound.put("entry_tasks", entryTaskList);
		
		compound.put("card_captchas", cardCaptchas.serialize());
		
		return compound;
	}
	
	private static CompoundTag writeRecovery(EditData data)
	{
		CompoundTag nbt = data.writeRecoveryData();
		nbt.putUUID("player", data.getEditor().getGameProfile().getId());
		return nbt;
	}
	
	private static CompoundTag writeRecovery(Map.Entry<UUID, EditData.PlayerRecovery> data)
	{
		CompoundTag nbt = data.getValue().write(new CompoundTag());
		nbt.putUUID("player", data.getKey());
		return nbt;
	}
	
	public static MSExtraData get(Level level)
	{
		MinecraftServer server = level.getServer();
		if(server == null)
			throw new IllegalArgumentException("Can't get extra data instance on client side! (Got null server from level)");
		return get(server);
	}
	
	public static MSExtraData get(MinecraftServer mcServer)
	{
		ServerLevel level = mcServer.getLevel(Level.OVERWORLD);
		
		DimensionDataStorage storage = level.getDataStorage();
		
		return storage.computeIfAbsent(new Factory<>(MSExtraData::new, MSExtraData::load), DATA_NAME);
	}
	
	@Nullable
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
	
	public void recoverConnections(MinecraftServer mcServer)
	{
		if(!editConnectionRecovery.isEmpty())
		{
			LOGGER.warn("Recovering extra connection data for {} players that were in editmode when the server shut down abruptly last session. An attempt to recover players will be made when they rejoin the server.", editConnectionRecovery.size());
			editConnectionRecovery.forEach(recovery -> recovery.recover(mcServer));
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
	
	public CardCaptchas getCardCaptchas()
	{
		return cardCaptchas;
	}
	
	@Override
	public boolean isDirty()
	{
		return super.isDirty() || !activeEditData.isEmpty(); //Always save at every opportunity if editmode is active for someone
	}
}
