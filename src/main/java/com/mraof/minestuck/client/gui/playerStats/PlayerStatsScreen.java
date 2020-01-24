package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.MinestuckScreen;
import com.mraof.minestuck.client.settings.MSKeyHandler;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.inventory.EditmodeContainer;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.MiscContainerPacket;
import com.mraof.minestuck.skaianet.SkaiaClient;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CCloseWindowPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public abstract class PlayerStatsScreen extends MinestuckScreen
{
	
	public static final ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	public enum NormalGuiType
	{
		
		CAPTCHA_DECK(CaptchaDeckScreen::new, CaptchaDeckScreen.TITLE, false),
		STRIFE_SPECIBUS(StrifeSpecibusScreen::new, StrifeSpecibusScreen.TITLE, false),
		ECHELADDER(EcheladderScreen::new, EcheladderScreen.TITLE, true),
		GRIST_CACHE(GristCacheScreen::new, GristCacheScreen.TITLE, true);
		
		final Supplier<? extends Screen> factory;
		final BiFunction<Integer, PlayerInventory, ? extends ContainerScreen<?>> factory2;
		final String name;
		final boolean isContainer;
		final boolean reqMedium;
		
		NormalGuiType(Supplier<? extends Screen> factory, String name, boolean reqMedium)
		{
			this.factory = factory;
			this.factory2 = null;
			this.name = name;
			this.isContainer = false;
			this.reqMedium = reqMedium;
		}
		
		NormalGuiType(BiFunction<Integer, PlayerInventory, ? extends ContainerScreen<?>> factory, String name, boolean reqMedium)
		{
			this.factory = null;
			this.factory2 = factory;
			this.name = name;
			this.isContainer = true;
			this.reqMedium = reqMedium;
		}
		
		public Screen createGuiInstance()
		{
			return factory.get();
		}
		
		public Screen createGuiInstance(int windowId)
		{
			return factory2.apply(windowId, Minecraft.getInstance().player.inventory);
		}
		
		public boolean reqMedium()
		{
			if(this == ECHELADDER)
				return !ClientPlayerData.echeladderAvailable;
			else return this.reqMedium;
		}
		
	}
	
	public enum EditmodeGuiType
	{
		DEPLOY_LIST(InventoryEditmodeScreen::new, InventoryEditmodeScreen.TITLE),
//		BLOCK_LIST(GuiInventoryEditmode.class, "gui.blockList.name", true, false),
		GRIST_CACHE(GristCacheScreen::new, GristCacheScreen.TITLE);
		
		final Supplier<? extends Screen> factory;
		final BiFunction<Integer, PlayerInventory, ? extends ContainerScreen<?>> factory2;
		final String name;
		final boolean isContainer;
		
		EditmodeGuiType(Supplier<? extends Screen> factory, String name)
		{
			this.factory = factory;
			this.factory2 = null;
			this.name = name;
			this.isContainer = false;
		}
		
		EditmodeGuiType(BiFunction<Integer, PlayerInventory, ? extends ContainerScreen<?>> factory, String name)
		{
			this.factory = null;
			this.factory2 = factory;
			this.name = name;
			this.isContainer = true;
		}
		
		public Screen createGuiInstance()
		{
			return factory.get();
		}
		
		public Screen createGuiInstance(int windowId)
		{
			return factory2.apply(windowId, Minecraft.getInstance().player.inventory);
		}
	}
	
	static final int tabWidth = 28, tabHeight = 32, tabOverlap = 4;
	
	public static NormalGuiType normalTab = NormalGuiType.CAPTCHA_DECK;
	public static EditmodeGuiType editmodeTab = EditmodeGuiType.DEPLOY_LIST;
	
	public Minecraft mc;
	
	protected int guiWidth, guiHeight;
	protected int xOffset, yOffset;
	
	private boolean mode;
	
	public PlayerStatsScreen(ITextComponent titleIn)
	{
		super(titleIn);
		this.mode = !ClientEditHandler.isActive();
	}
	
	@Override
	public void init()
	{
		super.init();
		xOffset = (width-guiWidth)/2;
		yOffset = (height-guiHeight+tabHeight-tabOverlap)/2;
		mc = Minecraft.getInstance();
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	protected void drawTabs()
	{
		GlStateManager.color3f(1,1,1);
		
		mc.getTextureManager().bindTexture(icons);
		
		if(mode)
		{
			for(NormalGuiType type : NormalGuiType.values())
				if(type != normalTab && (!type.reqMedium() || SkaiaClient.enteredMedium(SkaiaClient.playerId) || mc.playerController.isInCreativeMode()))
				{
					int i = type.ordinal();
					blit(xOffset + i*(tabWidth + 2), yOffset - tabHeight + tabOverlap, i==0? 0:tabWidth, 0, tabWidth, tabHeight);
				}
		} else
		{
			for(EditmodeGuiType type : EditmodeGuiType.values())
				if(type != editmodeTab)
				{
					int i = type.ordinal();
					blit(xOffset + i*(tabWidth + 2), yOffset - tabHeight + tabOverlap, i==0? 0:tabWidth, 0, tabWidth, tabHeight);
				}
		}
		
		if(MinestuckConfig.dataCheckerAccess)
			blit(xOffset + guiWidth - tabWidth, yOffset -tabHeight + tabOverlap, 2*tabWidth, 0, tabWidth, tabHeight);
	}
	
	protected void drawActiveTabAndOther(int xcor, int ycor)
	{
		GlStateManager.color3f(1,1,1);
		
		mc.getTextureManager().bindTexture(icons);
		
		int index = (mode? normalTab:editmodeTab).ordinal();
		blit(xOffset + index*(tabWidth+2), yOffset - tabHeight + tabOverlap,
				index == 0? 0:tabWidth, tabHeight, tabWidth, tabHeight);
		
		for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
			if(!mode || !NormalGuiType.values()[i].reqMedium() || SkaiaClient.enteredMedium(SkaiaClient.playerId) || mc.playerController.isInCreativeMode())
				blit(xOffset + (tabWidth - 16)/2 + (tabWidth+2)*i, yOffset - tabHeight + tabOverlap + 8, i*16, tabHeight*2 + (mode? 0:16), 16, 16);
		
		if(MinestuckConfig.dataCheckerAccess)
			blit(xOffset + guiWidth + (tabWidth - 16)/2 - tabWidth, yOffset - tabHeight + tabOverlap + 8, 5*16, tabHeight*2, 16, 16);
		
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepthTest();
		
		if(ycor < yOffset && ycor > yOffset - tabHeight + 4)
			for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
				if(xcor < xOffset + i*(tabWidth + 2))
					break;
				else if(xcor < xOffset + i*(tabWidth + 2) + tabWidth
						&& (!mode || !NormalGuiType.values()[i].reqMedium() || SkaiaClient.enteredMedium(SkaiaClient.playerId) || mc.playerController.isInCreativeMode()))
					renderTooltip(Arrays.asList(I18n.format(mode? NormalGuiType.values()[i].name:EditmodeGuiType.values()[i].name)),
							xcor, ycor, font);
	}
	
	
	@Override
	public boolean mouseClicked(double xcor, double ycor, int mouseButton)
	{
		if(mouseButton == 0 && ycor < (height - guiHeight + tabHeight - tabOverlap)/2 && ycor > (height - guiHeight - tabHeight + tabOverlap)/2)
		{
			for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
				if(xcor < xOffset + i*(tabWidth + 2))
					break;
				else if(xcor < xOffset + i*(tabWidth + 2) + tabWidth)
				{
					if(mode && NormalGuiType.values()[i].reqMedium() && !SkaiaClient.enteredMedium(SkaiaClient.playerId) && mc.playerController.isNotCreative())
						return true;
					mc.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
					if(i != (mode? normalTab:editmodeTab).ordinal())
					{
						if(mode)
							normalTab = NormalGuiType.values()[i];
						else editmodeTab = EditmodeGuiType.values()[i];
						openGui(true);
					}
					return true;
				}
			if(MinestuckConfig.dataCheckerAccess && xcor < xOffset + guiWidth && xcor >= xOffset + guiWidth - tabWidth)
			{
				mc.displayGuiScreen(new DataCheckerScreen());
				return true;
			}
		}
		return super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	public static void openGui(boolean reload)
	{
		Minecraft mc = Minecraft.getInstance();
		
		if(mc.player.isSpectator())
		{
			if(MinestuckConfig.dataCheckerAccess)
			{
				if(mc.currentScreen instanceof DataCheckerScreen)
					mc.displayGuiScreen(null);
				else mc.displayGuiScreen(new DataCheckerScreen());
			}
			
			return;
		}
		
		if(reload || mc.currentScreen == null)
		{
			if(mc.currentScreen instanceof ContainerScreen<?>)
			{
				mc.player.connection.sendPacket(new CCloseWindowPacket(mc.player.openContainer.windowId));
				mc.player.inventory.setItemStack(ItemStack.EMPTY);
			}
			if(ClientEditHandler.isActive() ? editmodeTab.isContainer : normalTab.isContainer)
			{
				int ordinal = (ClientEditHandler.isActive() ? editmodeTab : normalTab).ordinal();
				int windowId = 200 + ordinal;//ContainerHandler.clientWindowIdStart + ordinal;
				PlayerStatsContainerScreen containerScreen = (PlayerStatsContainerScreen) (ClientEditHandler.isActive() ? editmodeTab.createGuiInstance(windowId) : normalTab.createGuiInstance(windowId));
				
				mc.displayGuiScreen(containerScreen);
				if(mc.currentScreen == containerScreen)
					MSPacketHandler.sendToServer(new MiscContainerPacket(ordinal));
			}
			else mc.displayGuiScreen(ClientEditHandler.isActive()? editmodeTab.createGuiInstance():normalTab.createGuiInstance());
		}
		else if(mc.currentScreen instanceof PlayerStatsScreen || mc.currentScreen instanceof PlayerStatsContainerScreen)
			mc.displayGuiScreen(null);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(MSKeyHandler.statKey.isActiveAndMatches(InputMappings.getInputByCode(keyCode, scanCode)))
		{
			mc.displayGuiScreen(null);
			return true;
		}
		else return super.keyPressed(keyCode, scanCode, i);
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(ClientPlayerNetworkEvent.LoggedInEvent event)
	{
		normalTab = NormalGuiType.CAPTCHA_DECK;
		editmodeTab = EditmodeGuiType.DEPLOY_LIST;
		EditmodeContainer.clientScroll = 0;
		DataCheckerScreen.activeComponent = null;
		EcheladderScreen.lastRung = -1;
		EcheladderScreen.animatedRung = 0;
	}
}