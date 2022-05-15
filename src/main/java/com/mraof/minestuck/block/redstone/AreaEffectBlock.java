package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.tileentity.redstone.AreaEffectTileEntity;
import com.mraof.minestuck.util.ParticlesAroundSolidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * When powered, the tile entity applies an effect to entities in a designated area between two block pos, similar to beacons but with more versatility.
 * Only creative mode players(who are not under the effects of Creative Shock) can change the effect
 */
public class AreaEffectBlock extends HorizontalBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty ALL_MOBS = BlockStateProperties.ENABLED; //checks whether just players should be given the effect or if all living entities should be given the effect
	public static final String EFFECT_CHANGE_MESSAGE = "effect_change_message";
	
	public AreaEffectBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(ALL_MOBS, false));
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new AreaEffectTileEntity();
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(player.isCreative() && !CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS))
		{
			TileEntity tileEntity = worldIn.getBlockEntity(pos);
			if(tileEntity instanceof AreaEffectTileEntity)
			{
				AreaEffectTileEntity te = (AreaEffectTileEntity) tileEntity;
				
				ItemStack heldItemStack = player.getItemInHand(hand);
				
				if(heldItemStack.getItem() instanceof PotionItem)
				{
					clickWithPotion(worldIn, pos, player, te, heldItemStack);
				} else if(heldItemStack.isEmpty() && player.isCrouching())
				{
					cycleAllMobsProperty(state, worldIn, pos);
				} else if(!player.isCrouching() && worldIn.isClientSide)
				{
					MSScreenFactories.displayAreaEffectScreen(te);
				}
				
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.PASS;
	}
	
	private void clickWithPotion(World worldIn, BlockPos pos, PlayerEntity player, AreaEffectTileEntity te, ItemStack potionStack)
	{
		EffectInstance firstEffect = PotionUtils.getPotion(potionStack).getEffects().get(0);
		if(firstEffect != null && !worldIn.isClientSide)
		{
			te.setEffect(firstEffect.getEffect(), firstEffect.getAmplifier());
			
			player.displayClientMessage(new TranslationTextComponent(getDescriptionId() + "." + EFFECT_CHANGE_MESSAGE, firstEffect.getEffect().getRegistryName(), firstEffect.getAmplifier()), true); //getDescriptionId was getTranslationKey
			worldIn.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, 1F);
		}
	}
	
	private void cycleAllMobsProperty(BlockState state, World worldIn, BlockPos pos)
	{
		if(!worldIn.isClientSide)
		{
			worldIn.setBlock(pos, state.cycle(ALL_MOBS), Constants.BlockFlags.DEFAULT);
			if(state.getValue(ALL_MOBS))
				worldIn.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.2F);
			else
				worldIn.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.2F);
		}
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		updatePower(worldIn, pos);
	}
	
	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(state, worldIn, pos, oldState, isMoving);
		updatePower(worldIn, pos);
	}
	
	public void updatePower(World worldIn, BlockPos pos)
	{
		if(!worldIn.isClientSide)
		{
			BlockState state = worldIn.getBlockState(pos);
			boolean hasPower = worldIn.hasNeighborSignal(pos);
			
			if(state.getValue(POWERED) != hasPower)
				worldIn.setBlockAndUpdate(pos, state.setValue(POWERED, hasPower));
			else worldIn.sendBlockUpdated(pos, state, state, 2);
		}
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(stateIn.getValue(POWERED))
			ParticlesAroundSolidBlock.spawnParticles(worldIn, pos, () -> RedstoneParticleData.REDSTONE);
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(POWERED);
		builder.add(ALL_MOBS);
	}
}