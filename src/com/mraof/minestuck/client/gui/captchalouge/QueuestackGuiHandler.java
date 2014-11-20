package com.mraof.minestuck.client.gui.captchalouge;

import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.util.Debug;

public class QueuestackGuiHandler extends StackGuiHandler
{
	
	public QueuestackGuiHandler(Modus modus)
	{
		super(modus);
		textureIndex = 2;
	}
	
	@Override
	public void updateContent()
	{
		super.updateContent();
		for(int i = items.size() - 1; i > 0; i--)
			if(items.get(i).item != null)
			{
				items.get(i).index = 1;
				break;
			}
	}
}
