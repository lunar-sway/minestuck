package com.mraof.minestuck.world.gen.feature.structure;

public class ComponentCastleRoomPiece extends ComponentCastlePiece
{
	public ComponentCastleRoomPiece() {}
	/*protected ComponentCastleRoomPiece(int par1, ComponentCastleStartPiece startPiece, StructureBoundingBox structureBoundingBox)
	{
		super(par1, startPiece);
		this.boundingBox = structureBoundingBox;
		this.componentType = 2;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void buildComponent(StructureComponent par1StructureComponent, List components, Random random)
	{
		if(this.startPiece.bottom)
		{
			this.componentType = 0;
			this.getNextComponentNormal(startPiece, components, random, 0, -8, 0, true);
		}
	}

	public static ComponentCastleRoomPiece createRandomRoom(List<?> par0List, ComponentCastleStartPiece startPiece, int x, int y, int z, int par5, int par6, Random random)
	{
		ComponentCastleRoomPiece piece = null;
		switch(random.nextInt(30))
		{
		case 0:
			if(!startPiece.bottom)
				piece = ComponentCastleStaircasePiece.findValidPlacement(par0List, startPiece, x, y, z, par5, par6);
			else
				piece = findValidPlacement(par0List, startPiece, x, y, z, par5, par6);
			break;
		case 1:
			piece = ComponentCastleLibraryPiece.findValidPlacement(par0List, startPiece, x, y, z, par5, par6);
			break;
		default:
			piece = findValidPlacement(par0List, startPiece, x, y, z, par5, par6);
		}
		if(piece == null)
			System.out.printf("%d %d %d %d, ", x, y, z, par6);
		return piece;
	}

	public static ComponentCastleRoomPiece findValidPlacement(List<?> par0List, ComponentCastleStartPiece startPiece, int par2, int par3, int par4, int par5, int par6)
	{
		StructureBoundingBox structureboundingbox = new StructureBoundingBox(par2 + 0, par3 + 0, par4 + 0, par2 + 8 + 0, 0 + 8 + 0, par4 + 8 + 0);
		return new ComponentCastleRoomPiece(par6, startPiece, structureboundingbox);
	}

	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) 
	{
		IBlockState chessTile = getChessBlockState(startPiece.isBlack ? 0 : 1);
		IBlockState chessTile1 = getChessBlockState(startPiece.isBlack ? 2 : 3);
		if (startPiece.averageGroundLevel < 0)
		{
			startPiece.averageGroundLevel = startPiece.getAverageGroundLevel(world);
			
			if (startPiece.averageGroundLevel < 0)
			{
				return true;
			}

//			Debug.print(startPiece.averageGroundLevel);
		}
		if(this.boundingBox.minY < startPiece.averageGroundLevel - 1)
		{
//			System.out.print(this.boundingBox.minY);
			this.boundingBox.offset(0, startPiece.averageGroundLevel - 1, 0);
		}
		this.fillWithAlternatingBlocks(world, structureboundingbox, 0, 0, 0, 7 ,0, 7, chessTile,  chessTile1, false);
		this.fillWithAlternatingBlocks(world, structureboundingbox, 0, 7, 0, 7 ,7, 7, chessTile,  chessTile1, false);
		this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 7, 6, 7, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
		this.setBlockState(world, Blocks.TORCH.getDefaultState(), 3, 1, 3, structureboundingbox);	//placeBlockAtCurrentPosition
		return true;
	}
	@Override
	protected void writeStructureToNBT(NBTTagCompound p_143012_1_) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
	{
		// TODO Auto-generated method stub
		
	}*/
}