package com.mraof.minestuck.client.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockChessTile;
import com.mraof.minestuck.block.BlockMachine;
import com.mraof.minestuck.block.BlockStorage;
import com.mraof.minestuck.block.BlockChessTile.BlockType;
import com.mraof.minestuck.block.BlockColoredDirt;
import com.mraof.minestuck.util.Debug;

@SideOnly(Side.CLIENT)
public class MinestuckTextureManager
{
	
	/**
	 * Called during init.
	 * Tells the game which models that are used for different item states.
	 */
	public static void registerTextures()
	{
		Minestuck ms = Minestuck.instance;
		ItemModelMesher modelRegistry = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		
		//Items
		register(Minestuck.clawHammer);
		register(Minestuck.sledgeHammer);
		register(Minestuck.pogoHammer);
		register(Minestuck.telescopicSassacrusher);
		register(Minestuck.fearNoAnvil);
		register(Minestuck.popamaticVrillyhoo);
		register(Minestuck.scarletZillyhoo);
		
		register(Minestuck.sord);
		register(Minestuck.ninjaSword, 0, "katana");
		register(Minestuck.katana);
		register(Minestuck.caledscratch);
		register(Minestuck.royalDeringer);
		register(Minestuck.regisword);
		register(Minestuck.scarletRibbitar);
		register(Minestuck.doggMachete);
		
		register(Minestuck.sickle);
		register(Minestuck.homesSmellYaLater);
		register(Minestuck.regiSickle);
		register(Minestuck.clawSickle);
		
		register(Minestuck.deuceClub);
		
		register(Minestuck.cane);
		register(Minestuck.spearCane);
		register(Minestuck.dragonCane);
		
		modelRegistry.register(Minestuck.crockerSpork, new CrockerSporkDefinition());
		register(Minestuck.skaiaFork);
		
		if(MinestuckConfig.oldItemModels)
		{
			register(Minestuck.zillyhooHammer, 0, "zillyhoo_hammer_old");
		} else
		{
			register(Minestuck.zillyhooHammer);
		}
		
		register(Minestuck.rawCruxite);
		modelRegistry.register(Minestuck.cruxiteDowel, new CruxiteDowelDefinition());
		modelRegistry.register(Minestuck.captchaCard, new CaptchaCardDefinition());
		register(Minestuck.cruxiteArtifact, 0, "cruxite_apple");
		register(Minestuck.disk, 0, "disk_client");
		register(Minestuck.disk, 1, "disk_server");
		register(Minestuck.component, 0, "spoon_wood");
		register(Minestuck.component, 1, "spoon_silver");
		register(Minestuck.component, 2, "chessboard");
		register(Minestuck.minestuckBucket, 0, "bucket_blood");
		register(Minestuck.minestuckBucket, 1, "bucket_oil");
		register(Minestuck.minestuckBucket, 2, "bucket_brain_juice");
		for(int i = 0; i < Minestuck.modusCard.modusNames.length; i++)
			register(Minestuck.modusCard, i, "modus_" + Minestuck.modusCard.modusNames[i]);
		register(Minestuck.goldSeeds);
		for(int i = 0; i < Minestuck.metalBoat.names.length; i++)
			register(Minestuck.metalBoat, i, "boat_"+Minestuck.metalBoat.names[i]);
		
		//Blocks
		for(BlockChessTile.BlockType type : BlockChessTile.BlockType.values())
			register(Minestuck.chessTile, type.ordinal(), "chesstile_"+type.name);
		register(Minestuck.gatePortal);
		register(Minestuck.transportalizer);
		register(Minestuck.blockComputerOff);
		register(Minestuck.oreCruxite, 0, "cruxite_stone");
		register(Minestuck.oreCruxite, 1, "cruxite_netherrack");
		register(Minestuck.oreCruxite, 2, "cruxite_cobblestone");
		register(Minestuck.oreCruxite, 3, "cruxite_sandstone");
		for(BlockColoredDirt.BlockType type : BlockColoredDirt.BlockType.values())
			register(Minestuck.coloredDirt, type.ordinal(), "colored_dirt_"+type.name);
		for(BlockStorage.BlockType type : BlockStorage.BlockType.values())
			register(Minestuck.blockStorage, type.ordinal(), "storage_block_"+type.name);
		register(Minestuck.layeredSand);
		for(BlockMachine.MachineType type : BlockMachine.MachineType.values())
			register(Minestuck.blockMachine, type.ordinal(), "machine_"+type.getName());
		
		//Register block states
		BlockModelShapes blockModelRegistry = modelRegistry.getModelManager().getBlockModelShapes();
		//Blocks are registered here that got state properties that doesn't affect rendering, or if some properties should have their own blockstates files.
	}
	
	/**
	 * Called during pre-init after the blocks and items have been both created and registered.
	 * Tells which models that should be loaded for the different items.
	 */
	public static void registerVariants()
	{
		//Items
		ModelBakery.addVariantName(Minestuck.crockerSpork, "minestuck:crocker_fork", "minestuck:crocker_spoon");
		ModelBakery.addVariantName(Minestuck.cruxiteDowel, "minestuck:dowel_uncarved", "minestuck:dowel_carved");
		ModelBakery.addVariantName(Minestuck.cruxiteArtifact, "minestuck:cruxite_apple");
		ModelBakery.addVariantName(Minestuck.disk, "minestuck:disk_client", "minestuck:disk_server");
		ModelBakery.addVariantName(Minestuck.component, "minestuck:spoon_wood", "minestuck:spoon_silver", "minestuck:chessboard");
		ModelBakery.addVariantName(Minestuck.minestuckBucket, "minestuck:bucket_blood", "minestuck:bucket_oil", "minestuck:bucket_brain_juice");
		ModelBakery.addVariantName(Minestuck.captchaCard, "minestuck:card_empty", "minestuck:card_full", "minestuck:card_punched");
		ModelBakery.addVariantName(Minestuck.ninjaSword, "minestuck:katana");	//To prevent the game to try to load "minestuck:ninja_sword"
		if(MinestuckConfig.oldItemModels)
		{
			ModelBakery.addVariantName(Minestuck.zillyhooHammer, "minestuck:zillyhoo_hammer_old");
		}
		
		String[] str = new String[Minestuck.modusCard.modusNames.length];
		for(int i = 0; i < str.length; i++)
			str[i] = "minestuck:modus_"+Minestuck.modusCard.modusNames[i];
		ModelBakery.addVariantName(Minestuck.modusCard, str);
		for(String s : Minestuck.metalBoat.names)
			ModelBakery.addVariantName(Minestuck.metalBoat, "minestuck:boat_"+s);
		
		//Blocks
		for(BlockChessTile.BlockType type : BlockChessTile.BlockType.values())
			ModelBakery.addVariantName(Item.getItemFromBlock(Minestuck.chessTile), "minestuck:chesstile_"+type.name);
		ModelBakery.addVariantName(Item.getItemFromBlock(Minestuck.oreCruxite), "minestuck:cruxite_stone", "minestuck:cruxite_netherrack", "minestuck:cruxite_cobblestone", "minestuck:cruxite_sandstone");
		for(BlockColoredDirt.BlockType type : BlockColoredDirt.BlockType.values())
			ModelBakery.addVariantName(Item.getItemFromBlock(Minestuck.coloredDirt), "minestuck:colored_dirt_"+type.name);
		for(BlockStorage.BlockType type : BlockStorage.BlockType.values())
			ModelBakery.addVariantName(Item.getItemFromBlock(Minestuck.blockStorage), "minestuck:storage_block_"+type.name);
		for(BlockMachine.MachineType type : BlockMachine.MachineType.values())
			ModelBakery.addVariantName(Item.getItemFromBlock(Minestuck.blockMachine), "minestuck:machine_"+type.getName());
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
			return new ModelResourceLocation("minestuck:"+(Minestuck.crockerSpork.isSpoon(stack) ? "crocker_spoon" : "crocker_fork"), "inventory");
		}
	}
	
	private static class CruxiteDowelDefinition implements ItemMeshDefinition
	{
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack)
		{
			return new ModelResourceLocation("minestuck:"+(stack.hasTagCompound() && stack.getTagCompound().hasKey("contentID") ? "dowel_carved" : "dowel_uncarved"), "inventory");
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
				if(nbt.getBoolean("punched") && !(Item.itemRegistry.getObject(new ResourceLocation(nbt.getString("contentID"))) == Item.getItemFromBlock(Minestuck.blockStorage)
						&& nbt.getInteger("contentMeta") == 1))
					str = "card_punched";
				else str = "card_full";
			}
			else str = "card_empty";
			return new ModelResourceLocation("minestuck:" + str, "inventory");
		}
	}
	
}
