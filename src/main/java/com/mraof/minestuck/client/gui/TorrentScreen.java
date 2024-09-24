package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.TorrentHelper;
import com.mraof.minestuck.alchemy.TorrentSession;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.network.TorrentPackets;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@ParametersAreNonnullByDefault
public class TorrentScreen extends Screen
{
	public static final String TITLE = "minestuck.computer_themes"; //TODO
	
	public static final ResourceLocation GUI_MAIN = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/torrent.png");
	
	private static final int GUI_WIDTH = 190;
	private static final int GUI_HEIGHT = 200;
	
	private static final int LIGHT_BLUE = 0xFF19B3EF;
	
	private final ComputerBlockEntity computer;
	
	private int xOffset;
	private int yOffset;
	private int gristWidgetsYOffset;
	
	private GristSet gutterGrist;
	private long gutterRemainingCapacity;
	private static Map<TorrentSession, TorrentSession.LimitedCache> visibleTorrentData = new HashMap<>();
	private static TorrentSession userSession;
	
	private final List<TorrentContainer> torrentContainers = new ArrayList<>();
	private final List<GristStat> gristStats = new ArrayList<>();
	private final List<GutterBarWidget> gutterBars = new ArrayList<>();
	
	private List<GristType> allGristTypes;
	
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
		gristWidgetsYOffset = yOffset + 36;
		
		gutterGrist = ClientPlayerData.getGutterSet();
		visibleTorrentData = ClientPlayerData.getVisibleTorrentData();
		
		this.allGristTypes = GristTypes.REGISTRY.stream().toList();
		
		if(minecraft == null || minecraft.player == null)
			return;
		Map.Entry<TorrentSession, TorrentSession.LimitedCache> userDataEntry = visibleTorrentData.entrySet().stream().filter(entry -> entry.getKey().getSeeder().getUUID().equals(minecraft.player.getUUID())).findFirst().orElse(null);
		if(userDataEntry != null)
			userSession = userDataEntry.getKey();
		
		addTorrentSessions();
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
		
		clientDataUpdates();
		renderGristStats();
		renderGutter(guiGraphics);
	}
	
	private void clientDataUpdates()
	{
		if(updateTick % 20 == 0)
		{
			gutterGrist = ClientPlayerData.getGutterSet();
			gutterRemainingCapacity = ClientPlayerData.getGutterRemainingCapacity();
			visibleTorrentData = ClientPlayerData.getVisibleTorrentData();
			
			//TODO update gutter bar data
			renderTorrentSessions();
		}
		
		updateTick++;
	}
	
	private void renderTorrentSessions()
	{
		for(TorrentContainer torrentContainer : torrentContainers)
		{
			TorrentSession entrySession = torrentContainer.torrentSession;
			if(!visibleTorrentData.containsKey(entrySession))
			{
				addTorrentSessions();
				break;
			}
			
			TorrentSession.LimitedCache cache = visibleTorrentData.get(entrySession);
			
			torrentContainer.gristEntries.forEach(gristEntry ->
			{
				gristEntry.cache = cache;
				gristEntry.cacheLimit = cache.limit();
				gristEntry.gristAmount = visibleTorrentData.get(entrySession).set().getGrist(gristEntry.gristType);
			});
		}
	}
	
	private void renderGristStats()
	{
		gristStats.forEach(this::removeWidget);
		
		int i = 0;
		for(GristType gristType : allGristTypes)
		{
			GristStat gristStat = new GristStat(xOffset + GristStat.X_OFFSET_FROM_EDGE, yOffset + GristStat.Y_OFFSET_FROM_EDGE + ((GristStat.HEIGHT + 1) * i), font, gristType);
			
			if(gristStat.typeIsActive())
			{
				gristStats.add(gristStat);
				addRenderableWidget(gristStat);
				
				i++;
			}
		}
	}
	
	private void renderGutter(GuiGraphics guiGraphics)
	{
		if(gutterGrist == null)
			return; //TODO consider adding text that says "loading" if this early return is triggered
		
		gutterBars.forEach(this::removeWidget);
		
		double totalVolume = gutterRemainingCapacity;
		long filledVolume = 0;
		
		for(GristAmount amount : gutterGrist.asAmounts())
			filledVolume += amount.amount();
		
		totalVolume += filledVolume;
		
		int initialX = xOffset + 55;
		int y = yOffset + 181;
		
		for(GristAmount gristAmount : gutterGrist.asAmounts())
		{
			int length = (int) ((gristAmount.amount() / totalVolume) * 100);
			GutterBarWidget bar = new GutterBarWidget(initialX, y, length, gristAmount);
			gutterBars.add(bar);
			addRenderableWidget(bar);
			initialX += length;
		}
		
		int remainingVolume = (int) ((gutterRemainingCapacity / totalVolume) * 100);
		
		GutterBarWidget remainingBar = new GutterBarWidget(initialX, y, remainingVolume, gutterRemainingCapacity);
		gutterBars.add(remainingBar);
		addRenderableWidget(remainingBar);
		
		String remainingText = String.valueOf(filledVolume);
		guiGraphics.drawString(font, remainingText, (xOffset + 105) - font.width(remainingText) / 2, y + 5, LIGHT_BLUE, false);
	}
	
	private void addTorrentSessions()
	{
		if(userSession == null)
			return; //if the users own session isnt visible, there is no point in looking at any
		
		torrentContainers.forEach(container -> container.gristEntries.forEach(this::removeWidget));
		torrentContainers.forEach(this::removeWidget);
		
		int torrentXOffset = 0;
		
		ClientPlayerData.ClientCache clientCache = ClientPlayerData.getGristCache(ClientPlayerData.CacheSource.PLAYER);
		createTorrentWidgets(userSession, new TorrentSession.LimitedCache(clientCache.set().asImmutable(), clientCache.limit()), torrentXOffset);
		torrentXOffset++;
		
		for(Map.Entry<TorrentSession, TorrentSession.LimitedCache> entry : visibleTorrentData.entrySet())
		{
			TorrentSession entrySession = entry.getKey();
			TorrentSession.LimitedCache entryCache = entry.getValue();
			
			if(entrySession.sameOwner(userSession))
				continue;
			
			createTorrentWidgets(entrySession, entryCache, torrentXOffset);
			torrentXOffset++;
		}
	}
	
	public void createTorrentWidgets(TorrentSession torrentSession, TorrentSession.LimitedCache cache, int torrentXOffset)
	{
		int combinedXOffset = xOffset + 5 + ((GristEntry.WIDTH + 2) * torrentXOffset);
		
		List<GristEntry> gristEntries = new ArrayList<>();
		
		int yOffset = 1; //this is 1 because there needs to be room to render the name of the torrent's seeder
		for(GristType type : allGristTypes)
		{
			GristEntry gristEntry = new GristEntry(combinedXOffset, gristWidgetsYOffset + ((GristEntry.HEIGHT + 1) * yOffset), type);
			
			gristEntries.add(addRenderableWidget(gristEntry));
			
			yOffset++;
		}
		
		TorrentContainer torrentContainer = new TorrentContainer(combinedXOffset, gristWidgetsYOffset, torrentSession, cache, font, gristEntries, allGristTypes);
		torrentContainers.add(torrentContainer);
		addRenderableWidget(torrentContainer);
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
	
	protected static class GristEntry extends AbstractWidget
	{
		public static final int WIDTH = 40;
		public static final int HEIGHT = 14;
		public static final int GRIST_ICON_X = 2, GRIST_ICON_Y = 2;
		public static final int GRIST_COUNT_X = GRIST_ICON_X + 13;
		public static final float BAR_WIDTH = 20F;
		
		public TorrentSession torrentSession;
		public TorrentSession.LimitedCache cache;
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
			
			TorrentScreen.drawIcon(x + GRIST_ICON_X, gristIconYMod, gristType.getIcon());
			
			//renders amount of grist
			String amount = GuiUtil.addSuffix(gristAmount);
			guiGraphics.drawString(font, amount, scale(gristCountXMod), scale(gristIconYMod + 7), 0x19b3ef, false);
			
			//renders bars
			guiGraphics.fill(scale(gristCountXMod), scale(gristIconYMod + 1), scale((int) (gristCountXMod + BAR_WIDTH)), scale(gristIconYMod + 6), 0xFF444444);
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
			int color = 0xFF333333;
			
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
				PacketDistributor.SERVER.noArg().send(new TorrentPackets.ModifySeeding(gristType, isActive));
			else
				PacketDistributor.SERVER.noArg().send(new TorrentPackets.ModifyLeeching(torrentSession, gristType, isActive));
		}
		
		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
		{
		}
	}
	
	protected static class TorrentContainer extends AbstractWidget
	{
		public static final int WIDTH = GristEntry.WIDTH + 2;
		public static final int HEIGHT = (GristEntry.HEIGHT + 1) * 6;
		
		public final TorrentSession torrentSession;
		private final Font font;
		public List<GristEntry> gristEntries;
		
		private final List<GristType> allGristTypes;
		
		private final int gristWidgetsYOffset;
		private int scroll = 0;
		
		public TorrentContainer(int pX, int pY, TorrentSession torrentSession, TorrentSession.LimitedCache cache, Font font, List<GristEntry> gristEntries, List<GristType> allGristTypes)
		{
			super(pX, pY, WIDTH, HEIGHT, Component.empty());
			
			this.torrentSession = torrentSession;
			this.font = font;
			this.gristEntries = gristEntries;
			this.allGristTypes = allGristTypes;
			
			gristWidgetsYOffset = pY;
			
			completeGristEntryInit(torrentSession, cache, font, gristEntries);
		}
		
		private static void completeGristEntryInit(TorrentSession torrentSession, TorrentSession.LimitedCache cache, Font font, List<GristEntry> gristEntries)
		{
			for(int i = 0; i < gristEntries.size(); i++)
			{
				GristEntry gristEntry = gristEntries.get(i);
				if(i < 5)
					gristEntry.visible = true;
				
				GristType entryGristType = gristEntry.gristType;
				
				gristEntry.torrentSession = torrentSession;
				gristEntry.isOwner = userSession.sameOwner(torrentSession);
				gristEntry.isActive = gristEntry.isOwner ? torrentSession.getSeeding().contains(entryGristType) : torrentSession.isLeechForGristType(userSession.getSeeder(), entryGristType);
				gristEntry.font = font;
				gristEntry.cache = cache;
				gristEntry.gristAmount = cache.set().getGrist(entryGristType);
				gristEntry.cacheLimit = cache.limit();
				
				gristEntry.setTooltip();
			}
		}
		
		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v)
		{
			String text = torrentSession.getSeeder().getUsername();
			guiGraphics.pose().pushPose();
			guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
			guiGraphics.drawString(font, text, scale((getX() + WIDTH / 2)) - scale(font.width(text) / 2), scale(getY() + 4), 0xFF000000, false);
			guiGraphics.pose().popPose();
		}
		
		@Override
		public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
		{
			//TODO consider inverting scroll direction
			int maxScroll = Math.max(0, allGristTypes.size() - 5);
			
			if(scrollY > 0)
				scroll = Math.min(maxScroll, scroll + 1);
			else if(scrollY < 0)
				scroll = Math.max(0, scroll - 1);
			
			//update visibility and positions
			for(int i = 0; i < gristEntries.size(); i++)
			{
				GristEntry gristEntry = gristEntries.get(i);
				
				gristEntry.setY(gristWidgetsYOffset + (i + 1 - scroll) * (GristEntry.HEIGHT + 1)); // Adjust position relative to the visible area
				
				if(i >= scroll && i < scroll + 5)
				{
					gristEntry.visible = true;
					gristEntry.setTooltip();
				} else
				{
					gristEntry.visible = false;
				}
			}
			
			return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
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
	
	protected static class GristStat extends AbstractWidget
	{
		public static final int X_OFFSET_FROM_EDGE = 40;
		public static final int Y_OFFSET_FROM_EDGE = 130;
		public static final int WIDTH = GUI_WIDTH - X_OFFSET_FROM_EDGE;
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
			
			updateStats();
		}
		
		public void updateStats()
		{
			TorrentSession.LimitedCache userCache = visibleTorrentData.get(userSession);
			
			int minDownSpeed = Integer.MAX_VALUE;
			int maxDownSpeed = 1;
			AtomicInteger totalSeeds = new AtomicInteger(); //TODO is this appropriate?
			
			Map<TorrentSession, TorrentSession.LimitedCache> filteredData = new HashMap<>();
			visibleTorrentData.forEach((key, value) -> {
				boolean couldSeed = value.set().asAmounts().stream().anyMatch(gristAmount -> gristAmount.hasType(gristType) && !gristAmount.isEmpty());
				boolean tryingToSeed = key.getSeeding().stream().anyMatch(iterateType -> iterateType.equals(gristType));
				boolean userTryingToLeech = key.isLeechForGristType(userSession.getSeeder(), gristType);
				
				if(tryingToSeed)
				{
					if(couldSeed)
						totalSeeds.addAndGet(1);
					
					if(userTryingToLeech)
						filteredData.put(key, value);
				}
			}); //only include data if the user is trying to leech the grist
			
			if(filteredData.isEmpty())
				return; //widget will render due to how min/max speeds are obtained without this early return
			
			List<GristType> userSeeding = userSession.getViableSeeding(userCache);
			typeUpSpeed = TorrentHelper.getSeedRateMod(userSeeding);
			
			for(Map.Entry<TorrentSession, TorrentSession.LimitedCache> dataEntry : filteredData.entrySet())
			{
				TorrentSession entrySession = dataEntry.getKey();
				TorrentSession.LimitedCache entryCache = dataEntry.getValue();
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
			
			TorrentScreen.drawIcon(getX() + 1, getY() + 1, gristType.getIcon());
			
			//down
			MutableComponent downText = speedAppend(typeDownSpeedRange.getFirst()).append(" - ").append(speedAppend(typeDownSpeedRange.getSecond()));
			guiGraphics.drawString(font, downText, scale(getX() + 15), scale(getY() + TEXT_Y_OFFSET), LIGHT_BLUE, false);
			
			//up
			guiGraphics.drawString(font, speedAppend(typeUpSpeed), scale(getX() + 70), scale(getY() + TEXT_Y_OFFSET), LIGHT_BLUE, false);
			
			//seeds
			guiGraphics.drawString(font, Component.literal(seedsData.getFirst() + "(" + seedsData.getSecond() + ")"), scale(getX() + 110), scale(getY() + TEXT_Y_OFFSET), LIGHT_BLUE, false);
			
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
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
		{
		}
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
		
		BufferBuilder render = Tesselator.getInstance().getBuilder();
		render.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		
		render.vertex(x, y + scaledIconY, 0D).uv((iconU) * scale, (iconV + iconY) * scale).endVertex();
		render.vertex(x + scaledIconX, y + scaledIconY, 0D).uv((iconU + iconX) * scale, (iconV + iconY) * scale).endVertex();
		render.vertex(x + scaledIconX, y, 0D).uv((iconU + iconX) * scale, (iconV) * scale).endVertex();
		render.vertex(x, y, 0D).uv((iconU) * scale, (iconV) * scale).endVertex();
		
		Tesselator.getInstance().end();
	}
}