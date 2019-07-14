package com.mraof.minestuck.client.gui.playerStats;

import com.mraof.minestuck.client.gui.GuiButtonImpl;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.ContainerCaptchaDeck;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;

@OnlyIn(Dist.CLIENT)
public class GuiCaptchaDeck extends GuiPlayerStatsContainer implements GuiButtonImpl.ButtonClickhandler
{
	
	private static final ResourceLocation guiCaptchaDeck = new ResourceLocation("minestuck", "textures/gui/captcha_deck.png");
	
	private GuiButtonImpl modusButton, sylladexMap;
	private ContainerCaptchaDeck container;
	
	public GuiCaptchaDeck()
	{
		super(new ContainerCaptchaDeck(Minecraft.getInstance().player));
		container = (ContainerCaptchaDeck) inventorySlots;
		guiWidth = 178;
		guiHeight= 145;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		modusButton = new GuiButtonImpl(this, 1, xOffset + 102, yOffset + 31, 50, 18, I18n.format("gui.useItem"));
		sylladexMap = new GuiButtonImpl(this, 1, xOffset + 6, yOffset + 31, 60, 18, I18n.format("gui.sylladex"));
		addButton(modusButton);
		addButton(sylladexMap);
		sylladexMap.enabled = CaptchaDeckHandler.clientSideModus != null;
		modusButton.enabled = !container.inventory.getStackInSlot(0).isEmpty();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int xcor, int ycor)
	{
		sylladexMap.enabled = CaptchaDeckHandler.clientSideModus != null;
		modusButton.enabled = !container.inventory.getStackInSlot(0).isEmpty();
		
		drawTabs();
		
		mc.getTextureManager().bindTexture(guiCaptchaDeck);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		drawActiveTabAndIcons();
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int xcor, int ycor)
	{
		drawTabTooltip(xcor, ycor);
		
		String message = I18n.format("gui.captcha_deck.name");
		mc.fontRenderer.drawString(message, (this.width / 2) - mc.fontRenderer.getStringWidth(message) / 2 - guiLeft, yOffset + 12 - guiTop, 0x404040);
		
	}
	
	@Override
	public void actionPerformed(GuiButtonImpl button)
	{
		if(button == this.modusButton && !container.inventory.getStackInSlot(0).isEmpty())
		{
			ItemStack stack = container.inventory.getStackInSlot(0);
			if(!(stack.getItem() instanceof CaptchaCardItem))
			{
				Modus newModus = CaptchaDeckHandler.createInstance(CaptchaDeckHandler.getType(stack), LogicalSide.CLIENT);
				if(newModus != null && CaptchaDeckHandler.clientSideModus != null && newModus.getClass() != CaptchaDeckHandler.clientSideModus.getClass() && !newModus.canSwitchFrom(CaptchaDeckHandler.clientSideModus))
				{
					mc.currentScreen = new GuiYesNo(this, I18n.format("gui.emptySylladex1"), I18n.format("gui.emptySylladex2"), 0)
					{
						@Override
						public void onGuiClosed()
						{
							mc.currentScreen = (GuiScreen) parentScreen;
							mc.player.closeScreen();
						}
					};
					mc.currentScreen.setWorldAndResolution(mc, width, height);
					return;
				}
			}
			MinestuckPacketHandler.sendToServer(CaptchaDeckPacket.modus());
		}
		else if(button == this.sylladexMap && CaptchaDeckHandler.clientSideModus != null)
		{
			mc.player.connection.sendPacket(new CPacketCloseWindow(mc.player.openContainer.windowId));
			mc.player.inventory.setItemStack(ItemStack.EMPTY);
			mc.displayGuiScreen(CaptchaDeckHandler.clientSideModus.getGuiHandler());
			mc.player.openContainer = mc.player.inventoryContainer;
		}
	}
	
	@Override
	public void confirmResult(boolean result, int id)
	{
		if(result && !container.inventory.getStackInSlot(0).isEmpty())
			MinestuckPacketHandler.sendToServer(CaptchaDeckPacket.modus());
		mc.currentScreen = this;
	}
	
}
