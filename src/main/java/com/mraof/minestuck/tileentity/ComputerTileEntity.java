package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.computer.ProgramData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.OpEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Consumer;

public class ComputerTileEntity extends TileEntity implements ISburbComputer
{	//TODO The implementation of this class need a serious rewrite
	public ComputerTileEntity()
	{
		super(MSTileEntityTypes.COMPUTER.get());
	}
	
	/**
	 * 0 = client, 1 = server
	 */
	public Hashtable<Integer, Boolean> installedPrograms = new Hashtable<Integer, Boolean>();
	public ComputerScreen gui;
	public PlayerIdentifier owner;
	//client side only
	public int ownerId;
	public Hashtable<Integer, String> latestmessage = new Hashtable<Integer, String>();
	public CompoundNBT programData = new CompoundNBT();
	public int programSelected = -1;
	
	@Override
	public void read(BlockState state, CompoundNBT nbt)
	{
		super.read(state, nbt);
		if (nbt.contains("programs"))
		{
			CompoundNBT programs = nbt.getCompound("programs");
			for (Object name : programs.keySet())
			{
				installedPrograms.put(programs.getInt((String)name), true);
			}
		}

		latestmessage.clear();
		for(Entry<Integer,Boolean> e : installedPrograms.entrySet())
			if(e.getValue())
				latestmessage.put(e.getKey(), nbt.getString("text" + e.getKey()));

		programData = nbt.getCompound("programData");
		
		if(nbt.contains("ownerId"))
			ownerId = nbt.getInt("ownerId");
		else this.owner = IdentifierHandler.load(nbt, "owner");
		if(gui != null)
			gui.updateGui();
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		CompoundNBT programs = new CompoundNBT();
		Iterator<Entry<Integer, Boolean>> it = this.installedPrograms.entrySet().iterator();
		//int place = 0;
		while (it.hasNext()) 
		{
			Map.Entry<Integer, Boolean> pairs = it.next();
			int program = pairs.getKey();
			programs.putInt("program" + program,program);
			//place++;
		}
		for(Entry<Integer, String> e : latestmessage.entrySet())
			compound.putString("text" + e.getKey(), e.getValue());
		compound.put("programs",programs);
		compound.put("programData", programData.copy());
		if (owner != null) 
			owner.saveToNBT(compound, "owner");
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT tagCompound = this.write(new CompoundNBT());
		tagCompound.remove("owner");
		tagCompound.remove("ownerMost");
		tagCompound.remove("ownerLeast");
		if(owner != null)
			tagCompound.putInt("ownerId", owner.getId());
		if(hasProgram(1))
		{
			SburbConnection c = SkaianetHandler.get(getWorld()).getServerConnection(this);
			if(c != null)
				tagCompound.getCompound("programData").getCompound("program_1").putInt("connectedClient", c.getClientIdentifier().getId());
		}
		return tagCompound;
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 2, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.read(getBlockState(), pkt.getNbtCompound());
	}

	public boolean hasProgram(int id) 
	{
		return installedPrograms.get(id) == null ? false:installedPrograms.get(id);
	}

	public CompoundNBT getData(int id)
	{
		if(!programData.contains("program_"+id))
			programData.put("program_" + id, new CompoundNBT());
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
		}
		else
		{
			getData(1).putBoolean("isOpen", false);
		}
		markDirty();
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
		markDirty();
		markBlockForUpdate();
	}
	
	@Override
	public void putServerBoolean(String name, boolean value)
	{
		getData(1).putBoolean(name, value);
		markDirty();
		markBlockForUpdate();
	}
	
	@Override
	public void clearConnectedClient()
	{
		getData(1).putString("connectedClient", "");
		markDirty();
		markBlockForUpdate();
	}
	
	@Override
	public void putClientMessage(String message)
	{
		latestmessage.put(0, message);
		markDirty();
		markBlockForUpdate();
	}
	
	@Override
	public void putServerMessage(String message)
	{
		latestmessage.put(1, message);
		markDirty();
		markBlockForUpdate();
	}
	
	public void markBlockForUpdate()
	{
		BlockState state = world.getBlockState(pos);
		this.world.notifyBlockUpdate(pos, state, state, 3);
	}
	
	public static void forNetworkIfPresent(ServerPlayerEntity player, BlockPos pos, Consumer<ComputerTileEntity> consumer)
	{
		if(player.world.isAreaLoaded(pos, 0))	//TODO also check distance to the computer pos (together with a continual check clientside)
		{
			TileEntity te = player.world.getTileEntity(pos);
			if(te instanceof ComputerTileEntity)
			{
				ComputerTileEntity computer = (ComputerTileEntity) te;
				MinecraftServer mcServer = Objects.requireNonNull(player.getServer());
				OpEntry opsEntry = mcServer.getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
				if((!MinestuckConfig.SERVER.privateComputers.get() || IdentifierHandler.encode(player) == computer.owner || opsEntry != null && opsEntry.getPermissionLevel() >= 2) && ServerEditHandler.getData(player) == null)
					consumer.accept(computer);
			}
		}
	}
}
