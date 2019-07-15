package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class MinestuckGuiFactory //implements IModGuiFactory
{
	/*TODO Config gui how now?
	@Override
	public void initialize(Minecraft minecraftInstance)
	{}
	
	@Override
	public boolean hasConfigGui()
	{
		return true;
	}
	
	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new MinestuckConfigGui(parentScreen);
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
	
	public static class MinestuckConfigGui extends GuiConfig
	{

		public MinestuckConfigGui(GuiScreen parentScreen)
		{
			super(parentScreen, getConfigElements(), Minestuck.class.getAnnotation(Mod.class).modid(), false, false, I18n.format("minestuck.config.title"));
		}
		
		private static List<IConfigElement> getConfigElements()
		{
			Configuration c = MinestuckConfig.config;
			List<IConfigElement> list = new ArrayList<IConfigElement>();
			
			list.add(new ConfigElement(c.getCategory("IDs")));
			list.add(new ConfigElement(c.getCategory("Modus")));
			list.addAll(new ConfigElement(c.getCategory("General")).getChildElements());
			
			return list;
		}
		
	}*/
	
}