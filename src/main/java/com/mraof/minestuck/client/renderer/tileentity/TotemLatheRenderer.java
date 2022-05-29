package com.mraof.minestuck.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.block.CruxiteDowelBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.client.model.TotemLatheModel;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.tileentity.machine.TotemLatheDowelTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import javax.annotation.Nullable;

public class TotemLatheRenderer extends GeoBlockRenderer<TotemLatheDowelTileEntity>
{
	private TotemLatheDowelTileEntity lathe;
	private IRenderTypeBuffer renderTypeBuffer;
	
	public TotemLatheRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn, new TotemLatheModel());
	}
	
	@Override
	public RenderType getRenderType(TotemLatheDowelTileEntity animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation)
	{
		return RenderType.entityCutoutNoCull(textureLocation);
	}
	
	@Override
	public void renderEarly(TotemLatheDowelTileEntity animatable, MatrixStack stackIn, float ticks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		this.lathe = animatable;
		this.renderTypeBuffer = renderTypeBuffer;
	}
	
	@Override
	public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		if(bone.getName().equals("totem"))
		{
			ItemStack dowel = this.lathe.getStack();
			if(dowel.isEmpty())
			{
				return; // render nothing
			}
			
			BlockState cruxiteDowel = MSBlocks.CRUXITE_DOWEL.defaultBlockState();
			if(AlchemyHelper.hasDecodedItem(dowel))
			{
				cruxiteDowel = cruxiteDowel.setValue(MSProperties.DOWEL_BLOCK, CruxiteDowelBlock.Type.TOTEM).getBlockState();
			}
			
			// position adjustments
			stack.pushPose();
			RenderUtils.translate(bone, stack);
			RenderUtils.moveToPivot(bone, stack);
			stack.translate(0.25, -0.375, 0.03125);
			stack.mulPose(Vector3f.ZP.rotationDegrees(90));
			
			// render the dowel with the blockRenderer
			ClientWorld level = Minecraft.getInstance().level;
			BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
			BlockPos pos = lathe.getBlockPos();
			IModelData modelData = new ModelDataMap.Builder().build();
			blockRenderer.renderModel(cruxiteDowel, pos, level, stack, renderTypeBuffer.getBuffer(RenderTypeLookup.getRenderType(cruxiteDowel, false)), false, level.random, modelData);
			
			stack.popPose();
			renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(lathe)));
			return;
		}
		super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
}