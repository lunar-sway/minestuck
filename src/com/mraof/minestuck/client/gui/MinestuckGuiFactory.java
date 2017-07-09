package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class MinestuckGuiFactory implements IModGuiFactory
{
	
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
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return MinestuckConfigGui.class;
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
	
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
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
		
	}
	
}