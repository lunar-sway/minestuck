package com.mraof.minestuck.client.gui.captchalouge;

import com.mraof.minestuck.inventory.captchalogue.Modus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QueueSylladexScreen extends StackSylladexScreen
{

	public QueueSylladexScreen(Modus modus)
	{
		super(modus);
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
