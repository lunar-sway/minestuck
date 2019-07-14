package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.advancements.MinestuckCriteriaTriggers;
import com.mraof.minestuck.block.PunchDesignixBlock;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.CombinationRegistry;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

import static com.mraof.minestuck.block.MachineBlock.FACING;

public class PunchDesignixTileEntity extends TileEntity
{
	public boolean broken = false;
	protected ItemStack card = ItemStack.EMPTY;
	
	public PunchDesignixTileEntity()
	{
		super(ModTileEntityTypes.PUNCH_DESIGNIX);
	}
	
	public void setCard(ItemStack card)
	{
		if (card.getItem() == MinestuckItems.CAPTCHA_CARD || card.isEmpty())
		{
			this.card = card;
			if(world != null && !world.isRemote)
			{
				BlockState state = world.getBlockState(pos);
				boolean hasCard = !card.isEmpty();
				if(state.has(PunchDesignixBlock.Slot.HAS_CARD) && hasCard != state.get(PunchDesignixBlock.Slot.HAS_CARD))
					world.setBlockState(pos, state.with(PunchDesignixBlock.Slot.HAS_CARD, hasCard), 2);
			}
		}
	}
	
	@Nonnull
	public ItemStack getCard()
	{
		return card;
	}
	
	public void onRightClick(ServerPlayerEntity player, BlockState clickedState)
	{
		Block part = clickedState.getBlock();
		if (part == MinestuckBlocks.PUNCH_DESIGNIX.SLOT && !getCard().isEmpty())
		{    //Remove card from punch slot
			if (player.getHeldItemMainhand().isEmpty())
				player.setHeldItem(Hand.MAIN_HAND, getCard());
			else if (!player.inventory.addItemStackToInventory(getCard()))
				dropItem(false);
			else player.container.detectAndSendChanges();
			
			setCard(ItemStack.EMPTY);
			return;
		}
		
		if (isUseable(clickedState))
		{
			ItemStack heldStack = player.getHeldItemMainhand();
			if (part == MinestuckBlocks.PUNCH_DESIGNIX.SLOT && getCard().isEmpty())
			{
				if (!heldStack.isEmpty() && heldStack.getItem() == MinestuckItems.CAPTCHA_CARD)
					setCard(heldStack.split(1));    //Insert card into the punch slot
				
			} else if (part == MinestuckBlocks.PUNCH_DESIGNIX.KEYBOARD || part == MinestuckBlocks.PUNCH_DESIGNIX.RIGHT_LEG)
			{
				if (heldStack.isEmpty() || heldStack.getItem() != MinestuckItems.CAPTCHA_CARD)
					return;    //Not a valid item in hand
				
				if (!getCard().isEmpty() && getCard().getItem() == MinestuckItems.CAPTCHA_CARD &&
						heldStack.hasTag() && heldStack.getTag().contains("contentID"))
				{
					ItemStack output = AlchemyRecipes.getDecodedItem(heldStack);
					if (!output.isEmpty())
					{
						if(AlchemyRecipes.isPunchedCard(getCard()))
						{    //|| combination
							output = CombinationRegistry.getCombination(output, AlchemyRecipes.getDecodedItem(getCard()), CombinationRegistry.Mode.MODE_OR);
							if(!output.isEmpty())
							{
								MinestuckCriteriaTriggers.PUNCH_DESIGNIX.trigger(player, AlchemyRecipes.getDecodedItem(heldStack), AlchemyRecipes.getDecodedItem(getCard()), output);
								setCard(AlchemyRecipes.createCard(output, true));
								effects(true);
								return;
							}
						} else    //Just punch the card regularly
						{
							MinestuckCriteriaTriggers.PUNCH_DESIGNIX.trigger(player, output, ItemStack.EMPTY, output);
							setCard(AlchemyRecipes.createCard(output, true));
							effects(true);
							
							return;
						}
					}
				}
				effects(false);
			}
		}
	}
	
	private void effects(boolean success)
	{
		world.playEvent(success ? 1000 : 1001, pos, 0);
		if (success)
		{
			Direction direction = getBlockState().get(FACING);
			int i = direction.getXOffset() + 1 + (direction.getZOffset() + 1) * 3;
			world.playEvent(2000, pos, i);
		}
	}
	
	private boolean isUseable(BlockState state)
	{
		BlockState currentState = getWorld().getBlockState(getPos());
		if(!broken)
		{
			checkStates();
			if(broken)
				Debug.warnf("Failed to notice a block being broken or misplaced at the punch designix at %s", getPos());
		}
		if(!state.get(FACING).equals(currentState.get(FACING)))
			return false;
		return !broken;
	}
	
	public void checkStates()
	{
		if (broken || world == null)
			return;
		
		BlockState currentState = world.getBlockState(getPos());
		Direction facing = currentState.get(FACING);
		Direction hOffset = facing.rotateYCCW();
		if (!world.getBlockState(getPos().offset(hOffset)).equals(MinestuckBlocks.PUNCH_DESIGNIX.KEYBOARD.getDefaultState().with(FACING, facing)) ||
				!world.getBlockState(getPos().down()).equals(MinestuckBlocks.PUNCH_DESIGNIX.LEFT_LEG.getDefaultState().with(FACING, facing)) ||
				!world.getBlockState(getPos().down().offset(hOffset)).equals(MinestuckBlocks.PUNCH_DESIGNIX.RIGHT_LEG.getDefaultState().with(FACING, facing)))
		{
			broken = true;
		}
	}
	
	public void dropItem(boolean inBlock)
	{
		if(world == null) {
			Debug.warn("Tried to drop punch designix card before the world had been set!");
			return;
		}
		
		Direction direction = inBlock ? null : world.getBlockState(this.pos).get(FACING);
		BlockPos dropPos;
		if (inBlock)
			dropPos = this.pos;
		else if (!Block.hasSolidSide(world.getBlockState(this.pos.offset(direction)), world, this.pos.offset(direction), direction.getOpposite()))
			dropPos = this.pos.offset(direction);
		else if (!Block.hasSolidSide(world.getBlockState(this.pos.up()), world, this.pos.up(), Direction.DOWN))
			dropPos = this.pos.up();
		else dropPos = this.pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), getCard());
		setCard(ItemStack.EMPTY);
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		broken = compound.getBoolean("broken");
		setCard(ItemStack.read(compound.getCompound("card")));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		compound.putBoolean("broken", this.broken);
		compound.put("card", getCard().write(new CompoundNBT()));
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT nbt;
		nbt = super.getUpdateTag();
		nbt.put("card", getCard().write(new CompoundNBT()));
		return nbt;
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		SUpdateTileEntityPacket packet;
		packet = new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
		return packet;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}
}