package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.redstone.AreaEffectBlock;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class AreaEffectTileEntity extends TileEntity implements ITickableTileEntity
{
	//TODO deserialize/reserialize function + triggering of all summoners
	
	private Effect effect;
	private int effectAmplifier;
	private BlockPos minAreaOffset;
	private BlockPos maxAreaOffset;
	
	public AreaEffectTileEntity()
	{
		super(MSTileEntityTypes.AREA_EFFECT.get());
	}
	
	@Override
	public void tick()
	{
		if(level == null || !level.isAreaLoaded(getBlockPos(), 0))
			return;
		
		if(level.getGameTime() % 80 == 0 && !level.isClientSide && level.getBlockState(getBlockPos()).getBlock() instanceof AreaEffectBlock && getBlockState().getValue(AreaEffectBlock.POWERED))
		{
			giveEntitiesEffect();
		}
	}
	
	public void giveEntitiesEffect()
	{
		BlockPos tePos = getBlockPos();
		Direction teFacing = getBlockState().getValue(AreaEffectBlock.FACING);
		
		BlockPos minAreaPos = tePos.relative(teFacing, minAreaOffset.getX()).relative(Direction.UP, minAreaOffset.getY()).relative(teFacing.getClockWise(), minAreaOffset.getZ());
		BlockPos maxAreaPos = tePos.relative(teFacing, maxAreaOffset.getX()).relative(Direction.UP, maxAreaOffset.getY()).relative(teFacing.getClockWise(), maxAreaOffset.getZ());
		
		//TODO with the current area effect block being tested, only north and east work right now and with the axis align functions only the east directional effect works
		//minAreaPos = axisAlignBlockPosGetMin(minAreaPos, maxAreaPos);
		//maxAreaPos = axisAlignBlockPosGetMax(minAreaPos, maxAreaPos);
		
		if(getBlockState().getValue(AreaEffectBlock.ALL_MOBS))
		{
			for(LivingEntity livingEntity : level.getLoadedEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(minAreaPos, maxAreaPos)))
			{
				if(effect instanceof CreativeShockEffect) //skips later creative/harmful specific checks as the effect should always be given
				{
					livingEntity.addEffect(new EffectInstance(effect, 120, effectAmplifier, false, false));
				} else
				{
					if(livingEntity instanceof PlayerEntity)
					{
						if(((PlayerEntity) livingEntity).isCreative() && !effect.isBeneficial())
							break;
					}
					
					if(effect.isInstantenous())
						livingEntity.addEffect(new EffectInstance(effect, 1, effectAmplifier, false, false));
					else
						livingEntity.addEffect(new EffectInstance(effect, 120, effectAmplifier, false, false));
				}
			}
		} else
		{
			for(PlayerEntity playerEntity : level.getLoadedEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(minAreaPos, maxAreaPos)))
			{
				if(effect instanceof CreativeShockEffect) //skips later creative/harmful specific checks as the effect should always be given
				{
					playerEntity.addEffect(new EffectInstance(effect, 120, effectAmplifier, false, false));
				} else
				{
					if(playerEntity.isCreative() && !effect.isBeneficial())
						break;
					
					if(effect.isInstantenous())
						playerEntity.addEffect(new EffectInstance(effect, 1, effectAmplifier, false, false));
					else
						playerEntity.addEffect(new EffectInstance(effect, 120, effectAmplifier, false, false));
				}
			}
		}
	}
	
	/**
	 * Reorders the blockpos pair to get the overall min coords
	 */
	public static BlockPos axisAlignBlockPosGetMin(BlockPos minBlockPosIn, BlockPos maxBlockPosIn)
	{
		int blockPosMinX = Math.min(minBlockPosIn.getX(), maxBlockPosIn.getX());
		int blockPosMinY = Math.min(minBlockPosIn.getY(), maxBlockPosIn.getY());
		int blockPosMinZ = Math.min(minBlockPosIn.getZ(), maxBlockPosIn.getZ());
		
		return new BlockPos(blockPosMinX, blockPosMinY, blockPosMinZ);
	}
	
	/**
	 * Reorders the blockpos pair to get the overall max coords
	 */
	public static BlockPos axisAlignBlockPosGetMax(BlockPos minBlockPosIn, BlockPos maxBlockPosIn)
	{
		int blockPosMaxX = Math.max(minBlockPosIn.getX(), maxBlockPosIn.getX());
		int blockPosMaxY = Math.max(minBlockPosIn.getY(), maxBlockPosIn.getY());
		int blockPosMaxZ = Math.max(minBlockPosIn.getZ(), maxBlockPosIn.getZ());
		
		return new BlockPos(blockPosMaxX, blockPosMaxY, blockPosMaxZ);
	}
	
	public void setEffect(Effect effectIn, int effectAmplifierIn)
	{
		this.effect = effectIn;
		this.effectAmplifier = effectAmplifierIn;
	}
	
	public Effect getEffect()
	{
		if(effect == null)
			effect = MSEffects.CREATIVE_SHOCK.get();
		return this.effect;
	}
	
	public int getEffectAmplifier()
	{
		return this.effectAmplifier;
	}
	
	public void setMinAndMaxEffectPosOffset(BlockPos minAreaOffsetIn, BlockPos maxAreaOffsetIn)
	{
		this.minAreaOffset = minAreaOffsetIn;
		this.maxAreaOffset = maxAreaOffsetIn;
		this.setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 0);
	}
	
	public BlockPos getMinAreaOffset()
	{
		if(minAreaOffset == null)
		{
			minAreaOffset = new BlockPos(-16, -16, -16);
		}
		
		minAreaOffset = parseMinBlockPos(this, minAreaOffset.getX(), minAreaOffset.getY(), minAreaOffset.getZ());
		
		return this.minAreaOffset;
	}
	
	public BlockPos getMaxAreaOffset()
	{
		if(maxAreaOffset == null)
		{
			maxAreaOffset = new BlockPos(16, 16, 16);
		}
		
		maxAreaOffset = parseMaxBlockPos(this, maxAreaOffset.getX(), maxAreaOffset.getY(), maxAreaOffset.getZ());
		
		return this.maxAreaOffset;
	}
	
	/**
	 * Checks to make sure that the minimum effect pos is within legal bounds, defaults to the intended boundary if it is too far away from the block
	 */
	public static BlockPos parseMinBlockPos(AreaEffectTileEntity te, int x, int y, int z)
	{
		BlockPos tePos = te.getBlockPos();
		
		x = Math.max(x, -64);
		y = Math.max(y, -64);
		z = Math.max(z, -64);
		
		y = Math.max(y, -tePos.getY()); //cannot be offset to lower than y = min build height
		y = Math.min(y, te.getLevel().getMaxBuildHeight() - tePos.getY()); //cannot be offset to higher than y = max build height
		
		return new BlockPos(x, y, z);
	}
	
	/**
	 * Checks to make sure that the maximum effect pos is within legal bounds, defaults to the intended boundary if it is too far away from the block
	 */
	public static BlockPos parseMaxBlockPos(AreaEffectTileEntity te, int x, int y, int z)
	{
		BlockPos tePos = te.getBlockPos();
		
		x = Math.min(x, 64);
		y = Math.min(y, 64);
		z = Math.min(z, 64);
		
		y = Math.max(y, -tePos.getY()); //TODO change -tePos.getY() to new bottom height when switching to 1.18
		y = Math.min(y, te.getLevel().getMaxBuildHeight() - tePos.getY());
		
		return new BlockPos(x, y, z);
	}
	
	@Override
	public void onLoad()
	{
		super.onLoad();
		getMinAreaOffset(); //used only to update the boundaries in case they go too far
		getMaxAreaOffset();
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		
		Effect effectRead = Effect.byId(compound.getInt("effect"));
		if(effectRead != null)
		{
			effect = effectRead;
		}
		
		effectAmplifier = compound.getInt("effectAmplifier");
		
		int minAreaOffsetX = compound.getInt("minAreaOffsetX");
		int minAreaOffsetY = compound.getInt("minAreaOffsetY");
		int minAreaOffsetZ = compound.getInt("minAreaOffsetZ");
		this.minAreaOffset = new BlockPos(minAreaOffsetX, minAreaOffsetY, minAreaOffsetZ);
		
		int maxAreaOffsetX = compound.getInt("maxAreaOffsetX");
		int maxAreaOffsetY = compound.getInt("maxAreaOffsetY");
		int maxAreaOffsetZ = compound.getInt("maxAreaOffsetZ");
		this.maxAreaOffset = new BlockPos(maxAreaOffsetX, maxAreaOffsetY, maxAreaOffsetZ);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		compound.putInt("effect", Effect.getId(getEffect()));
		compound.putInt("effectAmplifier", effectAmplifier);
		
		getMinAreaOffset();
		getMaxAreaOffset();
		
		compound.putInt("minAreaOffsetX", minAreaOffset.getX());
		compound.putInt("minAreaOffsetY", minAreaOffset.getY());
		compound.putInt("minAreaOffsetZ", minAreaOffset.getZ());
		
		compound.putInt("maxAreaOffsetX", maxAreaOffset.getX());
		compound.putInt("maxAreaOffsetY", maxAreaOffset.getY());
		compound.putInt("maxAreaOffsetZ", maxAreaOffset.getZ());
		
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.save(new CompoundNBT());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(getBlockPos(), 2, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.load(getBlockState(), pkt.getTag());
	}
}