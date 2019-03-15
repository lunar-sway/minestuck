package com.mraof.minestuck.item.weapon;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import javax.annotation.Nullable;

import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.TabMinestuck;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRanged extends Item
{
	protected int startupTicks;
	protected Object ammo;
	protected Class<? extends Entity> projectile;
	protected double damage;
	protected float speedMultiplier = 1.0F;
	protected SoundEvent sound = SoundEvents.ENTITY_ARROW_SHOOT;
	
	public ItemRanged(String unlocalizedName, double damage, int durabillity, int startupTicks, Object ammo, Class<? extends Entity> projectile)
    {
		this.setUnlocalizedName(unlocalizedName);
		this.damage = damage;
        this.maxStackSize = 1;
        this.startupTicks = startupTicks;
        this.ammo = ammo;
        this.projectile = projectile;
        this.setMaxDamage(durabillity);
        this.setCreativeTab(TabMinestuck.instance);
        ItemRanged item = this;
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                if (entityIn == null) return 0.0F;
                else return entityIn.getActiveItemStack().getItem() != item ? 0.0F : (float)(stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F;
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) 
	{
		if(entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entityLiving;
			boolean useAmmo = player.capabilities.isCreativeMode || !requiresAmmo() || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
			ItemStack ammo = this.findAmmo(player);
			
			int i = this.getMaxItemUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, player, i, !ammo.isEmpty() || useAmmo);

            if (i < 0) return;
            
            if(!ammo.isEmpty() || useAmmo)
            {
            	float velocity = getBulletVelocity(i);
            	if(velocity >= 0.1F)
            	{
            		boolean arrowPickup = player.capabilities.isCreativeMode || (ammo.getItem() instanceof ItemArrow && ((ItemArrow) ammo.getItem()).isInfinite(ammo, stack, player));
            		if(!worldIn.isRemote)
            		{

            			if(projectile.isAssignableFrom(EntityArrow.class))
            			{
            				EntityArrow entityarrow = null;
            				if(ammo.getItem() instanceof ItemArrow)
            					entityarrow = ((ItemArrow)ammo.getItem()).createArrow(worldIn, ammo, player);
            				else
            					try{entityarrow = (EntityArrow) this.projectile.getConstructor(World.class, EntityLivingBase.class).newInstance(worldIn, player);}
            				catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) 
            				{
								e.printStackTrace();
            				}
							
                            entityarrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity * 3.0F, 1.0F);
                            int fire = 0;
                            
                            if (velocity >= 1.0F)
                            {
                                entityarrow.setIsCritical(true);
                                //TODO
                                if(this.equals(MinestuckItems.infernoShot))
                                	fire += 120;
                            }

                            int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                            if (j > 0) entityarrow.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
                            int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                            if (k > 0) entityarrow.setKnockbackStrength(k);
                            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) fire += 100;

                            stack.damageItem(1, player);

                            if (arrowPickup || player.capabilities.isCreativeMode && (ammo.getItem() == Items.SPECTRAL_ARROW || ammo.getItem() == Items.TIPPED_ARROW))
                                entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                            
                            entityarrow.setFire(fire);
                            
                            worldIn.spawnEntity(entityarrow);
            			}
            			else
            			{
            				Entity entity = null;
            				try{entity = (Entity) this.projectile.getConstructor(World.class).newInstance(worldIn);}
            				catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) 
            				{
								e.printStackTrace();
            				}
            				
            				shoot(entity, player.rotationPitch, player.rotationYaw, 0.0F, velocity * 3.0F, 1.0F);
            				
            			}
            		}

                    worldIn.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

                    if (!useAmmo && !player.capabilities.isCreativeMode)
                    {
                        ammo.shrink(1);

                        if (ammo.isEmpty())
                        {
                            player.inventory.deleteStack(ammo);
                        }
                    }

                    player.addStat(StatList.getObjectUseStats(this));
            	}
            }
		}
	}
	
	public void shoot(Entity projectile, Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy)
    {
        float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float f1 = -MathHelper.sin(pitch * 0.017453292F);
        float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        this.shoot(projectile, (double)f, (double)f1, (double)f2, velocity, inaccuracy);
        projectile.motionX += shooter.motionX;
        projectile.motionZ += shooter.motionZ;

        if (!shooter.onGround)
        {
        	projectile.motionY += shooter.motionY;
        }
    }
	
	public void shoot(Entity projectile, double x, double y, double z, float velocity, float inaccuracy)
    {
		Random rand = new Random();
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x + rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        y = y + rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        z = z + rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        x = x * (double)velocity;
        y = y * (double)velocity;
        z = z * (double)velocity;
        projectile.motionX = x;
        projectile.motionY = y;
        projectile.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        projectile.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
        projectile.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
        projectile.prevRotationYaw = projectile.rotationYaw;
        projectile.prevRotationPitch = projectile.rotationPitch;
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		boolean hasAmmo = !this.findAmmo(playerIn).isEmpty() || !this.requiresAmmo();
		
		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(stack, worldIn, playerIn, handIn, hasAmmo);
	        if (ret != null) return ret;
        
       if(!playerIn.capabilities.isCreativeMode && !hasAmmo)
	        	return hasAmmo  ? new ActionResult(EnumActionResult.PASS, stack) : new ActionResult(EnumActionResult.FAIL, stack);
    	else
	    {
	        playerIn.setActiveHand(handIn);
	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	    }
	}
	
	/**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }
	
	//ammo lookup
	protected ItemStack findAmmo(EntityPlayer player)
    {
        if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isAmmo(itemstack))
                {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }
	
	protected boolean requiresAmmo() 
	{
		if(ammo == null) return false;
		if(ammo instanceof ItemStack) return ((ItemStack)ammo).isEmpty();
		return true;
	}
	
	protected boolean isAmmo(ItemStack stack)
	{
		if(ammo instanceof ItemStack) return stack.isItemEqual((ItemStack)ammo);
		else if(ammo instanceof Item) return stack.getItem().equals(ammo);
		else return stack.getClass().isAssignableFrom(ammo.getClass());
	}
	
	//Getters & Setters
	
	
	public float getBulletVelocity(int charge)
    {
        float f = (float)charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        return f * speedMultiplier;
    }
	
	public Item setSound(SoundEvent in) {sound = in; return this;}
	public Item setBulletSpeed(float in) {speedMultiplier = in; return this;}
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {return startupTicks;}
	
}
