package com.mraof.minestuck.client.gui.playerStats;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckContainer;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CCloseWindowPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class CaptchaDeckScreen extends PlayerStatsContainerScreen<CaptchaDeckContainer>
{
	public static final String TITLE = "minestuck.captcha_deck";
	public static final String SYLLADEX = SylladexScreen.TITLE;
	public static final String USE_ITEM = "minestuck.captcha_deck.use_item";
	
	private static final ResourceLocation guiCaptchaDeck = new ResourceLocation("minestuck", "textures/gui/captcha_deck.png");
	
	private Button modusButton, sylladexMap;
	
	public CaptchaDeckScreen(int windowId, PlayerInventory playerInventory)
	{
		super(new CaptchaDeckContainer(windowId, playerInventory), playerInventory, new TranslationTextComponent(TITLE));
		guiWidth = 178;
		guiHeight= 145;
	}
	
	@Override
	public void init()
	{
		super.init();
		modusButton = new ExtendedButton(xOffset + 102, yOffset + 31, 50, 18, I18n.format(USE_ITEM), button -> use());
		sylladexMap = new ExtendedButton(xOffset + 6, yOffset + 31, 60, 18, I18n.format(SYLLADEX), button -> sylladex());
		addButton(modusButton);
		addButton(sylladexMap);
		sylladexMap.active = ClientPlayerData.getModus() != null;
		modusButton.active = !container.inventory.getStackInSlot(0).isEmpty();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		sylladexMap.active = ClientPlayerData.getModus() != null;
		modusButton.active = !container.inventory.getStackInSlot(0).isEmpty();
		
		drawTabs();
		
		minecraft.getTextureManager().bindTexture(guiCaptchaDeck);
		this.blit(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		drawActiveTabAndIcons();
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String message = getTitle().getFormattedText();
		font.drawString(message, (this.width / 2F) - font.getStringWidth(message) / 2F - guiLeft, yOffset + 12 - guiTop, 0x404040);
		
	}
	
	private void use() {
		if(!container.inventory.getStackInSlot(0).isEmpty())
		{
			ItemStack stack = container.inventory.getStackInSlot(0);
			if(!(stack.getItem() instanceof CaptchaCardItem))
			{
				ModusType<?> type = ModusTypes.getTypeFromItem(stack.getItem());
				Modus newModus = type.createClientSide();
				Modus modus = ClientPlayerData.getModus();
				if(newModus != null && modus != null && newModus.getClass() != modus.getClass() && !newModus.canSwitchFrom(modus))
				{
					minecraft.currentScreen = new ConfirmScreen(this::onConfirm, new TranslationTextComponent(SylladexScreen.EMPTY_SYLLADEX_1), new TranslationTextComponent(SylladexScreen.EMPTY_SYLLADEX_2))
					{
						@Override
						public void onClose()
						{
							minecraft.currentScreen = CaptchaDeckScreen.this;
							minecraft.player.closeScreen();
						}
					};
					minecraft.currentScreen.init(minecraft, width, height);
					return;
				}
			}
			MSPacketHandler.sendToServer(CaptchaDeckPacket.modus());
		}
	}
	
	private void sylladex()
	{
		if( ClientPlayerData.getModus() != null)
		{
			minecraft.player.connection.sendPacket(new CCloseWindowPacket(minecraft.player.openContainer.windowId));
			minecraft.player.inventory.setItemStack(ItemStack.EMPTY);
			MSScreenFactories.displaySylladexScreen(ClientPlayerData.getModus());
			minecraft.player.openContainer = minecraft.player.container;
		}
	}
	
	private void onConfirm(boolean result)
	{
		if(result && !container.inventory.getStackInSlot(0).isEmpty())
			MSPacketHandler.sendToServer(CaptchaDeckPacket.modus());
		minecraft.currentScreen = this;
	}
	
}
