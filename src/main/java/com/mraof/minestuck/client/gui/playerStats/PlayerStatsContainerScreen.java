package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen.*;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.computer.editmode.ClientEditmodeData;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import static com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen.*;

public abstract class PlayerStatsContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T>
{
	
	protected int guiWidth, guiHeight;
	protected int xOffset, yOffset;
	
	private boolean mode;
	
	public PlayerStatsContainerScreen(T screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn);
		this.mode = !ClientEditmodeData.isInEditmode();
	}
	
	@Override
	public void init()
	{
		super.init();
		minecraft.player.containerMenu = this.menu;
		
		xOffset = (width - guiWidth)/2;
		yOffset = (height - guiHeight + tabHeight - tabOverlap)/2;
		this.topPos = yOffset;
		this.leftPos = xOffset;
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
			NormalGuiType[] visible = visibleNormalTabs();
			for(NormalGuiType type : visible)
				if(type != normalTab)
				{
					int i = type.ordinal();
					TabSprites.TabSprite sprite = type.getSpritePool().get(positionOf(visible, type), false);
					guiGraphics.blit(PlayerStatsScreen.icons, xOffset + i*(tabWidth + 2), yOffset - tabHeight + tabOverlap, sprite.u(), sprite.v(), tabWidth, tabHeight);
				}
		} else
		{
			EditmodeGuiType[] visible = EditmodeGuiType.values();
			for(EditmodeGuiType type : visible)
				if(type != editmodeTab)
				{
					int i = type.ordinal();
					TabSprites.TabSprite sprite = type.getSpritePool().get(positionOf(visible, type), false);
					guiGraphics.blit(PlayerStatsScreen.icons, xOffset + i*(tabWidth + 2), yOffset - tabHeight + tabOverlap, sprite.u(), sprite.v(), tabWidth, tabHeight);
				}
		}
		
		if(ClientPlayerData.hasDataCheckerAccess())
			guiGraphics.blit(PlayerStatsScreen.icons, xOffset + guiWidth - tabWidth, yOffset -tabHeight + tabOverlap, 2*tabWidth, 0, tabWidth, tabHeight);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
		drawTabTooltip(guiGraphics, mouseX, mouseY);
	}
	
	protected void drawActiveTabAndIcons(GuiGraphics guiGraphics)
	{
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		int index = (mode? normalTab:editmodeTab).ordinal();
		TabSprites.TabPosition activePos = mode ? positionOf(visibleNormalTabs(), normalTab) : positionOf(EditmodeGuiType.values(), editmodeTab);
		TabSprites.TabSprite activeSprite = (mode ? normalTab.getSpritePool() : editmodeTab.getSpritePool()).get(activePos, true);
		guiGraphics.blit(PlayerStatsScreen.icons, xOffset + index*(tabWidth+2), yOffset - tabHeight + tabOverlap,
				activeSprite.u(), activeSprite.v(), tabWidth, tabHeight);
		
		for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
			if(!mode || !NormalGuiType.values()[i].reqMedium() || SkaiaClient.hasPlayerEntered() || minecraft.gameMode.hasInfiniteItems())
				guiGraphics.blit(PlayerStatsScreen.icons, xOffset + (tabWidth - 16)/2 + (tabWidth+2)*i, yOffset - tabHeight + tabOverlap + 8, i*16, tabHeight*2 + (mode? 0:16), 16, 16);
		
		if(ClientPlayerData.hasDataCheckerAccess())
			guiGraphics.blit(PlayerStatsScreen.icons, xOffset + guiWidth + (tabWidth - 16)/2 - tabWidth, yOffset - tabHeight + tabOverlap + 8, 5*16, tabHeight*2, 16, 16);
	}
	
	protected void drawTabTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		
		RenderSystem.disableDepthTest();
		if(mouseY < yOffset && mouseY > yOffset - tabHeight + 4)
			for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
				if(mouseX < xOffset + i*(tabWidth + 2))
					break;
				else if(mouseX < xOffset + i*(tabWidth + 2) + tabWidth
						&& (!mode || !NormalGuiType.values()[i].reqMedium() || SkaiaClient.hasPlayerEntered() || minecraft.gameMode.hasInfiniteItems()))
					guiGraphics.renderTooltip(font, Component.translatable(mode ? NormalGuiType.values()[i].name : EditmodeGuiType.values()[i].name),
							mouseX, mouseY);
		RenderSystem.enableDepthTest();
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
					if(mode && NormalGuiType.values()[i].reqMedium() && !SkaiaClient.hasPlayerEntered() && minecraft.gameMode.hasMissTime())
						return true;
					minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
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
				minecraft.setScreen(new DataCheckerScreen());
				return true;
			}
		}
		return super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(MSKeyHandler.statKey.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode)))
		{
			minecraft.setScreen(null);
			return true;
		}
		else return super.keyPressed(keyCode, scanCode, i);
	}
	
	private NormalGuiType[] visibleNormalTabs()
	{
		return java.util.Arrays.stream(NormalGuiType.values())
				.filter(type -> !type.reqMedium() || SkaiaClient.hasPlayerEntered() || minecraft.gameMode.hasInfiniteItems())
				.toArray(NormalGuiType[]::new);
	}
	
	private static <T> TabSprites.TabPosition positionOf(T[] visible, T type)
	{
		int idx = java.util.Arrays.asList(visible).indexOf(type);
		if(idx <= 0) return TabSprites.TabPosition.LEFT;
		if(idx == visible.length - 1) return TabSprites.TabPosition.RIGHT;
		return TabSprites.TabPosition.MIDDLE;
	}
}