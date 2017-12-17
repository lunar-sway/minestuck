package com.mraof.minestuck.item;

import com.mraof.minestuck.util.GristType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.TreeMap;

public class ItemMinestuckCandy extends ItemFood
{
	public static final String[] NAMES = new String[]{"candy_corn", "amber_gummy_worm", "amethyst_hard_candy", "artifact_war_head", "build_gushers",
			"caulk_pretzel", "chalk_candy_cigarette", "cobalt_gum", "diamond_mint", "garnet_twix", "gold_candy_ribbon",
			"iodine_licorice", "marble_jawbreaker", "mercury_sixlets", "quartz_jelly_bean", "ruby_lollipop", "rust_gummy_eye",
			"shale_peep", "sulfur_candy_apple", "tar_black_licorice", "uranium_gummy_bear", "zillium_skittles"};
	TreeMap<Integer, Candy> candyMap;
	Candy invalidCandy = new Candy(0, 0, "Invalid");

	public ItemMinestuckCandy()
	{
		super(0, 0, false);
		this.setHasSubtypes(true);
		this.setCreativeTab(MinestuckItems.tabMinestuck);
		this.setUnlocalizedName("candy");

		candyMap = new TreeMap<>();

		candyMap.put(0, new Candy(2, 0.3F, "Corn"));
	}
	
	public void updateCandy() {
		for (GristType type : GristType.REGISTRY.getValues())
		{
			float saturationModifier = type == GristType.Build ? 0.0F : 0.6F - type.getRarity(); //Perhaps change build to 0.1 or 0.05
			String name = type.getName();
			candyMap.put(type.getId() + 1, new Candy(2, saturationModifier, name.substring(0, 1).toUpperCase() + name.substring(1)));
		}
	}
	
	private Candy getCandy(int id) {
		return candyMap.getOrDefault(id, invalidCandy);
	}

	@Override
	public int getHealAmount(ItemStack stack)
	{
		return getCandy(stack.getItemDamage()).healAmount;
	}

	@Override
	public float getSaturationModifier(ItemStack stack)
	{
		return getCandy(stack.getItemDamage()).saturation;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return getCandy(stack.getItemDamage()).name;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (this.isInCreativeTab(tab))
			for (int id : candyMap.keySet())
				items.add(new ItemStack(this, 1, id));
	}

	private static class Candy
	{
		private final int healAmount;
		private final float saturation;
		private final String name;

		Candy(int healAmount, float saturation, String name)
		{
			this.healAmount = healAmount;
			this.saturation = saturation;
			this.name = "item.candy" + name;
		}
	}
}