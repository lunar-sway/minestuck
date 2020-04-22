package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.FrogEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class FrogItem extends Item
{
	public FrogItem(Properties properties)
	{
		super(properties);
		this.addPropertyOverride(new ResourceLocation(Minestuck.MOD_ID, "type"), (stack, world, holder) -> !stack.hasTag() ? 0 : stack.getTag().getInt("Type"));
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if(this.isInGroup(group))
		{
			items.add(new ItemStack(this));
			for (int i = 1; i <= FrogEntity.maxTypes(); ++i)
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
	{
		if(stack.hasTag())
		{
			CompoundNBT nbt = stack.getTag();
			int type = nbt.getInt("Type");
				//System.out.println(dmg);
			if(type < 1 || type > FrogEntity.maxTypes())
			{

					int eyeType = nbt.getInt("EyeType");
					int bellyType = nbt.getInt("BellyType");

					if(nbt.contains("EyeType"))for(int i = 0; i <= FrogEntity.maxEyes(); i++)
					{
						if(eyeType == i)tooltip.add(new TranslationTextComponent(getTranslationKey() + ".eyes."+i));
					}

					if(nbt.contains("EyeType"))for(int i = 1; i <= FrogEntity.maxBelly(); i++)
					{
						if(bellyType == i)tooltip.add(new TranslationTextComponent(getTranslationKey() + ".belly."+i));
					}

			}
			else
			{
				switch(type)
				{
					case 4: tooltip.add(new TranslationTextComponent(getTranslationKey() + ".desc.4")); break;
					case 6: tooltip.add(new TranslationTextComponent(getTranslationKey() + ".desc.6")); break;
				}
			}

			if(type != 6)
			{
				float size = nbt.getFloat("Size");

				if(nbt.contains("Size"))
				{
					if(size <= 0.4f) 	tooltip.add(new TranslationTextComponent(getTranslationKey() + ".size.0"));
					else if(size <= 0.8f) tooltip.add(new TranslationTextComponent(getTranslationKey() + ".size.1"));
					else if(size <= 1.4f) tooltip.add(new TranslationTextComponent(getTranslationKey() + ".size.2"));
					else if(size <= 2f) tooltip.add(new TranslationTextComponent(getTranslationKey() + ".size.3"));
					else				tooltip.add(new TranslationTextComponent(getTranslationKey() + ".size.4"));
				}
			}
		}
		else tooltip.add(new TranslationTextComponent(getTranslationKey() + ".random"));
	}

	@Override
	public ITextComponent getDisplayName(ItemStack stack)
	{
		int type = !stack.hasTag() ? 0 : stack.getTag().getInt("Type");

		return new TranslationTextComponent(getTranslationKey() + ".type."+type);

	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		ItemStack itemstack = context.getItem();
		Direction face = context.getFace();
		PlayerEntity player = context.getPlayer();
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		
		if (world.isRemote)
			return ActionResultType.SUCCESS;
		else if (player != null && !player.canPlayerEdit(pos.offset(face), face, itemstack))
		{
			return ActionResultType.FAIL;
		}
		else
		{
			BlockState blockState = world.getBlockState(pos);
			
			BlockPos spawnPos;
			if (blockState.getCollisionShape(world, pos).isEmpty())
				spawnPos = pos;
			else
				spawnPos = pos.offset(face);
			
			Entity entity =  createFrog(world, (double)spawnPos.getX() + 0.5D, (double)spawnPos.getY(), (double)spawnPos.getZ() + 0.5D, 0);
			
			if (entity != null)
			{
				if (entity instanceof LivingEntity && itemstack.hasDisplayName())
				{
					entity.setCustomName(itemstack.getDisplayName());
				}
				applyItemEntityDataToEntity(world, player, itemstack,(FrogEntity) entity);
				world.addEntity(entity);
				if (player == null || !player.isCreative())
				{
					itemstack.shrink(1);
				}
			}
			
			return ActionResultType.SUCCESS;
		}
	}
	
	@Nullable
	public static Entity createFrog(World worldIn, double x, double y, double z, int type)
	{

		FrogEntity frog = null;

			frog = new FrogEntity(worldIn);
			frog.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
			frog.rotationYawHead = frog.rotationYaw;
			frog.renderYawOffset = frog.rotationYaw;
			frog.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(new BlockPos(x,y,z)), null, null, null);

			frog.playAmbientSound();


		return frog;
	}
	public static void applyItemEntityDataToEntity(World entityWorld, @Nullable PlayerEntity player, ItemStack stack, @Nullable FrogEntity targetEntity)
	{
		MinecraftServer minecraftserver = entityWorld.getServer();
		
		if (minecraftserver != null && targetEntity != null)
		{
			CompoundNBT CompoundNBT = stack.getTag();
			
			if (CompoundNBT != null)
			{
				if (!entityWorld.isRemote && targetEntity.ignoreItemEntityData() && (player == null || !minecraftserver.getPlayerList().canSendCommands(player.getGameProfile())))
				{
					return;
				}
				
				CompoundNBT.putBoolean("PersistenceRequired", true);
				CompoundNBT CompoundNBT1 = new CompoundNBT();
				targetEntity.writeUnlessRemoved(CompoundNBT1);
				UUID uuid = targetEntity.getUniqueID();
				CompoundNBT1.merge(CompoundNBT);
				targetEntity.setUniqueId(uuid);
				targetEntity.read(CompoundNBT1);


			}
		}
	}
	
	public static int getSkinColor(ItemStack stack)
	{
		
		CompoundNBT CompoundNBT = stack.getTag();
		
		if (CompoundNBT != null)
		{
			if (CompoundNBT.contains("SkinColor"))
			{
				return CompoundNBT.getInt("SkinColor");
			}
		}
		
		return 0x4BEC13;
	}
	
	public static int getEyeColor(ItemStack stack)
	{
		
		CompoundNBT CompoundNBT  = stack.getTag();
		
		if (CompoundNBT != null)
		{
			if (CompoundNBT.contains("EyeColor"))
			{
				return CompoundNBT.getInt("EyeColor");
			}
		}
		
		return 0xC7DB95;
	}
	
	public static int getBellyColor(ItemStack stack)
	{
		
		CompoundNBT CompoundNBT = stack.getTag();
		
		if (CompoundNBT != null)
		{
			if(CompoundNBT.contains("bellyType") && CompoundNBT.getInt("bellyType") == 0)
			{
				if(CompoundNBT.contains("skinColor"))
				{
					return CompoundNBT.getInt("skinColor");
				}
				else return 0x4BEC13;
			}
			else if (CompoundNBT.contains("bellyColor"))
			{
				return CompoundNBT.getInt("bellyColor");
			}
		}
		
		return 0xD6DE83;
	}
	
	
}
