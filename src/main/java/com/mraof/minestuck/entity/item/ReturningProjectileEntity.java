package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.client.renderer.entity.RendersAsItem;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ReturningProjectileEntity extends ConsumableProjectileEntity implements RendersAsItem
{
	private int bounce;
	public int maxTick = 0;
	private int inBlockTicks = 0;
	private BlockRayTraceResult blockResult;
	private Direction blockFace;
	private BlockPos blockPos;
	private boolean umbralVoid;
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, double x, double y, double z, World worldIn)
	{
		super(type, x, y, z, worldIn);
	}
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, LivingEntity livingEntityIn, World worldIn, int damage, int maxTick)
	{
		super(type, livingEntityIn, worldIn, damage);
		this.maxTick = maxTick;
	}
	
	@Override
	protected void onImpact(RayTraceResult result) //Need to find a way to improve rate of checking
	{
		if(this.getThrower() instanceof PlayerEntity && !world.isRemote)
		{
			PlayerEntity throwerPlayer = (PlayerEntity) this.getThrower();
			
			Title title = PlayerSavedData.getData((ServerPlayerEntity) this.getThrower()).getTitle();
			if(title != null)
			{
				umbralVoid = title.getHeroAspect() == EnumAspect.VOID && func_213882_k().getItem() == MSItems.UMBRAL_INFILTRATOR;
			}
			
			if(result.getType() == RayTraceResult.Type.ENTITY)
			{
				++bounce;
				this.setMotion(getMotion().scale(-1.05));
				Entity entity = ((EntityRayTraceResult) result).getEntity();
				if(entity instanceof UnderlingEntity)
					entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage * 2);
				else if(entity != throwerPlayer)
					entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
				else
				{
					resetThrower();
				}
			} else if(result.getType() == RayTraceResult.Type.BLOCK && !umbralVoid)
			{
				blockResult = (BlockRayTraceResult) result;
				blockFace = blockResult.getFace();
				blockPos = blockResult.getPos();
				if(Block.hasEnoughSolidSide(world, blockPos, blockFace))
				{
					++bounce;
					this.setMotion(getMotion().scale(-1.05));
					this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 0.6F, 4.0F);
				}
				
				if(Block.hasEnoughSolidSide(world, blockPos, blockFace) && blockResult.isInside())
				{
					++inBlockTicks;
				}
			}
			
			if(bounce > 15)
			{
				resetThrower();
			}
		}
	}
	
	public void resetThrower()
	{
		PlayerEntity throwerPlayer = (PlayerEntity) this.getThrower();
		if(throwerPlayer != null)
		{
			throwerPlayer.getCooldownTracker().setCooldown(func_213882_k().getItem(), 5);
			this.remove();
		}
	}
	
	public void tick()
	{
		Vec3d pos = getPositionVec();
		this.lastTickPosX = pos.x;
		this.lastTickPosY = pos.y;
		this.lastTickPosZ = pos.z;
		
		if(this.isInWater())
			this.setMotion(getMotion().scale(1.2));
		else
			this.setMotion(getMotion().scale(1.005));
		
		if(this.ticksExisted >= maxTick || inBlockTicks >= 2)
		{
			resetThrower();
		}
		
		super.tick();
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	protected Item getDefaultItem()
	{
		return MSItems.CHAKRAM;
	}
}
