package com.mraof.minestuck.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.entity.EntityFrog;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemFrog extends Item
{
	public ItemFrog()
	{
		this.setHasSubtypes(true);
		setUnlocalizedName("frog");
		setCreativeTab(TabMinestuck.instance);
	}
	
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
        	//TODO
            for (int i = 0; i <= EntityFrog.maxTypes(); ++i)
            {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if(stack.hasTagCompound())
		{
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			NBTTagInt type = (NBTTagInt)nbttagcompound.getTag("Type");
			NBTTagInt eyeType = (NBTTagInt)nbttagcompound.getTag("eyeType");
			NBTTagInt bellyType = (NBTTagInt)nbttagcompound.getTag("bellyType");
			NBTTagFloat size = (NBTTagFloat)nbttagcompound.getTag("Size");
			
			if(type.getInt() < 1 || type.getInt() > EntityFrog.maxTypes())
			{
				
				for(int i = 0; i <= EntityFrog.maxEyes(); i++)
				{
					if(eyeType.getInt() == i)tooltip.add(I18n.translateToLocal("item.frog.eyes"+i));
				}
				
				for(int i = 0; i <= EntityFrog.maxBelly(); i++)
				{
					if(bellyType.getInt() == i)tooltip.add(I18n.translateToLocal("item.frog.belly"+i));
				}
			}
			else
			{
				for(int i = 1; i <= EntityFrog.maxTypes(); i++)
				{
					if(type.getInt() == i)tooltip.add(I18n.translateToLocal("item.frog.type"+i));
				}
			}
			
			if(size.getFloat() <= 0.4f) 	tooltip.add(I18n.translateToLocal("item.frog.size0"));
			else if(size.getFloat() <= 0.8f) tooltip.add(I18n.translateToLocal("item.frog.size1"));
			else if(size.getFloat() <= 1.4f) tooltip.add(I18n.translateToLocal("item.frog.size2"));
			else							 tooltip.add(I18n.translateToLocal("item.frog.size3"));
			
		} else {
			tooltip.add(I18n.translateToLocal("item.frog.random"));
		}
		
		
		
	}
	
	/**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        int dmg = itemstack.getMetadata();
        
        System.out.println(dmg);
        
        if (worldIn.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        else if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            
            BlockPos blockpos = pos.offset(facing);
            double d0 = this.getYOffset(worldIn, blockpos);
            Entity entity =  spawnCreature(worldIn, (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + d0, (double)blockpos.getZ() + 0.5D);

            if (entity != null)
            {
            	
            	
                if (entity instanceof EntityLivingBase && itemstack.hasDisplayName())
                {
                    entity.setCustomNameTag(itemstack.getDisplayName());
                }

                applyItemEntityDataToEntity(worldIn, player, itemstack, entity, dmg);

                if (!player.capabilities.isCreativeMode)
                {
                    itemstack.shrink(1);
                }
            }

            return EnumActionResult.SUCCESS;
        }
    }
    
    @Nullable
    public static Entity spawnCreature(World worldIn, double x, double y, double z)
    {
            Entity entity = null;

            for (int i = 0; i < 1; ++i)
            {
                entity = new EntityFrog(worldIn);

                if (entity instanceof EntityLiving)
                {
                    EntityLiving entityliving = (EntityLiving)entity;
                    entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                    entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
                    worldIn.spawnEntity(entity);
                    entityliving.playLivingSound();
                }
            }

            return entity;
    }
    
    public static void applyItemEntityDataToEntity(World entityWorld, @Nullable EntityPlayer player, ItemStack stack, @Nullable Entity targetEntity, int dmg)
    {
        MinecraftServer minecraftserver = entityWorld.getMinecraftServer();

        if (minecraftserver != null && targetEntity != null)
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound();

            if (nbttagcompound != null)
            {
                if (!entityWorld.isRemote && targetEntity.ignoreItemEntityData() && (player == null || !minecraftserver.getPlayerList().canSendCommands(player.getGameProfile())))
                {
                    return;
                }
                
                nbttagcompound.setBoolean("canDespawn", false);
                nbttagcompound.setInteger("Type", dmg);
                NBTTagCompound nbttagcompound1 = targetEntity.writeToNBT(new NBTTagCompound());
                UUID uuid = targetEntity.getUniqueID();
                nbttagcompound1.merge(nbttagcompound);
                targetEntity.setUniqueId(uuid);
                targetEntity.readFromNBT(nbttagcompound1);
            }
        }
    }
    
   
    
    protected double getYOffset(World p_190909_1_, BlockPos p_190909_2_)
    {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(p_190909_2_)).expand(0.0D, -1.0D, 0.0D);
        List<AxisAlignedBB> list = p_190909_1_.getCollisionBoxes((Entity)null, axisalignedbb);

        if (list.isEmpty())
        {
            return 0.0D;
        }
        else
        {
            double d0 = axisalignedbb.minY;

            for (AxisAlignedBB axisalignedbb1 : list)
            {
                d0 = Math.max(axisalignedbb1.maxY, d0);
            }

            return d0 - (double)p_190909_2_.getY();
        }
    }
    
    
}
