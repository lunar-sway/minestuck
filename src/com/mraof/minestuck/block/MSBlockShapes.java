package com.mraof.minestuck.block;

import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.LecternBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class MSBlockShapes
{
	//Decor Blocks
	public static final CustomVoxelShape FROG_STATUE_BASE = new CustomVoxelShape(new double[] {0.5D, 0, 1.5D, 15.5D, 2, 14.5D});
	public static final CustomVoxelShape FROG_STATUE_BODY = new CustomVoxelShape
			(
					new double[] {4.5D, 2, 2.5D, 11.5D, 4, 13.5D},
					new double[] {1.5D, 2, 3.5D, 14.5D, 4, 12.5D},
					new double[] {2.5D, 4, 4.5D, 13.5D, 5, 11.5D},
					new double[] {4.5,4,3.5,11.5,7,12.5}
			);
	public static final CustomVoxelShape FROG_STATUE_HEAD = new CustomVoxelShape
			(
					new double[] {5.5,7,2.5,10.5,8,13.5},
					new double[] {3.5,8,2.5,12.5,9,14.5},
					new double[] {2.5,9,2.5,13.5,12,14.5},
					new double[] {2.5,12,2.5,13.5,13,13.5},
					new double[] {2.5,13,4.5,13.5,14,13.5},
					new double[] {3.5,13,3.5,12.5,14,4.5},
					new double[] {4.5,13,2.5,11.5,14,3.5},
					new double[] {3.5,14,4.5,5.5,15,13.5},
					new double[] {5.5,14,3.5,10.5,15,12.5},
					new double[] {10.5,14,4.5,12.5,15,13.5},
					new double[] {5.5,12,2.5,10.5,13,14.5}
			);
	public static final CustomVoxelShape FROG_STATUE = FROG_STATUE_BASE.merge(FROG_STATUE_BODY, FROG_STATUE_HEAD).rotate(Direction.SOUTH);
	
	public static final CustomVoxelShape BLENDER_BASE = new CustomVoxelShape(new double[] {3, 0,3, 13, 3, 13}, new double[] {4, 3, 4, 12, 4, 12});
	public static final CustomVoxelShape BLENDER_CONTAINER = new CustomVoxelShape(new double[] {5, 4, 5, 11, 7, 11}, new double[] {4,7,4, 12, 13, 12}, new double[] {3, 13, 3, 13, 14, 13});
	public static final CustomVoxelShape BLENDER_HANDLE = new CustomVoxelShape(new double[] {3,6,7,5,7,9}, new double[] {2, 7, 7, 3, 11, 9}, new double[] {3, 11, 7, 4, 12, 9});
	public static final CustomVoxelShape BLENDER_LID = new CustomVoxelShape(new double[] {3,14,3,13,16,13});
	public static final CustomVoxelShape BLENDER = BLENDER_BASE.merge(BLENDER_CONTAINER, BLENDER_HANDLE, BLENDER_LID);
	
	public static final CustomVoxelShape CHESSBOARD = new CustomVoxelShape(new double[] {0, 0, 0, 16, 1, 16});
	
	//Machines
	public static final CustomVoxelShape ALCHEMITER_CENTER = new CustomVoxelShape(new double[] {0,0,0,16,16,16});
	public static final CustomVoxelShape ALCHEMITER_CORNER = new CustomVoxelShape(new double[] {0,0,0,16,8,16}, new double[] {0,8,6,10,12,16}, new double[] {0,12,8,8,16,16});
	public static final CustomVoxelShape ALCHEMITER_LEFT_SIDE = new CustomVoxelShape(new double[] {0,0,0,16,8,16}, new double[] {0,8,6,16,12,16}, new double[] {0,12,8,16,16,16}, new double[] {0,8,2,4,12,6}, new double[] {10,8,1,14,11,5});
	public static final CustomVoxelShape ALCHEMITER_RIGHT_SIDE = new CustomVoxelShape(new double[] {0,0,0,16,8,16}, new double[] {0,8,6,16,12,16}, new double[] {0,12,8,16,16,16}, new double[] {12,8,2,16,12,6}, new double[] {2,8,1,6,11,5});
	public static final CustomVoxelShape ALCHEMITER_TOTEM_CORNER = new CustomVoxelShape(new double[] {0,0,0,16,8,16}, new double[] {0,8,6,10,12,16}, new double[] {0,12,8,8,16,16}, new double[] {8,8,2,14,16,8}, new double[] {10,8,12,14,16,16});
	public static final CustomVoxelShape ALCHEMITER_TOTEM_PAD = new CustomVoxelShape(new double[] {8,0,2,14,4,8}, new double[] {10,0,12,14,14,16}, new double[] {11,14,13,13,16,15});
	public static final CustomVoxelShape ALCHEMITER_LOWER_ROD = new CustomVoxelShape(new double[] {10,0,2,14,16,6}, new double[] {10,0,7,14,16,11}, new double[] {10,0,7,14,16,16}, new double[] {11,-2,3,13,0,10});
	public static final CustomVoxelShape ALCHEMITER_UPPER_ROD = new CustomVoxelShape(new double[] {10,0,2,14,8,6}, new double[] {10,0,7,14,8,11}, new double[] {10,0,12,14,8,16}, new double[] {11,8,8,13,10,15},
			new double[] {9,8,3,13,10,5}, new double[] {7,4,2,9,10,6}, new double[] {3,5,3,7,6,5});
	
	public static final CustomVoxelShape CRUXTRUDER_BASE_CORNER = new CustomVoxelShape(new double[]{1,0,1,16,9,16}, new double[] {9,9,9,16,16,16});
	public static final CustomVoxelShape CRUXTRUDER_BASE_SIDE = new CustomVoxelShape(new double[]{1,0,1,16,9,16}, new double[] {0,9,9,16,16,16},
			new double[] {3,9,6,4,13,9}, new double[] {3,13,6,13,14,9}, new double[] {12,9,6,13,13,9}, new double[] {4,9,7,12,13,9});
	
	public static final CustomVoxelShape CRUXTRUDER_CENTER = new CustomVoxelShape(new double[] {0,0,0,16,16,16});
	public static final CustomVoxelShape CRUXTRUDER_KNOB = new CustomVoxelShape(new double[] {7.5,11,1,8.5,12,2}, new double[] {6.5,13,0,9.5,14,1}, new double[] {9.5,10,0,10.5,13,1}, new double[] {6.5,9,0,9.5,10,1}, new double[] {5.5,10,0,6.5,13,1},
			new double[] {7.5,12,0,8.5,13,1}, new double[] {6.5,11,0,9.5,12,1}, new double[] {7.5,10,0,8.5,11,1});
	public static final CustomVoxelShape CRUXTRUDER_TUBE = new CustomVoxelShape(new double[] {3,0,3,13,1,13}, new double[] {2,1,2,14,15,14}, new double[] {3,15,3,13,16,13}).merge(CRUXTRUDER_KNOB);
	
	//TODO Totem Lathe, Punch Designix & Dowels
}
