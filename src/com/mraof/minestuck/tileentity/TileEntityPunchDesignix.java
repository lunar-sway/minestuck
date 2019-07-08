package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.advancements.MinestuckCriteriaTriggers;
import com.mraof.minestuck.block.BlockPunchDesignix;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.CombinationRegistry;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

import static com.mraof.minestuck.block.BlockMachine.FACING;

public class TileEntityPunchDesignix extends TileEntity
{
	public boolean broken = false;
	protected ItemStack card = ItemStack.EMPTY;
	
	public TileEntityPunchDesignix()
	{
		super(MinestuckTiles.PUNCH_DESIGNIX);
	}
	
	public void setCard(ItemStack card)
	{
		if (card.getItem() == MinestuckItems.CAPTCHA_CARD || card.isEmpty())
		{
			this.card = card;
			if(world != null && !world.isRemote)
			{
				IBlockState state = world.getBlockState(pos);
				boolean hasCard = !card.isEmpty();
				if(state.has(BlockPunchDesignix.Slot.HAS_CARD) && hasCard != state.get(BlockPunchDesignix.Slot.HAS_CARD))
					world.setBlockState(pos, state.with(BlockPunchDesignix.Slot.HAS_CARD, hasCard), 2);
			}
		}
	}
	
	@Nonnull
	public ItemStack getCard()
	{
		return card;
	}
	
	public void onRightClick(EntityPlayerMP player, IBlockState clickedState)
	{
		Block part = clickedState.getBlock();
		if (part == MinestuckBlocks.PUNCH_DESIGNIX.SLOT && !getCard().isEmpty())
		{    //Remove card from punch slot
			if (player.getHeldItemMainhand().isEmpty())
				player.setHeldItem(EnumHand.MAIN_HAND, getCard());
			else if (!player.inventory.addItemStackToInventory(getCard()))
				dropItem(false);
			else player.inventoryContainer.detectAndSendChanges();
			
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
			EnumFacing direction = getBlockState().get(FACING);
			int i = direction.getXOffset() + 1 + (direction.getZOffset() + 1) * 3;
			world.playEvent(2000, pos, i);
		}
	}
	
	private boolean isUseable(IBlockState state)
	{
		IBlockState currentState = getWorld().getBlockState(getPos());
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
		if (broken)
			return;
		
		IBlockState currentState = getWorld().getBlockState(getPos());
		EnumFacing facing = currentState.get(FACING);
		EnumFacing hOffset = facing.rotateYCCW();
		if (!world.getBlockState(getPos().offset(hOffset)).equals(MinestuckBlocks.PUNCH_DESIGNIX.KEYBOARD.getDefaultState().with(FACING, facing)) ||
				!world.getBlockState(getPos().down()).equals(MinestuckBlocks.PUNCH_DESIGNIX.LEFT_LEG.getDefaultState().with(FACING, facing)) ||
				!world.getBlockState(getPos().down().offset(hOffset)).equals(MinestuckBlocks.PUNCH_DESIGNIX.RIGHT_LEG.getDefaultState().with(FACING, facing)))
		{
			broken = true;
		}
	}
	
	public void dropItem(boolean inBlock)
	{
		EnumFacing direction = inBlock ? null : world.getBlockState(this.pos).get(FACING);
		BlockPos dropPos;
		if (inBlock)
			dropPos = this.pos;
		else if (!world.getBlockState(this.pos.offset(direction)).isBlockNormalCube())
			dropPos = this.pos.offset(direction);
		else if (!world.getBlockState(this.pos.up()).isBlockNormalCube())
			dropPos = this.pos.up();
		else dropPos = this.pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), getCard());
		setCard(ItemStack.EMPTY);
	}
	
	@Override
	public void read(NBTTagCompound compound)
	{
		super.read(compound);
		broken = compound.getBoolean("broken");
		setCard(ItemStack.read(compound.getCompound("card")));
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		super.write(compound);
		compound.putBoolean("broken", this.broken);
		compound.put("card", getCard().write(new NBTTagCompound()));
		return compound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt;
		nbt = super.getUpdateTag();
		nbt.put("card", getCard().write(new NBTTagCompound()));
		return nbt;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		SPacketUpdateTileEntity packet;
		packet = new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
		return packet;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}
}