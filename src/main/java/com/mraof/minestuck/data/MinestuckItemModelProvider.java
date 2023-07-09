package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class MinestuckItemModelProvider extends ItemModelProvider {
	public MinestuckItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Minestuck.MOD_ID, existingFileHelper);}
	
	@Override
	protected void registerModels() {
		
		handheldItemTextureName(MSItems.ACE_OF_CLUBS, "ace_clubs");
		
		//Blocks
		blockItem(MSBlocks.UNCARVED_WOOD);
		
	}
	
	private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
		return withExistingParent(item.getId().getPath(),
				new ResourceLocation("item/generated")).texture("layer0",
				new ResourceLocation(Minestuck.MOD_ID,"item/" + item.getId().getPath()));}
	private ItemModelBuilder simpleItemTextureName(RegistryObject<Item> item, String textureName) {
		return withExistingParent(item.getId().getPath(),
				new ResourceLocation("item/generated")).texture("layer0",
				new ResourceLocation(Minestuck.MOD_ID,"item/" + textureName));}
	private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
		return withExistingParent(item.getId().getPath(),
				new ResourceLocation("item/handheld")).texture("layer0",
				new ResourceLocation(Minestuck.MOD_ID,"item/" + item.getId().getPath()));}
	private ItemModelBuilder handheldItemTextureName(RegistryObject<Item> item, String textureName) {
		return withExistingParent(item.getId().getPath(),
				new ResourceLocation("item/handheld")).texture("layer0",
				new ResourceLocation(Minestuck.MOD_ID,"item/" + textureName));}
	private ItemModelBuilder blockItem(RegistryObject<Block> block) {
		return withExistingParent(block.getId().getPath(),
				new ResourceLocation("minestuck:block/" + block.getId().getPath()));
	}
}
