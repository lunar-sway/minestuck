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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class AreaEffectTileEntity extends TileEntity implements ITickableTileEntity
{
	//TODO deserialize/reserialize function + triggering of all summoners
	
	private Effect effect;
	private int effectAmplifier;
	private BlockPos minEffectPos;
	private BlockPos maxEffectPos;
	
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
		//TODO improve flow/reduce repetition of code by extracting repetitive components into another function
		if(getBlockState().getValue(AreaEffectBlock.ALL_MOBS))
		{
			for(LivingEntity livingEntity : level.getLoadedEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(minEffectPos, maxEffectPos)))
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
			for(PlayerEntity playerEntity : level.getLoadedEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(minEffectPos, maxEffectPos)))
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
	
	public void setMinAndMaxEffectPos(BlockPos minEffectPosIn, BlockPos maxEffectPosIn)
	{
		this.minEffectPos = minEffectPosIn;
		this.maxEffectPos = maxEffectPosIn;
	}
	
	public BlockPos getMinEffectPos()
	{
		if(minEffectPos == null)
		{
			minEffectPos = new BlockPos(this.getBlockPos().offset(-16, -16, -16));
		}
		return this.minEffectPos;
	}
	
	public BlockPos getMaxEffectPos()
	{
		if(maxEffectPos == null)
		{
			maxEffectPos = new BlockPos(this.getBlockPos().offset(16, 16, 16));
		}
		return this.maxEffectPos;
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
		
		int minEffectPosX = compound.getInt("minEffectPosX");
		int minEffectPosY = compound.getInt("minEffectPosY");
		int minEffectPosZ = compound.getInt("minEffectPosZ");
		this.minEffectPos = new BlockPos(minEffectPosX, minEffectPosY, minEffectPosZ);
		
		int maxEffectPosX = compound.getInt("maxEffectPosX");
		int maxEffectPosY = compound.getInt("maxEffectPosY");
		int maxEffectPosZ = compound.getInt("maxEffectPosZ");
		this.maxEffectPos = new BlockPos(maxEffectPosX, maxEffectPosY, maxEffectPosZ);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		compound.putInt("effect", Effect.getId(getEffect()));
		compound.putInt("effectAmplifier", effectAmplifier);
		
		getMinEffectPos();
		getMaxEffectPos();
		
		compound.putInt("minEffectPosX", minEffectPos.getX());
		compound.putInt("minEffectPosY", minEffectPos.getY());
		compound.putInt("minEffectPosZ", minEffectPos.getZ());
		
		compound.putInt("maxEffectPosX", maxEffectPos.getX());
		compound.putInt("maxEffectPosY", maxEffectPos.getY());
		compound.putInt("maxEffectPosZ", maxEffectPos.getZ());
		
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