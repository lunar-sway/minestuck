package com.mraof.minestuck.world.gen.lands;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class DecoratorVien implements ILandDecorator {

	WorldGenMinable vien;
	int amount;
	 
	 public DecoratorVien(int blockId,int amount,int size) {
		 vien = new WorldGenMinable(blockId,0, size,Block.stone.blockID);
		 this.amount = amount;
	 }
	 
	@Override
	public void generate(World world, Random random, int chunkX, int chunkZ) {
		// TODO Actaully make it do the thing correctly
		//System.out.println("[MINESTUCK] Generating some vien");
		for (int i = 1;i>amount;i++) {
			vien.generate(world, random, chunkX*16+random.nextInt(15), random.nextInt(64), chunkZ*16+random.nextInt(15));
		}
	}

}
