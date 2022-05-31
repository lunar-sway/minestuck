package com.mraof.minestuck.tileentity.machine;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.EnumDowelType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.machine.AlchemiterBlock;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.item.crafting.alchemy.*;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.tileentity.IColored;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class AlchemiterTileEntity extends TileEntity implements IColored, GristWildcardHolder, IAnimatable
{
	private final AnimationFactory factory = new AnimationFactory(this);
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
			setChanged();
			if(level != null)
			{
				BlockState state = level.getBlockState(worldPosition);
				if(state.hasProperty(AlchemiterBlock.Pad.DOWEL))    //If not, then the machine has likely been destroyed; don't bother doing anything about it
				{
					state = state.setValue(AlchemiterBlock.Pad.DOWEL, EnumDowelType.getForDowel(newDowel));
					level.setBlock(worldPosition, state, Constants.BlockFlags.BLOCK_UPDATE);
				}
			}
		}
		this.requestModelDataUpdate();
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
		else */
		if(!AlchemyHelper.hasDecodedItem(dowel))
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
		if(level != null)
		{
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 2);
		}
	}
	
	//tells the tile entity to not stop working
	public void unbreakMachine()
	{
		broken = false;
		if(level != null)
		{
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 2);
		}
	}
	
	public void dropItem(Direction direction)
	{
		if(level == null)
		{
			Debug.warn("Tried to drop alchemiter dowel before the tile entity was given a world!");
			return;
		}
		BlockPos dropPos = direction == null ? this.worldPosition : this.worldPosition.relative(direction);
		if(direction != null && Block.canSupportCenter(level, worldPosition.relative(direction), direction.getOpposite()))
			dropPos = this.worldPosition;
		
		InventoryHelper.dropItemStack(level, dropPos.getX(), dropPos.getY(), dropPos.getZ(), dowel);
		setDowel(ItemStack.EMPTY);
	}
	
	private boolean isUseable(BlockState state)
	{
		if(!broken)
		{
			checkStates();
			if(broken)
				Debug.warnf("Failed to notice a block being broken or misplaced at the alchemiter at %s", getBlockPos());
		}
		return !broken;
	}
	
	public void checkStates()
	{
		if(this.broken || level == null)
			return;
		
		if(MSBlocks.ALCHEMITER.isInvalidFromPad(level, worldPosition))
			breakMachine();
	}
	
	public Direction getFacing()
	{
		return getBlockState().getValue(AlchemiterBlock.FACING);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		
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
			dowel = ItemStack.of(nbt.getCompound("dowel"));
		
		//This a slight hack to force a rerender (since it at the time of writing normally happens before we get the update packet). This should not be done normally
		if(level != null && level.isClientSide && !ItemStack.matches(oldDowel, dowel))
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.RERENDER_MAIN_THREAD);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		compound.putString("gristType", wildcardGrist.getRegistryName().toString());
		compound.putBoolean("broken", isBroken());
		
		if(dowel != null)
			compound.put("dowel", dowel.save(new CompoundNBT()));
		
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return save(new CompoundNBT());
	}
	
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.worldPosition, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		handleUpdateTag(getBlockState(), pkt.getTag());
	}
	
	public void onRightClick(World worldIn, PlayerEntity playerIn, BlockState state, Direction side)
	{
		if(worldIn.isClientSide)
		{
			if(state.getBlock() == MSBlocks.ALCHEMITER.CENTER.get() || state.getBlock() == MSBlocks.ALCHEMITER.CORNER.get() || state.getBlock() == MSBlocks.ALCHEMITER.LEFT_SIDE.get()
					|| state.getBlock() == MSBlocks.ALCHEMITER.RIGHT_SIDE.get() || state.getBlock() == MSBlocks.ALCHEMITER.TOTEM_CORNER.get())
			{
				BlockPos mainPos = worldPosition;
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
		if(isUseable(clickedState))
		{
			if(clickedState.getBlock() == MSBlocks.ALCHEMITER.TOTEM_PAD.get())
			{
				if(!dowel.isEmpty())
				{    //Remove dowel from pad
					if(player.getMainHandItem().isEmpty())
						player.setItemInHand(Hand.MAIN_HAND, dowel);
					else if(!player.inventory.add(dowel))
						dropItem(side);
					else player.inventoryMenu.broadcastChanges();
					
					setDowel(ItemStack.EMPTY);
				} else
				{
					ItemStack heldStack = player.getMainHandItem();
					if(!heldStack.isEmpty() && heldStack.getItem() == MSBlocks.CRUXITE_DOWEL.asItem())
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
		
		Direction facing = level.getBlockState(worldPosition).getValue(AlchemiterBlock.FACING);
		//get the position to spawn the item
		BlockPos spawnPos = this.getBlockPos().relative(facing.getOpposite()).relative(facing.getCounterClockWise());
		if(facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE)
			spawnPos = spawnPos.relative(facing.getOpposite());
		if(facing.getClockWise().getAxisDirection() == Direction.AxisDirection.NEGATIVE)
			spawnPos = spawnPos.relative(facing.getCounterClockWise());
		//get the grist cost
		GristSet cost = getGristCost(quantity);
		
		boolean canAfford = GristHelper.canAfford(player, cost);
		
		if(canAfford)
		{
			
			
			PlayerIdentifier pid = IdentifierHandler.encode(player);
			GristHelper.decrease(level, pid, cost);
			
			AlchemyEvent event = new AlchemyEvent(pid, this, getDowel(), newItem, cost);
			MinecraftForge.EVENT_BUS.post(event);
			newItem = event.getItemResult();
			
			while(quantity > 0)
			{
				ItemStack stack = newItem.copy();
				stack.setCount(Math.min(stack.getMaxStackSize(), quantity));
				quantity -= stack.getCount();
				ItemEntity item = new ItemEntity(level, spawnPos.getX(), spawnPos.getY() + 0.5, spawnPos.getZ(), stack);
				level.addFreshEntity(item);
			}
		}
	}
	
	public GristSet getGristCost(int quantity)
	{
		ItemStack dowel = getDowel();
		GristSet set;
		ItemStack stack = getOutput();
		if(dowel.isEmpty() || level == null)
			return null;
		
		stack.setCount(quantity);
		//get the grist cost of stack
		set = GristCostRecipe.findCostForItem(stack, getWildcardGrist(), false, level);
		
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
			this.setChanged();
			if(level != null && !level.isClientSide)
				level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return INFINITE_EXTENT_AABB;
	}
	
	@Override
	public AnimationFactory getFactory()
	{
		return factory;
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController<>(this, "scanAnimation", 0, this::scanAnimation));
	}
	
	private <E extends TileEntity & IAnimatable> PlayState scanAnimation(AnimationEvent<E> event)
	{
		if(!this.dowel.isEmpty())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("scan", false));
			return PlayState.CONTINUE;
		}
		event.getController().markNeedsReload();
		return PlayState.STOP;
	}
}