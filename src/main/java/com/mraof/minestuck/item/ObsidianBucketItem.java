package com.mraof.minestuck.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;

public class ObsidianBucketItem extends Item
{
	public ObsidianBucketItem(Properties properties)
	{
		super(properties);
		DispenserBlock.registerBehavior(this, new OptionalDispenseItemBehavior()
		{
			@Override
			protected ItemStack execute(BlockSource source, ItemStack stack)
			{
				Direction direction = source.state().getValue(DispenserBlock.FACING);
				BlockPos blockpos = source.pos().relative(direction);
				this.setSuccess(((BlockItem)Items.OBSIDIAN).place(new DirectionalPlaceContext(source.level(), blockpos, direction, Items.OBSIDIAN.getDefaultInstance(), Direction.UP)).consumesAction());
				if (this.isSuccess()) return Items.BUCKET.getDefaultInstance();
				return super.execute(source, stack);
			}
		});
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		level.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		if(!playerIn.getInventory().add(new ItemStack(Blocks.OBSIDIAN)))
			if(!level.isClientSide)
				playerIn.drop(new ItemStack(Blocks.OBSIDIAN), false);
		
		return InteractionResultHolder.success(new ItemStack(Items.BUCKET));
	}
	
}