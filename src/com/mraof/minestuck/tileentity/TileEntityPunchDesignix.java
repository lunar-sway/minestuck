package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.BlockPunchDesignix.EnumParts;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.CombinationRegistry;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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

import static com.mraof.minestuck.block.BlockPunchDesignix.DIRECTION;
import static com.mraof.minestuck.block.BlockPunchDesignix.PART;

public class TileEntityPunchDesignix extends TileEntity
{
	public boolean broken = false;
	protected ItemStack card = ItemStack.EMPTY;
	
	//constructor
	public TileEntityPunchDesignix()
	{
	}
	
	public void setCard(ItemStack card)
	{
		if (card.getItem() == MinestuckItems.captchaCard || card.isEmpty())
		{
			this.card = card;
			if(world != null)
			{
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 2);
			}
		}
	}
	
	@Nonnull
	public ItemStack getCard()
	{
		return card;
	}
	
	public void onRightClick(EntityPlayer player, IBlockState clickedState)
	{
		EnumParts part = clickedState.getValue(PART);
		if (part.equals(EnumParts.TOP_LEFT) && !card.isEmpty())
		{    //Remove card from punch slot
			if (player.getHeldItemMainhand().isEmpty())
				player.setHeldItem(EnumHand.MAIN_HAND, card);
			else if (!player.inventory.addItemStackToInventory(card))
				dropItem(false);
			else player.inventoryContainer.detectAndSendChanges();
			
			setCard(ItemStack.EMPTY);
			return;
		}
		
		if (checkStates(clickedState))
		{
			ItemStack heldStack = player.getHeldItemMainhand();
			if (part.equals(EnumParts.TOP_LEFT) && card.isEmpty())
			{
				if (!heldStack.isEmpty() && heldStack.getItem() == MinestuckItems.captchaCard)
					setCard(heldStack.splitStack(1));    //Insert card into the punch slot
				
			} else if (part.equals(EnumParts.TOP_RIGHT) || part.equals(EnumParts.BOTTOM_RIGHT))
			{
				if (heldStack.isEmpty() || heldStack.getItem() != MinestuckItems.captchaCard)
					return;    //Not a valid item in hand
				
				if (!card.isEmpty() && card.getItem() == MinestuckItems.captchaCard &&
						heldStack.hasTagCompound() && heldStack.getTagCompound().hasKey("contentID"))
				{
					ItemStack output = AlchemyRecipeHandler.getDecodedItem(heldStack);
					if (!output.isEmpty())
					{
						if (card.hasTagCompound() && card.getTagCompound().getBoolean("punched"))
						{    //|| combination
							output = CombinationRegistry.getCombination(output, AlchemyRecipeHandler.getDecodedItem(card), CombinationRegistry.MODE_OR);
							if (!output.isEmpty())
							{
								setCard(AlchemyRecipeHandler.createCard(output, true));
								effects(true);
								return;
							}
						} else    //Just punch the card regularly
						{
							setCard(AlchemyRecipeHandler.createCard(output, true));
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
			EnumFacing direction = world.getBlockState(pos).getValue(DIRECTION);
			int i = direction.getFrontOffsetX() + 1 + (direction.getFrontOffsetZ() + 1) * 3;
			world.playEvent(2000, pos, i);
		}
	}
	
	private boolean checkStates(IBlockState state)
	{
		if (broken)
			return false;
		IBlockState currentState = this.getWorld().getBlockState(this.getPos());
		EnumFacing hOffset = currentState.getValue(DIRECTION).rotateYCCW();
		if (!world.getBlockState(getPos().offset(hOffset)).equals(currentState.withProperty(PART, EnumParts.TOP_RIGHT)) ||
				!world.getBlockState(getPos().down()).equals(currentState.withProperty(PART, EnumParts.BOTTOM_LEFT)) ||
				!world.getBlockState(getPos().down().offset(hOffset)).equals(currentState.withProperty(PART, EnumParts.BOTTOM_RIGHT)))
		{
			broken = true;
			Debug.warnf("Failed to notice a block being broken or misplaced at the punch designix at %s", getPos());
			return false;
		}
		
		if (!state.getValue(DIRECTION).equals(currentState.getValue(DIRECTION)))
			return false;
		
		return true;
	}
	
	public void dropItem(boolean inBlock)
	{
		EnumFacing direction = inBlock ? null : world.getBlockState(this.pos).getValue(DIRECTION);
		BlockPos dropPos;
		if (inBlock)
			dropPos = this.pos;
		else if (!world.getBlockState(this.pos.offset(direction)).isBlockNormalCube())
			dropPos = this.pos.offset(direction);
		else if (!world.getBlockState(this.pos.up()).isBlockNormalCube())
			dropPos = this.pos.up();
		else dropPos = this.pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), card);
		setCard(ItemStack.EMPTY);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		broken = tagCompound.getBoolean("broken");
		setCard(new ItemStack(tagCompound.getCompoundTag("card")));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setBoolean("broken", this.broken);
		tagCompound.setTag("card", card.writeToNBT(new NBTTagCompound()));
		return tagCompound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt;
		nbt = super.getUpdateTag();
		nbt.setTag("card", card.writeToNBT(new NBTTagCompound()));
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