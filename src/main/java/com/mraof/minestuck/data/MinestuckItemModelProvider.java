package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class MinestuckItemModelProvider extends ItemModelProvider
{
	public MinestuckItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper)
	{
		super(output, Minestuck.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void registerModels()
	{
		
		handheldItemTextureName(MSItems.ACE_OF_CLUBS, "ace_clubs");

		//Staffs
		handheldItemTextureName(MSItems.WIZARD_STAFF, "wizard_staff");
		handheldItemTextureName(MSItems.WATER_STAFF, "water_staff");
		handheldItemTextureName(MSItems.FIRE_STAFF, "fire_staff");
		
		//Clubs
		handheldItem(MSItems.M_ACE_OF_CLUBS);
		handheldItem(MSItems.HOME_GROWN_MACE);
		handheldItemTextureName(MSItems.RUBIKS_MACE, "rubiks_mace");
		
	}
	
	private ItemModelBuilder simpleItem(RegistryObject<Item> item)
	{
		return withExistingParent(item.getId().getPath(),
				new ResourceLocation("item/generated")).texture("layer0",
				new ResourceLocation(Minestuck.MOD_ID, "item/" + item.getId().getPath()));
	}
	
	private ItemModelBuilder simpleItemTextureName(RegistryObject<Item> item, String textureName)
	{
		return withExistingParent(item.getId().getPath(),
				new ResourceLocation("item/generated")).texture("layer0",
				new ResourceLocation(Minestuck.MOD_ID, "item/" + textureName));
	}
	
	private ItemModelBuilder handheldItem(RegistryObject<Item> item)
	{
		return withExistingParent(item.getId().getPath(),
				new ResourceLocation("item/handheld")).texture("layer0",
				new ResourceLocation(Minestuck.MOD_ID, "item/" + item.getId().getPath()));
	}
	
	private ItemModelBuilder handheldItemTextureName(RegistryObject<Item> item, String textureName)
	{
		return withExistingParent(item.getId().getPath(),
				new ResourceLocation("item/handheld")).texture("layer0",
				new ResourceLocation(Minestuck.MOD_ID, "item/" + textureName));
	}
	
	private ItemModelBuilder blockItem(RegistryObject<Block> block)
	{
		return withExistingParent(block.getId().getPath(),
				new ResourceLocation("minestuck:block/" + block.getId().getPath()));
	}
}
