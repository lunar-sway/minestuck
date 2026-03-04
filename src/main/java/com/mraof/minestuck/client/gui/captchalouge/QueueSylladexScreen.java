package com.mraof.minestuck.client.gui.captchalouge;

import com.mraof.minestuck.inventory.captchalogue.Modus;
import net.minecraft.world.entity.player.Inventory;

public class QueueSylladexScreen extends StackSylladexScreen
{
	public QueueSylladexScreen(int windowId, Inventory inventory, Modus modus)
	{
		super(windowId, inventory, modus);
		
		textureIndex = 1;
	}
	
	@Override
	public void updateContent()
	{
		super.updateContent();
		if(!cards.isEmpty())
		{
			cards.get(0).index = -1;
			cards.get(cards.size() - 1).index = 0;
		}
	}
}
