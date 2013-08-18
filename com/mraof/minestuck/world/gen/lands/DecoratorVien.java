package com.mraof.minestuck.world.gen.lands;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.mraof.minestuck.world.gen.ChunkProviderLands;

public class DecoratorVien implements ILandDecorator {

	//WorldGenMinable vien;
	int amount;
	int id;
	int meta = 0;
	int size;
	 
	 public DecoratorVien(int blockId,int amount,int size) {
		 //vien = new WorldGenMinable(blockId,0, size,Block.stone.blockID);
		 this.amount = amount;
		 this.id = blockId;
		 this.amount = amount;
		 this.size = size;
	 }
	 
	 public DecoratorVien(int blockId,int meta,int amount,int size) {
		 this(blockId,amount,size);
		 this.meta = meta;
	 }
	 
	@Override
	public void generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider) {
//		for (int i = 1;i>amount;i++) {
//			vien.generate(world, random, chunkX*16+random.nextInt(15), random.nextInt(64), chunkZ*16+random.nextInt(15));
//		}
		
			int maxPossY = 0 + (60 - 1);
			int diffBtwnMinMaxY = 60 - 0;
			for(int x = 0; x < amount; x++)
		   {
		         int posX = chunkX * 16 + random.nextInt(16);
		         int posY = 0 + random.nextInt(diffBtwnMinMaxY);
		         int posZ = chunkZ * 16 + random.nextInt(16);
		         (new WorldGenMinable(id, meta, size/2 + random.nextInt(size*2),provider.surfaceBlock[0])).generate(world, random, posX, posY, posZ);
		   }
	}
}
