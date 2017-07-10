package com.mraof.minestuck.client.util;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.*;
import com.mraof.minestuck.item.ItemMinestuckCandy;
import com.mraof.minestuck.item.weapon.ItemDualWeapon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.Map;

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

		register(sledgeHammer);
		register(blacksmithHammer);
		register(pogoHammer);
		register(telescopicSassacrusher);
		register(fearNoAnvil);
		register(popamaticVrillyhoo);
		register(scarletZillyhoo);
		register(mwrthwl);
		
		register(sord);
		register(cactusCutlass);
		register(katana);
		register(unbreakableKatana, 0, "katana");
		register(firePoker);
		register(hotHandle);
		register(caledscratch);
		register(caledfwlch);
		register(royalDeringer);
		register(zillywairCutlass);
		register(regisword);
		register(scarletRibbitar);
		register(doggMachete);
		register(cobaltSabre);
		
		register(blacksmithBane);
		register(scraxe);
		register(rubyCroak);
		register(hephaestusLumber);
		register(copseCrusher);
		register(qPHammerAxe);
		
		
		register(dice);
		register(fluoriteOctet);
		
		modelRegistry.register(CatClaws, new DualWeaponDefinition(CatClaws));
		
		register(sickle);
		register(homesSmellYaLater);
		register(fudgeSickle);
		register(regiSickle);
		register(clawSickle);
		register(candySickle);
		
		register(deuceClub);
		register(nightClub);
		register(pogoClub);
		register(metalBat);
		register(spikedClub);
		
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
			register(clawHammer, 0, "claw_hammer_old");
			register(zillyhooHammer, 0, "zillyhoo_hammer_old");
		} else
		{
			//register(clawHammer);
			register(clawHammer, 0, "claw_hammer_old");	//Until the issues with the model are fixed
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
		modelRegistry.register(cruxitePotion, new ColoredItemDefinition("minestuck:cruxite_potion"));
		register(disk, 0, "disk_client");
		register(disk, 1, "disk_server");
		register(chessboard);
		register(minestuckBucket, 0, "bucket_oil");
		register(minestuckBucket, 1, "bucket_blood");
		register(minestuckBucket, 2, "bucket_brain_juice");
		for(int i = 0; i < modusCard.modusNames.length; i++)
			register(modusCard, i, "modus_" + modusCard.modusNames[i]);
		register(goldSeeds);
		for(int i = 0; i < metalBoat.names.length; i++)
			register(metalBoat, i, "boat_" + metalBoat.names[i]);
		register(obsidianBucket);
		for(int i = 0; i < ItemMinestuckCandy.modelNames.length; i++)
			register(candy, i, ItemMinestuckCandy.modelNames[i]);
		register(bugOnAStick);
		register(chocolateBeetle);
		register(coneOfFlies);
		register(grasshopper);
		register(jarOfBugs);
		register(onion);
		register(salad);
		register(threshDvd);
		register(crewPoster);
		register(sbahjPoster);
		register(carvingTool);
		register(crumplyHat);
		register(frogStatueReplica);
		register(stoneSlab);
		register(recordEmissaryOfDance);
		register(recordDanceStab);
		
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
		register(cruxiteBlock);
		register(genericObject);
		register(coalOreNetherrack);
		register(ironOreSandstone);
		register(ironOreSandstoneRed);
		register(goldOreSandstone);
		register(goldOreSandstoneRed);
		for(BlockColoredDirt.BlockType type : BlockColoredDirt.BlockType.values())
			register(coloredDirt, type.ordinal(), "colored_dirt_"+type.name);
		register(layeredSand);
		for(BlockSburbMachine.MachineType type : BlockSburbMachine.MachineType.values())
			register(sburbMachine, type.ordinal(), "machine_"+type.getName());
		for(BlockCrockerMachine.MachineType type : BlockCrockerMachine.MachineType.values())
			register(crockerMachine, type.ordinal(), "machine_"+type.getName());
		register(glowingMushroom);
		register(glowingLog);
		register(glowingPlanks);
		for(BlockMinestuckStone.BlockType type : BlockMinestuckStone.BlockType.values())
			register(stone, type.getMetadata(), type.getName());
		register(coarseStoneStairs);
		register(shadeBrickStairs);
		register(frostBrickStairs);
		register(castIronStairs);
		for(BlockMinestuckLog.BlockType type : BlockMinestuckLog.BlockType.values())
			register(log, type.ordinal(), type.getName()+"_log");
		register(woodenCactus);
		
		register(primedTnt);
		register(unstableTnt);
		register(instantTnt);
		register(woodenExplosiveButton);
		register(stoneExplosiveButton);
		
	}
	
	/**
	 * Called during pre-init after the blocks and items have been both created and registered.
	 * Tells which models that should be loaded for the different items.
	 */
	public static void registerVariants()
	{
		//Items
		ModelBakery.registerItemVariants(crockerSpork, new ResourceLocation("minestuck:crocker_fork"), new ResourceLocation("minestuck:crocker_spoon"));
		ModelBakery.registerItemVariants(CatClaws,new ResourceLocation("minestuck:catclaws_sheathed"), new ResourceLocation("minestuck:catclaws_drawn"));
		ModelBakery.registerItemVariants(cruxiteDowel, new ResourceLocation("minestuck:dowel_uncarved"), new ResourceLocation("minestuck:dowel_carved"), new ResourceLocation("minestuck:dowel_uncarved_blank"), new ResourceLocation("minestuck:dowel_carved_blank"));
		ModelBakery.registerItemVariants(cruxiteApple, new ResourceLocation("minestuck:cruxite_apple"), new ResourceLocation("minestuck:cruxite_apple_blank"));
		ModelBakery.registerItemVariants(cruxitePotion, new ResourceLocation("minestuck:cruxite_potion"), new ResourceLocation("minestuck:cruxite_potion_blank"));
		ModelBakery.registerItemVariants(disk, new ResourceLocation("minestuck:disk_client"), new ResourceLocation("minestuck:disk_server"));
		ModelBakery.registerItemVariants(minestuckBucket, new ResourceLocation("minestuck:bucket_blood"), new ResourceLocation("minestuck:bucket_oil"), new ResourceLocation("minestuck:bucket_brain_juice"));
		ModelBakery.registerItemVariants(captchaCard, new ResourceLocation("minestuck:card_empty"), new ResourceLocation("minestuck:card_full"), new ResourceLocation("minestuck:card_punched"));
		ModelBakery.registerItemVariants(unbreakableKatana, new ResourceLocation("minestuck:katana"));
		if(MinestuckConfig.oldItemModels)
		{
			ModelBakery.registerItemVariants(zillyhooHammer, new ResourceLocation("minestuck:zillyhoo_hammer_old"));
		}
		
		ResourceLocation[] resLoc = new ResourceLocation[modusCard.modusNames.length];
		for(int i = 0; i < resLoc.length; i++)
			resLoc[i] = new ResourceLocation("minestuck:modus_" + modusCard.modusNames[i]);
		ModelBakery.registerItemVariants(modusCard, resLoc);
		for(String s : metalBoat.names)
			ModelBakery.registerItemVariants(metalBoat, new ResourceLocation("minestuck:boat_" + s));
		for(String s : ItemMinestuckCandy.modelNames)
			ModelBakery.registerItemVariants(candy, new ResourceLocation("minestuck:"+s));
		
		//Blocks
		for(BlockChessTile.BlockType type : BlockChessTile.BlockType.values())
			ModelBakery.registerItemVariants(Item.getItemFromBlock(chessTile), new ResourceLocation("minestuck:chesstile_"+type.name));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(oreCruxite), new ResourceLocation("minestuck:cruxite_stone"), new ResourceLocation("minestuck:cruxite_netherrack"), new ResourceLocation("minestuck:cruxite_cobblestone"), new ResourceLocation("minestuck:cruxite_sandstone"), new ResourceLocation("minestuck:cruxite_sandstone_red"));
		for(BlockColoredDirt.BlockType type : BlockColoredDirt.BlockType.values())
			ModelBakery.registerItemVariants(Item.getItemFromBlock(coloredDirt), new ResourceLocation("minestuck:colored_dirt_"+type.name));
		for(BlockSburbMachine.MachineType type : BlockSburbMachine.MachineType.values())
			ModelBakery.registerItemVariants(Item.getItemFromBlock(sburbMachine), new ResourceLocation("minestuck:machine_"+type.getName()));
		for(BlockCrockerMachine.MachineType type : BlockCrockerMachine.MachineType.values())
			ModelBakery.registerItemVariants(Item.getItemFromBlock(crockerMachine), new ResourceLocation("minestuck:machine_"+type.getName()));
		for(BlockMinestuckStone.BlockType type : BlockMinestuckStone.BlockType.values())
			ModelBakery.registerItemVariants(Item.getItemFromBlock(stone), new ResourceLocation("minestuck:"+type.getName()));
		for(BlockMinestuckLog.BlockType type : BlockMinestuckLog.BlockType.values())
			ModelBakery.registerItemVariants(Item.getItemFromBlock(log), new ResourceLocation("minestuck:"+type.getName()+"_log"));
		
		ModelLoader.setCustomStateMapper(blockOil, (new StateMap.Builder()).ignore(BlockFluidBase.LEVEL).build());
		ModelLoader.setCustomStateMapper(blockBlood, (new StateMap.Builder()).ignore(BlockFluidBase.LEVEL).build());
		ModelLoader.setCustomStateMapper(blockBrainJuice, (new StateMap.Builder()).ignore(BlockFluidBase.LEVEL).build());
		ModelLoader.setCustomStateMapper(primedTnt, (new StateMap.Builder()).ignore(BlockTNT.EXPLODE).build());
		ModelLoader.setCustomStateMapper(unstableTnt, (new StateMap.Builder()).ignore(BlockTNT.EXPLODE).build());
		ModelLoader.setCustomStateMapper(instantTnt, (new StateMap.Builder()).ignore(BlockTNT.EXPLODE).build());
		ModelLoader.setCustomStateMapper(log, (new StateMap.Builder()).withName(BlockMinestuckLog.VARIANT).withSuffix("_log").build());
		ModelLoader.setCustomStateMapper(woodenCactus, new StateMap.Builder().ignore(BlockCactus.AGE).build());
		ModelLoader.setCustomStateMapper(returnNode, new IStateMapper()
		{
			@Override
			public Map putStateModelLocations(Block block)
			{
				return Collections.emptyMap();	//We're not using any models for rendering the return node
			}});
		ModelLoader.setCustomStateMapper(gate, new IStateMapper()
		{
			@Override
			public Map putStateModelLocations(Block block)
			{
				return Collections.emptyMap();
			}});
	}
	
	private static void register(Item item)
	{
		ItemModelMesher modelRegistry = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		modelRegistry.register(item, 0, new ModelResourceLocation((ResourceLocation) Item.REGISTRY.getNameForObject(item), "inventory"));
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
	
	
	private static class DualWeaponDefinition implements ItemMeshDefinition
	{
		private Item item;
		public DualWeaponDefinition(Item item){
			this.item=item;
		}
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack)
		{
			if(((ItemDualWeapon)this.item).IsDrawn(stack)){
				
				return new ModelResourceLocation("minestuck:" + ((ItemDualWeapon)this.item).Prefex + "_drawn","inventory");
			}else
				return new ModelResourceLocation("minestuck:" + ((ItemDualWeapon)this.item).Prefex + "_sheathed","inventory");
			
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
				if(nbt.getBoolean("punched") && !(Item.REGISTRY.getObject(new ResourceLocation(nbt.getString("contentID"))) == Item.getItemFromBlock(genericObject)))
					str = "card_punched";
				else str = "card_full";
			}
			else str = "card_empty";
			return new ModelResourceLocation("minestuck:" + str, "inventory");
		}
	}
}
