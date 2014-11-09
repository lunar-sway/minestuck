package com.mraof.minestuck.client.gui.playerStats;

import static com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats.editmodeTab;
import static com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats.normalTab;
import static com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats.tabHeight;
import static com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats.tabOverlap;
import static com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats.tabWidth;

import java.lang.reflect.Constructor;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.inventory.ContainerHandler;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.UsernameHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public abstract class GuiPlayerStats extends GuiScreen
{
	
	static final ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	public static enum NormalGuiType
	{
		
		CAPTCHA_DECK(GuiCaptchaDeck.class, "gui.captchaDeck.name", true, false),
		STRIFE_SPECIBUS(GuiStrifeSpecibus.class, "gui.strifeSpecibus.name", false, false),
		GRIST_CACHE(GuiGristCache.class, "gui.gristCache.name", false, true);
//		ECHELADDER(GuiEcheladder.class, "gui.echeladder.name", false, true);
		
		final Class<? extends GuiScreen> guiClass;
		final String name;
		final boolean isContainer;
		final boolean reqMedium;
		final Object[] param;
		
		NormalGuiType(Class<? extends GuiScreen> guiClass, String name, boolean container, boolean reqMedium, Object... param)
		{
			this.guiClass = guiClass;
			this.name = name;
			this.isContainer = container;
			this.reqMedium = reqMedium;
			this.param = param.length == 0? null:param;
		}
		
		public GuiScreen createGuiInstance()
		{
			GuiScreen gui = null;
			try
			{
				if(param == null)
					gui = guiClass.newInstance();
				else
				{
					Class<?>[] paramClasses = new Class<?>[param.length];
					for(int i = 0; i < param.length; i++)
						paramClasses[i] = param[i].getClass();
					Constructor<? extends GuiScreen> constructor = guiClass.getConstructor(paramClasses);
					gui = constructor.newInstance(param);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return gui;
		}
		
	}
	
	public static enum EditmodeGuiType
	{
		DEPLOY_LIST(GuiInventoryEditmode.class, "gui.deployList.name", true, true),
		BLOCK_LIST(GuiInventoryEditmode.class, "gui.blockList.name", true, false),
		GRIST_CACHE(GuiGristCache.class, "gui.gristCache.name", false);
		
		final Class<? extends GuiScreen> guiClass;
		final String name;
		final boolean isContainer;
		final Object[] param;
		
		EditmodeGuiType(Class<? extends GuiScreen> guiClass, String name, boolean container, Object... param)
		{
			this.guiClass = guiClass;
			this.name = name;
			this.isContainer = container;
			this.param = param.length == 0? null:param;
		}
		
		public GuiScreen createGuiInstance()
		{
			GuiScreen gui = null;
			try
			{
				if(param == null)
					gui = guiClass.newInstance();
				else
				{
					Class<?>[] paramClasses = new Class<?>[param.length];
					for(int i = 0; i < param.length; i++)
						paramClasses[i] = param[i].getClass();
					Constructor<? extends GuiScreen> constructor = guiClass.getConstructor(paramClasses);
					gui = constructor.newInstance(param);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return gui;
		}
		
	}
	
	static final int tabWidth = 28, tabHeight = 32, tabOverlap = 4;
	
	protected static RenderItem itemRenderer = new RenderItem();
	
	public static NormalGuiType normalTab = NormalGuiType.CAPTCHA_DECK;
	public static EditmodeGuiType editmodeTab = EditmodeGuiType.DEPLOY_LIST;
	
	public Minecraft mc;
	
	protected int guiWidth, guiHeight;
	protected int xOffset, yOffset;
	
	private boolean mode;
	
	public GuiPlayerStats()
	{
		this.mode = !ClientEditHandler.isActive();
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		xOffset = (width-guiWidth)/2;
		yOffset = (height-guiHeight+tabHeight-tabOverlap)/2;
		mc = Minecraft.getMinecraft();
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	protected void drawTabs()
	{
		GL11.glColor3f(1,1,1);
		
		mc.getTextureManager().bindTexture(icons);
		
		if(mode)
		{
			for(NormalGuiType type : NormalGuiType.values())
				if(type != normalTab && (!type.reqMedium || SkaiaClient.enteredMedium(UsernameHandler.encode(mc.thePlayer.getCommandSenderName()))))
				{
					int i = type.ordinal();
					drawTexturedModalRect(xOffset + i*(tabWidth + 2), yOffset - tabHeight + tabOverlap, i==0? 0:tabWidth, 0, tabWidth, tabHeight);
				}
				
		}
		else
		{
			for(EditmodeGuiType type : EditmodeGuiType.values())
				if(type != editmodeTab)
				{
					int i = type.ordinal();
					drawTexturedModalRect(xOffset + i*(tabWidth + 2), yOffset - tabHeight + tabOverlap, i==0? 0:tabWidth, 0, tabWidth, tabHeight);
				}
		}
	}
	
	protected void drawActiveTabAndOther(int xcor, int ycor)
	{
		GL11.glColor3f(1,1,1);
		
		mc.getTextureManager().bindTexture(icons);
		
		int index = (mode? normalTab:editmodeTab).ordinal();
		drawTexturedModalRect(xOffset + index*(tabWidth+2), yOffset - tabHeight + tabOverlap,
				index == 0? 0:tabWidth, tabHeight, tabWidth, tabHeight);
		
		for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
			if(!mode || !NormalGuiType.values()[i].reqMedium || SkaiaClient.enteredMedium(UsernameHandler.encode(mc.thePlayer.getCommandSenderName())))
				drawTexturedModalRect(xOffset + (tabWidth - 16)/2 + (tabWidth+2)*i, yOffset - tabHeight + tabOverlap + 8, i*16, tabHeight*2 + (mode? 0:16), 16, 16);
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		if(ycor < yOffset && ycor > yOffset - tabHeight + 4)
			for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
				if(xcor < xOffset + i*(tabWidth + 2))
					break;
				else if(xcor < xOffset + i*(tabWidth + 2) + tabWidth
						&& (!mode || !NormalGuiType.values()[i].reqMedium || SkaiaClient.enteredMedium(UsernameHandler.encode(mc.thePlayer.getCommandSenderName()))))
					drawTooltip(StatCollector.translateToLocal(mode? NormalGuiType.values()[i].name:EditmodeGuiType.values()[i].name), xcor, ycor,
							EnumChatFormatting.WHITE);
	}
	
	@Override
	protected void mouseClicked(int xcor, int ycor, int mouseButton)
	{
		if(mouseButton == 0 && ycor < (height - guiHeight + tabHeight - tabOverlap)/2 && ycor > (height - guiHeight - tabHeight + tabOverlap)/2)
		{
			for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
				if(xcor < xOffset + i*(tabWidth + 2))
					break;
				else if(xcor < xOffset + i*(tabWidth + 2) + tabWidth)
				{
					if(mode && NormalGuiType.values()[i].reqMedium && !SkaiaClient.enteredMedium(UsernameHandler.encode(mc.thePlayer.getCommandSenderName())))
						return;
					mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
					if(i != (mode? normalTab:editmodeTab).ordinal())
					{
						if(mode)
							normalTab = NormalGuiType.values()[i];
						else editmodeTab = EditmodeGuiType.values()[i];
						openGui(true);
					}
					return;
				}
		}
		super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	public static void openGui(boolean reload)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(reload || mc.currentScreen == null)
			if(ClientEditHandler.isActive() ? editmodeTab.isContainer : normalTab.isContainer)
				MinestuckChannelHandler.sendToServer(
						MinestuckPacket.makePacket(Type.CONTAINER, (ClientEditHandler.isActive() ? editmodeTab : normalTab).ordinal()));
			else mc.displayGuiScreen(ClientEditHandler.isActive()? editmodeTab.createGuiInstance():normalTab.createGuiInstance());
		else if(mc.currentScreen instanceof GuiPlayerStats || mc.currentScreen instanceof GuiPlayerStatsContainer)
			mc.displayGuiScreen(null);
	}
	
	protected void drawTooltip(String text,int par2, int par3, EnumChatFormatting color) {
		String[] list = {text};
		
		for (int k = 0; k < list.length; ++k) {
			list[k] = color + list[k];
		}
		
		if (list.length != 0) {
			int k = mc.fontRenderer.getStringWidth(text);
			
			int i1 = par2 + 12;
			int j1 = par3 - 12;
			int k1 = 8;
			
			if (list.length > 1) {
				k1 += 2 + (list.length - 1) * 10;
			}
			
			if (i1 + k > this.width) {
				i1 -= 28 + k;
			}
			
			if (j1 + k1 + 6 > this.height) {
				j1 = this.height - k1 - 6;
			}
			
			this.zLevel = 300.0F;
			itemRenderer.zLevel = 300.0F;
			int l1 = -267386864;
			this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
			this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
			int i2 = 1347420415;
			int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
			this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
			this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);
			
			for (int k2 = 0; k2 < list.length; ++k2) {
				String s1 = list[k2];
				mc.fontRenderer.drawStringWithShadow(s1, i1, j1, -1);
				
				if (k2 == 0) {
					j1 += 2;
				}
				
				j1 += 10;
			}
			
			this.zLevel = 0.0F;
			itemRenderer.zLevel = 0.0F;
		}
	}
	
	protected boolean isPointInRegion(int regionX, int regionY, int regionWidth, int regionHeight, int pointX, int pointY) {
		return pointX >= regionX && pointX < regionX + regionWidth && pointY >= regionY && pointY < regionY + regionHeight;
	}
	
}
