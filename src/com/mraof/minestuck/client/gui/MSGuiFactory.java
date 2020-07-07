package com.mraof.minestuck.client.gui;

public class MSGuiFactory //implements IModGuiFactory
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