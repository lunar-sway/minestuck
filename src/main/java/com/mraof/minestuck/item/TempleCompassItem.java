package com.mraof.minestuck.item;

import com.mraof.minestuck.util.Debug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;


public class TempleCompassItem extends Item
{
	private ServerWorld serverWorld;
	
	public TempleCompassItem(Item.Properties builder)
	{
		super(builder);
		this.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter()
		{
			@OnlyIn(Dist.CLIENT)
			private double rotation;
			@OnlyIn(Dist.CLIENT)
			private double rota;
			@OnlyIn(Dist.CLIENT)
			private long lastUpdateTick;
			
			//@OnlyIn(Dist.CLIENT)
			public float call(ItemStack p_call_1_, @Nullable World p_call_2_, @Nullable LivingEntity p_call_3_)
			{
				if(p_call_3_ == null && !p_call_1_.isOnItemFrame())
				{
					return 0.0F;
				} else
				{
					
					boolean flag = p_call_3_ != null;
					Entity entity = (Entity) (flag ? p_call_3_ : p_call_1_.getItemFrame());
					if(p_call_2_ == null)
					{
						p_call_2_ = entity.world;
					} else if (p_call_2_.isRemote){
						serverWorld = (ServerWorld) p_call_2_;
					}
					
					//if(entity instanceof ClientPlayerEntity)
					//Debug.debugf("entity = %s", entity);
					//Debug.debugf("p_call_2 = %s and serverWorld = %s", p_call_2_, serverWorld);
					/*if(entity instanceof ServerPlayerEntity){
						ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
						serverWorld = (ServerWorld) serverPlayerEntity.world;
					}*/
					
					
					double d0;
					
					if(p_call_2_.dimension.isSurfaceWorld())
					{
						double d1 = flag ? (double) entity.rotationYaw : this.getFrameRotation((ItemFrameEntity) entity);
						d1 = MathHelper.positiveModulo(d1 / 360.0D, 1.0D);
						
						
						if (serverWorld != null){
							double d2 = this.getStructureToAngle(serverWorld, entity) / (double)((float)Math.PI * 2F);
							//double d2 = this.getStructureToAngle(p_call_2_, entity) / (double) ((float) Math.PI * 2F);
							d0 = 0.5D - (d1 - 0.25D - d2);
						} else {
							d0 = Math.random();
						}
						
					} else
					{
						d0 = Math.random();
					}
					
					if(flag)
					{
						d0 = this.wobble(p_call_2_, d0);
					}
					
					return MathHelper.positiveModulo((float) d0, 1.0F);
				}
			}
			
			@OnlyIn(Dist.CLIENT)
			private double wobble(World worldIn, double amount)
			{
				if(worldIn.getGameTime() != this.lastUpdateTick)
				{
					this.lastUpdateTick = worldIn.getGameTime();
					double d0 = amount - this.rotation;
					d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
					this.rota += d0 * 0.1D;
					this.rota *= 0.8D;
					this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
				}
				
				return this.rotation;
			}
			
			@OnlyIn(Dist.CLIENT)
			private double getFrameRotation(ItemFrameEntity frameEntity)
			{
				return (double) MathHelper.wrapDegrees(180 + frameEntity.getHorizontalFacing().getHorizontalIndex() * 90);
			}
			
			//@OnlyIn(Dist.CLIENT)
			private double getStructureToAngle(IWorld worldIn, Entity entityIn)
			{
				Debug.debugf("getStructureToAngle called, worldIn = %s", worldIn);
				if (worldIn instanceof ServerWorld)
				{
					ServerWorld serverWorld = (ServerWorld) entityIn.world;
					BlockPos blockpos = serverWorld.getChunkProvider().getChunkGenerator().findNearestStructure(serverWorld, "Stronghold", new BlockPos(entityIn), 100, false);
					return Math.atan2((double) blockpos.getZ() - entityIn.getPosZ(), (double) blockpos.getX() - entityIn.getPosX());
				}
				else
				{
					BlockPos blockpos = worldIn.getSpawnPoint();
					return Math.atan2((double) blockpos.getZ() - entityIn.getPosZ(), (double) blockpos.getX() - entityIn.getPosX());
				}
				//BlockPos blockpos = worldIn.getSpawnPoint();
				//BlockPos blockpos = ((ServerWorld)worldIn).getChunkProvider().getChunkGenerator().findNearestStructure((ServerWorld) worldIn, "Stronghold", new BlockPos(entityIn), 100, false);
				
			}
		});
	}
}
