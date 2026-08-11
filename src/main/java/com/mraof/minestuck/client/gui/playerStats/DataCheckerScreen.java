package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.renderer.LandSkySpriteUploader;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.network.DataCheckerPackets;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataCheckerScreen extends Screen
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final ResourceLocation icons = ResourceLocation.fromNamespaceAndPath("minestuck", "textures/gui/icons.png");
	private static final ResourceLocation guiBackground = ResourceLocation.fromNamespaceAndPath("minestuck", "textures/gui/data_check.png");
	private static final int GUI_WIDTH = 210, GUI_HEIGHT = 140;
	private static final int LIST_Y = 25;
	
	private static final int LAND_RADIUS = 40;
	
	public static CompoundTag nbt = new CompoundTag();
	private Button refreshButton;
	private int index;
	private boolean isScrolling;
	private int xOffset;
	private int yOffset;
	
	public DataCheckerScreen()
	{
		super(Component.literal("Data Checker"));
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	@Override
	public void init()
	{
		xOffset = (width - GUI_WIDTH) / 2;
		yOffset = (height - GUI_HEIGHT) / 2;
		
		if(nbt.isEmpty())
			PacketDistributor.sendToServer(DataCheckerPackets.Request.create());
		
		buildWidgets();
	}
	
	private void buildWidgets()
	{
		//TODO window needs resizing or closed for session buttons to show up. Using refresh button does not work to refresh
		refreshButton = addRenderableWidget(Button.builder(Component.empty(), button -> refresh()).pos(xOffset + GUI_WIDTH - 45, yOffset + 5).size(18, 18).build());
		
		ListTag sessionList = nbt.getList("sessions", Tag.TAG_COMPOUND);
		List<SessionButton> buttons = new ArrayList<>();
		
		for(int sessionIt = 0; sessionIt < sessionList.size(); sessionIt++)
		{
			CompoundTag sessionTag = sessionList.getCompound(sessionIt);
			ListTag connectionTags = sessionTag.getList("connections", Tag.TAG_COMPOUND);
			
			boolean completed = false;
			if(sessionTag.contains("completed"))
				completed = sessionTag.getBoolean("completed");
			
			List<LandWidget> landWidgets = new ArrayList<>();
			SessionWidget sessionWidget = new SessionWidget(xOffset + 5, yOffset + 5, GUI_HEIGHT - 10, landWidgets);
			sessionWidget.visible = false;
			sessionWidget.active = false;
			addRenderableWidget(sessionWidget);
			SessionButton sessionButton = new SessionButton(xOffset + GUI_HEIGHT - 4, yOffset + 24 + (sessionIt * 22), 56, 20, Component.literal("Session " + sessionIt), sessionWidget);
			//TODO add player names to Session button
			//sessionButton.setTooltip(Tooltip.create(Component.literal()));
			addRenderableWidget(sessionButton);
			buttons.add(sessionButton);
			
			buildLandWidgets(connectionTags, completed, sessionWidget, landWidgets);
		}
	}
	
	private void buildLandWidgets(ListTag connectionTags, boolean completed, SessionWidget sessionWidget, List<LandWidget> landWidgets)
	{
		//leave a space open visually if the loop is not closed
		int landPositionCount = connectionTags.size() + (completed ? 0 : 1);
		int size = Math.clamp(-(landPositionCount / 5) + 14, 6, 14);
		int spriteOffset = size / 2;
		
		for(int connectionIt = 0; connectionIt < connectionTags.size(); connectionIt++)
		{
			CompoundTag connectionTag = connectionTags.getCompound(connectionIt);
			
			float rotation = ((float) connectionIt / landPositionCount) * 360;
			int landX = getXOnRadius(sessionWidget.getCenterX(), LAND_RADIUS, rotation) - spriteOffset;
			int landY = getYOnRadius(sessionWidget.getCenterY(), LAND_RADIUS, rotation) - spriteOffset;
			
			LandWidget landWidget = new LandWidget(landX, landY, size, connectionTag);
			landWidget.visible = false;
			landWidget.active = false;
			landWidgets.add(landWidget);
			addRenderableWidget(landWidget);
		}
	}
	
	private static int getXOnRadius(int center, int radius, float rotation)
	{
		return (int) (center + radius * Math.cos(Math.toRadians(rotation)));
	}
	
	private static int getYOnRadius(int center, int radius, float rotation)
	{
		return (int) (center + radius * Math.sin(Math.toRadians(rotation)));
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		guiGraphics.blit(guiBackground, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		xOffset = (width - GUI_WIDTH) / 2;
		yOffset = (height - GUI_HEIGHT) / 2;
		/*boolean canScroll = guiComponent != null && guiComponent.getComponentList().size() > 5;
		
		if(canScroll && isScrolling)
		{
			displayIndex = (mouseY - yOffset - 28.5F) / 91;
			displayIndex = Mth.clamp(displayIndex, 0.0F, 1.0F);
			int newIndex = (int) ((guiComponent.getComponentList().size() - 5) * displayIndex + 0.5);
			if(newIndex != index)
			{
				index = newIndex;
				//updateGuiButtons();
			}
		}*/
		
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		if(this.refreshButton.active)
			RenderSystem.setShaderColor(1, 1, 1, 1);
		else RenderSystem.setShaderColor(.5F, .5F, .5F, 1);
		guiGraphics.blit(icons, xOffset + GUI_WIDTH - 44, yOffset + 6, 224, 0, 16, 16);
		
		/*
		if(guiComponent != null)
		{
			List<IDataComponent> list = guiComponent.getComponentList();
			for(int i = 0; i < 5; i++)
			{
				guiGraphics.drawString(font, guiComponent.getName(), xOffset + 9, yOffset + 15 - font.lineHeight / 2, 0, false);
				IDataComponent component = i + index < list.size() ? list.get(i + index) : null;
				if(component != null && !component.isButton())
				{
					RenderSystem.setShaderColor(1, 1, 1, 1);
					guiGraphics.blit(guiBackground, xOffset + 5, yOffset + LIST_Y + i * 22, 0, 236, 180, 20);
					guiGraphics.drawString(font, component.getName(), xOffset + 9, yOffset + LIST_Y + 10 - font.lineHeight / 2 + i * 22, 0, false);
				}
			}
		} else
			guiGraphics.drawString(font, "Retrieving data from server...", xOffset + 9, yOffset + 15 - font.lineHeight / 2, 0, false);
		*/
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		//int textureIndex = canScroll ? 232 : 244;
		//guiGraphics.blit(guiBackground, (width - GUI_WIDTH) / 2 + 190, (height - GUI_HEIGHT) / 2 + LIST_Y + 1 + (int) displayIndex * 91, textureIndex, 0, 12, 15);
	}
	
	@Override
	public void tick()
	{
		if(!ClientPlayerData.hasDataCheckerAccess())
			minecraft.setScreen(null);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
	{
		/*
		if(scrollY != 0 && guiComponent != null)
		{
			int size = guiComponent.getComponentList().size();
			if(size <= 5)
				return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
			
			int prevIndex = index;
			if(scrollY > 0)
				index -= 1;
			else index += 1;
			index = Mth.clamp(index, 0, size - 5);
			
			if(index != prevIndex)
			{
				displayIndex = index / ((float) size - 5);
				updateGuiButtons();
			}
			return true;
		} else return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
		 */
		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		int xOffset = (width - GUI_WIDTH) / 2;
		int yOffset = (height - GUI_HEIGHT) / 2;
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
	
	private void refresh()
	{
		PacketDistributor.sendToServer(DataCheckerPackets.Request.create());
		nbt = new CompoundTag();
		
		buildWidgets();
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(MSKeyHandler.statKey.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode)))
		{
			minecraft.setScreen(null);
			return true;
		} else return super.keyPressed(keyCode, scanCode, i);
	}
	
	public static class SessionWidget extends IncipisphereWidget
	{
		public final List<LandWidget> landWidgets;
		
		public SessionWidget(int x, int y, int size, List<LandWidget> landWidgets)
		{
			super(x, y, size);
			this.landWidgets = landWidgets;
		}
		
		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
		{
			updateChildren();
			
			//backdrop
			guiGraphics.fill(getX(), getY(), getX() + width, getY() + height, 0xFF000000);
			
			RandomSource randomSource = RandomSource.create(0);
			
			int meteorCount = 200;
			for(int i = 0; i < meteorCount; i++)
			{
				float rotation = ((float) i / meteorCount) * 360;
				int veilRadius = (int) (LAND_RADIUS * 1.35);
				int veilX = getXOnRadius(getCenterX(), veilRadius, rotation) + randomSource.nextIntBetweenInclusive(-3, 3);
				int veilY = getYOnRadius(getCenterY(), veilRadius, rotation) + randomSource.nextIntBetweenInclusive(-3, 3);
				guiGraphics.fill(veilX, veilY, veilX + 1, veilY + 1, randomSource.nextBoolean() ? 0xFFFFFFFF : 0xFFDDDDDD);
			}
			
			int skaiaSize = 16;
			int kingdomSize = 4;
			guiGraphics.blit(getCenterX() - skaiaSize / 2, getCenterY() - skaiaSize / 2, 0, skaiaSize, skaiaSize, LandSkySpriteUploader.getInstance().getSkaiaSprite());
			guiGraphics.blit(getCenterX() + 13, getCenterY() - 5, 0, kingdomSize, kingdomSize, LandSkySpriteUploader.getInstance().getProspitSprite());
			
			guiGraphics.blit(getX() + 10, getY() + height - 10, 0, kingdomSize, kingdomSize, LandSkySpriteUploader.getInstance().getDerseSprite());
			
			//TODO replace with blank representation, handle on connection/land basis?
			//if(isOpen)
			//	guiGraphics.blitSprite(GristType.DUMMY_ICON_LOCATION, getX() + 10, getY() + height - 10, 0, 4, 4);
		}
		
		@Override
		protected boolean isValidClickButton(int button)
		{
			return false;
		}
		
		public void updateChildren()
		{
			landWidgets.forEach(landWidget -> landWidget.visible = this.visible);
			landWidgets.forEach(landWidget -> landWidget.active = this.active);
		}
	}
	
	public static class LandWidget extends IncipisphereWidget
	{
		public final ResourceKey<Level> land;
		public final Optional<LandTypePair.Named> oNamed;
		public final CompoundTag landNbt;
		
		public LandWidget(int x, int y, int size, CompoundTag landNbt)
		{
			super(x, y, size);
			
			ResourceKey<Level> landKey = null;
			if(landNbt.contains("clientDim"))
				landKey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(landNbt.getString("clientDim")));
			
			this.land = landKey;
			this.landNbt = landNbt;
			
			MutableComponent component = Component.empty();
			
			Optional<LandTypePair.Named> oNamed = Optional.empty();
			if(land != null && landNbt.contains("landTypes"))
				oNamed = LandTypePair.Named.CODEC.parse(NbtOps.INSTANCE, landNbt.get("landTypes")).resultOrPartial(LOGGER::error);
			this.oNamed = oNamed;
			if(oNamed.isPresent())
				component.append(oNamed.get().asComponentWithLandFont());
			else
				component.append("Land of ").append("Null").withStyle(ChatFormatting.OBFUSCATED).append(" and ").withStyle(ChatFormatting.RESET).append("Null").withStyle(ChatFormatting.OBFUSCATED);
			
			MutableComponent landPlayer = Component.literal("\n" + landNbt.getString("client")).withStyle(ChatFormatting.RESET);
			if(landNbt.contains("playerColor"))
				landPlayer.withColor(landNbt.getInt("playerColor"));
			component.append(landPlayer);
			
			component.append("\n\n" + "Server is " + landNbt.getString("server"));
			component.append("\n" + "Is Primary Connection: " + landNbt.getBoolean("isMain"));
			
			setTooltip(Tooltip.create(component));
		}
		
		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
		{
			//TODO highlight if hovered
			
			if(oNamed.isPresent())
			{
				LandTypePair.Named named = oNamed.get();
				
				TextureAtlasSprite planetSprite = LandSkySpriteUploader.getInstance().getPlanetSprite(named.landTypes().getTerrain(), named.terrainNameIndex());
				TextureAtlasSprite overlaySprite = LandSkySpriteUploader.getInstance().getOverlaySprite(named.landTypes().getTitle(), named.titleNameIndex());
				
				guiGraphics.blit(getX(), getY(), 0, size, size, planetSprite);
				guiGraphics.blit(getX(), getY(), 0, size, size, overlaySprite);
			} else
			{
				//placeholder for planet
				guiGraphics.fill(getX(), getY(), getX() + size, getY() + size, 0xFFFFFFFF);
				guiGraphics.fill(getX() + 1, getY() + 1, getX() + size - 1, getY() + size - 1, 0xFF000000);
			}
		}
		
		@Override
		public boolean isFocused()
		{
			return false;
		}
		
		public void temp()
		{
			//list.add(new TextField("Land dim: %s", (!landDim.isEmpty() ? landDim : "Pre-entry")));
			
			if(landNbt.contains("class"))
			{
				byte cl = landNbt.getByte("class"), as = landNbt.getByte("aspect");
				Title title = new Title(EnumClass.values()[cl], EnumAspect.values()[as]);
			}
			
			//if(landNbt.contains("titleLandType"))
			//String titleType = "Title land type: %s" + landNbt.getString("titleLandType");
			//if(landNbt.contains("terrainLandType"))
			//list.add(new TextField("Terrain land type: %s", landNbt.getString("terrainLandType")));
			
			//list.add(new GristCacheButton(landNbt.getString("clientId")));
		}
	}
	
	public static class IncipisphereWidget extends AbstractWidget
	{
		public int size;
		
		public IncipisphereWidget(int x, int y, int size)
		{
			super(x, y, size, size, Component.empty());
			this.size = size;
		}
		
		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
		{
		}
		
		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
		{
			this.defaultButtonNarrationText(narrationElementOutput);
		}
		
		public int getCenterX()
		{
			return getX() + (width / 2);
		}
		
		public int getCenterY()
		{
			return getY() + (height / 2);
		}
	}
	
	public static class SessionButton extends ExtendedButton
	{
		public final SessionWidget sessionWidget;
		
		protected SessionButton(int x, int y, int width, int height, Component message, SessionWidget sessionWidget)
		{
			super(x, y, width, height, message, Button::onPress, DEFAULT_NARRATION);
			this.sessionWidget = sessionWidget;
		}
		
		@Override
		public void onPress()
		{
		
		}
		
		@Override
		public boolean isHoveredOrFocused()
		{
			boolean hoveredOrFocused = super.isHoveredOrFocused();
			sessionWidget.visible = hoveredOrFocused;
			sessionWidget.active = hoveredOrFocused;
			sessionWidget.updateChildren();
			
			return hoveredOrFocused;
		}
	}
}
