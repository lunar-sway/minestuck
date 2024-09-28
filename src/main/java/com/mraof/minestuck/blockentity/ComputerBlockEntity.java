package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.machine.ComputerBlock;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.computer.ProgramData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.computer.theme.MSComputerThemes;
import com.mraof.minestuck.item.IncompleteSburbCodeItem;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnections;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.Map.Entry;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ComputerBlockEntity extends BlockEntity implements ISburbComputer
{
	//TODO The implementation of this class need a serious rewrite
	public ComputerBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.COMPUTER.get(), pos, state);
		
		// always should exist on computers
		this.installedPrograms.add(2);
		this.installedPrograms.add(3);
	}
	
	/**
	 * 0 = client, 1 = server, 2 = disk burner, 3 = settings
	 */
	public HashSet<Integer> installedPrograms = new HashSet<>();
	public ComputerScreen gui;
	@Nullable
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
	private ResourceLocation computerTheme = MSComputerThemes.DEFAULT;
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		
		if(nbt.contains("programs"))
		{
			if (nbt.contains("programs", Tag.TAG_INT_ARRAY))
			{
				for(int id : nbt.getIntArray("programs"))
					installedPrograms.add(id);
			}
			else
			{
				CompoundTag programs = nbt.getCompound("programs");
				for(String name : programs.getAllKeys())
					installedPrograms.add(programs.getInt(name));
			}
		}
		
		latestmessage.clear();
		for(int id : installedPrograms)
			latestmessage.put(id, nbt.getString("text" + id));
		
		programData = nbt.getCompound("programData");
		if(nbt.contains("theme", Tag.TAG_STRING))
			computerTheme = Objects.requireNonNullElse(ResourceLocation.tryParse(nbt.getString("theme")), computerTheme);
		// Backwards-compatibility with Minestuck-1.20.1-1.11.2.0 and earlier
		else if(nbt.contains("theme", Tag.TAG_INT))
			computerTheme = MSComputerThemes.getThemeFromOldOrdinal(nbt.getInt("theme"));
		
		hieroglyphsStored = IncompleteSburbCodeItem.readBlockSet(nbt, "hieroglyphsStored");
		if(nbt.contains("hasParadoxInfoStored"))
			hasParadoxInfoStored = nbt.getBoolean("hasParadoxInfoStored");
		if(nbt.contains("blankDisksStored"))
			blankDisksStored = nbt.getInt("blankDisksStored");
		
		if(nbt.contains("ownerId"))
			ownerId = nbt.getInt("ownerId");
		else this.owner = IdentifierHandler.loadOrThrow(nbt, "owner");
		
		//keep this after everything else has been loaded
		if(gui != null)
			gui.updateGui();
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		for(Entry<Integer, String> e : latestmessage.entrySet())
			compound.putString("text" + e.getKey(), e.getValue());
		
		
		compound.put("programs", new IntArrayTag(installedPrograms.stream().toList()));
		compound.put("programData", programData.copy());
		
		if(owner != null)
			owner.saveToNBT(compound, "owner");
		
		IncompleteSburbCodeItem.writeBlockSet(compound, "hieroglyphsStored", hieroglyphsStored);
		compound.putBoolean("hasParadoxInfoStored", hasParadoxInfoStored);
		
		compound.putInt("blankDisksStored", blankDisksStored);
		
		compound.putString("theme", computerTheme.toString());
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
			SburbConnections.get(getLevel().getServer()).getServerConnection(this).ifPresent(c ->
					tagCompound.getCompound("programData").getCompound("program_1")
							.putInt("connectedClient", c.client().getId())
			);
		}
		return tagCompound;
	}
	
	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public void onLoad()
	{
		super.onLoad();
		for(int id : installedPrograms)
			ProgramData.getHandler(id).ifPresent(handler -> handler.onLoad(this));
	}
	
	public boolean isBroken()
	{
		return getBlockState().getValue(ComputerBlock.STATE) == ComputerBlock.State.BROKEN;
	}
	
	public boolean hasProgram(int id)
	{
		return installedPrograms.contains(id);
	}
	
	public CompoundTag getData(int id)
	{
		if(!programData.contains("program_" + id))
			programData.put("program_" + id, new CompoundTag());
		return programData.getCompound("program_" + id);
	}
	
	public void closeAll()
	{
		for(int id : installedPrograms)
			ProgramData.getHandler(id).ifPresent(handler -> handler.onClosed(this));
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
	
	public ResourceLocation getTheme()
	{
		return computerTheme;
	}
	
	public void setTheme(ResourceLocation themeId)
	{
		this.computerTheme = themeId;
		setChanged();
		markBlockForUpdate();
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
		if(level==null) return;
		BlockState state = level.getBlockState(worldPosition);
		this.level.sendBlockUpdated(worldPosition, state, state, 3);
	}
	
	public static Optional<ComputerBlockEntity> getAccessibleComputer(ServerPlayer player, BlockPos pos)
	{
		return MSPacket.getAccessibleBlockEntity(player, pos, ComputerBlockEntity.class)
				.filter(computer -> computer.canAccessComputer(player));
	}
	
	public boolean canAccessComputer(ServerPlayer player)
	{
		if(this.isBroken())
			return false;
		if(ServerEditHandler.isInEditmode(player))
			return false;
		
		if(MinestuckConfig.SERVER.privateComputers.get())
		{
			return (this.owner != null && this.owner.appliesTo(player))
					|| player.hasPermissions(Commands.LEVEL_GAMEMASTERS);
		} else
			return true;
	}
}
