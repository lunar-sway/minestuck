package com.mraof.minestuck.client.gui.computer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.alchemy.TorrentHelper;
import com.mraof.minestuck.alchemy.TorrentSession;
import com.mraof.minestuck.alchemy.TorrentSession.LimitedCache;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.network.TorrentPackets;
import com.mraof.minestuck.player.IdentifierHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TorrentWidgets
{
	private static int scale(int input)
	{
		return input * 2;
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
		
		BufferBuilder render = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		
		render.addVertex(x, y + scaledIconY, 0).setUv((iconU) * scale, (iconV + iconY) * scale);
		render.addVertex(x + scaledIconX, y + scaledIconY, 0).setUv((iconU + iconX) * scale, (iconV + iconY) * scale);
		render.addVertex(x + scaledIconX, y, 0).setUv((iconU + iconX) * scale, (iconV) * scale);
		render.addVertex(x, y, 0).setUv((iconU) * scale, (iconV) * scale);
		
		BufferUploader.drawWithShader(render.buildOrThrow());
	}
	
	protected static class GristEntry extends AbstractWidget
	{
		public static final int WIDTH = 40;
		public static final int HEIGHT = 14;
		public static final int GRIST_ICON_X = 2, GRIST_ICON_Y = 2;
		public static final int GRIST_COUNT_X = GRIST_ICON_X + 13;
		public static final float BAR_WIDTH = 20F;
		
		public TorrentSession torrentSession;
		public LimitedCache cache;
		public final GristType gristType;
		public long gristAmount;
		public long cacheLimit;
		public boolean isOwner;
		private boolean isActive;
		private Font font;
		
		public GristEntry(int pX, int pY, GristType gristType)
		{
			super(pX, pY, WIDTH, HEIGHT, Component.empty());
			
			this.gristType = gristType;
			
			visible = false;
		}
		
		//TODO may be called unnecessarily
		public void setTooltip()
		{
			MutableComponent tooltip = gristType.getDisplayName();
			
			if(torrentSession != null && torrentSession.getSeeding().contains(gristType))
			{
				if(gristAmount > 0)
					tooltip.append("\n(Is being seeded)");
				else
					tooltip.append("\n(Will be seeded)");
			}
			
			setTooltip(Tooltip.create(tooltip));
		}
		
		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
		{
			int x = getX();
			int y = getY();
			
			guiGraphics.pose().pushPose();
			guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
			
			guiGraphics.renderOutline(scale(x), scale(y), scale(width), scale(height), getColor());
			
			int gristIconYMod = y + GRIST_ICON_Y;
			int gristCountXMod = x + GRIST_COUNT_X;
			
			drawIcon(x + GRIST_ICON_X, gristIconYMod, gristType.getIcon());
			
			//renders amount of grist
			String amount = GuiUtil.addSuffix(gristAmount);
			guiGraphics.drawString(font, amount, scale(gristCountXMod), scale(gristIconYMod + 7), 0x19b3ef, false);
			
			//renders bars
			guiGraphics.fill(scale(gristCountXMod), scale(gristIconYMod + 1), scale((int) (gristCountXMod + BAR_WIDTH)), scale(gristIconYMod + 6), GristTorrentGui.DARK_GREY);
			if(cacheLimit > 0)
			{
				double gristFraction = Math.min(1D, (double) gristAmount / cacheLimit);
				guiGraphics.fill(scale(gristCountXMod), scale(gristIconYMod + 2), scale((int) (gristCountXMod + (BAR_WIDTH * gristFraction))), scale(gristIconYMod + 6), 0xFF19B3EF);
				guiGraphics.fill(scale(gristCountXMod), scale(gristIconYMod + 1), scale((int) (gristCountXMod + (BAR_WIDTH * gristFraction))), scale(gristIconYMod + 2), 0xFF7ED8E5);
			}
			
			guiGraphics.pose().popPose();
		}
		
		private int getColor()
		{
			//is gray unless active, at which point color is determined by whether it is owned by the user
			int color = GristTorrentGui.DARK_GREY;
			
			if(isActive)
				color = isOwner ? 0xFFFF0000 : 0xFF00FF00;
			
			return color;
		}
		
		@Override
		public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY)
		{
			//TODO doesnt change things
			return false; //when true this stops the TorrentContainer from scrolling
		}
		
		@Override
		public void onClick(double mouseX, double mouseY, int button)
		{
			super.onClick(mouseX, mouseY, button);
			
			isActive = !isActive;
			if(isOwner)
				PacketDistributor.sendToServer(new TorrentPackets.ModifySeeding(gristType, isActive));
			else
				PacketDistributor.sendToServer(new TorrentPackets.ModifyLeeching(torrentSession, gristType, isActive));
		}
		
		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
		{
		}
	}
	
	protected static class TorrentContainer extends ScrollingWidget<GristEntry>
	{
		public static final int WIDTH = GristEntry.WIDTH + 2;
		public static final int HEIGHT = (GristEntry.HEIGHT + 1) * 6;
		
		private final String playerUsername;
		private final Font font;
		
		private final List<GristType> allGristTypes;
		
		public TorrentContainer(int pX, int pY, Font font, IdentifierHandler.UUIDIdentifier player, List<GristType> allGristTypes)
		{
			super(pX, pY, WIDTH, HEIGHT);
			
			this.playerUsername = player.getUsername();
			this.font = font;
			this.allGristTypes = allGristTypes;
			
			int yOffset = 1; //this is 1 because there needs to be room to render the name of the torrent's seeder
			for(GristType type : allGristTypes)
			{
				GristEntry gristEntry = new GristEntry(pX, pY + ((GristEntry.HEIGHT + 1) * yOffset), type);
				if(this.children().size() < visibleEntryCount())
					gristEntry.visible = true;
				
				gristEntry.isOwner = GristTorrentGui.userSession.sameOwner(player);
				gristEntry.font = font;
				
				this.children().add(gristEntry);
				
				yOffset++;
			}
		}
		
		public void refreshEntries(TorrentSession torrentSession, LimitedCache cache)
		{
			for(GristEntry gristEntry : this.children())
			{
				GristType entryGristType = gristEntry.gristType;
				
				gristEntry.torrentSession = torrentSession;
				gristEntry.isActive = gristEntry.isOwner ? torrentSession.getSeeding().contains(entryGristType) : torrentSession.isLeechForGristType(GristTorrentGui.userSession.getSeeder(), entryGristType);
				gristEntry.cache = cache;
				gristEntry.gristAmount = cache.set().getGrist(entryGristType);
				gristEntry.cacheLimit = cache.limit();
				
				gristEntry.setTooltip();
			}
		}
		
		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
		{
			super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
			
			guiGraphics.pose().pushPose();
			guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
			guiGraphics.drawString(font, this.playerUsername, scale((getX() + WIDTH / 2)) - scale(font.width(this.playerUsername) / 2), scale(getY() + 4), 0xFF000000, false);
			guiGraphics.pose().popPose();
		}
		
		@Override
		public int visibleEntryCount()
		{
			return 5;
		}
		
		@Override
		public int subWidgetHeight()
		{
			return GristEntry.HEIGHT;
		}
		
		@Override
		public int getMaxScroll()
		{
			return Math.max(0, allGristTypes.size() - visibleEntryCount());
		}
		
		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
		{
		}
	}
	
	protected static class GristStat extends AbstractWidget
	{
		public static final int X_OFFSET_FROM_EDGE = 40;
		public static final int Y_OFFSET_FROM_EDGE = 130;
		public static final int WIDTH = GristTorrentGui.GUI_WIDTH - X_OFFSET_FROM_EDGE;
		public static final int HEIGHT = 12;
		public static final int TEXT_Y_OFFSET = 5;
		
		private final Font font;
		private final GristType gristType;
		
		private Pair<Integer, Integer> seedsData = Pair.of(0, 0); //first is seeds being utilized and second is total seeds available
		private Pair<Integer, Integer> typeDownSpeedRange = Pair.of(0, 0); //first is minimum speed and second is maximum
		private int typeUpSpeed = 0;
		
		public GristStat(int pX, int pY, Font font, GristType gristType)
		{
			super(pX, pY, WIDTH, HEIGHT, Component.empty());
			
			this.font = font;
			this.gristType = gristType;
			
			visible = false;
			
			updateStats();
		}
		
		public void updateStats()
		{
			LimitedCache userCache = GristTorrentGui.visibleTorrentData.get(GristTorrentGui.userSession);
			
			int minDownSpeed = Integer.MAX_VALUE;
			int maxDownSpeed = 1;
			AtomicInteger totalSeeds = new AtomicInteger(); //TODO is this appropriate?
			
			Map<TorrentSession, LimitedCache> filteredData = new HashMap<>();
			GristTorrentGui.visibleTorrentData.forEach((key, value) -> {
				boolean couldSeed = value.set().asAmounts().stream().anyMatch(gristAmount -> gristAmount.hasType(gristType) && !gristAmount.isEmpty());
				boolean tryingToSeed = key.getSeeding().stream().anyMatch(iterateType -> iterateType.equals(gristType));
				boolean userTryingToLeech = key.isLeechForGristType(GristTorrentGui.userSession.getSeeder(), gristType);
				
				if(tryingToSeed && !key.sameOwner(GristTorrentGui.userSession))
				{
					if(couldSeed)
						totalSeeds.addAndGet(1);
					
					if(userTryingToLeech)
						filteredData.put(key, value);
				}
			}); //only include data if the user is trying to leech the grist
			
			if(filteredData.isEmpty())
				return; //widget will render due to how min/max speeds are obtained without this early return
			
			List<GristType> userSeeding = GristTorrentGui.userSession.getViableSeeding(userCache);
			typeUpSpeed = TorrentHelper.getSeedRateMod(userSeeding);
			
			for(Map.Entry<TorrentSession, LimitedCache> dataEntry : filteredData.entrySet())
			{
				TorrentSession entrySession = dataEntry.getKey();
				LimitedCache entryCache = dataEntry.getValue();
				List<GristType> entrySeeding = entrySession.getViableSeeding(entryCache);
				
				int entrySeedRate = TorrentHelper.getSeedRateMod(entrySeeding);
				
				minDownSpeed = Math.min(minDownSpeed, entrySeedRate);
				maxDownSpeed = Math.max(maxDownSpeed, entrySeedRate);
			}
			
			seedsData = Pair.of(filteredData.size(), totalSeeds.get());
			
			//TODO downSpeed includes userSession, making the GristStat visible even if the user is the only one capable of seeding
			typeDownSpeedRange = Pair.of(minDownSpeed, maxDownSpeed);
		}
		
		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v)
		{
			guiGraphics.pose().pushPose();
			guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
			
			drawIcon(getX() + 1, getY() + 1, gristType.getIcon());
			
			//down
			MutableComponent downText = speedAppend(typeDownSpeedRange.getFirst()).append(" - ").append(speedAppend(typeDownSpeedRange.getSecond()));
			guiGraphics.drawString(font, downText, scale(getX() + 25), scale(getY() + TEXT_Y_OFFSET), GristTorrentGui.LIGHT_BLUE, false);
			
			//up
			guiGraphics.drawString(font, speedAppend(typeUpSpeed), scale(getX() + 70), scale(getY() + TEXT_Y_OFFSET), GristTorrentGui.LIGHT_BLUE, false);
			
			//seeds
			guiGraphics.drawString(font, Component.literal(seedsData.getFirst() + "(" + seedsData.getSecond() + ")"), scale(getX() + 110), scale(getY() + TEXT_Y_OFFSET), GristTorrentGui.LIGHT_BLUE, false);
			
			guiGraphics.pose().popPose();
		}
		
		public boolean typeIsActive()
		{
			return typeDownSpeedRange.getSecond() > 0 || typeUpSpeed > 0;
		}
		
		public static MutableComponent speedAppend(int value)
		{
			return Component.literal(GuiUtil.addSuffix(value) + " g/s");
		}
		
		@Override
		protected boolean isValidClickButton(int pButton)
		{
			return false;
		}
		
		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
		{
		}
	}
	
	protected static class StatsContainer extends ScrollingWidget<GristStat>
	{
		public static final int WIDTH = GristStat.WIDTH;
		public static final int HEIGHT = (GristStat.HEIGHT + 1) * 3;
		
		private final Font font;
		
		public StatsContainer(int pX, int pY, Font font)
		{
			super(pX, pY, WIDTH, HEIGHT);
			
			this.font = font;
		}
		
		public void updateStats()
		{
			this.children().clear();
			
			int i = 0;
			for(GristType gristType : GristTypes.REGISTRY)
			{
				GristStat gristStat = new GristStat(this.getX(), this.getY() + 6 + ((GristStat.HEIGHT + 1) * i), font, gristType);
				
				if(gristStat.typeIsActive())
				{
					this.children().add(gristStat);
					i++;
				}
			}
			
			this.updateVisibilityAndPosition();
		}
		
		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
		{
			guiGraphics.pose().pushPose();
			guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
			
			guiGraphics.drawString(font, Component.literal("Grist"), scale(getX() + 2), scale(getY() + 2), GristTorrentGui.LIGHT_BLUE, false);
			
			//down
			guiGraphics.drawString(font, Component.literal("Down Speed"), scale(getX() + 25), scale(getY() + 2), GristTorrentGui.LIGHT_BLUE, false);
			
			//up
			guiGraphics.drawString(font, Component.literal("Up Speed"), scale(getX() + 70), scale(getY() + 2), GristTorrentGui.LIGHT_BLUE, false);
			
			//seeds
			guiGraphics.drawString(font, Component.literal("Seeds"), scale(getX() + 110), scale(getY() + 2), GristTorrentGui.LIGHT_BLUE, false);
			
			guiGraphics.pose().popPose();
			
			super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
		}
		
		@Override
		public int visibleEntryCount()
		{
			return 2;
		}
		
		@Override
		public int subWidgetHeight()
		{
			return GristStat.HEIGHT;
		}
		
		@Override
		public int getMaxScroll()
		{
			return Math.max(0, this.children().size() - visibleEntryCount());
		}
		
		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
		{
		}
	}
	
	protected static class GutterBar extends AbstractWidget
	{
		final int x;
		final int y;
		final int color;
		
		public GutterBar(int pX, int pY, int pWidth, GristAmount gristAmount)
		{
			super(pX, pY, pWidth, 3, Component.empty());
			x = pX;
			y = pY;
			
			GristType gristType = gristAmount.type();
			
			if(gristType == GristTypes.BUILD.get() || gristType == GristTypes.DIAMOND.get())
				color = GristTorrentGui.LIGHT_BLUE;
			else if(gristType == GristTypes.MARBLE.get())
				color = 0xFFFFC0CB; //pink
			else
				color = gristType.getUnderlingColor();
			
			setTooltip(Tooltip.create(gristAmount.type().getDisplayName().append(Component.literal(": " + gristAmount.amount()))));
		}
		
		public GutterBar(int pX, int pY, int pWidth, long remainingCapacity)
		{
			super(pX, pY, pWidth, 3, Component.empty());
			x = pX;
			y = pY;
			color = 0xFFFFFFFF;
			
			//TODO no longer renders
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
		protected boolean isValidClickButton(int pButton)
		{
			return false;
		}
		
		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
		{
		
		}
	}
	
	public abstract static class ScrollingWidget<T extends AbstractWidget> extends AbstractContainerWidget
	{
		private int scroll = 0;
		private final List<T> widgets = new ArrayList<>();
		
		public ScrollingWidget(int pX, int pY, int pWidth, int pHeight)
		{
			super(pX, pY, pWidth, pHeight, Component.empty());
		}
		
		@Override
		public List<T> children()
		{
			return widgets;
		}
		
		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
		{
			for (T widget : this.widgets)
				widget.render(guiGraphics, mouseX, mouseY, partialTick);
		}
		
		@Override
		public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
		{
			//TODO consider inverting scroll direction
			
			if(scrollY > 0)
				scroll = Math.min(getMaxScroll(), scroll + 1);
			else if(scrollY < 0)
				scroll = Math.max(0, scroll - 1);
			
			updateVisibilityAndPosition();
			
			return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
		}
		
		public void updateVisibilityAndPosition()
		{
			for(int i = 0; i < widgets.size(); i++)
			{
				T widget = widgets.get(i);
				
				// Adjust position relative to the visible area
				adjustYValue(widget, i);
				
				widget.visible = i >= scroll && i < scroll + visibleEntryCount();
			}
		}
		
		private void adjustYValue(T widget, int i)
		{
			widget.setY(getY() + (i + 1 - scroll) * (subWidgetHeight() + 1));
		}
		
		public abstract int visibleEntryCount();
		
		public abstract int subWidgetHeight();
		
		public abstract int getMaxScroll();
		
		@Override
		protected boolean isValidClickButton(int pButton)
		{
			return false;
		}
	}
}
