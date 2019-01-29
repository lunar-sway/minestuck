package com.mraof.minestuck.world.gen.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.block.MinestuckBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class ComponentCloudDungeonCenterPiece extends ComponentCloudDungeonPiece 
{

    /** List of other Castle components linked to this room. */

    protected boolean bottom;
    public int averageGroundLevel = -1;
    public int castleWidth, castleLength, x, z, totalPieces;
    
    public ComponentCloudDungeonCenterPiece() {}
	protected ComponentCloudDungeonCenterPiece(int par1, int x, int z) 
	{
		super(par1, (ComponentCloudDungeonCenterPiece)null);
        this.boundingBox = new StructureBoundingBox(x, 0, z, x, 74, z);
        this.x = x;
        this.z = z;
		if(pendingPieces == null)pendingPieces = new ArrayList<ComponentCloudDungeonPiece>();
 		this.bottom = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void buildComponent(StructureComponent structureComponent,
			@SuppressWarnings("rawtypes") List components, Random random)
	{
		this.castleWidth = (random.nextInt(12) + 4) * 16;
		this.castleLength = (random.nextInt(24) + 8) * 16;
		this.componentType = 1;
		this.getNextComponentNormal(this, components, random, 8, 0);
		this.componentType = 2;
		for(int depth = 8; depth < this.castleLength; depth += 8)
			this.getNextComponentNormal(this, components, random,  0, depth);
		this.componentType = 0;
		this.getNextComponentNormal(this, (List<ComponentCloudDungeonPiece>)components, random, 0, -8, 0);
		
	}
	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox)
	{
//    	Debug.print("addComponentParts in ComponentCastleStartPiece running");
        if (this.averageGroundLevel < 0)
        {
            this.averageGroundLevel = this.getAverageGroundLevel(world, structureBoundingBox);

            if (this.averageGroundLevel < 0)
            {
                return false;
            }

        }
        this.boundingBox.offset(0, this.averageGroundLevel - 1, 0);
        if (this.isLiquidInStructureBoundingBox(world, structureBoundingBox))
        {
            return false;
        }
        else
        {
//        	Debug.print("CCSP: " + this.averageGroundLevel);
//            Debug.print(structureBoundingBox.minX + ", " + structureBoundingBox.minY + ", " + structureBoundingBox.minZ + ", " + structureBoundingBox.maxX + ", " + structureBoundingBox.maxY + ", " + structureBoundingBox.maxZ);
            this.fillWithBlock(world, structureBoundingBox, 0, 0, 0,  7, 6, 7, MinestuckBlocks.chessTile.getDefaultState());
            this.fillWithBlock(world, structureBoundingBox, 0, 0, 0,  7, 7, 0, MinestuckBlocks.chessTile.getDefaultState());
            this.fillWithBlock(world, structureBoundingBox, 0, 0, 7,  7, 7, 7, MinestuckBlocks.chessTile.getDefaultState());
            this.fillWithAir(world, structureBoundingBox, 2, 1, 0, 5, 5, 7);

            return true;
        }
    }
	@Override
	protected void writeStructureToNBT(NBTTagCompound p_143012_1_) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
	{
		// TODO Auto-generated method stub
		
	}
}