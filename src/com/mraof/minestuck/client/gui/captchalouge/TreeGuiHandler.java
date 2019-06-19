package com.mraof.minestuck.client.gui.captchalouge;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.GuiButtonImpl;
import com.mraof.minestuck.inventory.captchalogue.TreeModus;
import com.mraof.minestuck.inventory.captchalogue.TreeModus.TreeNode;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TreeGuiHandler extends SylladexGuiHandler
{
	
	protected TreeModus modus;
	protected int maxDepth;
	protected int xOffset, yOffset;
	protected GuiCard[] guiIndexList;
	protected GuiButtonImpl guiButton;
	
	public TreeGuiHandler(TreeModus modus)
	{
		this.modus = modus;
		textureIndex = 3;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		guiButton = new GuiButtonImpl(this, 0, (width - GUI_WIDTH)/2 + 15, (height - GUI_HEIGHT)/2 + 175, 120, 20, "");
		buttons.add(guiButton);
	}
	
	@Override
	public void render(int xcor, int ycor, float f)
	{
		guiButton.x = (width - GUI_WIDTH)/2 + 15;
		guiButton.y = (height - GUI_HEIGHT)/2 + 175;
		boolean autobalance = MinestuckConfig.clientTreeAutobalance == 0 ? modus.autoBalance : MinestuckConfig.clientTreeAutobalance == 1;
		guiButton.displayString = I18n.format(autobalance ? "gui.autobalance.on" : "gui.autobalance.off");
		guiButton.enabled = MinestuckConfig.clientTreeAutobalance == 0;
		super.render(xcor, ycor, f);
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
		guiIndexList = new GuiCard[(int) Math.pow(2, maxDepth + 1) - 1];
		cards.clear();
		addNodes(modus.node, 0, 0, 0);
		
		int size = modus.node == null ? 0 : modus.node.getSize();
		if(modus.size > size)
		{
			cards.add(new ModusSizeCard(this, modus.size - size, 10, 10));
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
	public void actionPerformed(GuiButtonImpl button)
	{
		super.actionPerformed(button);
		if(button == this.guiButton && MinestuckConfig.clientTreeAutobalance == 0)
		{
			modus.autoBalance = !modus.autoBalance;
			MinestuckPacketHandler.sendToServer(CaptchaDeckPacket.modusParam((byte) 0, modus.autoBalance ? 1 : 0));
		}
	}
	
	protected void drawNodeLines(int index, int depth)
	{
		if(depth >= maxDepth)
			return;
		
		GuiCard card = guiIndexList[index];
		
		if(guiIndexList[index*2 + 1] != null)
		{
			GuiCard other = guiIndexList[index*2 + 1];
			
			if(mapX < card.xPos + 10 && mapX + mapWidth > card.xPos + 9 && mapY < card.yPos + CARD_HEIGHT + 6 && mapY + mapHeight > card.yPos + CARD_HEIGHT)
				drawRect(card.xPos + 9 - mapX, card.yPos + CARD_HEIGHT - mapY, card.xPos + 10 - mapX, card.yPos + CARD_HEIGHT - mapY + 6, 0xFF000000);
			
			if(mapX < card.xPos + 10 && mapX + mapWidth > other.xPos + 10 && mapY < card.yPos + CARD_HEIGHT + 6 && mapY + mapHeight > card.yPos + CARD_HEIGHT + 5)
				drawRect(Math.max(0, other.xPos - mapX + 10), card.yPos + CARD_HEIGHT - mapY + 5, Math.min(mapWidth, card.xPos + 10 - mapX), card.yPos + CARD_HEIGHT - mapY + 6, 0xFF000000);
			
			if(mapX < other.xPos + 11 && mapX + mapWidth > other.xPos + 10 && mapY < other.yPos && mapY + mapHeight > card.yPos + CARD_HEIGHT + 5)
				drawRect(other.xPos + 10 - mapX, card.yPos + CARD_HEIGHT + 5 - mapY, other.xPos + 11 - mapX, other.yPos - mapY, 0xFF000000);
			
			drawNodeLines(index*2 + 1, depth + 1);
		}
		
		if(guiIndexList[index*2 + 2] != null)
		{
			GuiCard other = guiIndexList[index*2 + 2];
			
			if(mapX < card.xPos + 12 && mapX + mapWidth > card.xPos + 11 && mapY < card.yPos + CARD_HEIGHT + 6 && mapY + mapHeight > card.yPos + CARD_HEIGHT)
				drawRect(card.xPos + 11 - mapX, card.yPos + CARD_HEIGHT - mapY, card.xPos + 12 - mapX, card.yPos + CARD_HEIGHT - mapY + 6, 0xFF000000);
			
			if(mapX < other.xPos + 10 && mapX + mapWidth > card.xPos + 11 && mapY < card.yPos + CARD_HEIGHT + 6 && mapY + mapHeight > card.yPos + CARD_HEIGHT + 5)
				drawRect(Math.max(0, card.xPos + 11 - mapX), card.yPos + CARD_HEIGHT - mapY + 5, Math.min(mapWidth, other.xPos + 10 - mapX), card.yPos + CARD_HEIGHT - mapY + 6, 0xFF000000);
			
			if(mapX < other.xPos + 10 && mapX + mapWidth > other.xPos + 9 && mapY < other.yPos && mapY + mapHeight > card.yPos + CARD_HEIGHT + 5)
				drawRect(other.xPos + 9 - mapX, card.yPos + CARD_HEIGHT + 5 - mapY, other.xPos + 10 - mapX, other.yPos - mapY, 0xFF000000);
			
			drawNodeLines(index*2 + 1, depth + 1);
		}
		
		if(guiIndexList[index*2 + 2] != null)
			drawNodeLines(index*2 + 2, depth + 1);
		
	}
	
	protected void addNodes(TreeNode node, int index, int pos, int depth)
	{
		if(node == null)
			return;
		GuiCard card = new GuiCard(node.stack, this, index, 0, 0);
		setPosition(card, pos, depth);
		cards.add(card);
		guiIndexList[(int) (pos + Math.pow(2, depth)- 1)] = card;
		
		addNodes(node.node1, index + (int) Math.pow(2, depth), pos*2, depth + 1);
		addNodes(node.node2, index + (int) Math.pow(2, depth + 1), pos*2 + 1, depth + 1);
		
	}
	
	protected void setPosition(GuiCard guiCard, int pos, int depth)
	{
		int invertedPow = (int) Math.pow(2, maxDepth - depth);
		double xOffset = ((double)invertedPow - 1)/2;
		xOffset += Math.pow(2, maxDepth - depth)*pos;
		guiCard.xPos = this.xOffset + (int)(xOffset*CARD_WIDTH);
		guiCard.yPos = yOffset + depth*(CARD_HEIGHT + 20);
	}
	
}
