package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.TitleSelectPacket;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.Title;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class TitleSelectorScreen extends Screen
{
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/title_selector.png");
	private static final int guiWidth = 186, guiHeight = 157;
	
	private EnumClass currentClass;
	private EnumAspect currentAspect;
	private Button[] classButtons = new Button[12], aspectButtons = new Button[12];
	private Button selectButton;
	
	private Title previous;
	private boolean sendPacket = true;
	
	TitleSelectorScreen(Title title)
	{
		super(new StringTextComponent("Title Selector"));
		previous = title;
	}
	
	@Override
	public void init()
	{
		for(EnumClass c : EnumClass.values())
		{
			int i = EnumClass.getIntFromClass(c);
			if(i < 12)
			{
				Button button = new GuiButtonExt((width - guiWidth) / 2 + 4 + (i % 2) * 40, (height - guiHeight) / 2 + 24 + (i / 2) * 16, 40, 16, c.getDisplayName(), button1 -> pickClass(c));
				addButton(button);
				classButtons[i] = button;
			}
		}
		for(EnumAspect a : EnumAspect.values())
		{
			int i = EnumAspect.getIntFromAspect(a);
			if(i < 12)
			{
				Button button = new GuiButtonExt((width - guiWidth) / 2 + 102 + (i % 2) * 40, (height - guiHeight) / 2 + 24 + (i / 2) * 16, 40, 16, a.getDisplayName(), button1 -> pickAspect(a));
				addButton(button);
				aspectButtons[i] = button;
			}
		}
		selectButton = new GuiButtonExt((width - guiWidth)/2 + 63, (height - guiHeight)/2 + 128, 60, 20, "Select", button -> select());
		addButton(selectButton);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		selectButton.active = currentClass != null && currentAspect != null;
		
		int xOffset = (width - guiWidth)/2;
		int yOffset = (height - guiHeight)/2;
		
		this.renderBackground();
		
		this.minecraft.getTextureManager().bindTexture(guiBackground);
		this.blit(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		String message = previous == null ? I18n.format("gui.selectTitle") : I18n.format("gui.selectTitle.used", previous.getTitleName());
		font.drawString(message, (this.width / 2) - font.getStringWidth(message) / 2, yOffset + 12, 0x404040);
		
		message = I18n.format("title.format", "", "");
		font.drawString(message, (this.width / 2) - font.getStringWidth(message) / 2, yOffset + 56 - font.FONT_HEIGHT/2, 0x404040);
		
		super.render(mouseX, mouseY, partialTicks);
		
	}
	
	private void pickClass(EnumClass c)
	{
		if(currentClass != null)
			classButtons[EnumClass.getIntFromClass(currentClass)].active = true;
		currentClass = c;
		classButtons[EnumClass.getIntFromClass(currentClass)].active = false;
	}
	
	private void pickAspect(EnumAspect a)
	{
		if(currentAspect != null)
			aspectButtons[EnumAspect.getIntFromAspect(currentAspect)].active = true;
		currentAspect = a;
		aspectButtons[EnumAspect.getIntFromAspect(currentAspect)].active = false;
	}
	
	private void select()
	{
		MSPacketHandler.sendToServer(new TitleSelectPacket(currentClass, currentAspect));
		sendPacket = false;
		minecraft.displayGuiScreen(null);
	}
	
	@Override
	public void onClose()
	{
		if(sendPacket)
			MSPacketHandler.sendToServer(new TitleSelectPacket());
	}
	
}