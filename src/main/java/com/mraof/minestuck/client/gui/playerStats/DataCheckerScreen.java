package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.network.DataCheckerPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DataCheckerScreen extends Screen
{
	private static final ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/data_check.png");
	private static final int GUI_WIDTH = 210, GUI_HEIGHT = 140;
	private static final int LIST_Y = 25;
	
	public static IDataComponent activeComponent = null;
	private IDataComponent guiComponent;
	private Button[] contentButtons = new Button[5];
	private Button returnButton, refreshButton;
	private int index;
	private float displayIndex;
	private boolean isScrolling;
	
	public DataCheckerScreen()
	{
		super(new StringTextComponent("Data Checker"));
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	@Override
	public void init()
	{
		int xOffset = (width - GUI_WIDTH)/2;
		int yOffset = (height - GUI_HEIGHT)/2;
		for(int i = 0; i < 5; i++)
		{
			final int id = i;
			contentButtons[id] = addButton(new ExtendedButton(xOffset + 5, yOffset + LIST_Y + i*22, 180, 20, StringTextComponent.EMPTY, button -> contentButton(id)));
		}
		returnButton = addButton(new Button(xOffset + GUI_WIDTH - 25, yOffset + 5, 18, 18, StringTextComponent.EMPTY, button -> goBack()));
		refreshButton = addButton(new Button(xOffset + GUI_WIDTH - 45, yOffset + 5, 18, 18, StringTextComponent.EMPTY, button -> refresh()));
		
		if(activeComponent == null)
			MSPacketHandler.sendToServer(DataCheckerPacket.request());
		
		componentChanged();
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		int xOffset = (width - GUI_WIDTH)/2;
		int yOffset = (height - GUI_HEIGHT)/2;
		boolean canScroll = guiComponent != null && guiComponent.getComponentList().size() > 5 ? true : false;
		
		if(canScroll && isScrolling)
		{
			displayIndex = (mouseY - yOffset - 28.5F) / 91;
			displayIndex = MathHelper.clamp(displayIndex, 0.0F, 1.0F);
			int newIndex = (int) ((guiComponent.getComponentList().size() - 5) * displayIndex + 0.5);
			if(newIndex != index)
			{
				index = newIndex;
				updateGuiButtons();
			}
		}
		
		renderBackground(matrixStack);
		
		this.minecraft.getTextureManager().bind(guiBackground);
		
		blit(matrixStack, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		
		this.minecraft.getTextureManager().bind(icons);
		
		if(this.returnButton.active)
			RenderSystem.color3f(1, 1, 1);
		else RenderSystem.color3f(.5F, .5F, .5F);
		blit(matrixStack, xOffset + GUI_WIDTH - 24, yOffset + 6, 240, 0, 16, 16);
		if(this.refreshButton.active)
			RenderSystem.color3f(1, 1, 1);
		else RenderSystem.color3f(.5F, .5F, .5F);
		blit(matrixStack, xOffset + GUI_WIDTH - 44, yOffset + 6, 224, 0, 16, 16);
		
		if(guiComponent != null)
		{
			List<IDataComponent> list = guiComponent.getComponentList();
			for(int i = 0; i < 5; i++)
			{
				 font.draw(matrixStack, guiComponent.getName(), xOffset + 9, yOffset + 15 - font.lineHeight/2, 0);
				IDataComponent component = i + index < list.size() ? list.get(i + index) : null;
				if(component != null && !component.isButton())
				{
					RenderSystem.color3f(1, 1, 1);
					this.minecraft.getTextureManager().bind(guiBackground);
					blit(matrixStack, xOffset + 5, yOffset + LIST_Y + i*22, 0, 236, 180, 20);
					font.draw(matrixStack, component.getName(), xOffset + 9, yOffset + LIST_Y + 10 - font.lineHeight/2 + i*22, 0);
				}
			}
		} else font.draw(matrixStack, "Retrieving data from server...", xOffset + 9, yOffset + 15 - font.lineHeight/2, 0);

		RenderSystem.color3f(1, 1, 1);
		int textureIndex = canScroll ? 232 : 244;
		this.minecraft.getTextureManager().bind(guiBackground);
		blit(matrixStack, (width - GUI_WIDTH)/2 + 190, (height - GUI_HEIGHT)/2 + LIST_Y + 1 + (int) (displayIndex*91), textureIndex, 0, 12, 15);
	}
	
	@Override
	public void tick()
	{
		if(guiComponent != activeComponent)
			componentChanged();
		if(!ClientPlayerData.hasDataCheckerAccess())
			minecraft.setScreen(null);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scroll)
	{
		if(scroll != 0 && guiComponent != null)
		{
			int size = guiComponent.getComponentList().size();
			if(size <= 5)
				return super.mouseScrolled(mouseX, mouseY, scroll);
			
			int prevIndex = index;
			if(scroll > 0)
				index -= 1;
			else index += 1;
			index = MathHelper.clamp(index, 0, size - 5);
			
			if(index != prevIndex)
			{
				displayIndex = index/((float) size - 5);
				updateGuiButtons();
			}
			return true;
		} else return super.mouseScrolled(mouseX, mouseY, scroll);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		int xOffset = (width - GUI_WIDTH)/2;
		int yOffset = (height - GUI_HEIGHT)/2;
		if(mouseButton == 0 && mouseX >= xOffset + 190 && mouseX < xOffset + 202 && mouseY >= yOffset + LIST_Y + 1 && mouseY < yOffset + LIST_Y + 102)
		{
			isScrolling = true;
			return true;
		} else return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton)
	{
		if(isScrolling)
		{
			isScrolling = false;
			return true;
		}
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}
	
	private void contentButton(int id)
	{
		int buttonIndex = index + id;
		if(buttonIndex < guiComponent.getComponentList().size() && guiComponent.getComponentList().get(buttonIndex) != null)
		{
			IDataComponent component = guiComponent.getComponentList().get(buttonIndex).onButtonPressed();
			if(component != null)
			{
				activeComponent = component;
				componentChanged();
			}
		}
	}
	
	private void goBack()
	{
		if(guiComponent != null && guiComponent.getParentComponent() != null)
		{
			activeComponent = guiComponent.getParentComponent();
			componentChanged();
		}
	}
	
	private void refresh()
	{
		MSPacketHandler.sendToServer(DataCheckerPacket.request());
		activeComponent = null;
		componentChanged();
	}
	
	public void componentChanged()
	{
		index = 0;
		displayIndex = 0F;
		guiComponent = activeComponent;
		returnButton.active = guiComponent != null && guiComponent.getParentComponent() != null;
		refreshButton.active = guiComponent != null;
		updateGuiButtons();
	}
	
	public void updateGuiButtons()
	{
		if(guiComponent != null)
		{
			List<IDataComponent> components = guiComponent.getComponentList();
			
			for(int i = 0; i < 5; i++)
			{
				Button button = contentButtons[i];
				IDataComponent component = i + index < components.size() ? components.get(i + index) : null;
				if(component != null && component.isButton())
				{
					button.visible = true;
					button.setMessage(new StringTextComponent(component.getName()));
					
				} else button.visible = false;
			}
		} else for(Button button : contentButtons)
			button.visible = false;
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(MSKeyHandler.statKey.isActiveAndMatches(InputMappings.getKey(keyCode, scanCode)))
		{
			minecraft.setScreen(null);
			return true;
		}
		else return super.keyPressed(keyCode, scanCode, i);
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
			return I18n.get(message, params);
		}
	}
	
	public static class MainComponent implements IDataComponent
	{
		List<IDataComponent> list = new ArrayList<IDataComponent>();
		
		public MainComponent(CompoundNBT data)
		{
			if(data == null || data.isEmpty())
				return;
			
			ListNBT sessionList = data.getList("sessions", Constants.NBT.TAG_COMPOUND);
			int nameIndex = 1;
			for(int i = 0; i < sessionList.size(); i++)
			{
				CompoundNBT sessionTag = sessionList.getCompound(i);
				SessionComponent session = new SessionComponent(this, sessionTag, data);
				if(sessionTag.contains("name", Constants.NBT.TAG_STRING))
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
		List<IDataComponent> list = new ArrayList<>();
		MainComponent parent;
		String name;
		int players, playersEntered;
		
		public SessionComponent(MainComponent parent, CompoundNBT sessionTag, CompoundNBT dataTag)
		{
			this.parent = parent;
			HashSet<String> playerSet = new HashSet<>();
			ListNBT connectionList = sessionTag.getList("connections", Constants.NBT.TAG_COMPOUND);
			for(int i = 0; i < connectionList.size(); i++)
			{
				ConnectionComponent connection = new ConnectionComponent(this, connectionList.getCompound(i), dataTag);
				list.add(connection);
				
				if(!connection.landDim.isEmpty())
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
		String landDim = "";
		
		public ConnectionComponent(SessionComponent parent, CompoundNBT connectionTag, CompoundNBT dataTag)
		{
			this.parent = parent;
			this.client = connectionTag.getString("client");
			this.server = connectionTag.getString("server");
			this.isMain = connectionTag.getBoolean("isMain");
			if(isMain)
				landDim = connectionTag.getString("clientDim");
			
			list.add(new TextField("Client Player: %s", client));
			if(!server.isEmpty())
				list.add(new TextField("Server Player: %s", server));
			list.add(new TextField("Is Active: %b", connectionTag.getBoolean("isActive")));
			list.add(new TextField("Is Primary Connection: %b", isMain));
			
			list.add(null);
			if(isMain)
			{
				list.add(new TextField("Land dim: %s", (!landDim.isEmpty() ? landDim : "Pre-entry")));
				if(!landDim.isEmpty() && connectionTag.contains("landType1"))
					list.add(new LocalizedTextField(LandTypePair.FORMAT, new TranslationTextComponent("land."+connectionTag.getString("landType1")).getString(), new TranslationTextComponent("land."+connectionTag.getString("landType2")).getString()));
				if(connectionTag.contains("class"))
				{
					byte cl = connectionTag.getByte("class"), as = connectionTag.getByte("aspect");
					Title title = new Title(EnumClass.values()[cl], EnumAspect.values()[as]);
					list.add(new TextField(title.asTextComponent().getString()));
				}
				
				if(connectionTag.contains("titleLandType"))
					list.add(new TextField("Title land type: %s", connectionTag.getString("titleLandType")));
				if(connectionTag.contains("terrainLandType"))
					list.add(new TextField("Terrain land type: %s", connectionTag.getString("terrainLandType")));
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
			ChatScreen chat = new ChatScreen("/grist @"+name+" get");
			Minecraft.getInstance().setScreen(chat);
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