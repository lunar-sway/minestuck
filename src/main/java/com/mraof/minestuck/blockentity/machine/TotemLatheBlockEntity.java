package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationMode;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipe;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinerContainer;
import com.mraof.minestuck.block.EnumDowelType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.machine.TotemLatheBlock;
import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Stores 1-2 punched captcha cards. Handles the triggering and logic of && combination recipes using those punched cards, the result of which is applied to a cruxite dowel.
 * {@link TotemLatheDowelBlockEntity} handles the storage of the dowel that will be lathed. The Totem Lathe is a core Editmode deployable
 */
@ParametersAreNonnullByDefault
public class TotemLatheBlockEntity extends BlockEntity
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private boolean isProcessing;
	private int animationticks;
	private boolean broken = false;
	//two cards so that we can preform the && alchemy operation
	private ItemStack card1 = ItemStack.EMPTY;
	private ItemStack card2 = ItemStack.EMPTY;
	
	public TotemLatheBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.TOTEM_LATHE.get(), pos, state);
	}
	
	private boolean tryAddCard(ItemStack stack)
	{
		if(!isBroken() && stack.is(MSItems.CAPTCHA_CARD.get()))
		{
			if(card1.isEmpty())
				card1 = stack;
			else if(card2.isEmpty())
				card2 = stack;
			else return false;
			
			updateState();
			return true;
		}
		return false;
	}
	
	private ItemStack tryTakeCard()
	{
		ItemStack card = ItemStack.EMPTY;
		if(!card2.isEmpty())
		{
			card = card2;
			card2 = ItemStack.EMPTY;
		} else if(!card1.isEmpty())
		{
			card = card1;
			card1 = ItemStack.EMPTY;
		}
		if(!card.isEmpty())
			updateState();
		return card;
	}
	
	private void updateState()
	{
		int worldCount = getBlockState().getValue(TotemLatheBlock.Slot.COUNT);
		int actualCount = getActualCardCount();
		if(worldCount != actualCount)
		{
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(TotemLatheBlock.Slot.COUNT, actualCount));
		}
	}
	
	private int getActualCardCount()
	{
		if(!card2.isEmpty())
			return 2;
		else if(!card1.isEmpty())
			return 1;
		else return 0;
	}
	
	@Nonnull
	public ItemStack getCard1()
	{
		return card1;
	}
	
	public ItemStack getCard2()
	{
		return card2;
	}
	
	public boolean isBroken()
	{
		return broken;
	}
	
	public void setBroken()
	{
		broken = true;
	}
	
	public void dropItems()
	{
		Objects.requireNonNull(this.level);
		BlockPos pos = this.getBlockPos();
		
		if(!this.card1.isEmpty())
		{
			Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), this.card1);
			this.card1 = ItemStack.EMPTY;
		}
		if(!this.card2.isEmpty())
		{
			Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), this.card2);
			this.card2 = ItemStack.EMPTY;
		}
	}
	
	public boolean setDowel(ItemStack dowelStack)
	{
		Objects.requireNonNull(this.level);
		if(!(dowelStack.is(MSItems.CRUXITE_DOWEL.get()) || dowelStack.isEmpty()))
			return false;
		
		Direction facing = getFacing();
		BlockPos dowelPos = MSBlocks.TOTEM_LATHE.getDowelPos(getBlockPos(), getBlockState());
		BlockState oldState = level.getBlockState(dowelPos);
		
		BlockState newState = MSBlocks.TOTEM_LATHE.DOWEL_ROD.get()
				.defaultBlockState().setValue(TotemLatheBlock.FACING, facing)
				.setValue(TotemLatheBlock.DowelRod.DOWEL, EnumDowelType.getForDowel(dowelStack));
		
		if(isValidDowelRod(oldState, facing))
		{
			BlockEntity be = level.getBlockEntity(dowelPos);
			if(!(be instanceof TotemLatheDowelBlockEntity))
			{
				be = new TotemLatheDowelBlockEntity(dowelPos, newState);
				level.setBlockEntity(be);
			}
			
			TotemLatheDowelBlockEntity beItem = (TotemLatheDowelBlockEntity) be;
			beItem.setStack(dowelStack);
			//updating the dowel block entity
			if(!oldState.equals(newState))
				level.setBlockAndUpdate(dowelPos, newState);
			else level.sendBlockUpdated(dowelPos, oldState, oldState, Block.UPDATE_ALL);
			
			//updating the machine's block entity
			isProcessing = false;
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
			return true;
		}
		return false;
	}
	
	private boolean setCarvedItem(ItemStack output)
	{
		Objects.requireNonNull(this.level);
		
		Direction facing = getFacing();
		BlockPos pos = MSBlocks.TOTEM_LATHE.getDowelPos(getBlockPos(), getBlockState());
		BlockState oldState = level.getBlockState(pos);
		
		if(!isValidDowelRod(oldState, facing))
			return false;	// This is not our dowel rod block!
		
		BlockEntity be = level.getBlockEntity(pos);
		if(be instanceof ItemStackBlockEntity beItem)
		{
			ItemStack oldDowel = beItem.getStack();
			ItemStack newDowel = output.is(MSItems.GENERIC_OBJECT.get()) ? new ItemStack(MSItems.CRUXITE_DOWEL.get()) : AlchemyHelper.createEncodedItem(output, false);
			ColorHandler.setColor(newDowel, ColorHandler.getColorFromStack(oldDowel));
			beItem.setStack(newDowel);
			
			BlockState newState = MSBlocks.TOTEM_LATHE.DOWEL_ROD.get()
					.defaultBlockState().setValue(TotemLatheBlock.FACING, facing)
					.setValue(TotemLatheBlock.DowelRod.DOWEL, EnumDowelType.getForDowel(newDowel));
			
			if(!oldState.equals(newState))
				level.setBlockAndUpdate(pos, newState);
			else
				level.sendBlockUpdated(pos, oldState, newState, 2);	// Make sure the new dowel item is synced to client
			
			return true;
		} else
			return false;
	}
	
	public ItemStack getDowel()
	{
		BlockPos pos = MSBlocks.TOTEM_LATHE.getDowelPos(getBlockPos(), getBlockState());
		if(isValidDowelRod(level.getBlockState(pos), getFacing()))
		{
			if(level.getBlockEntity(pos) instanceof TotemLatheDowelBlockEntity blockEntity)
				return blockEntity.getStack();
		}
		return ItemStack.EMPTY;
		
	}
	
	private boolean isValidDowelRod(BlockState state, Direction facing)
	{
		return state.is(MSBlocks.TOTEM_LATHE.DOWEL_ROD.get()) && state.getValue(TotemLatheBlock.FACING) == facing;
	}
	
	public Direction getFacing()
	{
		return getBlockState().getValue(TotemLatheBlock.FACING);
	}
	
	public void onRightClick(Player player, BlockState clickedState)
	{
		boolean working = isUseable(clickedState);
		
		//if they have clicked on the part that holds the captcha cards
		if(clickedState.getBlock() instanceof TotemLatheBlock.Slot)
			handleSlotClick(player, working);
		
		//if they have clicked the dowel block
		if(clickedState.is(MSBlocks.TOTEM_LATHE.DOWEL_ROD.get()))
			handleDowelClick(player, working);
		
		//if they have clicked on the lever
		if(clickedState.is(MSBlocks.TOTEM_LATHE.TOP.get()) && level != null)
		{
			boolean startingCarving = false;
			
			//carve the dowel.
			if(working && !getDowel().isEmpty() && !AlchemyHelper.hasDecodedItem(getDowel()) && (!card1.isEmpty() || !card2.isEmpty()))
			{
				this.level.playSound(null, this.getBlockPos(), MSSoundEvents.TOTEM_LATHE_LATHE.get(), SoundSource.BLOCKS, 1F, 1F);
				startingCarving = true;
				isProcessing = true;
				animationticks = 25;
				level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
			}
			
			level.levelEvent(startingCarving ? LevelEvent.SOUND_DISPENSER_DISPENSE : LevelEvent.SOUND_DISPENSER_FAIL, getBlockPos(), 0);
		}
	}
	
	private void handleSlotClick(Player player, boolean isWorking)
	{
		ItemStack heldStack = player.getMainHandItem();
		ItemStack card = heldStack.copy().split(1);
		if(tryAddCard(card))
		{
			heldStack.shrink(1);
		} else
		{
			card = tryTakeCard();
			if(!card.isEmpty())
			{
				if(player.getMainHandItem().isEmpty())
					player.setItemInHand(InteractionHand.MAIN_HAND, card);
				else if(!player.getInventory().add(card))
					dropItem(false, getBlockPos(), card);
				else player.inventoryMenu.broadcastChanges();
			}
		}
	}
	
	private void handleDowelClick(Player player, boolean isWorking)
	{
		ItemStack heldStack = player.getMainHandItem();
		ItemStack dowel = getDowel();
		if (dowel.isEmpty())
		{
			if(isWorking && heldStack.is(MSItems.CRUXITE_DOWEL.get()))
			{
				ItemStack copy = heldStack.copy();
				copy.setCount(1);
				if(setDowel(copy))
				{
					heldStack.shrink(1);
				}
			}
		} else
		{
			if(player.getMainHandItem().isEmpty())
				player.setItemInHand(InteractionHand.MAIN_HAND, dowel);
			else if(!player.getInventory().add(dowel))
				dropItem(true, getBlockPos().above().relative(getFacing().getCounterClockWise(), 2), dowel);
			else player.inventoryMenu.broadcastChanges();
			setDowel(ItemStack.EMPTY);
		}
	}
	
	private boolean isUseable(BlockState state)
	{
		BlockState currentState = getLevel().getBlockState(getBlockPos());
		if(!isBroken())
		{
			checkStates();
			if(isBroken())
				LOGGER.warn("Failed to notice a block being broken or misplaced at the totem lathe at {}", getBlockPos());
		}
		
		if(!state.getValue(TotemLatheBlock.FACING).equals(currentState.getValue(TotemLatheBlock.FACING)))
			return false;
		return !isBroken();
	}
	
	public void checkStates()
	{
		if(isBroken())
			return;
		
		if(MSBlocks.TOTEM_LATHE.isInvalidFromSlot(level, getBlockPos()))
			setBroken();
	}
	
	private void dropItem(boolean inBlock, BlockPos pos, ItemStack stack)
	{
		Direction direction = getFacing();
		BlockPos dropPos;
		if(inBlock)
			dropPos = pos;
		else if(!Block.canSupportCenter(level, pos.relative(direction), direction.getOpposite()))
			dropPos = pos.relative(direction);
		else dropPos = pos;
		
		Containers.dropItemStack(level, dropPos.getX(), dropPos.getY(), dropPos.getZ(), stack);
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		broken = nbt.getBoolean("broken");
		card1 = ItemStack.of(nbt.getCompound("card1"));
		card2 = ItemStack.of(nbt.getCompound("card2"));
		isProcessing = nbt.getBoolean("isProcessing");
		if(card1.isEmpty() && !card2.isEmpty())
		{
			card1 = card2;
			card2 = ItemStack.EMPTY;
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		compound.putBoolean("broken",broken);
		compound.put("card1", card1.save(new CompoundTag()));
		compound.put("card2", card2.save(new CompoundTag()));
		compound.putBoolean("isProcessing", isProcessing);
	}
	
	private void processContents(Level level)
	{
		ItemStack dowel = getDowel();
		ItemStack output;
		boolean success = false;
		if(!dowel.isEmpty() && !AlchemyHelper.hasDecodedItem(dowel) && (!card1.isEmpty() || !card2.isEmpty()))
		{
			if(!card1.isEmpty() && !card2.isEmpty())
				if(!AlchemyHelper.isPunchedCard(card1) || !AlchemyHelper.isPunchedCard(card2))
					output = new ItemStack(MSItems.GENERIC_OBJECT.get());
				else output = CombinationRecipe.findResult(new CombinerContainer.Wrapper(card1, card2, CombinationMode.AND), level);
			else
			{
				ItemStack input = card1.isEmpty() ? card2 : card1;
				if(!AlchemyHelper.isPunchedCard(input))
					output = new ItemStack(MSItems.GENERIC_OBJECT.get());
				else output = AlchemyHelper.getDecodedItem(input);
			}
			
			if(!output.isEmpty())
				success = setCarvedItem(output);
		}
		
		//effects(success);
	}
	
	public static void tick(Level level, BlockPos pos, BlockState state, TotemLatheBlockEntity blockEntity)
	{
		if(blockEntity.animationticks > 0)
		{
			blockEntity.animationticks--;
			if(blockEntity.animationticks <= 0)
			{
				blockEntity.processContents(level);
			}
		}
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		return this.saveWithoutMetadata();
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	public boolean isProcessing()
	{
		return isProcessing;
	}
}
