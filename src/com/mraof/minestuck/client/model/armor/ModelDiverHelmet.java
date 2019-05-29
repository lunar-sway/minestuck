package com.mraof.minestuck.client.model.armor;

//Made with Blockbench
//Paste this code into your mod.

import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelDiverHelmet extends ModelBiped {
	private final ModelRenderer Helmet;
	private final ModelRenderer Main;
	private final ModelRenderer Bottom;
	private final ModelRenderer Top;
	private final ModelRenderer Windows;

	public ModelDiverHelmet() {
		bipedHead.cubeList.remove(0);
		textureWidth = 128;
		textureHeight = 128;

		Helmet = new ModelRenderer(this);
		Helmet.setRotationPoint(0.0F, 0.0F, 0.0F);

		Main = new ModelRenderer(this);
		Main.setRotationPoint(0.0F, 24.0F, 0.0F);
		Helmet.addChild(Main);
		Main.cubeList.add(new ModelBox(Main, 0, 43, -4.0F, -25.0F, -5.0F, 8, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 36, 45, -4.0F, -25.0F, 4.0F, 8, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 36, 25, -5.0F, -25.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 54, 25, 4.0F, -25.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 54, 34, 5.0F, -26.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 4, 57, 4.0F, -26.0F, 4.0F, 1, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 8, 57, -5.0F, -26.0F, 4.0F, 1, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 28, 57, -5.0F, -26.0F, -5.0F, 1, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 0, 57, 4.0F, -26.0F, -5.0F, 1, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 72, 43, -4.0F, -26.0F, -6.0F, 8, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 0, 45, -4.0F, -26.0F, 5.0F, 8, 1, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 62, 16, -6.0F, -26.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 32, 0, -6.0F, -32.0F, -5.0F, 1, 6, 10, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 54, 0, 5.0F, -32.0F, -5.0F, 1, 6, 10, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 0, 16, -5.0F, -32.0F, -6.0F, 10, 6, 1, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 22, 16, -5.0F, -32.0F, 5.0F, 10, 6, 1, 0.0F, false));

		Bottom = new ModelRenderer(this);
		Bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
		Helmet.addChild(Bottom);
		Bottom.cubeList.add(new ModelBox(Bottom, 0, 25, 3.0F, -24.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Bottom.cubeList.add(new ModelBox(Bottom, 18, 25, -4.0F, -24.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Bottom.cubeList.add(new ModelBox(Bottom, 0, 49, -3.0F, -24.0F, 3.0F, 6, 1, 1, 0.0F, false));
		Bottom.cubeList.add(new ModelBox(Bottom, 54, 45, -3.0F, -24.0F, -4.0F, 6, 1, 1, 0.0F, false));

		Top = new ModelRenderer(this);
		Top.setRotationPoint(0.0F, 24.0F, 0.0F);
		Helmet.addChild(Top);
		Top.cubeList.add(new ModelBox(Top, 12, 57, 4.0F, -33.0F, -5.0F, 1, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 16, 57, -5.0F, -33.0F, -5.0F, 1, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 20, 57, -5.0F, -33.0F, 4.0F, 1, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 24, 57, 4.0F, -33.0F, 4.0F, 1, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 44, 16, 5.0F, -33.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 0, 34, -6.0F, -33.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 54, 43, -4.0F, -33.0F, -6.0F, 8, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 36, 43, -4.0F, -33.0F, 5.0F, 8, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 18, 43, -4.0F, -34.0F, 4.0F, 8, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 18, 45, -4.0F, -34.0F, -5.0F, 8, 1, 1, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 18, 34, 4.0F, -34.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 36, 34, -5.0F, -34.0F, -4.0F, 1, 1, 8, 0.0F, false));
		Top.cubeList.add(new ModelBox(Top, 0, 0, -4.0F, -35.0F, -4.0F, 8, 1, 8, 0.0F, false));

		Windows = new ModelRenderer(this);
		Windows.setRotationPoint(0.0F, 24.0F, 0.0F);
		Helmet.addChild(Windows);
		Windows.cubeList.add(new ModelBox(Windows, 68, 45, -3.0F, -32.0F, -7.0F, 6, 1, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 0, 47, -3.0F, -27.0F, -7.0F, 6, 1, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 20, 52, 2.0F, -31.0F, -7.0F, 1, 4, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 10, 52, -3.0F, -31.0F, -7.0F, 1, 4, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 34, 47, -7.0F, -31.0F, -2.0F, 1, 1, 4, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 14, 47, -7.0F, -28.0F, -2.0F, 1, 1, 4, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 30, 52, -7.0F, -30.0F, -2.0F, 1, 2, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 38, 52, -7.0F, -30.0F, 1.0F, 1, 2, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 30, 52, 6.0F, -30.0F, 1.0F, 1, 2, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 42, 52, 6.0F, -30.0F, -2.0F, 1, 2, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 44, 47, 6.0F, -31.0F, -2.0F, 1, 1, 4, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 24, 47, 6.0F, -28.0F, -2.0F, 1, 1, 4, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 0, 52, -2.0F, -36.0F, 1.0F, 4, 1, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 0, 54, -2.0F, -36.0F, -2.0F, 4, 1, 1, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 24, 52, -2.0F, -36.0F, -1.0F, 1, 1, 2, 0.0F, false));
		Windows.cubeList.add(new ModelBox(Windows, 14, 52, 1.0F, -36.0F, -1.0F, 1, 1, 2, 0.0F, false));
		this.bipedHead.addChild(Helmet);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
	}
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}