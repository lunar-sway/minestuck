package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.alchemy.CardCaptchas;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.machine.PunchDesignixBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.recipe.CombinationMode;
import com.mraof.minestuck.alchemy.recipe.CombinationRecipe;
import com.mraof.minestuck.alchemy.recipe.CombinerWrapper;
import com.mraof.minestuck.util.WorldEventUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

import java.util.Objects;

import static com.mraof.minestuck.block.machine.MachineBlock.FACING;

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
		this.captcha = "CCCCCCCC";
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
		//when there is a check to prevent client side changes here, it has issues
		if(level != null/**/ && !level.isClientSide/**/)
		{
			this.captcha = captcha;
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
			//this.setChanged();
			//updateState();
			//BlockState state = level.getBlockState(worldPosition);
			//level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
		}
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
		if(part instanceof PunchDesignixBlock.Slot && !player.level.isClientSide)
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
		
		if(player.level.isClientSide)
		{
			//Go to screen if no valid item in hand or crouching
			//TODO if the player is holding a captcha card and is crouching, it wont open the screen
			if(player.isShiftKeyDown() || heldStack.getItem() != MSItems.CAPTCHA_CARD.get())
			{
				MSScreenFactories.displayPunchDesignixScreen(this);
			}
		} else
		{
			if(AlchemyHelper.isReadableCard(heldStack))
			{
				setCaptcha(CardCaptchas.getCaptchaFromItem(AlchemyHelper.getDecodedItem(heldStack).getItem()));
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
		Item itemFromCaptcha = CardCaptchas.getItemFromCaptcha(captcha);
		
		if(itemFromCaptcha != null && !getCard().isEmpty())
		{
			ItemStack captchaItemStack = itemFromCaptcha.getDefaultInstance();
			ItemStack storedStackInCard = AlchemyHelper.getDecodedItem(getCard());
			ItemStack output;
			
			if(AlchemyHelper.isPunchedCard(getCard())) //|| combination. A temporary new captcha card containing captchaItemStack is made
			{
				output = CombinationRecipe.findResult(new CombinerWrapper(AlchemyHelper.createCard(captchaItemStack, false), getCard(), CombinationMode.OR), player.level);
			} else
				output = captchaItemStack;
			
			if(!output.isEmpty())
			{
				MSCriteriaTriggers.PUNCH_DESIGNIX.trigger(player, captchaItemStack, storedStackInCard, output);
				setCard(AlchemyHelper.createCard(output, true));
				effects(true);
			}
		}
	}
	
	private void effects(boolean success)
	{
		WorldEventUtil.dispenserEffect(getLevel(), getBlockPos(), getBlockState().getValue(FACING), success);
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
		
		//TODO it doesnt seem to save correctly
		if(nbt.contains("captcha"))
			setCaptcha(nbt.getString("captcha"));
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		compound.putBoolean("broken", this.broken);
		compound.put("card", getCard().save(new CompoundTag()));
		compound.putString("captcha", Objects.requireNonNullElse(this.captcha, "BBBBBBBB"));
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
	
	/*@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag nbt = super.getUpdateTag();
		nbt.putString("captcha", Objects.requireNonNullElse(this.captcha, "AAAAAAAA"));
		return nbt;
	}
	
	@Override
	public void handleUpdateTag(CompoundTag tag)
	{
		if(tag.contains("captcha"))
			setCaptcha(tag.getString("captcha"));
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
	{
		handleUpdateTag(pkt.getTag());
	}*/
}