package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.tileentity.redstone.AreaEffectTileEntity;
import com.mraof.minestuck.util.ParticlesAroundSolidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * When powered, the tile entity applies an effect to entities in a designated area between two block pos, similar to beacons but with more versatility.
 * Only creative mode players(who are not under the effects of Creative Shock) can change the effect
 */
public class AreaEffectBlock extends Block
{
	public static final String EFFECT_CHANGE_MESSAGE = "effect_change_message";
	
	public AreaEffectBlock(Properties properties)
	{
		super(properties);
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
		if(player.isCreative() && !CreativeShockEffect.doesCreativeShockLimit(player, 1))
		{
			TileEntity tileEntity = worldIn.getBlockEntity(pos);
			if(tileEntity instanceof AreaEffectTileEntity)
			{
				AreaEffectTileEntity te = (AreaEffectTileEntity) tileEntity;
				
				ItemStack heldItemStack = player.getItemInHand(hand);
				
				if(heldItemStack.getItem() instanceof PotionItem)
				{
					EffectInstance firstEffect = PotionUtils.getPotion(heldItemStack).getEffects().get(0);
					if(firstEffect != null)
					{
						te.setEffect(firstEffect.getEffect(), firstEffect.getAmplifier());
						
						player.displayClientMessage(new TranslationTextComponent(getDescriptionId() + "." + EFFECT_CHANGE_MESSAGE, firstEffect.getEffect().getRegistryName(), firstEffect.getAmplifier()), true); //getDescriptionId was getTranslationKey
						worldIn.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, 1F);
					}
				} else
				{
					MSScreenFactories.displayAreaEffectScreen(te);
				}
				
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.PASS;
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(worldIn.hasNeighborSignal(pos))
			ParticlesAroundSolidBlock.spawnParticles(worldIn, pos, () -> RedstoneParticleData.REDSTONE);
	}
}