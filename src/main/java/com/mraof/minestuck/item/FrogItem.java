package com.mraof.minestuck.item;

import com.mraof.minestuck.entity.FrogEntity;
import com.mraof.minestuck.item.components.FrogTraitsComponent;
import com.mraof.minestuck.item.components.MSItemComponents;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class FrogItem extends Item
{
	public FrogItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable TooltipContext context, List<Component> tooltip, TooltipFlag flagIn)
	{
		FrogTraitsComponent traits = getFrogTraits(stack);
		if(traits.variant().isPresent())
		{
			String descKey = getDescriptionId() + ".desc." + traits.variant().get().getSerializedName();
			if(I18n.exists(descKey))
				tooltip.add(Component.translatable(descKey));
			
			if(traits.variant().get() == FrogEntity.FrogVariants.DEFAULT)
			{
				if(!(traits.bellyType().isPresent() && traits.bellyType().get() == FrogEntity.BellyTypes.NONE))
					tooltip.add(Component.translatable(getDescriptionId() + ".belly." + (traits.bellyType().isPresent() ? traits.bellyType().get().getSerializedName() : "random")));
				tooltip.add(Component.translatable(getDescriptionId() + ".eyes." + (traits.eyeType().isPresent() ? traits.eyeType().get().getSerializedName() : "random")));
			}
			
			if(traits.size().isPresent())
			{
				double size = traits.size().get();
				if(size <= 0.4f) tooltip.add(Component.translatable(getDescriptionId() + ".size.0"));
				else if(size <= 0.8f) tooltip.add(Component.translatable(getDescriptionId() + ".size.1"));
				else if(size <= 1.4f) tooltip.add(Component.translatable(getDescriptionId() + ".size.2"));
				else if(size <= 2f) tooltip.add(Component.translatable(getDescriptionId() + ".size.3"));
				else tooltip.add(Component.translatable(getDescriptionId() + ".size.4"));
			}
			else tooltip.add(Component.translatable(getDescriptionId() + ".size.random"));
		}
		else tooltip.add(Component.translatable(getDescriptionId() + ".random"));
	}

	@Override
	public Component getName(ItemStack stack)
	{
		String variant = getFrogTraits(stack).variant().orElse(FrogEntity.FrogVariants.DEFAULT).getSerializedName();
		return Component.translatable(getDescriptionId() + ".type." + variant);

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
				if (entity instanceof LivingEntity && itemstack.has(DataComponents.CUSTOM_NAME))
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
		FrogEntity frog = new FrogEntity(level);
		frog.moveTo(x, y, z, Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
		frog.yHeadRot = frog.getYRot();
		frog.yBodyRot = frog.getYRot();
		
		frog.playAmbientSound();
		return frog;
	}
	public static void applyItemEntityDataToEntity(Level level, @Nullable Player player, ItemStack stack, @Nullable FrogEntity targetEntity)
	{
		MinecraftServer minecraftserver = level.getServer();
		
		if (minecraftserver != null && targetEntity != null)
		{
			if (stack.has(DataComponents.ENTITY_DATA))
			{
				CompoundTag stackTag = stack.get(DataComponents.ENTITY_DATA).copyTag();
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
			
			FrogTraitsComponent traits = getFrogTraits(stack);
			traits.apply(targetEntity);
		}
	}
	
	private static FrogTraitsComponent getFrogTraits(ItemStack stack)
	{
		return stack.getOrDefault(MSItemComponents.FROG_TRAITS, FrogTraitsComponent.RANDOM);
	}
	
	public static int getSkinColor(ItemStack stack)
	{
		
		return getFrogTraits(stack).skinColor().orElse(0x4BEC13);
	}
	
	public static int getEyeColor(ItemStack stack)
	{
		return getFrogTraits(stack).skinColor().orElse(0xC7DB95);
	}
	
	public static int getBellyColor(ItemStack stack)
	{
		FrogTraitsComponent traits = getFrogTraits(stack);
		return traits.bellyType().isPresent() && traits.bellyType().get() == FrogEntity.BellyTypes.NONE ? getSkinColor(stack) :
				traits.bellyColor().orElse(0xD6DE83);
	}
	
	
}
