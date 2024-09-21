package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.TorrentSession;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class TorrentScreen extends Screen
{
	public static final String TITLE = "minestuck.computer_themes"; //TODO
	
	public static final ResourceLocation GUI_MAIN = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/torrent.png");
	
	private static final int GUI_WIDTH = 176;
	private static final int GUI_HEIGHT = 166;
	
	private static final int LIGHT_BLUE = 0xFF19B3EF;
	
	private final ComputerBlockEntity computer;
	
	private int xOffset;
	private int yOffset;
	
	private GristSet gutterGrist;
	private long gutterRemainingCapacity;
	private static List<TorrentSession> visibleTorrentSessions = new ArrayList<>();
	
	private int updateTick = 0;
	
	
	public TorrentScreen(ComputerBlockEntity computer)
	{
		super(Component.translatable(TITLE));
		
		this.computer = computer;
	}
	
	@Override
	public void init()
	{
		yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		xOffset = (this.width / 2) - (GUI_WIDTH / 2);
		
		gutterGrist = ClientPlayerData.getGutterSet();
		visibleTorrentSessions = ClientPlayerData.getVisibleTorrentSessions();
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		guiGraphics.blit(GUI_MAIN, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		//TODO not clearing renderables causes performance to drop with time, but clearing them prevents tooltips from showing up
		//renderables.clear();
		
		clientDataUpdates();
		
		renderGutter(guiGraphics);
		
		renderTorrentSessions(guiGraphics, xOffset, yOffset);
	}
	
	private void renderGutter(GuiGraphics guiGraphics)
	{
		if(gutterGrist == null)
			return; //TODO consider adding text that says "loading" if this early return is triggered
		
		double totalVolume = gutterRemainingCapacity;
		long filledVolume = 0;
		
		for(GristAmount amount : gutterGrist.asAmounts())
			filledVolume += amount.amount();
		
		totalVolume += filledVolume;
		
		int initialX = xOffset + 55;
		int y = yOffset + 147;
		
		for(GristAmount gristAmount : gutterGrist.asAmounts())
		{
			int length = (int) ((gristAmount.amount() / totalVolume) * 100);
			addRenderableWidget(new GutterBarWidget(initialX, y, length, gristAmount));
			initialX += length;
		}
		
		int remainingVolume = (int) ((gutterRemainingCapacity / totalVolume) * 100);
		addRenderableWidget(new GutterBarWidget(initialX, y, remainingVolume, gutterRemainingCapacity));
		
		String remainingText = String.valueOf(filledVolume);
		guiGraphics.drawString(font, remainingText, (xOffset + 105) - font.width(remainingText) / 2, y + 5, LIGHT_BLUE, false);
	}
	
	private void renderTorrentSessions(GuiGraphics guiGraphics, int xOffset, int yOffset)
	{
		if(minecraft == null || minecraft.player == null)
			return;
		
		TorrentSession userSession = visibleTorrentSessions.stream().filter(session -> session.getSeeder().getUUID().equals(minecraft.player.getUUID())).findFirst().orElse(null);
		
		if(userSession == null)
			return;
		
		ClientPlayerData.ClientCache cache = ClientPlayerData.getGristCache(ClientPlayerData.CacheSource.PLAYER);
		GristSet clientGrist = cache.set();
		long cacheLimit = cache.limit();
		
		List<GristType> types = GristTypes.REGISTRY.stream().limit(6).toList();
		
		int offset = 0;
		for(GristType type : types)
		{
			GristAmount gristAmount = new GristAmount(type, clientGrist.getGrist(type));
			
			addRenderableWidget(new GristEntry(xOffset + 5, yOffset + 32 + (GristEntry.HEIGHT * offset), gristAmount, cacheLimit, font));
			
			offset++;
		}
	}
	
	private void clientDataUpdates()
	{
		if(updateTick % 20 == 0)
		{
			gutterGrist = ClientPlayerData.getGutterSet();
			gutterRemainingCapacity = ClientPlayerData.getGutterRemainingCapacity();
			visibleTorrentSessions = ClientPlayerData.getVisibleTorrentSessions();
		}
		
		updateTick++;
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	@Override
	public boolean shouldCloseOnEsc()
	{
		return false;
	}
	
	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers)
	{
		if(pKeyCode == GLFW.GLFW_KEY_ESCAPE)
		{
			computer.gui.exitProgram();
			computer.gui.getMinecraft().setScreen(null);
			MSScreenFactories.displayComputerScreen(computer);
			
			return true;
		}
		
		return super.keyPressed(pKeyCode, pScanCode, pModifiers);
	}
	
	public static class GristEntry extends AbstractWidget
	{
		public static final int WIDTH = 40;
		public static final int HEIGHT = 14;
		public static final int GRIST_ICON_X = 2, GRIST_ICON_Y = 2;
		public static final int GRIST_COUNT_X = GRIST_ICON_X + 14, GRIST_COUNT_Y = GRIST_ICON_Y + 1;
		
		int x;
		int y;
		GristAmount gristAmount;
		Font font;
		long cacheLimit;
		
		int color;
		
		public GristEntry(int pX, int pY, GristAmount gristAmount, long cacheLimit, Font font)
		{
			super(pX, pY, WIDTH, HEIGHT, Component.empty());
			
			x = pX;
			y = pY;
			this.gristAmount = gristAmount;
			this.font = font;
			this.cacheLimit = cacheLimit;
			
			setTooltip(Tooltip.create(gristAmount.type().getDisplayName()));
			
			//TODO replace grist color background with different graphics
			GristType gristType = gristAmount.type();
			if(gristType == GristTypes.BUILD.get() || gristType == GristTypes.DIAMOND.get())
				color = 0x99000000 | LIGHT_BLUE;
			else if(gristType == GristTypes.MARBLE.get())
				color = 0x99FFC0CB; //pink
			else
				color = 0x99000000 | gristType.getUnderlingColor();
		}
		
		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v)
		{
			float scale = 0.5F;
			int scaleMod = (int) (1 / scale);
			
			guiGraphics.pose().pushPose();
			guiGraphics.pose().scale(scale, scale, scale);
			
			guiGraphics.fill(x * scaleMod, y * scaleMod, (x + width) * scaleMod, (y + height) * scaleMod, color);
			
			TorrentScreen.drawIcon(x + GRIST_ICON_X, y + GRIST_ICON_Y, gristAmount.type().getIcon());
			
			//renders amount of grist
			String amount = GuiUtil.addSuffix(gristAmount.amount());
			guiGraphics.drawString(font, amount, (x + GRIST_COUNT_X - 2) * scaleMod, (y + GRIST_COUNT_Y + 6) * scaleMod, 0x19b3ef, false);
			
			//renders bars
			double gristFraction = Math.min(1, (double) gristAmount.amount() / cacheLimit);
			guiGraphics.fill((x + GRIST_COUNT_X - 1) * scaleMod, (y + GRIST_COUNT_Y + 1) * scaleMod, (int) (x + GRIST_COUNT_X + (20.0 * gristFraction)) * scaleMod, (y + (GRIST_COUNT_Y + 5)) * scaleMod, 0xff19B3EF); //0xE64C10
			guiGraphics.fill((x + GRIST_COUNT_X - 1) * scaleMod, (y + GRIST_COUNT_Y + 1) * scaleMod, (int) (x + GRIST_COUNT_X + (20.0 * gristFraction)) * scaleMod, (y + (GRIST_COUNT_Y)) * scaleMod, 0xff7ED8E5); //0xE64C10
			
			guiGraphics.pose().popPose();
		}
		
		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
		{
		}
	}
	
	protected static void drawIcon(int x, int y, ResourceLocation icon)
	{
		//TODO does not have the null checks present in MinestuckScreen
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, icon);
		
		float scale = (float) 1 / 16;
		
		int iconX = 16;
		int iconY = 16;
		int iconU = 0;
		int iconV = 0;
		
		float scaledIconX = (float) iconX * 0.65F;
		float scaledIconY = (float) iconY * 0.65F;
		
		BufferBuilder render = Tesselator.getInstance().getBuilder();
		render.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		
		render.vertex(x, y + scaledIconY, 0D).uv((iconU) * scale, (iconV + iconY) * scale).endVertex();
		render.vertex(x + scaledIconX, y + scaledIconY, 0D).uv((iconU + iconX) * scale, (iconV + iconY) * scale).endVertex();
		render.vertex(x + scaledIconX, y, 0D).uv((iconU + iconX) * scale, (iconV) * scale).endVertex();
		render.vertex(x, y, 0D).uv((iconU) * scale, (iconV) * scale).endVertex();
		
		Tesselator.getInstance().end();
	}
	
	public static class GutterBarWidget extends AbstractWidget
	{
		final int x;
		final int y;
		final int color;
		
		public GutterBarWidget(int pX, int pY, int pWidth, GristAmount gristAmount)
		{
			super(pX, pY, pWidth, 3, Component.empty());
			x = pX;
			y = pY;
			
			GristType gristType = gristAmount.type();
			
			if(gristType == GristTypes.BUILD.get() || gristType == GristTypes.DIAMOND.get())
				color = LIGHT_BLUE;
			else if(gristType == GristTypes.MARBLE.get())
				color = 0xFFFFC0CB; //pink
			else
				color = gristType.getUnderlingColor();
			
			setTooltip(Tooltip.create(gristAmount.type().getDisplayName().append(Component.literal(": " + gristAmount.amount()))));
		}
		
		public GutterBarWidget(int pX, int pY, int pWidth, long remainingCapacity)
		{
			super(pX, pY, pWidth, 3, Component.empty());
			x = pX;
			y = pY;
			color = 0xFFFFFFFF;
			
			setTooltip(Tooltip.create(Component.literal("Remaining capacity: " + remainingCapacity)));
		}
		
		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v)
		{
			guiGraphics.fill(x, y, x + width, y + height, getColor());
		}
		
		private int getColor()
		{
			return 0xFF000000 | color; //OR operation converts RGB integer to ARGB with full opacity
		}
		
		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
		{
		}
		
		@Override
		protected boolean isValidClickButton(int pButton)
		{
			return false;
		}
	}
}