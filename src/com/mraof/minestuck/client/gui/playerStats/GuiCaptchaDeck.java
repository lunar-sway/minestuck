package com.mraof.minestuck.client.gui.playerStats;

import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.ContainerCaptchaDeck;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.item.ItemModus;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.relauncher.Side;

public class GuiCaptchaDeck extends GuiPlayerStatsContainer implements GuiYesNoCallback
{
	
	private static final ResourceLocation guiCaptchaDeck = new ResourceLocation("minestuck", "textures/gui/captcha_deck.png");
	
	private GuiButton modusButton, sylladexMap;
	private ContainerCaptchaDeck container;
	
	public GuiCaptchaDeck()
	{
		super(new ContainerCaptchaDeck(Minecraft.getMinecraft().player));
		container = (ContainerCaptchaDeck) inventorySlots;
		guiWidth = 178;
		guiHeight= 145;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		modusButton = new GuiButtonExt(1, xOffset + 102, yOffset + 31, 50, 18, I18n.format("gui.useItem"));
		sylladexMap = new GuiButtonExt(1, xOffset + 6, yOffset + 31, 60, 18, I18n.format("gui.sylladex"));
		buttonList.add(modusButton);
		buttonList.add(sylladexMap);
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
		
		String message = I18n.format("gui.captchaDeck.name");
		mc.fontRenderer.drawString(message, (this.width / 2) - mc.fontRenderer.getStringWidth(message) / 2 - guiLeft, yOffset + 12 - guiTop, 0x404040);
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button == this.modusButton && !container.inventory.getStackInSlot(0).isEmpty())
		{
			ItemStack stack = container.inventory.getStackInSlot(0);
			if(stack.getItem() instanceof ItemModus)
			{
				Modus newModus = CaptchaDeckHandler.ModusType.getType(stack.getItemDamage()).createInstance(Side.CLIENT);
				if(CaptchaDeckHandler.clientSideModus != null && !newModus.canSwitchFrom(CaptchaDeckHandler.ModusType.getType(CaptchaDeckHandler.clientSideModus)))
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
			MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(Type.CAPTCHA, CaptchaDeckPacket.MODUS));
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
	public void confirmClicked(boolean result, int id)
	{
		if(result && !container.inventory.getStackInSlot(0).isEmpty())
			MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(Type.CAPTCHA, CaptchaDeckPacket.MODUS));
		mc.currentScreen = this;
	}
	
}
