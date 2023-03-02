package com.mraof.minestuck.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;


public class GutterThumbDriveItem extends Item
{
	public GutterThumbDriveItem(Properties pProperties)
	{
		super(pProperties);
	}
	
/**	@Override
	public InteractionResult useOn(UseOnContext pContext)
	{
		if(pContext.getLevel().isClientSide)
		BlockPos posClicked = pContext.getClickedPos();
		return super.useOn(pContext);
	}
	**/
}

