package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.MSEntityTypes;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PosterEntity extends Painting
{
	private ItemStack droppedItem;
	
	public PosterEntity(Level pLevel)
	{
		super(MSEntityTypes.POSTER.get(), pLevel);
	}
	
	public PosterEntity(Level level, BlockPos pos, ItemStack droppedItem)
	{
		this(level);
		this.pos = pos;
		this.droppedItem = droppedItem;
	}
	
	public PosterEntity(Level pLevel, BlockPos pPos, Direction pDirection, Holder<PaintingVariant> pVariant, ItemStack droppedItem)
	{
		super(pLevel, pPos, pDirection, pVariant);
		this.droppedItem = droppedItem;
	}
	
	public PosterEntity(EntityType<PosterEntity> hangingArtEntityEntityType, Level level)
	{
		super(hangingArtEntityEntityType, level);
	}
	
	/**
	 * Called when this entity is broken. Entity parameter may be null.
	 */
	@Override
	public void dropItem(@Nullable Entity pBrokenEntity) {
		if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
			this.playSound(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);
			if (pBrokenEntity instanceof Player player && player.hasInfiniteMaterials()) {
				return;
			}
			
			this.spawnAtLocation(droppedItem);
		}
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag pCompound)
	{
		droppedItem = ItemStack.parseOptional(registryAccess(), pCompound.getCompound("item"));
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag pCompound)
	{
		pCompound.put("item", droppedItem.save(registryAccess()));
	}
	
	private static int variantArea(Holder<PaintingVariant> p_218899_) {
		return p_218899_.value().area();
	}
	
	public static Optional<PosterEntity> createArt(Level pLevel, BlockPos pPos, Direction pDirection, ItemStack itemStack, TagKey<PaintingVariant> pool) {
		PosterEntity painting = new PosterEntity(pLevel, pPos, itemStack);
		List<Holder<PaintingVariant>> list = new ArrayList<>();
		pLevel.registryAccess().registryOrThrow(Registries.PAINTING_VARIANT).getTagOrEmpty(pool).forEach(list::add);
		if (list.isEmpty()) {
			return Optional.empty();
		} else {
			painting.setDirection(pDirection);
			list.removeIf(p_344343_ -> {
				painting.setVariant(p_344343_);
				return !painting.survives();
			});
			if (list.isEmpty()) {
				return Optional.empty();
			} else {
				int i = list.stream().mapToInt(PosterEntity::variantArea).max().orElse(0);
				list.removeIf(p_218883_ -> variantArea(p_218883_) < i);
				Optional<Holder<PaintingVariant>> optional = Util.getRandomSafe(list, painting.random);
				if (optional.isEmpty()) {
					return Optional.empty();
				} else {
					painting.setVariant(optional.get());
					painting.setDirection(pDirection);
					return Optional.of(painting);
				}
			}
		}
	}
	
	public ItemStack getItem()
	{
		return droppedItem;
	}
}
