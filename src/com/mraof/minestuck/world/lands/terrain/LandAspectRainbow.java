package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.MesaDecorator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
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
		registry.setBlockState("upper", Blocks.WHITE_TERRACOTTA.getDefaultState());
		registry.setBlockState("surface", Blocks.WHITE_WOOL.getDefaultState());
		//registry.setBlockState("ocean", MinestuckBlocks.blockWatercolors.getDefaultState());
		//registry.setBlockState("river", MinestuckBlocks.blockWatercolors.getDefaultState());
		//registry.setBlockState("structure_primary", MinestuckBlocks.log.getDefaultState().withProperty(BlockMinestuckLog1.VARIANT, BlockMinestuckLog1.BlockType.RAINBOW).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE));
		registry.setBlockState("structure_primary_decorative", Blocks.ACACIA_LOG.getDefaultState());
		registry.setBlockState("structure_primary_stairs", Blocks.DARK_OAK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary", MinestuckBlocks.RAINBOW_PLANKS.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", Blocks.SPRUCE_PLANKS.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", Blocks.JUNGLE_STAIRS.getDefaultState());
		registry.setBlockState("salamander_floor", Blocks.STONE_BRICKS.getDefaultState());
		//registry.setBlockState("fall_fluid", MinestuckBlocks.blockWatercolors.getDefaultState());
		//registry.setBlockState("light_block", MinestuckBlocks.glowingLog.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE));
		BlockState rainbow_leaves = MinestuckBlocks.RAINBOW_LEAVES.getDefaultState();
		//	rainbow_leaves = rainbow_leaves.with(BlockMinestuckLeaves1.CHECK_DECAY, false).withProperty(BlockMinestuckLeaves1.DECAYABLE, false);
		registry.setBlockState("bush", rainbow_leaves);
		registry.setBlockState("mushroom_1", rainbow_leaves);
		registry.setBlockState("mushroom_2", rainbow_leaves);
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.GREEN_WOOL.getDefaultState());
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
		chunkProvider.waterMobsList.add(new SpawnListEntry(EntityType.SQUID, 2, 3, 5));
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		
		list.add(new MesaDecorator().setAltFrequency(.01F));
		//list.add(new WorldgenTreeDecorator(4, new RainbowTree(false), BiomeMinestuck.mediumNormal));
		//list.add(new WorldgenTreeDecorator(2, new RainbowTree(false), BiomeMinestuck.mediumRough));
		
		//Each of these is associated with one of the primary colors in Minecraft: black, red, blue, yellow, green, brown, and white
		list.add(new UndergroundDecoratorVein(Blocks.COAL_ORE.getDefaultState(), 20, 17, 128));
		list.add(new UndergroundDecoratorVein(Blocks.REDSTONE_ORE.getDefaultState(), 10, 8, 32));
		list.add(new UndergroundDecoratorVein(Blocks.LAPIS_ORE.getDefaultState(), 4, 7, 24));
		list.add(new UndergroundDecoratorVein(Blocks.GOLD_ORE.getDefaultState(), 4, 9, 32));
		list.add(new UndergroundDecoratorVein(Blocks.EMERALD_ORE.getDefaultState(), 10, 8, 32));
		list.add(new UndergroundDecoratorVein(Blocks.DIRT.getDefaultState(), 3, 24, 64));
		list.add(new UndergroundDecoratorVein(Blocks.DIORITE.getDefaultState(), 10, 8, 32));
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
