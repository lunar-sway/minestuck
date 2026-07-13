package com.mraof.minestuck.client.gui.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.TorrentSession;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.client.gui.computer.TorrentWidgets.*;
import com.mraof.minestuck.computer.ProgramType;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.mraof.minestuck.client.gui.computer.TorrentWidgets.scale;

@ParametersAreNonnullByDefault
public final class GristTorrentGui extends Screen implements ProgramGui<ProgramType.EmptyData>
{
	public static final String NAME = "minestuck.program.grist_torrent";
	public static final String TITLE = "minestuck.program.grist_torrent.title";
	
	public static final String TOOLTIP_SEEDING_ON = "minestuck.seeding.on";
	public static final String TOOLTIP_SEEDING_OFF = "minestuck.seeding.off";
	public static final String TOOLTIP_LEECHING_ON = "minestuck.leeching.on";
	public static final String TOOLTIP_LEECHING_OFF = "minestuck.leeching.off";
	public static final String GUTTER_LOADING = "minestuck.gutter.loading";
	
	public static final ResourceLocation GUI_MAIN = Minestuck.id("textures/gui/torrent_menu.png");
	
	private static final ResourceLocation BUILD_ICON = Minestuck.id("textures/grist/build.png");
	private static final ResourceLocation TORRENT_ICON = Minestuck.id("textures/gui/torrent.png");
	
	static final int GUI_WIDTH = 190;
	static final int GUI_HEIGHT = 200;
	private GatesContainer gatesContainer;
	
	static final int LIGHT_BLUE = 0xFF19B3EF;
	static final int LIGHT_GREY = 0xFFF5F5F5;
	static final int GREY = 0xFFCFCFCF;
	static final int DARK_GREY = 0xFF333333;
	
	private ComputerBlockEntity computer;
	
	private int xOffset;
	private int yOffset;
	private int gristWidgetsYOffset;
	
	private GristSet gutterGrist;
	private long filledVolume = 0;
	private GristSet previousGutterGrist = null;
	private long gutterRemainingCapacity;
	static final Map<Integer, TorrentSession.TorrentClientData> visibleTorrentData = new HashMap<>();
	
	private TorrentContainerRow torrentContainerRow;
	private final List<GutterBar> gutterBars = new ArrayList<>();
	private StatsContainer statsContainer;
	private boolean gutterLoading = true;
	private FilterContainer filterContainer;
	
	private int updateTick = 0;
	
	public enum TorrentFilter
	{
		ALL,
		DOWNLOADING,
		COMPLETED,
		ACTIVE,
		INACTIVE;
		
		@Override
		public String toString()
		{
			String title = this.name();
			return title.charAt(0) + title.substring(1).toLowerCase();
		}
	}
	
	protected TorrentFilter activeFilter = TorrentFilter.ALL;
	
	public GristTorrentGui()
	{
		super(Component.translatable(TITLE));
	}
	
	@Override
	public void init()
	{
		yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		xOffset = (this.width / 2) - (GUI_WIDTH / 2);
		gristWidgetsYOffset = yOffset + 39;
		
		gutterGrist = ClientPlayerData.getGutterSet();
		visibleTorrentData.clear();
		visibleTorrentData.putAll(ClientPlayerData.getVisibleTorrentData());
		
		if(minecraft == null || minecraft.player == null)
			return;
		torrentContainerRow = new TorrentContainerRow(xOffset + 5, gristWidgetsYOffset);
		addRenderableWidget(torrentContainerRow);
		addTorrentSessions();
		
		ScrollArrowButton leftArrow = new ScrollArrowButton(
				xOffset - 12, gristWidgetsYOffset, TorrentContainer.HEIGHT,
				ScrollArrowButton.Direction.LEFT, torrentContainerRow);
		addRenderableWidget(leftArrow);
		
		ScrollArrowButton rightArrow = new ScrollArrowButton(
				xOffset + 190 + 2, gristWidgetsYOffset, TorrentContainer.HEIGHT,
				ScrollArrowButton.Direction.RIGHT, torrentContainerRow);
		addRenderableWidget(rightArrow);
		
		statsContainer = new StatsContainer(xOffset + GristStat.X_OFFSET_FROM_EDGE, yOffset + GristStat.Y_OFFSET_FROM_EDGE, font);
		addRenderableWidget(statsContainer);
		
		filterContainer = new FilterContainer(xOffset + FilterContainer.X_OFFSET_FROM_EDGE, yOffset + FilterContainer.Y_OFFSET_FROM_EDGE, font, this);
		addRenderableWidget(filterContainer);
		
		int gateX = xOffset + 122;
		int gateY = yOffset + 184 - (int) (47 * 0.27F) - 2;
		gatesContainer = new GatesContainer(gateX, gateY);
		gatesContainer.setPlayers(visibleTorrentData);
		addRenderableWidget(gatesContainer);
		
		HorizontalScrollBar gatesScrollBar = new HorizontalScrollBar(
				gateX, gateY - 3,
				55, 1, gatesContainer);
		addRenderableWidget(gatesScrollBar);
		
		updateGutterBars();
	}
	
	public void setFilter(TorrentFilter filter)
	{
		activeFilter = filter;
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
		clientDataUpdates();
		statsContainer.updateStats(activeFilter);
		filterContainer.updateCounts();
		
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		int gutterX = xOffset + 53;
		int gutterY = yOffset + 185;
		
		int iconSize = 16;
		
		int buildX = gutterX - 6 - iconSize;
		int torrentX = buildX - 12 - iconSize;
		int iconY = gutterY - (iconSize - 3) / 2 - 8;
		
		TorrentWidgets.drawIcon(xOffset + 44, yOffset + 3, TORRENT_ICON, 1.25f);
		TorrentWidgets.drawIcon(buildX, iconY, BUILD_ICON, 1.25f);
		TorrentWidgets.drawIcon(torrentX, iconY, TORRENT_ICON, 1.25f);
		
		if(gutterLoading)
		{
			Component loading = Component.translatable(GUTTER_LOADING);
			guiGraphics.drawString(font, loading, xOffset + 92 - font.width(loading) / 2, yOffset + 185 + 5, DARK_GREY, false);
		} else
		{
			String volumeText = String.valueOf(filledVolume);
			guiGraphics.pose().pushPose();
			guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
			guiGraphics.drawString(font, volumeText, scale(xOffset + 105), scale(yOffset + 185 + 5), LIGHT_BLUE, false);
			guiGraphics.pose().popPose();
		}
	}
	
	private void clientDataUpdates()
	{
		if(updateTick % 20 == 0)
		{
			gutterGrist = ClientPlayerData.getGutterSet();
			gutterRemainingCapacity = ClientPlayerData.getGutterRemainingCapacity();
			visibleTorrentData.clear();
			visibleTorrentData.putAll(ClientPlayerData.getVisibleTorrentData());
			gatesContainer.setPlayers(visibleTorrentData);
			statsContainer.trackDownloads();
			
			//TODO update gutter bar data
			if(!gutterGrist.equals(previousGutterGrist))
			{
				updateGutterBars();
				previousGutterGrist = gutterGrist;
			}
			renderTorrentSessions();
		}
		
		updateTick++;
	}
	
	private void renderTorrentSessions()
	{
		for(TorrentContainer container : torrentContainerRow.children())
		{
			if(!visibleTorrentData.containsKey(container.playerId))
			{
				addTorrentSessions();
				break;
			}
			TorrentSession.TorrentClientData data = visibleTorrentData.get(container.playerId);
			container.refreshEntries(data);
		}
	}
	
	private void updateGutterBars()
	{
		if(gutterGrist == null)
		{
			gutterLoading = true;
			return;
		}
		gutterLoading = false;
		
		filledVolume = 0;
		for(GristAmount amount : gutterGrist.asAmounts())
			filledVolume += amount.amount();
		
		double totalVolume = filledVolume + gutterRemainingCapacity;
		
		record BarData(int width, Object payload)
		{
		}
		List<BarData> newBars = new ArrayList<>();
		
		if(totalVolume > 0)
		{
			final int BAR_TOTAL_WIDTH = 117;
			int allocatedWidth = 0;
			
			List<GristAmount> amounts = gutterGrist.asAmounts().stream()
					.filter(a -> a.amount() > 0)
					.toList();
			
			for(int i = 0; i < amounts.size(); i++)
			{
				GristAmount gristAmount = amounts.get(i);
				boolean isLast = (i == amounts.size() - 1) && gutterRemainingCapacity <= 0;
				int w = isLast
						? BAR_TOTAL_WIDTH - allocatedWidth
						: (int) Math.round((gristAmount.amount() / totalVolume) * BAR_TOTAL_WIDTH);
				if(w <= 0) continue;
				allocatedWidth += w;
				newBars.add(new BarData(w, gristAmount));
			}
			
			if(gutterRemainingCapacity > 0)
			{
				int remainingWidth = BAR_TOTAL_WIDTH - allocatedWidth;
				if(remainingWidth > 0)
					newBars.add(new BarData(remainingWidth, gutterRemainingCapacity));
			}
		}
		
		boolean changed = gutterBars.size() != newBars.size();
		if(!changed)
		{
			changed = IntStream.range(0, gutterBars.size())
					.anyMatch(i -> gutterBars.get(i).getWidth() != newBars.get(i).width());
		}
		
		if(!changed) return;
		
		gutterBars.forEach(this::removeWidget);
		gutterBars.clear();
		
		if(newBars.isEmpty()) return;
		
		final int barStartX = xOffset + 53;
		final int barY = yOffset + 185;
		int currentX = barStartX;
		
		for(BarData bd : newBars)
		{
			GutterBar bar = switch(bd.payload())
			{
				case GristAmount ga -> new GutterBar(currentX, barY, bd.width(), ga);
				case Long cap -> new GutterBar(currentX, barY, bd.width(), cap);
				default -> throw new IllegalStateException();
			};
			gutterBars.add(bar);
			addRenderableWidget(bar);
			currentX += bd.width();
		}
	}
	
	private void addTorrentSessions()
	{
		TorrentSession.TorrentClientData userData = visibleTorrentData.get(SkaiaClient.playerId);
		if(userData == null) return;
		
		torrentContainerRow.children().forEach(this::removeWidget);
		torrentContainerRow.children().clear();
		
		createTorrentWidget(SkaiaClient.playerId, userData);
		
		for(Map.Entry<Integer, TorrentSession.TorrentClientData> entry : visibleTorrentData.entrySet())
		{
			if(entry.getKey() == SkaiaClient.playerId) continue;
			createTorrentWidget(entry.getKey(), entry.getValue());
		}
		
		torrentContainerRow.updateVisibilityAndPosition();
	}
	
	private void createTorrentWidget(int playerId, TorrentSession.TorrentClientData torrentData)
	{
		TorrentContainer container = new TorrentContainer(
				torrentContainerRow.getX(), gristWidgetsYOffset, font, playerId, torrentData.username());
		container.refreshEntries(torrentData);
		torrentContainerRow.children().add(container);
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
			MSScreenFactories.displayComputerScreen(computer);
			
			return true;
		}
		
		return super.keyPressed(pKeyCode, pScanCode, pModifiers);
	}
	
	@Override
	public void onInit(ThemedScreen screen)
	{
		GristTorrentGui gui = new GristTorrentGui();
		gui.computer = screen.computer;
		screen.getMinecraft().setScreen(gui);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, ThemedScreen screen)
	{
		//handled by the screen render method
	}
}