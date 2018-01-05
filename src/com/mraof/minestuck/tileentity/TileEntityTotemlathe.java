package com.mraof.minestuck.tileentity;

import static com.mraof.minestuck.block.BlockPunchDesignix.DIRECTION;
import static com.mraof.minestuck.block.BlockTotemlathe.PART;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.block.BlockTotemlathe.EnumParts;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.CombinationRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class TileEntityTotemlathe extends TileEntityMachine
{
	private boolean broken=false;
	//two cards so that we can preform the && alchemy operation
	protected ItemStack card1 = ItemStack.EMPTY;
	protected ItemStack card2 = ItemStack.EMPTY;
	protected ItemStack dowel = ItemStack.EMPTY;
	//constructor
	public TileEntityTotemlathe() {}
	//data checking
	public ItemStack getCard1() {
		return card1;
	}
	public void setCard1(ItemStack stack) {
		if (stack.getItem()==MinestuckItems.captchaCard||stack==ItemStack.EMPTY) 
			card1=stack;
	}
	public ItemStack getCard2() {
		return card2;
	}
	public void setCard2(ItemStack stack) {
		if (stack.getItem()==MinestuckItems.captchaCard||stack==ItemStack.EMPTY) 
			card2=stack;
	}
	public ItemStack getDowel() {
		return dowel;
	}
	public void setDowel(ItemStack stack) {
		if (stack.getItem()==MinestuckItems.cruxiteDowel||stack==ItemStack.EMPTY) 
			dowel=stack;
	}
	
	public void onRightClick(EntityPlayer player, IBlockState clickedState)
	{
		EnumParts part = clickedState.getValue(PART);
		ItemStack heldStack =player.getHeldItemMainhand();
		//if they have clicked on the part that holds the chapta cards.
		if (part.equals(EnumParts.BOTTOM_LEFT))	{
			if(!card1.isEmpty()){
				if(!card2.isEmpty()){
					player.inventory.addItemStackToInventory(card2);
				}else if(heldStack.getItem()==MinestuckItems.captchaCard){
					setCard2(heldStack.splitStack(1));
				}
				else {
					player.inventory.addItemStackToInventory(card1);
				}
			}
			else if(heldStack.getItem()==MinestuckItems.captchaCard) {
					setCard1(heldStack.splitStack(1));
				}
		}
		
		
		//if they have clicked the dowel block
		if (part== EnumParts.MID_MIDLEFT) {
			if (dowel.isEmpty()) {
				if(heldStack.getItem()==MinestuckItems.cruxiteDowel) {
					setDowel(heldStack.splitStack(1));
				}
			}else {
				player.inventory.addItemStackToInventory(dowel);
			}
		}
		
		
		
		//if they have clicked on the lever
		if (part == EnumParts.TOP_MIDRIGHT) {
			//carve the dowel.
			processContents();
		}
		
	}
	public void dropCard1(boolean inBlock) {
		EnumFacing direction = inBlock ? null : world.getBlockState(this.pos).getValue(DIRECTION);
		BlockPos dropPos;
		if(inBlock)
			dropPos = this.pos;
		else if(!world.getBlockState(this.pos.offset(direction)).isBlockNormalCube())
			dropPos = this.pos.offset(direction);
		else if(!world.getBlockState(this.pos.up()).isBlockNormalCube())
			dropPos = this.pos.up();
		else dropPos = this.pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), card1);
		setCard1( ItemStack.EMPTY);
	}
	public void dropCard2(boolean inBlock) {
		EnumFacing direction = inBlock ? null : world.getBlockState(this.pos).getValue(DIRECTION);
		BlockPos dropPos;
		if(inBlock)
			dropPos = this.pos;
		else if(!world.getBlockState(this.pos.offset(direction)).isBlockNormalCube())
			dropPos = this.pos.offset(direction);
		else if(!world.getBlockState(this.pos.up()).isBlockNormalCube())
			dropPos = this.pos.up();
		else dropPos = this.pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), card2);
		setCard2( ItemStack.EMPTY);
	}
	public void dropDowel(boolean inBlock) {
		EnumFacing direction = inBlock ? null : world.getBlockState(this.pos).getValue(DIRECTION);
		BlockPos dropPos;
		if(inBlock)
			dropPos = this.pos;
		else if(!world.getBlockState(this.pos.offset(direction)).isBlockNormalCube())
			dropPos = this.pos.offset(direction);
		else if(!world.getBlockState(this.pos.up()).isBlockNormalCube())
			dropPos = this.pos.up();
		else dropPos = this.pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), dowel);
		setDowel( ItemStack.EMPTY);
	
	}
	
	public boolean isBroken() {
		return broken;
	}
	
	public void Brake() {
		broken=true;
	}
	
	
	@Override
	public boolean isAutomatic()
	{
		return false;
	}
	
	
	@Override
	public int getSizeInventory()
	{

		return 4;
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		broken = tagCompound.getBoolean("broken");
		card1 = new ItemStack(tagCompound.getCompoundTag("card1"));
		card2 = new ItemStack(tagCompound.getCompoundTag("card2"));
		dowel = new ItemStack(tagCompound.getCompoundTag("dowel"));
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setBoolean("broken", broken);
		tagCompound.setTag("card1", card1.writeToNBT(new NBTTagCompound()));
		tagCompound.setTag("card2", card2.writeToNBT(new NBTTagCompound()));
		tagCompound.setTag("dowel", dowel.writeToNBT(new NBTTagCompound()));
		return tagCompound;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
	
			return i == 0 || i == 1 ? itemstack.getItem() == MinestuckItems.captchaCard : i == 2 ? itemstack.getItem() == MinestuckItems.cruxiteDowel : false;
	}
	
	@Override
	public boolean contentsValid()
	{
			if((!card1.isEmpty() || !card2.isEmpty()) && !dowel.isEmpty() && !(dowel.hasTagCompound() && dowel.getTagCompound().hasKey("contentID"))
					&& (inv.get(3).isEmpty() || inv.get(3).getCount() < inv.get(3).getMaxStackSize() && inv.get(3).getItemDamage() == dowel.getItemDamage()))
			{
				if(!card1.isEmpty() && !card2.isEmpty())
				{
					if(!card1.hasTagCompound() || !card1.getTagCompound().getBoolean("punched") || !card2.hasTagCompound() || !card2.getTagCompound().getBoolean("punched"))
						return inv.get(3).isEmpty() || !(inv.get(3).hasTagCompound() && inv.get(3).getTagCompound().hasKey("contentID"));
					else
					{
						ItemStack output = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(card1), AlchemyRecipeHandler.getDecodedItem(card2), CombinationRegistry.MODE_AND);
						return !output.isEmpty() && (inv.get(3).isEmpty() || AlchemyRecipeHandler.getDecodedItem(inv.get(3)).isItemEqual(output));
					}
				}
				else
				{
					ItemStack input = card1.isEmpty() ? card2 : card1;
					return (inv.get(3).isEmpty() || (AlchemyRecipeHandler.getDecodedItem(inv.get(3)).isItemEqual(AlchemyRecipeHandler.getDecodedItem(input))
							|| !(input.hasTagCompound() && input.getTagCompound().getBoolean("punched")) && !(inv.get(3).hasTagCompound() && inv.get(3).getTagCompound().hasKey("contentID"))));
				}
			}
			else return false;
		
	}
	
	public int comparatorValue()
	{
		
		return 0;
	}
	
	@Override
	public void processContents()
	{
	
		ItemStack output;
		if(!card1.isEmpty() && !card2.isEmpty())
			if(!card1.hasTagCompound() || !card1.getTagCompound().getBoolean("punched") || !card2.hasTagCompound() || !card2.getTagCompound().getBoolean("punched"))
				output = new ItemStack(MinestuckBlocks.genericObject);
			else output = CombinationRegistry.getCombination(AlchemyRecipeHandler.getDecodedItem(card1), AlchemyRecipeHandler.getDecodedItem(card2), CombinationRegistry.MODE_AND);
		else
		{
			ItemStack input = card1.isEmpty() ? card2 : card1;
			if(!input.hasTagCompound() || !input.getTagCompound().getBoolean("punched"))
				output = new ItemStack(MinestuckBlocks.genericObject);
			else output = AlchemyRecipeHandler.getDecodedItem(input);
		}
		
		ItemStack outputDowel = output.getItem().equals(Item.getItemFromBlock(MinestuckBlocks.genericObject)) ? new ItemStack(MinestuckItems.cruxiteDowel) : AlchemyRecipeHandler.createEncodedItem(output, false);
		outputDowel.setItemDamage(dowel.getItemDamage());
				
		setDowel(outputDowel);

	}
	
	@Override
	public void markDirty()
	{
		this.progress = 0;
		this.ready = false;
		super.markDirty();
	}

	@Override
	public String getName()
	{
		return "tile.sburbMachine.Totemlathe.name";
	}
	@Override
	public boolean allowOverrideStop() {
		return false;
	}
	
}
