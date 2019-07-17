package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.block.BlockMinestuckStone;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.block.MinestuckBlocks.EnumSlabStairMaterial;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.LeaflessTreeDecorator;
import com.mraof.minestuck.world.lands.decorator.PillarDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.DefaultTerrainGen;
import com.mraof.minestuck.world.lands.gen.ILandTerrainGen;
import com.mraof.minestuck.world.lands.gen.LandTerrainGenBase;
import com.mraof.minestuck.world.lands.structure.MapGenCloudDungeon;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.title.LandAspectLight;

import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.structure.MapGenStructure;

public class LandAspectRain extends TerrainLandAspect 
{
	static Vec3d skyColor = new Vec3d(0.3D, 0.5D, 0.98D);
	static Vec3d fogColor = new Vec3d(0.9D, 0.8D, 0.6D);
	
	//TODO:
	//Magic Beans
	//Fertile Soil
	//Giant Beanstalks (only grows on fertile soil, underneath cloud blocks)
	
	//Cloud blocks
	
	//Pink stone brick temples		Monsters in these temples tend to guard living trees, Magic Beans, and Fertile Soil.
	//Light Cloud Dungeons
	//Custom dungeon loot
	
	//Do not add anything underwater. It is important that the oceans are boring and empty.
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", MinestuckBlocks.chalk.getDefaultState());
		registry.setBlockState("upper", MinestuckBlocks.chalk.getDefaultState());
		registry.setBlockState("ground", MinestuckBlocks.pinkStoneSmooth.getDefaultState());
		registry.setBlockState("ocean", Blocks.WATER.getDefaultState());
		registry.setBlockState("structure_primary", MinestuckBlocks.pinkStoneBricks.getDefaultState());
		registry.setBlockState("structure_primary_stairs", EnumSlabStairMaterial.PINK_BRICK.getStair().getDefaultState());
		registry.setBlockState("structure_primary_decorative", MinestuckBlocks.pinkStoneChisel.getDefaultState());
		registry.setBlockState("structure_secondary", MinestuckBlocks.pinkStonePolish.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", EnumSlabStairMaterial.CHALK_BRICK.getStair().getDefaultState());
		registry.setBlockState("structure_secondary_decorative", MinestuckBlocks.pinkStoneChisel.getDefaultState());
		registry.setBlockState("structure_planks", MinestuckBlocks.deadPlanks.getDefaultState());
		registry.setBlockState("structure_planks_slab", EnumSlabStairMaterial.DEAD.getSlab().getDefaultState());
		registry.setBlockState("bush", Blocks.DEADBUSH.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW));
		registry.setBlockState("structure_wool_3", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.MAGENTA));
	}
	
	@Override
	public String getPrimaryName()
	{
		return "rain";
	}
	
	@Override
	public String[] getNames() {
		return new String[] {"rain", "islands", "sky", "clouds"};
	}
	
	@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.aspect2 instanceof LandAspectLight)
		{
			chunkProvider.blockRegistry.setBlockState("ocean", MinestuckBlocks.blockLightWater.getDefaultState());
			chunkProvider.decorators.forEach((decorator) -> {
				if(decorator instanceof PillarDecorator && ((PillarDecorator) decorator).blockType.equals("light_block"))
				{
					chunkProvider.decorators.remove(decorator);
				}
			});
		}
	}
	
	@Override
	public ILandTerrainGen createTerrainGenerator(ChunkProviderLands chunkProvider, Random rand)
	{
		DefaultTerrainGen out = new DefaultTerrainGen(chunkProvider, rand);
		out.setSeaHeight(64);		//The ocean is two blocks higher than any other Lands
		out.oceanVariation += 0.2F;	//The ground under the ocean is a bit more varied
		out.roughHeight -= 0.1F;	//Rough biomes average a bit lower than normal
		out.roughVariation += 0.1F;	//But they are more hilly overall
		return out;
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new LeaflessTreeDecorator(MinestuckBlocks.deadLog.getDefaultState(), 0.5F, BiomeMinestuck.mediumNormal));
		list.add(new LeaflessTreeDecorator(MinestuckBlocks.deadLog.getDefaultState(), 0.25F, BiomeMinestuck.mediumRough));
		
		list.add(new UndergroundDecoratorVein(MinestuckBlocks.pinkStonePolish.getDefaultState(), 2, 8, 64));
		list.add(new UndergroundDecoratorVein(MinestuckBlocks.coalOrePinkStone.getDefaultState(), 13, 17, 64));
		list.add(new UndergroundDecoratorVein(MinestuckBlocks.lapisOrePinkStone.getDefaultState(), 4, 7, 24));
		list.add(new UndergroundDecoratorVein(MinestuckBlocks.goldOrePinkStone.getDefaultState(), 4, 9, 32));
		list.add(new UndergroundDecoratorVein(MinestuckBlocks.diamondOrePinkStone.getDefaultState(), 3, 6, 24));
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
		return 0.5F;
	}
	
	@Override
	public float getOceanChance()
	{
		return 3/4F;
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
	
	@Override
	public MapGenStructure customMapGenStructure(ChunkProviderLands cpl)
	{
		return new MapGenCloudDungeon(cpl);
	}
}