package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.MinestuckScreen;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.MiscContainerPacket;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CCloseWindowPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public abstract class PlayerStatsScreen extends MinestuckScreen
{
	//TODO A better way of working with inventory-like guis like these?
	public static final int WINDOW_ID_START = 105;	//Note that window ids used MUST be a byte. (that's how the window id is serialized in minecraft's packets)
	
	public static final ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	public enum NormalGuiType
	{
		
		CAPTCHA_DECK((MenuScreenFactory) CaptchaDeckScreen::new, CaptchaDeckScreen.TITLE, false),
		STRIFE_SPECIBUS((ScreenFactory) StrifeSpecibusScreen::new, StrifeSpecibusScreen.TITLE, false),
		ECHELADDER((ScreenFactory) EcheladderScreen::new, EcheladderScreen.TITLE, true),
		GRIST_CACHE((ScreenFactory) GristCacheScreen::new, GristCacheScreen.TITLE, true);
		
		private final TabAction action;
		final String name;
		final boolean reqMedium;
		
		NormalGuiType(TabAction factory, String name, boolean reqMedium)
		{
			this.action = factory;
			this.name = name;
			this.reqMedium = reqMedium;
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
		DEPLOY_LIST((MenuScreenFactory) InventoryEditmodeScreen::new, InventoryEditmodeScreen.TITLE),
		//BLOCK_LIST(GuiInventoryEditmode.class, "gui.blockList.name", true, false),
		GRIST_CACHE((ScreenFactory) GristCacheScreen::new, GristCacheScreen.TITLE);
		
		private final TabAction action;
		final String name;
		
		EditmodeGuiType(TabAction factory, String name)
		{
			this.action = factory;
			this.name = name;
		}
	}
	
	public interface TabAction
	{
		void trigger(Minecraft mc);
	}
	
	public interface ScreenFactory extends TabAction
	{
		@Override
		default void trigger(Minecraft mc)
		{
			mc.setScreen(this.createScreen());
		}
		PlayerStatsScreen createScreen();
	}
	
	public interface MenuScreenFactory extends TabAction
	{
		@Override
		default void trigger(Minecraft mc)
		{
			if (mc.player != null)
			{
				int ordinal = (ClientEditHandler.isActive() ? editmodeTab : normalTab).ordinal();
				int windowId = WINDOW_ID_START + ordinal;
				PlayerStatsContainerScreen<?> containerScreen = createScreen(windowId, mc.player.inventory);
				
				mc.setScreen(containerScreen);
				if(mc.screen == containerScreen)
					MSPacketHandler.sendToServer(new MiscContainerPacket(ordinal, ClientEditHandler.isActive()));
			}
		}
		PlayerStatsContainerScreen<?> createScreen(int windowId, PlayerInventory inventory);
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
	
	protected void drawTabs(MatrixStack matrixStack)
	{
		RenderSystem.color3f(1,1,1);
		
		mc.getTextureManager().bind(icons);
		
		if(mode)
		{
			for(NormalGuiType type : NormalGuiType.values())
				if(type != normalTab && (!type.reqMedium() || SkaiaClient.enteredMedium(SkaiaClient.playerId) || mc.gameMode.hasInfiniteItems()))
				{
					int i = type.ordinal();
					blit(matrixStack, xOffset + i*(tabWidth + 2), yOffset - tabHeight + tabOverlap, i==0? 0:tabWidth, 0, tabWidth, tabHeight);
				}
		} else
		{
			for(EditmodeGuiType type : EditmodeGuiType.values())
				if(type != editmodeTab)
				{
					int i = type.ordinal();
					blit(matrixStack, xOffset + i*(tabWidth + 2), yOffset - tabHeight + tabOverlap, i==0? 0:tabWidth, 0, tabWidth, tabHeight);
				}
		}
		
		if(ClientPlayerData.hasDataCheckerAccess())
			blit(matrixStack, xOffset + guiWidth - tabWidth, yOffset -tabHeight + tabOverlap, 2*tabWidth, 0, tabWidth, tabHeight);
	}
	
	protected void drawActiveTabAndOther(MatrixStack matrixStack, int xcor, int ycor)
	{
		RenderSystem.color3f(1,1,1);
		
		mc.getTextureManager().bind(icons);
		
		int index = (mode? normalTab:editmodeTab).ordinal();
		blit(matrixStack, xOffset + index*(tabWidth+2), yOffset - tabHeight + tabOverlap,
				index == 0? 0:tabWidth, tabHeight, tabWidth, tabHeight);
		
		for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
			if(!mode || !NormalGuiType.values()[i].reqMedium() || SkaiaClient.enteredMedium(SkaiaClient.playerId) || mc.gameMode.hasInfiniteItems())
				blit(matrixStack, xOffset + (tabWidth - 16)/2 + (tabWidth+2)*i, yOffset - tabHeight + tabOverlap + 8, i*16, tabHeight*2 + (mode? 0:16), 16, 16);
		
		if(ClientPlayerData.hasDataCheckerAccess())
			blit(matrixStack, xOffset + guiWidth + (tabWidth - 16)/2 - tabWidth, yOffset - tabHeight + tabOverlap + 8, 5*16, tabHeight*2, 16, 16);
		
		RenderSystem.disableRescaleNormal();
		RenderHelper.turnOff();
		RenderSystem.disableLighting();
		RenderSystem.disableDepthTest();
		
		if(ycor < yOffset && ycor > yOffset - tabHeight + 4)
			for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
				if(xcor < xOffset + i*(tabWidth + 2))
					break;
				else if(xcor < xOffset + i*(tabWidth + 2) + tabWidth
						&& (!mode || !NormalGuiType.values()[i].reqMedium() || SkaiaClient.enteredMedium(SkaiaClient.playerId) || mc.gameMode.hasInfiniteItems()))
					renderTooltip(matrixStack, new TranslationTextComponent(mode? NormalGuiType.values()[i].name:EditmodeGuiType.values()[i].name),
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
					if(mode && NormalGuiType.values()[i].reqMedium() && !SkaiaClient.enteredMedium(SkaiaClient.playerId) && mc.gameMode.hasMissTime())
						return true;
					mc.getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
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
			if(mc.screen instanceof ContainerScreen<?>)
			{
				mc.player.connection.send(new CCloseWindowPacket(mc.player.containerMenu.containerId));
				mc.player.inventory.setCarried(ItemStack.EMPTY);
			}
			
			(ClientEditHandler.isActive() ? editmodeTab.action : normalTab.action).trigger(mc);
		} else if(mc.screen instanceof PlayerStatsScreen || mc.screen instanceof PlayerStatsContainerScreen)
			mc.setScreen(null);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(MSKeyHandler.statKey.isActiveAndMatches(InputMappings.getKey(keyCode, scanCode)))
		{
			mc.setScreen(null);
			return true;
		}
		else return super.keyPressed(keyCode, scanCode, i);
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(ClientPlayerNetworkEvent.LoggedInEvent event)
	{
		normalTab = NormalGuiType.CAPTCHA_DECK;
		editmodeTab = EditmodeGuiType.DEPLOY_LIST;
		DataCheckerScreen.activeComponent = null;
		EcheladderScreen.lastRung = -1;
		EcheladderScreen.animatedRung = 0;
	}
}