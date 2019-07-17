package com.mraof.minestuck.client.gui.playerStats;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.settings.MinestuckKeyHandler;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.LocalizedObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GuiDataChecker extends GuiScreen
{
	private static final ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/data_check.png");
	private static final int GUI_WIDTH = 210, GUI_HEIGHT = 140;
	private static final int LIST_Y = 25;
	
	public static IDataComponent activeComponent = null;
	private IDataComponent guiComponent;
	private GuiButton[] buttons = new GuiButton[5];
	private GuiButton returnButton, refreshButton;
	private int index;
	private float displayIndex;
	private boolean wasClicking, isScrolling;
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	@Override
	public void initGui()
	{
		int xOffset = (width - GUI_WIDTH)/2;
		int yOffset = (height - GUI_HEIGHT)/2;
		for(int i = 0; i < 5; i++)
		{
			GuiButton button = new GuiButton(i, xOffset + 5, yOffset + LIST_Y + i*22, 180, 20, "");
			buttons[i] = button;
			this.buttonList.add(button);
		}
		returnButton = new GuiButtonExt(5, xOffset + GUI_WIDTH - 25, yOffset + 5, 18, 18, "");
		this.buttonList.add(returnButton);
		refreshButton = new GuiButtonExt(6, xOffset + GUI_WIDTH - 45, yOffset + 5, 18, 18, "");
		this.buttonList.add(refreshButton);
		
		if(activeComponent == null)
			MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(MinestuckPacket.Type.DATA_CHECKER));
		
		componentChanged();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		int xOffset = (width - GUI_WIDTH)/2;
		int yOffset = (height - GUI_HEIGHT)/2;
		boolean canScroll = guiComponent != null && guiComponent.getComponentList().size() > 5 ? true : false;
		
		boolean mouseButtonDown = Mouse.isButtonDown(0);
		if(canScroll)
		{
			if(!wasClicking && mouseButtonDown && mouseX >= xOffset + 190 && mouseX < xOffset + 202 && mouseY >= yOffset + LIST_Y + 1 && mouseY < yOffset + LIST_Y + 102)
				isScrolling = true;
			else if(!mouseButtonDown)
				isScrolling = false;
			
			if(isScrolling)
			{
				displayIndex = (mouseY -  yOffset - 28.5F)/91;
				displayIndex = MathHelper.clamp(displayIndex, 0.0F, 1.0F);
				int newIndex = (int) ((guiComponent.getComponentList().size() - 5)*displayIndex + 0.5);
				if(newIndex != index)
				{
					index = newIndex;
					updateGuiButtons();
				}
			}
		} else isScrolling = false;
		wasClicking = mouseButtonDown;
		
		drawDefaultBackground();
		
		this.mc.getTextureManager().bindTexture(guiBackground);
		
		drawTexturedModalRect(xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		this.mc.getTextureManager().bindTexture(icons);
		
		if(this.returnButton.enabled)
			GlStateManager.color(1, 1, 1);
		else GlStateManager.color(.5F, .5F, .5F);
		drawTexturedModalRect(xOffset + GUI_WIDTH - 24, yOffset + 6, 240, 0, 16, 16);
		if(this.refreshButton.enabled)
			GlStateManager.color(1, 1, 1);
		else GlStateManager.color(.5F, .5F, .5F);
		drawTexturedModalRect(xOffset + GUI_WIDTH - 44, yOffset + 6, 224, 0, 16, 16);
		
		if(guiComponent != null)
		{
			List<IDataComponent> list = guiComponent.getComponentList();
			for(int i = 0; i < 5; i++)
			{
				 mc.fontRenderer.drawString(guiComponent.getName(), xOffset + 9, yOffset + 15 - mc.fontRenderer.FONT_HEIGHT/2, 0);
				IDataComponent component = i + index < list.size() ? list.get(i + index) : null;
				if(component != null && !component.isButton())
				{
					GlStateManager.color(1, 1, 1);
					this.mc.getTextureManager().bindTexture(guiBackground);
					drawTexturedModalRect(xOffset + 5, yOffset + LIST_Y + i*22, 0, 236, 180, 20);
					mc.fontRenderer.drawString(component.getName(), xOffset + 9, yOffset + LIST_Y + 10 - mc.fontRenderer.FONT_HEIGHT/2 + i*22, 0);
				}
			}
		} else mc.fontRenderer.drawString("Retrieving data from server...", xOffset + 9, yOffset + 15 - mc.fontRenderer.FONT_HEIGHT/2, 0);
		
		GlStateManager.color(1, 1, 1);
		int textureIndex = canScroll ? 232 : 244;
		this.mc.getTextureManager().bindTexture(guiBackground);
		drawTexturedModalRect((width - GUI_WIDTH)/2 + 190, (height - GUI_HEIGHT)/2 + LIST_Y + 1 + displayIndex*91, textureIndex, 0, 12, 15);
	}
	
	@Override
	public void updateScreen()
	{
		if(guiComponent != activeComponent)
			componentChanged();
		if(!MinestuckConfig.dataCheckerAccess)
			mc.displayGuiScreen(null);
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
			index = MathHelper.clamp(index, 0, size - 5);
			
			if(index != prevIndex)
			{
				displayIndex = index/((float) size - 5);
				updateGuiButtons();
			}
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(button.id >= 0 && button.id < 5)
		{
			int buttonIndex = index + button.id;
			if(buttonIndex < guiComponent.getComponentList().size() && guiComponent.getComponentList().get(buttonIndex) != null)
			{
				IDataComponent component = guiComponent.getComponentList().get(buttonIndex).onButtonPressed();
				if(component != null)
				{
					activeComponent = component;
					componentChanged();
				}
			}
		} else if(button.id == 5 && guiComponent != null && guiComponent.getParentComponent() != null)
		{
			activeComponent = guiComponent.getParentComponent();
			componentChanged();
		} else if(button.id == 6)
		{
			MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(MinestuckPacket.Type.DATA_CHECKER));
			activeComponent = null;
			componentChanged();
		}
	}
	
	public void componentChanged()
	{
		index = 0;
		displayIndex = 0F;
		guiComponent = activeComponent;
		returnButton.enabled = guiComponent != null && guiComponent.getParentComponent() != null;
		refreshButton.enabled = guiComponent != null;
		updateGuiButtons();
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
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);
		if(MinestuckKeyHandler.instance.statKey.isActiveAndMatches(keyCode))
			mc.displayGuiScreen(null);
	}
	
	public interface IDataComponent
	{
		public IDataComponent getParentComponent();
		
		public List<IDataComponent> getComponentList();
		
		public IDataComponent onButtonPressed();
		
		public boolean isButton();
		
		public String getName();
	}
	
	public static class TextField implements IDataComponent
	{
		String message;
		
		public TextField(String message, Object... args)
		{
			this(String.format(message, args));
		}
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
	
	public static class LocalizedTextField extends TextField
	{
		Object[] params;
		public LocalizedTextField(String message, Object... params)
		{
			super(message);
			this.params = params;
		}
		@Override
		public String getName()
		{
			return I18n.format(message, params);
		}
	}
	
	public static class MainComponent implements IDataComponent
	{
		List<IDataComponent> list = new ArrayList<IDataComponent>();
		
		public MainComponent(NBTTagCompound data)
		{
			if(data == null || data.hasNoTags())
				return;
			
			NBTTagList sessionList = data.getTagList("sessions", 10);
			int nameIndex = 1;
			for(int i = 0; i < sessionList.tagCount(); i++)
			{
				NBTTagCompound sessionTag = sessionList.getCompoundTagAt(i);
				SessionComponent session = new SessionComponent(this, sessionTag, data);
				if(sessionTag.hasKey("name", 8))
					session.name = sessionTag.getString("name");
				else
				{
					session.name = "Session " + String.valueOf(nameIndex);
					nameIndex++;
				}
				list.add(session);
			}
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
			return "Data Checker";	//Either "Data Checker" or "Sessions"
		}
	}
	
	public static class SessionComponent implements IDataComponent
	{
		List<IDataComponent> list = new ArrayList<IDataComponent>();
		MainComponent parent;
		String name;
		int players, playersEntered;
		
		public SessionComponent(MainComponent parent, NBTTagCompound sessionTag, NBTTagCompound dataTag)
		{
			this.parent = parent;
			HashSet<String> playerSet = new HashSet<String>();
			NBTTagList connectionList = sessionTag.getTagList("connections", 10);
			for(int i = 0; i < connectionList.tagCount(); i++)
			{
				ConnectionComponent connection = new ConnectionComponent(this, connectionList.getCompoundTagAt(i), dataTag);
				list.add(connection);
				
				if(connection.landDim != 0)
					playersEntered++;
				playerSet.add(connection.client);
				playerSet.add(connection.server);
			}
			
			playerSet.remove("");
			players = playerSet.size();
		}
		
		@Override
		public IDataComponent getParentComponent()
		{
			return parent;
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
			return String.format("%s (%d/%d)", name, playersEntered, players);
		}
	}
	
	public static class ConnectionComponent implements IDataComponent
	{
		List<IDataComponent> list = new ArrayList<IDataComponent>();
		SessionComponent parent;
		String client;
		String server;
		boolean isMain;
		int landDim;
		
		public ConnectionComponent(SessionComponent parent, NBTTagCompound connectionTag, NBTTagCompound dataTag)
		{
			this.parent = parent;
			this.client = connectionTag.getString("client");
			this.server = connectionTag.getString("server");
			this.isMain = connectionTag.getBoolean("isMain");
			if(isMain)
				landDim = connectionTag.getInteger("clientDim");
			
			list.add(new TextField("Client Player: %s", client));
			if(!server.isEmpty())
				list.add(new TextField("Server Player: %s", server));
			list.add(new TextField("Is Active: %b", connectionTag.getBoolean("isActive")));
			list.add(new TextField("Is Primary Connection: %b", isMain));
			
			list.add(null);
			if(isMain)
			{
				list.add(new TextField("Land dimension: %s", (landDim != 0 ? String.valueOf(landDim) : "Pre-entry")));
				if(landDim != 0 && connectionTag.hasKey("aspect1"))
					list.add(new LocalizedTextField("land.message.format", new LocalizedObject("land."+connectionTag.getString("aspect1")), new LocalizedObject("land."+connectionTag.getString("aspect2"))));
				if(connectionTag.hasKey("class"))
				{
					byte cl = connectionTag.getByte("class"), as = connectionTag.getByte("aspect");
					String titleClass = cl == -1 ? "Unknown" : "title."+EnumClass.values()[cl].toString();
					String titleAspect = as == -1 ? "Unknown" : "title."+EnumAspect.values()[as].toString();
					list.add(new LocalizedTextField("title.format", new LocalizedObject(titleClass), new LocalizedObject(titleAspect)));
				}
				
				if(connectionTag.hasKey("aspectTitle"))
					list.add(new TextField("Title aspect: %s", connectionTag.getString("aspectTitle")));
				if(connectionTag.hasKey("aspectTerrain"))
					list.add(new TextField("Terrain aspect: %s", connectionTag.getString("aspectTerrain")));
			}
			list.add(new GristCacheButton(connectionTag.getString("clientId")));
		}
		@Override
		public IDataComponent getParentComponent()
		{
			return parent;
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
			if(isMain)
				return String.format("'%s' - %s", client, server.isEmpty() ? '?' : '\'' + server + '\'');
			else return String.format("('%s' - '%s')", client, server);
		}
	}
	
	public static class GristCacheButton implements IDataComponent
	{
		String name;
		public GristCacheButton(String name)
		{
			this.name = name;
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
			GuiChat chat = new GuiChat("/grist @"+name+" get");
			Minecraft.getMinecraft().displayGuiScreen(chat);
			return null;
		}
		@Override
		public boolean isButton()
		{
			return true;
		}
		@Override
		public String getName()
		{
			return "View Grist Cache";
		}
	}
}