package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.redstone.AreaEffectBlock;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
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
	private Effect effect;
	private int effectAmplifier;
	private BlockPos minEffectPos;
	private BlockPos maxEffectPos;
	
	public AreaEffectTileEntity()
	{
		super(MSTileEntityTypes.AREA_EFFECT.get());
	}
	
	public void giveEntitiesEffect()
	{
		for(LivingEntity livingEntity : world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(minEffectPos, maxEffectPos)))
		{
			if(livingEntity instanceof PlayerEntity)
			{
				if(((PlayerEntity) livingEntity).isCreative())
					break;
			}
			
			livingEntity.addPotionEffect(new EffectInstance(effect, 120, effectAmplifier, false, false));
		}
	}
	
	@Override
	public void tick()
	{
		if(world == null || !world.isAreaLoaded(pos, 0))
			return; // Forge: prevent loading unloaded chunks
		
		if(world.getGameTime() % 60 == 0 && world.getBlockState(pos).getBlock() instanceof AreaEffectBlock && world.isBlockPowered(pos))
		{
			giveEntitiesEffect();
		}
	}
	
	public void setEffect(Effect effectIn)
	{
		this.effect = effectIn;
	}
	
	public Effect getEffect()
	{
		if(effect == null)
			effect = MSEffects.CREATIVE_SHOCK.get();
		return this.effect;
	}
	
	public void setEffectAmplifier(int effectAmplifierIn)
	{
		this.effectAmplifier = effectAmplifierIn;
	}
	
	public int getEffectAmplifier()
	{
		return this.effectAmplifier;
	}
	
	public void setMinEffectPos(BlockPos minEffectPosIn)
	{
		this.minEffectPos = minEffectPosIn;
	}
	
	public BlockPos getMinEffectPos()
	{
		if(minEffectPos == null)
		{
			minEffectPos = new BlockPos(this.pos.add(-16, -16, -16));
		}
		return this.minEffectPos;
	}
	
	public void setMaxEffectPos(BlockPos maxEffectPosIn)
	{
		this.maxEffectPos = maxEffectPosIn;
	}
	
	public BlockPos getMaxEffectPos()
	{
		if(maxEffectPos == null)
		{
			maxEffectPos = new BlockPos(this.pos.add(16, 16, 16));
		}
		return this.maxEffectPos;
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		
		Effect effectRead = Effect.get(compound.getInt("effect"));
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
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		
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
		return this.write(new CompoundNBT());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 2, this.write(new CompoundNBT()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.read(pkt.getNbtCompound());
	}
	
}