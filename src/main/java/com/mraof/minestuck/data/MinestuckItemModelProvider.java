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
import net.minecraftforge.registries.ForgeRegistries;
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
		
		//Clubs
		handheldItem(MSItems.M_ACE_OF_CLUBS);
		handheldItem(MSItems.RUBIKS_MACE);
		handheldItem(MSItems.HOME_GROWN_MACE);
		
		handheldItemTextureName(MSItems.ACE_OF_CLUBS, "ace_clubs");
		
		//Staffs
		handheldItem(MSItems.WIZARD_STAFF);
		handheldItem(MSItems.WATER_STAFF);
		handheldItem(MSItems.FIRE_STAFF);
		
		simpleItem(MSItems.SCALEMATE_APPLESCAB);
		simpleItem(MSItems.SCALEMATE_BERRYBREATH);
		simpleItem(MSItems.SCALEMATE_CINNAMONWHIFF);
		simpleItem(MSItems.SCALEMATE_HONEYTONGUE);
		simpleItem(MSItems.SCALEMATE_LEMONSNOUT);
		simpleItem(MSItems.SCALEMATE_PINESNORT);
		simpleItem(MSItems.SCALEMATE_PUCEFOOT);
		simpleItem(MSItems.SCALEMATE_PUMPKINSNUFFLE);
		simpleItem(MSItems.SCALEMATE_PYRALSPITE);
		simpleItem(MSItems.SCALEMATE_WITNESS);
		simpleItem(MSItems.PLUSH_MUTATED_CAT);
		
		
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
	
	public ItemModelBuilder itemModelBlockItem(RegistryObject<Block> block)
	{
		return withExistingParent(block.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(Minestuck.MOD_ID, "block/" + block.getId().getPath()));
	}
	
	public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory")).texture("texture", new ResourceLocation(Minestuck.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
	}
	
	public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory")).texture("texture",  new ResourceLocation(Minestuck.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
	}
	
	public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory")).texture("wall",  new ResourceLocation(Minestuck.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
	}
}
