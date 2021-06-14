package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class TowerFeature extends Feature<NoFeatureConfig>
{
	private static final ResourceLocation STRUCTURE_TOWER = new ResourceLocation(Minestuck.MOD_ID, "tower");
	private static final ResourceLocation STRUCTURE_TOWER_WITH_CHEST = new ResourceLocation(Minestuck.MOD_ID, "tower_with_chest");
	private static final ResourceLocation STRUCTURE_TOWER_WALL = new ResourceLocation(Minestuck.MOD_ID, "tower_wall");
	private static final ResourceLocation STRUCTURE_ELEVATED_TOWER_DOOR = new ResourceLocation(Minestuck.MOD_ID, "elevated_tower_door");
	private static final ResourceLocation STRUCTURE_TOWER_DOOR = new ResourceLocation(Minestuck.MOD_ID, "tower_door");
	private static final ResourceLocation STRUCTURE_TOWER_BALCONY = new ResourceLocation(Minestuck.MOD_ID, "tower_balcony");
	
	public TowerFeature(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		Rotation rotation = Rotation.getRandom(rand);
		ResourceLocation tower = rand.nextInt(50) == 0 ? STRUCTURE_TOWER_WITH_CHEST : STRUCTURE_TOWER;
		TemplateManager templates = world.getLevel().getStructureManager();
		Template template = templates.getOrCreate(tower);
		
		PlacementSettings settings = new PlacementSettings().setChunkPos(new ChunkPos(pos)).setRandom(rand).addProcessor(StructureBlockRegistryProcessor.INSTANCE);
		
		BlockPos size = template.getSize(rotation);
		int xOffset = rand.nextInt(16 - size.getX() - 2) + 1, zOffset = rand.nextInt(16 - size.getZ() - 2) + 1;
		
		int y = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos.getX() + xOffset + size.getX()/2, pos.getZ() + zOffset + size.getZ()/2) - 2;
		
		BlockPos center = pos.offset(xOffset + size.getX()/2, y - pos.getY(), zOffset + size.getZ()/2);
		final int doorSide = 1, doorFront = 4;
		for(Direction direction : Direction.Plane.HORIZONTAL)
		{
			BlockPos doorPos = center.relative(direction, doorFront);
			
			ResourceLocation doorType;
			if(world.getBlockState(doorPos.above(2)).canOcclude())
			{
				if(world.getBlockState(doorPos.above(3)).canOcclude())
				{
					doorType = STRUCTURE_TOWER_WALL;
				} else
				{
					doorType = STRUCTURE_ELEVATED_TOWER_DOOR;
				}
			} else
			{
				if(world.getBlockState(doorPos.above()).canOcclude())
				{
					doorType = STRUCTURE_TOWER_DOOR;
				} else
				{
					doorType = STRUCTURE_TOWER_BALCONY;
				}
			}
			
			Template door = templates.getOrCreate(doorType);
			
			Rotation doorRotation;
			switch(direction)
			{
				case EAST:
					doorRotation = Rotation.CLOCKWISE_90;
					break;
				case SOUTH:
					doorRotation = Rotation.CLOCKWISE_180;
					break;
				case WEST:
					doorRotation = Rotation.COUNTERCLOCKWISE_90;
					break;
				case NORTH: default:
					doorRotation = Rotation.NONE;
					break;
			}
			
			settings.setRotation(doorRotation);
			BlockPos structurePos = doorPos.relative(direction.getCounterClockWise(), doorSide).relative(direction, door.getSize(doorRotation).getZ() - 2);
			door.placeInWorld(world, structurePos, structurePos, settings, rand, Constants.BlockFlags.NO_RERENDER);
		}
		
		settings.setRotation(rotation);
		BlockPos structurePos = template.getZeroPositionWithTransform(new BlockPos(pos.getX() + xOffset, y, pos.getZ() + zOffset), Mirror.NONE, rotation);
		template.placeInWorld(world, structurePos, structurePos, settings, rand, Constants.BlockFlags.NO_RERENDER);
		
		for(Template.BlockInfo blockInfo : template.filterBlocks(structurePos, settings, Blocks.STRUCTURE_BLOCK))
		{
			if(blockInfo.nbt != null)
			{
				StructureMode structuremode = StructureMode.valueOf(blockInfo.nbt.getString("mode"));
				if (structuremode == StructureMode.DATA)
				{
					String data = blockInfo.nbt.getString("metadata");
					if(data.equals("basic_chest"))
					{
						world.setBlock(blockInfo.pos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.DEFAULT);
						TileEntity tileentity = world.getBlockEntity(blockInfo.pos.below());
						if (tileentity instanceof ChestTileEntity)
						{
							((ChestTileEntity) tileentity).setLootTable(MSLootTables.BASIC_MEDIUM_CHEST, rand.nextLong());
						}
					}
				}
			}
		}
		
		return true;
	}
}