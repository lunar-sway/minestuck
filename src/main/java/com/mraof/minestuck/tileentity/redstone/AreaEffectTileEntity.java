package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.redstone.AreaEffectBlock;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class AreaEffectTileEntity extends BlockEntity
{
	//TODO deserialize/reserialize function + triggering of all summoners
	
	private MobEffect effect;
	private int effectAmplifier;
	private BlockPos minAreaOffset;
	private BlockPos maxAreaOffset;
	private boolean needsTranslation = false;
	
	public AreaEffectTileEntity(BlockPos pos, BlockState state)
	{
		super(MSTileEntityTypes.AREA_EFFECT.get(), pos, state);
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, AreaEffectTileEntity blockEntity)
	{
		
		if(!level.isAreaLoaded(pos, 0))
			return;
		
		if(level.getGameTime() % 80 == 0 && !level.isClientSide && state.getValue(AreaEffectBlock.POWERED) && !state.getValue(AreaEffectBlock.SHUT_DOWN))
		{
			blockEntity.giveEntitiesEffect();
		}
	}
	
	public void giveEntitiesEffect()
	{
		BlockPos tePos = getBlockPos();
		Direction teFacing = getBlockState().getValue(AreaEffectBlock.FACING);
		if(needsTranslation)
			translateAbsoluteOffsetToDirectional(teFacing);
		
		BlockPos minAreaPos = tePos.relative(teFacing, minAreaOffset.getX()).relative(Direction.UP, minAreaOffset.getY()).relative(teFacing.getClockWise(), minAreaOffset.getZ());
		BlockPos maxAreaPos = tePos.relative(teFacing, maxAreaOffset.getX()).relative(Direction.UP, maxAreaOffset.getY()).relative(teFacing.getClockWise(), maxAreaOffset.getZ());
		
		if(getBlockState().getValue(AreaEffectBlock.ALL_MOBS))
		{
			for(LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, new AABB(minAreaPos, maxAreaPos)))
			{
				iterateThroughEntities(livingEntity);
			}
		} else
		{
			for(Player playerEntity : level.getEntitiesOfClass(Player.class, new AABB(minAreaPos, maxAreaPos)))
			{
				iterateThroughEntities(playerEntity);
			}
		}
	}
	
	private void iterateThroughEntities(LivingEntity entityIterate)
	{
		if(effect instanceof CreativeShockEffect) //skips later creative/harmful specific checks as the effect should always be given
		{
			entityIterate.addEffect(new MobEffectInstance(effect, 120, effectAmplifier, false, false));
		} else
		{
			boolean ignoreEntity = entityIterate instanceof Player player && player.isCreative() && !effect.isBeneficial(); //if not a player, or it is a player but they are in creative and the effect is not beneficial, ignore
			
			if(!ignoreEntity)
				entityIterate.addEffect(new MobEffectInstance(effect, effect.isInstantenous() ? 1 : 120, effectAmplifier, false, false));
		}
	}
	
	public void setEffect(MobEffect effectIn, int effectAmplifierIn)
	{
		this.effect = effectIn;
		this.effectAmplifier = effectAmplifierIn;
	}
	
	public MobEffect getEffect()
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
		
		y = Mth.clamp(y, -tePos.getY(), te.getLevel().getMaxBuildHeight() - tePos.getY());
		
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
		
		y = Mth.clamp(y, -tePos.getY(), te.getLevel().getMaxBuildHeight() - tePos.getY());
		
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
	public void load(CompoundTag compound)
	{
		super.load(compound);
		
		MobEffect effectRead = MobEffect.byId(compound.getInt("effect"));
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
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		compound.putInt("effect", MobEffect.getId(getEffect()));
		compound.putInt("effectAmplifier", effectAmplifier);
		
		getMinAreaOffset();
		getMaxAreaOffset();
		
		compound.putInt("minAreaOffsetX", minAreaOffset.getX());
		compound.putInt("minAreaOffsetY", minAreaOffset.getY());
		compound.putInt("minAreaOffsetZ", minAreaOffset.getZ());
		
		compound.putInt("maxAreaOffsetX", maxAreaOffset.getX());
		compound.putInt("maxAreaOffsetY", maxAreaOffset.getY());
		compound.putInt("maxAreaOffsetZ", maxAreaOffset.getZ());
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		return this.saveWithoutMetadata();
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
}