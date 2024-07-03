package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckMenu;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.network.CaptchaDeckPackets;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

public class CaptchaDeckScreen extends PlayerStatsContainerScreen<CaptchaDeckMenu>
{
	public static final String TITLE = "minestuck.captcha_deck";
	public static final String SYLLADEX = SylladexScreen.TITLE;
	public static final String USE_ITEM = "minestuck.captcha_deck.use_item";
	
	private static final ResourceLocation guiCaptchaDeck = new ResourceLocation("minestuck", "textures/gui/captcha_deck.png");
	
	private Button modusButton, sylladexMap;
	
	public CaptchaDeckScreen(int windowId, Inventory playerInventory)
	{
		super(new CaptchaDeckMenu(windowId, playerInventory), playerInventory, Component.translatable(TITLE));
		guiWidth = 178;
		guiHeight= 145;
	}
	
	@Override
	public void init()
	{
		super.init();
		modusButton = addRenderableWidget(new ExtendedButton(xOffset + 102, yOffset + 31, 50, 18, Component.translatable(USE_ITEM), button -> use()));
		sylladexMap = addRenderableWidget(new ExtendedButton(xOffset + 6, yOffset + 31, 60, 18, Component.translatable(SYLLADEX), button -> sylladex()));
		sylladexMap.active = ClientPlayerData.getModus() != null;
		modusButton.active = !menu.getMenuItem().isEmpty();
	}
	
	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
	{
		sylladexMap.active = ClientPlayerData.getModus() != null;
		modusButton.active = !menu.getMenuItem().isEmpty();
		
		drawTabs(guiGraphics);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		guiGraphics.blit(guiCaptchaDeck, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		drawActiveTabAndIcons(guiGraphics);
		
	}
	
	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		String message = getTitle().getString();
		guiGraphics.drawString(font, message, (this.width / 2F) - font.width(message) / 2F - leftPos, yOffset + 12 - topPos, 0x404040, false);
		
	}
	
	private void use() {
		ItemStack stack = menu.getMenuItem();
		if(!stack.isEmpty())
		{
			if(!(stack.getItem() instanceof CaptchaCardItem))
			{
				ModusType<?> type = ModusTypes.getTypeFromItem(stack.getItem());
				Modus newModus = type.createClientSide();
				Modus modus = ClientPlayerData.getModus();
				if(newModus != null && modus != null && newModus.getClass() != modus.getClass() && !newModus.canSwitchFrom(modus))
				{
					minecraft.screen = new ConfirmScreen(this::onConfirm, Component.translatable(SylladexScreen.EMPTY_SYLLADEX_1), Component.translatable(SylladexScreen.EMPTY_SYLLADEX_2))
					{
						@Override
						public void removed()
						{
							minecraft.screen = CaptchaDeckScreen.this;
							minecraft.player.closeContainer();
						}
					};
					minecraft.screen.init(minecraft, width, height);
					return;
				}
			}
			PacketDistributor.SERVER.noArg().send(new CaptchaDeckPackets.TriggerModusButton());
		}
	}
	
	private void sylladex()
	{
		if( ClientPlayerData.getModus() != null)
		{
			minecraft.player.connection.send(new ServerboundContainerClosePacket(minecraft.player.containerMenu.containerId));
			MSScreenFactories.displaySylladexScreen(ClientPlayerData.getModus());
			minecraft.player.containerMenu = minecraft.player.inventoryMenu;
		}
	}
	
	private void onConfirm(boolean result)
	{
		if(result && !menu.getMenuItem().isEmpty())
			PacketDistributor.SERVER.noArg().send(new CaptchaDeckPackets.TriggerModusButton());
		minecraft.screen = this;
	}
	
}
