package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.computer.ProgramData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.item.ReadableSburbCodeItem;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.ServerOpListEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class ComputerTileEntity extends BlockEntity implements ISburbComputer
{	//TODO The implementation of this class need a serious rewrite
	public ComputerTileEntity(BlockPos pos, BlockState state)
	{
		super(MSTileEntityTypes.COMPUTER.get(), pos, state);
	}
	
	/**
	 * 0 = client, 1 = server, 2 = disk burner
	 */
	public Hashtable<Integer, Boolean> installedPrograms = new Hashtable<Integer, Boolean>();
	public ComputerScreen gui;
	public PlayerIdentifier owner;
	//client side only
	public int ownerId;
	public Hashtable<Integer, String> latestmessage = new Hashtable<Integer, String>();
	public CompoundTag programData = new CompoundTag();
	public int programSelected = -1;
	public List<Block> hieroglyphsStored = new ArrayList<>();
	public boolean hasParadoxInfoStored = false; //sburb code component received from the lotus flower
	public int blankDisksStored;
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		if (nbt.contains("programs"))
		{
			CompoundTag programs = nbt.getCompound("programs");
			for (Object name : programs.getAllKeys())
			{
				installedPrograms.put(programs.getInt((String) name), true);
			}
		}
		
		latestmessage.clear();
		for(Entry<Integer, Boolean> e : installedPrograms.entrySet())
			if(e.getValue())
				latestmessage.put(e.getKey(), nbt.getString("text" + e.getKey()));
		
		programData = nbt.getCompound("programData");
		
		if(nbt.contains("ownerId"))
			ownerId = nbt.getInt("ownerId");
		else this.owner = IdentifierHandler.load(nbt, "owner");
		if(gui != null)
			gui.updateGui();
		
		if(nbt.contains("hieroglyphsStored"))
			hieroglyphsStored = ReadableSburbCodeItem.getRecordedBlocks(nbt.getList("hieroglyphsStored", Tag.TAG_STRING));
		if(nbt.contains("hasParadoxInfoStored"))
			hasParadoxInfoStored = nbt.getBoolean("hasParadoxInfoStored");
		if(nbt.contains("blankDisksStored"))
			blankDisksStored = nbt.getInt("blankDisksStored");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		CompoundTag programs = new CompoundTag();
		Iterator<Entry<Integer, Boolean>> it = this.installedPrograms.entrySet().iterator();
		//int place = 0;
		while(it.hasNext())
		{
			Map.Entry<Integer, Boolean> pairs = it.next();
			int program = pairs.getKey();
			programs.putInt("program" + program, program);
			//place++;
		}
		for(Entry<Integer, String> e : latestmessage.entrySet())
			compound.putString("text" + e.getKey(), e.getValue());
		compound.put("programs", programs);
		compound.put("programData", programData.copy());
		if(owner != null)
			owner.saveToNBT(compound, "owner");
		
		ListTag hieroglyphListTag = ReadableSburbCodeItem.getListTagFromBlockList(hieroglyphsStored);
		if(!hieroglyphListTag.isEmpty())
		{
			compound.put("hieroglyphsStored", hieroglyphListTag);
		}
		compound.putBoolean("hasParadoxInfoStored", hasParadoxInfoStored);
		
		compound.putInt("blankDisksStored", blankDisksStored);
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag tagCompound = this.saveWithoutMetadata();
		tagCompound.remove("owner");
		tagCompound.remove("ownerMost");
		tagCompound.remove("ownerLeast");
		if(owner != null)
			tagCompound.putInt("ownerId", owner.getId());
		if(hasProgram(1))
		{
			SburbConnection c = SkaianetHandler.get(getLevel()).getServerConnection(this);
			if(c != null)
				tagCompound.getCompound("programData").getCompound("program_1").putInt("connectedClient", c.getClientIdentifier().getId());
		}
		return tagCompound;
	}
	
	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	public boolean hasProgram(int id)
	{
		return installedPrograms.get(id) == null ? false : installedPrograms.get(id);
	}

	public CompoundTag getData(int id)
	{
		if(!programData.contains("program_"+id))
			programData.put("program_" + id, new CompoundTag());
		return programData.getCompound("program_" + id);
	}
	
	public void closeAll()
	{
		for(Entry<Integer, Boolean> entry : installedPrograms.entrySet())
			if(entry.getValue() && entry.getKey() != -1)
				ProgramData.closeProgram(entry.getKey(), this);
	}
	
	@Override
	public void connected(PlayerIdentifier player, boolean isClient)
	{
		if(isClient)
		{
			getData(0).putBoolean("isResuming", false);
			getData(0).putBoolean("connectedToServer", true);
		} else
		{
			getData(1).putBoolean("isOpen", false);
		}
		setChanged();
		markBlockForUpdate();
	}
	
	@Override
	public PlayerIdentifier getOwner()
	{
		return owner;
	}
	
	@Override
	public ComputerReference createReference()
	{
		return ComputerReference.of(this);
	}
	
	@Override
	public boolean getClientBoolean(String name)
	{
		return getData(0).getBoolean(name);
	}
	
	@Override
	public boolean getServerBoolean(String name)
	{
		return getData(1).getBoolean(name);
	}
	
	@Override
	public void putClientBoolean(String name, boolean value)
	{
		getData(0).putBoolean(name, value);
		setChanged();
		markBlockForUpdate();
	}
	
	@Override
	public void putServerBoolean(String name, boolean value)
	{
		getData(1).putBoolean(name, value);
		setChanged();
		markBlockForUpdate();
	}
	
	@Override
	public void clearConnectedClient()
	{
		getData(1).putString("connectedClient", "");
		setChanged();
		markBlockForUpdate();
	}
	
	@Override
	public void putClientMessage(String message)
	{
		latestmessage.put(0, message);
		setChanged();
		markBlockForUpdate();
	}
	
	@Override
	public void putServerMessage(String message)
	{
		latestmessage.put(1, message);
		setChanged();
		markBlockForUpdate();
	}
	
	public void markBlockForUpdate()
	{
		BlockState state = level.getBlockState(worldPosition);
		this.level.sendBlockUpdated(worldPosition, state, state, 3);
	}
	
	public static void forNetworkIfPresent(ServerPlayer player, BlockPos pos, Consumer<ComputerTileEntity> consumer)
	{
		if(player.level.isAreaLoaded(pos, 0))    //TODO also check distance to the computer pos (together with a continual check clientside)
		{
			if(player.level.getBlockEntity(pos) instanceof ComputerTileEntity computer)
			{
				MinecraftServer mcServer = Objects.requireNonNull(player.getServer());
				ServerOpListEntry opsEntry = mcServer.getPlayerList().getOps().get(player.getGameProfile());
				if((!MinestuckConfig.SERVER.privateComputers.get() || IdentifierHandler.encode(player) == computer.owner || opsEntry != null && opsEntry.getLevel() >= 2) && ServerEditHandler.getData(player) == null)
					consumer.accept(computer);
			}
		}
	}
}
