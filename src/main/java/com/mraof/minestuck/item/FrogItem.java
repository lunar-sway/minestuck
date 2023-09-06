package com.mraof.minestuck.item;

import com.mraof.minestuck.entity.FrogEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class FrogItem extends Item
{
	public FrogItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn)
	{
		if(stack.hasTag())
		{
			CompoundTag nbt = stack.getTag();
			int type = nbt.getInt("Type");
				//System.out.println(dmg);
			if(type < 1 || type > FrogEntity.maxTypes())
			{

					int eyeType = nbt.getInt("EyeType");
					int bellyType = nbt.getInt("BellyType");

					if(nbt.contains("EyeType"))for(int i = 0; i <= FrogEntity.maxEyes(); i++)
					{
						if(eyeType == i)tooltip.add(Component.translatable(getDescriptionId() + ".eyes."+i));
					}

					if(nbt.contains("EyeType"))for(int i = 1; i <= FrogEntity.maxBelly(); i++)
					{
						if(bellyType == i)tooltip.add(Component.translatable(getDescriptionId() + ".belly."+i));
					}

			}
			else
			{
				switch(type)
				{
					case 4: tooltip.add(Component.translatable(getDescriptionId() + ".desc.4")); break;
					case 6: tooltip.add(Component.translatable(getDescriptionId() + ".desc.6")); break;
				}
			}

			if(type != 6)
			{
				float size = nbt.getFloat("Size");

				if(nbt.contains("Size"))
				{
					if(size <= 0.4f) 	tooltip.add(Component.translatable(getDescriptionId() + ".size.0"));
					else if(size <= 0.8f) tooltip.add(Component.translatable(getDescriptionId() + ".size.1"));
					else if(size <= 1.4f) tooltip.add(Component.translatable(getDescriptionId() + ".size.2"));
					else if(size <= 2f) tooltip.add(Component.translatable(getDescriptionId() + ".size.3"));
					else				tooltip.add(Component.translatable(getDescriptionId() + ".size.4"));
				}
			}
		}
		else tooltip.add(Component.translatable(getDescriptionId() + ".random"));
	}

	@Override
	public Component getName(ItemStack stack)
	{
		int type = !stack.hasTag() ? 0 : stack.getTag().getInt("Type");

		return Component.translatable(getDescriptionId() + ".type."+type);

	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		ItemStack itemstack = context.getItemInHand();
		Direction face = context.getClickedFace();
		Player player = context.getPlayer();
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		
		if (level.isClientSide)
			return InteractionResult.SUCCESS;
		else if (player != null && !player.mayUseItemAt(pos.relative(face), face, itemstack))
		{
			return InteractionResult.FAIL;
		}
		else
		{
			BlockState blockState = level.getBlockState(pos);
			
			BlockPos spawnPos;
			if (blockState.getBlockSupportShape(level, pos).isEmpty())
				spawnPos = pos;
			else
				spawnPos = pos.relative(face);
			
			Entity entity =  createFrog((ServerLevel) level, (double)spawnPos.getX() + 0.5D, (double)spawnPos.getY(), (double)spawnPos.getZ() + 0.5D, 0);
			
			if (entity != null)
			{
				if (entity instanceof LivingEntity && itemstack.hasCustomHoverName())
				{
					entity.setCustomName(itemstack.getHoverName());
				}
				applyItemEntityDataToEntity(level, player, itemstack,(FrogEntity) entity);
				level.addFreshEntity(entity);
				if (player == null || !player.isCreative())
				{
					itemstack.shrink(1);
				}
			}
			
			return InteractionResult.SUCCESS;
		}
	}
	
	@Nullable
	public static Entity createFrog(ServerLevel level, double x, double y, double z, int type)
	{

		FrogEntity frog = null;

			frog = new FrogEntity(level);
			frog.moveTo(x, y, z, Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
			frog.yHeadRot = frog.getYRot();
			frog.yBodyRot = frog.getYRot();
			frog.finalizeSpawn(level, level.getCurrentDifficultyAt(BlockPos.containing(x,y,z)), null, null, null);

			frog.playAmbientSound();


		return frog;
	}
	public static void applyItemEntityDataToEntity(Level level, @Nullable Player player, ItemStack stack, @Nullable FrogEntity targetEntity)
	{
		MinecraftServer minecraftserver = level.getServer();
		
		if (minecraftserver != null && targetEntity != null)
		{
			CompoundTag stackTag = stack.getTag();
			
			if (stackTag != null)
			{
				if (!level.isClientSide && targetEntity.onlyOpCanSetNbt() && (player == null || !minecraftserver.getPlayerList().isOp(player.getGameProfile())))
				{
					return;
				}
				
				stackTag.putBoolean("PersistenceRequired", true);
				CompoundTag entityTag = new CompoundTag();
				targetEntity.saveAsPassenger(entityTag);
				UUID uuid = targetEntity.getUUID();
				entityTag.merge(stackTag);
				targetEntity.setUUID(uuid);
				targetEntity.load(entityTag);


			}
		}
	}
	
	public static int getSkinColor(ItemStack stack)
	{
		
		CompoundTag tag = stack.getTag();
		
		if (tag != null)
		{
			if (tag.contains("SkinColor"))
			{
				return tag.getInt("SkinColor");
			}
		}
		
		return 0x4BEC13;
	}
	
	public static int getEyeColor(ItemStack stack)
	{
		
		CompoundTag tag  = stack.getTag();
		
		if (tag != null)
		{
			if (tag.contains("EyeColor"))
			{
				return tag.getInt("EyeColor");
			}
		}
		
		return 0xC7DB95;
	}
	
	public static int getBellyColor(ItemStack stack)
	{
		
		CompoundTag tag = stack.getTag();
		
		if (tag != null)
		{
			if(tag.contains("bellyType") && tag.getInt("bellyType") == 0)
			{
				if(tag.contains("skinColor"))
				{
					return tag.getInt("skinColor");
				}
				else return 0x4BEC13;
			}
			else if (tag.contains("bellyColor"))
			{
				return tag.getInt("bellyColor");
			}
		}
		
		return 0xD6DE83;
	}
	
	
}
