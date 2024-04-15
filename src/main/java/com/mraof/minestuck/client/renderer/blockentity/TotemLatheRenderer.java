package com.mraof.minestuck.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.block.CruxiteDowelBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.machine.TotemLatheDowelBlockEntity;
import com.mraof.minestuck.client.model.blockentity.TotemLatheModel;
import net.minecraft.MethodsReturnNonnullByDefault;
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
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.model.data.ModelData;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.RenderUtils;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TotemLatheRenderer extends GeoBlockRenderer<TotemLatheDowelBlockEntity>
{
	public TotemLatheRenderer(BlockEntityRendererProvider.Context ignored)
	{
		super(new TotemLatheModel());
	}
	
	@Override
	public RenderType getRenderType(TotemLatheDowelBlockEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick)
	{
		return RenderType.entityCutoutNoCull(texture);
	}
	
	@Override
	public void renderRecursively(PoseStack poseStack, TotemLatheDowelBlockEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		if(bone.getName().equals("totem"))
		{
			ItemStack dowel = this.animatable.getStack();
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
			poseStack.pushPose();
			RenderUtils.translateMatrixToBone(poseStack, bone);
			RenderUtils.translateToPivotPoint(poseStack, bone);
			poseStack.translate(0.25, -0.375, 0.03125);
			poseStack.mulPose(Axis.ZP.rotationDegrees(90));
			
			// render the dowel with the blockRenderer
			ClientLevel level = Minecraft.getInstance().level;
			BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
			BlockPos pos = animatable.getBlockPos();
			ModelData modelData = ModelData.EMPTY;
			//appears darker than intended, may be lighting issues
			blockRenderer.renderBatched(cruxiteDowel, pos, level, poseStack, bufferSource.getBuffer(ItemBlockRenderTypes.getRenderType(cruxiteDowel, false)), false, level.random, modelData, null);
			
			poseStack.popPose();
			
			// reset the buffer to render the rest of the totemlathe
			bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(animatable)));
			return;
		}
		super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	@Override
	public AABB getRenderBoundingBox(TotemLatheDowelBlockEntity blockEntity)
	{
		return INFINITE_EXTENT_AABB;
	}
}