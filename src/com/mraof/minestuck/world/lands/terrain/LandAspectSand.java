
package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.decorator.BasicTreeDecorator;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceMushroomGenerator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.WorldGenDecorator;
import com.mraof.minestuck.world.lands.decorator.structure.OasisDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.LandTerrainGenBase;
import com.mraof.minestuck.world.lands.gen.RiverFreeTerrainGen;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenDeadBush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LandAspectSand extends TerrainLandAspect
{
	private final Vec3d fogColor, skyColor;
	private final Variant type;
	private final List<TerrainLandAspect> variations;
	
	public LandAspectSand()
	{
		this(Variant.SAND);
	}
	
	public LandAspectSand(Variant variation)
	{
		variations = new ArrayList<>();
		type = variation;
		
		if(type == Variant.SAND)
		{
			fogColor = new Vec3d(0.99D, 0.8D, 0.05D);
			skyColor = new Vec3d(0.8D, 0.8D, 0.1D);
			
			variations.add(this);
			variations.add(new LandAspectSand(Variant.SAND_RED));
			variations.add(new LandAspectSand(Variant.LUSH_DESERTS));
		} else
		{
			fogColor = new Vec3d(0.99D, 0.6D, 0.05D);
			skyColor = new Vec3d(0.8D, 0.6D, 0.1D);
		}
		
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.SAND || type == Variant.LUSH_DESERTS)
		{
			registry.setBlockState("upper", Blocks.SAND.getDefaultState());
			registry.setBlockState("ground", Blocks.SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary", Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH));
			registry.setBlockState("structure_primary_decorative", Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED));
			registry.setBlockState("structure_primary_stairs", Blocks.SANDSTONE_STAIRS.getDefaultState());
			registry.setBlockState("village_path", Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND));
		} else
		{
			registry.setBlockState("upper", Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND));
			registry.setBlockState("ground", Blocks.RED_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary", Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH));
			registry.setBlockState("structure_primary_decorative", Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED));
			registry.setBlockState("structure_primary_stairs", Blocks.RED_SANDSTONE_STAIRS.getDefaultState());
			registry.setBlockState("village_path", Blocks.SAND.getDefaultState());
		}
		registry.setBlockState("structure_secondary", Blocks.STONEBRICK.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));
		registry.setBlockState("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_planks", Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA));
		registry.setBlockState("structure_planks_slab", Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA));
		registry.setBlockState("river", registry.getBlockState("upper"));
		registry.setBlockState("structure_wool_1", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW));
		registry.setBlockState("structure_wool_3", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.MAGENTA));
	}
	
	@Override
	public String getPrimaryName()
	{
		return type.getName();
	}

	@Override
	public String[] getNames()
	{
		if(type == Variant.LUSH_DESERTS) {
			return new String[] {"lush_deserts"};
		} else {
			return new String[] {"sand", "dune", "desert"};
		}
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new WorldGenDecorator(new WorldGenCactus(), 15, 0.4F, BiomeMinestuck.mediumNormal));
		list.add(new WorldGenDecorator(new WorldGenCactus(), 5, 0.4F, BiomeMinestuck.mediumRough));
		list.add(new WorldGenDecorator(new WorldGenDeadBush(), 1, 0.4F, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
		list.add(new OasisDecorator());
		if(type == Variant.LUSH_DESERTS) {
			list.add(new WorldGenDecorator(new WorldGenCactus(), 45, 0.4F, BiomeMinestuck.mediumNormal));
			list.add(new WorldGenDecorator(new WorldGenCactus(), 30, 0.4F, BiomeMinestuck.mediumRough));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.bloomingCactus, true, 55, 15, BiomeMinestuck.mediumRough));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.bloomingCactus, true, 65, 15, BiomeMinestuck.mediumNormal));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.desertBush, true, 1, 1, BiomeMinestuck.mediumNormal));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.desertBush, true, 1, 1, BiomeMinestuck.mediumRough));
		} else {
			list.add(new WorldGenDecorator(new WorldGenCactus(), 15, 0.4F, BiomeMinestuck.mediumNormal));
			list.add(new WorldGenDecorator(new WorldGenCactus(), 5, 0.4F, BiomeMinestuck.mediumRough));
			list.add(new WorldGenDecorator(new WorldGenDeadBush(), 1, 0.4F, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
			list.add(new WorldGenDecorator(new WorldGenDeadBush(), 1, 0.4F, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.bloomingCactus, true, 5, 32, BiomeMinestuck.mediumRough));
		}
		list.add(new OasisDecorator(BiomeMinestuck.mediumNormal));
		
		list.add(new UndergroundDecoratorVein(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, type == Variant.SAND_RED?BlockSand.EnumType.RED_SAND:BlockSand.EnumType.SAND), 8, 28, 256));
		list.add(new UndergroundDecoratorVein((type == Variant.SAND_RED?MinestuckBlocks.ironOreSandstoneRed:MinestuckBlocks.ironOreSandstone).getDefaultState(), 24, 9, 64));
		list.add(new UndergroundDecoratorVein((type == Variant.SAND_RED?MinestuckBlocks.goldOreSandstoneRed:MinestuckBlocks.goldOreSandstone).getDefaultState(), 6, 9, 32));
		
		return list;
	}
	
	@Override
	public float getTemperature()
	{
		return 2.0F;
	}
	
	@Override
	public float getRainfall()
	{
		return 0.0F;
	}
	
	@Override
	public float getOceanChance()
	{
		return 0F;
	}
	
	@Override
	public Vec3d getFogColor() 
	{
		return fogColor;
	}
	
	@Override
	public Vec3d getSkyColor()
	{
		return skyColor;
	}
	
	@Override
	public List<TerrainLandAspect> getVariations()
	{
		return variations;
	}
	
	@Override
	public TerrainLandAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromNameTerrain("sand");
	}
	
	@Override
	public LandTerrainGenBase createTerrainGenerator(ChunkProviderLands chunkProvider, Random rand)
	{
		return new RiverFreeTerrainGen(chunkProvider, rand);
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.TURTLE;
	}
	
	public static enum Variant
	{
		SAND,
		LUSH_DESERTS,
		SAND_RED;
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}
