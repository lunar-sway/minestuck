package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mraof.minestuck.client.renderer.LandSkySpriteUploader;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.network.DataCheckerPackets;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.ChatScreen;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataCheckerScreen extends Screen
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final ResourceLocation icons = ResourceLocation.fromNamespaceAndPath("minestuck", "textures/gui/icons.png");
	private static final ResourceLocation guiBackground = ResourceLocation.fromNamespaceAndPath("minestuck", "textures/gui/data_check.png");
	private static final int GUI_WIDTH = 322, GUI_HEIGHT = 140;
	private static final int LAND_INFO_X = 96;
	private static final int LIST_Y = 25;
	private static final int VISIBLE_BUTTON_COUNT = 6;
	private static final int LAND_RADIUS = 40;
	private static final int COLOR_BLACK = 0xFF000000;
	private static final int COLOR_WHITE = 0xFFFFFFFF;
	
	public static CompoundTag nbt = new CompoundTag();
	private boolean needsRefresh = true;
	private List<SessionButton> sessionButtons = new ArrayList<>();
	public SessionButton focusedButton;
	private float displayIndex;
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
		//TODO add a way to search through sessions
		
		xOffset = (width - GUI_WIDTH) / 2;
		yOffset = (height - GUI_HEIGHT) / 2;
		
		if(nbt.isEmpty())
			PacketDistributor.sendToServer(DataCheckerPackets.Request.create());
		
		needsRefresh = true;
	}
	
	private void buildWidgets()
	{
		clearWidgets();
		needsRefresh = false;
		sessionButtons.clear();
		
		//TODO window needs resizing or closed for session buttons to show up. Using refresh button does not work to refresh
		addRenderableWidget(Button.builder(Component.empty(), button -> refresh()).pos(xOffset + GUI_WIDTH - 23, yOffset + 5).size(18, 18).build());
		
		ListTag sessionList = nbt.getList("sessions", Tag.TAG_COMPOUND);
		
		for(int sessionIt = 0; sessionIt < sessionList.size(); sessionIt++)
		{
			List<LandWidget> landWidgets = new ArrayList<>();
			
			CompoundTag sessionTag = sessionList.getCompound(sessionIt);
			ListTag connectionTags = sessionTag.getList("connections", Tag.TAG_COMPOUND);
			
			boolean completed = false;
			if(sessionTag.contains("completed"))
				completed = sessionTag.getBoolean("completed");
			
			SessionWidget sessionWidget = new SessionWidget(xOffset + LAND_INFO_X + 5, yOffset + 5, GUI_HEIGHT - 10, landWidgets);
			sessionWidget.visible = false;
			sessionWidget.active = false;
			addRenderableWidget(sessionWidget);
			
			Component sessionComponent = Component.literal("Session " + sessionIt);
			SessionButton sessionButton = new SessionButton(xOffset + LAND_INFO_X + GUI_HEIGHT - 2, yOffset + 5 + (sessionIt * 22), 60, 20, sessionComponent, sessionWidget);
			MutableComponent sessionPlayers = sessionComponent.copy().withStyle(ChatFormatting.BOLD);
			for(int i = 0; i < connectionTags.size(); i++)
			{
				CompoundTag connectionTag = connectionTags.getCompound(i);
				sessionPlayers.append("\n" + connectionTag.getString("client")).withStyle(ChatFormatting.RESET);
			}
			sessionButton.setTooltip(Tooltip.create(sessionPlayers));
			addRenderableWidget(sessionButton);
			sessionButtons.add(sessionButton);
			modifySessionButtonVisibility();
			
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
		
		guiGraphics.blit(guiBackground, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT, 352, 256);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		xOffset = (width - GUI_WIDTH) / 2;
		yOffset = (height - GUI_HEIGHT) / 2;
		
		boolean canScroll = sessionButtons.size() > VISIBLE_BUTTON_COUNT;
		
		if(canScroll && isScrolling)
		{
			displayIndex = (mouseY - yOffset - 28.5F) / 91;
			displayIndex = Mth.clamp(displayIndex, 0.0F, 1.0F);
			int newIndex = (int) ((sessionButtons.size() - VISIBLE_BUTTON_COUNT) * displayIndex + 0.5);
			if(newIndex != index)
			{
				index = newIndex;
				modifySessionButtonVisibility();
			}
		}
		
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		guiGraphics.blit(icons, xOffset + GUI_WIDTH - 22, yOffset + 6, 224, 0, 16, 16);
		
		int textureIndex = canScroll ? 328 : 340;
		guiGraphics.blit(guiBackground, xOffset + GUI_WIDTH - 20, yOffset + LIST_Y + 1 + (int) (displayIndex * 91), textureIndex, 0, 12, 15, 352, 256);
	}
	
	private void modifySessionButtonVisibility()
	{
		if(sessionButtons.isEmpty())
			return;
		
		sessionButtons.forEach(sessionButton -> {
			sessionButton.active = false;
			sessionButton.visible = false;
		});
		for(int i = 0; i < VISIBLE_BUTTON_COUNT; i++)
		{
			if(sessionButtons.size() > index + i)
			{
				sessionButtons.get(index + i).setY(yOffset + 5 + (i * 22));
				sessionButtons.get(index + i).active = true;
				sessionButtons.get(index + i).visible = true;
			}
		}
	}
	
	private void refresh()
	{
		PacketDistributor.sendToServer(DataCheckerPackets.Request.create());
		nbt = new CompoundTag();
		needsRefresh = true;
	}
	
	@Override
	public void tick()
	{
		if(!ClientPlayerData.hasDataCheckerAccess())
			minecraft.setScreen(null);
		
		if(needsRefresh)
			buildWidgets();
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
	{
		if(scrollY != 0)
		{
			int size = sessionButtons.size();
			if(size <= VISIBLE_BUTTON_COUNT)
				return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
			
			int prevIndex = index;
			if(scrollY > 0)
				index -= 1;
			else index += 1;
			index = Mth.clamp(index, 0, size - VISIBLE_BUTTON_COUNT);
			
			if(index != prevIndex)
			{
				displayIndex = index / ((float) size - VISIBLE_BUTTON_COUNT);
				modifySessionButtonVisibility();
			}
			return true;
		} else return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(mouseButton == 0 && mouseX >= xOffset + GUI_WIDTH - 20 && mouseX < xOffset + GUI_WIDTH - 8 && mouseY >= yOffset + LIST_Y + 1 && mouseY < yOffset + LIST_Y + 102)
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
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(MSKeyHandler.statKey.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode)))
		{
			minecraft.setScreen(null);
			return true;
		} else return super.keyPressed(keyCode, scanCode, i);
	}
	
	public class SessionWidget extends IncipisphereWidget
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
			guiGraphics.fill(getX(), getY(), getX() + width, getY() + height, COLOR_BLACK);
			
			RandomSource randomSource = RandomSource.create(0);
			
			int skaiaSize = 16;
			guiGraphics.blit(getCenterX() - skaiaSize / 2, getCenterY() - skaiaSize / 2, 0, skaiaSize, skaiaSize, LandSkySpriteUploader.getInstance().getSkaiaSprite());
			int meteorCount = 200;
			
			PoseStack poseStack = guiGraphics.pose();
			poseStack.pushPose();
			float rotation = (float) (RenderUtil.getCurrentTick() % 10000) * 0.0001F * 360;
			poseStack.rotateAround(Axis.ZP.rotationDegrees(rotation), getCenterX(), getCenterY(), 0);
			
			for(int i = 0; i < meteorCount; i++)
			{
				float veilRotation = ((float) i / meteorCount) * 360;
				int veilRadius = (int) (LAND_RADIUS * 1.4);
				int veilX = getXOnRadius(getCenterX(), veilRadius, veilRotation) + randomSource.nextIntBetweenInclusive(-3, 3);
				int veilY = getYOnRadius(getCenterY(), veilRadius, veilRotation) + randomSource.nextIntBetweenInclusive(-3, 3);
				guiGraphics.fill(veilX, veilY, veilX + 1, veilY + 1, randomSource.nextBoolean() ? COLOR_WHITE : 0xFFDDDDDD);
			}
			
			int kingdomSize = 4;
			
			guiGraphics.blit(getCenterX() + 8, getCenterY() - 7, 0, kingdomSize, kingdomSize, LandSkySpriteUploader.getInstance().getProspitSprite());
			
			poseStack.pushPose();
			int derseX = getX() + 16;
			int derseY = getY() + height - 27;
			poseStack.rotateAround(Axis.ZP.rotationDegrees(180), derseX, derseY, 0);
			guiGraphics.blit(derseX, derseY, 0, kingdomSize, kingdomSize, LandSkySpriteUploader.getInstance().getDerseSprite());
			poseStack.popPose();
			
			poseStack.popPose();
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
	
	public class LandWidget extends IncipisphereWidget
	{
		public final ResourceKey<Level> land;
		public final Optional<LandTypePair.Named> oNamed;
		public final CompoundTag landNbt;
		public final Button gristButton;
		
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
			appendLandAndPlayer(landNbt, oNamed, component);
			
			setTooltip(Tooltip.create(component));
			
			gristButton = addRenderableWidget(Button.builder(Component.literal("View Grist Cache"), button -> gristButtonPress(landNbt)).pos(xOffset + 3, yOffset + GUI_HEIGHT - 18).size(90, 14).build());
			gristButton.visible = false;
		}
		
		private void gristButtonPress(CompoundTag landNbt)
		{
			ChatScreen chat = new ChatScreen("/grist get " + landNbt.getString("client"));
			Minecraft.getInstance().setScreen(chat);
		}
		
		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
		{
			if(isHoveredOrFocused())
			{
				//highlight Land
				guiGraphics.fill(getX() - 1, getY() - 1, getX() + size + 1, getY() + size + 1, COLOR_WHITE);
			}
			
			gristButton.visible = isFocused();
			if(isFocused())
			{
				//fill in left section of GUI
				MutableComponent component = Component.empty();
				appendLandAndPlayer(landNbt, oNamed, component);
				component.append("\n");
				if(landNbt.contains("class"))
				{
					Title title = new Title(EnumClass.values()[landNbt.getByte("class")], EnumAspect.values()[landNbt.getByte("aspect")]);
					component.append("\n" + "Title: ").append(title.asTextComponent()).withStyle(ChatFormatting.BOLD);
				}
				if(landNbt.contains("server"))
					component.append("\n" + "Server is " + landNbt.getString("server")).withStyle(ChatFormatting.RESET);
				component.append("\n" + "Is Primary Connection: " + landNbt.getBoolean("isMain"));
				
				guiGraphics.drawWordWrap(minecraft.font, component, xOffset + 4, yOffset + 1, 90, COLOR_BLACK);
			}
			
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
				guiGraphics.fill(getX(), getY(), getX() + size, getY() + size, COLOR_WHITE);
				guiGraphics.fill(getX() + 1, getY() + 1, getX() + size - 1, getY() + size - 1, COLOR_BLACK);
			}
		}
		
		private static void appendLandAndPlayer(CompoundTag landNbt, Optional<LandTypePair.Named> oNamed, MutableComponent component)
		{
			//TODO formatting is wrong for null lands
			if(oNamed.isPresent())
				component.append(oNamed.get().asComponentWithLandFont());
			else
				component.append("Land of §kNull§r and §kNull").withStyle(LandTypePair.LAND_OF_COPYLEFT_AND_FREEDOM_FONT_STYLE);
			
			MutableComponent landPlayer = Component.literal("\n\n" + landNbt.getString("client")).withStyle(ChatFormatting.RESET);
			if(landNbt.contains("playerColor"))
				landPlayer.withColor(landNbt.getInt("playerColor"));
			component.append(landPlayer);
		}
	}
	
	public class IncipisphereWidget extends AbstractWidget
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
	
	public class SessionButton extends ExtendedButton
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
			focusedButton = this;
		}
		
		@Override
		public boolean isFocused()
		{
			return focusedButton == this || super.isFocused();
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
