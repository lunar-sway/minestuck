package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.EnumDowelType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.machine.AlchemiterBlock;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.item.crafting.alchemy.*;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;

public class AlchemiterTileEntity extends TileEntity implements IColored, GristWildcardHolder
{
	
	private GristType wildcardGrist = GristTypes.BUILD.get();
	protected boolean broken = false;
	protected ItemStack dowel = ItemStack.EMPTY;
	
	public AlchemiterTileEntity()
	{
		super(MSTileEntityTypes.ALCHEMITER.get());
	}
	
	public void setDowel(ItemStack newDowel)
	{
		if(newDowel.getItem() == MSBlocks.CRUXITE_DOWEL.asItem() || newDowel.isEmpty())
		{
			dowel = newDowel;
			markDirty();
			if(world != null)
			{
				BlockState state = world.getBlockState(pos);
				if(state.hasProperty(AlchemiterBlock.Pad.DOWEL))	//If not, then the machine has likely been destroyed; don't bother doing anything about it
				{
					state = state.with(AlchemiterBlock.Pad.DOWEL, EnumDowelType.getForDowel(newDowel));
					world.setBlockState(pos, state, Constants.BlockFlags.BLOCK_UPDATE);
				}
			}
		}
	}
	
	public ItemStack getDowel()
	{
		return dowel;
	}
	
	@Override
	public int getColor()
	{
		return ColorHandler.getColorFromStack(dowel);
	}
	
	public ItemStack getOutput()
	{
		/*if(hasUpgrade(AlchemiterUpgrades.captchaCard))
		{
		if (!AlchemyRecipes.hasDecodedItem(dowel))
			return AlchemyRecipes.createCard(new ItemStack(MinestuckBlocks.GENERIC_OBJECT), false);
		else return AlchemyRecipes.createCard(new ItemStack(AlchemyRecipes.getDecodedItem(dowel).getItem(), 1), false);
		}
		else */if (!AlchemyHelper.hasDecodedItem(dowel))
			return new ItemStack(MSBlocks.GENERIC_OBJECT);
		else return AlchemyHelper.getDecodedItem(dowel);
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
			BlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 2);
		}
	}

	//tells the tile entity to not stop working
		public void unbreakMachine()
		{
			broken = false;
			if(world != null)
			{
				BlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 2);
			}
		}
	
	public void dropItem(Direction direction)
	{
		if(world == null)
		{
			Debug.warn("Tried to drop alchemiter dowel before the tile entity was given a world!");
			return;
		}
		BlockPos dropPos = direction == null ? this.pos : this.pos.offset(direction);
		if(direction != null && Block.hasEnoughSolidSide(world, pos.offset(direction), direction.getOpposite()))
			dropPos = this.pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), dowel);
		setDowel(ItemStack.EMPTY);
	}
	
	private boolean isUseable(BlockState state)
	{
		if(!broken)
		{
			checkStates();
			if(broken)
				Debug.warnf("Failed to notice a block being broken or misplaced at the alchemiter at %s", getPos());
		}
		return !broken;
	}
	
	public void checkStates()
	{
		if(this.broken || world == null)
			return;
		
		if(MSBlocks.ALCHEMITER.isInvalidFromPad(world, pos))
			breakMachine();
	}
	
	public Direction getFacing()
	{
		return getBlockState().get(AlchemiterBlock.FACING);
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt)
	{
		super.read(state, nbt);
		
		wildcardGrist = GristType.read(nbt, "gristType");
		
		/*
		this.upgraded = compound.getBoolean("upgraded");
		
		if(upgraded)
		{
			for(int i = 0; i < upgradeItem.length; i++)
			{
				setUpgrade(ItemStack.read(compound.getCompound("upgrade" + i)), i);
			}
		}
		*/
		
		broken = nbt.getBoolean("broken");
		
		ItemStack oldDowel = dowel;
		if(nbt.contains("dowel"))
			dowel = ItemStack.read(nbt.getCompound("dowel"));
		
		//This a slight hack to force a rerender (since it at the time of writing normally happens before we get the update packet). This should not be done normally
		if(world != null && world.isRemote && !ItemStack.areItemStacksEqual(oldDowel, dowel))
			world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.RERENDER_MAIN_THREAD);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);

		compound.putString("gristType", wildcardGrist.getRegistryName().toString());
		compound.putBoolean("broken", isBroken());
		
		if(dowel!= null)
			compound.put("dowel", dowel.write(new CompoundNBT()));
		
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return write(new CompoundNBT());
	}
	
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		handleUpdateTag(getBlockState(), pkt.getNbtCompound());
	}
	
	public void onRightClick(World worldIn, PlayerEntity playerIn, BlockState state, Direction side)
	{
		if(worldIn.isRemote)
		{
			if(state.getBlock() == MSBlocks.ALCHEMITER.CENTER.get() || state.getBlock() == MSBlocks.ALCHEMITER.CORNER.get() || state.getBlock() == MSBlocks.ALCHEMITER.LEFT_SIDE.get()
					|| state.getBlock() == MSBlocks.ALCHEMITER.RIGHT_SIDE.get() || state.getBlock() == MSBlocks.ALCHEMITER.TOTEM_CORNER.get())
			{
				BlockPos mainPos = pos;
				if(!isBroken())
				{
					MSScreenFactories.displayAlchemiterScreen(this);
				}
			}
			return;
		}
		
		onPadRightClick(playerIn, state, side);
	}
	
	public void onPadRightClick(PlayerEntity player, BlockState clickedState, Direction side)
	{
		if (isUseable(clickedState))
		{
			if(clickedState.getBlock() == MSBlocks.ALCHEMITER.TOTEM_PAD.get())
			{
				if (!dowel.isEmpty())
				{    //Remove dowel from pad
					if (player.getHeldItemMainhand().isEmpty())
						player.setHeldItem(Hand.MAIN_HAND, dowel);
					else if (!player.inventory.addItemStackToInventory(dowel))
						dropItem(side);
					else player.container.detectAndSendChanges();
					
					setDowel(ItemStack.EMPTY);
				} else
				{
					ItemStack heldStack = player.getHeldItemMainhand();
					if (!heldStack.isEmpty() && heldStack.getItem() == MSBlocks.CRUXITE_DOWEL.asItem())
						setDowel(heldStack.split(1));    //Put a dowel on the pad
				}
			}
		}
	}
	
	public void processContents(int quantity, ServerPlayerEntity player)
	{
		ItemStack newItem = getOutput();
		//Clamp quantity
		quantity = Math.min(newItem.getMaxStackSize() * MinestuckConfig.SERVER.alchemiterMaxStacks.get(), Math.max(1, quantity));
		
		Direction facing = world.getBlockState(pos).get(AlchemiterBlock.FACING);
		//get the position to spawn the item
		BlockPos spawnPos = this.getPos().offset(facing.getOpposite()).offset(facing.rotateYCCW());
		if(facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE)
			spawnPos = spawnPos.offset(facing.getOpposite());
		if(facing.rotateY().getAxisDirection() == Direction.AxisDirection.NEGATIVE)
			spawnPos = spawnPos.offset(facing.rotateYCCW());
		//get the grist cost
		GristSet cost = getGristCost(quantity);
		
		boolean canAfford = GristHelper.canAfford(player, cost);
		
		if(canAfford)
		{
			
			
			PlayerIdentifier pid = IdentifierHandler.encode(player);
			GristHelper.decrease(world, pid, cost);
			
			AlchemyEvent event = new AlchemyEvent(pid, this, getDowel(), newItem, cost);
			MinecraftForge.EVENT_BUS.post(event);
			newItem = event.getItemResult();
			
			while(quantity > 0)
			{
				ItemStack stack = newItem.copy();
				stack.setCount(Math.min(stack.getMaxStackSize(), quantity));
				quantity -= stack.getCount();
				ItemEntity item = new ItemEntity(world, spawnPos.getX(), spawnPos.getY() + 0.5, spawnPos.getZ(), stack);
				world.addEntity(item);
			}
		}
	}
	
	public GristSet getGristCost(int quantity)
	{
		ItemStack dowel = getDowel();
		GristSet set;
		ItemStack stack = getOutput();
		if(dowel.isEmpty() || world == null)
			return null;
		
		stack.setCount(quantity);
		//get the grist cost of stack
		set = GristCostRecipe.findCostForItem(stack, getWildcardGrist(), false, world);
		
		return set;
	}

	public GristType getWildcardGrist()
	{
		return wildcardGrist;
	}
	
	@Override
	public void setWildcardGrist(GristType wildcardGrist)
	{
		if(this.wildcardGrist != wildcardGrist)
		{
			this.wildcardGrist = wildcardGrist;
			this.markDirty();
			if(world != null && !world.isRemote)
				world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 0);
		}
	}
}