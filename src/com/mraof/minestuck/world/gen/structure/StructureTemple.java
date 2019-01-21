package com.mraof.minestuck.world.gen.structure;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.lands.structure.IStructure;
import com.mraof.minestuck.world.storage.loot.MinestuckLoot;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class StructureTemple extends WorldGenerator implements IStructure
{

	public static int rotation = 0;
	private static int lowestPointOffset = 16;
	private static int craterRadius = 64;
	
	public StructureTemple() 
	{
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos pos) 
	{
		Debug.warnf("appearifying temple at: %s", pos);
		//worldIn.setBlockState(pos, MinestuckBlocks.templePlacer.getDefaultState());
		//this.generateStructure(worldIn, pos);
		return true;
	}
	
	public static void generateStructure(World world, BlockPos pos)
	{
		
		rotation = new Random().nextInt(4);
		
		
		MinecraftServer server = world.getMinecraftServer();
		TemplateManager manager = worldServer.getStructureTemplateManager();
		ResourceLocation[] templateLoc = 
				{
						new ResourceLocation("minestuck:temple_base"),
						new ResourceLocation("minestuck:temple_middle"),
						new ResourceLocation("minestuck:temple_stairs"),
						new ResourceLocation("minestuck:temple_top"),
						new ResourceLocation("minestuck:pillar_bottom"),
						new ResourceLocation("minestuck:pillar_top"),
						new ResourceLocation("minestuck:pillar_derse")
						
				};
		
		Rotation rot = Rotation.NONE;
		for(int i = 0; i < rotation; i++)
			rot = rot.add(Rotation.CLOCKWISE_90);
		
		
			
        final PlacementSettings settings = new PlacementSettings().setRotation(rot);
		
		Template[] templeTemplate = {manager.get(server, templateLoc[0]), manager.get(server, templateLoc[1]), manager.get(server, templateLoc[2]), manager.get(server, templateLoc[3])};
		Template[] pillarTemplate = {manager.get(server, templateLoc[4]), manager.get(server, templateLoc[5])};
		Template dersePillarTemplate = manager.get(server, templateLoc[6]);
		
		for(int i = 0; i < 16; i++)
		{
			world.setBlockState(pos.up(i), MinestuckBlocks.genericObject.getDefaultState());
		}
		
		generateCrater(pos, world);
		generateTemple(templeTemplate, settings, pos, world);
		generatePillars(pillarTemplate, settings, pos, world);
		generateDersePillar(dersePillarTemplate, settings, pos, world);
	}
	
	public static void generateDersePillar(Template dersePillarTemplate, PlacementSettings settings, BlockPos pos, World world)
	{
		int distance = craterRadius + 16;

		
		switch(rotation)
		{
			default: 
				pos = pos.add(0,0,-distance);
				break;
			case 1:  
				pos = pos.add(-distance,0,0);
				break;
			case 2:  
				pos = pos.add(0,0,distance);
				break;
			case 3:  
				pos = pos.add(distance,0,0);
				break;
		}
		
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		dersePillarTemplate.addBlocksToWorldChunk(world, pos, settings);
	}
	
	public static void generatePillars(Template[] pillarTemplate, PlacementSettings settings, BlockPos pos, World world)
	{
		int radius = (craterRadius * 3) / 4;
		int pillarCount = new Random().nextInt(9);
		pillarCount = pillarCount + 4 - (pillarCount % 2);
		
		//pillarCount = 12;
		
		for(int i = 0; i < pillarCount; i++)
		{
			double angle = (360 / pillarCount * i) * Math.PI / 180;	
			int x = (int)(Math.cos(angle) * radius) - 2;
			int z = (int)(Math.sin(angle) * radius) - 2;
			
			BlockPos piecePos = new BlockPos(pos.getX()+x,pos.getY() - lowestPointOffset,pos.getZ()+z);
			
			world.notifyBlockUpdate(piecePos, world.getBlockState(piecePos), world.getBlockState(piecePos), 3);			
			pillarTemplate[0].addBlocksToWorldChunk(world, piecePos, settings);
			piecePos = piecePos.up(30);
			world.notifyBlockUpdate(piecePos, world.getBlockState(piecePos), world.getBlockState(piecePos), 3);
			pillarTemplate[1].addBlocksToWorldChunk(world, piecePos, settings);
			
		}
		
	}
	
	public static void generateCrater(BlockPos pos, World world)
	{
		int radius = craterRadius;
		
		Debug.warn("generating crater...");
		
		for(int y = 0; y > -lowestPointOffset ; y--)
		{
			for(int x = -radius; x < radius; x++)
			{
				
				int z2 = (int)Math.sqrt(Math.pow(radius, 2) - Math.pow(x, 2));
				
				for(int z = -z2; z < z2; z++)
				{
						BlockPos p = pos.add(x,y,z);
						
						world.setBlockState(p, Blocks.AIR.getDefaultState());
				}
			}
			
			radius = (int)(radius - (Math.pow(y,2) / lowestPointOffset));
		}
		
		
		//y = (x^2 - r^2) / (r^2 / abs(z / (r / (l)))
		//
	}
	
	public static void generateTemple(Template[] templeTemplate, PlacementSettings settings, BlockPos pos, World world)
	{		
		if(pos.down(lowestPointOffset+1).getY() <= 0)
			pos = new BlockPos(pos.getX(),1,pos.getZ());
		else
			pos = pos.down(lowestPointOffset+1);
		
		BlockPos[] pieceOffset = {new BlockPos(0,0,0), new BlockPos(0,32,0), new BlockPos(32,0,0), new BlockPos(12,48,12)};
		BlockPos piecePos;
		Rotation rot = Rotation.NONE;
		for(int i = 0; i < rotation; i++)
			rot = rot.add(Rotation.CLOCKWISE_90);
		
		if(checkTemplateListNull(templeTemplate))
		{
			for(int i = 0; i < templeTemplate.length; i++)
			{
				switch(rot)
				{
					default: 
						piecePos = pos.add(pieceOffset[i].getX()-16, pieceOffset[i].getY(), pieceOffset[i].getZ()-16);
						break;
					case CLOCKWISE_90: 
						piecePos = pos.add(-pieceOffset[i].getZ()+16, pieceOffset[i].getY(), pieceOffset[i].getX()-16);
						break;
					case CLOCKWISE_180: 
						piecePos = pos.add(-pieceOffset[i].getX()+16, pieceOffset[i].getY(), -pieceOffset[i].getZ()+16);
						break;
					case COUNTERCLOCKWISE_90: 
						piecePos = pos.add(pieceOffset[i].getZ()-16, pieceOffset[i].getY(), -pieceOffset[i].getX()+16);
						break;
					
				}
				world.notifyBlockUpdate(piecePos, world.getBlockState(piecePos), world.getBlockState(piecePos), 3);
				templeTemplate[i].addBlocksToWorldChunk(world, piecePos, settings);	
				
				
				Map<BlockPos, String> datablocks = templeTemplate[i].getDataBlocks(piecePos, settings);
				for (Entry<BlockPos, String> entry : datablocks.entrySet())
	            {
	                if ("lotus_loot".equals(entry.getValue()))
	                {
	                    BlockPos blockpos = entry.getKey();
	                    world.setBlockState(blockpos, Blocks.WHITE_SHULKER_BOX.getDefaultState(), 3);
	                    TileEntity tileentity = world.getTileEntity(blockpos);

	                    if (tileentity instanceof TileEntityShulkerBox)
	                    {
	                        ((TileEntityShulkerBox)tileentity).setLootTable(MinestuckLoot.FROG_TEMPLE_LOTUS, new Random().nextLong());
	                    }
	                }
	            }
			}
			
		}
	}
	
	public static boolean checkTemplateListNull(Template[] list)
	{
		for(Template i : list)
			if(i == null)
				return false;
		return true;
	}
	
	
}
