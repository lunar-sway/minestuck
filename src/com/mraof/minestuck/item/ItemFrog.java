package com.mraof.minestuck.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.FrogEntity;

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
import net.minecraft.util.ResourceLocation;
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
		this.addPropertyOverride(new ResourceLocation(Minestuck.MOD_ID, "type"), (stack, world, holder) -> stack.hasTag() ? 0 : stack.getTag().getInt("Type"));
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if(this.isInGroup(group))
		{
			for (int i = 0; i <= FrogEntity.maxTypes(); ++i)
			{
				if(i != 3 && i != 4)
				{
					ItemStack item = new ItemStack(this);
					item.getOrCreateTag().putInt("Type", i);
					items.add(item);
				}
			}
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{stack.hasTag()
		int dmg = stack.hasTag() ? 0 : stack.getTag().getInt("Type");
		
		if(dmg < 1 || dmg > FrogEntity.maxTypes())
		{
			if(stack.hasTag())
			{
				NBTTagCompound nbt = stack.getTag();
				NBTTagInt type = (NBTTagInt)nbt.get("Type");
				NBTTagInt eyeType = (NBTTagInt)nbt.get("EyeType");
				NBTTagInt bellyType = (NBTTagInt)nbt.get("BellyType");
				
					if(nbt.contains("EyeType"))for(int i = 0; i <= FrogEntity.maxEyes(); i++)
					{
						if(eyeType.getInt() == i)tooltip.add(new TextComponentTranslation("item.frog.eyes"+i));
					}
					
					if(nbt.contains("EyeType"))for(int i = 1; i <= FrogEntity.maxBelly(); i++)
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
			NBTTagFloat size = (NBTTagFloat)nbt.get("Size");
			
			if(nbt.contains("Size"))
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

				applyItemEntityDataToEntity(world, player, itemstack,(FrogEntity) entity);

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
		EntityLiving entity = null;

			for (int i = 0; i < 1; ++i)
			{
				entity = new FrogEntity(worldIn, type);
	
				entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
				entity.rotationYawHead = entity.rotationYaw;
				entity.renderYawOffset = entity.rotationYaw;
				entity.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entity)), null, null);
				worldIn.spawnEntity(entity);
				entity.playAmbientSound();
			}

			return entity;
	}
	
	public static void applyItemEntityDataToEntity(World entityWorld, @Nullable EntityPlayer player, ItemStack stack, @Nullable FrogEntity targetEntity)
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
				
				nbttagcompound.putBoolean("PersistenceRequired", true);
				if(nbttagcompound.getInt("Type") == 6)
					nbttagcompound.putFloat("Size", 0.5f);
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				targetEntity.writeUnlessRemoved(nbttagcompound1);
				UUID uuid = targetEntity.getUniqueID();
				nbttagcompound1.merge(nbttagcompound);
				targetEntity.setUniqueId(uuid);
				targetEntity.read(nbttagcompound1);

				//System.out.println("Type: " + nbttagcompound.getInt("Type"));
			}
		}
	}
	
	public static int getSkinColor(ItemStack stack)
	{
		
		NBTTagCompound nbttagcompound = stack.getTag();

		if (nbttagcompound != null)
		{
			if (nbttagcompound.contains("SkinColor"))
			{
				return nbttagcompound.getInt("SkinColor");
			}
		}

		return 0x4BEC13;
	}
	
	public static int getEyeColor(ItemStack stack)
	{
		
		NBTTagCompound nbttagcompound = stack.getTag();

		if (nbttagcompound != null)
		{
			if (nbttagcompound.contains("EyeColor"))
			{
				return nbttagcompound.getInt("eyeColor");
			}
		}

		return 0xC7DB95;
	}
	
	public static int getBellyColor(ItemStack stack)
	{
		
		NBTTagCompound nbttagcompound = stack.getTag();

		if (nbttagcompound != null)
		{
			if(nbttagcompound.contains("bellyType") && nbttagcompound.getInt("bellyType") == 0)
			{
				if(nbttagcompound.contains("skinColor"))
				{
					return nbttagcompound.getInt("skinColor");
				}
				else return 0x4BEC13;
			}
			else if (nbttagcompound.contains("bellyColor"))
			{
				return nbttagcompound.getInt("bellyColor");
			}
		}

		return 0xD6DE83;
	}
	
	
}
