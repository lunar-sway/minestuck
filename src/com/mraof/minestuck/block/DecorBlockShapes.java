package com.mraof.minestuck.block;

import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.LecternBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class DecorBlockShapes
{
	
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
	
	public static final VoxelShape BLENDER_SHAPE_N = VoxelShapes.or
			(
					//Base
					VoxelShapes.or(Block.makeCuboidShape(3, 0,3, 13, 3, 13),
							Block.makeCuboidShape(4, 3, 4, 12, 4, 12)),
					//Glass
					VoxelShapes.or(Block.makeCuboidShape(5, 4, 5, 11, 7, 11),
							Block.makeCuboidShape(4,7,4, 12, 13, 12),
							Block.makeCuboidShape(3, 13, 3, 13, 14, 13) ),
					//Handle
					VoxelShapes.or(Block.makeCuboidShape(3,6,7,5,7,9),
							Block.makeCuboidShape(2, 7, 7, 3, 11, 9),
							Block.makeCuboidShape(3, 11, 7, 4, 12, 9)),
					//Lid
					Block.makeCuboidShape(3,14,3,13,16,13)
			);
	
}
