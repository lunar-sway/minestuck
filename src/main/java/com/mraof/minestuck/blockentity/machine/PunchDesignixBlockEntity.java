package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.CardCaptchas;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationMode;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipe;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinerContainer;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.machine.PunchDesignixBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

import static com.mraof.minestuck.block.machine.MachineBlock.FACING;

/**
 * Stores a captchalogue card and stores an 8 character long captcha to be punched into the card.
 * Handles the triggering and logic of || combination recipes using that card and typed code.
 * Right clicking a card to the keyboard will copy its captcha code unless the card contains an item in the UNREADABLE tag and has not been decoded.
 * Core Editmode deployable
 */
public class PunchDesignixBlockEntity extends BlockEntity
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private boolean broken = false;
	private ItemStack card = ItemStack.EMPTY;
	private String captcha;
	
	public static final String REJECT_CARD = "block.minestuck.punch_designix.code_rejected";
	
	public PunchDesignixBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.PUNCH_DESIGNIX.get(), pos, state);
		this.captcha = "";
	}
	
	public void setCard(ItemStack card)
	{
		if(card.getItem() == MSItems.CAPTCHA_CARD.get() || card.isEmpty())
		{
			this.card = card;
			updateState();
		}
	}
	
	public void setCaptcha(String captcha)
	{
		if(level == null)
			return;
		
		this.captcha = captcha; //update regardless of side
		
		if(!level.isClientSide)
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
	}
	
	public void breakMachine()
	{
		broken = true;
		setChanged();
	}
	
	private void updateState()
	{
		if(level != null && !level.isClientSide)
		{
			BlockState state = level.getBlockState(worldPosition);
			boolean hasCard = !card.isEmpty();
			if(state.hasProperty(PunchDesignixBlock.Slot.HAS_CARD) && hasCard != state.getValue(PunchDesignixBlock.Slot.HAS_CARD))
				level.setBlock(worldPosition, state.setValue(PunchDesignixBlock.Slot.HAS_CARD, hasCard), Block.UPDATE_CLIENTS);
		}
	}
	
	@Nonnull
	public ItemStack getCard()
	{
		return card;
	}
	
	public String getCaptcha()
	{
		return captcha;
	}
	
	public void onRightClick(Player player, BlockState clickedState)
	{
		validateMachine();
		
		Block part = clickedState.getBlock();
		if(part instanceof PunchDesignixBlock.Slot && !player.level().isClientSide)
		{
			handleSlotClick(player);
		} else if(isUsable(clickedState) && (part == MSBlocks.PUNCH_DESIGNIX.KEYBOARD.get() || part == MSBlocks.PUNCH_DESIGNIX.RIGHT_LEG.get()))
		{
			handleKeyboardClick(player);
		}
	}
	
	private void handleSlotClick(Player player)
	{
		if(!getCard().isEmpty())
		{
			if(player.getMainHandItem().isEmpty())
				player.setItemInHand(InteractionHand.MAIN_HAND, getCard());
			else if(!player.getInventory().add(getCard()))
				dropItem(false);
			else player.inventoryMenu.broadcastChanges();
			
			setCard(ItemStack.EMPTY);
		} else if(!broken)
		{
			ItemStack heldStack = player.getMainHandItem();
			if(!heldStack.isEmpty() && heldStack.getItem() == MSItems.CAPTCHA_CARD.get())
				setCard(heldStack.split(1));    //Insert card into the punch slot
		}
	}
	
	private void handleKeyboardClick(Player player)
	{
		ItemStack heldStack = player.getMainHandItem();
		
		if(player.level().isClientSide)
		{
			//Go to screen if no valid item in hand
			if(!heldStack.is(MSItems.CAPTCHA_CARD.get()))
			{
				MSScreenFactories.displayPunchDesignixScreen(this);
			}
		} else
		{
			if(AlchemyHelper.isReadableCard(heldStack))
			{
				setCaptcha(CardCaptchas.getCaptcha(AlchemyHelper.getDecodedItem(heldStack).getItem(), player.getServer()));
				effects(false);
			} else if(heldStack.is(MSItems.CAPTCHA_CARD.get()) && AlchemyHelper.getDecodedItem(heldStack).isEmpty())
			{
				setCaptcha(CardCaptchas.EMPTY_CARD_CAPTCHA);
				effects(false);
			} else if(heldStack.is(MSItems.CAPTCHA_CARD.get()) && !AlchemyHelper.isReadableCard(heldStack))
				player.displayClientMessage(Component.translatable(REJECT_CARD), true); //card unreadable
		}
	}
	
	public void punchCard(ServerPlayer player)
	{
		Item itemFromCaptcha = CardCaptchas.getItemFromCaptcha(captcha, player.server);
		
		if(itemFromCaptcha != null && !getCard().isEmpty())
		{
			ItemStack captchaItemStack = itemFromCaptcha.getDefaultInstance();
			ItemStack storedStackInCard = AlchemyHelper.getDecodedItem(getCard());
			ItemStack output;
			
			if(AlchemyHelper.isPunchedCard(getCard())) //|| combination. A temporary new captcha card containing captchaItemStack is made
			{
				output = CombinationRecipe.findResult(new CombinerContainer.Wrapper(AlchemyHelper.createCard(captchaItemStack, player.server), getCard(), CombinationMode.OR), player.level());
			} else
				output = captchaItemStack;
			
			if(!output.isEmpty())
			{
				MSCriteriaTriggers.PUNCH_DESIGNIX.get().trigger(player, captchaItemStack, storedStackInCard, output);
				setCard(AlchemyHelper.createPunchedCard(output));
				effects(true);
			}
		}
	}
	
	private void effects(boolean success)
	{
		if(this.level == null)
			return;
		
		this.level.levelEvent(success ? LevelEvent.SOUND_DISPENSER_DISPENSE : LevelEvent.SOUND_DISPENSER_FAIL, this.worldPosition, 0);
		if(success)
		{
			Direction direction = getBlockState().getValue(FACING);
			int i = direction.getStepX() + 1 + (direction.getStepZ() + 1) * 3;
			this.level.levelEvent(LevelEvent.PARTICLES_SHOOT_SMOKE, this.worldPosition, i);
		}
	}
	
	private boolean isUsable(BlockState state)
	{
		return !broken && state.getValue(FACING).equals(getBlockState().getValue(FACING));
	}
	
	private void validateMachine()
	{
		if(broken || level == null)
			return;
		
		if(MSBlocks.PUNCH_DESIGNIX.isInvalidFromSlot(level, getBlockPos()))
			broken = true;
	}
	
	public void dropItem(boolean inBlock)
	{
		if(level == null)
		{
			LOGGER.warn("Tried to drop punch designix card before the world had been set!");
			return;
		}
		
		Direction direction = inBlock ? null : level.getBlockState(this.worldPosition).getValue(FACING);
		BlockPos dropPos;
		if(inBlock)
			dropPos = this.worldPosition;
		else if(!Block.canSupportCenter(level, this.worldPosition.relative(direction), direction.getOpposite()))
			dropPos = this.worldPosition.relative(direction);
		else if(!Block.canSupportCenter(level, this.worldPosition.above(), Direction.DOWN))
			dropPos = this.worldPosition.above();
		else dropPos = this.worldPosition;
		
		Containers.dropItemStack(level, dropPos.getX(), dropPos.getY(), dropPos.getZ(), getCard());
		setCard(ItemStack.EMPTY);
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		broken = nbt.getBoolean("broken");
		setCard(ItemStack.of(nbt.getCompound("card")));
		
		if(nbt.contains("captcha"))
			this.captcha = nbt.getString("captcha");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		compound.putBoolean("broken", this.broken);
		compound.put("card", getCard().save(new CompoundTag()));
		compound.putString("captcha", this.captcha);
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
}
