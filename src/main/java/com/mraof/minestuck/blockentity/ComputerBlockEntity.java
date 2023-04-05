package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.machine.ComputerBlock;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.computer.ProgramData;
import com.mraof.minestuck.computer.Theme;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.MSNBTUtil;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.ServerOpListEntry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class ComputerBlockEntity extends BlockEntity implements ISburbComputer
{
	//TODO The implementation of this class need a serious rewrite
	public ComputerBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.COMPUTER.get(), pos, state);
		
		// always should exist on computers
		this.installedPrograms.put(2, true);
		this.installedPrograms.put(3, true);
	}
	
	/**
	 * 0 = client, 1 = server, 2 = disk burner, 3 = settings
	 */
	public Hashtable<Integer, Boolean> installedPrograms = new Hashtable<>();
	public ComputerScreen gui;
	public PlayerIdentifier owner;
	//client side only
	public int ownerId;
	public Hashtable<Integer, String> latestmessage = new Hashtable<>();
	public CompoundTag programData = new CompoundTag();
	public int programSelected = -1;
	@Nonnull
	public Set<Block> hieroglyphsStored = new HashSet<>();
	public boolean hasParadoxInfoStored = false; //sburb code component received from the lotus flower
	public int blankDisksStored;
	private Theme theme = Theme.DEFAULT;
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		if(nbt.contains("programs"))
		{
			for(int id : nbt.getIntArray("programs"))
				installedPrograms.put(id, true);
		}
		
		latestmessage.clear();
		for(Entry<Integer, Boolean> e : installedPrograms.entrySet())
			if(e.getValue())
				latestmessage.put(e.getKey(), nbt.getString("text" + e.getKey()));
		
		programData = nbt.getCompound("programData");
		
		hieroglyphsStored = MSNBTUtil.readBlockSet(nbt, "hieroglyphsStored");
		if(nbt.contains("hasParadoxInfoStored"))
			hasParadoxInfoStored = nbt.getBoolean("hasParadoxInfoStored");
		if(nbt.contains("blankDisksStored"))
			blankDisksStored = nbt.getInt("blankDisksStored");
		
		if(nbt.contains("ownerId"))
			ownerId = nbt.getInt("ownerId");
		else this.owner = IdentifierHandler.load(nbt, "owner");
		
		//keep this after everything else has been loaded
		if(gui != null)
			gui.updateGui();
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		IntArrayTag programs = new IntArrayTag(new int[]{});
		
		for(Entry<Integer,Boolean> program : installedPrograms.entrySet())
		{
			int id = program.getKey();
			programs.add(IntTag.valueOf(id));
		}
		
		for(Entry<Integer, String> e : latestmessage.entrySet())
			compound.putString("text" + e.getKey(), e.getValue());
		
		compound.put("programs", programs);
		compound.put("programData", programData.copy());
		
		if(owner != null)
			owner.saveToNBT(compound, "owner");
		
		MSNBTUtil.writeBlockSet(compound, "hieroglyphsStored", hieroglyphsStored);
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
	
	public boolean isBroken()
	{
		return getBlockState().getValue(ComputerBlock.STATE) == ComputerBlock.State.BROKEN;
	}
	
	public boolean hasProgram(int id)
	{
		return installedPrograms.get(id) != null && installedPrograms.get(id);
	}
	
	public CompoundTag getData(int id)
	{
		if(!programData.contains("program_" + id))
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
	
	@Override
	public Theme getTheme()
	{
		return theme;
	}
	
	@Override
	public void setTheme(Theme theme)
	{
		this.theme = theme;
	}
	
	public void burnDisk(int programId)
	{
		if(level == null)
			return;
		
		BlockPos bePos = getBlockPos();
		ItemStack diskStack = ProgramData.getItem(programId);
		if(!diskStack.isEmpty() && blankDisksStored > 0 && hasAllCode())
		{
			RandomSource random = level.getRandom();
			
			float rx = random.nextFloat() * 0.8F + 0.1F;
			float ry = random.nextFloat() * 0.8F + 0.1F;
			float rz = random.nextFloat() * 0.8F + 0.1F;
			
			ItemEntity entityItem = new ItemEntity(level, bePos.getX() + rx, bePos.getY() + ry, bePos.getZ() + rz, diskStack);
			entityItem.setDeltaMovement(random.nextGaussian() * 0.05F, random.nextGaussian() * 0.05F + 0.2F, random.nextGaussian() * 0.05F);
			level.addFreshEntity(entityItem);
			
			blankDisksStored--;
			
			//Imitates the structure block to ensure that changes are sent client-side
			setChanged();
			markBlockForUpdate();
		}
	}
	
	/**
	 * Returns true if the block entity has the paradox info and all the hieroglyphs
	 */
	public boolean hasAllCode()
	{
		return hasParadoxInfoStored && hieroglyphsStored.containsAll(MSTags.getBlocksFromTag(MSTags.Blocks.GREEN_HIEROGLYPHS));
	}
	
	public void markBlockForUpdate()
	{
		BlockState state = level.getBlockState(worldPosition);
		this.level.sendBlockUpdated(worldPosition, state, state, 3);
	}
	
	public static void forNetworkIfPresent(ServerPlayer player, BlockPos pos, Consumer<ComputerBlockEntity> consumer)
	{
		if(player.level.isAreaLoaded(pos, 0))    //TODO also check distance to the computer pos (together with a continual check clientside)
		{
			if(player.level.getBlockEntity(pos) instanceof ComputerBlockEntity computer && !computer.isBroken())
			{
				MinecraftServer mcServer = Objects.requireNonNull(player.getServer());
				ServerOpListEntry opsEntry = mcServer.getPlayerList().getOps().get(player.getGameProfile());
				if((!MinestuckConfig.SERVER.privateComputers.get() || IdentifierHandler.encode(player) == computer.owner || opsEntry != null && opsEntry.getLevel() >= 2) && ServerEditHandler.getData(player) == null)
					consumer.accept(computer);
			}
		}
	}
}
