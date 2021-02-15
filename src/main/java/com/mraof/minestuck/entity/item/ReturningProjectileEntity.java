package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.client.renderer.entity.RendersAsItem;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
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
	private Direction entityDirection;
	
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
	protected void onImpact(RayTraceResult result)
	{
		PlayerEntity throwerPlayer = (PlayerEntity) this.getThrower();
		if(throwerPlayer != null && !world.isRemote)
		{
			int cooldownTicks = throwerPlayer.getCooldownTracker().hashCode();
			
			if(result.getType() == RayTraceResult.Type.ENTITY)
			{
				++bounce;
				this.setMotion(getMotion().scale(-1.05));
				Entity entity = ((EntityRayTraceResult) result).getEntity();
				if(entity != throwerPlayer)
					entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
				if(entity == throwerPlayer)
				{
					resetThrower();
				}
			} else if(result.getType() == RayTraceResult.Type.BLOCK && func_213882_k().getItem() != MSItems.UMBRAL_INFILTRATOR)
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
			
			if(bounce > 15 || cooldownTicks <= 5)
			{
				resetThrower();
			}
		}
	}
	/*
	private void setDirection(@Nullable Direction directionIn) {
		this.entityDirection = directionIn;
	}
	
	private void selectNextMoveDirection(@Nullable Direction.Axis p_184569_1_) {
		double d0 = 0.5D;
		BlockPos blockpos;
		if (this.owner == null) {
			blockpos = (new BlockPos(this)).down();
		} else {
			d0 = (double)this.owner.getHeight() * 0.5D;
			blockpos = new BlockPos(this.owner.getPosX(), this.owner.getPosY() + d0, this.owner.getPosZ());
		}
		
		double d1 = (double)blockpos.getX() + 0.5D;
		double d2 = (double)blockpos.getY() + d0;
		double d3 = (double)blockpos.getZ() + 0.5D;
		Direction direction = null;
		if (!blockpos.withinDistance(this.getPositionVec(), 2.0D)) {
			BlockPos blockpos1 = new BlockPos(this);
			List<Direction> list = Lists.newArrayList();
			if (p_184569_1_ != Direction.Axis.X) {
				if (blockpos1.getX() < blockpos.getX() && this.world.isAirBlock(blockpos1.east())) {
					list.add(Direction.EAST);
				} else if (blockpos1.getX() > blockpos.getX() && this.world.isAirBlock(blockpos1.west())) {
					list.add(Direction.WEST);
				}
			}
			
			if (p_184569_1_ != Direction.Axis.Y) {
				if (blockpos1.getY() < blockpos.getY() && this.world.isAirBlock(blockpos1.up())) {
					list.add(Direction.UP);
				} else if (blockpos1.getY() > blockpos.getY() && this.world.isAirBlock(blockpos1.down())) {
					list.add(Direction.DOWN);
				}
			}
			
			if (p_184569_1_ != Direction.Axis.Z) {
				if (blockpos1.getZ() < blockpos.getZ() && this.world.isAirBlock(blockpos1.south())) {
					list.add(Direction.SOUTH);
				} else if (blockpos1.getZ() > blockpos.getZ() && this.world.isAirBlock(blockpos1.north())) {
					list.add(Direction.NORTH);
				}
			}
			
			direction = Direction.random(this.rand);
			if (list.isEmpty()) {
				for(int i = 5; !this.world.isAirBlock(blockpos1.offset(direction)) && i > 0; --i) {
					direction = Direction.random(this.rand);
				}
			} else {
				direction = list.get(this.rand.nextInt(list.size()));
			}
			
			d1 = this.getPosX() + (double)direction.getXOffset();
			d2 = this.getPosY() + (double)direction.getYOffset();
			d3 = this.getPosZ() + (double)direction.getZOffset();
		}
		
		this.setDirection(direction);
		double d6 = d1 - this.getPosX();
		double d7 = d2 - this.getPosY();
		double d4 = d3 - this.getPosZ();
		double d5 = (double)MathHelper.sqrt(d6 * d6 + d7 * d7 + d4 * d4);
		if (d5 == 0.0D) {
			this.targetDeltaX = 0.0D;
			this.targetDeltaY = 0.0D;
			this.targetDeltaZ = 0.0D;
		} else {
			this.targetDeltaX = d6 / d5 * 0.15D;
			this.targetDeltaY = d7 / d5 * 0.15D;
			this.targetDeltaZ = d4 / d5 * 0.15D;
		}
		
		this.isAirBorne = true;
		//this.steps = 10 + this.rand.nextInt(5) * 10;
	}
	*/
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
		
		if(this.ticksExisted >= maxTick || inBlockTicks >= 1)
		{
			resetThrower();
		}
		
		ProjectileHelper.rotateTowardsMovement(this, 0.2F);
		
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
