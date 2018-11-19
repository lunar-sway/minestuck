package com.mraof.minestuck.block;

import com.mraof.minestuck.block.BlockMinestuckPlanks.BlockType;
import com.mraof.minestuck.item.TabMinestuck;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockFrostPlanks extends Block {

	public BlockFrostPlanks() {
		super(Material.WOOD);
		setCreativeTab(TabMinestuck.instance);
		setUnlocalizedName("frostPlanks");
		this.setHardness(2.0F);
		this.setSoundType(SoundType.WOOD);
	}

}
