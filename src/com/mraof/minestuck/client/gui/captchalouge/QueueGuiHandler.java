package com.mraof.minestuck.client.gui.captchalouge;

import com.mraof.minestuck.inventory.captchalouge.Modus;

public class QueueGuiHandler extends StackGuiHandler {

	public QueueGuiHandler(Modus modus)
	{
		super(modus);
		textureIndex = 1;
	}
	
	@Override
	public void updateContent()
	{
		super.updateContent();
		if(!items.isEmpty())
		{
			items.get(0).index = -1;
			items.get(items.size() - 1).index = 0;
		}
	}
	
}
