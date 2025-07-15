package com.mraof.minestuck.client.gui.computer;

import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.TorrentSession;
import com.mraof.minestuck.alchemy.TorrentSession.LimitedCache;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.client.gui.computer.TorrentWidgets.*;
import com.mraof.minestuck.computer.ProgramType;
import com.mraof.minestuck.player.ClientPlayerData;
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

@ParametersAreNonnullByDefault
public final class GristTorrentGui extends Screen implements ProgramGui<ProgramType.EmptyData>
{
	public static final String NAME = "minestuck.program.grist_torrent";
	public static final String TITLE = "minestuck.program.grist_torrent.title";
	
	public static final ResourceLocation GUI_MAIN = Minestuck.id("textures/gui/torrent.png");
	
	static final int GUI_WIDTH = 190;
	static final int GUI_HEIGHT = 200;
	
	static final int LIGHT_BLUE = 0xFF19B3EF;
	static final int DARK_GREY = 0xFF333333;
	
	private ComputerBlockEntity computer;
	
	private int xOffset;
	private int yOffset;
	private int gristWidgetsYOffset;
	
	private GristSet gutterGrist;
	private long gutterRemainingCapacity;
	static Map<TorrentSession, LimitedCache> visibleTorrentData = new HashMap<>();
	static TorrentSession userSession;
	
	private final List<Pair<TorrentSession, TorrentContainer>> torrentContainers = new ArrayList<>();
	private final List<GutterBar> gutterBars = new ArrayList<>();
	private StatsContainer statsContainer;
	
	private List<GristType> allGristTypes;
	
	private int updateTick = 0;
	
	public GristTorrentGui()
	{
		super(Component.translatable(TITLE));
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
		Map.Entry<TorrentSession, LimitedCache> userDataEntry = visibleTorrentData.entrySet().stream().filter(entry -> entry.getKey().getSeeder().getUUID().equals(minecraft.player.getUUID())).findFirst().orElse(null);
		if(userDataEntry != null)
			userSession = userDataEntry.getKey();
		
		addTorrentSessions();
		
		statsContainer = new StatsContainer(xOffset + GristStat.X_OFFSET_FROM_EDGE, yOffset + GristStat.Y_OFFSET_FROM_EDGE, font);
		addRenderableWidget(statsContainer);
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
		statsContainer.updateStats();
		
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
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
		for(Pair<TorrentSession, TorrentContainer> torrentPair : torrentContainers)
		{
			TorrentSession entrySession = torrentPair.getFirst();
			if(!visibleTorrentData.containsKey(entrySession))
			{
				addTorrentSessions();
				break;
			}
			
			LimitedCache cache = visibleTorrentData.get(entrySession);
			torrentPair.getSecond().refreshEntries(entrySession, cache);
		}
	}
	
	private void renderGutter(GuiGraphics guiGraphics)
	{
		if(gutterGrist == null)
			return; //TODO consider adding text that says "loading" if this early return is triggered
		
		gutterBars.forEach(this::removeWidget);
		gutterBars.clear();
		
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
			GutterBar bar = new GutterBar(initialX, y, length, gristAmount);
			gutterBars.add(bar);
			addRenderableWidget(bar);
			initialX += length;
		}
		
		int remainingVolume = (int) ((gutterRemainingCapacity / totalVolume) * 100);
		
		GutterBar remainingBar = new GutterBar(initialX, y, remainingVolume, gutterRemainingCapacity);
		gutterBars.add(remainingBar);
		addRenderableWidget(remainingBar);
		
		String remainingText = String.valueOf(filledVolume);
		guiGraphics.drawString(font, remainingText, (xOffset + 105) - font.width(remainingText) / 2, y + 5, LIGHT_BLUE, false);
	}
	
	private void addTorrentSessions()
	{
		if(userSession == null)
			return; //if the users own session isnt visible, there is no point in looking at any
		
		torrentContainers.forEach(pair -> this.removeWidget(pair.getSecond()));
		
		int torrentXOffset = 0;
		
		ClientPlayerData.ClientCache clientCache = ClientPlayerData.getGristCache(ClientPlayerData.CacheSource.PLAYER);
		createTorrentWidgets(userSession, new LimitedCache(clientCache.set().asImmutable(), clientCache.limit()), torrentXOffset);
		torrentXOffset++;
		
		for(Map.Entry<TorrentSession, LimitedCache> entry : visibleTorrentData.entrySet())
		{
			TorrentSession entrySession = entry.getKey();
			LimitedCache entryCache = entry.getValue();
			
			if(entrySession.sameOwner(userSession))
				continue;
			
			createTorrentWidgets(entrySession, entryCache, torrentXOffset);
			torrentXOffset++;
		}
	}
	
	public void createTorrentWidgets(TorrentSession torrentSession, LimitedCache cache, int torrentXOffset)
	{
		int combinedXOffset = xOffset + 5 + ((GristEntry.WIDTH + 2) * torrentXOffset);
		
		//TODO because this is being reset, the tooltip is flashing and the scroll returns to 0 preventing it from moving
		TorrentContainer torrentContainer = new TorrentContainer(combinedXOffset, gristWidgetsYOffset, font, torrentSession.getSeeder(), allGristTypes);
		torrentContainer.refreshEntries(torrentSession, cache);
		torrentContainers.add(Pair.of(torrentSession, torrentContainer));
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
			MSScreenFactories.displayComputerScreen(computer);
			
			return true;
		}
		
		return super.keyPressed(pKeyCode, pScanCode, pModifiers);
	}
	
	@Override
	public void onInit(ComputerScreen screen)
	{
		GristTorrentGui gui = new GristTorrentGui();
		gui.computer = screen.be;
		screen.getMinecraft().setScreen(gui);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, ComputerScreen screen)
	{
		//handled by the screen render method
	}
}
