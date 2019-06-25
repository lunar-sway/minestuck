package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.EnumDowelType;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.gui.GuiAlchemiter;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraft.world.World;

public class TileEntityAlchemiter extends TileEntity
{
	
	protected GristType wildcardGrist = GristType.BUILD;
	protected boolean broken = false;
	protected ItemStack dowel = ItemStack.EMPTY;
	protected ItemStack upgradeItem[] = {ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY};
	protected AlchemiterUpgrades upgrade[] = new AlchemiterUpgrades[7];
	public boolean upgraded = false;
	protected TileEntity jbe = null;
	
	public TileEntityAlchemiter()
	{
		super(MinestuckTiles.ALCHEMITER);
	}
	
	public void setDowel(ItemStack newDowel)
	{
		if(newDowel.getItem() == MinestuckBlocks.CRUXITE_DOWEL.asItem() || newDowel.isEmpty())
		{
			dowel = newDowel;
			if(world != null)
			{
				IBlockState state = world.getBlockState(pos);
				if(newDowel.isEmpty())
					state = state.with(BlockAlchemiter.Pad.DOWEL, EnumDowelType.NONE);
				else if(AlchemyRecipes.hasDecodedItem(newDowel))
					state = state.with(BlockAlchemiter.Pad.DOWEL, EnumDowelType.CARVED_DOWEL);
					else state = state.with(BlockAlchemiter.Pad.DOWEL, EnumDowelType.DOWEL);
				
				world.setBlockState(pos, state, 2);
			}
			markDirty();
		}
	}
	
	public ItemStack getDowel()
	{
		return dowel;
	}
	
	public ItemStack getOutput()
	{
		/*if(hasUpgrade(AlchemiterUpgrades.captchaCard))
		{
		if (!AlchemyRecipes.hasDecodedItem(dowel))
			return AlchemyRecipes.createCard(new ItemStack(MinestuckBlocks.GENERIC_OBJECT), false);
		else return AlchemyRecipes.createCard(new ItemStack(AlchemyRecipes.getDecodedItem(dowel).getItem(), 1), false);
		}
		else */if (!AlchemyRecipes.hasDecodedItem(dowel))
			return new ItemStack(MinestuckBlocks.GENERIC_OBJECT);
		else return AlchemyRecipes.getDecodedItem(dowel);
	}
	
	/**
	 * @return true if the machine is marked as broken
	 */
	public boolean isBroken()
	{
		return broken;
	}
	
	//tells the tile entity to stop working
	public void breakMachine()
	{
		broken = true;
		if(world != null)
		{
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 2);
		}
	}

	//tells the tile entity to not stop working
		public void unbreakMachine()
		{
			broken = false;
			if(world != null)
			{
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 2);
			}
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
	
	//JBE upgrades
	public void setUpgrade(ItemStack stack, int id)
	{
		if(!stack.isEmpty())
		{
			this.upgradeItem[id] = stack;
			if(world != null)
			{
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 2);
			}
		}
	}
	
	public void setUpgraded(boolean bool, BlockPos pos)
	{
		
		TileEntity te = world.getTileEntity(pos);
		
		if(te instanceof TileEntityJumperBlock)
			jbe = te;
		else
		{
			Debug.warnf("%s is not a jbe tile entity", te);
			return;
		}
		
		TileEntityJumperBlock jbeTe = (TileEntityJumperBlock) te;
		this.upgraded = bool;
		
		if(bool == true)
		{
			for(int i = 0; i < upgradeItem.length; i++)
			{
				this.upgradeItem[i] = jbeTe.getUpgrade(i);
				this.upgradeItem[i].setCount(1);
			}
			
		}
		else
		{
			for(int i = 0; i < upgradeItem.length; i++)
			{
				this.upgradeItem[i] = ItemStack.EMPTY;
			}
		}
		
		//this.upgrade = AlchemiterUpgrades.getUpgradesFromList(getUpgradeItemsList());
	}
	
		
	/*public boolean hasUpgrade(AlchemiterUpgrades upgrade)
	{
		return AlchemiterUpgrades.hasUpgrade(getUpgradeItemsList(), upgrade);
	}*/
	
	public boolean isUpgraded()
	{
		return this.upgraded;
	}
	
	//TODO
	public void doTheBlenderThing()
	{
		if(!dowel.isEmpty())
		{
			InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(MinestuckItems.RAW_CRUXITE, 1));
			setDowel(ItemStack.EMPTY);
		}
	}
	
	private boolean isUseable(IBlockState state)
	{
		if(!broken)
		{
			checkStates();
			if(broken)
				Debug.warnf("Failed to notice a block being broken or misplaced at the alchemiter at %s", getPos());
		}
		return !broken;
	}
	
	public AlchemiterUpgrades[] getUpgradeList()
	{
		return upgrade;
	}
	
	public ItemStack[] getUpgradeItemsList()
	{
		return this.upgradeItem;
	}
	
	public ItemStack getUpgrade(int id)
	{
		return upgradeItem[id];
	}
	
	public void checkStates()
	{
		if(this.broken || world == null)
			return;
		
		EnumFacing facing = world.getBlockState(this.getPos()).get(BlockAlchemiter.FACING);
		EnumFacing x = facing.rotateYCCW();
		EnumFacing z = facing.getOpposite();
		BlockPos pos = getPos().down();
		if(!world.getBlockState(pos.up(3)).equals(MinestuckBlocks.ALCHEMITER.UPPER_ROD.getDefaultState().with(BlockAlchemiter.FACING, facing)) ||
				!world.getBlockState(pos.up(2)).equals(MinestuckBlocks.ALCHEMITER.LOWER_ROD.getDefaultState().with(BlockAlchemiter.FACING, facing)) ||
				//!world.getBlockState(pos.up()).equals(MinestuckBlocks.ALCHEMITER.TOTEM_PAD.getDefaultState().with(BlockAlchemiter.FACING, facing)) ||
				!world.getBlockState(pos).equals(MinestuckBlocks.ALCHEMITER.TOTEM_CORNER.getDefaultState().with(BlockAlchemiter.FACING, facing)) ||
				!world.getBlockState(pos.offset(x)).equals(MinestuckBlocks.ALCHEMITER.LEFT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing)) ||
				!world.getBlockState(pos.offset(x, 2)).equals(MinestuckBlocks.ALCHEMITER.RIGHT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing)) ||
				!world.getBlockState(pos.offset(z).offset(x)).equals(MinestuckBlocks.ALCHEMITER.CENTER.getDefaultState().with(BlockAlchemiter.FACING, facing)) ||
				!world.getBlockState(pos.offset(x, 3)).equals(MinestuckBlocks.ALCHEMITER.CORNER.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateYCCW())) ||
				!world.getBlockState(pos.offset(z).offset(x, 3)).equals(MinestuckBlocks.ALCHEMITER.LEFT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateYCCW())) ||
				!world.getBlockState(pos.offset(z, 2).offset(x, 3)).equals(MinestuckBlocks.ALCHEMITER.RIGHT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateYCCW())) ||
				!world.getBlockState(pos.offset(z).offset(x, 2)).equals(MinestuckBlocks.ALCHEMITER.CENTER.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateYCCW())) ||
				!world.getBlockState(pos.offset(z, 3).offset(x, 3)).equals(MinestuckBlocks.ALCHEMITER.CORNER.getDefaultState().with(BlockAlchemiter.FACING, facing.getOpposite())) ||
				!world.getBlockState(pos.offset(z, 3).offset(x, 2)).equals(MinestuckBlocks.ALCHEMITER.LEFT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.getOpposite())) ||
				!world.getBlockState(pos.offset(z, 3).offset(x, 1)).equals(MinestuckBlocks.ALCHEMITER.RIGHT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.getOpposite())) ||
				!world.getBlockState(pos.offset(z, 2).offset(x, 2)).equals(MinestuckBlocks.ALCHEMITER.CENTER.getDefaultState().with(BlockAlchemiter.FACING, facing.getOpposite())) ||
				!world.getBlockState(pos.offset(z, 3)).equals(MinestuckBlocks.ALCHEMITER.CORNER.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateY())) ||
				!world.getBlockState(pos.offset(z, 2)).equals(MinestuckBlocks.ALCHEMITER.LEFT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateY())) ||
				!world.getBlockState(pos.offset(z)).equals(MinestuckBlocks.ALCHEMITER.RIGHT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateY())) ||
				!world.getBlockState(pos.offset(z, 2).offset(x, 1)).equals(MinestuckBlocks.ALCHEMITER.CENTER.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateY())))
			
		{
			breakMachine();
			return;
		}
		
		return;
	}
	
	public EnumFacing getFacing()
	{
		return getBlockState().get(BlockAlchemiter.FACING);
	}
	
	@Override
	public void read(NBTTagCompound tagCompound)
	{
		super.read(tagCompound);

		if(tagCompound.hasKey("gristType"))
			this.wildcardGrist = GristType.getTypeFromString(tagCompound.getString("gristType"));
		if(this.wildcardGrist == null)
		{
			this.wildcardGrist = GristType.BUILD;
		}
		
		this.upgraded = tagCompound.getBoolean("upgraded");
		
		if(upgraded)
		{
			for(int i = 0; i < upgradeItem.length; i++)
			{
				setUpgrade(ItemStack.read(tagCompound.getCompound("upgrade" + i)), i);
			}
		}
		
		broken = tagCompound.getBoolean("broken");
		
		if(tagCompound.hasKey("dowel")) 
			dowel = ItemStack.read(tagCompound.getCompound("dowel"));
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound tagCompound)
	{
		super.write(tagCompound);

		tagCompound.setString("gristType", wildcardGrist.getRegistryName().toString());
		tagCompound.setBoolean("upgraded", upgraded);
		tagCompound.setBoolean("broken", isBroken());
		
		for(int i = 0; i < upgradeItem.length; i++)
		{
			tagCompound.setTag("upgrade" + i, upgradeItem[i].write(new NBTTagCompound()));
		}
		
		if(dowel!= null)
			tagCompound.setTag("dowel", dowel.write(new NBTTagCompound()));
		
		return tagCompound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return write(new NBTTagCompound());
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
		return packet;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	public void onRightClick(World worldIn, EntityPlayer playerIn, IBlockState state)
	{
		if(worldIn.isRemote)
		{
			if(state.getBlock() == MinestuckBlocks.ALCHEMITER.CENTER || state.getBlock() == MinestuckBlocks.ALCHEMITER.CORNER || state.getBlock() == MinestuckBlocks.ALCHEMITER.LEFT_SIDE
					|| state.getBlock() == MinestuckBlocks.ALCHEMITER.RIGHT_SIDE || state.getBlock() == MinestuckBlocks.ALCHEMITER.TOTEM_CORNER)
			{
				BlockPos mainPos = pos;
				if(!isBroken())
				{
					Minecraft.getInstance().displayGuiScreen(new GuiAlchemiter(this));
				}
			}
			return;
		}
		else
		{
			//if(hasUpgrade(AlchemiterUpgrades.blender) && !dowel.isEmpty())
			//	doTheBlenderThing();
		}
		
		onPadRightClick(playerIn, state);
	}
	
	public void onPadRightClick(EntityPlayer player, IBlockState clickedState)
	{
		if (isUseable(clickedState))
		{
			if(clickedState.getBlock() == MinestuckBlocks.ALCHEMITER.TOTEM_PAD)
			{
				if (!dowel.isEmpty())
				{    //Remove dowel from pad
					if (player.getHeldItemMainhand().isEmpty())
						player.setHeldItem(EnumHand.MAIN_HAND, dowel);
					else if (!player.inventory.addItemStackToInventory(dowel))
						dropItem(false);
					else player.inventoryContainer.detectAndSendChanges();
					
					setDowel(ItemStack.EMPTY);
				} else
				{
					ItemStack heldStack = player.getHeldItemMainhand();
					if (!heldStack.isEmpty() && heldStack.getItem() == MinestuckBlocks.CRUXITE_DOWEL.asItem())
						setDowel(heldStack.split(1));    //Put a dowel on the pad
				}
			}
		}
	}
	
	public void processContents(int quantity, EntityPlayerMP player)
	{
		ItemStack newItem = getOutput();
		//Clamp quantity
		quantity = Math.min(newItem.getMaxStackSize() * MinestuckConfig.alchemiterMaxStacks, Math.max(1, quantity));
		
		EnumFacing facing = world.getBlockState(pos).get(BlockAlchemiter.FACING);
		//get the position to spawn the item
		BlockPos spawnPos = this.getPos().offset(facing.getOpposite()).offset(facing.rotateYCCW());
		if(facing.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE)
			spawnPos = spawnPos.offset(facing.getOpposite());
		if(facing.rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE)
			spawnPos = spawnPos.offset(facing.rotateYCCW());
		//get the grist cost
		GristSet cost = getGristCost(quantity);
		
		boolean canAfford = GristHelper.canAfford(MinestuckPlayerData.getGristSet(player), cost);
		
		if(canAfford)
		{
			while(quantity > 0)
			{
				ItemStack stack = newItem.copy();
				//TODO
				/*if(hasUpgrade(AlchemiterUpgrades.captchaCard)) {
					int stackCount =  Math.min(AlchemyRecipes.getDecodedItem(stack).getMaxStackSize(), quantity);
					
					stack = AlchemyRecipes.changeEncodeSize(stack, stackCount);
					quantity -=  Math.min(AlchemyRecipes.getDecodedItem(stack).getMaxStackSize(), quantity);
				}
				else*/{
					stack.setCount(Math.min(stack.getMaxStackSize(), quantity));
					quantity -= stack.getCount();
				}
				EntityItem item = new EntityItem(world, spawnPos.getX(), spawnPos.getY() + 0.5, spawnPos.getZ(), stack);
				world.spawnEntity(item);
			}
			
			AlchemyRecipes.onAlchemizedItem(newItem, player);
			
			PlayerIdentifier pid = IdentifierHandler.encode(player);
			GristHelper.decrease(world.getServer(), pid, cost);
			MinestuckPlayerTracker.updateGristCache(world.getServer(), pid);
		}
	}
	
	public GristSet getGristCost(int quantity)
	{
		ItemStack dowel = getDowel();
		GristSet set;
		ItemStack stack = getOutput();
		//if(hasUpgrade(AlchemiterUpgrades.captchaCard))
		//	stack = AlchemyRecipes.getDecodedItem(getOutput());
		boolean useSelectedType;
		if(dowel.isEmpty())
			return null;
		
		//get the grist cost of stack
		set = AlchemyCostRegistry.getGristConversion(stack);

		//if the item is a captcha card do other stuff
		useSelectedType = stack.getItem() == MinestuckItems.CAPTCHA_CARD;
		if (useSelectedType)
			set = new GristSet(getWildcardGrist(), !world.isRemote ? MinestuckConfig.cardCost : MinestuckConfig.clientCardCost);
		
		if (set != null)
		{
			//multiply cost by quantity
			set.scaleGrist(quantity);
		}
		
		return set;
	}

	public GristType getWildcardGrist()
	{
		return wildcardGrist;
	}
	
	public void setWildcardGrist(GristType wildcardGrist)
	{
		if(this.wildcardGrist != wildcardGrist)
		{
			this.wildcardGrist = wildcardGrist;
			this.markDirty();
		}
	}
}