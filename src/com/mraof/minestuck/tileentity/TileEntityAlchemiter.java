package com.mraof.minestuck.tileentity;


import java.util.Iterator;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.BlockAlchemiter.EnumParts;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityAlchemiter extends TileEntity
{
	//still private because programming teacher and data protection
	private PlayerIdentifier owner;
	public GristType selectedGrist = GristType.Build;
	private boolean broken=false;
	private ItemStack dowel=ItemStack.EMPTY;

	
	public void setDowel(ItemStack newDowel) {
		if(newDowel.getItem()==MinestuckItems.cruxiteDowel||newDowel==ItemStack.EMPTY) {
			dowel=newDowel;
			resendState();
			
		}
	}
	public ItemStack getDowel() {
		return dowel;
	}
	
	public PlayerIdentifier getOwner(){
		return owner;
	}
	public void setOwner(PlayerIdentifier Owner){
		owner= Owner;
	}
	
	
	
	//checks if the tile enity should work
	public boolean isBroken() {
		return broken;
	}
	//tells the tile entity to stop working
	public void brake() {
		broken = true;		
	}

	
	public void dropItem(boolean inBlock)
	{
		BlockPos dropPos;
		if(inBlock)
			dropPos = this.pos;
		else if(!world.getBlockState(this.pos).isBlockNormalCube())
			dropPos = this.pos;
		else if(!world.getBlockState(this.pos.up()).isBlockNormalCube())
			dropPos = this.pos.up();
		else dropPos = this.pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), dowel);
		setDowel(ItemStack.EMPTY);
	}
	

	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		
		if(tagCompound.hasKey("gristType"))
			this.selectedGrist = GristType.getTypeFromString(tagCompound.getString("gristType"));
		if(this.selectedGrist == null)
		{
			this.selectedGrist = GristType.Build;
		}
		
		if(tagCompound.hasKey("owner") || tagCompound.hasKey("ownerMost"))
			owner = IdentifierHandler.load(tagCompound, "owner");
		
		if(tagCompound.hasKey("dowel")) 
			setDowel(new ItemStack(tagCompound.getCompoundTag("dowel")));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		
		tagCompound.setString("gristType", selectedGrist.getRegistryName().toString());
		
		if(owner != null)
			owner.saveToNBT(tagCompound, "owner");

		if(dowel!= null)
			tagCompound.setTag("dowel", dowel.writeToNBT(new NBTTagCompound()));
		
		return tagCompound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag(){
		NBTTagCompound nbt;
		nbt = super.getUpdateTag();
		nbt.setTag("dowel",dowel.writeToNBT(new NBTTagCompound()));
		return nbt;
	}
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		SPacketUpdateTileEntity packet;
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("dowel",dowel.writeToNBT(new NBTTagCompound()));
		dowel.writeToNBT(nbt);
		packet = new SPacketUpdateTileEntity(this.pos, 0, nbt);				
		return packet;
	}
	
	public int comparatorValue()
	{
		if (dowel != null && owner != null) {
			ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(dowel);
			if (newItem.isEmpty())
				if (!dowel.hasTagCompound() || !dowel.getTagCompound().hasKey("contentID"))
					newItem = new ItemStack(MinestuckBlocks.genericObject);
				else return 0;
			GristSet cost = GristRegistry.getGristConversion(newItem);
			if (newItem.getItem() == MinestuckItems.captchaCard)
				cost = new GristSet(getSelectedGrist(), MinestuckConfig.cardCost);
			if (cost != null && newItem.isItemDamaged()) {
				float multiplier = 1 - newItem.getItem().getDamage(newItem) / ((float) newItem.getMaxDamage());
				for (GristAmount amount : cost.getArray())
				{
					cost.setGrist(amount.getType(), (int) Math.ceil(amount.getAmount() * multiplier));
				}
			}
			// We need to run the check 16 times. Don't want to hammer the game with too many of these, so the comparators are only told to update every 20 ticks.
			// Additionally, we need to check if the item in the slot is empty. Otherwise, it will attempt to check the cost for air, which cannot be alchemized anyway.
			if (cost != null && !dowel.isEmpty()) {
				GristSet scale_cost;
				for (int lvl = 1; lvl <= 17; lvl++) {
					// We went through fifteen item cost checks and could still afford it. No sense in checking more than this.
					if (lvl == 17) {
						return 15;
					}
					// We need to make a copy to preserve the original grist amounts and avoid scaling values that have already been scaled. Keeps scaling linear as opposed to exponential.
					scale_cost = cost.copy().scaleGrist(lvl);
					if (!GristHelper.canAfford(MinestuckPlayerData.getGristSet(owner), scale_cost)) {
						return lvl - 1;
					}
				}
				return 0;
			}
		}
		
		return 0;
	}
	
	
	public void onRightClick(EntityPlayer player, IBlockState clickedState)
	{
		BlockAlchemiter alchemiter=(BlockAlchemiter)clickedState.getBlock();
		EnumParts part = clickedState.getValue(alchemiter.PART);
		if(part.equals(EnumParts.TOTEM_PAD) && !dowel.isEmpty())
		{	//Remove card from punch slot
			if(player.getHeldItemMainhand().isEmpty())
				player.setHeldItem(EnumHand.MAIN_HAND, dowel);
			else if(!player.inventory.addItemStackToInventory(dowel))
				dropItem(false);
			
			setDowel(ItemStack.EMPTY);
			return;
		}
		

		ItemStack heldStack = player.getHeldItemMainhand();
		if(part.equals(EnumParts.TOTEM_PAD) && dowel.isEmpty())
		{
			if(!heldStack.isEmpty() && heldStack.getItem() == MinestuckItems.cruxiteDowel)
				setDowel(heldStack.splitStack(1));	//Insert card into the punch slot
		} 
		//it it's part of the pad
		if(part==EnumParts.CENTER_PAD||part==EnumParts.CORNER||part==EnumParts.EDGE_LEFT||part==EnumParts.EDGE_RIGHT) {
			/**
			 * bring up the gui
			 * and stuff
			 * 
			 * 
			 * 
			 */
			player.openGui(Minestuck.instance, GuiHandler.GuiId.ALCHEMITER.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());

			
		}
			
			
	}
	

	
	public void processContents(int quantity)
	{
		if(quantity==0) {
			return;
		}
		EnumFacing facing = world.getBlockState(pos).getValue(BlockAlchemiter.DIRECTION);
		ItemStack newItem = AlchemyRecipeHandler.getDecodedItem(dowel);
		if( !(dowel.hasTagCompound() && dowel.getTagCompound().hasKey("contentID")))
			newItem = new ItemStack(MinestuckBlocks.genericObject);
		BlockPos pos =this.getPos().offset(facing).offset(facing.rotateY()).up();
		newItem.setCount(quantity);
		GristSet cost = GristRegistry.getGristConversion(newItem);
		EntityPlayerMP player = owner.getPlayer();

		
		for (GristAmount amount : cost.getArray()) {
			cost.setGrist(amount.getType(), amount.getAmount()*quantity);
		}
		
		boolean CanAfford=true;
		
		
		
		for (GristAmount amount : cost.getArray()) {
			GristType type=amount.getType();
			//if they dont have enough of said grist type
			if(!(cost.getGrist(type) <= MinestuckPlayerData.getClientGrist().getGrist(type))) {
				CanAfford=false;
			}
		}
		
		if(CanAfford){
			EntityItem item=new EntityItem(world, pos.getX(),pos.getY(), pos.getZ(),newItem);
			world.spawnEntity(item);
			if(player != null)
				MinestuckAchievementHandler.onAlchemizedItem(newItem, player);
			
			if(newItem.getItem() == MinestuckItems.captchaCard)
				cost = new GristSet(getSelectedGrist(), MinestuckConfig.cardCost);
			if(newItem.isItemDamaged())
			{
				float multiplier = 1 - newItem.getItem().getDamage(newItem) / ((float) newItem.getMaxDamage());
				for (GristAmount amount : cost.getArray())
				{
					cost.setGrist(amount.getType(), (int) Math.ceil(amount.getAmount() * multiplier));
				}
			}
			GristHelper.decrease(owner, cost);
			MinestuckPlayerTracker.updateGristCache(owner);
		}
	}
	
	
	@Override
	public void markDirty()
	{
		super.markDirty();
	}


	public GristType getSelectedGrist() {
		return selectedGrist;
	}
	public void setSelectedGrist(GristType selectedGrist) {
		this.selectedGrist = selectedGrist;
	}

	
	public void resendState()
	{
		if(dowel.isEmpty())
		{
			BlockAlchemiter.updateItem(false, world, this.getPos());
		} else
		{
			BlockAlchemiter.updateItem(true, world, this.getPos());
		}
	}
	

	
}
