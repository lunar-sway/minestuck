package com.mraof.minestuck.client.gui.captchalouge;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.captchalouge.TreeModus;
import com.mraof.minestuck.inventory.captchalouge.TreeModus.TreeNode;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.util.Debug;

public class TreeGuiHandler extends SylladexGuiHandler
{
	
	protected TreeModus modus;
	protected int maxDepth;
	protected int xOffset, yOffset;
	protected GuiItem[] guiIndexList;
	protected GuiButton guiButton;
	
	public TreeGuiHandler(TreeModus modus)
	{
		this.modus = modus;
		textureIndex = 3;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		guiButton = new GuiButton(0, (width - GUI_WIDTH)/2 + 15, (height - GUI_HEIGHT)/2 + 175, 120, 20, "");
		buttonList.add(guiButton);
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float f)
	{
		guiButton.xPosition = (width - GUI_WIDTH)/2 + 15;
		guiButton.yPosition = (height - GUI_HEIGHT)/2 + 175;
		boolean autobalance = MinestuckConfig.clientTreeAutobalance == 0 ? modus.autobalance : MinestuckConfig.clientTreeAutobalance == 1;
		guiButton.displayString = StatCollector.translateToLocal(autobalance ? "gui.autobalance.on" : "gui.autobalance.off");
		guiButton.enabled = MinestuckConfig.clientTreeAutobalance == 0;
		super.drawScreen(xcor, ycor, f);
	}
	
	@Override
	public void updateContent()
	{
		maxDepth = getDepthIndex(modus.node);
		int pow = (int) Math.pow(2, maxDepth);
		maxHeight = Math.max(mapHeight, CARD_HEIGHT*(maxDepth+1) + 20*maxDepth + 20);
		maxWidth = Math.max(mapWidth, pow*CARD_WIDTH + 20);
		xOffset = (maxWidth - pow*CARD_WIDTH)/2;
		yOffset = (maxHeight - CARD_HEIGHT*(maxDepth+1) - 20*maxDepth)/2;
		guiIndexList = new GuiItem[(int) Math.pow(2, maxDepth + 1) - 1];
		items.clear();
		addNodes(modus.node, 0, 0, 0);
		
		int size = modus.node == null ? 0 : modus.node.getSize();
		if(MinestuckConfig.clientInfTreeModus || modus.size > size)
		{
			items.add(new ModusSizeCard(this, MinestuckConfig.clientInfTreeModus ? 1 : modus.size - size, 10, 10));
		}
		
		super.updateContent();
	}
	
	protected int getDepthIndex(TreeNode node)
	{
		if(node == null || node.node1 == null && node.node2 == null)
			return 0;
		return Math.max(getDepthIndex(node.node1), getDepthIndex(node.node2)) + 1;
	}
	
	@Override
	public void updatePosition()
	{
		if(maxHeight != Math.max(mapHeight, CARD_HEIGHT*2*maxDepth + CARD_HEIGHT)
				|| maxWidth != Math.max(mapWidth, (int)Math.pow(2, maxDepth - 1)*CARD_WIDTH + 2*CARD_WIDTH))
			updateContent();
	}
	
	@Override
	public void drawGuiMap(int xcor, int ycor)
	{
		super.drawGuiMap(xcor, ycor);
		if(guiIndexList[0] != null)
			drawNodeLines(0, 0);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		if(button == this.guiButton && MinestuckConfig.clientTreeAutobalance == 0)
		{
			modus.autobalance = !modus.autobalance;
			MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.VALUE, (byte) 0, modus.autobalance ? 1 : 0)); 
		}
	}
	
	protected void drawNodeLines(int index, int depth)
	{
		if(depth >= maxDepth)
			return;
		
		GuiItem item = guiIndexList[index];
		
		if(guiIndexList[index*2 + 1] != null)
		{
			GuiItem other = guiIndexList[index*2 + 1];
			
			if(mapX < item.xPos + 10 && mapX + mapWidth > item.xPos + 9 && mapY < item.yPos + CARD_HEIGHT + 6 && mapY + mapHeight > item.yPos + CARD_HEIGHT)
				drawRect(item.xPos + 9 - mapX, item.yPos + CARD_HEIGHT - mapY, item.xPos + 10 - mapX, item.yPos + CARD_HEIGHT - mapY + 6, 0xFF000000);
			
			if(mapX < item.xPos + 10 && mapX + mapWidth > other.xPos + 10 && mapY < item.yPos + CARD_HEIGHT + 6 && mapY + mapHeight > item.yPos + CARD_HEIGHT + 5)
				drawRect(Math.max(0, other.xPos - mapX + 10), item.yPos + CARD_HEIGHT - mapY + 5, Math.min(mapWidth, item.xPos + 10 - mapX), item.yPos + CARD_HEIGHT - mapY + 6, 0xFF000000);
			
			if(mapX < other.xPos + 11 && mapX + mapWidth > other.xPos + 10 && mapY < other.yPos && mapY + mapHeight > item.yPos + CARD_HEIGHT + 5)
				drawRect(other.xPos + 10 - mapX, item.yPos + CARD_HEIGHT + 5 - mapY, other.xPos + 11 - mapX, other.yPos - mapY, 0xFF000000);
			
			drawNodeLines(index*2 + 1, depth + 1);
		}
		
		if(guiIndexList[index*2 + 2] != null)
		{
			GuiItem other = guiIndexList[index*2 + 2];
			
			if(mapX < item.xPos + 12 && mapX + mapWidth > item.xPos + 11 && mapY < item.yPos + CARD_HEIGHT + 6 && mapY + mapHeight > item.yPos + CARD_HEIGHT)
				drawRect(item.xPos + 11 - mapX, item.yPos + CARD_HEIGHT - mapY, item.xPos + 12 - mapX, item.yPos + CARD_HEIGHT - mapY + 6, 0xFF000000);
			
			if(mapX < other.xPos + 10 && mapX + mapWidth > item.xPos + 11 && mapY < item.yPos + CARD_HEIGHT + 6 && mapY + mapHeight > item.yPos + CARD_HEIGHT + 5)
				drawRect(Math.max(0, item.xPos + 11 - mapX), item.yPos + CARD_HEIGHT - mapY + 5, Math.min(mapWidth, other.xPos + 10 - mapX), item.yPos + CARD_HEIGHT - mapY + 6, 0xFF000000);
			
			if(mapX < other.xPos + 10 && mapX + mapWidth > other.xPos + 9 && mapY < other.yPos && mapY + mapHeight > item.yPos + CARD_HEIGHT + 5)
				drawRect(other.xPos + 9 - mapX, item.yPos + CARD_HEIGHT + 5 - mapY, other.xPos + 10 - mapX, other.yPos - mapY, 0xFF000000);
			
			drawNodeLines(index*2 + 1, depth + 1);
		}
		
		if(guiIndexList[index*2 + 2] != null)
			drawNodeLines(index*2 + 2, depth + 1);
		
	}
	
	protected void addNodes(TreeNode node, int index, int pos, int depth)
	{
		if(node == null)
			return;
		GuiItem item = new GuiItem(node.stack, this, index, 0, 0);
		setPosition(item, pos, depth);
		items.add(item);
		guiIndexList[(int) (pos + Math.pow(2, depth)- 1)] = item;
		
		addNodes(node.node1, index + (int) Math.pow(2, depth), pos*2, depth + 1);
		addNodes(node.node2, index + (int) Math.pow(2, depth + 1), pos*2 + 1, depth + 1);
		
	}
	
	protected void setPosition(GuiItem guiItem, int pos, int depth)
	{
		int invertedPow = (int) Math.pow(2, maxDepth - depth);
		double xOffset = ((double)invertedPow - 1)/2;
		xOffset += Math.pow(2, maxDepth - depth)*pos;
		guiItem.xPos = this.xOffset + (int)(xOffset*CARD_WIDTH);
		guiItem.yPos = yOffset + depth*(CARD_HEIGHT + 20);
	}
	
}
