package com.mraof.minestuck.world.gen.structure.temple;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class ComponentTempleStartPiece extends ComponentTemplePiece
{
	public int x, y, z;
	public World world;
	
	protected ComponentTempleStartPiece(int par1, int x, int z, World worldIn) 
	{
		super(par1, (ComponentTempleStartPiece)null);
        this.boundingBox = new StructureBoundingBox(x, 0, z, x, 74, z);
        this.x = x;
        this.z = z;
        world = worldIn;
	}
	
	@Override
	public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) 
	{
		BlockPos pos = new BlockPos(x, getAverageGroundLevel(world), z);
		
		generateTemple(world, rand, pos);
	}

    protected BlockPos generateTemple(World world, Random random, BlockPos pos) {
        
    	pos = pos.down(8);
    	Map<BlockPos, String> datablocks;
    	String[] templateStr = {"minestuck:temple_base", "minestuck:temple_middle", "minestuck:temple_stairs", "minestuck:temple_top"};
    	BlockPos[] pieceOffset = {new BlockPos(0,0,0), new BlockPos(0,32,0), new BlockPos(32,0,0), new BlockPos(12,48,12)};
    	
        direction = random.nextInt(4);
        Rotation rotation;
        
        if(!world.getBlockState(pos).getBlock().isTopSolid(world.getBlockState(pos)) || !world.canSeeSky(pos.up(8)))
    		return null;        
        for(int i = 0; i < templateStr.length; i++)
        {
        	BlockPos piecePos;
        	switch(direction)
            {
            default: 
            	rotation = Rotation.NONE; 
            	piecePos = pos.add(pieceOffset[i].getX(), pieceOffset[i].getY(), pieceOffset[i].getZ());
            break;	
            case 1: 
            	rotation = Rotation.CLOCKWISE_90; 
            	piecePos = pos.add(pieceOffset[i].getZ(), pieceOffset[i].getY(), pieceOffset[i].getX());
            break;
            case 2: 
            	rotation = Rotation.CLOCKWISE_180; 
            	piecePos = pos.add(-pieceOffset[i].getX(), pieceOffset[i].getY(), pieceOffset[i].getZ());
            break;
            case 3: 
            	rotation = Rotation.COUNTERCLOCKWISE_90; 
            	piecePos = pos.add(pieceOffset[i].getZ(), pieceOffset[i].getY(), -pieceOffset[i].getX());
            break;
            
            }
            final PlacementSettings settings = new PlacementSettings().setRotation(rotation);
            
        	
        	Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), new ResourceLocation(templateStr[i]));
        	
        	datablocks = template.getDataBlocks(piecePos, settings);
        	template.addBlocksToWorld(world, piecePos, settings);
        	
        	for (Entry<BlockPos, String> entry : datablocks.entrySet())
            {
                if ("loot".equals(entry.getValue()))
                {
                    BlockPos blockpos = entry.getKey();
                    world.setBlockState(blockpos, Blocks.WHITE_SHULKER_BOX.getDefaultState(), 3);
                    TileEntity tileentity = world.getTileEntity(blockpos);

                    if (tileentity instanceof TileEntityShulkerBox)
                    {
                    	//TODO frog temple lotus loot
                        ((TileEntityShulkerBox)tileentity).setLootTable(LootTableList.CHESTS_IGLOO_CHEST, random.nextLong());
                    }
                }
            }
        }
        
        
        return pos;
    }
	
	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
		// TODO Auto-generated method stub
		return false;
	}

}
