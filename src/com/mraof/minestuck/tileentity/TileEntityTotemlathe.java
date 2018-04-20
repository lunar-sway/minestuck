package com.mraof.minestuck.tileentity;


import com.mraof.minestuck.block.BlockTotemlathe;
import com.mraof.minestuck.block.BlockTotemlathe2;
import com.mraof.minestuck.block.BlockTotemlathe3;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.CombinationRegistry;
import com.mraof.minestuck.util.Debug;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntityTotemlathe extends TileEntity
{
	private boolean broken=false;
	//two cards so that we can preform the && alchemy operation
	protected ItemStack card1 = ItemStack.EMPTY;
	protected ItemStack card2 = ItemStack.EMPTY;
	protected ItemStack dowel = ItemStack.EMPTY;
	//constructor
	public TileEntityTotemlathe() {}
	//data checking

	public void setCard1(ItemStack stack)
	{
		if(stack.getItem() == MinestuckItems.captchaCard || stack == ItemStack.EMPTY)
		{
			card1 = stack;
			if(world != null)
				resendState();
		}
	}
	public ItemStack getCard1() {
		return card1;
	}
	public void setCard2(ItemStack stack)
	{
		if(stack.getItem() == MinestuckItems.captchaCard || stack == ItemStack.EMPTY)
			card2 = stack;
	}
	public ItemStack getCard2() {
		return card2;
	}
	
	public boolean isBroken() {
		return broken;
	}
	
	public void Brake() {
		broken=true;
	}
	
	public void setDowel(ItemStack stack) {
		if (stack.getItem()==MinestuckItems.cruxiteDowel||stack==ItemStack.EMPTY) {
			dowel=stack;
			resendState();
			if(world != null)
			{
			    IBlockState state = world.getBlockState(pos);
			    this.world.notifyBlockUpdate(pos, state, state, 2);
			}
		}
	}
	public ItemStack getDowel() {
		return dowel;
		
	}
	
	public void onRightClick(EntityPlayer player, IBlockState clickedState)
	{
		if(checkStates(clickedState)) {
			ItemStack heldStack =player.getHeldItemMainhand();
			//if they have clicked on the part that holds the chapta cards.
			if (clickedState.getBlock()==MinestuckBlocks.totemlathe && clickedState.getValue(BlockTotemlathe.PART).equals(BlockTotemlathe.EnumParts.BOTTOM_LEFT))	{
				if(!card1.isEmpty()){
					if(!card2.isEmpty()){
						player.inventory.addItemStackToInventory(card2);
					}else if(heldStack.getItem()==MinestuckItems.captchaCard){
						setCard2(heldStack.splitStack(1));
					}
					else {
						player.inventory.addItemStackToInventory(card1);
						resendState();
					}
				}
				else if(heldStack.getItem()==MinestuckItems.captchaCard) {
						setCard1(heldStack.splitStack(1));
					}
			}			
			
			//if they have clicked the dowel block
			if (clickedState.getBlock()==MinestuckBlocks.totemlathe2 
					&&(clickedState.getValue(BlockTotemlathe2.PART)== BlockTotemlathe2.EnumParts.MID_MIDLEFT
					||clickedState.getValue(BlockTotemlathe2.PART)== BlockTotemlathe2.EnumParts.MID_MIDRIGHT)){
				if (dowel.isEmpty()) {
					if(heldStack.getItem()==MinestuckItems.cruxiteDowel) {
						setDowel(heldStack.splitStack(1));
					}
				}else {
					player.inventory.addItemStackToInventory(dowel);
					setDowel(ItemStack.EMPTY);
				}
			}
			
			//if they have clicked on the lever
			if (clickedState.getBlock()==MinestuckBlocks.totemlathe3 && clickedState.getValue(BlockTotemlathe3.PART) == BlockTotemlathe3.EnumParts.TOP_MIDRIGHT) {
				//carve the dowel.
				processContents();
			}
		}
		
	}
	
	private boolean checkStates(IBlockState state){
		if(this.broken)
			return false;
		EnumFacing hOffset = this.getWorld().getBlockState(this.getPos()).getValue(BlockTotemlathe.DIRECTION).rotateY();
		Block Block1=MinestuckBlocks.totemlathe;
		Block Block2=MinestuckBlocks.totemlathe2;
		Block Block3=MinestuckBlocks.totemlathe3;
		
		if(	!world.getBlockState(getPos()).getBlock().equals(Block1) ||
			!world.getBlockState(getPos().offset(hOffset,1)).getBlock().equals(Block1) ||
			!world.getBlockState(getPos().offset(hOffset,2)).getBlock().equals(Block1) ||
			!world.getBlockState(getPos().offset(hOffset,3)).getBlock().equals(Block1)||
			
			!world.getBlockState(getPos().up()).getBlock().equals(Block2)||
			!world.getBlockState(getPos().up().offset(hOffset,1)).getBlock().equals(Block2)||
			!world.getBlockState(getPos().up().offset(hOffset,2)).getBlock().equals(Block2)||
			!world.getBlockState(getPos().up().offset(hOffset,3)).getBlock().equals(Block2)||
			
			!world.getBlockState(getPos().up(2)).getBlock().equals(Block3)||
			!world.getBlockState(getPos().up(2).offset(hOffset,1)).getBlock().equals(Block3)||
			!world.getBlockState(getPos().up(2).offset(hOffset,2)).getBlock().equals( Block3))
		{
			Debug.info(world.getBlockState(getPos().offset(hOffset))+","+world.getBlockState(getPos().down())+","+world.getBlockState(getPos().down().offset(hOffset)));
			return false;
		}

		
		return true;
	}
	
	
	public void dropCard1(boolean inBlock,BlockPos pos) {
		EnumFacing direction = inBlock ? null : world.getBlockState(this.pos).getValue(BlockTotemlathe.DIRECTION);
		BlockPos dropPos;
		if(inBlock)
			dropPos = pos;
		else if(!world.getBlockState(pos.offset(direction)).isBlockNormalCube())
			dropPos = pos.offset(direction);
		else if(!world.getBlockState(pos.up()).isBlockNormalCube())
			dropPos = pos.up();
		else dropPos = pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), card1);
		setCard1(ItemStack.EMPTY);
	}
	public void dropCard2(boolean inBlock,BlockPos pos) {
		EnumFacing direction = inBlock ? null : world.getBlockState(this.pos).getValue(BlockTotemlathe.DIRECTION);
		BlockPos dropPos;
		if(inBlock)
			dropPos = pos;
		else if(!world.getBlockState(pos.offset(direction)).isBlockNormalCube())
			dropPos = pos.offset(direction);
		else if(!world.getBlockState(pos.up()).isBlockNormalCube())
			dropPos = pos.up();
		else dropPos = pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), card2);
		setCard2(ItemStack.EMPTY);
	}
	public void dropDowel(boolean inBlock,BlockPos pos) {
		EnumFacing direction = inBlock ? null : world.getBlockState(this.pos).getValue(BlockTotemlathe.DIRECTION);
		BlockPos dropPos;
		if(inBlock)
			dropPos = pos;
		else if(!world.getBlockState(pos.offset(direction)).isBlockNormalCube())
			dropPos = pos.offset(direction);
		else if(!world.getBlockState(pos.up()).isBlockNormalCube())
			dropPos = pos.up();
		else dropPos = pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), dowel);
		setDowel(ItemStack.EMPTY);
	}

	

	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		broken = tagCompound.getBoolean("broken");
		setCard1(new ItemStack(tagCompound.getCompoundTag("card1")));
		setCard2(new ItemStack(tagCompound.getCompoundTag("card2")));
		setDowel(new ItemStack(tagCompound.getCompoundTag("dowel")));
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setBoolean("broken",broken);
		tagCompound.setTag("card1", card1.writeToNBT(new NBTTagCompound()));
		tagCompound.setTag("card2", card2.writeToNBT(new NBTTagCompound()));
		tagCompound.setTag("dowel", dowel.writeToNBT(new NBTTagCompound()));
		return tagCompound;
	}
	@Override
	public NBTTagCompound getUpdateTag(){
		NBTTagCompound nbt;
		nbt = super.getUpdateTag();
		nbt.setBoolean("broken", broken);
		nbt.setTag("card1",card1.writeToNBT(new NBTTagCompound()));
		nbt.setTag("card2",card2.writeToNBT(new NBTTagCompound()));
		nbt.setTag("dowel", dowel.writeToNBT(new NBTTagCompound()));
		return nbt;
	}
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		SPacketUpdateTileEntity packet;
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("broken", broken);
		nbt.setTag("card1",card1.writeToNBT(new NBTTagCompound()));
		nbt.setTag("card2",card2.writeToNBT(new NBTTagCompound()));
		nbt.setTag("dowel", dowel.writeToNBT(new NBTTagCompound()));
		packet = new SPacketUpdateTileEntity(this.pos, 0, nbt);				
		return packet;
	}

	
	public void resendState()
	{
		if(card1.isEmpty())
		{
			if(!dowel.isEmpty()) {				
				if(AlchemyRecipeHandler.getDecodedItem(dowel).isEmpty()){
					BlockTotemlathe.updateItem(false,BlockTotemlathe2.EnumDowel.UNCARVED_DOWEL , world, pos);
				}else {
					BlockTotemlathe.updateItem(false,BlockTotemlathe2.EnumDowel.CARVED_DOWEL , world, pos);
				}
			}else {
				BlockTotemlathe.updateItem(false,BlockTotemlathe2.EnumDowel.NO_DOWEL, world, pos);
			}
		} else
		{
			if(!dowel.isEmpty()) {				
				if(AlchemyRecipeHandler.getDecodedItem(dowel).isEmpty()){
					BlockTotemlathe.updateItem(true,BlockTotemlathe2.EnumDowel.UNCARVED_DOWEL , world, pos);
				}else {
					BlockTotemlathe.updateItem(true,BlockTotemlathe2.EnumDowel.CARVED_DOWEL , world, pos);
				}
			}else {
				BlockTotemlathe.updateItem(true,BlockTotemlathe2.EnumDowel.NO_DOWEL, world, pos);
			}
		}
		
		

	}
	public void processContents()
	{
	
		ItemStack output;
		if(!dowel.isEmpty()) {
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
		


	}
	


	
}
