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
import net.minecraft.util.math.MathHelper;

public class AreaEffectTileEntity extends TileEntity implements ITickableTileEntity
{
	//TODO deserialize/reserialize function + triggering of all summoners
	
	private Effect effect;
	private int effectAmplifier;
	private BlockPos minAreaOffset;
	private BlockPos maxAreaOffset;
	private boolean needsTranslation = false;
	
	public AreaEffectTileEntity()
	{
		super(MSTileEntityTypes.AREA_EFFECT.get());
	}
	
	@Override
	public void tick()
	{
		if(level == null || !level.isAreaLoaded(getBlockPos(), 0))
			return;
		
		if(level.getGameTime() % 80 == 0 && !level.isClientSide && getBlockState().getValue(AreaEffectBlock.POWERED) && !getBlockState().getValue(AreaEffectBlock.SHUT_DOWN))
		{
			giveEntitiesEffect();
		}
	}
	
	public void giveEntitiesEffect()
	{
		//TODO improve flow/reduce repetition of code by extracting repetitive components into another function
		BlockPos tePos = getBlockPos();
		Direction teFacing = getBlockState().getValue(AreaEffectBlock.FACING);
		if(needsTranslation)
			translateAbsoluteOffsetToDirectional(teFacing);
		
		BlockPos minAreaPos = tePos.relative(teFacing, minAreaOffset.getX()).relative(Direction.UP, minAreaOffset.getY()).relative(teFacing.getClockWise(), minAreaOffset.getZ());
		BlockPos maxAreaPos = tePos.relative(teFacing, maxAreaOffset.getX()).relative(Direction.UP, maxAreaOffset.getY()).relative(teFacing.getClockWise(), maxAreaOffset.getZ());
		
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
					
					addEffect(livingEntity);
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
					
					addEffect(playerEntity);
				}
			}
		}
	}
	
	private void addEffect(LivingEntity livingEntity)
	{
		livingEntity.addEffect(new EffectInstance(effect, effect.isInstantenous() ? 1 : 120, effectAmplifier, false, false));
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
	
	/**
	 * sets the area offset pos values from absolute coordinates of the destination block pos, these values will need further modification before use as it does not factor in rotational properties
	 */
	private void setAbsoluteOffsetFromDestinationBlockPos(BlockPos minDestinationPosIn, BlockPos maxDestinationPosIn)
	{
		int minOffsetX = minDestinationPosIn.getX() - worldPosition.getX();
		int minOffsetY = minDestinationPosIn.getY() - worldPosition.getY();
		int minOffsetZ = minDestinationPosIn.getZ() - worldPosition.getZ();
		
		int maxOffsetX = maxDestinationPosIn.getX() - worldPosition.getX();
		int maxOffsetY = maxDestinationPosIn.getY() - worldPosition.getY();
		int maxOffsetZ = maxDestinationPosIn.getZ() - worldPosition.getZ();
		
		this.minAreaOffset = new BlockPos(minOffsetX, minOffsetY, minOffsetZ);
		this.maxAreaOffset = new BlockPos(maxOffsetX, maxOffsetY, maxOffsetZ);
		this.needsTranslation = true;
	}
	
	/**
	 * if the current offset values were set through the backwards compatability function setAbsoluteOffsetFromDestinationBlockPos, this utilizes the blockstate facing direction now that it can be loaded
	 */
	private void translateAbsoluteOffsetToDirectional(Direction stateFacing)
	{
		int minOffsetX = minAreaOffset.getX();
		int minOffsetZ = minAreaOffset.getZ();
		int maxOffsetX = maxAreaOffset.getX();
		int maxOffsetZ = maxAreaOffset.getZ();
		
		int minOffsetModX = minOffsetX;
		int minOffsetModZ = minOffsetZ;
		int maxOffsetModX = maxOffsetX;
		int maxOffsetModZ = maxOffsetZ;
		
		if(stateFacing == Direction.NORTH)
		{
			minOffsetModX = -minOffsetZ;
			maxOffsetModX = -maxOffsetZ;
			minOffsetModZ = minOffsetX;
			maxOffsetModZ = maxOffsetX;
		} else if(stateFacing == Direction.SOUTH) //Direction.EAST is ignored because it is already as desired
		{
			minOffsetModX = minOffsetZ;
			maxOffsetModX = maxOffsetZ;
			minOffsetModZ = -minOffsetX;
			maxOffsetModZ = -maxOffsetX;
		} else if(stateFacing == Direction.WEST)
		{
			minOffsetModX = -minOffsetX;
			maxOffsetModX = -maxOffsetX;
			minOffsetModZ = -minOffsetZ;
			maxOffsetModZ = -maxOffsetZ;
		}
		
		this.minAreaOffset = new BlockPos(minOffsetModX, minAreaOffset.getY(), minOffsetModZ);
		this.maxAreaOffset = new BlockPos(maxOffsetModX, maxAreaOffset.getY(), maxOffsetModZ);
		
		this.needsTranslation = false;
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
		
		y = MathHelper.clamp(y, -tePos.getY(), te.getLevel().getMaxBuildHeight() - tePos.getY());
		
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
		
		y = MathHelper.clamp(y, -tePos.getY(), te.getLevel().getMaxBuildHeight() - tePos.getY());
		
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
		
		if(compound.contains("minEffectPosX") && compound.contains("minEffectPosY") && compound.contains("minEffectPosZ") &&
				compound.contains("maxEffectPosX") && compound.contains("maxEffectPosY") && compound.contains("maxEffectPosZ")) //backwards-portability to the destination based method first utilized
		{
			int minDestX = compound.getInt("minEffectPosX");
			int minDestY = compound.getInt("minEffectPosY");
			int minDestZ = compound.getInt("minEffectPosZ");
			int maxDestX = compound.getInt("maxEffectPosX");
			int maxDestY = compound.getInt("maxEffectPosY");
			int maxDestZ = compound.getInt("maxEffectPosZ");
			setAbsoluteOffsetFromDestinationBlockPos(new BlockPos(minDestX, minDestY, minDestZ), new BlockPos(maxDestX, maxDestY, maxDestZ));
		}
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