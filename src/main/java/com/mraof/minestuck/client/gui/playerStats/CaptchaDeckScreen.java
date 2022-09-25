package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckContainer;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class CaptchaDeckScreen extends PlayerStatsContainerScreen<CaptchaDeckContainer>
{
	public static final String TITLE = "minestuck.captcha_deck";
	public static final String SYLLADEX = SylladexScreen.TITLE;
	public static final String USE_ITEM = "minestuck.captcha_deck.use_item";
	
	private static final ResourceLocation guiCaptchaDeck = new ResourceLocation("minestuck", "textures/gui/captcha_deck.png");
	
	private Button modusButton, sylladexMap;
	
	public CaptchaDeckScreen(int windowId, Inventory playerInventory)
	{
		super(new CaptchaDeckContainer(windowId, playerInventory), playerInventory, new TranslatableComponent(TITLE));
		guiWidth = 178;
		guiHeight= 145;
	}
	
	@Override
	public void init()
	{
		super.init();
		modusButton = addRenderableWidget(new ExtendedButton(xOffset + 102, yOffset + 31, 50, 18, new TranslatableComponent(USE_ITEM), button -> use()));
		sylladexMap = addRenderableWidget(new ExtendedButton(xOffset + 6, yOffset + 31, 60, 18, new TranslatableComponent(SYLLADEX), button -> sylladex()));
		sylladexMap.active = ClientPlayerData.getModus() != null;
		modusButton.active = !menu.getContainerItem().isEmpty();
	}
	
	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
	{
		sylladexMap.active = ClientPlayerData.getModus() != null;
		modusButton.active = !menu.getContainerItem().isEmpty();
		
		drawTabs(poseStack);
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, guiCaptchaDeck);
		this.blit(poseStack, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		drawActiveTabAndIcons(poseStack);
		
	}
	
	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY)
	{
		String message = getTitle().getString();
		font.draw(poseStack, message, (this.width / 2F) - font.width(message) / 2F - leftPos, yOffset + 12 - topPos, 0x404040);
		
	}
	
	private void use() {
		ItemStack stack = menu.getContainerItem();
		if(!stack.isEmpty())
		{
			if(!(stack.getItem() instanceof CaptchaCardItem))
			{
				ModusType<?> type = ModusTypes.getTypeFromItem(stack.getItem());
				Modus newModus = type.createClientSide();
				Modus modus = ClientPlayerData.getModus();
				if(newModus != null && modus != null && newModus.getClass() != modus.getClass() && !newModus.canSwitchFrom(modus))
				{
					minecraft.screen = new ConfirmScreen(this::onConfirm, new TranslatableComponent(SylladexScreen.EMPTY_SYLLADEX_1), new TranslatableComponent(SylladexScreen.EMPTY_SYLLADEX_2))
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
			MSPacketHandler.sendToServer(CaptchaDeckPacket.modus());
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
		if(result && !menu.getContainerItem().isEmpty())
			MSPacketHandler.sendToServer(CaptchaDeckPacket.modus());
		minecraft.screen = this;
	}
	
}
