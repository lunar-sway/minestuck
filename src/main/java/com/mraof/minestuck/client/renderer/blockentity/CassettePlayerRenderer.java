package com.mraof.minestuck.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mraof.minestuck.blockentity.CassettePlayerBlockEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

@MethodsReturnNonnullByDefault
public class CassettePlayerRenderer implements BlockEntityRenderer<CassettePlayerBlockEntity>
{
	private final ItemRenderer itemRenderer;
	
	public CassettePlayerRenderer(BlockEntityRendererProvider.Context context)
	{
		super();
		
		this.itemRenderer = context.getItemRenderer();
	}
	
	@Override
	public void render(CassettePlayerBlockEntity cassettePlayer, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
	{
		ItemStack cassette = cassettePlayer.getCassette();
		Direction direction = cassettePlayer.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
		
		if(!cassette.isEmpty())
		{
			poseStack.pushPose();
			poseStack.translate(.5, 1.75f / 16f, .5);
			Direction d = Direction.from2DDataValue(direction.get2DDataValue());
			poseStack.mulPose(Axis.YP.rotationDegrees(-d.toYRot()));
			poseStack.mulPose(Axis.XP.rotationDegrees(90));
			poseStack.scale(.4F, .4F, .4F);
			itemRenderer.renderStatic(cassette, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, cassettePlayer.getLevel(), (int) cassettePlayer.getBlockPos().asLong());
			poseStack.popPose();
		}
	}
}
