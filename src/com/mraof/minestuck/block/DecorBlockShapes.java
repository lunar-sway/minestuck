package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.LecternBlock;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class DecorBlockShapes
{
	
	public static final VoxelShape FROG_STATUE_SHAPE_N = VoxelShapes.or
			(
					//Base
					Block.makeCuboidShape(0.5D, 0, 1.5D, 15.5D, 2, 14.5D),
					//Body
					VoxelShapes.or(Block.makeCuboidShape(4.5D, 2, 2.5D, 11.5D, 4, 13.5D),
							Block.makeCuboidShape(1.5D, 2, 3.5D, 14.5D, 4, 12.5D),
							Block.makeCuboidShape(2.5D, 4, 4.5D, 13.5D, 5, 11.5D),
							Block.makeCuboidShape(4.5,4,3.5,11.5,7,12.5)),
					//Head
					VoxelShapes.or(Block.makeCuboidShape(5.5,7,2.5,10.5,8,13.5),
							Block.makeCuboidShape(3.5,8,2.5,12.5,9,14.5),
							Block.makeCuboidShape(2.5,9,2.5,13.5,12,14.5),
							Block.makeCuboidShape(2.5,12,2.5,13.5,13,13.5),
							Block.makeCuboidShape(2.5,13,4.5,13.5,14,13.5),
							Block.makeCuboidShape(3.5,13,3.5,12.5,14,4.5),
							Block.makeCuboidShape(4.5,13,2.5,11.5,14,3.5),
							Block.makeCuboidShape(3.5,14,4.5,5.5,15,13.5),
							Block.makeCuboidShape(5.5,14,3.5,10.5,15,12.5),
							Block.makeCuboidShape(10.5,14,4.5,12.5,15,13.5),
							Block.makeCuboidShape(5.5,12,2.5,10.5,13,14.5))
			);
	public static final VoxelShape FROG_STATUE_SHAPE_W = VoxelShapes.or
			(
					//Base
					Block.makeCuboidShape(1.5D, 0, 0.5D, 14.5D, 2, 15.5D),
					//Body
					VoxelShapes.or(Block.makeCuboidShape(2.5D, 2, 4.5D, 13.5D, 4, 11.5D),
							Block.makeCuboidShape(3.5D, 2, 1.5D, 12.5D, 4, 14.5D),
							Block.makeCuboidShape(4.5D, 4, 2.5D, 11.5D, 5, 13.5D),
							Block.makeCuboidShape(3.5,4,4.5,12.5,7,11.5)),
					//Head
					VoxelShapes.or(Block.makeCuboidShape(2.5,7,5.5,13.5,8,10.5),
							Block.makeCuboidShape(2.5,8,3.5,14.5,9,12.5),
							Block.makeCuboidShape(2.5,9,2.5,14.5,12,13.5),
							Block.makeCuboidShape(2.5,12,2.5,13.5,13,13.5),
							Block.makeCuboidShape(4.5,13,2.5,13.5,14,13.5),
							Block.makeCuboidShape(3.5,13,3.5,4.5,14,12.5),
							Block.makeCuboidShape(2.5,13,4.5,3.5,14,11.5),
							Block.makeCuboidShape(4.5,14,3.5,13.5,15,5.5),
							Block.makeCuboidShape(3.5,14,5.5,12.5,15,10.5),
							Block.makeCuboidShape(4.5,14,10.5,13.5,15,12.5),
							Block.makeCuboidShape(2.5,12,5.5,14.5,13,10.5))
			);
	public static final VoxelShape FROG_STATUE_SHAPE_S = VoxelShapes.or
			(
					//Base
					Block.makeCuboidShape(0.5D, 0, 1.5D, 15.5D, 2, 14.5D),
					//Body
					VoxelShapes.or(Block.makeCuboidShape(4.5D, 2, 2.5D, 11.5D, 4, 13.5D),
							Block.makeCuboidShape(1.5D, 2, 3.5D, 14.5D, 4, 12.5D),
							Block.makeCuboidShape(2.5D, 4, 4.5D, 13.5D, 5, 11.5D),
							Block.makeCuboidShape(4.5,4,3.5,11.5,7,12.5)),
					//Head
					VoxelShapes.or(Block.makeCuboidShape(5.5,7,2.5,10.5,8,13.5),
							Block.makeCuboidShape(3.5,8,1.5,12.5,9,13.5),
							Block.makeCuboidShape(2.5,9,1.5,13.5,12,13.5),
							Block.makeCuboidShape(2.5,12,2.5,13.5,13,13.5),
							Block.makeCuboidShape(2.5,13,2.5,13.5,14,11.5),
							Block.makeCuboidShape(3.5,13,11.5,12.5,14,12.5),
							Block.makeCuboidShape(4.5,13,12.5,11.5,14,13.5),
							Block.makeCuboidShape(10.5,14,2.5,12.5,15,11.5),
							Block.makeCuboidShape(5.5,14,3.5,10.5,15,12.5),
							Block.makeCuboidShape(3.5,14,2.5,5.5,15,11.5),
							Block.makeCuboidShape(5.5,12,1.5,10.5,13,13.5))
			);
	public static final VoxelShape FROG_STATUE_SHAPE_E = VoxelShapes.or
			(
					//Base
					Block.makeCuboidShape(1.5D, 0, 0.5D, 14.5D, 2, 15.5D),
					//Body
					VoxelShapes.or(Block.makeCuboidShape(2.5D, 2, 4.5D, 13.5D, 4, 11.5D),
							Block.makeCuboidShape(3.5D, 2, 1.5D, 12.5D, 4, 14.5D),
							Block.makeCuboidShape(4.5D, 4, 2.5D, 11.5D, 5, 13.5D),
							Block.makeCuboidShape(3.5,4,4.5,12.5,7,11.5)),
					//Head
					VoxelShapes.or(Block.makeCuboidShape(10.5,7,13.5,5.5,8,2.5),
							Block.makeCuboidShape(1.5,8,3.5,13.5,9,12.5),
							Block.makeCuboidShape(1.5,9,2.5,13.5,12,13.5),
							Block.makeCuboidShape(2.5,12,2.5,13.5,13,13.5),
							Block.makeCuboidShape(2.5,13,2.5,11.5,14,13.5),
							Block.makeCuboidShape(11.5,13,3.5,12.5,14,12.5),
							Block.makeCuboidShape(12.5,13,4.5,13.5,14,11.5),
							Block.makeCuboidShape(2.5,14,10.5,11.5,15,12.5),
							Block.makeCuboidShape(3.5,14,5.5,12.5,15,10.5),
							Block.makeCuboidShape(2.5,14,3.5,11.5,15,5.5),
							Block.makeCuboidShape(1.5,12,5.5,13.5,13,10.5))
			);
	
	
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
	public static final VoxelShape BLENDER_SHAPE_E = VoxelShapes.or
		(
				//Base
				VoxelShapes.or(Block.makeCuboidShape(3, 0,3, 13, 3, 13),
						Block.makeCuboidShape(4, 3, 4, 12, 4, 12)),
				//Glass
				VoxelShapes.or(Block.makeCuboidShape(5, 4, 5, 11, 7, 11),
						Block.makeCuboidShape(4,7,4, 12, 13, 12),
						Block.makeCuboidShape(3, 13, 3, 13, 14, 13) ),
				//Handle
				VoxelShapes.or(Block.makeCuboidShape(7,6,3,9,7,5),
						Block.makeCuboidShape(7, 7, 2, 9, 11, 3),
						Block.makeCuboidShape(7, 11, 3, 9, 12, 4)),
				//Lid
				Block.makeCuboidShape(3,14,3,13,16,13)
		);
	public static final VoxelShape BLENDER_SHAPE_W = VoxelShapes.or
			(
					//Base
					VoxelShapes.or(Block.makeCuboidShape(3, 0,3, 13, 3, 13),
							Block.makeCuboidShape(4, 3, 4, 12, 4, 12)),
					//Glass
					VoxelShapes.or(Block.makeCuboidShape(5, 4, 5, 11, 7, 11),
							Block.makeCuboidShape(4,7,4, 12, 13, 12),
							Block.makeCuboidShape(3, 13, 3, 13, 14, 13) ),
					//Handle
					VoxelShapes.or(Block.makeCuboidShape(9,6,13,7,7,11),
							Block.makeCuboidShape(9, 7, 14, 7, 11, 13),
							Block.makeCuboidShape(9, 11, 13, 7, 12, 12)),
					//Lid
					Block.makeCuboidShape(3,14,3,13,16,13)
			);
	public static final VoxelShape BLENDER_SHAPE_S = VoxelShapes.or
			(
					//Base
					VoxelShapes.or(Block.makeCuboidShape(3, 0,3, 13, 3, 13),
							Block.makeCuboidShape(4, 3, 4, 12, 4, 12)),
					//Glass
					VoxelShapes.or(Block.makeCuboidShape(5, 4, 5, 11, 7, 11),
							Block.makeCuboidShape(4,7,4, 12, 13, 12),
							Block.makeCuboidShape(3, 13, 3, 13, 14, 13) ),
					//Handle
					VoxelShapes.or(Block.makeCuboidShape(13,6,9,11,7,7),
							Block.makeCuboidShape(14, 7, 9, 13, 11, 7),
							Block.makeCuboidShape(13, 11, 9, 12, 12, 7)),
					//Lid
					Block.makeCuboidShape(3,14,3,13,16,13)
			);
	public static final VoxelShape[] BLENDER_SHAPE = {BLENDER_SHAPE_N,BLENDER_SHAPE_E,BLENDER_SHAPE_S,BLENDER_SHAPE_W};
	public static final VoxelShape[] FROG_STATUE_SHAPE = {FROG_STATUE_SHAPE_N, FROG_STATUE_SHAPE_E, FROG_STATUE_SHAPE_S, FROG_STATUE_SHAPE_W};
	public static final VoxelShape[] CHESSBOARD_SHAPE = {Block.makeCuboidShape(0, 0, 0, 16, 1, 16)};
	
}
