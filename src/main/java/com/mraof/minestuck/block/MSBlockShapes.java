package com.mraof.minestuck.block;

import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.util.Direction;

public class MSBlockShapes
{
	//Decor Blocks
	public static final CustomVoxelShape FROG_STATUE_BASE = new CustomVoxelShape(new double[]{0.5D, 0, 1.5D, 15.5D, 2, 14.5D});
	public static final CustomVoxelShape FROG_STATUE_BODY = new CustomVoxelShape
			(
					new double[]{4.5D, 2, 2.5D, 11.5D, 4, 13.5D},
					new double[]{1.5D, 2, 3.5D, 14.5D, 4, 12.5D},
					new double[]{2.5D, 4, 4.5D, 13.5D, 5, 11.5D},
					new double[]{4.5,4,3.5,11.5,7,12.5}
			);
	public static final CustomVoxelShape FROG_STATUE_HEAD = new CustomVoxelShape
			(
					new double[]{5.5,7,2.5,10.5,8,13.5},
					new double[]{3.5,8,2.5,12.5,9,14.5},
					new double[]{2.5,9,2.5,13.5,12,14.5},
					new double[]{2.5,12,2.5,13.5,13,13.5},
					new double[]{2.5,13,4.5,13.5,14,13.5},
					new double[]{3.5,13,3.5,12.5,14,4.5},
					new double[]{4.5,13,2.5,11.5,14,3.5},
					new double[]{3.5,14,4.5,5.5,15,13.5},
					new double[]{5.5,14,3.5,10.5,15,12.5},
					new double[]{10.5,14,4.5,12.5,15,13.5},
					new double[]{5.5,12,2.5,10.5,13,14.5}
			);
	
	public static final CustomVoxelShape SPIKES = new CustomVoxelShape(new double[]{0, 0, 0, 16, 4, 16}/*, new double[]{1, 1, 1, 15, 15, 15}*/);
	
	public static final CustomVoxelShape FROG_STATUE = FROG_STATUE_BASE.merge(FROG_STATUE_BODY, FROG_STATUE_HEAD).rotate(Direction.SOUTH);
	
	public static final CustomVoxelShape WIZARD_STATUE = new CustomVoxelShape(new double[]{3, 0, 3, 13, 16, 12});
	public static final CustomVoxelShape DENIZEN_STATUE = new CustomVoxelShape(new double[]{2, 0, 2, 14, 14, 14});
	public static final CustomVoxelShape NAKAGATOR_STATUE = new CustomVoxelShape(new double[]{1, 0, 1, 15, 1, 15}, new double[]{5, 1, 6, 11, 16, 13}, new double[]{5, 11, 0, 11, 15, 6}, new double[]{6, 1, 11, 10, 4, 16});
	
	public static final CustomVoxelShape STEEP_STAIRS_BASE = new CustomVoxelShape(new double[]{0, 0, 0, 16, 4, 16}, new double[]{0, 4, 2, 16, 8, 16}, new double[]{0, 8, 4, 16, 12, 16}, new double[]{0, 12, 6, 16, 16, 16});
	public static final CustomVoxelShape STEEP_STAIRS_TOP = new CustomVoxelShape(new double[]{0, 0, 8, 16, 4, 16}, new double[]{0, 4, 10, 16, 8, 16}, new double[]{0, 8, 12, 16, 12, 16}, new double[]{0, 12, 14, 16, 16, 16});
	
	public static final CustomVoxelShape LOTUS_TIME_CAPSULE = new CustomVoxelShape(new double[]{0, 0, 0, 16, 7, 16}, new double[]{0, 7, 1, 15, 9, 16}, new double[]{0, 9, 0, 16, 11, 16}, new double[]{0, 11, 2, 14, 14, 16}, new double[]{0, 11, 7, 9, 15, 16}, new double[]{0, 15, 9, 7, 16, 16});
	
	public static final CustomVoxelShape STONE_TABLET = new CustomVoxelShape(new double[]{3, 0, 3, 13, 1, 15}, new double[]{3, 0, 2, 12, 1, 3}, new double[]{3, 0, 1, 11, 1, 2});
	
	public static final CustomVoxelShape BLENDER_BASE = new CustomVoxelShape(new double[]{3, 0, 3, 13, 3, 13}, new double[]{4, 3, 4, 12, 4, 12});
	public static final CustomVoxelShape BLENDER_CONTAINER = new CustomVoxelShape(new double[]{5, 4, 5, 11, 7, 11}, new double[]{4, 7, 4, 12, 13, 12}, new double[]{3, 13, 3, 13, 14, 13});
	public static final CustomVoxelShape BLENDER_HANDLE = new CustomVoxelShape(new double[]{3, 6, 7, 5, 7, 9}, new double[]{2, 7, 7, 3, 11, 9}, new double[]{3, 11, 7, 4, 12, 9});
	public static final CustomVoxelShape BLENDER_LID = new CustomVoxelShape(new double[]{3, 14, 3, 13, 16, 13});
	public static final CustomVoxelShape BLENDER = BLENDER_BASE.merge(BLENDER_CONTAINER, BLENDER_HANDLE, BLENDER_LID);
	
	public static final CustomVoxelShape CHESSBOARD = new CustomVoxelShape(new double[]{0, 0, 0, 16, 1, 16});
	
	public static final CustomVoxelShape CASSETTE_PLAYER = new CustomVoxelShape(new double[]{4, 0, 4, 12, 2, 15});
	
	public static final CustomVoxelShape PIPE = new CustomVoxelShape(new double[]{0, 0, 0, 2, 14, 16}, new double[]{0, 16, 0, 14, 14, 16}, new double[]{16, 16, 0, 14, 2, 16}, new double[]{16, 0, 0, 2, 2, 16});
	public static final CustomVoxelShape PARCEL_PYXIS = new CustomVoxelShape(new double[]{3, 0, 3, 13, 16, 13}, new double[]{2, 0, 2, 14, 3, 14});
	public static final CustomVoxelShape PYXIS_LID = new CustomVoxelShape(new double[]{3, 0, 3, 13, 3, 13});
	
	//Machines
	public static final CustomVoxelShape ALCHEMITER_CENTER = new CustomVoxelShape(new double[]{0, 0, 0, 16, 16, 16});
	public static final CustomVoxelShape ALCHEMITER_CORNER = new CustomVoxelShape(new double[]{0, 0, 0, 16, 8, 16}, new double[]{0, 8, 6, 10, 12, 16}, new double[]{0, 12, 8, 8, 16, 16});
	public static final CustomVoxelShape ALCHEMITER_LEFT_SIDE = new CustomVoxelShape(new double[]{0, 0, 0, 16, 8, 16}, new double[]{0, 8, 6, 16, 12, 16}, new double[]{0, 12, 8, 16, 16, 16}, new double[]{0, 8, 2, 4, 12, 6}, new double[]{10, 8, 1, 14, 11, 5});
	public static final CustomVoxelShape ALCHEMITER_RIGHT_SIDE = new CustomVoxelShape(new double[]{0, 0, 0, 16, 8, 16}, new double[]{0, 8, 6, 16, 12, 16}, new double[]{0, 12, 8, 16, 16, 16}, new double[]{12, 8, 2, 16, 12, 6}, new double[]{2, 8, 1, 6, 11, 5});
	public static final CustomVoxelShape ALCHEMITER_TOTEM_CORNER = new CustomVoxelShape(new double[]{0, 0, 0, 16, 8, 16}, new double[]{0, 8, 6, 10, 12, 16}, new double[]{0, 12, 8, 8, 16, 16}, new double[]{8, 8, 2, 14, 16, 8}, new double[]{10, 8, 12, 14, 16, 16});
	public static final CustomVoxelShape ALCHEMITER_TOTEM_PAD = new CustomVoxelShape(new double[]{8, 0, 2, 14, 4, 8}, new double[]{10, 0, 12, 14, 14, 16}, new double[]{11, 14, 13, 13, 16, 15});
	public static final CustomVoxelShape ALCHEMITER_LOWER_ROD = new CustomVoxelShape(new double[]{10, 0, 2, 14, 16, 6}, new double[]{10, 0, 7, 14, 16, 11}, new double[]{10, 0, 7, 14, 16, 16}, new double[]{11, -2, 3, 13, 0, 10});
	public static final CustomVoxelShape ALCHEMITER_UPPER_ROD = new CustomVoxelShape(new double[]{10, 0, 2, 14, 8, 6}, new double[]{10, 0, 7, 14, 8, 11}, new double[]{10, 0, 12, 14, 8, 16}, new double[]{11, 8, 8, 13, 10, 15},
			new double[]{9,8,3,13,10,5}, new double[]{7,4,2,9,10,6}, new double[]{3,5,3,7,6,5});
	
	public static final CustomVoxelShape CRUXTRUDER_BASE_CORNER = new CustomVoxelShape(new double[]{1,0,1,16,9,16}, new double[]{9,9,9,16,16,16});
	public static final CustomVoxelShape CRUXTRUDER_BASE_SIDE = new CustomVoxelShape(new double[]{0,0,1,16,9,16}, new double[]{0,9,9,16,16,16},
			new double[]{3,9,6,4,13,9}, new double[]{3,13,6,13,14,9}, new double[]{12,9,6,13,13,9}, new double[]{4,9,7,12,13,9});
	public static final CustomVoxelShape CRUXTRUDER_CENTER = new CustomVoxelShape(new double[]{0,0,0,16,16,16});
	public static final CustomVoxelShape CRUXTRUDER_KNOB = new CustomVoxelShape(new double[]{7.5,11,1,8.5,12,2}, new double[]{6.5,13,0,9.5,14,1}, new double[]{9.5,10,0,10.5,13,1}, new double[]{6.5,9,0,9.5,10,1}, new double[]{5.5,10,0,6.5,13,1},
			new double[]{7.5,12,0,8.5,13,1}, new double[]{6.5,11,0,9.5,12,1}, new double[]{7.5,10,0,8.5,11,1});
	public static final CustomVoxelShape CRUXTRUDER_TUBE = new CustomVoxelShape(new double[]{3,0,3,13,1,13}, new double[]{2,1,2,14,15,14}, new double[]{3,15,3,13,16,13}).merge(CRUXTRUDER_KNOB);
	
	
	public static final CustomVoxelShape TOTEM_LATHE_BOTTOM_CORNER = new CustomVoxelShape(new double[]{4,0,5,16,2,15}, new double[]{4,2,8,16,7,12}, new double[]{5,5,7,12,12,13}, new double[]{6,12,7,12,16,13},
			new double[]{8,6,5,9,7,7}, new double[]{7,7,5,8,9,6}, new double[]{6,9,5,7,10,6});
	public static final CustomVoxelShape TOTEM_LATHE_BOTTOM_LEFT = new CustomVoxelShape(new double[]{0,0,5,16,2,15}, new double[]{0,2,6,16,9,14}, new double[]{0,9,4,16,12,16}, new double[]{12,12,8,16,16,12},
			new double[]{4,5,4,5,6,6}, new double[]{5,6,4,6,7,5}, new double[]{3,6,4,4,7,5}, new double[]{5,4,4,6,5,5}, new double[]{3,4,4,4,5,5});
	public static final CustomVoxelShape TOTEM_LATHE_BOTTOM_RIGHT = new CustomVoxelShape(new double[]{0,0,5,16,2,15}, new double[]{8,2,6,16,9,14}, new double[]{0,2,8,8,7,12}, new double[]{6,9,4,16,12,16},
			new double[]{12,9,3,13,10,4}, new double[]{11,11,2,14,12,3}, new double[]{11,9,2,14,10,3}, new double[]{11,7,2,14,8,3}, new double[]{14,8,2,15,11,3}, new double[]{10,8,2,11,11,3},
			new double[]{12,10,2,13,11,3}, new double[]{12,8,2,13,9,3});
	public static final CustomVoxelShape TOTEM_LATHE_CARD_SLOT = new CustomVoxelShape(new double[]{8,0,4,16,16,14}, new double[]{0,0,5,7,2,15}, new double[]{0,2,3,7,13,14}, new double[]{0,13,6,7,16,14},
			new double[]{7,0,6,8,16,14}, new double[]{9,12,3,16,16,4});
	public static final CustomVoxelShape TOTEM_LATHE_CARVER = new CustomVoxelShape(new double[]{6,12,5,16,16,15}, new double[]{8,3,7,16,12,13}, new double[]{9,2,8,15,3,12}, new double[]{13,1,9,14,2,11}, new double[]{10,0,9,12,2,11},
			new double[]{11,3,5,15,7,7}, new double[]{7,5,5,11,6,6});
	public static final CustomVoxelShape TOTEM_LATHE_MIDDLE = new CustomVoxelShape(new double[]{0,0,6,8,16,14}, new double[]{8,0,4,16,12,14}, new double[]{8,12,7,16,16,15});
	public static CustomVoxelShape TOTEM_LATHE_MIDDLE_RIGHT_ACTIVE = new CustomVoxelShape(new double[]{6,0,7,12,5,13}, new double[]{2,5,8,16,9,12},
			new double[]{1,7,10,2,8,11}, new double[]{0,9,9,1,10,12}, new double[]{0,7,9,1,8,12}, new double[]{0,5,9,1,6,12}, new double[]{0,6,8,1,9,9}, new double[]{0,6,10,1,9,11}, new double[]{0,6,12,1,9,13});
public static CustomVoxelShape TOTEM_LATHE_MIDDLE_RIGHT = TOTEM_LATHE_MIDDLE_RIGHT_ACTIVE.merge(new CustomVoxelShape(new double[]{13,4,7,16,10,13}));
public static CustomVoxelShape TOTEM_LATHE_ROD_LEFT = new CustomVoxelShape(new double[]{8,4,7,16,10,13});
public static CustomVoxelShape TOTEM_LATHE_ROD_LEFT_ACTIVE = new CustomVoxelShape(new double[]{8,5,8,16,9,12}, new double[]{0,4,7,8,10,13});
public static CustomVoxelShape TOTEM_LATHE_ROD_RIGHT_BASE = new CustomVoxelShape(new double[]{0,5,8,5,9,12}, new double[]{5,4,7,8,10,13});
public static CustomVoxelShape TOTEM_LATHE_ROD_RIGHT_CARVED = TOTEM_LATHE_ROD_RIGHT_BASE.merge(new CustomVoxelShape(new double[]{8,5,12,9,9,13}, new double[]{8,9,8,9,10,12}, new double[]{8,5,7,9,9,8}, new double[]{8,4,8,9,5,12}, new double[]{8,5,8,11,9,12}, new double[]{11,5,5,8,5,13/*,8.5,11.5*/}, new double[]{13,5,8,16,9,12}, new double[]{15,9,8,16,10,12}, new double[]{15,5,12,16,9,13}, new double[]{15,4,8,16,5,12}, new double[]{15,5,7,16,9,8}));
public static CustomVoxelShape TOTEM_LATHE_ROD_RIGHT = TOTEM_LATHE_ROD_RIGHT_BASE.merge(new CustomVoxelShape(new double[]{8,5,12,16,9,13}, new double[]{8,4,8,16,10,12}, new double[]{8,5,7,16,9,8}));
public static CustomVoxelShape TOTEM_LATHE_TOP_LEFT = new CustomVoxelShape(new double[]{8,0,7,16,7,15}, new double[]{0,0,6,8,12,14}, new double[]{8,7,7,16,12,14}, new double[]{0,12,5,16,16,15}, new double[]{9,3,5,15,9,7});
public static CustomVoxelShape TOTEM_LATHE_TOP_MIDDLE = new CustomVoxelShape(new double[]{0,12,5,16,16,15}, new double[]{12,4,6,16,12,14}, new double[]{2,4,7,12,10,13}, new double[]{5,6,6,12,10,7}, new double[]{0,3,7,2,12,13});

public static CustomVoxelShape PUNCH_DESIGNIX_BOTTOM_LEFT = new CustomVoxelShape(new double[]{12,0,7,15,11,16}, new double[]{0,11,7,15,15,16}, new double[]{0,15,3.5,16,16,16});
public static CustomVoxelShape PUNCH_DESIGNIX_BOTTOM_RIGHT = new CustomVoxelShape(new double[]{1,0,7,4,11,16}, new double[]{0,15,3.5,16,16,16}, new double[]{1,11,7,16,15,16});
public static CustomVoxelShape PUNCH_DESIGNIX_TOP_LEFT = new CustomVoxelShape(new double[]{13,7,13.55,15,7.5,16}, new double[]{0,0,9.075,15,3,16}, new double[]{0,2,13.55,15,7.25,16}, new double[]{0,3,10.075,15,4,13.55}, new double[]{0,4,11.075,15,5,13.55}, new double[]{0,5,12.075,15,6,13.55}, new double[]{0,6,13.075,15,7,13.55}, new double[]{11,7,13.55,12,7.5,16}, new double[]{0,7,13.55,10,7.5,16});

public static CustomVoxelShape PUNCH_DESIGNIX_KEYBOARD = new CustomVoxelShape(new double[]{8,0.3,3.775,12,1.1,4.675}, new double[]{3,0.6,4.475,12,1.4,5.375}, new double[]{3,0.89,5.175,12,1.69,6.075}, new double[]{3,1.18,5.875,12,1.98,6.775}, new double[]{3,1.47,6.575,12,2.27,7.475}, new double[]{3,1.76,7.275,12,2.56,7.725});
public static CustomVoxelShape PUNCH_DESIGNIX_RIGHT_SLOPE = new CustomVoxelShape(new double[]{12,3,10.075,16,4,14.075}, new double[]{12,4,11.075,16,5,14.075}, new double[]{12,5,12.075,16,6,14.075}, new double[]{2,6,13.075,16,7,14.075}, new double[]{2,3,10.075,3,4,14.075}, new double[]{2,4,11.075,3,5,14.075}, new double[]{2,5,12.075,3,6,14.075}, new double[]{2,6,13.075,3,7,14.075});
public static CustomVoxelShape PUNCH_DESIGNIX_TOP_RIGHT_BASE = new CustomVoxelShape(new double[]{1,0,13.55,16,7.5,16}, new double[]{12,0,9.075,16,3,13.55}, new double[]{3,0,9.075,12,2.5,13.55}, new double[]{1,0,9.075,3,3,13.55}, new double[]{3,2,13,12,6.5,15}, new double[]{3,2,12,15,4.5,14});

public static CustomVoxelShape PUNCH_DESIGNIX_TOP_RIGHT = PUNCH_DESIGNIX_TOP_RIGHT_BASE.merge(PUNCH_DESIGNIX_RIGHT_SLOPE, PUNCH_DESIGNIX_KEYBOARD);

	//Small Machines
	public static CustomVoxelShape SMALL_PUNCH_DESIGNIX = new CustomVoxelShape(new double[]{0,0,8,2,6,16}, new double[]{14,0,8,16,6,16}, new double[]{0,6,8,16,10,16}, new double[]{0,10,6,16,11,16}, new double[]{0,11,11,16,16,16}, new double[]{1,11,6.5,9.5,13,10.5});
	public static CustomVoxelShape SMALL_CRUXTRUDER = new CustomVoxelShape(new double[]{0,0,0,16,4,16}, new double[]{3,4,3,13,9,13}, new double[]{6,9,6,10,15,10}, new double[]{1.5,4,6,3,6,10}, new double[]{6,4,1.5,10,6,3}, new double[]{13,4,6,14.5,6,10}, new double[]{6,4,13,10,6,14.5});
	public static CustomVoxelShape SMALL_TOTEM_LATHE = new CustomVoxelShape(new double[]{13,0,5.5,16,10,11}, new double[]{13,10,6.5,16,14,11}, new double[]{0,0,5,13,1,11}, new double[]{11,1,5,13,14,11}, new double[]{6,14,5,16,16,11}, new double[]{7,12,5,11,14,11}, new double[]{6,1,5.5,11,2.5,10.5}, new double[]{5,2.5,5,11,3,11}, new double[]{4,1,6.5,6,2,9.5}, new double[]{2,1,5.5,4,5,10.5}, new double[]{1,5,5.5,5,7,10.5}, new double[]{0.5,4.5,6.5,1,7.5,9.5}, new double[]{7.5,11,5.5,8.5,12,10.5}, new double[]{10,5.5,7.5,11,6.5,8.5}, new double[]{5,5.5,7.5,6,6.5,8.5});
	public static CustomVoxelShape SMALL_ALCHEMITER = new CustomVoxelShape(new double[]{0,0,0,16,4,16}, new double[]{1,4,1,15,6,15}, new double[]{2,6,2,14,8,14}, new double[]{0.5,4,6.75,1,5,9.25}, new double[]{6.75,4,0.5,9.25,5,1}, new double[]{15,4,6.75,15.5,5,9.25}, new double[]{6.75,4,15,9.25,5,15.5}, new double[]{13.5,4,13.5,15.5,9,15.5}, new double[]{11.5,4,14.5,12.5,15,15.5}, new double[]{13,11,14.5,14,15,15.5}, new double[]{14.5,11,14.5,15.5,15,15.5}, new double[]{12,15,14.5,13.5,15.5,15.5}, new double[]{13.5,10.5,14.5,15,11,15.5}, new double[]{14,15,14,16,17,16});
	
	//Misc Machines
	public static CustomVoxelShape TRANSPORTALIZER = new CustomVoxelShape(new double[]{0,0,0,16,6,16}, new double[]{1,6,1,15,8,15});
	public static CustomVoxelShape SENDIFICATOR = new CustomVoxelShape(new double[]{0,0,0,16,1,16},new double[]{0,16,0,16,15,16},new double[]{0,1,0,1,15,16},new double[]{16,1,0,15,15,16},new double[]{1,1,15,15,15,16});
	public static CustomVoxelShape GRIST_WIDGET = new CustomVoxelShape(new double[]{6,0,5,10,1.5,11.7}, new double[]{2.5,1,5.3,13.5,2.1,11.15}, new double[]{10,0,5,14,1.75,12}, new double[]{11,0.3,11.2,13,1.3,12.2}, new double[]{2,0,5,6,1.75,11}, new double[]{6,0,5,4,1.75,7}).rotate(Direction.SOUTH);
	public static CustomVoxelShape COMPUTER = new CustomVoxelShape(/*mouse*/new double[]{0.5,0,2.5,2.5,1,5}, /*keyboard*/new double[]{3.5,0,0.5,15.5,1,7}, /*base*/new double[]{0.5,0,8.5,15.5,3,15.5}, /*stand*/new double[]{3.5,3,11,12,4.5,13}, /*screen*/new double[]{1,4.5,11.5,15,15.5,12.5});
	public static CustomVoxelShape LAPTOP_CLOSED = new CustomVoxelShape(new double[]{1,0,3,15,2,14}, new double[]{1,0.5,14,15,1.5,14.5});
	public static CustomVoxelShape LAPTOP_OPEN = new CustomVoxelShape(new double[]{1,0,3,15,1,14}, new double[]{1,0.5,14,15,1,14.5}, new double[]{1,1,14,15,12,15});
	public static CustomVoxelShape OLD_COMPUTER = new CustomVoxelShape(/*mouse*/new double[]{0,0,0,3,1,4}, /*keyboard*/new double[]{4,0,0,16,1,5}, /*body*/new double[]{1,0,6,15,13,16});
	public static CustomVoxelShape LUNCHTOP_OPEN = new CustomVoxelShape(/*base*/new double[]{4,0,1.5,12,3,15.5}, /*core*/new double[]{5,3,2.5,11,5,7.5});
}
