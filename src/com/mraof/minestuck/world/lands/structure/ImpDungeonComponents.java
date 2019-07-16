package com.mraof.minestuck.world.lands.structure;

public class ImpDungeonComponents
{
	/*
	public static void registerComponents()
	{
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.EntryCorridor.class, "MinestuckIDEC");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.StraightCorridor.class, "MinestuckIDC");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.CrossCorridor.class, "MinestuckIDCC");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.TurnCorridor.class, "MinestuckIDCT");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.ReturnRoom.class, "MinestuckIDRR");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.ReturnRoomAlt.class, "MinestuckIDRRA");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.SpawnerRoom.class, "MinestuckIDSpR");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.BookcaseRoom.class, "MinestuckIDR");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.SpawnerCorridor.class, "MinestuckIDSpC");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.LargeSpawnerCorridor.class, "MinestuckIDLSpC");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.OgreCorridor.class, "MinestuckIDOgC");
	}
	
	public static class EntryCorridor extends ImpDungeonComponent
	{
		
		public EntryCorridor()
		{
			corridors = new boolean[2];
		}
		
		public EntryCorridor(EnumFacing coordBaseMode, int posX, int posZ, Random rand, List<StructureComponent> componentList)
		{
			this();
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 10 : 6;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 10 : 6;
			
			int height = 45 - rand.nextInt(8);
			int offset = getCoordBaseMode().getAxisDirection().equals(EnumFacing.AxisDirection.POSITIVE) ? 4 : -3;
			int x = posX + (getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 0 : offset);
			int z = posZ + (getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 0 : offset);
			
			this.boundingBox = new StructureBoundingBox(x, height, z, x + xWidth - 1, height + 6, z + zWidth - 1);
			
			BlockPos compoPos = new BlockPos(x + (xWidth/2 - 1), height, z + (zWidth/2 - 1));
			
			StructureContext ctxt = new StructureContext(componentList, rand);
			ctxt.compoGen[6][6] = this;
			
			int xOffset = coordBaseMode.getFrontOffsetX();
			int zOffset = coordBaseMode.getFrontOffsetZ();
			if(rand.nextBoolean())
			{
				corridors[0] = !generatePart(ctxt, 6 + xOffset, 6 + zOffset, compoPos.add(xOffset*10, 0, zOffset*10), coordBaseMode, 0);
				corridors[1] = !generatePart(ctxt, 6 - xOffset, 6 - zOffset, compoPos.add(-xOffset*10, 0, -zOffset*10), coordBaseMode.getOpposite(), 0);
			} else
			{
				corridors[1] = !generatePart(ctxt, 6 - xOffset, 6 - zOffset, compoPos.add(-xOffset*10, 0, -zOffset*10), coordBaseMode.getOpposite(), 0);
				corridors[0] = !generatePart(ctxt, 6 + xOffset, 6 + zOffset, compoPos.add(xOffset*10, 0, zOffset*10), coordBaseMode, 0);
			}
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			if(getCoordBaseMode().equals(facing))
				corridors[0] = false;
			else if(getCoordBaseMode().getOpposite().equals(facing))
				corridors[1] = false;
			return getCoordBaseMode().getAxis().equals(facing.getAxis());
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState wallDecor = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState floorDecor = provider.blockRegistry.getBlockState("structure_secondary_decorative");
			IBlockState fluid = provider.blockRegistry.getBlockState("fall_fluid");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 4, 4, 0, 6, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 4, 4, 4, 6);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 5, 3, 0, 5, fluid, fluid, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 5, 3, -1, 5, floorDecor, floorDecor, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 3, 0, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 3, 5, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 3, 4, 5, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 7, 4, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 3, 1, 3, 3, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 1, 3, 7, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 3, 4, 3, 3, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 7, 4, 3, 7, wallDecor, wallDecor, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 3, 0, 3, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 7, 3, 0, 9, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 0, 3, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 8, 3, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 1, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 0, 4, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 8, 1, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 8, 4, 4, 9, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 0, 3, 3, 3);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 7, 3, 3, 9);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 9, 3, 3, 9, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 3, 3, 0, wallBlock, wallBlock, false);
			
			return true;
		}
	}
	
	public static boolean generatePart(StructureContext ctxt, int xIndex, int zIndex, BlockPos pos, EnumFacing facing, int index)
	{
		if(xIndex >= ctxt.compoGen.length || zIndex >= ctxt.compoGen[0].length
				|| xIndex < 0 || zIndex < 0)
			return false;
		
		if(ctxt.compoGen[xIndex][zIndex] != null)
			return ctxt.compoGen[xIndex][zIndex].connectFrom(facing.getOpposite());
		
		if(ctxt.rand.nextDouble() >= (1.4 - index*0.1))
			if(ctxt.rand.nextDouble() < 1/3D)
			{
				ctxt.compoList.add(genRoom(facing, pos, xIndex, zIndex, index, ctxt));
				return true;
			} else return false;
		
		ImpDungeonComponent component;
		
		int corridors = ctxt.corridors;
		double i = ctxt.rand.nextDouble();
		if(i < 1.2 - corridors*0.12)	//Cross corridor
		{
			ctxt.corridors += 3;
			component = new CrossCorridor(facing, pos, xIndex, zIndex, index, ctxt);
		} else if(i < 0.96 - corridors*0.06)	//Any room
		{
			component = genRoom(facing, pos, xIndex, zIndex, index, ctxt);
		} else	//Straight or corner corridor
		{
			ctxt.corridors -= 1;
			if(ctxt.rand.nextBoolean())
				component = new TurnCorridor(facing, pos, xIndex, zIndex, index, ctxt);
			else
			{	//Corridor
				i = ctxt.rand.nextFloat();
				if(i < 0.2)
					component = new SpawnerCorridor(facing, pos, xIndex, zIndex, index, ctxt);
				else if (i < 0.3 && !ctxt.generatedOgreRoom)
				{
					component = new OgreCorridor(facing, pos, xIndex, zIndex, index, ctxt);
				}
				else if (i < 0.4)
				{
					component = new LargeSpawnerCorridor(facing, pos, xIndex, zIndex, index, ctxt);
				}
				else component = new StraightCorridor(facing, pos, xIndex, zIndex, index, ctxt);
			}
		}
		
		ctxt.corridors = corridors;
		ctxt.compoList.add(component);
		
		return true;
	}
	
	protected static ImpDungeonComponent genRoom(EnumFacing facing, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
	{
		float i = ctxt.rand.nextFloat();
		if(i < 0.2 || !ctxt.generatedReturn)
		{
			if(ctxt.rand.nextBoolean())
				return new ReturnRoom(facing, pos, xIndex, zIndex, index, ctxt);
			else return new ReturnRoomAlt(facing, pos, xIndex, zIndex, index, ctxt);
		}
		else if(i < 0.5)
			return new BookcaseRoom(facing, pos, xIndex, zIndex, index, ctxt);
		else return new SpawnerRoom(facing, pos, xIndex, zIndex, index, ctxt);
	}
	
	protected static class StructureContext
	{
		ImpDungeonComponent[][] compoGen = new ImpDungeonComponent[13][13];
		List<StructureComponent> compoList;
		Random rand;
		int corridors = 3;
		boolean generatedReturn = false;
		boolean generatedOgreRoom = false;
		
		public StructureContext(List<StructureComponent> compoList, Random rand)
		{
			this.compoList = compoList;
			this.rand = rand;
		}
	}
	
	public static abstract class ImpDungeonComponent extends StructureComponentUtil
	{
		protected boolean[] corridors = new boolean[0];
		
		protected abstract boolean connectFrom(EnumFacing facing);
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				tagCompound.setBoolean("bl"+i, corridors[i]);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			for(int i = 0; i < corridors.length; i++)
				corridors[i] = tagCompound.getBoolean("bl"+i);
		}
	}
	
	public static class StraightCorridor extends ImpDungeonComponent
	{
		
		boolean light;
		byte lightPos;
		
		public StraightCorridor()
		{
			corridors = new boolean[1];
		}
		
		public StraightCorridor(EnumFacing coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			this();
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 10 : 4;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 10 : 4;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			light = true;//ctxt.rand.nextFloat() < 0.1F;
			if(light)
				lightPos = (byte) ctxt.rand.nextInt(4);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			int xOffset = coordBaseMode.getFrontOffsetX();
			int zOffset = coordBaseMode.getFrontOffsetZ();
			corridors[0] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*10, 0, zOffset*10), coordBaseMode, index + 1);
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setBoolean("l", light);
			if(light)
				tagCompound.setByte("lpos", lightPos);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			super.readStructureFromNBT(tagCompound, templates);
			light = tagCompound.getBoolean("l");
			if(light)
				lightPos = tagCompound.getByte("lpos");
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			if(getCoordBaseMode().equals(facing))
				corridors[0] = false;
			return getCoordBaseMode().getAxis().equals(facing.getAxis());
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 2, 0, 9, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 0, 2, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 3, 4, 9, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 0, 2, 3, 9);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 9, 2, 3, 9, wallBlock, wallBlock, false);
			
			if(light)
			{
				IBlockState torch = provider.blockRegistry.getBlockState("torch");
				if(lightPos/2 == 0)
					setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.WEST), 2, 2, 4 + lightPos%2, structureBoundingBoxIn);
				else setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.EAST), 1, 2, 4 + lightPos%2, structureBoundingBoxIn);
			}
			
			return true;
		}
	}
	
	public static class CrossCorridor extends ImpDungeonComponent
	{
		boolean light;
		
		public CrossCorridor()
		{
			corridors = new boolean[3];
		}
		
		public CrossCorridor(EnumFacing coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			this();
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = 10;
			int zWidth = 10;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 5, z + zWidth - 1);
			
			light = ctxt.rand.nextFloat() < 0.3F;
			
			ctxt.compoGen[xIndex][zIndex] = this;
			int xOffset = coordBaseMode.getFrontOffsetX();
			int zOffset = coordBaseMode.getFrontOffsetZ();
			
			if(ctxt.rand.nextBoolean())
			{
				corridors[0] = !generatePart(ctxt, xIndex - zOffset, zIndex + xOffset, pos.add(-zOffset*10, 0, xOffset*10), coordBaseMode.rotateY(), index + 1);
				corridors[2] = !generatePart(ctxt, xIndex + zOffset, zIndex - xOffset, pos.add(zOffset*10, 0, -xOffset*10), coordBaseMode.rotateYCCW(), index + 1);
			} else
			{
				corridors[2] = !generatePart(ctxt, xIndex + zOffset, zIndex - xOffset, pos.add(zOffset*10, 0, -xOffset*10), coordBaseMode.rotateYCCW(), index + 1);
				corridors[0] = !generatePart(ctxt, xIndex - zOffset, zIndex + xOffset, pos.add(-zOffset*10, 0, xOffset*10), coordBaseMode.rotateY(), index + 1);
			}
			corridors[1] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*10, 0, zOffset*10), coordBaseMode, index + 2);
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setBoolean("l", light);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			super.readStructureFromNBT(tagCompound, templates);
			light = tagCompound.getBoolean("l");
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			if(getCoordBaseMode().rotateY().equals(facing))
				corridors[0] = false;
			else if(getCoordBaseMode().equals(facing))
				corridors[1] = false;
			else if(getCoordBaseMode().rotateYCCW().equals(facing))
				corridors[2] = false;
			return true;
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState wallDecor = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 5, 0, 9, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 4, 3, 0, 5, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 4, 9, 0, 5, floorBlock, floorBlock, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 3, 4, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 4, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 6, 3, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 6, 6, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 3, 2, 4, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 6, 2, 4, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 3, 9, 4, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 6, 9, 4, 6, wallBlock, wallBlock, false);
			
			fillWithAir(worldIn, structureBoundingBoxIn, 4, 1, 0, 5, 3, 9);
			fillWithAir(worldIn, structureBoundingBoxIn, 0, 1, 4, 3, 3, 5);
			fillWithAir(worldIn, structureBoundingBoxIn, 6, 1, 4, 9, 3, 5);
			fillWithAir(worldIn, structureBoundingBoxIn, 4, 4, 4, 5, 4, 5);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 0, 5, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 3, 5, 4, 3, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 7, 5, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 6, 5, 4, 6, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 4, 2, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 4, 3, 4, 5, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 4, 9, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 4, 6, 4, 5, wallDecor, wallDecor, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 5, 3, 6, 5, 6, wallBlock, wallBlock, false);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 4, 0, 3, 5, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 9, 5, 3, 9, wallBlock, wallBlock, false);
			if(corridors[2])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 4, 9, 3, 5, wallBlock, wallBlock, false);
			
			if(light)
			{
				IBlockState lightBlock = provider.blockRegistry.getBlockState("light_block");
				fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 4, 5, 5, 5, lightBlock, lightBlock, false);
			}
			
			return true;
		}
	}
	
	public static class TurnCorridor extends ImpDungeonComponent
	{
		boolean light;
		
		public TurnCorridor()
		{
			corridors = new boolean[2];
		}
		
		public TurnCorridor(EnumFacing coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			this();
			boolean direction = ctxt.rand.nextBoolean();
			if(direction)
				setCoordBaseMode(coordBaseMode.rotateY());
			else setCoordBaseMode(coordBaseMode);
			
			int xWidth = 7;
			int zWidth = 7;
			
			int i = coordBaseMode.getAxisDirection().getOffset() + 1;
			int j = direction^(coordBaseMode.getAxis() == EnumFacing.Axis.Z)^(coordBaseMode.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)?2:0;
			int x = pos.getX() - 4 + (getCoordBaseMode() == EnumFacing.NORTH || getCoordBaseMode() == EnumFacing.WEST ? 3 : 0);
			int z = pos.getZ() - 4 + (getCoordBaseMode() == EnumFacing.NORTH || getCoordBaseMode() == EnumFacing.EAST ? 3 : 0);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			light = ctxt.rand.nextFloat() < 0.2F;
			
			ctxt.compoGen[xIndex][zIndex] = this;
			EnumFacing newFacing = direction ? coordBaseMode.rotateYCCW() : coordBaseMode.rotateY();
			int xOffset = newFacing.getFrontOffsetX();
			int zOffset = newFacing.getFrontOffsetZ();
			corridors[direction ? 0 : 1] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*10, 0, zOffset*10), newFacing, index + 1);
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setBoolean("l", light);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			super.readStructureFromNBT(tagCompound, templates);
			light = tagCompound.getBoolean("l");
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			if(getCoordBaseMode().rotateY().equals(facing))
				corridors[1] = false;
			else if(getCoordBaseMode().getOpposite().equals(facing))
				corridors[0] = false;
			else return false;
			return true;
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 5, 0, 5, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 4, 3, 0, 5, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 4, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 3, 4, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 3, 2, 4, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 6, 5, 4, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 0, 5, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 4, 3, 4, 5, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 4, 1, 0, 5, 3, 5);
			fillWithAir(worldIn, structureBoundingBoxIn, 0, 1, 4, 3, 3, 5);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 0, 5, 3, 0, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 4, 0, 3, 5, wallBlock, wallBlock, false);
			
			if(light)
			{
				IBlockState torch = provider.blockRegistry.getBlockState("torch");
				setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.WEST), 5, 2, 3, structureBoundingBoxIn);
				setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.NORTH), 3, 2, 5, structureBoundingBoxIn);
			}
			
			return true;
		}
	}
	
	public static class ReturnRoom extends ImpDungeonComponent
	{
		public ReturnRoom()
		{}
		
		public ReturnRoom(EnumFacing coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 8 : 6;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 8 : 6;
			
			int x = pos.getX() - (xWidth/2 - 1) - coordBaseMode.getFrontOffsetX();
			int z = pos.getZ() - (zWidth/2 - 1) - coordBaseMode.getFrontOffsetZ();
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 10, z + zWidth - 1);
			
			ctxt.generatedReturn = true;
			ctxt.compoGen[xIndex][zIndex] = this;
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			return getCoordBaseMode().getOpposite().equals(facing);
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState wallDecor = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState floorDecor = provider.blockRegistry.getBlockState("structure_secondary_decorative");
			IBlockState light = provider.blockRegistry.getBlockState("light_block");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 3, 0, 2, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 3, 4, 0, 6, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 4, 3, 0, 5, floorDecor, floorDecor, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 0, 3, 3, 2);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 3, 4, 4, 6);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 5, 4, 3, 9, 5);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 1, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 4, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 0, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 2, 5, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 4, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 0, 3, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 2, 4, 5, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 4, 0, 3, 5, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 4, 5, 3, 5, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 3, 7, 3, 3, 7, wallDecor, wallDecor, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 3, 4, 10, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 6, 4, 10, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 4, 1, 10, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 4, 4, 10, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 10, 4, 3, 10, 5, light, light, false);
			
			placeReturnNode(worldIn, structureBoundingBoxIn);
			
			return true;
		}
		
		private void placeReturnNode(World world, StructureBoundingBox boundingBox)
		{
			int x = getXWithOffset(2, 4), y = getYWithOffset(1), z = getZWithOffset(2, 4);
			
			if(getCoordBaseMode() == EnumFacing.NORTH || getCoordBaseMode() == EnumFacing.WEST)
				x--;
			if(getCoordBaseMode() == EnumFacing.NORTH || getCoordBaseMode() == EnumFacing.EAST)
				z--;
			BlockPos nodePos = new BlockPos(x, y, z);
			
			for(int i = 0; i < 4; i++)
			{
				BlockPos pos = nodePos.add(i % 2, 0, i/2);
				if(boundingBox.isVecInside(pos))
				{
					if(i == 3)
						world.setBlockState(pos, MinestuckBlocks.returnNode.getDefaultState().cycleProperty(BlockGate.isMainComponent), 2);
					else world.setBlockState(pos, MinestuckBlocks.returnNode.getDefaultState(), 2);
				}
			}
		}
	}
	
	public static class ReturnRoomAlt extends ImpDungeonComponent
	{
		public ReturnRoomAlt()
		{}
		
		public ReturnRoomAlt(EnumFacing coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = coordBaseMode.getAxis().equals(EnumFacing.Axis.X) ? 10 : 8;
			int zWidth = coordBaseMode.getAxis().equals(EnumFacing.Axis.Z) ? 10 : 8;
			
			int x = pos.getX() - (xWidth/2 - 1);	//This method works for symmetrical and centered componenets
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 10, z + zWidth - 1);
			
			ctxt.generatedReturn = true;
			ctxt.compoGen[xIndex][zIndex] = this;
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			return getCoordBaseMode().getOpposite().equals(facing);
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState floorStairsFront = provider.blockRegistry.getStairs("structure_secondary_stairs", EnumFacing.SOUTH, false);
			IBlockState floorStairsBack = provider.blockRegistry.getStairs("structure_secondary_stairs", EnumFacing.NORTH, false);
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState floorDecor = provider.blockRegistry.getBlockState("structure_secondary_decorative");
			IBlockState light = provider.blockRegistry.getBlockState("light_block");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 4, 0, 1, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 7, 0, 2, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -1, 4, 1, -1, 5, floorDecor, floorDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 4, 5, -1, 5, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, -1, 4, 6, -1, 5, floorDecor, floorDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 6, 0, 8, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 3, 1);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 2, 6, 4, 3);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 0, 3, 6, 5, 5);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 6, 6, 5, 8);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 3, 6, 0, 3, floorStairsBack, floorStairsBack, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 6, 6, 0, 6, floorStairsFront, floorStairsFront, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 2, 3, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 0, 5, 3, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 1, 3, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 1, 7, 3, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 1, 7, 5, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 0, 5, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 2, 7, 5, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 9, 7, 5, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 0, 4, 4, 0, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 2, 6, 5, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 4, 6, 6, 8, wallBlock, wallBlock, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 5, 1, 6, 6, light, light, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 6, 5, 6, 6, 6, light, light, false);
			
			placeReturnNode(worldIn, structureBoundingBoxIn);
			
			return true;
		}
		
		private void placeReturnNode(World world, StructureBoundingBox boundingBox)
		{
			int x = getXWithOffset(3, 7), y = getYWithOffset(1), z = getZWithOffset(3, 7);
			
			if(getCoordBaseMode() == EnumFacing.NORTH || getCoordBaseMode() == EnumFacing.WEST)
				x--;
			if(getCoordBaseMode() == EnumFacing.NORTH || getCoordBaseMode() == EnumFacing.EAST)
				z--;
			BlockPos nodePos = new BlockPos(x, y, z);
			
			for(int i = 0; i < 4; i++)
			{
				BlockPos pos = nodePos.add(i % 2, 0, i/2);
				if(boundingBox.isVecInside(pos))
				{
					if(i == 3)
						world.setBlockState(pos, MinestuckBlocks.returnNode.getDefaultState().cycleProperty(BlockGate.isMainComponent), 2);
					else world.setBlockState(pos, MinestuckBlocks.returnNode.getDefaultState(), 2);
				}
			}
		}
	}
	public static class SpawnerRoom extends ImpDungeonComponent
	{
		private boolean spawner1, spawner2;
		
		public SpawnerRoom()
		{}
		
		public SpawnerRoom(EnumFacing coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 7 : 8;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 7 : 8;
			
			int x = pos.getX() - (getCoordBaseMode().equals(EnumFacing.WEST)?2:3) - coordBaseMode.getFrontOffsetX();
			int z = pos.getZ() - (getCoordBaseMode().equals(EnumFacing.NORTH)?2:3) - coordBaseMode.getFrontOffsetZ();
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			
			if(ctxt.rand.nextBoolean())
			{
				spawner1 = true;
				spawner2 = true;
			} else
			{
				spawner1 = ctxt.rand.nextBoolean();
				spawner2 = !spawner1;
			}
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setBoolean("sp1", spawner1);
			tagCompound.setBoolean("sp2", spawner2);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			super.readStructureFromNBT(tagCompound, templates);
			spawner1 = tagCompound.getBoolean("sp1");
			spawner2 = tagCompound.getBoolean("sp2");
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			return getCoordBaseMode().getOpposite().equals(facing);
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState wallDecor = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState floorDecor = provider.blockRegistry.getBlockState("structure_secondary_decorative");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 4, 0, 2, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 3, 6, 0, 5, floorBlock, floorBlock, false);
			setBlockState(worldIn, floorBlock, 1, 0, 2, structureBoundingBoxIn);
			setBlockState(worldIn, floorBlock, 6, 0, 2, structureBoundingBoxIn);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 5, 4, 0, 5, floorDecor, floorDecor, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 3, 2);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 3, 6, 3, 5);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 2, 1, 3, 2);
			fillWithAir(worldIn, structureBoundingBoxIn, 6, 1, 2, 6, 3, 2);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 2, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 0, 5, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 2, 2, 4, 2, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 2, 5, 4, 2, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 1, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 1, 6, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 1, 0, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 1, 7, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 6, 7, 4, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 0, 4, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 3, 6, 4, 5, wallBlock, wallBlock, false);
			setBlockState(worldIn, wallBlock, 1, 4, 2, structureBoundingBoxIn);
			setBlockState(worldIn, wallBlock, 6, 4, 2, structureBoundingBoxIn);
			
			if(spawner1)
			{
				BlockPos spawnerPos = new BlockPos(this.getXWithOffset(1, 2), this.getYWithOffset(1), this.getZWithOffset(1, 2));
				spawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, worldIn, structureBoundingBoxIn, "minestuck:imp");
			}
			if(spawner2)
			{
				BlockPos spawnerPos = new BlockPos(this.getXWithOffset(6, 2), this.getYWithOffset(1), this.getZWithOffset(6, 2));
				spawner2 = !StructureBlockUtil.placeSpawner(spawnerPos, worldIn, structureBoundingBoxIn, "minestuck:imp");
			}
			
			BlockPos chestPos = new BlockPos(this.getXWithOffset(3, 5), this.getYWithOffset(1), this.getZWithOffset(3, 5));
			StructureBlockUtil.placeLootChest(chestPos, worldIn, structureBoundingBoxIn, getCoordBaseMode().getOpposite(), MinestuckLoot.BASIC_MEDIUM_CHEST, randomIn);
			chestPos = new BlockPos(this.getXWithOffset(4, 5), this.getYWithOffset(1), this.getZWithOffset(4, 5));
			StructureBlockUtil.placeLootChest(chestPos, worldIn, structureBoundingBoxIn, getCoordBaseMode().getOpposite(), MinestuckLoot.BASIC_MEDIUM_CHEST, randomIn);
			
			return true;
		}
	}
	
	public static class BookcaseRoom extends ImpDungeonComponent
	{
		float bookChance;
		boolean light;
		
		public BookcaseRoom()
		{}
		
		public BookcaseRoom(EnumFacing coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = 8;
			int zWidth = 8;
			
			int x = pos.getX() - (xWidth/2 - 1) - coordBaseMode.getFrontOffsetX();
			int z = pos.getZ() - (zWidth/2 - 1) - coordBaseMode.getFrontOffsetZ();
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			
			light = ctxt.rand.nextFloat() < 0.4F;
			bookChance = ctxt.rand.nextFloat() - 0.5F;
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setFloat("b", bookChance);
			tagCompound.setBoolean("l", light);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			super.readStructureFromNBT(tagCompound, templates);
			bookChance = tagCompound.getFloat("b");
			light = tagCompound.getBoolean("l");
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			return getCoordBaseMode().getOpposite().equals(facing);
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState wallDecor = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState floorDecor = provider.blockRegistry.getBlockState("structure_secondary_decorative");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 4, 0, 0, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 6, 0, 1, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 6, 6, 0, 6, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 1, 0, 5, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 2, 6, 0, 5, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 3, 4, 0, 4, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 2, 5, 0, 2, floorDecor, floorDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 5, 5, 0, 5, floorDecor, floorDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 3, 2, 0, 4, floorDecor, floorDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 3, 5, 0, 4, floorDecor, floorDecor, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 3, 0);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 1, 6, 4, 6);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 5, 2, 5, 5, 5);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 2, 5, 0, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 0, 7, 5, 0, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 1, 0, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 1, 7, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 6, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 0, 4, 5, 0, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 1, 1, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 5, 1, 6, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 1, 5, 5, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 6, 5, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 1, 6, 6, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 6, 3, 4, 6, 4, wallDecor, wallDecor, false);
			
			IBlockState bookshelf = Blocks.BOOKSHELF.getDefaultState();
			
			generateMaybeBox(worldIn, structureBoundingBoxIn, randomIn, bookChance, 1, 1, 1, 1, 4, 2, bookshelf, bookshelf, false, 0);
			generateMaybeBox(worldIn, structureBoundingBoxIn, randomIn, bookChance, 1, 1, 5, 1, 4, 6, bookshelf, bookshelf, false, 0);
			generateMaybeBox(worldIn, structureBoundingBoxIn, randomIn, bookChance, 6, 1, 1, 6, 4, 2, bookshelf, bookshelf, false, 0);
			generateMaybeBox(worldIn, structureBoundingBoxIn, randomIn, bookChance, 6, 1, 5, 6, 4, 6, bookshelf, bookshelf, false, 0);
			generateMaybeBox(worldIn, structureBoundingBoxIn, randomIn, bookChance, 3, 1, 6, 4, 4, 6, bookshelf, bookshelf, false, 0);
			
			if(light)
			{
				IBlockState torch = provider.blockRegistry.getBlockState("torch");
				if(randomIn.nextBoolean())
					setBlockState(worldIn, torch, 2, 1, 2, structureBoundingBoxIn);
				if(randomIn.nextBoolean())
					setBlockState(worldIn, torch, 5, 1, 2, structureBoundingBoxIn);
				if(randomIn.nextBoolean())
					setBlockState(worldIn, torch, 2, 1, 5, structureBoundingBoxIn);
				if(randomIn.nextBoolean())
					setBlockState(worldIn, torch, 5, 1, 5, structureBoundingBoxIn);
			}
			
			return true;
		}
	}
	
	public static class SpawnerCorridor extends ImpDungeonComponent
	{
		private boolean spawner1, spawner2, chestPos;
		
		public SpawnerCorridor()
		{
			corridors = new boolean[2];
		}
		
		public SpawnerCorridor(EnumFacing coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			this();
			boolean mirror = ctxt.rand.nextBoolean();
			if(mirror)
				setCoordBaseMode(coordBaseMode.getOpposite());
			else setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 10 : 6;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 10 : 6;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			
			if(ctxt.rand.nextBoolean())
			{
				spawner1 = true;
				spawner2 = true;
			} else
			{
				spawner1 = ctxt.rand.nextBoolean();
				spawner2 = !spawner1;
			}
			chestPos = ctxt.rand.nextBoolean();
			
			int xOffset = coordBaseMode.getFrontOffsetX();
			int zOffset = coordBaseMode.getFrontOffsetZ();
			corridors[mirror ? 0 : 1] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*10, 0, zOffset*10), coordBaseMode, index + 1);
			
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setBoolean("sp1", spawner1);
			tagCompound.setBoolean("sp2", spawner2);
			tagCompound.setBoolean("ch", chestPos);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			super.readStructureFromNBT(tagCompound, templates);
			spawner1 = tagCompound.getBoolean("sp1");
			spawner2 = tagCompound.getBoolean("sp2");
			chestPos = tagCompound.getBoolean("ch");
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			if(getCoordBaseMode().getAxis().equals(facing.getAxis()))
			{
				corridors[getCoordBaseMode().equals(facing)?1:0] = false;
				return true;
			}
			return false;
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState wallDecor = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 3, 0, 9, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 3, 1, 0, 6, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 3, 4, 0, 6, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 0, 3, 3, 9);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 3, 1, 3, 6);
			fillWithAir(worldIn, structureBoundingBoxIn, 4, 1, 3, 4, 3, 6);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 1, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 4, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 0, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 2, 5, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 1, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 7, 4, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 0, 3, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 3, 1, 4, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 3, 4, 4, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 0, 2, 5, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 4, 5, 2, 5, wallDecor, wallDecor, false);
			
			if(spawner1)
			{
				BlockPos spawnerPos = new BlockPos(this.getXWithOffset(1, 4), this.getYWithOffset(1), this.getZWithOffset(1, 4));
				spawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, worldIn, structureBoundingBoxIn, "minestuck:imp");
			}
			if(spawner2)
			{
				BlockPos spawnerPos = new BlockPos(this.getXWithOffset(1, 5), this.getYWithOffset(1), this.getZWithOffset(1, 5));
				spawner2 = !StructureBlockUtil.placeSpawner(spawnerPos, worldIn, structureBoundingBoxIn, "minestuck:imp");
			}
			
			int z = chestPos ? 4 : 5;
			BlockPos chestPos = new BlockPos(this.getXWithOffset(4, z), this.getYWithOffset(1), this.getZWithOffset(4, z));
			StructureBlockUtil.placeLootChest(chestPos, worldIn, structureBoundingBoxIn, getCoordBaseMode().rotateY(), MinestuckLoot.BASIC_MEDIUM_CHEST, randomIn);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 3, 3, 0, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 9, 3, 3, 9, wallBlock, wallBlock, false);
			
			return true;
		}
	}
	
	public static class OgreCorridor extends ImpDungeonComponent
	{
		private boolean chestPos;
		private boolean ogreSpawned;
		
		public OgreCorridor()
		{
			corridors = new boolean[1];
		}
		
		public OgreCorridor(EnumFacing coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			this();
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 10 : 8;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 10 : 8;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			ctxt.generatedOgreRoom = true;
			
			chestPos = ctxt.rand.nextBoolean();
			
			int xOffset = coordBaseMode.getFrontOffsetX();
			int zOffset = coordBaseMode.getFrontOffsetZ();
			
			corridors[0] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*10, 0, zOffset*10), coordBaseMode, index + 1);
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setBoolean("ch", chestPos);
			tagCompound.setBoolean("spwn", ogreSpawned);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			super.readStructureFromNBT(tagCompound, templates);
			chestPos = tagCompound.getBoolean("ch");
			ogreSpawned = tagCompound.getBoolean("spwn");
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			if(getCoordBaseMode().equals(facing.getOpposite()))
			{
				corridors[0] = false;
				return true;
			}
			return false;
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState wallDecor = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState floorDecor = provider.blockRegistry.getBlockState("structure_secondary_decorative");
			IBlockState light = provider.blockRegistry.getBlockState("light_block");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 4, 0, 9, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 2, 2, 0, 7, floorDecor, floorDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 2, 5, 0, 7, floorDecor, floorDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 1, 0, 7, light, light, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 2, 6, 0, 7, light, light, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 2, 6, 5, 7);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 6, 3, 5, 6, 6);
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 3, 2);
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 3, 9);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 3, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 2, 3, 0, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 1, 6, 3, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 0, 5, 3, 0, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 1, 0, 5, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 1, 7, 5, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 8, 2, 3, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 9, 2, 3, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 8, 6, 3, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 9, 5, 3, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 6, 6, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 0, 5, 4, 0, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 8, 6, 6, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 9, 5, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 2, 6, 6, 2, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 3, 1, 6, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 7, 6, 6, 7, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 6, 3, 6, 6, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 7, 3, 5, 7, 6, wallBlock, wallBlock, false);
			
			
			int x = chestPos ? 1 : 6;
			BlockPos chestPos = new BlockPos(this.getXWithOffset(x, 4), this.getYWithOffset(1), this.getZWithOffset(x, 4));
			StructureBlockUtil.placeLootChest(chestPos, worldIn, structureBoundingBoxIn, getCoordBaseMode().rotateYCCW(), MinestuckLoot.BASIC_MEDIUM_CHEST, randomIn);
			chestPos = new BlockPos(this.getXWithOffset(x, 5), this.getYWithOffset(1), this.getZWithOffset(x, 5));
			StructureBlockUtil.placeLootChest(chestPos, worldIn, structureBoundingBoxIn, getCoordBaseMode().rotateYCCW(), MinestuckLoot.BASIC_MEDIUM_CHEST, randomIn);
			
			if(!ogreSpawned && structureBoundingBoxIn.isVecInside(new BlockPos(getXWithOffset(3, 4), getYWithOffset(1), getZWithOffset(3, 4))))
			spawnOgre(3, 1, 4, worldIn, randomIn);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 9, 4, 3, 9, wallBlock, wallBlock, false);
			
			return true;
		}
		
		private void spawnOgre(int xPos, int yPos, int zPos, World worldIn, Random rand)
		{
			BlockPos pos = new BlockPos(getXWithOffset(xPos, zPos), getYWithOffset(yPos), getZWithOffset(xPos, zPos));
			EntityOgre ogre = new EntityOgre(worldIn);
			ogre.setPositionAndRotation(pos.getX(), pos.getY(), pos.getZ(), rand.nextFloat()*360F, 0);
			ogre.onInitialSpawn(null, null);
			ogre.setHomePosAndDistance(pos, 2);
			worldIn.spawnEntity(ogre);
		}
	}
	
	public static class LargeSpawnerCorridor extends ImpDungeonComponent
	{
		private boolean spawner1, spawner2, chestPos;
		
		public LargeSpawnerCorridor()
		{
			corridors = new boolean[2];
		}
		
		public LargeSpawnerCorridor(EnumFacing coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			this();
			boolean mirror = ctxt.rand.nextBoolean();
			if(mirror)
				setCoordBaseMode(coordBaseMode.getOpposite());
			else setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 10 : 10;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 10 : 10;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			
			if(ctxt.rand.nextBoolean())
			{
				spawner1 = true;
				spawner2 = true;
			} else
			{
				spawner1 = ctxt.rand.nextBoolean();
				spawner2 = !spawner1;
			}
			chestPos = ctxt.rand.nextBoolean();
			
			int xOffset = coordBaseMode.getFrontOffsetX();
			int zOffset = coordBaseMode.getFrontOffsetZ();
			corridors[mirror ? 0 : 1] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*10, 0, zOffset*10), coordBaseMode, index + 1);
			
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setBoolean("sp1", spawner1);
			tagCompound.setBoolean("sp2", spawner2);
			tagCompound.setBoolean("ch", chestPos);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			super.readStructureFromNBT(tagCompound, templates);
			spawner1 = tagCompound.getBoolean("sp1");
			spawner2 = tagCompound.getBoolean("sp2");
			chestPos = tagCompound.getBoolean("ch");
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			if(getCoordBaseMode().getAxis().equals(facing.getAxis()))
			{
				corridors[getCoordBaseMode().equals(facing)?1:0] = false;
				return true;
			}
			return false;
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState wallDecor = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 5, 0, 9, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 3, 0, 7, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 2, 8, 0, 7, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 4, 1, 0, 5, 3, 9);
			fillWithAir(worldIn, structureBoundingBoxIn, 0, 1, 2, 3, 3, 7);
			fillWithAir(worldIn, structureBoundingBoxIn, 6, 1, 2, 9, 3, 7);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 3, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 2, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 1, 8, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 7, 8, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 1, 0, 4, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 1, 9, 4, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 7, 3, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 7, 6, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 0, 5, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 3, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 2, 8, 4, 7, wallBlock, wallBlock, false);
			
			if(spawner1)
			{
				BlockPos spawnerPos = new BlockPos(this.getXWithOffset(1, 3), this.getYWithOffset(1), this.getZWithOffset(1, 3));
				spawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, worldIn, structureBoundingBoxIn, "minestuck:imp");
			}
			if(spawner2)
			{
				BlockPos spawnerPos = new BlockPos(this.getXWithOffset(8, 5), this.getYWithOffset(1), this.getZWithOffset(8, 5));
				spawner2 = !StructureBlockUtil.placeSpawner(spawnerPos, worldIn, structureBoundingBoxIn, "minestuck:imp");
			}
			
			BlockPos chestPos = new BlockPos(this.getXWithOffset(4, 4), this.getYWithOffset(1), this.getZWithOffset(4, 4));
			StructureBlockUtil.placeLootChest(chestPos, worldIn, structureBoundingBoxIn, getCoordBaseMode().getOpposite(), MinestuckLoot.BASIC_MEDIUM_CHEST, randomIn);
			chestPos = new BlockPos(this.getXWithOffset(5, 4), this.getYWithOffset(1), this.getZWithOffset(5, 4));
			StructureBlockUtil.placeLootChest(chestPos, worldIn, structureBoundingBoxIn, getCoordBaseMode().getOpposite(), MinestuckLoot.BASIC_MEDIUM_CHEST, randomIn);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 5, 3, 0, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 9, 5, 3, 9, wallBlock, wallBlock, false);
			
			return true;
		}	
	}*/
}