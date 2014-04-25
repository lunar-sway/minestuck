package com.mraof.minestuck.client.renderer;

import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

@SideOnly(Side.CLIENT)
public class RenderCard implements IItemRenderer {
	
	public static ResourceLocation cardResource = new ResourceLocation("minestuck", "textures/items/CardBig.png");
	public static ResourceLocation itemResource = new ResourceLocation("minestuck", "textures/items/AbstractItem.png");
	
	public static int experiment = 0;
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return Minestuck.specialCardRenderer && item.getItem().equals(Minestuck.captchaCard);
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return helper == ItemRendererHelper.ENTITY_BOBBING;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack card, Object... data) {
		ItemStack item = AlchemyRecipeHandler.getDecodedItem(card);
		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		RenderBlocks blockRenderer = (RenderBlocks) data[0];
		
		if(type != ItemRenderType.INVENTORY) {
			GL11.glScalef(0.3F, 0.3F, 0.3F);
		}
		
		Tessellator t = Tessellator.instance;
		textureManager.bindTexture(cardResource);
		t.startDrawingQuads();
		t.addVertexWithUV(0, 16, 0F, 0, 1);
		t.addVertexWithUV(16, 16, 0F, 1, 1);
		t.addVertexWithUV(16, 0, 0F, 1, 0);
		t.addVertexWithUV(0, 0, 0F, 0, 0);
		t.draw();
		
		if(item == null || !card.hasTagCompound())
			return;
		
		IItemRenderer renderer = MinecraftForgeClient.getItemRenderer(item, ItemRenderType.INVENTORY);
		if(renderer != null && !item.getItem().equals(Minestuck.captchaCard)) {
			//TODO Add support for custom renderer.
			textureManager.bindTexture(itemResource);
			t.startDrawingQuads();
			t.addVertexWithUV(0, 16, 0F, 0, 1);
			t.addVertexWithUV(16, 16, 0F, 1, 1);
			t.addVertexWithUV(16, 0, 0F, 1, 0);
			t.addVertexWithUV(0, 0, 0F, 0, 0);
			t.draw();
		} else {
			if((item.getItemSpriteNumber() == 0) && (RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item.getItem()).getRenderType()))) {
				textureManager.bindTexture(TextureMap.locationBlocksTexture);
				Block block = Block.getBlockFromItem(item.getItem());
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glPushMatrix();
				
				GL11.glScalef(10F, 10F, 10F);
				GL11.glTranslatef(1.0F, 0.5F, 1.0F);
				GL11.glScalef(1.0F, 1.0F, -1F);
				GL11.glRotatef(210F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
				
				GL11.glTranslatef(0.2F, -0.2F, -1F);
				GL11.glScalef(0.5F, 0.5F, 0.5F);
				
				blockRenderer.renderBlockAsItem(block, item.getItemDamage(), 1.0F);
				GL11.glPopMatrix();
				GL11.glDisable(GL11.GL_LIGHTING);
			} else {
				textureManager.bindTexture(item.getItemSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
				renderIcon(3, 5, item.getIconIndex(), 8, 8);
			}
		}
		
		if(card.getTagCompound().getBoolean("punched")) {
			GL11.glTranslatef(0F, 0F, 1F);
			renderPunchHoles(card);
		}
	}
	
	public static void renderIcon(int x, int y, IIcon icon, int width, int height) {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.addVertexWithUV(x, y + height, 0F, icon.getMinU(), icon.getMaxV());
		t.addVertexWithUV(x + width, y + height, 0F, icon.getMaxU(), icon.getMaxV());
		t.addVertexWithUV(x + width, y, 0F, icon.getMaxU(), icon.getMinV());
		t.addVertexWithUV(x, y, 0F, icon.getMinU(), icon.getMinV());
		t.draw();
	}
	
	public static void renderPunchHoles(ItemStack card) {
		
		Tessellator t = Tessellator.instance;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		t.startDrawingQuads();
		t.setColorOpaque_I(0);
		int hashCode = card.getTagCompound().getString("contentID").hashCode()+card.getTagCompound().getInteger("contentMeta");
		for(int i = 0; i < 12; i++) {
			if((hashCode & (1 << i)) != 0)
				continue;
			int x = 3+(i%3)*3;
			int y = 5+(i/3)*2;
			t.addVertex(x  , y+1, 0);
			t.addVertex(x+2, y+1, 0);
			t.addVertex(x+2, y  , 0);
			t.addVertex(x  , y  , 0);
		}
		t.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
	}
	
	public static ItemStack getDisplayItem(ItemStack card) {
		NBTTagCompound nbt = card.getTagCompound();
		if(nbt == null || !nbt.hasKey("displayID"))
			return null;
		else return new ItemStack((Item) Item.itemRegistry.getObject(nbt.getString("displayID")),nbt.getInteger("displayMeta"));
	}
	
}
