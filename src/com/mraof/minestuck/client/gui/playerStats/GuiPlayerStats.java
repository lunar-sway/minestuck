package com.mraof.minestuck.client.gui.playerStats;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.inventory.ContainerHandler;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.util.UsernameHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public abstract class GuiPlayerStats extends GuiScreen
{
	
	static final ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	public static enum NormalGuiType
	{
		
		CAPTCHA_DECK(GuiCaptchaDeck.class, "gui.captchaDeck.name", true, false),
		STRIFE_SPECIBUS(GuiStrifeSpecibus.class, "gui.strifeSpecibus.name", false, false),
		GRIST_CACHE(GuiGristCache.class, "gui.gristCache.name", false, true),
		ECHELADDER(GuiEcheladder.class, "gui.echeladder.name", false, true);
		
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
		DEPLOY_LIST(GuiInventoryEditmode.class, "gui.deployList.name", true),
//		BLOCK_LIST(GuiInventoryEditmode.class, "gui.blockList.name", true, false),
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
		GlStateManager.color(1,1,1);
		
		mc.getTextureManager().bindTexture(icons);
		
		if(mode)
		{
			for(NormalGuiType type : NormalGuiType.values())
				if(type != normalTab && (!type.reqMedium || SkaiaClient.enteredMedium(UsernameHandler.encode(mc.thePlayer.getCommandSenderName())) || mc.playerController.isInCreativeMode()))
				{
					int i = type.ordinal();
					drawTexturedModalRect(xOffset + i*(tabWidth + 2), yOffset - tabHeight + tabOverlap, i==0? 0:tabWidth, 0, tabWidth, tabHeight);
				}
		} else
		{
			for(EditmodeGuiType type : EditmodeGuiType.values())
				if(type != editmodeTab)
				{
					int i = type.ordinal();
					drawTexturedModalRect(xOffset + i*(tabWidth + 2), yOffset - tabHeight + tabOverlap, i==0? 0:tabWidth, 0, tabWidth, tabHeight);
				}
		}
		
		if(MinestuckConfig.dataCheckerAccess)
			drawTexturedModalRect(xOffset + guiWidth - tabWidth, yOffset -tabHeight + tabOverlap, 2*tabWidth, 0, tabWidth, tabHeight);
	}
	
	protected void drawActiveTabAndOther(int xcor, int ycor)
	{
		GlStateManager.color(1,1,1);
		
		mc.getTextureManager().bindTexture(icons);
		
		int index = (mode? normalTab:editmodeTab).ordinal();
		drawTexturedModalRect(xOffset + index*(tabWidth+2), yOffset - tabHeight + tabOverlap,
				index == 0? 0:tabWidth, tabHeight, tabWidth, tabHeight);
		
		for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
			if(!mode || !NormalGuiType.values()[i].reqMedium || SkaiaClient.enteredMedium(UsernameHandler.encode(mc.thePlayer.getCommandSenderName())) || mc.playerController.isInCreativeMode())
				drawTexturedModalRect(xOffset + (tabWidth - 16)/2 + (tabWidth+2)*i, yOffset - tabHeight + tabOverlap + 8, i*16, tabHeight*2 + (mode? 0:16), 16, 16);
		
		if(MinestuckConfig.dataCheckerAccess)
			drawTexturedModalRect(xOffset + guiWidth + (tabWidth - 16)/2 - tabWidth, yOffset - tabHeight + tabOverlap + 8, 5*16, tabHeight*2, 16, 16);
		
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		
		if(ycor < yOffset && ycor > yOffset - tabHeight + 4)
			for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
				if(xcor < xOffset + i*(tabWidth + 2))
					break;
				else if(xcor < xOffset + i*(tabWidth + 2) + tabWidth
						&& (!mode || !NormalGuiType.values()[i].reqMedium || SkaiaClient.enteredMedium(UsernameHandler.encode(mc.thePlayer.getCommandSenderName())) || mc.playerController.isInCreativeMode()))
					drawHoveringText(Arrays.asList(StatCollector.translateToLocal(mode? NormalGuiType.values()[i].name:EditmodeGuiType.values()[i].name)),
							xcor, ycor, fontRendererObj);
	}
	
	@Override
	protected void mouseClicked(int xcor, int ycor, int mouseButton) throws IOException
	{
		if(mouseButton == 0 && ycor < (height - guiHeight + tabHeight - tabOverlap)/2 && ycor > (height - guiHeight - tabHeight + tabOverlap)/2)
		{
			for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
				if(xcor < xOffset + i*(tabWidth + 2))
					break;
				else if(xcor < xOffset + i*(tabWidth + 2) + tabWidth)
				{
					if(mode && NormalGuiType.values()[i].reqMedium && !SkaiaClient.enteredMedium(UsernameHandler.encode(mc.thePlayer.getCommandSenderName())) && mc.playerController.isNotCreative())
						return;
					mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
					if(i != (mode? normalTab:editmodeTab).ordinal())
					{
						if(mode)
							normalTab = NormalGuiType.values()[i];
						else editmodeTab = EditmodeGuiType.values()[i];
						openGui(true);
					}
					return;
				}
			if(MinestuckConfig.dataCheckerAccess && xcor < xOffset + guiWidth && xcor >= xOffset + guiWidth - tabWidth)
				mc.displayGuiScreen(new GuiDataChecker());
		}
		super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	public static void openGui(boolean reload)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(reload || mc.currentScreen == null)
		{
			if(mc.currentScreen != null && mc.currentScreen instanceof GuiContainer)
			{
				mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.openContainer.windowId));
				mc.thePlayer.inventory.setItemStack((ItemStack)null);
			}
			if(ClientEditHandler.isActive() ? editmodeTab.isContainer : normalTab.isContainer)
			{
				GuiPlayerStatsContainer guiContainer = (GuiPlayerStatsContainer) (ClientEditHandler.isActive() ? editmodeTab.createGuiInstance() : normalTab.createGuiInstance());
				int ordinal = (ClientEditHandler.isActive() ? editmodeTab : normalTab).ordinal();
				guiContainer.inventorySlots.windowId = ContainerHandler.clientWindowIdStart + ordinal;
				
				MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(Type.CONTAINER, ordinal));
				mc.displayGuiScreen(guiContainer);
			}
			else mc.displayGuiScreen(ClientEditHandler.isActive()? editmodeTab.createGuiInstance():normalTab.createGuiInstance());
		}
		else if(mc.currentScreen instanceof GuiPlayerStats || mc.currentScreen instanceof GuiPlayerStatsContainer)
			mc.displayGuiScreen(null);
	}
	
	protected boolean isPointInRegion(int regionX, int regionY, int regionWidth, int regionHeight, int pointX, int pointY) {
		return pointX >= regionX && pointX < regionX + regionWidth && pointY >= regionY && pointY < regionY + regionHeight;
	}
	
}
