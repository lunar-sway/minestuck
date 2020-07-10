package com.mraof.minestuck.entity;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityBullet extends ArrowEntity
{
    // TODO this class is in bad need of a cleanup. Nearly all code in this class looks to have been taken directly from the arrow class, which is likely not needed
    /*
	private static final Predicate<Entity> BULLET_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_.canBeCollidedWith();
        }
    });
     */
	protected static final DataParameter<Byte> CRITICAL = EntityDataManager.<Byte>createKey(ArrowEntity.class, DataSerializers.BYTE);
    protected int xTile;
    protected int yTile;
    protected int zTile;
    protected Block inTile;
    protected int inData;
    protected boolean inGround;
    protected int timeInGround;
    /** 1 if the player can pick up the arrow */
    public PickupStatus pickupStatus;
    /** Seems to be some sort of timer for animating an arrow. */
    public int arrowShake;
    /** The owner of this arrow. */
    public Entity shootingEntity;
    protected int ticksInGround;
    protected int ticksInAir;
    protected double damage;
    /** The amount of knockback an arrow applies when it hits a mob. */
    protected int knockbackStrength;
    
	protected ItemStack stack;
	protected ParticleType<?> critParticles = ParticleTypes.CRIT;
	protected boolean despawnOnLand = false;
	protected int fire = 0;
	protected ResourceLocation texture = new ResourceLocation("minestuck", "textures/entity/projectiles/energy_arrow_blue.png");;
    
    public EntityBullet(EntityType<? extends EntityBullet> type, World world)
    {
        super(type, world);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.pickupStatus = PickupStatus.DISALLOWED;
        this.damage = 2.0D;
    }

    public EntityBullet(World worldIn, double x, double y, double z)
    {
        this(MSEntityTypes.BULLET, worldIn);
        this.setPosition(x, y, z);
    }

    public EntityBullet(World worldIn, LivingEntity shooter)
    {
        this(worldIn, shooter.posX, shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ);
        this.shootingEntity = shooter;

        if (shooter instanceof PlayerEntity)
        {
            this.pickupStatus = PickupStatus.ALLOWED;
        }
    }
    /*
    @Override
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getBoundingBox().getAverageEdgeLength() * 10.0D;

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * getRenderDistanceWeight();
        return distance < d0 * d0;
    }
    
	
	/**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     * /
    public void shoot(double x, double y, double z, float velocity, float inaccuracy)
    {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x + this.rand.nextGaussian() * 0.0075D * (double)inaccuracy;
        y = y + this.rand.nextGaussian() * 0.0075D * (double)inaccuracy;
        z = z + this.rand.nextGaussian() * 0.0075D * (double)inaccuracy;
        x = x * (double)velocity;
        y = y * (double)velocity;
        z = z * (double)velocity;
        setMotion(x, y, z);
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.ticksInGround = 0;
    }
    
    @Override
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    public void setVelocity(double x, double y, double z)
    {
        setMotion(x, y, z);

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (180D / Math.PI));
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    /**
     * Called when the arrow hits a block or an entity
     * /
    protected void onHit(RayTraceResult raytraceResultIn)
    {
        Entity entity = raytraceResultIn.entityHit;

        if (entity != null)
        {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            int i = MathHelper.ceil((double)f * this.damage);

            if (this.getIsCritical())
            {
                i += this.rand.nextInt(i / 2 + 2);
            }

            DamageSource damagesource;

            if (this.shootingEntity == null)
            {
                damagesource = DamageSource.causeArrowDamage(this, this);
            }
            else
            {
                damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
            }

            if (this.isBurning() && !(entity instanceof EndermanEntity))
            {
            	//TODO
                entity.setFire(fire*5/100);
            }

            if (entity.attackEntityFrom(damagesource, (float)i))
            {
                if (entity instanceof LivingEntity)
                {
                    LivingEntity entitylivingbase = (LivingEntity)entity;

                    if (!this.world.isRemote)
                    {
                        entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                    }

                    if (this.knockbackStrength > 0)
                    {
                        float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                        if (f1 > 0.0F)
                        {
                            entitylivingbase.addVelocity(this.motionX * (double)this.knockbackStrength * 0.6000000238418579D / (double)f1, 0.1D, this.motionZ * (double)this.knockbackStrength * 0.6000000238418579D / (double)f1);
                        }
                    }

                    if (this.shootingEntity instanceof LivingEntity)
                    {
                        EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments((LivingEntity)this.shootingEntity, entitylivingbase);
                    }

                    this.arrowHit(entitylivingbase);

                    if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof PlayerEntity && this.shootingEntity instanceof ServerPlayerEntity)
                    {
                        ((ServerPlayerEntity)this.shootingEntity).connection.sendPacket(new SChangeGameStatePacket(6, 0.0F));
                    }
                }

                this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                if (!(entity instanceof EndermanEntity))
                {
                    this.remove();
                }
            }
            else
            {
                this.motionX *= -0.1D;
                this.motionY *= -0.1D;
                this.motionZ *= -0.1D;
                this.rotationYaw += 180.0F;
                this.prevRotationYaw += 180.0F;
                this.ticksInAir = 0;

                if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ < 0.0010000000474974513D)
                {
                    if (this.pickupStatus == PickupStatus.ALLOWED)
                    {
                        this.entityDropItem(this.getArrowStack(), 0.1F);
                    }

                    this.remove();
                }
            }
        }
        else
        {
            BlockPos blockpos = raytraceResultIn.getBlockPos();
            this.xTile = blockpos.getX();
            this.yTile = blockpos.getY();
            this.zTile = blockpos.getZ();
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            this.inTile = iblockstate.getBlock();
            this.inData = this.inTile.getMetaFromState(iblockstate);
            this.motionX = (double)((float)(raytraceResultIn.hitVec.x - this.posX));
            this.motionY = (double)((float)(raytraceResultIn.hitVec.y - this.posY));
            this.motionZ = (double)((float)(raytraceResultIn.hitVec.z - this.posZ));
            float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            this.posX -= this.motionX / (double)f2 * 0.05000000074505806D;
            this.posY -= this.motionY / (double)f2 * 0.05000000074505806D;
            this.posZ -= this.motionZ / (double)f2 * 0.05000000074505806D;
            this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            this.inGround = true;
            this.arrowShake = 7;
            this.setIsCritical(false);

            if (iblockstate.getMaterial() != Material.AIR)
            {
                this.inTile.onEntityCollidedWithBlock(this.world, blockpos, iblockstate, this);
                if(this.despawnOnLand) this.setDead();
            }
        }
    }
    
    @Override
    public void move(MoverType typeIn, Vec3d pos)
    {
        super.move(typeIn, pos);
        
        if (this.inGround)
        {
            this.xTile = MathHelper.floor(this.posX);
            this.yTile = MathHelper.floor(this.posY);
            this.zTile = MathHelper.floor(this.posZ);
        }
    }

    protected void arrowHit(LivingEntity living)
    {
    }

    @Nullable
    protected Entity findEntityOnPath(Vec3d start, Vec3d end)
    {
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D), BULLET_TARGETS);
        double d0 = 0.0D;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity1 = list.get(i);

            if (entity1 != this.shootingEntity || this.ticksInAir >= 5)
            {
                AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(0.3D);
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

                if (raytraceresult != null)
                {
                    double d1 = start.squareDistanceTo(raytraceresult.getHitVec());

                    if (d1 < d0 || d0 == 0.0D)
                    {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity;
    }
    */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        compound.putInt("xTile", this.xTile);
        compound.putInt("yTile", this.yTile);
        compound.putInt("zTile", this.zTile);
        compound.putShort("life", (short)this.ticksInGround);
        ResourceLocation resourcelocation = inTile.getRegistryName();
        compound.putString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        compound.putByte("inData", (byte)this.inData);
        compound.putByte("shake", (byte)this.arrowShake);
        compound.putByte("inGround", (byte)(this.inGround ? 1 : 0));
        compound.putByte("pickup", (byte)this.pickupStatus.ordinal());
        compound.putDouble("damage", this.damage);
        compound.putBoolean("crit", this.getIsCritical());
    }
    
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        this.xTile = compound.getInt("xTile");
        this.yTile = compound.getInt("yTile");
        this.zTile = compound.getInt("zTile");
        this.ticksInGround = compound.getShort("life");

        this.inTile = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryCreate(compound.getString("inTile")));

        this.inData = compound.getByte("inData") & 255;
        this.arrowShake = compound.getByte("shake") & 255;
        this.inGround = compound.getByte("inGround") == 1;

        if (compound.contains("damage", 99))
        {
            this.damage = compound.getDouble("damage");
        }

        if (compound.contains("pickup", 99))
        {
            this.pickupStatus = PickupStatus.getByOrdinal(compound.getByte("pickup"));
        }
        else if (compound.contains("player", 99))
        {
            this.pickupStatus = compound.getBoolean("player") ? PickupStatus.ALLOWED : PickupStatus.DISALLOWED;
        }

        this.setIsCritical(compound.getBoolean("crit"));
    }
    
    @Override
    public void onCollideWithPlayer(PlayerEntity entityIn)
    {
        if (!this.world.isRemote && this.inGround && this.arrowShake <= 0)
        {
            boolean flag = this.pickupStatus == PickupStatus.ALLOWED || this.pickupStatus == PickupStatus.CREATIVE_ONLY && entityIn.abilities.isCreativeMode;

            if (this.pickupStatus == PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(this.getArrowStack()))
            {
                flag = false;
            }

            if (flag)
            {
                entityIn.onItemPickup(this, 1);
                this.remove();
            }
        }
    }

    public ItemStack getArrowStack()		{return stack;}
    public void setArrowStack(ItemStack in) {stack = in;}
    
    public int getFire() 	 	{return fire;}
    //public void setFire(int in) {fire = in; ((Entity)this).setFire(in);}
    
    public void setTexture(ResourceLocation in) {texture = in; System.out.println(texture);}
    public ResourceLocation getTexture() 		{return texture;}
    
    public void setDespawnOnGround(boolean in)  {despawnOnLand = in;}
	
    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking(){return false;}

    public void setDamage(double damageIn){this.damage = damageIn;}
    public double getDamage(){return this.damage;}

    /**
     * Sets the amount of knockback the arrow applies when it hits a mob.
     */
    public void setKnockbackStrength(int knockbackStrengthIn)
    {
        this.knockbackStrength = knockbackStrengthIn;
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return false;
    }
    
    @Override
    protected float getEyeHeight(Pose poseIn, EntitySize sizeIn)
    {
        return 0.0F;
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */
    public void setIsCritical(boolean critical)
    {
        byte b0 = ((Byte)this.dataManager.get(CRITICAL)).byteValue();

        if (critical)
        {
            this.dataManager.set(CRITICAL, Byte.valueOf((byte)(b0 | 1)));
        }
        else
        {
            this.dataManager.set(CRITICAL, Byte.valueOf((byte)(b0 & -2)));
        }
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */
    public boolean getIsCritical()
    {
        byte b0 = ((Byte)this.dataManager.get(CRITICAL)).byteValue();
        return (b0 & 1) != 0;
    }

    public void setEnchantmentEffectsFromEntity(LivingEntity p_190547_1_, float p_190547_2_)
    {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, p_190547_1_);
        int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, p_190547_1_);
        this.setDamage((double)(p_190547_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.world.getDifficulty().getId() * 0.11F));

        if (i > 0)
        {
            this.setDamage(this.getDamage() + (double)i * 0.5D + 0.5D);
        }

        if (j > 0)
        {
            this.setKnockbackStrength(j);
        }

        if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, p_190547_1_) > 0)
        {
            this.setFire(fire);
        }
    }

    public static enum PickupStatus
    {
        DISALLOWED,
        ALLOWED,
        CREATIVE_ONLY;

        public static PickupStatus getByOrdinal(int ordinal)
        {
            if (ordinal < 0 || ordinal > values().length)
            {
                ordinal = 0;
            }

            return values()[ordinal];
        }
    }

}
