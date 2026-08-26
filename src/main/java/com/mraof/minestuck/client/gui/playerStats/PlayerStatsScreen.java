package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.client.gui.MSScreenFactories.NoModusFactoryException;
import com.mraof.minestuck.client.gui.MinestuckScreen;
import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.client.gui.playerStats.TabSprites.TabPosition;
import com.mraof.minestuck.client.gui.playerStats.TabSprites.TabSprite;
import com.mraof.minestuck.client.gui.playerStats.TabSprites.TabSpritePool;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public abstract class PlayerStatsScreen extends MinestuckScreen
{
	//TODO A better way of working with inventory-like guis like these?
	public static final int WINDOW_ID_START = 105;    //Note that window ids used MUST be a byte. (that's how the window id is serialized in minecraft's packets)
	
	public static final ResourceLocation icons = ResourceLocation.fromNamespaceAndPath("minestuck", "textures/gui/icons.png");
	
	static final int tabWidth = 28, tabHeight = 32, tabOverlap = 4;
	
	public static final TabSpritePool CLASSIC_POOL = new TabSpritePool(
			new TabSprite(0, 0), new TabSprite(tabWidth, 0), new TabSprite(tabWidth, 0),
			new TabSprite(0, tabHeight), new TabSprite(tabWidth, tabHeight), new TabSprite(tabWidth, tabHeight)
	);
	
	public static final TabSpritePool STRIFE_POOL = new TabSpritePool(
			new TabSprite(0, 192), new TabSprite(tabWidth, 192), new TabSprite(tabWidth * 2, 192),
			new TabSprite(0, 224), new TabSprite(tabWidth, 224), new TabSprite(tabWidth * 2, 224)
	);
	
	public static final TabSpritePool NEW_POOL = new TabSpritePool(
			new TabSprite(84, 192), new TabSprite(84 + tabWidth, 192), new TabSprite(84 + (tabWidth * 2), 192),
			new TabSprite(84, 224), new TabSprite(84 + tabWidth, 224), new TabSprite(84 + (tabWidth * 2), 224)
	);
	
	public enum NormalGuiType
	{
		
		CAPTCHA_DECK((windowId, inventory) -> {
			if(ClientPlayerData.getModus() != null)
			{
				try
				{
					return MSScreenFactories.displaySylladexScreen(ClientPlayerData.getModus(), windowId, inventory);
				} catch(NoModusFactoryException e)
				{
					e.printStackTrace();
					Minecraft.getInstance().screen = null;
				}
			}
			return null;
		}, SylladexScreen.TITLE, false, null),
		STRIFE_SPECIBUS(StrifeSpecibusScreen::new, StrifeSpecibusScreen.TITLE, false, STRIFE_POOL),
		ECHELADDER(EcheladderScreen::new, EcheladderScreen.TITLE, false, NEW_POOL),
		GRIST_CACHE(GristCacheScreen::new, GristCacheScreen.TITLE, true, NEW_POOL);
		
		final Supplier<? extends Screen> factory;
		final BiFunction<Integer, Inventory, ? extends AbstractContainerScreen<?>> factory2;
		final String name;
		final boolean isContainer;
		final boolean reqMedium;
		final TabSpritePool spritePool;
		
		NormalGuiType(Supplier<? extends Screen> factory, String name, boolean reqMedium, TabSpritePool spritePool)
		{
			this.factory = factory;
			this.factory2 = null;
			this.name = name;
			this.isContainer = false;
			this.reqMedium = reqMedium;
			this.spritePool = spritePool;
		}
		
		NormalGuiType(BiFunction<Integer, Inventory, ? extends AbstractContainerScreen<?>> factory, String name, boolean reqMedium, TabSpritePool spritePool)
		{
			this.factory = null;
			this.factory2 = factory;
			this.name = name;
			this.isContainer = true;
			this.reqMedium = reqMedium;
			this.spritePool = spritePool;
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
			return this.reqMedium;
		}
		
		public TabSpritePool getSpritePool()
		{
			return spritePool != null ? spritePool : CLASSIC_POOL;
		}
		
	}
	
	public enum EditmodeGuiType
	{
		DEPLOY_LIST(InventoryEditmodeScreen::new, InventoryEditmodeScreen.TITLE, null),
		GRIST_CACHE(GristCacheScreen::new, GristCacheScreen.TITLE, NEW_POOL),
		ATHENEUM(AtheneumScreen::new, AtheneumScreen.TITLE, null);
		
		final Supplier<? extends Screen> factory;
		final BiFunction<Integer, Inventory, ? extends AbstractContainerScreen<?>> factory2;
		final String name;
		final boolean isContainer;
		final TabSpritePool spritePool;
		
		EditmodeGuiType(Supplier<? extends Screen> factory, String name, TabSpritePool spritePool)
		{
			this.factory = factory;
			this.factory2 = null;
			this.name = name;
			this.isContainer = false;
			this.spritePool = spritePool;
		}
		
		EditmodeGuiType(BiFunction<Integer, Inventory, ? extends AbstractContainerScreen<?>> factory, String name, TabSpritePool spritePool)
		{
			this.factory = null;
			this.factory2 = factory;
			this.name = name;
			this.isContainer = true;
			this.spritePool = spritePool;
		}
		
		public Screen createGuiInstance()
		{
			return factory.get();
		}
		
		public Screen createGuiInstance(int windowId)
		{
			return factory2.apply(windowId, Minecraft.getInstance().player.getInventory());
		}
		
		public TabSpritePool getSpritePool()
		{
			return spritePool != null ? spritePool : CLASSIC_POOL;
		}
	}
	
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
		xOffset = (width - guiWidth) / 2;
		yOffset = (height - guiHeight + tabHeight - tabOverlap) / 2;
		mc = Minecraft.getInstance();
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	private NormalGuiType[] visibleNormalTabs()
	{
		return Arrays.stream(NormalGuiType.values())
				.filter(type -> !type.reqMedium() || SkaiaClient.hasPlayerEntered() || mc.gameMode.hasInfiniteItems())
				.toArray(NormalGuiType[]::new);
	}
	
	private static <T> TabPosition positionOf(T[] visible, T type)
	{
		int idx = Arrays.asList(visible).indexOf(type);
		if(idx <= 0) return TabPosition.LEFT;
		if(idx == visible.length - 1) return TabPosition.RIGHT;
		return TabPosition.MIDDLE;
	}
	
	protected void drawTabs(GuiGraphics guiGraphics)
	{
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		if(mode)
		{
			NormalGuiType[] visible = visibleNormalTabs();
			for(NormalGuiType type : visible)
				if(type != normalTab)
				{
					int i = type.ordinal();
					TabSprite sprite = type.getSpritePool().get(positionOf(visible, type), false);
					guiGraphics.blit(icons, xOffset + i * (tabWidth + 2), yOffset - tabHeight + tabOverlap, sprite.u(), sprite.v(), tabWidth, tabHeight);
				}
		} else
		{
			EditmodeGuiType[] visible = EditmodeGuiType.values();
			for(EditmodeGuiType type : visible)
				if(type != editmodeTab)
				{
					int i = type.ordinal();
					TabSprite sprite = type.getSpritePool().get(positionOf(visible, type), false);
					guiGraphics.blit(icons, xOffset + i * (tabWidth + 2), yOffset - tabHeight + tabOverlap, sprite.u(), sprite.v(), tabWidth, tabHeight);
				}
		}
		
		if(ClientPlayerData.hasDataCheckerAccess())
			guiGraphics.blit(icons, xOffset + guiWidth - tabWidth, yOffset - tabHeight + tabOverlap, 2 * tabWidth, 0, tabWidth, tabHeight);
	}
	
	protected void drawActiveTabAndOther(GuiGraphics guiGraphics, int xcor, int ycor)
	{
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		int index = (mode ? normalTab : editmodeTab).ordinal();
		TabPosition activePos = mode ? positionOf(visibleNormalTabs(), normalTab) : positionOf(EditmodeGuiType.values(), editmodeTab);
		TabSprite activeSprite = (mode ? normalTab.getSpritePool() : editmodeTab.getSpritePool()).get(activePos, true);
		guiGraphics.blit(icons, xOffset + index * (tabWidth + 2), yOffset - tabHeight + tabOverlap,
				activeSprite.u(), activeSprite.v(), tabWidth, tabHeight);
		
		for(int i = 0; i < (mode ? NormalGuiType.values() : EditmodeGuiType.values()).length; i++)
			if(!mode || !NormalGuiType.values()[i].reqMedium() || SkaiaClient.hasPlayerEntered() || mc.gameMode.hasInfiniteItems())
				guiGraphics.blit(icons, xOffset + (tabWidth - 16) / 2 + (tabWidth + 2) * i, yOffset - tabHeight + tabOverlap + 8, i * 16, tabHeight * 2 + (mode ? 0 : 16), 16, 16);
		
		if(ClientPlayerData.hasDataCheckerAccess())
			guiGraphics.blit(icons, xOffset + guiWidth + (tabWidth - 16) / 2 - tabWidth, yOffset - tabHeight + tabOverlap + 8, 5 * 16, tabHeight * 2, 16, 16);
		
		if(ycor < yOffset && ycor > yOffset - tabHeight + 4)
			for(int i = 0; i < (mode ? NormalGuiType.values() : EditmodeGuiType.values()).length; i++)
				if(xcor < xOffset + i * (tabWidth + 2))
					break;
				else if(xcor < xOffset + i * (tabWidth + 2) + tabWidth
						&& (!mode || !NormalGuiType.values()[i].reqMedium() || SkaiaClient.hasPlayerEntered() || mc.gameMode.hasInfiniteItems()))
					guiGraphics.renderTooltip(font, Component.translatable(mode ? NormalGuiType.values()[i].name : EditmodeGuiType.values()[i].name),
							xcor, ycor);
	}
	
	
	@Override
	public boolean mouseClicked(double xcor, double ycor, int mouseButton)
	{
		if(mouseButton == 0 && ycor < (height - guiHeight + tabHeight - tabOverlap) / 2 && ycor > (height - guiHeight - tabHeight + tabOverlap) / 2)
		{
			for(int i = 0; i < (mode ? NormalGuiType.values() : EditmodeGuiType.values()).length; i++)
				if(xcor < xOffset + i * (tabWidth + 2))
					break;
				else if(xcor < xOffset + i * (tabWidth + 2) + tabWidth)
				{
					if(mode && NormalGuiType.values()[i].reqMedium() && !SkaiaClient.hasPlayerEntered() && mc.gameMode.hasMissTime())
						return true;
					mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
					if(i != (mode ? normalTab : editmodeTab).ordinal())
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
					PacketDistributor.sendToServer(new MiscContainerPacket(ordinal, ClientEditmodeData.isInEditmode()));
			} else
				mc.setScreen(ClientEditmodeData.isInEditmode() ? editmodeTab.createGuiInstance() : normalTab.createGuiInstance());
		} else if(mc.screen instanceof PlayerStatsScreen || mc.screen instanceof PlayerStatsContainerScreen)
			mc.setScreen(null);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(MSKeyHandler.statKey.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode)))
		{
			mc.setScreen(null);
			return true;
		} else return super.keyPressed(keyCode, scanCode, i);
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(ClientPlayerNetworkEvent.LoggingIn event)
	{
		normalTab = NormalGuiType.CAPTCHA_DECK;
		editmodeTab = EditmodeGuiType.DEPLOY_LIST;
		DataCheckerScreen.nbt = new CompoundTag();
		EcheladderScreen.lastRung = -1;
		EcheladderScreen.animatedRung = 0;
	}
}