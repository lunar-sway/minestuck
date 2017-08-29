package com.mraof.minestuck.world.lands.decorator.structure;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;


public class RabbitHoleDecorator  extends SimpleStructureDecorator{
	public RabbitHoleDecorator(Biome biome){
		super(biome);
	}

	@Override
	public float getPriority() {
		return 0.5f;
	}

	@Override
	protected BlockPos generateStructure(World world, Random random, BlockPos pos, ChunkProviderLands provider) {

		IBlockState Ground=provider.getSurfaceBlock();
		IBlockState Air=Blocks.AIR.getDefaultState();

		BlockPos newpos = pos;
		while((world.getBlockState(newpos)==Air)){
				newpos=newpos.down();
		}
		while(world.getBlockState(newpos.up())!=Air){
			newpos=newpos.up();
		}
		if(world.getBlockState(newpos)!=Ground){
			if (world.getBlockState(newpos.down())==Ground){
				newpos=newpos.down();
			}else{
				return null;
			}
		}

			boolean mirror;
			xCoord = newpos.getX();
			zCoord = newpos.getZ();
			yCoord = newpos.getY();
			//check wich way it should be facing
			if(world.getBlockState(new BlockPos(xCoord, yCoord -1, zCoord)).getMaterial().isLiquid())
				return null;
			if(world.getBlockState(newpos.north())==Air){
				rotation=false;
				mirror=false;
			}else if(world.getBlockState(newpos.east())==Air){
				rotation=true;
				mirror=true;
			}else if(world.getBlockState(newpos.south())==Air){
				rotation=false;
				mirror=true;
			}else if(world.getBlockState(newpos.west())==Air){
				rotation=true;
				mirror=false;
			}else return null;
		
		if(mirror){
			placeBlock(world,Blocks.DEADBUSH.getDefaultState(),0,0,1);
			placeBlock(world,Air,0,0,0);
			placeBlock(world,Air,0,-1,0);
			placeBlock(world,Air,0,-1,-1);
			placeBlock(world,MinestuckBlocks.RabbitSpawner.getDefaultState(),0,-1,-2);
		}
		else{
			placeBlock(world,Blocks.DEADBUSH.getDefaultState(),0,0,-1);
			placeBlock(world,Air,0,0,0);
			placeBlock(world,Air,0,-1,0);
			placeBlock(world,Air,0,-1,1);
			placeBlock(world,MinestuckBlocks.RabbitSpawner.getDefaultState(),0,-1,2);
		}

		System.out.println(Ground);
		return null;
	}
		
	@Override
	public int getCount(Random random) {
		return 5;
	}
}

