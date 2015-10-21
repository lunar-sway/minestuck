package com.mraof.minestuck.client.gui.playerStats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GuiDataChecker extends GuiScreen
{
	
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/DataCheck.png");
	private static final int GUI_WIDTH = 210, GUI_HEIGHT = 135;
	
	public static IDataComponent activeComponent = null;
	private IDataComponent guiComponent;
	private GuiButton[] buttons = new GuiButton[5];
	private int index;
	private float displayIndex;
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	@Override
	public void initGui()
	{
		for(int i = 0; i < 5; i++)
		{
			GuiButton button = new GuiButton(i, (width - GUI_WIDTH)/2 + 5, (height - GUI_HEIGHT)/2 + 20 + i*22, 180, 20, "");
			buttons[i] = button;
			this.buttonList.add(button);
		}
		
		activeComponent = null;	//Remove after adding refresh button
//		if(activeComponent == null)
		MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(MinestuckPacket.Type.DATA_CHECKER));
		
		index = 0;
		displayIndex = 0F;
		guiComponent = activeComponent;
		updateGuiButtons();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		
		this.mc.getTextureManager().bindTexture(guiBackground);
		
		drawTexturedModalRect((width - GUI_WIDTH)/2, (height - GUI_HEIGHT)/2, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		this.mc.getTextureManager().bindTexture(guiBackground);
		if(guiComponent != null)
		{
			List<IDataComponent> list = guiComponent.getComponentList();
			for(int i = 0; i < 5; i++)
			{
				IDataComponent component = i + index < list.size() ? list.get(i + index) : null;
				if(component != null && !component.isButton())
				{
					GlStateManager.color(1, 1, 1);
					drawTexturedModalRect((width - GUI_WIDTH)/2 + 5, (height - GUI_HEIGHT)/2 + 20 + i*22, 0, 236, 180, 20);
					mc.fontRendererObj.drawString(component.getName(), (width - GUI_WIDTH)/2 + 9, (height - GUI_HEIGHT)/2 + 30 - mc.fontRendererObj.FONT_HEIGHT/2 + i*22, 0);
				}
			}
		}
		
		GlStateManager.color(1, 1, 1);
		int textureIndex = guiComponent != null && guiComponent.getComponentList().size() > 5 ? 232 : 244;
		drawTexturedModalRect((width - GUI_WIDTH)/2 + 190, (height - GUI_HEIGHT)/2 + 21 + displayIndex*91, textureIndex, 0, 12, 15);
	}
	
	@Override
	public void updateScreen()
	{
		if(guiComponent != activeComponent)
		{
			index = 0;
			displayIndex = 0F;
			guiComponent = activeComponent;
			updateGuiButtons();
		}
	}
	
	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		
		if(i != 0 && guiComponent != null)
		{
			int size = guiComponent.getComponentList().size();
			if(size <= 5)
				return;
			
			int prevIndex = index;
			if(i > 0)
				index -= 1;
			else index += 1;
			index = MathHelper.clamp_int(index, 0, size - 5);
			
			if(index != prevIndex)
			{
				displayIndex = index/((float) size - 5);
				updateGuiButtons();
			}
		}
	}
	
	public void updateGuiButtons()
	{
		if(guiComponent != null)
		{
			List<IDataComponent> components = guiComponent.getComponentList();
			
			for(int i = 0; i < 5; i++)
			{
				GuiButton button = buttons[i];
				IDataComponent component = i + index < components.size() ? components.get(i + index) : null;
				if(component != null && component.isButton())
				{
					button.visible = true;
					button.displayString = component.getName();
					
				} else button.visible = false;
			}
		} else for(GuiButton button : buttons)
			button.visible = false;
	}
	
	
	public static interface IDataComponent
	{
		public IDataComponent getParentComponent();
		
		public List<IDataComponent> getComponentList();
		
		public IDataComponent onButtonPressed();
		
		public boolean isButton();
		
		public String getName();
	}
	
	public static class TextField implements IDataComponent
	{
		private String message;
		public TextField(String message)
		{
			this.message = message;
		}
		
		@Override
		public IDataComponent getParentComponent()
		{
			return null;
		}
		@Override
		public List<IDataComponent> getComponentList()
		{
			return null;
		}
		@Override
		public IDataComponent onButtonPressed()
		{
			return null;
		}
		@Override
		public boolean isButton()
		{
			return false;
		}
		@Override
		public String getName()
		{
			return message;
		}
	}
	
	public static class MainComponent implements IDataComponent
	{
		List<IDataComponent> list = new ArrayList<IDataComponent>();
		
		public MainComponent(int[] sessionSizeData)
		{
			for(int i = 0; i < sessionSizeData.length/2; i++)
			{
				SessionComponent session = new SessionComponent(this, String.valueOf(i + 1), sessionSizeData[i*2], sessionSizeData[i*2 + 1]);
				list.add(session);
			}
			list.add(new SessionComponent(this, "Filler", 2, -1));
			list.add(new SessionComponent(this, "Filler", 3, 0));
			list.add(new SessionComponent(this, "Filler", -7, 8));
			list.add(new SessionComponent(this, "Filler", 1, 1));
		}
		
		@Override
		public IDataComponent getParentComponent()
		{
			return null;
		}
		@Override
		public List<IDataComponent> getComponentList()
		{
			return list;
		}
		@Override
		public IDataComponent onButtonPressed()
		{
			return this;
		}
		@Override
		public boolean isButton()
		{
			return true;
		}
		@Override
		public String getName()
		{
			return "main";
		}
	}
	
	public static class SessionComponent implements IDataComponent
	{
		MainComponent parent;
		String name;
		int players, playersEntered;
		
		public SessionComponent(MainComponent parent, String name, int players, int playersEntered)
		{
			this.parent = parent;
			this.name = name;
			this.players = players;
			this.playersEntered = playersEntered;
		}
		
		@Override
		public IDataComponent getParentComponent()
		{
			return parent;
		}
		@Override
		public List<IDataComponent> getComponentList()
		{
			return new ArrayList<IDataComponent>();
		}
		@Override
		public IDataComponent onButtonPressed()
		{
			return this;
		}
		@Override
		public boolean isButton()
		{
			return true;
		}
		@Override
		public String getName()
		{
			return String.format("Session %s (%d/%d)", name, playersEntered, players);
		}
	}
}