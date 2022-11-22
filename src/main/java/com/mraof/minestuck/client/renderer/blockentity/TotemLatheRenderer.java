package com.mraof.minestuck.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.block.CruxiteDowelBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.machine.TotemLatheBlockEntity;
import com.mraof.minestuck.blockentity.machine.TotemLatheDowelBlockEntity;
import com.mraof.minestuck.client.model.TotemLatheModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import javax.annotation.Nullable;

public class TotemLatheRenderer extends GeoBlockRenderer<TotemLatheDowelBlockEntity>
{
	private TotemLatheDowelBlockEntity lathe;
	private MultiBufferSource renderTypeBuffer;
	
	public TotemLatheRenderer(BlockEntityRendererProvider.Context context)
	{
		super(context, new TotemLatheModel());
	}
	
	@Override
	public RenderType getRenderType(TotemLatheDowelBlockEntity animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation)
	{
		return RenderType.entityCutoutNoCull(textureLocation);
	}
	
	@Override
	public void renderEarly(TotemLatheDowelBlockEntity animatable, PoseStack stackIn, float partialTicks, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		super.renderEarly(animatable, stackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.lathe = animatable;
		this.renderTypeBuffer = renderTypeBuffer;
	}
	
	@Override
	public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		if(bone.getName().equals("totem"))
		{
			ItemStack dowel = this.lathe.getStack();
			if(dowel.isEmpty())
			{
				return; // render nothing
			}
			
			BlockState cruxiteDowel = MSBlocks.CRUXITE_DOWEL.get().defaultBlockState();
			if(AlchemyHelper.hasDecodedItem(dowel))
			{
				cruxiteDowel = cruxiteDowel.setValue(MSProperties.DOWEL_BLOCK, CruxiteDowelBlock.Type.TOTEM);
			}
			
			// position adjustments
			stack.pushPose();
			RenderUtils.translate(bone, stack);
			RenderUtils.moveToPivot(bone, stack);
			stack.translate(0.25, -0.375, 0.03125);
			stack.mulPose(Vector3f.ZP.rotationDegrees(90));
			
			// render the dowel with the blockRenderer
			ClientLevel level = Minecraft.getInstance().level;
			BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
			BlockPos pos = lathe.getBlockPos();
			IModelData modelData = EmptyModelData.INSTANCE;
			//appears darker than intended, may be lighting issues
			blockRenderer.renderBatched(cruxiteDowel, pos, level, stack, renderTypeBuffer.getBuffer(ItemBlockRenderTypes.getRenderType(cruxiteDowel, false)), false, level.random, modelData);
			
			stack.popPose();
			
			// reset the buffer to render the rest of the totemlathe
			renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(lathe)));
			return;
		}
		super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
}