package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.MinestuckScreen;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.computer.editmode.ClientEditmodeData;
import com.mraof.minestuck.network.MiscContainerPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public abstract class PlayerStatsScreen extends MinestuckScreen
{
	//TODO A better way of working with inventory-like guis like these?
	public static final int WINDOW_ID_START = 105;	//Note that window ids used MUST be a byte. (that's how the window id is serialized in minecraft's packets)
	
	public static final ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	public enum NormalGuiType
	{
		
		CAPTCHA_DECK(CaptchaDeckScreen::new, CaptchaDeckScreen.TITLE, false),
		STRIFE_SPECIBUS(StrifeSpecibusScreen::new, StrifeSpecibusScreen.TITLE, false),
		ECHELADDER(EcheladderScreen::new, EcheladderScreen.TITLE, true),
		GRIST_CACHE(GristCacheScreen::new, GristCacheScreen.TITLE, true);
		
		final Supplier<? extends Screen> factory;
		final BiFunction<Integer, Inventory, ? extends AbstractContainerScreen<?>> factory2;
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
		
		NormalGuiType(BiFunction<Integer, Inventory, ? extends AbstractContainerScreen<?>> factory, String name, boolean reqMedium)
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
			return factory2.apply(windowId, Minecraft.getInstance().player.getInventory());
		}
		
		public boolean reqMedium()
		{
			if(this == ECHELADDER)
				return MinestuckConfig.SERVER.preEntryRungLimit.get() == 0;
			else return this.reqMedium;
		}
		
	}
	
	public enum EditmodeGuiType
	{
		DEPLOY_LIST(InventoryEditmodeScreen::new, InventoryEditmodeScreen.TITLE),
		GRIST_CACHE(GristCacheScreen::new, GristCacheScreen.TITLE),
		ATHENEUM(AtheneumScreen::new, AtheneumScreen.TITLE);
		
		final Supplier<? extends Screen> factory;
		final BiFunction<Integer, Inventory, ? extends AbstractContainerScreen<?>> factory2;
		final String name;
		final boolean isContainer;
		
		EditmodeGuiType(Supplier<? extends Screen> factory, String name)
		{
			this.factory = factory;
			this.factory2 = null;
			this.name = name;
			this.isContainer = false;
		}
		
		EditmodeGuiType(BiFunction<Integer, Inventory, ? extends AbstractContainerScreen<?>> factory, String name)
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
			return factory2.apply(windowId, Minecraft.getInstance().player.getInventory());
		}
	}
	
	static final int tabWidth = 28, tabHeight = 32, tabOverlap = 4;
	
	public static NormalGuiType normalTab = NormalGuiType.CAPTCHA_DECK;
	public static EditmodeGuiType editmodeTab = EditmodeGuiType.DEPLOY_LIST;
	
	public Minecraft mc;
	
	protected int guiWidth, guiHeight;
	protected int xOffset, yOffset;
	
	private boolean mode;
	
	public PlayerStatsScreen(Component titleIn)
	{
		super(titleIn);
		this.mode = !ClientEditmodeData.isInEditmode();
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
	
	protected void drawTabs(GuiGraphics guiGraphics)
	{
		RenderSystem.setShaderColor(1,1,1, 1);
		
		if(mode)
		{
			for(NormalGuiType type : NormalGuiType.values())
				if(type != normalTab && (!type.reqMedium() || SkaiaClient.hasPlayerEntered() || mc.gameMode.hasInfiniteItems()))
				{
					int i = type.ordinal();
					guiGraphics.blit(icons, xOffset + i*(tabWidth + 2), yOffset - tabHeight + tabOverlap, i==0? 0:tabWidth, 0, tabWidth, tabHeight);
				}
		} else
		{
			for(EditmodeGuiType type : EditmodeGuiType.values())
				if(type != editmodeTab)
				{
					int i = type.ordinal();
					guiGraphics.blit(icons, xOffset + i*(tabWidth + 2), yOffset - tabHeight + tabOverlap, i==0? 0:tabWidth, 0, tabWidth, tabHeight);
				}
		}
		
		if(ClientPlayerData.hasDataCheckerAccess())
			guiGraphics.blit(icons, xOffset + guiWidth - tabWidth, yOffset -tabHeight + tabOverlap, 2*tabWidth, 0, tabWidth, tabHeight);
	}
	
	protected void drawActiveTabAndOther(GuiGraphics guiGraphics, int xcor, int ycor)
	{
		RenderSystem.setShaderColor(1,1,1, 1);
		
		int index = (mode? normalTab:editmodeTab).ordinal();
		guiGraphics.blit(icons, xOffset + index*(tabWidth+2), yOffset - tabHeight + tabOverlap,
				index == 0? 0:tabWidth, tabHeight, tabWidth, tabHeight);
		
		for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
			if(!mode || !NormalGuiType.values()[i].reqMedium() || SkaiaClient.hasPlayerEntered() || mc.gameMode.hasInfiniteItems())
				guiGraphics.blit(icons, xOffset + (tabWidth - 16)/2 + (tabWidth+2)*i, yOffset - tabHeight + tabOverlap + 8, i*16, tabHeight*2 + (mode? 0:16), 16, 16);
		
		if(ClientPlayerData.hasDataCheckerAccess())
			guiGraphics.blit(icons, xOffset + guiWidth + (tabWidth - 16)/2 - tabWidth, yOffset - tabHeight + tabOverlap + 8, 5*16, tabHeight*2, 16, 16);
		
		if(ycor < yOffset && ycor > yOffset - tabHeight + 4)
			for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
				if(xcor < xOffset + i*(tabWidth + 2))
					break;
				else if(xcor < xOffset + i*(tabWidth + 2) + tabWidth
						&& (!mode || !NormalGuiType.values()[i].reqMedium() || SkaiaClient.hasPlayerEntered() || mc.gameMode.hasInfiniteItems()))
					guiGraphics.renderTooltip(font, Component.translatable(mode? NormalGuiType.values()[i].name:EditmodeGuiType.values()[i].name),
							xcor, ycor);
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
					if(mode && NormalGuiType.values()[i].reqMedium() && !SkaiaClient.hasPlayerEntered() && mc.gameMode.hasMissTime())
						return true;
					mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
					if(i != (mode? normalTab:editmodeTab).ordinal())
					{
						if(mode)
							normalTab = NormalGuiType.values()[i];
						else editmodeTab = EditmodeGuiType.values()[i];
						openGui(true);
					}
					return true;
				}
			if(ClientPlayerData.hasDataCheckerAccess() && xcor < xOffset + guiWidth && xcor >= xOffset + guiWidth - tabWidth)
			{
				mc.setScreen(new DataCheckerScreen());
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
			if(ClientPlayerData.hasDataCheckerAccess())
			{
				if(mc.screen instanceof DataCheckerScreen)
					mc.setScreen(null);
				else mc.setScreen(new DataCheckerScreen());
			}
			
			return;
		}
		
		if(reload || mc.screen == null)
		{
			if(mc.screen instanceof AbstractContainerScreen<?>)
			{
				mc.player.connection.send(new ServerboundContainerClosePacket(mc.player.containerMenu.containerId));
				mc.player.containerMenu.setCarried(ItemStack.EMPTY);
			}
			if(ClientEditmodeData.isInEditmode() ? editmodeTab.isContainer : normalTab.isContainer)
			{
				int ordinal = (ClientEditmodeData.isInEditmode() ? editmodeTab : normalTab).ordinal();
				int windowId = WINDOW_ID_START + ordinal;
				PlayerStatsContainerScreen<?> containerScreen = (PlayerStatsContainerScreen<?>) (ClientEditmodeData.isInEditmode() ? editmodeTab.createGuiInstance(windowId) : normalTab.createGuiInstance(windowId));
				
				mc.setScreen(containerScreen);
				if(mc.screen == containerScreen)
					PacketDistributor.SERVER.noArg().send(new MiscContainerPacket(ordinal, ClientEditmodeData.isInEditmode()));
			}
			else mc.setScreen(ClientEditmodeData.isInEditmode() ? editmodeTab.createGuiInstance():normalTab.createGuiInstance());
		}
		else if(mc.screen instanceof PlayerStatsScreen || mc.screen instanceof PlayerStatsContainerScreen)
			mc.setScreen(null);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(MSKeyHandler.statKey.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode)))
		{
			mc.setScreen(null);
			return true;
		}
		else return super.keyPressed(keyCode, scanCode, i);
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(ClientPlayerNetworkEvent.LoggingIn event)
	{
		normalTab = NormalGuiType.CAPTCHA_DECK;
		editmodeTab = EditmodeGuiType.DEPLOY_LIST;
		DataCheckerScreen.activeComponent = null;
		EcheladderScreen.lastRung = -1;
		EcheladderScreen.animatedRung = 0;
	}
}