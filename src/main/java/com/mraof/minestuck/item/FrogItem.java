package com.mraof.minestuck.item;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class FrogItem extends Item
{
	public FrogItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items)
	{
		if(this.allowdedIn(group))
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
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
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
						if(eyeType == i)tooltip.add(new TranslationTextComponent(getDescriptionId() + ".eyes."+i));
					}

					if(nbt.contains("EyeType"))for(int i = 1; i <= FrogEntity.maxBelly(); i++)
					{
						if(bellyType == i)tooltip.add(new TranslationTextComponent(getDescriptionId() + ".belly."+i));
					}

			}
			else
			{
				switch(type)
				{
					case 4: tooltip.add(new TranslationTextComponent(getDescriptionId() + ".desc.4")); break;
					case 6: tooltip.add(new TranslationTextComponent(getDescriptionId() + ".desc.6")); break;
				}
			}

			if(type != 6)
			{
				float size = nbt.getFloat("Size");

				if(nbt.contains("Size"))
				{
					if(size <= 0.4f) 	tooltip.add(new TranslationTextComponent(getDescriptionId() + ".size.0"));
					else if(size <= 0.8f) tooltip.add(new TranslationTextComponent(getDescriptionId() + ".size.1"));
					else if(size <= 1.4f) tooltip.add(new TranslationTextComponent(getDescriptionId() + ".size.2"));
					else if(size <= 2f) tooltip.add(new TranslationTextComponent(getDescriptionId() + ".size.3"));
					else				tooltip.add(new TranslationTextComponent(getDescriptionId() + ".size.4"));
				}
			}
		}
		else tooltip.add(new TranslationTextComponent(getDescriptionId() + ".random"));
	}

	@Override
	public ITextComponent getName(ItemStack stack)
	{
		int type = !stack.hasTag() ? 0 : stack.getTag().getInt("Type");

		return new TranslationTextComponent(getDescriptionId() + ".type."+type);

	}

	@Override
	public ActionResultType useOn(ItemUseContext context)
	{
		ItemStack itemstack = context.getItemInHand();
		Direction face = context.getClickedFace();
		PlayerEntity player = context.getPlayer();
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		
		if (world.isClientSide)
			return ActionResultType.SUCCESS;
		else if (player != null && !player.mayUseItemAt(pos.relative(face), face, itemstack))
		{
			return ActionResultType.FAIL;
		}
		else
		{
			BlockState blockState = world.getBlockState(pos);
			
			BlockPos spawnPos;
			if (blockState.getBlockSupportShape(world, pos).isEmpty())
				spawnPos = pos;
			else
				spawnPos = pos.relative(face);
			
			Entity entity =  createFrog((ServerWorld) world, (double)spawnPos.getX() + 0.5D, (double)spawnPos.getY(), (double)spawnPos.getZ() + 0.5D, 0);
			
			if (entity != null)
			{
				if (entity instanceof LivingEntity && itemstack.hasCustomHoverName())
				{
					entity.setCustomName(itemstack.getHoverName());
				}
				applyItemEntityDataToEntity(world, player, itemstack,(FrogEntity) entity);
				world.addFreshEntity(entity);
				if (player == null || !player.isCreative())
				{
					itemstack.shrink(1);
				}
			}
			
			return ActionResultType.SUCCESS;
		}
	}
	
	@Nullable
	public static Entity createFrog(ServerWorld worldIn, double x, double y, double z, int type)
	{

		FrogEntity frog = null;

			frog = new FrogEntity(worldIn);
			frog.moveTo(x, y, z, MathHelper.wrapDegrees(worldIn.random.nextFloat() * 360.0F), 0.0F);
			frog.yHeadRot = frog.yRot;
			frog.yBodyRot = frog.yRot;
			frog.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(new BlockPos(x,y,z)), null, null, null);

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
				if (!entityWorld.isClientSide && targetEntity.onlyOpCanSetNbt() && (player == null || !minecraftserver.getPlayerList().isOp(player.getGameProfile())))
				{
					return;
				}
				
				CompoundNBT.putBoolean("PersistenceRequired", true);
				CompoundNBT CompoundNBT1 = new CompoundNBT();
				targetEntity.saveAsPassenger(CompoundNBT1);
				UUID uuid = targetEntity.getUUID();
				CompoundNBT1.merge(CompoundNBT);
				targetEntity.setUUID(uuid);
				targetEntity.load(CompoundNBT1);


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
