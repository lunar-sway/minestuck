package com.mraof.minestuck.item;

import com.mraof.minestuck.block.*;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class KeyItem extends Item
{
	public EnumKeyType keyID;
	
	public KeyItem(Properties builder, EnumKeyType keyName)
	{
		super(builder);
		this.keyID = keyName;
	}
	
	/*public EnumKeyType getKeyEnum()
	{
		return this.keyID;
	}*/
	
	/*@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
		BlockState blockstate = world.getBlockState(blockpos);
		if(blockstate.getBlock() == MSBlocks.DUNGEON_DOOR_INTERFACE && blockstate.get(DungeonDoorInterfaceBlock.KEY) != EnumKeyType.none)
		{
			ItemStack itemstack = context.getItem();
			if(!world.isRemote)
			{
			
			}
			
			//return ActionResultType.PASS;
		} else
		{
			return ActionResultType.PASS;
		}
		return ActionResultType.PASS;
	}*/
}
