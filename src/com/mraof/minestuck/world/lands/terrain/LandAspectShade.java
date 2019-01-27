package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.BlockColoredDirt;
import com.mraof.minestuck.block.BlockMinestuckStone;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.LeaflessTreeDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceMushroomGenerator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class LandAspectShade extends TerrainLandAspect 
{
	
	static Vec3d skyColor = new Vec3d(0.16D, 0.38D, 0.54D);
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("upper", MinestuckBlocks.coloredDirt.getDefaultState().withProperty(BlockColoredDirt.BLOCK_TYPE, BlockColoredDirt.BlockType.BLUE));
		registry.setBlockState("ocean", MinestuckBlocks.blockOil.getDefaultState());
		registry.setBlockState("structure_primary_decorative", Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));
		registry.setBlockState("structure_primary_stairs", Blocks.STONE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary", MinestuckBlocks.stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.SHADE_BRICK));
		registry.setBlockState("structure_secondary_decorative", MinestuckBlocks.stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.SHADE_SMOOTH));
		registry.setBlockState("structure_secondary_stairs", MinestuckBlocks.shadeBrickStairs.getDefaultState());
		registry.setBlockState("village_path", Blocks.GRAVEL.getDefaultState());
		registry.setBlockState("light_block", MinestuckBlocks.glowingLog.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE));
		registry.setBlockState("torch", Blocks.REDSTONE_TORCH.getDefaultState());
		registry.setBlockState("mushroom_1", MinestuckBlocks.glowingMushroom.getDefaultState());
		registry.setBlockState("mushroom_2", MinestuckBlocks.glowingMushroom.getDefaultState());
		registry.setBlockState("bush", MinestuckBlocks.glowingMushroom.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.CYAN));
		registry.setBlockState("structure_wool_3", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.GRAY));
	}
	
	@Override
	public String getPrimaryName()
	{
		return "shade";
	}
	
	@Override
	public String[] getNames() {
		return new String[] {"shade"};
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new SurfaceMushroomGenerator(10, 64, BiomeMinestuck.mediumNormal));
		list.add(new SurfaceMushroomGenerator(5, 32, BiomeMinestuck.mediumRough));
		list.add(new LeaflessTreeDecorator(MinestuckBlocks.glowingLog.getDefaultState(), 0.5F, BiomeMinestuck.mediumNormal));
		list.add(new LeaflessTreeDecorator(MinestuckBlocks.glowingLog.getDefaultState(), 2, BiomeMinestuck.mediumRough));
		
		list.add(new UndergroundDecoratorVein(Blocks.GRAVEL.getDefaultState(), 8, 33, 256));
		list.add(new UndergroundDecoratorVein(Blocks.IRON_ORE.getDefaultState(), 24, 9, 64));
		list.add(new UndergroundDecoratorVein(Blocks.LAPIS_ORE.getDefaultState(), 6, 7, 35));
		
		return list;
	}
	
	@Override
	public float getSkylightBase()
	{
		return 0F;
	}
	
	@Override
	public Vec3d getFogColor() 
	{
		return skyColor;
	}
	
	@Override
	public int getWeatherType()
	{
		return 0;
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.SALAMANDER;
	}
}
