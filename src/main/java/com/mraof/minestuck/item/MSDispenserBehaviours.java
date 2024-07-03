package com.mraof.minestuck.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public final class MSDispenserBehaviours
{
	// Add dispenser behaviour for items with classes we do not own
	public static void registerBehaviours() {
		DispenserBlock.registerBehavior(MSItems.OIL_BUCKET.get(), DISPENSE_BUCKET);
		DispenserBlock.registerBehavior(MSItems.BLOOD_BUCKET.get(), DISPENSE_BUCKET);
		DispenserBlock.registerBehavior(MSItems.BRAIN_JUICE_BUCKET.get(), DISPENSE_BUCKET);
		DispenserBlock.registerBehavior(MSItems.WATER_COLORS_BUCKET.get(), DISPENSE_BUCKET);
		DispenserBlock.registerBehavior(MSItems.ENDER_BUCKET.get(), DISPENSE_BUCKET);
		DispenserBlock.registerBehavior(MSItems.LIGHT_WATER_BUCKET.get(), DISPENSE_BUCKET);
	}
	
	// mostly copied from a private field in DispenseItemBehaviour
	static DefaultDispenseItemBehavior DISPENSE_BUCKET = new DefaultDispenseItemBehavior()
	{
		@Override
		protected ItemStack execute(BlockSource source, ItemStack stack)
		{
			DispensibleContainerItem item = (DispensibleContainerItem) stack.getItem();
			BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
			Level level = source.level();
			if (item.emptyContents(null, level, blockpos, null, stack)) {
				item.checkExtraContent(null, level, stack, blockpos);
				return Items.BUCKET.getDefaultInstance();
			} else {
				return super.execute(source, stack);
			}
		}
	};
}
