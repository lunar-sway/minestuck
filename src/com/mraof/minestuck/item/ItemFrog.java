package com.mraof.minestuck.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mraof.minestuck.entity.EntityFrog;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemFrog extends Item
{
	public ItemFrog(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if(this.isInGroup(group))
		{
			for (int i = 0; i <= EntityFrog.maxTypes(); ++i)
			{
				if(i != 3 && i != 4)
				{
					ItemStack item = new ItemStack(this);
					item.getOrCreateTag().setInt("Type", i);
					items.add(item);
				}
			}
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		int dmg = stack.hasTag() ? 0 : stack.getTag().getInt("Type");
		
		if(dmg < 1 || dmg > EntityFrog.maxTypes())
		{
			if(stack.hasTag())
			{
				NBTTagCompound nbt = stack.getTag();
				NBTTagInt type = (NBTTagInt)nbt.getTag("Type");
				NBTTagInt eyeType = (NBTTagInt)nbt.getTag("EyeType");
				NBTTagInt bellyType = (NBTTagInt)nbt.getTag("BellyType");
				
					if(nbt.hasKey("EyeType"))for(int i = 0; i <= EntityFrog.maxEyes(); i++)
					{
						if(eyeType.getInt() == i)tooltip.add(new TextComponentTranslation("item.frog.eyes"+i));
					}
					
					if(nbt.hasKey("EyeType"))for(int i = 1; i <= EntityFrog.maxBelly(); i++)
					{
						if(bellyType.getInt() == i)tooltip.add(new TextComponentTranslation("item.frog.belly"+i));
					}
			} 
			else tooltip.add(new TextComponentTranslation("item.frog.random"));
		}
		else
		{
			switch(dmg)
			{
				case 4: tooltip.add(new TextComponentTranslation("item.frog.type4")); break;
				case 6: tooltip.add(new TextComponentTranslation("item.frog.type6")); break;
			}
		}
		
		if(stack.hasTag())
		{
			NBTTagCompound nbt = stack.getTag();
			NBTTagFloat size = (NBTTagFloat)nbt.getTag("Size");
			
			if(nbt.hasKey("Size"))
			{
				if(size.getFloat() <= 0.4f) 	tooltip.add(new TextComponentTranslation("item.frog.size0"));
				else if(size.getFloat() <= 0.8f) tooltip.add(new TextComponentTranslation("item.frog.size1"));
				else if(size.getFloat() <= 1.4f) tooltip.add(new TextComponentTranslation("item.frog.size2"));
				else if(size.getFloat() <= 2f) tooltip.add(new TextComponentTranslation("item.frog.size3"));
				else							 tooltip.add(new TextComponentTranslation("item.frog.size4"));
			}
		}
		
	}
	
	@Override
	public EnumActionResult onItemUse(ItemUseContext context)
	{
		ItemStack itemstack = context.getItem();
		EnumFacing face = context.getFace();
		EntityPlayer player = context.getPlayer();
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		
		if (world.isRemote)
			return EnumActionResult.SUCCESS;
		else if (player != null && !player.canPlayerEdit(pos.offset(face), face, itemstack))
		{
			return EnumActionResult.FAIL;
		}
		else
		{
			IBlockState blockState = world.getBlockState(pos);
	
			BlockPos spawnPos;
			if (blockState.getCollisionShape(world, pos).isEmpty())
				spawnPos = pos;
			else
				spawnPos = pos.offset(face);
			
			Entity entity =  spawnCreature(world, (double)spawnPos.getX() + 0.5D, (double)spawnPos.getY(), (double)spawnPos.getZ() + 0.5D, 0);

			if (entity != null)
			{
				if (entity instanceof EntityLivingBase && itemstack.hasDisplayName())
				{
					entity.setCustomName(itemstack.getDisplayName());
				}

				applyItemEntityDataToEntity(world, player, itemstack,(EntityFrog) entity);

				if (player == null || !player.isCreative())
				{
					itemstack.shrink(1);
				}
			}

			return EnumActionResult.SUCCESS;
		}
	}
	
	@Nullable
	public static Entity spawnCreature(World worldIn, double x, double y, double z, int type)
	{
			Entity entity = null;

			for (int i = 0; i < 1; ++i)
			{
				entity = new EntityFrog(worldIn, type);
	
				EntityLiving entityliving = (EntityLiving) entity;
				entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
				entityliving.rotationYawHead = entityliving.rotationYaw;
				entityliving.renderYawOffset = entityliving.rotationYaw;
				entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), null, null);
				worldIn.spawnEntity(entity);
				entityliving.playAmbientSound();
			}

			return entity;
	}
	
	public static void applyItemEntityDataToEntity(World entityWorld, @Nullable EntityPlayer player, ItemStack stack, @Nullable EntityFrog targetEntity)
	{
		MinecraftServer minecraftserver = entityWorld.getServer();

		if (minecraftserver != null && targetEntity != null)
		{
			NBTTagCompound nbttagcompound = stack.getTag();

			if (nbttagcompound != null)
			{
				if (!entityWorld.isRemote && targetEntity.ignoreItemEntityData() && (player == null || !minecraftserver.getPlayerList().canSendCommands(player.getGameProfile())))
				{
					return;
				}
				
				nbttagcompound.setBoolean("PersistenceRequired", true);
				if(nbttagcompound.getInt("Type") == 6)
					nbttagcompound.setFloat("Size", 0.5f);
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				targetEntity.writeEntityToNBT(nbttagcompound1);
				UUID uuid = targetEntity.getUniqueID();
				nbttagcompound1.merge(nbttagcompound);
				targetEntity.setUniqueId(uuid);
				targetEntity.readEntityFromNBT(nbttagcompound1);

				//System.out.println("Type: " + nbttagcompound.getInt("Type"));
			}
		}
	}
	
	public int getSkinColor(ItemStack stack)
	{
		
		NBTTagCompound nbttagcompound = stack.getTag();

		if (nbttagcompound != null)
		{
			if (nbttagcompound.hasKey("SkinColor"))
			{
				return nbttagcompound.getInt("SkinColor");
			}
		}

		return 0x4BEC13;
	}
	
	public int getEyeColor(ItemStack stack)
	{
		
		NBTTagCompound nbttagcompound = stack.getTag();

		if (nbttagcompound != null)
		{
			if (nbttagcompound.hasKey("EyeColor"))
			{
				return nbttagcompound.getInt("eyeColor");
			}
		}

		return 0xC7DB95;
	}
	
	public int getBellyColor(ItemStack stack)
	{
		
		NBTTagCompound nbttagcompound = stack.getTag();

		if (nbttagcompound != null)
		{
			if(nbttagcompound.hasKey("bellyType") && nbttagcompound.getInt("bellyType") == 0)
			{
				if(nbttagcompound.hasKey("skinColor"))
				{
					return nbttagcompound.getInt("skinColor");
				}
				else return 0x4BEC13;
			}
			else if (nbttagcompound.hasKey("bellyColor"))
			{
				return nbttagcompound.getInt("bellyColor");
			}
		}

		return 0xD6DE83;
	}
	
	
}
