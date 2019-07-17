package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.block.BlockMinestuckLog;
import com.mraof.minestuck.block.BlockMinestuckLog1;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.decorator.BasicTreeDecorator;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.SpruceTreeDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.TallGrassDecorator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.WorldGenDecorator;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;

import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenBigTree;

public class LandAspectForest extends TerrainLandAspect
{
	private final Variant type;
	private final List<TerrainLandAspect> variations;
	
	public LandAspectForest()
	{
		this(Variant.FOREST);
	}
	
	public LandAspectForest(Variant variation)
	{
		variations = new ArrayList<>();
		type = variation;
		
		if(type == Variant.FOREST)
		{
			variations.add(this);
			variations.add(new LandAspectForest(Variant.TAIGA));
		}
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", Blocks.GRASS.getDefaultState());
		registry.setBlockState("upper", Blocks.DIRT.getDefaultState());
		if(type == Variant.TAIGA) {
			registry.setBlockState("structure_primary", Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockMinestuckLog.LOG_AXIS, EnumAxis.NONE));
			registry.setBlockState("structure_primary_decorative", MinestuckBlocks.log.getDefaultState().withProperty(BlockMinestuckLog1.VARIANT, BlockMinestuckLog1.BlockType.FROST).withProperty(BlockMinestuckLog.LOG_AXIS, EnumAxis.NONE));
		} else {
			registry.setBlockState("structure_primary", MinestuckBlocks.log.getDefaultState().withProperty(BlockMinestuckLog1.VARIANT, BlockMinestuckLog1.BlockType.VINE_OAK).withProperty(BlockMinestuckLog.LOG_AXIS, EnumAxis.NONE));
			registry.setBlockState("structure_primary_decorative", MinestuckBlocks.log.getDefaultState().withProperty(BlockMinestuckLog1.VARIANT, BlockMinestuckLog1.BlockType.FLOWERY_VINE_OAK).withProperty(BlockMinestuckLog.LOG_AXIS, EnumAxis.NONE));
		}
		registry.setBlockState("structure_secondary", Blocks.STONEBRICK.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));
		registry.setBlockState("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("village_path", Blocks.GRASS_PATH.getDefaultState());
		registry.setBlockState("bush", Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN));
		registry.setBlockState("structure_wool_1", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.GREEN));
		if(type == Variant.TAIGA) {
			registry.setBlockState("structure_wool_3", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.LIGHT_BLUE));
		} else {
			registry.setBlockState("structure_wool_3", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN));
		}
	}
	
	@Override
	public String getPrimaryName()
	{
		return type.getName();
	}
	
	@Override
	public String[] getNames()
	{
		if(type == Variant.FOREST) {
			return new String[] {"forest", "tree"};
		} else {
			return new String[] {"taiga", "boreal_forest", "cold_forest"};
		}
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<>();
		if(type == Variant.FOREST) {
			list.add(new BasicTreeDecorator(5, BiomeMinestuck.mediumNormal));
			list.add(new BasicTreeDecorator(8, BiomeMinestuck.mediumRough));
			list.add(new WorldGenDecorator(new WorldGenBigTree(false), 15, 0.6F, BiomeMinestuck.mediumNormal));
			list.add(new WorldGenDecorator(new WorldGenBigTree(false), 25, 0.6F, BiomeMinestuck.mediumRough));
		} else {
			list.add(new SpruceTreeDecorator(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE), Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, false), BiomeMinestuck.mediumNormal));
			list.add(new SpruceTreeDecorator(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE), Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, false), BiomeMinestuck.mediumRough));
			list.add(new SurfaceDecoratorVein(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL), 25, 40, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
		}
		list.add(new TallGrassDecorator(0.3F, BiomeMinestuck.mediumNormal));
		list.add(new TallGrassDecorator(0.5F, 0.2F, BiomeMinestuck.mediumRough));
		
		list.add(new UndergroundDecoratorVein(Blocks.DIRT.getDefaultState(), 3, 33, 64));	//Have 64 be the highest value because stone is used as a building material for structures right now
		list.add(new UndergroundDecoratorVein(Blocks.GRAVEL.getDefaultState(), 2, 28, 64));
		list.add(new UndergroundDecoratorVein(Blocks.COAL_ORE.getDefaultState(), 13, 17, 64));
		list.add(new UndergroundDecoratorVein(Blocks.EMERALD_ORE.getDefaultState(), 8, 3, 32));
		list.add(new SurfaceDecoratorVein(Blocks.CLAY.getDefaultState(), 15, 10, BiomeMinestuck.mediumOcean));
		return list;
	}
	
	@Override
	public Vec3d getFogColor()
	{
		return new Vec3d(0.0D, 1.0D, 0.6D);
	}
	
	@Override
	public Vec3d getSkyColor()
	{
		return new Vec3d(0.4D, 0.7D, 1.0D);
	}
	
	@Override
	public float getRainfall()	//Same as vanilla forest
	{
		return 0.8F;
	}
	
	@Override
	public List<TerrainLandAspect> getVariations()
	{
		return variations;
	}
	
	@Override
	public TerrainLandAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromNameTerrain("forest");
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.IGUANA;
	}
	
	public enum Variant
	{
		FOREST,
		TAIGA;
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}