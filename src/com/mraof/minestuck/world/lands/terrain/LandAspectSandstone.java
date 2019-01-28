package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.decorator.*;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.DefaultTerrainGen;
import com.mraof.minestuck.world.lands.gen.ILandTerrainGen;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenDeadBush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LandAspectSandstone extends TerrainLandAspect
{
	
	private final Vec3d fogColor, skyColor;
	private final Variant type;
	private final List<TerrainLandAspect> variations;
	
	public LandAspectSandstone()
	{
		this(Variant.SANDSTONE);
	}
	
	public LandAspectSandstone(Variant type)
	{
		variations = new ArrayList<>();
		this.type = type;
		if(type == Variant.SANDSTONE)
		{
			fogColor = new Vec3d(0.9D, 0.7D, 0.05D);
			skyColor = new Vec3d(0.8D, 0.6D, 0.2D);
			
			variations.add(this);
			variations.add(new LandAspectSandstone(Variant.SANDSTONE_RED));
		} else
		{
			fogColor = new Vec3d(0.7D, 0.4D, 0.05D);
			skyColor = new Vec3d(0.8D, 0.5D, 0.1D);
			
		}
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.SANDSTONE)
		{
			registry.setBlockState("upper", Blocks.SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary", Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH));
			registry.setBlockState("structure_primary_decorative", Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED));
			registry.setBlockState("structure_primary_stairs", Blocks.SANDSTONE_STAIRS.getDefaultState());
			registry.setBlockState("village_path", Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND));
		} else
		{
			registry.setBlockState("upper", Blocks.RED_SANDSTONE.getDefaultState());
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
		registry.setBlockState("torch", Blocks.REDSTONE_TORCH.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE));
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
		return new String[] {"sandstone", "desertStone"};
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		List<ILandDecorator> list = new ArrayList<ILandDecorator>();
		IBlockState sand = Blocks.SAND.getDefaultState();
		IBlockState sandstone = Blocks.SANDSTONE.getDefaultState();
		if(type == Variant.SANDSTONE_RED)
		{
			sand = sand.withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
			sandstone = Blocks.RED_SANDSTONE.getDefaultState();
		}
		list.add(new SurfaceDecoratorVein(sand, 10, 32));
		list.add(new BlockBlobDecorator(sandstone, 0, 3, BiomeMinestuck.mediumNormal));
		list.add(new BlockBlobDecorator(sandstone, 0, 5, BiomeMinestuck.mediumRough));
		list.add(new WorldGenDecorator(new WorldGenDeadBush(), 15, 0.4F));
		
		list.add(new UndergroundDecoratorVein(sandstone, 8, 28, 256));
		list.add(new UndergroundDecoratorVein(Blocks.IRON_ORE.getDefaultState(), 24, 9, 64));
		list.add(new UndergroundDecoratorVein(Blocks.REDSTONE_ORE.getDefaultState(), 12, 8, 32));
		return list;
	}
	
	@Override
	public float getSkylightBase()
	{
		return 3/4F;
	}
	
	@Override
	public float getTemperature()
	{
		return 1.8F;
	}
	
	@Override
	public float getRainfall()
	{
		return 0.0F;
	}
	
	@Override
	public float getOceanChance()
	{
		return 1/10F;
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
	public TerrainLandAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromNameTerrain("sandstone");
	}
	
	@Override
	public List<TerrainLandAspect> getVariations()
	{
		return variations;
	}
	
	@Override
	public ILandTerrainGen createTerrainGenerator(ChunkProviderLands chunkProvider, Random rand)
	{
		DefaultTerrainGen terrainGen = new DefaultTerrainGen(chunkProvider, rand);
		terrainGen.normalVariation = 0.6F;
		terrainGen.oceanVariation = 0.3F;
		return terrainGen;
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.TURTLE;
	}
	
	public static enum Variant
	{
		SANDSTONE,
		SANDSTONE_RED;
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}