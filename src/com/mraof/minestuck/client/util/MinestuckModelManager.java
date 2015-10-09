package com.mraof.minestuck.client.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockChessTile;
import com.mraof.minestuck.block.BlockMachine;
import com.mraof.minestuck.block.BlockStorage;
import com.mraof.minestuck.block.BlockColoredDirt;

import static com.mraof.minestuck.block.MinestuckBlocks.*;
import static com.mraof.minestuck.item.MinestuckItems.*;

@SideOnly(Side.CLIENT)
public class MinestuckModelManager
{
	
	/**
	 * Called during init.
	 * Tells the game which models that are used for different item states.
	 */
	public static void registerTextures()
	{
		ItemModelMesher modelRegistry = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		
		//Items
		register(clawHammer);
		register(sledgeHammer);
		register(blacksmithHammer);
		register(pogoHammer);
		register(telescopicSassacrusher);
		register(fearNoAnvil);
		register(popamaticVrillyhoo);
		register(scarletZillyhoo);
		
		register(sord);
		register(ninjaSword, 0, "katana");
		register(katana);
		register(caledscratch);
		register(royalDeringer);
		register(regisword);
		register(scarletRibbitar);
		register(doggMachete);
		
		register(dice);
		register(fluoriteOctet);
		
		register(sickle);
		register(homesSmellYaLater);
		register(regiSickle);
		register(clawSickle);
		
		register(deuceClub);
		
		register(cane);
		register(spearCane);
		register(dragonCane);
		
		register(woodenSpoon);
		register(silverSpoon);
		modelRegistry.register(crockerSpork, new CrockerSporkDefinition());
		register(skaiaFork);
		register(fork);
		register(spork);
		
		if(MinestuckConfig.oldItemModels)
		{
			register(zillyhooHammer, 0, "zillyhoo_hammer_old");
		} else
		{
			register(zillyhooHammer);
		}
		
		register(emeraldSword);
		register(emeraldAxe);
		register(emeraldPickaxe);
		register(emeraldShovel);
		register(emeraldHoe);
		
		register(prismarineHelmet);
		register(prismarineChestplate);
		register(prismarineLeggings);
		register(prismarineBoots);
		
		register(rawCruxite);
		modelRegistry.register(cruxiteDowel, new CruxiteDowelDefinition());
		modelRegistry.register(captchaCard, new CaptchaCardDefinition());
		modelRegistry.register(cruxiteApple, new ColoredItemDefinition("minestuck:cruxite_apple"));
		register(disk, 0, "disk_client");
		register(disk, 1, "disk_server");
		register(component, 0, "spoon_wood");
		register(component, 1, "spoon_silver");
		register(component, 2, "chessboard");
		register(minestuckBucket, 0, "bucket_oil");
		register(minestuckBucket, 1, "bucket_blood");
		register(minestuckBucket, 2, "bucket_brain_juice");
		for(int i = 0; i < modusCard.modusNames.length; i++)
			register(modusCard, i, "modus_" + modusCard.modusNames[i]);
		register(goldSeeds);
		for(int i = 0; i < metalBoat.names.length; i++)
			register(metalBoat, i, "boat_" + metalBoat.names[i]);
		register(obsidianBucket);
		
		
		//Blocks
		for(BlockChessTile.BlockType type : BlockChessTile.BlockType.values())
			register(chessTile, type.ordinal(), "chesstile_"+type.name);
		register(skaiaPortal);
		register(transportalizer);
		register(blockComputerOff);
		register(oreCruxite, 0, "cruxite_stone");
		register(oreCruxite, 1, "cruxite_netherrack");
		register(oreCruxite, 2, "cruxite_cobblestone");
		register(oreCruxite, 3, "cruxite_sandstone");
		register(oreCruxite, 4, "cruxite_sandstone_red");
		for(BlockColoredDirt.BlockType type : BlockColoredDirt.BlockType.values())
			register(coloredDirt, type.ordinal(), "colored_dirt_"+type.name);
		for(BlockStorage.BlockType type : BlockStorage.BlockType.values())
			register(blockStorage, type.ordinal(), "storage_block_"+type.name);
		register(layeredSand);
		for(BlockMachine.MachineType type : BlockMachine.MachineType.values())
			register(blockMachine, type.ordinal(), "machine_"+type.getName());
		register(glowingMushroom);
		
	}
	
	/**
	 * Called during pre-init after the blocks and items have been both created and registered.
	 * Tells which models that should be loaded for the different items.
	 */
	public static void registerVariants()
	{
		//Items
		ModelBakery.addVariantName(crockerSpork, "minestuck:crocker_fork", "minestuck:crocker_spoon");
		ModelBakery.addVariantName(cruxiteDowel, "minestuck:dowel_uncarved", "minestuck:dowel_carved", "minestuck:dowel_uncarved_blank", "minestuck:dowel_carved_blank");
		ModelBakery.addVariantName(cruxiteApple, "minestuck:cruxite_apple", "minestuck:cruxite_apple_blank");
		ModelBakery.addVariantName(disk, "minestuck:disk_client", "minestuck:disk_server");
		ModelBakery.addVariantName(component, "minestuck:spoon_wood", "minestuck:spoon_silver", "minestuck:chessboard");
		ModelBakery.addVariantName(minestuckBucket, "minestuck:bucket_blood", "minestuck:bucket_oil", "minestuck:bucket_brain_juice");
		ModelBakery.addVariantName(captchaCard, "minestuck:card_empty", "minestuck:card_full", "minestuck:card_punched");
		ModelBakery.addVariantName(ninjaSword, "minestuck:katana");	//To prevent the game to try to load "minestuck:ninja_sword"
		if(MinestuckConfig.oldItemModels)
		{
			ModelBakery.addVariantName(zillyhooHammer, "minestuck:zillyhoo_hammer_old");
		}
		
		String[] str = new String[modusCard.modusNames.length];
		for(int i = 0; i < str.length; i++)
			str[i] = "minestuck:modus_" + modusCard.modusNames[i];
		ModelBakery.addVariantName(modusCard, str);
		for(String s : metalBoat.names)
			ModelBakery.addVariantName(metalBoat, "minestuck:boat_" + s);
		
		//Blocks
		for(BlockChessTile.BlockType type : BlockChessTile.BlockType.values())
			ModelBakery.addVariantName(Item.getItemFromBlock(chessTile), "minestuck:chesstile_"+type.name);
		ModelBakery.addVariantName(Item.getItemFromBlock(oreCruxite), "minestuck:cruxite_stone", "minestuck:cruxite_netherrack", "minestuck:cruxite_cobblestone", "minestuck:cruxite_sandstone", "minestuck:cruxite_sandstone_red");
		for(BlockColoredDirt.BlockType type : BlockColoredDirt.BlockType.values())
			ModelBakery.addVariantName(Item.getItemFromBlock(coloredDirt), "minestuck:colored_dirt_"+type.name);
		for(BlockStorage.BlockType type : BlockStorage.BlockType.values())
			ModelBakery.addVariantName(Item.getItemFromBlock(blockStorage), "minestuck:storage_block_"+type.name);
		for(BlockMachine.MachineType type : BlockMachine.MachineType.values())
			ModelBakery.addVariantName(Item.getItemFromBlock(blockMachine), "minestuck:machine_"+type.getName());
		
		ModelLoader.setCustomStateMapper(blockOil, (new StateMap.Builder()).addPropertiesToIgnore(BlockFluidBase.LEVEL).build());
		ModelLoader.setCustomStateMapper(blockBlood, (new StateMap.Builder()).addPropertiesToIgnore(BlockFluidBase.LEVEL).build());
		ModelLoader.setCustomStateMapper(blockBrainJuice, (new StateMap.Builder()).addPropertiesToIgnore(BlockFluidBase.LEVEL).build());
		ModelLoader.setCustomStateMapper(returnNode, new IStateMapper()
		{
			@Override
			public Map putStateModelLocations(Block block)
			{
				return new HashMap();	//We're not using any models for rendering the return node
			}});
		ModelLoader.setCustomStateMapper(gate, new IStateMapper()
		{
			@Override
			public Map putStateModelLocations(Block block)
			{
				return new HashMap();
			}});
	}
	
	private static void register(Item item)
	{
		ItemModelMesher modelRegistry = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		modelRegistry.register(item, 0, new ModelResourceLocation((ResourceLocation) Item.itemRegistry.getNameForObject(item), "inventory"));
	}
	
	private static void register(Item item, int meta, String modelResource)
	{
		ItemModelMesher modelRegistry = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		modelRegistry.register(item, meta, new ModelResourceLocation("minestuck:"+modelResource, "inventory"));
	}
	
	private static void register(Block block)
	{
		register(Item.getItemFromBlock(block));
	}
	
	private static void register(Block block, int meta, String modelResource)
	{
		register(Item.getItemFromBlock(block), meta, modelResource);
	}
	
	private static class CrockerSporkDefinition implements ItemMeshDefinition
	{
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack)
		{
			return new ModelResourceLocation("minestuck:" + (crockerSpork.isSpoon(stack) ? "crocker_spoon" : "crocker_fork"), "inventory");
		}
	}
	
	private static class CruxiteDowelDefinition implements ItemMeshDefinition
	{
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack)
		{
			String suffix = stack.getMetadata() == 0 ? "" : "_blank";
			return new ModelResourceLocation("minestuck:"+(stack.hasTagCompound() && stack.getTagCompound().hasKey("contentID") ? "dowel_carved" : "dowel_uncarved")+suffix, "inventory");
		}
	}
	
	private static class ColoredItemDefinition implements ItemMeshDefinition
	{
		private String name;
		ColoredItemDefinition(String name)
		{
			this.name = name;
		}
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack)
		{
			if(stack.getMetadata() == 0)
				return new ModelResourceLocation(name, "inventory");
			else return new ModelResourceLocation(name + "_blank", "inventory");
		}
	}
	
	private static class CaptchaCardDefinition implements ItemMeshDefinition
	{
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack)
		{
			NBTTagCompound nbt = stack.getTagCompound();
			String str;
			if(nbt != null && nbt.hasKey("contentID"))
			{
				if(nbt.getBoolean("punched") && !(Item.itemRegistry.getObject(new ResourceLocation(nbt.getString("contentID"))) == Item.getItemFromBlock(blockStorage)
						&& nbt.getInteger("contentMeta") == 1))
					str = "card_punched";
				else str = "card_full";
			}
			else str = "card_empty";
			return new ModelResourceLocation("minestuck:" + str, "inventory");
		}
	}
}