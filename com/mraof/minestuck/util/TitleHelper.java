package com.mraof.minestuck.util;

import java.util.Random;

public final class TitleHelper {
	
	public static String[] classes = {"INVALID","ROGUE","THIEF","HEIR","MAID","PAGE","KNIGHT","SEER","MAGE","SYLPH","WITCH","BARD","PRINCE"};
	public static String[] aspects = {"INVALID","TIME","SPACE","VOID","LIGHT","MIND","HEART","RAGE","HOPE","DOOM","LIFE","BLOOD","BREATH"};
	public static Title randomTitle() {
		
		Random random = new Random();
		return new Title(random.nextInt(classes.length-1)+1,random.nextInt(aspects.length-1)+1);
		
	}
}
