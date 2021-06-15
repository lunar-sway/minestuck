// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.entity.item.LotusFlowerEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LotusFlowerModel extends EntityModel<LotusFlowerEntity>
{
	private final ModelRenderer base;
	private final ModelRenderer root;
	private final ModelRenderer fourpetals;
	private final ModelRenderer petal1;
	private final ModelRenderer petaltip1;
	private final ModelRenderer petal2;
	private final ModelRenderer petaltip2;
	private final ModelRenderer petal3;
	private final ModelRenderer petaltip3;
	private final ModelRenderer petal4;
	private final ModelRenderer petaltip4;
	private final ModelRenderer fourpetals2;
	private final ModelRenderer petal5;
	private final ModelRenderer petaltip5;
	private final ModelRenderer petal6;
	private final ModelRenderer petaltip6;
	private final ModelRenderer petal7;
	private final ModelRenderer petaltip7;
	private final ModelRenderer petal8;
	private final ModelRenderer petaltip8;

	public LotusFlowerModel() {
		textureWidth = 128;
		textureHeight = 128;

		base = new ModelRenderer(this);
		base.setRotationPoint(0.0F, 24.0F, 0.0F);
		base.setTextureOffset(0, 0).addBox(-9.0F, -0.05F, -9.0F, 18.0F, 0.0F, 18.0F, 0.0F, false);

		root = new ModelRenderer(this);
		root.setRotationPoint(0.0F, 0.0F, 0.0F);
		base.addChild(root);
		root.setTextureOffset(1, 16).addBox(-3.0F, -0.5F, -3.0F, 6.0F, 0.0F, 6.0F, 0.0F, true);

		fourpetals = new ModelRenderer(this);
		fourpetals.setRotationPoint(0.0F, -0.5F, 0.0F);
		root.addChild(fourpetals);
		

		petal1 = new ModelRenderer(this);
		petal1.setRotationPoint(2.25F, 0.0F, 0.0F);
		fourpetals.addChild(petal1);
		petal1.setTextureOffset(4, 7).addBox(0.0F, -5.0F, -2.0F, 0.0F, 5.0F, 4.0F, 0.0F, false);

		petaltip1 = new ModelRenderer(this);
		petaltip1.setRotationPoint(0.0F, -5.0F, 0.0F);
		petal1.addChild(petaltip1);
		petaltip1.setTextureOffset(4, 0).addBox(0.0F, -7.0F, -2.0F, 0.0F, 7.0F, 4.0F, 0.0F, false);

		petal2 = new ModelRenderer(this);
		petal2.setRotationPoint(0.0F, 0.0F, 2.45F);
		fourpetals.addChild(petal2);
		petal2.setTextureOffset(4, 11).addBox(-2.0F, -5.0F, 0.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);

		petaltip2 = new ModelRenderer(this);
		petaltip2.setRotationPoint(0.0F, -5.0F, 0.0F);
		petal2.addChild(petaltip2);
		petaltip2.setTextureOffset(4, 4).addBox(-2.0F, -7.0F, 0.0F, 4.0F, 7.0F, 0.0F, 0.0F, false);

		petal3 = new ModelRenderer(this);
		petal3.setRotationPoint(-2.65F, 0.0F, 0.0F);
		fourpetals.addChild(petal3);
		petal3.setTextureOffset(8, 7).addBox(0.0F, -5.0F, -2.0F, 0.0F, 5.0F, 4.0F, 0.0F, false);

		petaltip3 = new ModelRenderer(this);
		petaltip3.setRotationPoint(0.0F, -5.0F, 0.0F);
		petal3.addChild(petaltip3);
		petaltip3.setTextureOffset(8, 0).addBox(0.0F, -7.0F, -2.0F, 0.0F, 7.0F, 4.0F, 0.0F, false);

		petal4 = new ModelRenderer(this);
		petal4.setRotationPoint(0.0F, 0.0F, -2.75F);
		fourpetals.addChild(petal4);
		petal4.setTextureOffset(8, 11).addBox(-2.0F, -5.0F, 0.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);

		petaltip4 = new ModelRenderer(this);
		petaltip4.setRotationPoint(0.0F, -5.0F, 0.0F);
		petal4.addChild(petaltip4);
		petaltip4.setTextureOffset(8, 4).addBox(-2.0F, -7.0F, 0.0F, 4.0F, 7.0F, 0.0F, 0.0F, false);
		
		fourpetals2 = new ModelRenderer(this);
		fourpetals2.setRotationPoint(0.0F, -0.5F, 0.0F);
		root.addChild(fourpetals2);
		setRotationAngle(fourpetals2, 0.0F, -0.7854F, 0.0F);
		

		petal5 = new ModelRenderer(this);
		petal5.setRotationPoint(2.0129F, 0.03F, 0.0F);
		fourpetals2.addChild(petal5);
		petal5.setTextureOffset(4, 7).addBox(0.1285F, -4.73F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, false);

		petaltip5 = new ModelRenderer(this);
		petaltip5.setRotationPoint(0.1499F, -4.7F, 0.0F);
		petal5.addChild(petaltip5);
		petaltip5.setTextureOffset(4, 0).addBox(-0.0214F, -6.61F, -2.0F, 0.0F, 6.0F, 4.0F, 0.0F, false);

		petal6 = new ModelRenderer(this);
		petal6.setRotationPoint(0.0F, 0.03F, 2.2788F);
		fourpetals2.addChild(petal6);
		petal6.setTextureOffset(4, 11).addBox(-2.0F, -4.73F, 0.1455F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		petaltip6 = new ModelRenderer(this);
		petaltip6.setRotationPoint(-0.0043F, -4.725F, 0.1371F);
		petal6.addChild(petaltip6);
		petaltip6.setTextureOffset(4, 4).addBox(-1.9957F, -6.585F, 0.0083F, 4.0F, 6.0F, 0.0F, 0.0F, false);

		petal7 = new ModelRenderer(this);
		petal7.setRotationPoint(-2.5447F, 0.03F, 0.0F);
		fourpetals2.addChild(petal7);
		petal7.setTextureOffset(8, 7).addBox(-0.1624F, -4.73F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, false);

		petaltip7 = new ModelRenderer(this);
		petaltip7.setRotationPoint(-0.1591F, -4.725F, -0.0177F);
		petal7.addChild(petaltip7);
		petaltip7.setTextureOffset(8, 0).addBox(-0.0023F, -6.585F, -1.9823F, 0.0F, 6.0F, 4.0F, 0.0F, false);

		petal8 = new ModelRenderer(this);
		petal8.setRotationPoint(-0.1329F, 0.03F, -2.3948F);
		fourpetals2.addChild(petal8);
		petal8.setTextureOffset(8, 11).addBox(-2.0085F, -4.73F, -0.1709F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		petaltip8 = new ModelRenderer(this);
		petaltip8.setRotationPoint(0.0F, -4.725F, -0.1664F);
		petal8.addChild(petaltip8);
		petaltip8.setTextureOffset(8, 4).addBox(-2.0085F, -6.585F, -0.0045F, 4.0F, 6.0F, 0.0F, 0.0F, false);
	}
	
	@Override
	public void setRotationAngles(LotusFlowerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
	
	}

	/*@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}*/

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		base.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}