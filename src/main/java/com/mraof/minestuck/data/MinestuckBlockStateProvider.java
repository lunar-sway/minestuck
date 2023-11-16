package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Function;

public class MinestuckBlockStateProvider extends BlockStateProvider
{
	public MinestuckBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper)
	{
		super(output, Minestuck.MOD_ID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels()
	{
		//Skaia
		simpleBlockWithItem(MSBlocks.BLACK_CHESS_DIRT);
		simpleBlockWithItem(MSBlocks.WHITE_CHESS_DIRT);
		simpleBlockWithItem(MSBlocks.DARK_GRAY_CHESS_DIRT);
		simpleBlockWithItem(MSBlocks.LIGHT_GRAY_CHESS_DIRT);
		simpleBlock(MSBlocks.SKAIA_PORTAL,
				id -> models().getBuilder(id.getPath()).texture("particle", id.withPrefix("item/")));
		
		simpleBlockWithItem(MSBlocks.BLACK_CHESS_BRICKS);
		simpleBlockWithItem(MSBlocks.DARK_GRAY_CHESS_BRICKS);
		simpleBlockWithItem(MSBlocks.LIGHT_GRAY_CHESS_BRICKS);
		simpleBlockWithItem(MSBlocks.WHITE_CHESS_BRICKS);
		simpleBlockWithItem(MSBlocks.BLACK_CHESS_BRICK_SMOOTH);
		simpleBlockWithItem(MSBlocks.DARK_GRAY_CHESS_BRICK_SMOOTH);
		simpleBlockWithItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_SMOOTH);
		simpleBlockWithItem(MSBlocks.WHITE_CHESS_BRICK_SMOOTH);
		trimWithItem(MSBlocks.BLACK_CHESS_BRICK_TRIM,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("black_chess_bricks")),
				id -> models().cubeColumn(
						id.withSuffix("_flipped").getPath(),
						texture(id.withSuffix("_flipped")),
						texture("black_chess_bricks")));
		trimWithItem(MSBlocks.DARK_GRAY_CHESS_BRICK_TRIM,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("dark_gray_chess_bricks")),
				id -> models().cubeColumn(
						id.withSuffix("_flipped").getPath(),
						texture(id.withSuffix("_flipped")),
						texture("dark_gray_chess_bricks")));
		trimWithItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_TRIM,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("light_gray_chess_bricks")),
				id -> models().cubeColumn(
						id.withSuffix("_flipped").getPath(),
						texture(id.withSuffix("_flipped")),
						texture("light_gray_chess_bricks")));
		trimWithItem(MSBlocks.WHITE_CHESS_BRICK_TRIM,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("white_chess_bricks")),
				id -> models().cubeColumn(
						id.withSuffix("_flipped").getPath(),
						texture(id.withSuffix("_flipped")),
						texture("white_chess_bricks")));
		
		simpleBlockWithItem(MSBlocks.CHECKERED_STAINED_GLASS,
				id -> models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		simpleBlockWithItem(MSBlocks.BLACK_CROWN_STAINED_GLASS,
				id -> models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		simpleBlockWithItem(MSBlocks.BLACK_PAWN_STAINED_GLASS,
				id -> models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		simpleBlockWithItem(MSBlocks.WHITE_CROWN_STAINED_GLASS,
				id -> models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		simpleBlockWithItem(MSBlocks.WHITE_PAWN_STAINED_GLASS,
				id -> models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		
		simpleBlockWithItem(MSBlocks.STONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.NETHERRACK_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.COBBLESTONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.SANDSTONE_CRUXITE_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.RED_SANDSTONE_CRUXITE_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.END_STONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.SHADE_STONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.MYCELIUM_STONE_CRUXITE_ORE);
		
		simpleBlockWithItem(MSBlocks.STONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.DEEPSLATE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.NETHERRACK_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.COBBLESTONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.SANDSTONE_URANIUM_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.RED_SANDSTONE_URANIUM_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.END_STONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.SHADE_STONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.MYCELIUM_STONE_URANIUM_ORE);
		
		simpleBlockWithItem(MSBlocks.NETHERRACK_COAL_ORE);
		simpleBlockWithItem(MSBlocks.SHADE_STONE_COAL_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_COAL_ORE);
		
		simpleBlockWithItem(MSBlocks.END_STONE_IRON_ORE);
		simpleBlockWithItem(MSBlocks.SANDSTONE_IRON_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.RED_SANDSTONE_IRON_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		
		simpleBlockWithItem(MSBlocks.SANDSTONE_GOLD_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.RED_SANDSTONE_GOLD_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.SHADE_STONE_GOLD_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_GOLD_ORE);
		
		simpleBlockWithItem(MSBlocks.END_STONE_REDSTONE_ORE);
		simpleBlockWithItem(MSBlocks.STONE_QUARTZ_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_LAPIS_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_DIAMOND_ORE);
		
		//Resource Blocks
		simpleBlockWithItem(MSBlocks.CRUXITE_BLOCK);
		simpleBlockWithItem(MSBlocks.URANIUM_BLOCK);
		simpleBlockWithItem(MSBlocks.GENERIC_OBJECT);
		
		//Land Environment
		simpleBlockWithItem(MSBlocks.THOUGHT_DIRT);
		
		simpleBlockWithItem(MSBlocks.COARSE_STONE);
		simpleBlockWithItem(MSBlocks.CHISELED_COARSE_STONE);
		simpleBlockWithItem(MSBlocks.COARSE_STONE_BRICKS);
		unflippedColumnWithItem(MSBlocks.COARSE_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_COARSE_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_COARSE_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_COARSE_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.SHADE_STONE);
		simpleBlockWithItem(MSBlocks.SMOOTH_SHADE_STONE);
		simpleBlockWithItem(MSBlocks.SHADE_BRICKS);
		unflippedColumnWithItem(MSBlocks.SHADE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture("shade_stone_column"),
						texture("shade_stone_column_top")));
		simpleBlockWithItem(MSBlocks.CHISELED_SHADE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_SHADE_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_SHADE_BRICKS);
		simpleBlockWithItem(MSBlocks.BLOOD_SHADE_BRICKS);
		simpleBlockWithItem(MSBlocks.TAR_SHADE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.FROST_TILE);
		simpleBlockWithItem(MSBlocks.CHISELED_FROST_TILE);
		simpleBlockWithItem(MSBlocks.FROST_BRICKS);
		unflippedColumnWithItem(MSBlocks.FROST_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_FROST_BRICKS,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id).withSuffix("_side"),
						texture(id)));
		simpleBlockWithItem(MSBlocks.CRACKED_FROST_BRICKS);
		simpleBlockWithItem(MSBlocks.FLOWERY_FROST_BRICKS);
		
		simpleBlockWithItem(MSBlocks.CAST_IRON);
		simpleBlockWithItem(MSBlocks.CHISELED_CAST_IRON);
		unflippedColumnWithItem(MSBlocks.STEEL_BEAM,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_top"))));
		
		simpleBlockWithItem(MSBlocks.NATIVE_SULFUR);
		
		simpleBlockWithItem(MSBlocks.MYCELIUM_STONE);
		simpleBlockWithItem(MSBlocks.MYCELIUM_COBBLESTONE);
		simpleBlockWithItem(MSBlocks.POLISHED_MYCELIUM_STONE);
		simpleBlockWithItem(MSBlocks.MYCELIUM_BRICKS);
		unflippedColumnWithItem(MSBlocks.MYCELIUM_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.SUSPICIOUS_CHISELED_MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.FLOWERY_MYCELIUM_BRICKS);
		
		simpleBlockWithItem(MSBlocks.BLACK_SAND);
		simpleBlockWithItem(MSBlocks.BLACK_STONE);
		simpleBlockWithItem(MSBlocks.BLACK_COBBLESTONE);
		simpleBlockWithItem(MSBlocks.POLISHED_BLACK_STONE);
		simpleBlockWithItem(MSBlocks.BLACK_STONE_BRICKS);
		unflippedColumnWithItem(MSBlocks.BLACK_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_BLACK_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_BLACK_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.CHALK);
		simpleBlockWithItem(MSBlocks.POLISHED_CHALK);
		simpleBlockWithItem(MSBlocks.CHALK_BRICKS);
		unflippedColumnWithItem(MSBlocks.CHALK_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_CHALK_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_CHALK_BRICKS);
		simpleBlockWithItem(MSBlocks.FLOWERY_CHALK_BRICKS);
		
		simpleBlockWithItem(MSBlocks.PINK_STONE);
		simpleBlockWithItem(MSBlocks.POLISHED_PINK_STONE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_BRICKS);
		unflippedColumnWithItem(MSBlocks.PINK_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_end"))));
		simpleBlockWithItem(MSBlocks.CHISELED_PINK_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_PINK_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_PINK_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.BROWN_STONE);
		simpleBlockWithItem(MSBlocks.POLISHED_BROWN_STONE);
		simpleBlockWithItem(MSBlocks.BROWN_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_BROWN_STONE_BRICKS);
		unflippedColumnWithItem(MSBlocks.BROWN_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("polished_brown_stone")));
		
		simpleBlockWithItem(MSBlocks.GREEN_STONE);
		simpleBlockWithItem(MSBlocks.POLISHED_GREEN_STONE);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICKS);
		unflippedColumnWithItem(MSBlocks.GREEN_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("polished_green_stone")));
		simpleBlockWithItem(MSBlocks.CHISELED_GREEN_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.VERTICAL_GREEN_STONE_BRICKS);
		horizontalWithItem(MSBlocks.GREEN_STONE_BRICK_EMBEDDED_LADDER,
				id -> models().getExistingFile(id));
		trimWithItem(MSBlocks.GREEN_STONE_BRICK_TRIM,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("green_stone_bricks")),
				id -> models().cubeColumn(
						id.withSuffix("_flipped").getPath(),
						texture(id.withSuffix("_flipped")),
						texture("green_stone_bricks")));
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_FROG);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_IGUANA_LEFT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_IGUANA_RIGHT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_LOTUS);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_NAK_LEFT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_NAK_RIGHT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_SALAMANDER_LEFT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_SALAMANDER_RIGHT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_SKAIA);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_TURTLE);
		
		unflippedColumnWithItem(MSBlocks.SANDSTONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_end"))));
		directionalWithItem(MSBlocks.CHISELED_SANDSTONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("sandstone_column_end")));
		unflippedColumnWithItem(MSBlocks.RED_SANDSTONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_end"))));
		directionalWithItem(MSBlocks.CHISELED_RED_SANDSTONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("red_sandstone_column_end")));
		
		simpleBlockWithItem(MSBlocks.UNCARVED_WOOD);
		simpleBlockWithItem(MSBlocks.CHIPBOARD);
		simpleBlockWithItem(MSBlocks.WOOD_SHAVINGS);
		simpleBlockWithItem(MSBlocks.CARVED_HEAVY_PLANKS);
		simpleBlockWithItem(MSBlocks.CARVED_PLANKS);
		simpleBlockWithItem(MSBlocks.POLISHED_UNCARVED_WOOD);
		simpleBlockWithItem(MSBlocks.CARVED_KNOTTED_WOOD);
		
		simpleBlockWithItem(MSBlocks.DENSE_CLOUD);
		simpleBlockWithItem(MSBlocks.BRIGHT_DENSE_CLOUD);
		simpleBlockWithItem(MSBlocks.SUGAR_CUBE);
	}
	
	private void simpleBlockWithItem(RegistryObject<Block> block)
	{
		simpleBlockWithItem(block.get(), cubeAll(block.get()));
	}
	
	private void simpleBlock(RegistryObject<Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		simpleBlock(block.get(), modelProvider.apply(block.getId()));
	}
	
	private void simpleBlockWithItem(RegistryObject<Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		simpleBlockWithItem(block.get(), modelProvider.apply(block.getId()));
	}
	
	private void horizontalWithItem(RegistryObject<Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		getVariantBuilder(block.get())
				.forAllStatesExcept(state -> ConfiguredModel.builder()
								.modelFile(model)
								.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
								.build(),
						BlockStateProperties.WATERLOGGED
				);
		simpleBlockItem(block.get(), model);
	}
	
	private void directionalWithItem(RegistryObject<Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		directionalBlock(block.get(), model);
		simpleBlockItem(block.get(), model);
	}
	
	/**
	 * While the standard directional block has the down-facing state rotated 180 degrees along the y-axis,
	 * blocks with this function are instead not rotated in both the up-facing state and the down-facing state.
	 */
	private void unflippedColumnWithItem(RegistryObject<Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		getVariantBuilder(block.get())
				.forAllStates(state -> {
					Direction dir = state.getValue(BlockStateProperties.FACING);
					return ConfiguredModel.builder()
							.modelFile(model)
							.rotationX(dir.getAxis().isHorizontal() ? 90 : 0)
							.rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
							.build();
				});
		simpleBlockItem(block.get(), model);
	}
	
	private void trimWithItem(RegistryObject<Block> block, Function<ResourceLocation, ModelFile> modelProvider, Function<ResourceLocation, ModelFile> flippedModelProvider)
	{
		var model = modelProvider.apply(block.getId());
		var flippedModel = flippedModelProvider.apply(block.getId());
		getVariantBuilder(block.get())
				.forAllStates(state -> {
					Direction dir = state.getValue(BlockStateProperties.FACING);
					return ConfiguredModel.builder()
							.modelFile(dir == Direction.DOWN || dir == Direction.SOUTH || dir == Direction.WEST
									? flippedModel : model)
							.rotationX(dir.getAxis().isHorizontal() ? 90 : 0)
							.rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
							.build();
				});
		simpleBlockItem(block.get(), model);
	}
	
	private void simpleStairsWithItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		stairsBlock((StairBlock) block.get(), blockTexture(baseBlock.get()));
		blockItem(block);
	}
	
	private void simpleSlabWithItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		slabBlock((SlabBlock) block.get(), blockTexture(baseBlock.get()), blockTexture(baseBlock.get()));
		blockItem(block);
	}
	
	private void simpleCrossBlock(RegistryObject<Block> block)
	{
		simpleBlock(block.get(), models().cross(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath(), blockTexture(block.get())).renderType("cutout"));
	}
	
	private void trapdoorBlockItem(RegistryObject<Block> block)
	{
		simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile("minestuck:block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath() + "_bottom"));
	}
	
	private void cutoutBlockWithItem(RegistryObject<Block> block)
	{
		simpleBlockWithItem(block.get(), models().cubeAll(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath(), blockTexture(block.get())).renderType("cutout"));
	}
	
	public void simpleCutoutDoorBlock(RegistryObject<Block> block)
	{
		String id = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath();
		doorBlockWithRenderType((DoorBlock) block.get(), modLoc("block/" + id + "_bottom"), modLoc("block/" + id + "_top"), "cutout");
	}
	
	public void simpleButtonBlockWithItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		buttonBlock((ButtonBlock) block.get(), blockTexture(baseBlock.get()));
		blockItem(block);
	}
	
	public void simplePressurePlateBlockWithItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		pressurePlateBlock((PressurePlateBlock) block.get(), blockTexture(baseBlock.get()));
		blockItem(block);
	}
	
	public void simpleFenceBlock(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		fenceBlock((FenceBlock) block.get(), blockTexture(baseBlock.get()));
	}
	
	public void simpleWallBlock(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		wallBlock((WallBlock) block.get(), blockTexture(baseBlock.get()));
	}
	
	public void simpleFenceGateBlockWithItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		fenceGateBlock((FenceGateBlock) block.get(), blockTexture(baseBlock.get()));
		blockItem(block);
	}
	
	public void simpleLogBlockWithItem(RegistryObject<Block> block) {
		logBlock((RotatedPillarBlock) block.get());
		blockItem(block);
	}
	
	public void simpleAxisBlockWithItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock1, RegistryObject<Block> baseBlock2)
	{
		axisBlock(((RotatedPillarBlock) block.get()), blockTexture(baseBlock1.get()), blockTexture(baseBlock2.get()));
		blockItem(block);
	}
	
	public void blockItem(RegistryObject<Block> block)
	{
		simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile("minestuck:block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath()));
	}
	
	public static ResourceLocation texture(RegistryObject<? extends Block> block)
	{
		return texture(block.getId());
	}
	
	public static ResourceLocation texture(ResourceLocation id)
	{
		return id.withPrefix(ModelProvider.BLOCK_FOLDER + "/");
	}
	
	public static ResourceLocation texture(String path)
	{
		return new ResourceLocation(Minestuck.MOD_ID, ModelProvider.BLOCK_FOLDER + "/" + path);
	}
}
