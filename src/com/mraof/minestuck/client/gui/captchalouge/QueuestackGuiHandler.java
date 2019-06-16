package com.mraof.minestuck.client.gui.captchalouge;

import com.mraof.minestuck.inventory.captchalogue.Modus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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
		for(int i = cards.size() - 1; i > 0; i--)
			if(!cards.get(i).item.isEmpty())
			{
				cards.get(i).index = 1;
				break;
			}
	}
}
