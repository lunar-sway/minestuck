package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.*;
import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.biome.ModBiomes;
import com.mraof.minestuck.world.lands.decorator.*;
import com.mraof.minestuck.world.lands.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;

import java.util.ArrayList;
import java.util.List;

public class LandAspectFrost extends TerrainLandAspect
{
	static Vec3d fogColor = new Vec3d(0.5D, 0.6D, 0.98D);
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", Blocks.GRASS.getDefaultState());
		registry.setBlockState("upper", Blocks.DIRT.getDefaultState());
		registry.setBlockState("structure_primary", Blocks.PRISMARINE.getDefaultState());
		registry.setBlockState("structure_primary_decorative", Blocks.PRISMARINE_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary", MinestuckBlocks.FROST_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", MinestuckBlocks.FROST_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", MinestuckBlocks.CHISELED_FROST_BRICKS.getDefaultState());
		registry.setBlockState("structure_planks", Blocks.SPRUCE_PLANKS.getDefaultState());
		registry.setBlockState("structure_planks_slab", Blocks.SPRUCE_SLAB.getDefaultState());
		registry.setBlockState("river", Blocks.ICE.getDefaultState());
		registry.setBlockState("light_block", Blocks.SEA_LANTERN.getDefaultState());
		registry.setBlockState("bucket1", Blocks.SNOW.getDefaultState());
		registry.setBlockState("bush", Blocks.FERN.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.WHITE_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.CYAN_WOOL.getDefaultState());
	}
	
	@Override
	public String getPrimaryName()
	{
		return "frost";
	}
	
	@Override
	public String[] getNames() {
		return new String[] {"frost", "ice", "snow"};
	}
	
	@Override
	public void setBiomeSettings(LandBiomeHolder settings)
	{
		settings.rainType = Biome.RainType.SNOW;
		settings.temperature = 0.0F;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = 1/4F;
	}
	
	@Override
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blockRegistry)
	{
		DefaultBiomeFeatures.addFreezeTopLayer(biome);
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new IceDecorator());
		list.add(new LayeredBlockDecorator(Blocks.SNOW, true));
		//list.add(new SpruceTreeDecorator(MinestuckBlocks.log.getDefaultState().withProperty(BlockMinestuckLog1.VARIANT, BlockMinestuckLog1.BlockType.FROST), MinestuckBlocks.leaves1.getDefaultState().withProperty(BlockMinestuckLeaves1.VARIANT, BlockMinestuckLeaves1.BlockType.FROST).withProperty(BlockMinestuckLeaves1.CHECK_DECAY, Boolean.valueOf(false)), BiomeMinestuck.mediumNormal));
		//list.add(new SpruceTreeDecorator(MinestuckBlocks.log.getDefaultState().withProperty(BlockMinestuckLog1.VARIANT, BlockMinestuckLog1.BlockType.FROST), MinestuckBlocks.leaves1.getDefaultState().withProperty(BlockMinestuckLeaves1.VARIANT, BlockMinestuckLeaves1.BlockType.FROST).withProperty(BlockMinestuckLeaves1.CHECK_DECAY, Boolean.valueOf(false)), BiomeMinestuck.mediumRough));

		list.add(new SurfaceDecoratorVein(Blocks.COARSE_DIRT.getDefaultState(), 10, 32, ModBiomes.mediumRough, ModBiomes.mediumOcean));
		list.add(new SurfaceDecoratorVein(Blocks.ICE.getDefaultState(), 5, 8, ModBiomes.mediumRough));
		list.add(new SurfaceDecoratorVein(Blocks.SNOW_BLOCK.getDefaultState(), 8, 16, ModBiomes.mediumRough));
		list.add(new SurfaceDecoratorVein(Blocks.SNOW_BLOCK.getDefaultState(), 15, 16, ModBiomes.mediumNormal));
		
		list.add(new UndergroundDecoratorVein(Blocks.PACKED_ICE.getDefaultState(), 2, 8, 64));
		list.add(new UndergroundDecoratorVein(Blocks.SNOW_BLOCK.getDefaultState(), 3, 16, 64));
		list.add(new UndergroundDecoratorVein(Blocks.DIRT.getDefaultState(), 3, 28, 64));
		list.add(new UndergroundDecoratorVein(Blocks.COAL_ORE.getDefaultState(), 13, 17, 64));
		list.add(new UndergroundDecoratorVein(Blocks.DIAMOND_ORE.getDefaultState(), 3, 6, 24));
		return list;
	}
	
	@Override
	public float getSkylightBase()
	{
		return 7/8F;
	}
	
	@Override
	public Vec3d getFogColor()
	{
		return fogColor;
	}
	
	@Override
	public Vec3d getSkyColor()
	{
		return new Vec3d(0.6D, 0.7D, 0.9D);
	}
	
	@Override
	public int getWeatherType()
	{
		return 1;
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return ModEntityTypes.IGUANA;
	}
}