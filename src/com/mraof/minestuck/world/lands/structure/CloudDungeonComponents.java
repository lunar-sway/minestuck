package com.mraof.minestuck.world.lands.structure;

import java.util.List;
import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class CloudDungeonComponents
{
	public static final int COMPONENT_WIDTH = 10;
	public static final int COMPONENT_HEIGHT = 8;
	
	public static void registerComponents()
	{
		MapGenStructureIO.registerStructureComponent(CloudDungeonStart.EntryComponent.class, "MinestuckCloudEntry");
		MapGenStructureIO.registerStructureComponent(CloudDungeonComponents.Entrance.class, "MinestuckCloudEntrance");
		MapGenStructureIO.registerStructureComponent(CloudDungeonComponents.Edge.class, "MinestuckCloudEdge");
		MapGenStructureIO.registerStructureComponent(CloudDungeonComponents.EdgeTop.class, "MinestuckCloudEdgeTop");
		MapGenStructureIO.registerStructureComponent(CloudDungeonComponents.Corner.class, "MinestuckCloudCorner");
		MapGenStructureIO.registerStructureComponent(CloudDungeonComponents.CornerTop.class, "MinestuckCloudCornerTop");
		MapGenStructureIO.registerStructureComponent(CloudDungeonComponents.Boss.class, "MinestuckCloudBoss");
	}
	
	public static abstract class CloudDungeonComponent extends StructureComponentUtil
	{
		protected int variant = 0;
		
		protected abstract boolean connectFrom(EnumFacing facing);
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			tagCompound.setInteger("variant", variant);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			variant = tagCompound.getInteger("variant");
		}
	}
	
	public static class Entrance extends CloudDungeonComponent
	{
		public Entrance()
		{
			
		}
		
		public Entrance(EnumFacing coordBaseMode, int posX, int posZ, Random rand, List<StructureComponent> componentList)
		{
			this();
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = COMPONENT_WIDTH;
			int zWidth = COMPONENT_WIDTH;
			
			int y = 96 + rand.nextInt(8) + rand.nextInt(8);
			int offset = COMPONENT_WIDTH / 2;
			
			int x = posX + offset;
			int z = posZ + offset;
			
			this.boundingBox = new StructureBoundingBox(x, y, z, x + COMPONENT_WIDTH - 1, y + COMPONENT_HEIGHT - 1, z + COMPONENT_WIDTH - 1);
			
			StructureContext ctxt = new StructureContext(componentList, rand);
			ctxt.compoGen[1][0][1] = this;
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			return true;
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			return true;
		}
	}
	
	public static class Edge extends CloudDungeonComponent
	{
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected boolean connectFrom(EnumFacing facing) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	public static class EdgeTop extends CloudDungeonComponent
	{
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected boolean connectFrom(EnumFacing facing) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	public static class Corner extends CloudDungeonComponent
	{
		public Corner(EnumFacing facing, BlockPos pos, int x, int y, int z, int index, StructureContext ctxt)
		{
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected boolean connectFrom(EnumFacing facing) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	public static class CornerTop extends CloudDungeonComponent
	{
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected boolean connectFrom(EnumFacing facing) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	public static class Boss extends CloudDungeonComponent
	{
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected boolean connectFrom(EnumFacing facing) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	protected static class StructureContext
	{
		CloudDungeonComponent[][][] compoGen = new CloudDungeonComponent[3][2][3];
		List<StructureComponent> compoList;
		Random rand;
		
		public StructureContext(List<StructureComponent> compoList, Random rand)
		{
			this.compoList = compoList;
			this.rand = rand;
		}
	}
	
	public static boolean generatePart(StructureContext ctxt, int x, int y, int z, BlockPos pos, EnumFacing facing, int index)
	{
		if(Math.abs(x) > 1 || y < 0	|| y > 1 || Math.abs(z) > 1)
			return false;
		
		if(ctxt.compoGen[x+1][y][z+1] != null)
		{
			return ctxt.compoGen[x+1][y][z+1].connectFrom(facing.getOpposite());
		}
		
		CloudDungeonComponent component;
		
//		if(Math.abs(x) == 1 && Math.abs(z) == 1)
//		{
//			if(y==0)
//				component = new Corner(facing, pos, x, y, z, index, ctxt);
//			else
//				component = new CornerTop(facing, pos, x, y, z, index, ctxt);
//		} else if(Math.abs(x) == 1 || Math.abs(z) == 1)
//		{
//			if(y==0)
//				component = new Edge(facing, pos, x, y, z, index, ctxt);
//			else
//				component = new EdgeTop(facing, pos, x, y, z, index, ctxt);
//		} else
//		{
//			component = new Boss(facing, pos, x, y, z, index, ctxt);
//		}
//		
//		ctxt.compoList.add(component);
		
		return true;
	}
}