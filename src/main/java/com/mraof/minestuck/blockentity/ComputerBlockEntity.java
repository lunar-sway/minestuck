package com.mraof.minestuck.blockentity;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.machine.ComputerBlock;
import com.mraof.minestuck.computer.*;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.computer.theme.MSComputerThemes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ComputerBlockEntity extends BlockEntity implements ISburbComputer
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final int PROGRAM_DISK_CAPACITY = 2, BLANK_DISK_CAPACITY = 2;
	private static final Codec<NonNullList<ItemStack>> DISK_LIST_CODEC = NonNullList.codecOf(ItemStack.SINGLE_ITEM_CODEC);
	
	@Nullable
	private Runnable guiCallback = null;
	
	@Nullable
	private PlayerIdentifier owner;
	//client side only
	private int ownerId;
	
	private final NonNullList<ItemStack> programDisks = NonNullList.createWithCapacity(PROGRAM_DISK_CAPACITY);
	private final NonNullList<ItemStack> blankDisks = NonNullList.createWithCapacity(BLANK_DISK_CAPACITY);
	
	private final Map<ProgramType<?>, ProgramType.Data> existingPrograms = new HashMap<>();
	private ResourceLocation computerTheme = MSComputerThemes.DEFAULT;
	
	public ComputerBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.COMPUTER.get(), pos, state);
		
		// always should exist on computers
		insertNewProgramInstance(ProgramTypes.DISK_BURNER.get());
		insertNewProgramInstance(ProgramTypes.SETTINGS.get());
	}
	
	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider pRegistries)
	{
		super.loadAdditional(tag, pRegistries);
		
		this.programDisks.clear();
		if(tag.contains("program_disks"))
			DISK_LIST_CODEC.parse(NbtOps.INSTANCE, tag.get("program_disks"))
					.resultOrPartial(LOGGER::error).ifPresent(this.programDisks::addAll);
		this.blankDisks.clear();
		DISK_LIST_CODEC.parse(NbtOps.INSTANCE, tag.get("blank_disks"))
				.resultOrPartial(LOGGER::error).ifPresent(this.blankDisks::addAll);
		
		CompoundTag programs = tag.getCompound("programs");
		for(String programKey : programs.getAllKeys())
		{
			ProgramType<?> programType = ResourceLocation.read(programKey).result()
					.flatMap(ProgramTypes.REGISTRY::getOptional).orElse(null);
			if(programType == null)
			{
				LOGGER.error("Unknown program type by name \"{}\" in computer data.", programKey);
				continue;
			}
			insertNewProgramInstance(programType).read(programs.getCompound(programKey));
		}
		
		if(tag.contains("theme", Tag.TAG_STRING))
			computerTheme = Objects.requireNonNullElse(ResourceLocation.tryParse(tag.getString("theme")), computerTheme);
		// Backwards-compatibility with Minestuck-1.20.1-1.11.2.0 and earlier
		else if(tag.contains("theme", Tag.TAG_INT))
			computerTheme = MSComputerThemes.getThemeFromOldOrdinal(tag.getInt("theme"));
		
		if(tag.contains("ownerId"))
			ownerId = tag.getInt("ownerId");
		else this.owner = IdentifierHandler.load(tag, "owner").result().orElse(null);
		
		//keep this after everything else has been loaded
		if(guiCallback != null)
			guiCallback.run();
	}
	
	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider)
	{
		super.saveAdditional(tag, provider);
		
		tag.put("program_disks", DISK_LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.blankDisks).result().orElseThrow());
		
		if(owner != null)
			owner.saveToNBT(tag, "owner");
		
		writeSharedData(tag, ProgramType.Data::write);
	}
	
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider)
	{
		CompoundTag compoundtag = new CompoundTag();
		super.saveAdditional(compoundtag, provider);
		
		if(owner != null)
			compoundtag.putInt("ownerId", owner.getId());
		
		writeSharedData(compoundtag, programData -> programData.writeForSync(this, getLevel().getServer()));
		
		return compoundtag;
	}
	
	private void writeSharedData(CompoundTag tag, Function<ProgramType.Data, CompoundTag> serializer)
	{
		tag.put("blank_disks", DISK_LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.blankDisks).result().orElseThrow());
		
		CompoundTag programs = new CompoundTag();
		for(Map.Entry<ProgramType<?>, ProgramType.Data> entry : this.existingPrograms.entrySet())
		{
			String programKey = Objects.requireNonNull(ProgramTypes.REGISTRY.getKey(entry.getKey())).toString();
			programs.put(programKey, serializer.apply(entry.getValue()));
		}
		tag.put("programs", programs);
		
		tag.putString("theme", computerTheme.toString());
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public void onLoad()
	{
		super.onLoad();
		for(ProgramType<?> programType : this.existingPrograms.keySet())
			programType.eventHandler().onLoad(this);
	}
	
	public boolean isBroken()
	{
		return getBlockState().getValue(ComputerBlock.STATE) == ComputerBlock.State.BROKEN;
	}
	
	public boolean hasProgram(ProgramType<?> programType)
	{
		return this.existingPrograms.containsKey(programType);
	}
	
	private <D extends ProgramType.Data> D insertNewProgramInstance(ProgramType<D> programType)
	{
		D data = programType.newDataInstance(this::markDirtyAndResend);
		this.existingPrograms.put(programType, data);
		return data;
	}
	
	public <D extends ProgramType.Data> Optional<D> getProgramData(Supplier<ProgramType<D>> type)
	{
		return this.getProgramData(type.get());
	}
	
	@SuppressWarnings("unchecked")
	public <D extends ProgramType.Data> Optional<D> getProgramData(ProgramType<D> type)
	{
		return Optional.ofNullable((D) this.existingPrograms.get(type));
	}
	
	@Override
	public Optional<SburbClientData> getSburbClientData()
	{
		return this.getProgramData(ProgramTypes.SBURB_CLIENT);
	}
	
	@Override
	public Optional<SburbServerData> getSburbServerData()
	{
		return this.getProgramData(ProgramTypes.SBURB_SERVER);
	}
	
	public boolean hasBlankDisks()
	{
		return !this.blankDisks.isEmpty();
	}
	
	public boolean tryTakeBlankDisk()
	{
		if(!this.blankDisks.isEmpty())
		{
			this.blankDisks.removeLast();
			this.markDirtyAndResend();
			return true;
		}
		return false;
	}
	
	public void dropItems()
	{
		if(this.level == null)
			return;
		
		Containers.dropContents(this.level, this.getBlockPos(), this.programDisks);
		Containers.dropContents(this.level, this.getBlockPos(), this.blankDisks);
	}
	
	public void closeAll()
	{
		for(ProgramType<?> programType : this.existingPrograms.keySet())
			programType.eventHandler().onClosed(this);
	}
	
	public Stream<ProgramType<?>> installedPrograms()
	{
		return this.existingPrograms.keySet().stream();
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
		
		@Nullable
		Holder<ProgramType<?>> optionalType = stackInHand.get(MSItemComponents.PROGRAM_TYPE);
		
		if(stackInHand.is(MSItems.BLANK_DISK))
		{
			if(this.blankDisks.size() < BLANK_DISK_CAPACITY)
			{
				this.blankDisks.add(stackInHand.split(1));
				markDirtyAndResend();
				return true;
			}
		} else if(stackInHand.is(Items.MUSIC_DISC_11))
		{
			if(!level.isClientSide && this.programDisks.size() < PROGRAM_DISK_CAPACITY)
			{
				this.programDisks.add(stackInHand.split(1));
				closeAll();
				level.setBlock(getBlockPos(), getBlockState().setValue(ComputerBlock.STATE, ComputerBlock.State.BROKEN), Block.UPDATE_CLIENTS);
				markDirtyAndResend();
			}
			return true;
		} else if(optionalType != null)
		{
			ProgramType<?> programType = optionalType.value();
			if(!level.isClientSide && !hasProgram(programType) && this.programDisks.size() < PROGRAM_DISK_CAPACITY)
			{
				this.programDisks.add(stackInHand.split(1));
				insertNewProgramInstance(programType);
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
