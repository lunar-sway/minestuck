package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen.*;
import com.mraof.minestuck.client.settings.MSKeyHandler;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;

import java.util.Arrays;

import static com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen.*;

public abstract class PlayerStatsContainerScreen<T extends Container> extends ContainerScreen<T>
{
	
	protected int guiWidth, guiHeight;
	protected int xOffset, yOffset;
	
	private boolean mode;
	
	public PlayerStatsContainerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
		this.mode = !ClientEditHandler.isActive();
	}
	
	@Override
	public void init()
	{
		super.init();
		minecraft.player.openContainer = this.container;
		
		xOffset = (width - guiWidth)/2;
		yOffset = (height - guiHeight + tabHeight - tabOverlap)/2;
		this.guiTop = yOffset;
		this.guiLeft = xOffset;
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	protected void drawTabs()
	{
		GlStateManager.color3f(1,1,1);
		
		minecraft.getTextureManager().bindTexture(PlayerStatsScreen.icons);
		
		if(mode)
		{
			for(NormalGuiType type : NormalGuiType.values())
				if(type != normalTab && (!type.reqMedium() || SkaiaClient.enteredMedium(SkaiaClient.playerId) || minecraft.playerController.isInCreativeMode()))
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
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	protected void drawActiveTabAndIcons()
	{
		GlStateManager.color3f(1,1,1);
		
		minecraft.getTextureManager().bindTexture(PlayerStatsScreen.icons);
		
		int index = (mode? normalTab:editmodeTab).ordinal();
		blit(xOffset + index*(tabWidth+2), yOffset - tabHeight + tabOverlap,
				index == 0? 0:tabWidth, tabHeight, tabWidth, tabHeight);
		
		for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
			if(!mode || !NormalGuiType.values()[i].reqMedium() || SkaiaClient.enteredMedium(SkaiaClient.playerId) || minecraft.playerController.isInCreativeMode())
				blit(xOffset + (tabWidth - 16)/2 + (tabWidth+2)*i, yOffset - tabHeight + tabOverlap + 8, i*16, tabHeight*2 + (mode? 0:16), 16, 16);
		
		if(MinestuckConfig.dataCheckerAccess)
			blit(xOffset + guiWidth + (tabWidth - 16)/2 - tabWidth, yOffset - tabHeight + tabOverlap + 8, 5*16, tabHeight*2, 16, 16);
	}
	
	protected void drawTabTooltip(int xcor, int ycor)
	{
		
		GlStateManager.disableDepthTest();
		if(ycor < yOffset && ycor > yOffset - tabHeight + 4)
			for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
				if(xcor < xOffset + i*(tabWidth + 2))
					break;
				else if(xcor < xOffset + i*(tabWidth + 2) + tabWidth
						&& (!mode || !NormalGuiType.values()[i].reqMedium() || SkaiaClient.enteredMedium(SkaiaClient.playerId) || minecraft.playerController.isInCreativeMode()))
					renderTooltip(Arrays.asList(I18n.format(mode? NormalGuiType.values()[i].name:EditmodeGuiType.values()[i].name)),
							xcor - guiLeft, ycor - guiTop, font);
		GlStateManager.enableDepthTest();
		GlStateManager.disableLighting();
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
					if(mode && NormalGuiType.values()[i].reqMedium() && !SkaiaClient.enteredMedium(SkaiaClient.playerId) && minecraft.playerController.isNotCreative())
						return true;
					minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
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
				minecraft.displayGuiScreen(new DataCheckerScreen());
				return true;
			}
		}
		return super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(MSKeyHandler.instance.statKey.isActiveAndMatches(InputMappings.getInputByCode(keyCode, scanCode)))
		{
			minecraft.displayGuiScreen(null);
			return true;
		}
		else return super.keyPressed(keyCode, scanCode, i);
	}
}