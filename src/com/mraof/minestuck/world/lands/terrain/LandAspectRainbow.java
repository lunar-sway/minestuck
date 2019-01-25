package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.block.BlockMinestuckLeaves1;
import com.mraof.minestuck.block.BlockMinestuckLog1;
import com.mraof.minestuck.block.BlockMinestuckPlanks;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.gen.feature.WorldGenRainbowTree;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.MesaDecorator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.WorldgenTreeDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.ILandTerrainGen;
import com.mraof.minestuck.world.lands.gen.SpecialTerrainGen;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;

import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome.SpawnListEntry;

public class LandAspectRainbow extends TerrainLandAspect 
{
	static Vec3d fogColor = new Vec3d(0.0D, 0.6D, 0.8D);
	static Vec3d skyColor = new Vec3d(0.9D, 0.6D, 0.8D);
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("ground", Blocks.STONE.getDefaultState());
		registry.setBlockState("upper", Blocks.STAINED_HARDENED_CLAY.getDefaultState());
		registry.setBlockState("surface", Blocks.WOOL.getDefaultState());
		registry.setBlockState("ocean", MinestuckBlocks.blockWatercolors.getDefaultState());
		registry.setBlockState("river", MinestuckBlocks.blockWatercolors.getDefaultState());
		registry.setBlockState("structure_primary", MinestuckBlocks.log.getDefaultState().withProperty(BlockMinestuckLog1.VARIANT, BlockMinestuckLog1.BlockType.RAINBOW).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE));
		registry.setBlockState("structure_primary_decorative", Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA));
		registry.setBlockState("structure_primary_stairs", Blocks.DARK_OAK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary", MinestuckBlocks.planks.getDefaultState().withProperty(BlockMinestuckPlanks.VARIANT, BlockMinestuckPlanks.BlockType.RAINBOW));
		registry.setBlockState("structure_secondary_decorative", Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE));
		registry.setBlockState("structure_secondary_stairs", Blocks.JUNGLE_STAIRS.getDefaultState());
		registry.setBlockState("salamander_floor", Blocks.STONEBRICK.getDefaultState());
		registry.setBlockState("fall_fluid", MinestuckBlocks.blockWatercolors.getDefaultState());
		registry.setBlockState("light_block", MinestuckBlocks.glowingLog.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE));
		IBlockState rainbow_leaves = MinestuckBlocks.leaves1.getDefaultState().withProperty(BlockMinestuckLeaves1.VARIANT, BlockMinestuckLeaves1.BlockType.RAINBOW);
			rainbow_leaves = rainbow_leaves.withProperty(BlockMinestuckLeaves1.CHECK_DECAY, false).withProperty(BlockMinestuckLeaves1.DECAYABLE, false);
		registry.setBlockState("bush", rainbow_leaves);
		registry.setBlockState("mushroom_1", rainbow_leaves);
		registry.setBlockState("mushroom_2", rainbow_leaves);
		registry.setBlockState("structure_wool_1", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW));
		registry.setBlockState("structure_wool_3", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.GREEN));
	}
	
	@Override
	public ILandTerrainGen createTerrainGenerator(ChunkProviderLands chunkProvider, Random rand)
	{
		return new SpecialTerrainGen(chunkProvider, rand);
	}
	
	@Override
	public String getPrimaryName()
	{
		return "rainbow";
	}

	@Override
	public String[] getNames() {
		return new String[] {"rainbow", "colors"};
	}
	
	@Override
	public void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.waterMobsList.add(new SpawnListEntry(EntitySquid.class, 2, 3, 5));
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		
		list.add(new MesaDecorator().setAltFrequency(.01F));
		list.add(new WorldgenTreeDecorator(4, new WorldGenRainbowTree(false), BiomeMinestuck.mediumNormal));
		list.add(new WorldgenTreeDecorator(2, new WorldGenRainbowTree(false), BiomeMinestuck.mediumRough));
		
		//Each of these is associated with one of the primary colors in Minecraft: black, red, blue, yellow, green, brown, and white
		list.add(new UndergroundDecoratorVein(Blocks.COAL_ORE.getDefaultState(), 20, 17, 128));
		list.add(new UndergroundDecoratorVein(Blocks.REDSTONE_ORE.getDefaultState(), 10, 8, 32));
		list.add(new UndergroundDecoratorVein(Blocks.LAPIS_ORE.getDefaultState(), 4, 7, 24));
		list.add(new UndergroundDecoratorVein(Blocks.GOLD_ORE.getDefaultState(), 4, 9, 32));
		list.add(new UndergroundDecoratorVein(Blocks.EMERALD_ORE.getDefaultState(), 10, 8, 32));
		list.add(new UndergroundDecoratorVein(Blocks.DIRT.getDefaultState(), 3, 24, 64));
		list.add(new UndergroundDecoratorVein(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), 10, 8, 32));
		return list;
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
	public float getTemperature()
	{
		return 1.0F;
	}
	
	@Override
	public float getRainfall()
	{
		return 0.6F;
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.TURTLE;
	}
}
