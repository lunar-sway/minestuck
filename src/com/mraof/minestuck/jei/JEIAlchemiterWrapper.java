package com.mraof.minestuck.jei;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;

import mezz.jei.api.recipe.BlankRecipeWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class JEIAlchemiterWrapper extends BlankRecipeWrapper {
	@Nonnull
	private final ItemStack output;
	@Nullable
	private final GristSet gristCosts;

	public JEIAlchemiterWrapper(@Nonnull ItemStack output, GristSet gristCosts){
		this.output = output;
		this.gristCosts = gristCosts;
	}

	@Override
	public List<ItemStack> getOutputs() {
		return Collections.singletonList(output);
	}

	public GristSet getgristCosts() {
		return gristCosts;
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
		if (gristCosts == null)
		{
			
			minecraft.fontRendererObj.drawString(StatCollector.translateToLocal("gui.notAlchemizable"), 4,34, 16711680);
			return;
		}
		GristSet playerGrist = MinestuckPlayerData.getClientGrist();
		
		if (gristCosts.isEmpty())
		{
			minecraft.fontRendererObj.drawString(StatCollector.translateToLocal("gui.free"), 4,34, 65280);
			return;
		}

		if(!MinestuckConfig.alchemyIcons)
		{
			int place = 0;
			for(GristAmount entry : gristCosts.getArray())
			{
				GristType type = entry.getType();
				int need = entry.getAmount();
				int have = playerGrist.getGrist(type);
				
				int row = place % 3;
				int col = place / 3;
				
				int color = need <= have ? 0x00FF00 : 0xFF0000; //Green if we have enough grist, red if not
				
				String needStr = GuiHandler.addSuffix(need), haveStr = GuiHandler.addSuffix(have);
				minecraft.fontRendererObj.drawString(needStr + " " + type.getDisplayName() + " (" + haveStr + ")", 4 + 79*col, 34 + 8*row, color);
				
				place++;
			}
		} else
		{
			int index = 0;
			for(GristAmount entry : gristCosts.getArray())
			{
				GristType type = entry.getType();
				int need = entry.getAmount();
				int have = playerGrist.getGrist(type);
				int row = index/158;
				int color = need <= have ? 0x00FF00 : 0xFF0000;
				
				String needStr = GuiHandler.addSuffix(need), haveStr = GuiHandler.addSuffix(have);
				haveStr = '('+haveStr+')';
				int needStrWidth = minecraft.fontRendererObj.getStringWidth(needStr);
				
				if(index + needStrWidth + 10 + minecraft.fontRendererObj.getStringWidth(haveStr) > (row + 1)*158)
				{
					row++;
					index = row*158;
				}
				
				minecraft.fontRendererObj.drawString(needStr, 2 + index%158, 31 + 8*row, color);
				minecraft.fontRendererObj.drawString(haveStr, needStrWidth + 14 + index%158, 31 + (8 * (row)), color);
				
				GlStateManager.color(1, 1, 1);
				minecraft.getTextureManager().bindTexture(new ResourceLocation("minestuck", "textures/grist/" + type.getName()+ ".png"));
				Gui.drawModalRectWithCustomSizedTexture(needStrWidth + 3 + index%158, 31 + 8*row, 0, 0, 8, 8, 8, 8);
				
				index += needStrWidth + 10 + minecraft.fontRendererObj.getStringWidth(haveStr);
				index = Math.min(index + 6, (row + 1)*158);
			}
		}
	}
}