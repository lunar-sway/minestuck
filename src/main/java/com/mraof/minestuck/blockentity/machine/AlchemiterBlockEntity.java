package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.block.EnumDowelType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.machine.AlchemiterBlock;
import com.mraof.minestuck.blockentity.IColored;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.player.GristCache;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.MSParticleType;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Stores a cruxite dowel and through use of interface can take a players grist and create new copies of the item the dowel encodes.
 * When a new dowel is placed, animation plays where the arm "scans" the dowels code. Core Editmode deployable
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlchemiterBlockEntity extends BlockEntity implements IColored, GristWildcardHolder, GeoBlockEntity
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final RawAnimation SCAN_ANIMATION = RawAnimation.begin().then("scan", Animation.LoopType.PLAY_ONCE);
	
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	
	private GristType wildcardGrist = GristTypes.BUILD.get();
	protected boolean broken = false;
	protected ItemStack dowel = ItemStack.EMPTY;
	
	public AlchemiterBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.ALCHEMITER.get(), pos, state);
	}
	
	public void setDowel(ItemStack newDowel)
	{
		if(newDowel.getItem() == MSBlocks.CRUXITE_DOWEL.get().asItem() || newDowel.isEmpty())
		{
			dowel = newDowel;
			setChanged();
			if(level != null)
			{
				BlockState state = level.getBlockState(worldPosition);
				if(state.hasProperty(AlchemiterBlock.Pad.DOWEL))    //If not, then the machine has likely been destroyed; don't bother doing anything about it
				{
					state = state.setValue(AlchemiterBlock.Pad.DOWEL, EnumDowelType.getForDowel(newDowel));
					level.setBlock(worldPosition, state, Block.UPDATE_CLIENTS);
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
		if (!AlchemyHelper.hasDecodedItem(dowel))
			return new ItemStack(MSBlocks.GENERIC_OBJECT.get());
		else return AlchemyHelper.getDecodedItem(dowel);
	}
	
	/**
	 * @return true if the machine is marked as broken
	 */
	public boolean isBroken()
	{
		return broken;
	}
	
	//tells the block entity to stop working
	public void breakMachine()
	{
		broken = true;
		if(level != null)
		{
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 2);
		}
	}
	
	//tells the block entity to not stop working
		public void unbreakMachine()
		{
			broken = false;
			if(level != null)
			{
				BlockState state = level.getBlockState(worldPosition);
				level.sendBlockUpdated(worldPosition, state, state, 2);
			}
		}
	
	public void dropItem(@Nullable Direction direction)
	{
		if(level == null)
		{
			LOGGER.warn("Tried to drop alchemiter dowel before the block entity was given a world!");
			return;
		}
		BlockPos dropPos = direction == null ? this.worldPosition : this.worldPosition.relative(direction);
		if(direction != null && Block.canSupportCenter(level, worldPosition.relative(direction), direction.getOpposite()))
			dropPos = this.worldPosition;
		
		Containers.dropItemStack(level, dropPos.getX(), dropPos.getY(), dropPos.getZ(), dowel);
		setDowel(ItemStack.EMPTY);
	}
	
	private boolean isUseable(BlockState state)
	{
		if(!broken)
		{
			checkStates();
			if(broken)
				LOGGER.warn("Failed to notice a block being broken or misplaced at the alchemiter at {}", getBlockPos());
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
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		
		wildcardGrist = GristHelper.parseGristType(nbt.get("gristType")).orElseGet(GristTypes.BUILD);
		
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
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_IMMEDIATE);
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		compound.put("gristType", GristHelper.encodeGristType(wildcardGrist));
		compound.putBoolean("broken", isBroken());
		
		if(dowel!= null)
			compound.put("dowel", dowel.save(new CompoundTag()));
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		return saveWithoutMetadata();
	}
	
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	public void onRightClick(Level level, Player playerIn, BlockState state, Direction side)
	{
		if(level.isClientSide)
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
	
	public void onPadRightClick(Player player, BlockState clickedState, Direction side)
	{
		if(isUseable(clickedState))
		{
			if(clickedState.getBlock() == MSBlocks.ALCHEMITER.TOTEM_PAD.get())
			{
				if(!dowel.isEmpty())
				{    //Remove dowel from pad
					if (player.getMainHandItem().isEmpty())
						player.setItemInHand(InteractionHand.MAIN_HAND, dowel);
					else if (!player.getInventory().add(dowel))
						dropItem(side);
					else player.inventoryMenu.broadcastChanges();
					
					setDowel(ItemStack.EMPTY);
				} else
				{
					ItemStack heldStack = player.getMainHandItem();
					if (!heldStack.isEmpty() && heldStack.getItem() == MSBlocks.CRUXITE_DOWEL.get().asItem())
						setDowel(heldStack.split(1));    //Put a dowel on the pad
				}
			}
		}
	}
	
	public void processContents(int quantity, ServerPlayer player)
	{
		if(this.isBroken())
			return;
		
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
		
		if(GristCache.get(player).tryTake(cost, GristHelper.EnumSource.CLIENT))
		{
			AlchemyEvent event = new AlchemyEvent(IdentifierHandler.encode(player), this, getDowel(), newItem, cost);
			NeoForge.EVENT_BUS.post(event);
			newItem = event.getItemResult();
			
			while(quantity > 0)
			{
				ServerLevel blockLevel = (ServerLevel) this.level;
				
				ItemStack stack = newItem.copy();
				stack.setCount(Math.min(stack.getMaxStackSize(), quantity));
				quantity -= stack.getCount();
				ItemEntity item = new ItemEntity(level, spawnPos.getX(), spawnPos.getY() + 0.5, spawnPos.getZ(), stack);
				level.addFreshEntity(item);
				
				if(blockLevel != null)
					blockLevel.sendParticles(MSParticleType.PLASMA.get(), spawnPos.getX(), spawnPos.getY() + 0.5, spawnPos.getZ(), 1, 0, 0, 0, 0);
				level.playSound(null, this.getBlockPos(), MSSoundEvents.ALCHEMITER_RESONATE.get(), SoundSource.BLOCKS, 1F, 1F);
			}
		}
	}
	
	@Nullable
	public GristSet getGristCost(int quantity)
	{
		ItemStack dowel = getDowel();
		ItemStack stack = getOutput();
		if(dowel.isEmpty() || level == null)
			return null;
		
		stack.setCount(quantity);
		//get the grist cost of stack
		return GristCostRecipe.findCostForItem(stack, getWildcardGrist(), false, level);
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
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, "scanAnimation", 0, this::scanAnimation));
	}
	
	private <E extends BlockEntity & GeoAnimatable> PlayState scanAnimation(AnimationState<E> state)
	{
		if(!this.dowel.isEmpty())
		{
			state.getController().setAnimation(SCAN_ANIMATION);
			return PlayState.CONTINUE;
		}
		state.getController().forceAnimationReset();
		return PlayState.STOP;
	}
}
