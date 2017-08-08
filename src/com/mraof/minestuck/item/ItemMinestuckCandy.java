package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.GristType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemMinestuckCandy extends ItemFood
{
	public static String[] modelNames = new String[]{"candy_corn","amber_gummy_worm","amethyst_hard_candy","artifact_war_head","build_gusher",
		"caulk_pretzel","chalk_candy_cigarette","cobalt_gum","diamond_mint","garnet_twix","gold_candy_ribbon",
		"iodine_licorice","marble_jawbreaker","mercury_sixlets","quartz_jelly_bean","ruby_lollipop","rust_gummy_eye",
		"shale_peep","sulfur_candy_apple","tar_black_licorice","uranium_gummy_bear","zillium_skittles"};
	
	private int[] healAmounts;
	private float[] saturationModifiers;
	private String[] unlocalizedNames;
	
	public ItemMinestuckCandy()
	{
		super(0, 0, false);
		this.setHasSubtypes(true);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("candy");
		
		healAmounts = new int[22];
		saturationModifiers = new float[22];
		unlocalizedNames = new String[22];
		
		for(int i = 0; i < 22; i++)
			healAmounts[i] = 2;
		
		saturationModifiers[0] = 0.3F;
		unlocalizedNames[0] = "item.candyCorn";
		for(int i = 0; i < 21; i++)
		{
			GristType type = GristType.values()[i];
			if(type == GristType.Build) saturationModifiers[i + 1] = 0.0F;	//Perhaps change to 0.1 or 0.05
			else saturationModifiers[i + 1] = 0.6F - type.getRarity();
			String name = type.getName();
			unlocalizedNames[i + 1] = "item.candy"+(name.substring(0, 1).toUpperCase() + name.substring(1));
		}
	}
	
	@Override
	public int getHealAmount(ItemStack stack)
	{
		return healAmounts[stack.getItemDamage() % healAmounts.length];
	}
	
	@Override
	public float getSaturationModifier(ItemStack stack)
	{
		return saturationModifiers[stack.getItemDamage() % saturationModifiers.length];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return unlocalizedNames[stack.getItemDamage()];
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		for(int i = 0; i < 22; i++)
			items.add(new ItemStack(this, 1, i));
	}
}