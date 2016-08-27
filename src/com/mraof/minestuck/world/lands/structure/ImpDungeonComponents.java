package com.mraof.minestuck.world.lands.structure;

import java.util.List;
import java.util.Random;

import com.mraof.minestuck.block.BlockGate;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.StructureUtil;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ImpDungeonComponents
{
	
	public static void registerComponents()
	{
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.EntryCorridor.class, "MinestuckIDEntryCorridor");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.StraightCorridor.class, "MinestuckIDCorridor");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.CrossCorridor.class, "MinestuckIDCross");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.TurnCorridor.class, "MinestuckIDTurn");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.ReturnRoom.class, "MinestuckIDReturn");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.SpawnerRoom.class, "MinestuckIDMonstSpawn");
		MapGenStructureIO.registerStructureComponent(ImpDungeonComponents.BookcaseRoom.class, "MinestuckIDRoom");
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
			
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 8 : 6;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 8 : 6;
			
			int height = 45 - rand.nextInt(8);
			int offset = getCoordBaseMode().getAxisDirection().equals(EnumFacing.AxisDirection.POSITIVE) ? 5 : -2;
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
				corridors[0] = !generatePart(ctxt, 6 + xOffset, 6 + zOffset, compoPos.add(xOffset*8, 0, zOffset*8), coordBaseMode, 0);
				corridors[1] = !generatePart(ctxt, 6 - xOffset, 6 - zOffset, compoPos.add(-xOffset*8, 0, -zOffset*8), coordBaseMode.getOpposite(), 0);
			} else
			{
				corridors[1] = !generatePart(ctxt, 6 - xOffset, 6 - zOffset, compoPos.add(-xOffset*8, 0, -zOffset*8), coordBaseMode.getOpposite(), 0);
				corridors[0] = !generatePart(ctxt, 6 + xOffset, 6 + zOffset, compoPos.add(xOffset*8, 0, zOffset*8), coordBaseMode, 0);
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
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 3, 4, 0, 5, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 3, 4, 4, 5);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 4, 3, 0, 4, fluid, fluid, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 4, 3, -1, 4, floorDecor, floorDecor, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 0, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 2, 5, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 4, 5, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 6, 4, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 1, 3, 2, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 6, 1, 3, 6, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 2, 4, 3, 2, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 6, 4, 3, 6, wallDecor, wallDecor, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 3, 0, 2, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 6, 3, 0, 7, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 0, 3, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 7, 3, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 1, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 0, 4, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 1, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 7, 4, 4, 7, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 0, 3, 3, 2);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 6, 3, 3, 7);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 7, 3, 3, 7, wallBlock, wallBlock, false);
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
			else component = new StraightCorridor(facing, pos, xIndex, zIndex, index, ctxt);
		}
		
		ctxt.corridors = corridors;
		ctxt.compoList.add(component);
		
		return true;
	}
	
	protected static ImpDungeonComponent genRoom(EnumFacing facing, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
	{
		if(ctxt.rand.nextFloat() < 0.2 || !ctxt.generatedReturn)
			return new ReturnRoom(facing, pos, xIndex, zIndex, index, ctxt);
		else if(ctxt.rand.nextFloat() < 0.5)
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
		
		public StructureContext(List<StructureComponent> compoList, Random rand)
		{
			this.compoList = compoList;
			this.rand = rand;
		}
	}
	
	public static abstract class ImpDungeonComponent extends StructureComponent
	{
		protected boolean[] corridors = new boolean[0];
		
		protected abstract boolean connectFrom(EnumFacing facing);
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				tagCompound.setBoolean("blocked"+i, corridors[i]);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				corridors[i] = tagCompound.getBoolean("blocked"+i);
		}
		
		@Override
		protected int getXWithOffset(int x, int z)
		{
			EnumFacing enumfacing = this.getCoordBaseMode();
			
			if (enumfacing == null)
				return x;
			else switch (enumfacing)
			{
			case NORTH:
				return this.boundingBox.maxX - x;
			case SOUTH:
				return this.boundingBox.minX + x;
			case WEST:
				return this.boundingBox.maxX - z;
			case EAST:
				return this.boundingBox.minX + z;
			default:
				return x;
			}
		}
		
		@Override
		protected int getZWithOffset(int x, int z)
		{
			EnumFacing enumfacing = this.getCoordBaseMode();
			
			if (enumfacing == null)
				return z;
			else switch (enumfacing)
			{
			case NORTH:
				return this.boundingBox.maxZ - z;
			case SOUTH:
				return this.boundingBox.minZ + z;
			case WEST:
				return this.boundingBox.minZ + x;
			case EAST:
				return this.boundingBox.maxZ - x;
			default:
				return z;
			}
		}
		
		@Override
		protected void setBlockState(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
		{
			BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
			
			if (boundingboxIn.isVecInside(blockpos))
			{
				EnumFacing facing = this.getCoordBaseMode();
				switch(facing)
				{
				case NORTH:
					blockstateIn = blockstateIn.withRotation(Rotation.CLOCKWISE_180);
					break;
				case WEST:
					blockstateIn = blockstateIn.withRotation(Rotation.CLOCKWISE_90);
					break;
				case EAST:
					blockstateIn = blockstateIn.withRotation(Rotation.COUNTERCLOCKWISE_90);
					break;
				default:
				}
				
				worldIn.setBlockState(blockpos, blockstateIn, 2);
			}
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
			
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 8 : 4;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 8 : 4;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			light = true;//ctxt.rand.nextFloat() < 0.1F;
			if(light)
				lightPos = (byte) ctxt.rand.nextInt(4);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			int xOffset = coordBaseMode.getFrontOffsetX();
			int zOffset = coordBaseMode.getFrontOffsetZ();
			corridors[0] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*8, 0, zOffset*8), coordBaseMode, index + 1);
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setBoolean("light", light);
			if(light)
				tagCompound.setByte("lpos", lightPos);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			super.readStructureFromNBT(tagCompound);
			light = tagCompound.getBoolean("light");
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
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 2, 0, 7, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 0, 2, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 3, 4, 7, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 0, 2, 3, 7);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 2, 3, 7, wallBlock, wallBlock, false);
			
			if(light)
			{
				IBlockState torch = provider.blockRegistry.getBlockState("torch");
				if(lightPos/2 == 0)
					setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.WEST), 2, 2, 3 + lightPos%2, structureBoundingBoxIn);
				else setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.EAST), 1, 2, 3 + lightPos%2, structureBoundingBoxIn);
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
			
			int xWidth = 8;
			int zWidth = 8;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 5, z + zWidth - 1);
			
			light = ctxt.rand.nextFloat() < 0.3F;
			
			ctxt.compoGen[xIndex][zIndex] = this;
			int xOffset = coordBaseMode.getFrontOffsetX();
			int zOffset = coordBaseMode.getFrontOffsetZ();
			
			if(ctxt.rand.nextBoolean())
			{
				corridors[0] = !generatePart(ctxt, xIndex - zOffset, zIndex + xOffset, pos.add(-zOffset*8, 0, xOffset*8), coordBaseMode.rotateY(), index + 1);
				corridors[2] = !generatePart(ctxt, xIndex + zOffset, zIndex - xOffset, pos.add(zOffset*8, 0, -xOffset*8), coordBaseMode.rotateYCCW(), index + 1);
			} else
			{
				corridors[2] = !generatePart(ctxt, xIndex + zOffset, zIndex - xOffset, pos.add(zOffset*8, 0, -xOffset*8), coordBaseMode.rotateYCCW(), index + 1);
				corridors[0] = !generatePart(ctxt, xIndex - zOffset, zIndex + xOffset, pos.add(-zOffset*8, 0, xOffset*8), coordBaseMode.rotateY(), index + 1);
			}
			corridors[1] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*8, 0, zOffset*8), coordBaseMode, index + 2);
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setBoolean("light", light);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			super.readStructureFromNBT(tagCompound);
			light = tagCompound.getBoolean("light");
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
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 4, 0, 7, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 3, 2, 0, 4, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 3, 7, 0, 4, floorBlock, floorBlock, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 2, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 0, 5, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 5, 2, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 5, 5, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 1, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 5, 1, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 2, 7, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 5, 7, 4, 5, wallBlock, wallBlock, false);
			
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 3, 7);
			fillWithAir(worldIn, structureBoundingBoxIn, 0, 1, 3, 2, 3, 4);
			fillWithAir(worldIn, structureBoundingBoxIn, 5, 1, 3, 7, 3, 4);
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 4, 3, 4, 4, 4);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 0, 4, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 2, 4, 4, 2, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 6, 4, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 5, 4, 4, 5, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 3, 1, 4, 4, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 3, 2, 4, 4, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 3, 7, 4, 4, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 3, 5, 4, 4, wallDecor, wallDecor, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 2, 5, 5, 5, wallBlock, wallBlock, false);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 3, 4, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 3, 7, wallBlock, wallBlock, false);
			if(corridors[2])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 3, 4, wallBlock, wallBlock, false);
			
			if(light)
			{
				IBlockState lightBlock = provider.blockRegistry.getBlockState("light_block");
				fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 5, 3, 4, 5, 4, lightBlock, lightBlock, false);
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
			
			int xWidth = 6;
			int zWidth = 6;
			
			int i = coordBaseMode.getAxisDirection().getOffset() + 1;
			int j = direction^(coordBaseMode.getAxis() == EnumFacing.Axis.Z)^(coordBaseMode.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)?2:0;
			int x = pos.getX() - 3 + (getCoordBaseMode() == EnumFacing.NORTH || getCoordBaseMode() == EnumFacing.WEST ? 2 : 0);
			int z = pos.getZ() - 3 + (getCoordBaseMode() == EnumFacing.NORTH || getCoordBaseMode() == EnumFacing.EAST ? 2 : 0);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			light = ctxt.rand.nextFloat() < 0.2F;
			
			ctxt.compoGen[xIndex][zIndex] = this;
			EnumFacing newFacing = direction ? coordBaseMode.rotateYCCW() : coordBaseMode.rotateY();
			int xOffset = newFacing.getFrontOffsetX();
			int zOffset = newFacing.getFrontOffsetZ();
			corridors[direction ? 0 : 1] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*8, 0, zOffset*8), newFacing, index + 1);
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setBoolean("light", light);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			super.readStructureFromNBT(tagCompound);
			light = tagCompound.getBoolean("light");
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
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 4, 0, 4, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 3, 2, 0, 4, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 0, 5, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 2, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 1, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 5, 4, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 0, 4, 4, 4, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 3, 2, 4, 4, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 3, 4);
			fillWithAir(worldIn, structureBoundingBoxIn, 0, 1, 3, 2, 3, 4);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 3, 0, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 3, 4, wallBlock, wallBlock, false);
			
			if(light)
			{
				IBlockState torch = provider.blockRegistry.getBlockState("torch");
				setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.WEST), 4, 2, 2, structureBoundingBoxIn);
				setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.NORTH), 2, 2, 4, structureBoundingBoxIn);
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
			
			int x = pos.getX() - (xWidth/2 - 1);
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
	
	public static class SpawnerRoom extends ImpDungeonComponent
	{
		private boolean spawner1, spawner2, chestPos;
		
		public SpawnerRoom()
		{}
		
		public SpawnerRoom(EnumFacing coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 7 : 8;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 7 : 8;
			
			int x = pos.getX() - (getCoordBaseMode().equals(EnumFacing.WEST)?2:3);
			int z = pos.getZ() - (getCoordBaseMode().equals(EnumFacing.NORTH)?2:3);
			
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
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			super.readStructureFromNBT(tagCompound);
			spawner1 = tagCompound.getBoolean("sp1");
			spawner2 = tagCompound.getBoolean("sp2");
			chestPos = tagCompound.getBoolean("ch");
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
				spawner1 = !StructureUtil.placeSpawner(spawnerPos, worldIn, structureBoundingBoxIn, "minestuck.Imp");
			}
			if(spawner2)
			{
				BlockPos spawnerPos = new BlockPos(this.getXWithOffset(6, 2), this.getYWithOffset(1), this.getZWithOffset(6, 2));
				spawner2 = !StructureUtil.placeSpawner(spawnerPos, worldIn, structureBoundingBoxIn, "minestuck.Imp");
			}
			
			int x = chestPos ? 3 : 4;
			BlockPos chestPos = new BlockPos(this.getXWithOffset(x, 5), this.getYWithOffset(1), this.getZWithOffset(x, 5));
			StructureUtil.placeLootChest(chestPos, worldIn, structureBoundingBoxIn, getCoordBaseMode().getOpposite(), AlchemyRecipeHandler.BASIC_MEDIUM_CHEST, randomIn);
			
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
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			
			light = ctxt.rand.nextFloat() < 0.4F;
			bookChance = ctxt.rand.nextFloat() - 0.5F;
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setFloat("book", bookChance);
			tagCompound.setBoolean("light", light);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			super.readStructureFromNBT(tagCompound);
			bookChance = tagCompound.getFloat("book");
			light = tagCompound.getBoolean("light");
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
			fillWithBlocksRandomly(worldIn, structureBoundingBoxIn, randomIn, bookChance, 1, 1, 1, 1, 4, 2, bookshelf, bookshelf, false);
			fillWithBlocksRandomly(worldIn, structureBoundingBoxIn, randomIn, bookChance, 1, 1, 5, 1, 4, 6, bookshelf, bookshelf, false);
			fillWithBlocksRandomly(worldIn, structureBoundingBoxIn, randomIn, bookChance, 6, 1, 1, 6, 4, 2, bookshelf, bookshelf, false);
			fillWithBlocksRandomly(worldIn, structureBoundingBoxIn, randomIn, bookChance, 6, 1, 5, 6, 4, 6, bookshelf, bookshelf, false);
			fillWithBlocksRandomly(worldIn, structureBoundingBoxIn, randomIn, bookChance, 3, 1, 6, 4, 4, 6, bookshelf, bookshelf, false);
			
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
}