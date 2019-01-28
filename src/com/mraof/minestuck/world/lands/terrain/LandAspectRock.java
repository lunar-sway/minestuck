package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.BlockMinestuckStone;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.decorator.BlockBlobDecorator;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.LeaflessTreeDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.SurfaceMushroomGenerator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.DefaultTerrainGen;
import com.mraof.minestuck.world.lands.gen.ILandTerrainGen;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LandAspectRock extends TerrainLandAspect
{
	private final Variant type;
	private final List<TerrainLandAspect> variations;

	public LandAspectRock()
	{
		this(Variant.ROCK);
	}
	
	public LandAspectRock(Variant variation)
	{
		variations = new ArrayList<>();
		type = variation;
		
		if(type == Variant.ROCK)
		{
			variations.add(this);
			variations.add(new LandAspectRock(Variant.PETRIFICATION));
		}
	}

	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.PETRIFICATION) {
			registry.setBlockState("surface", Blocks.STONE.getDefaultState());	
		} else {
			registry.setBlockState("surface", Blocks.GRAVEL.getDefaultState());
		}
		registry.setBlockState("upper", Blocks.COBBLESTONE.getDefaultState());
		registry.setBlockState("structure_primary_decorative", Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));
		registry.setBlockState("structure_primary_stairs", Blocks.STONE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary", MinestuckBlocks.stone.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", MinestuckBlocks.stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.COARSE_CHISELED));
		registry.setBlockState("structure_secondary_stairs", MinestuckBlocks.coarseStoneStairs.getDefaultState());
		registry.setBlockState("structure_planks_slab", Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.BRICK));		registry.setBlockState("structure_planks_slab", Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.BRICK));
		registry.setBlockState("village_path", Blocks.MOSSY_COBBLESTONE.getDefaultState());
		registry.setBlockState("village_fence", Blocks.COBBLESTONE_WALL.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN));
		registry.setBlockState("structure_wool_3", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.GRAY));
	}
	
	@Override
	public String getPrimaryName()
	{
		return type.getName();
	}
	
	@Override
	public String[] getNames()
	{
		if(type == Variant.PETRIFICATION) {
			return new String[] {"petrification"};
		} else {
			return new String[] {"rock", "stone", "ore"};
		}
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		List<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new UndergroundDecoratorVein(Blocks.COAL_ORE.getDefaultState(), 20, 17, 128));
		list.add(new UndergroundDecoratorVein(Blocks.IRON_ORE.getDefaultState(), 20, 9, 64));
		list.add(new UndergroundDecoratorVein(Blocks.REDSTONE_ORE.getDefaultState(), 10, 8, 32));
		list.add(new UndergroundDecoratorVein(Blocks.LAPIS_ORE.getDefaultState(), 4, 7, 24));
		list.add(new UndergroundDecoratorVein(Blocks.GOLD_ORE.getDefaultState(), 4, 9, 32));
		list.add(new UndergroundDecoratorVein(Blocks.DIAMOND_ORE.getDefaultState(), 2, 6, 24));
		list.add(new UndergroundDecoratorVein(Blocks.GRAVEL.getDefaultState(), 10, 33, 256));
		list.add(new UndergroundDecoratorVein(Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONE), 7, 9, 64));
		list.add(new SurfaceDecoratorVein(Blocks.CLAY.getDefaultState(), 25, 20, BiomeMinestuck.mediumOcean));
		
		list.add(new BlockBlobDecorator(Blocks.COBBLESTONE.getDefaultState(), 0, 3, BiomeMinestuck.mediumNormal));
		list.add(new BlockBlobDecorator(Blocks.COBBLESTONE.getDefaultState(), 1, 4, BiomeMinestuck.mediumRough));
		if(type == Variant.ROCK) {
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.petrifiedGrass, true, 25, 32, BiomeMinestuck.mediumRough));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.petrifiedGrass, true, 10, 48, BiomeMinestuck.mediumNormal));
			list.add(new LeaflessTreeDecorator(MinestuckBlocks.petrifiedLog.getDefaultState(), 0.05F, BiomeMinestuck.mediumRough));
			list.add(new BlockBlobDecorator(Blocks.COBBLESTONE.getDefaultState(), 0, 3, BiomeMinestuck.mediumNormal));
			list.add(new BlockBlobDecorator(Blocks.COBBLESTONE.getDefaultState(), 1, 4, BiomeMinestuck.mediumRough));
		} else {
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.petrifiedPoppy, true, 10, 25, BiomeMinestuck.mediumNormal));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.petrifiedPoppy, true, 5, 25, BiomeMinestuck.mediumRough));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.petrifiedGrass, true, 35, 35, BiomeMinestuck.mediumNormal));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.petrifiedGrass, true, 55, 55, BiomeMinestuck.mediumRough));
			list.add(new LeaflessTreeDecorator(MinestuckBlocks.petrifiedLog.getDefaultState(), 0.1F, BiomeMinestuck.mediumNormal));
			list.add(new LeaflessTreeDecorator(MinestuckBlocks.petrifiedLog.getDefaultState(), 2.5F, BiomeMinestuck.mediumRough));
		}
		return list;
	}
	
	@Override
	public float getSkylightBase()
	{
		return 7/8F;
	}
	
	@Override
	public float getTemperature()
	{
		return 0.3F;
	}
	
	@Override
	public float getRainfall()
	{
		return 0.2F;
	}
	
	@Override
	public float getOceanChance()
	{
		return 1/4F;
	}
	
	@Override
	public Vec3d getFogColor()
	{
		return new Vec3d(0.5, 0.5, 0.55);
	}
	
	@Override
	public Vec3d getSkyColor()
	{
		return new Vec3d(0.6D, 0.6D, 0.7D);
	}
	
	@Override
	public ILandTerrainGen createTerrainGenerator(ChunkProviderLands chunkProvider, Random rand)
	{
		DefaultTerrainGen terrainGen = new DefaultTerrainGen(chunkProvider, rand);
		terrainGen.normalVariation = 0.6F;
		terrainGen.roughVariation = 0.9F;
		terrainGen.oceanVariation = 0.4F;
		return terrainGen;
	}
	
	@Override
	public List<TerrainLandAspect> getVariations()
	{
		return variations;
	}
	
	@Override
	public TerrainLandAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromNameTerrain("rock");
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.NAKAGATOR;
	}
	
	public static enum Variant
	{
		ROCK,
		PETRIFICATION;
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}