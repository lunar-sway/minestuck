package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.machine.ComputerBlock;
import com.mraof.minestuck.computer.*;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.computer.theme.MSComputerThemes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ComputerBlockEntity extends BlockEntity implements ISburbComputer
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public ComputerBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.COMPUTER.get(), pos, state);
		
		// always should exist on computers
		this.installedPrograms.add(ProgramType.DISK_BURNER);
		this.installedPrograms.add(ProgramType.SETTINGS);
	}
	
	private final Set<ProgramType<?>> installedPrograms = new HashSet<>();
	@Nullable
	private Runnable guiCallback = null;
	@Nullable
	private PlayerIdentifier owner;
	//client side only
	private int ownerId;
	private SburbClientData sburbClientProgramData = new SburbClientData(this::markDirtyAndResend);
	private SburbServerData sburbServerProgramData = new SburbServerData(this::markDirtyAndResend);
	@Nullable
	public ProgramType<?> programSelected = null;
	private final DiskBurnerData diskBurnerData = new DiskBurnerData(this::markDirtyAndResend);
	private int blankDisksStored = 0;
	private ResourceLocation computerTheme = MSComputerThemes.DEFAULT;
	
	@Override
	protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
	{
		super.loadAdditional(nbt, pRegistries);
		
		installedPrograms.addAll(
				ProgramType.LIST_CODEC.parse(NbtOps.INSTANCE, nbt.get("programs"))
						.resultOrPartial(LOGGER::error).orElse(Collections.emptyList()));
		
		sburbClientProgramData.read(nbt.getCompound("sburb_client_data"));
		sburbServerProgramData.read(nbt.getCompound("sburb_server_data"));
		
		if(nbt.contains("theme", Tag.TAG_STRING))
			computerTheme = Objects.requireNonNullElse(ResourceLocation.tryParse(nbt.getString("theme")), computerTheme);
		// Backwards-compatibility with Minestuck-1.20.1-1.11.2.0 and earlier
		else if(nbt.contains("theme", Tag.TAG_INT))
			computerTheme = MSComputerThemes.getThemeFromOldOrdinal(nbt.getInt("theme"));
		
		this.diskBurnerData.read(nbt.getCompound("disk_burner_data"));
		
		if(nbt.contains("blankDisksStored"))
			blankDisksStored = nbt.getInt("blankDisksStored");
		
		if(nbt.contains("ownerId"))
			ownerId = nbt.getInt("ownerId");
		else this.owner = IdentifierHandler.load(nbt, "owner").result().orElse(null);
		
		//keep this after everything else has been loaded
		if(guiCallback != null)
			guiCallback.run();
	}
	
	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider provider)
	{
		super.saveAdditional(compound, provider);
		
		compound.put("sburb_client_data", sburbClientProgramData.write());
		compound.put("sburb_server_data", sburbServerProgramData.write());
		
		if(owner != null)
			owner.saveToNBT(compound, "owner");
		
		writeSharedData(compound);
	}
	
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider)
	{
		CompoundTag compoundtag = new CompoundTag();
		super.saveAdditional(compoundtag, provider);
		
		compoundtag.put("sburb_client_data", sburbClientProgramData.write());
		compoundtag.put("sburb_server_data", sburbServerProgramData.writeForUpdatePacket(this, getLevel().getServer()));
		
		if(owner != null)
			compoundtag.putInt("ownerId", owner.getId());
		
		writeSharedData(compoundtag);
		
		return compoundtag;
	}
	
	private void writeSharedData(CompoundTag compoundtag)
	{
		compoundtag.put("programs", ProgramType.LIST_CODEC.encodeStart(NbtOps.INSTANCE, new ArrayList<>(installedPrograms)).getOrThrow());
		
		compoundtag.put("disk_burner_data", this.diskBurnerData.write());
		
		compoundtag.putInt("blankDisksStored", blankDisksStored);
		
		compoundtag.putString("theme", computerTheme.toString());
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
		for(ProgramType<?> programType : installedPrograms)
			programType.eventHandler().onLoad(this);
	}
	
	public boolean isBroken()
	{
		return getBlockState().getValue(ComputerBlock.STATE) == ComputerBlock.State.BROKEN;
	}
	
	public boolean hasProgram(ProgramType<?> programType)
	{
		return installedPrograms.contains(programType);
	}
	
	@SuppressWarnings("unchecked")
	public <D> Optional<D> getProgramData(ProgramType<D> type)
	{
		if(!hasProgram(type))
			return Optional.empty();
		
		if(type == ProgramType.SBURB_CLIENT)
			return Optional.of((D) this.sburbClientProgramData);
		else if(type == ProgramType.SBURB_SERVER)
			return Optional.of((D) this.sburbServerProgramData);
		else if(type == ProgramType.DISK_BURNER)
			return Optional.of((D) this.diskBurnerData);
		else if(type == ProgramType.SETTINGS)
			return Optional.of((D) Unit.INSTANCE);
		else
			throw new IllegalArgumentException();
	}
	
	@Override
	public SburbClientData getSburbClientData()
	{
		return this.sburbClientProgramData;
	}
	
	@Override
	public SburbServerData getSburbServerData()
	{
		return this.sburbServerProgramData;
	}
	
	public DiskBurnerData getDiskBurnerData()
	{
		return diskBurnerData;
	}
	
	public int getBlankDisksStored()
	{
		return this.blankDisksStored;
	}
	
	public boolean tryTakeBlankDisk()
	{
		if(this.blankDisksStored > 0)
		{
			this.blankDisksStored = this.blankDisksStored - 1;
			this.markDirtyAndResend();
			return true;
		}
		return false;
	}
	
	@Deprecated
	public void clearComputerData()
	{
		this.sburbClientProgramData = new SburbClientData(this::markDirtyAndResend);
		this.sburbServerProgramData = new SburbServerData(this::markDirtyAndResend);
	}
	
	public void closeAll()
	{
		for(ProgramType<?> programType : installedPrograms)
			programType.eventHandler().onClosed(this);
	}
	
	public Stream<ProgramType<?>> installedPrograms()
	{
		return this.installedPrograms.stream();
	}
	
	@Nullable
	@Override
	public PlayerIdentifier getOwner()
	{
		return owner;
	}
	
	public void initializeOwner(PlayerIdentifier owner)
	{
		if(this.owner != null)
			throw new IllegalStateException("Not allowed to set computer owner in this state");
		this.owner = owner;
	}
	
	public int clientSideOwnerId()
	{
		return this.ownerId;
	}
	
	@Override
	public ComputerReference createReference()
	{
		return ComputerReference.of(this);
	}
	
	public ResourceLocation getTheme()
	{
		return computerTheme;
	}
	
	public void setTheme(ResourceLocation themeId)
	{
		this.computerTheme = themeId;
		markDirtyAndResend();
	}
	
	public boolean tryInsertDisk(ItemStack stackInHand)
	{
		if(isBroken() || level == null)
			return false;
		
		Optional<ProgramType<?>> optionalType = ProgramType.getForDisk(stackInHand);
		
		if(stackInHand.is(MSItems.BLANK_DISK))
		{
			if(blankDisksStored < 2) //only allow two blank disks to be burned at a time
			{
				stackInHand.shrink(1);
				blankDisksStored++;
				markDirtyAndResend();
				return true;
			}
		} else if(stackInHand.is(Items.MUSIC_DISC_11))
		{
			if(!level.isClientSide && installedPrograms.size() < 3)
			{
				stackInHand.shrink(1);
				closeAll();
				level.setBlock(getBlockPos(), getBlockState().setValue(ComputerBlock.STATE, ComputerBlock.State.BROKEN), Block.UPDATE_CLIENTS);
				markDirtyAndResend();
			}
			return true;
		} else if(optionalType.isPresent())
		{
			ProgramType<?> programType = optionalType.get();
			if(!level.isClientSide && !hasProgram(programType))
			{
				stackInHand.shrink(1);
				installedPrograms.add(programType);
				level.setBlock(getBlockPos(), getBlockState().setValue(ComputerBlock.STATE, ComputerBlock.State.GAME_LOADED), Block.UPDATE_CLIENTS);
				markDirtyAndResend();
				programType.eventHandler().onDiskInserted(this);
			}
			return true;
		}
		
		return false;
	}
	
	public void setGuiCallback(Runnable guiCallback)
	{
		if(this.level == null || !this.level.isClientSide())
			throw new IllegalStateException("Tried to set gui callback in a non-client context");
		this.guiCallback = guiCallback;
	}
	
	private void markDirtyAndResend()
	{
		setChanged();
		if(level instanceof ServerLevel serverLevel)
			serverLevel.getChunkSource().blockChanged(worldPosition);
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
