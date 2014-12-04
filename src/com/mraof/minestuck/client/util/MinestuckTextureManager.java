package com.mraof.minestuck.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;

@SideOnly(Side.CLIENT)
public class MinestuckTextureManager
{
	
	public static void registerTextures()
	{
		Minestuck ms = Minestuck.instance;
		ItemModelMesher modelRegistry = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		
		
		register(Minestuck.clawHammer);
		register(Minestuck.sledgeHammer);
		register(Minestuck.pogoHammer);
		register(Minestuck.telescopicSassacrusher);
		register(Minestuck.fearNoAnvil);
		register(Minestuck.zillyhooHammer);
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
		for(int i = 0; i < Minestuck.captchaModus.modusNames.length; i++)
			register(Minestuck.captchaModus, i, "modus_" + Minestuck.captchaModus.modusNames[i]);
	}
	
	public static void registerVariants()
	{
		ModelBakery.addVariantName(Minestuck.crockerSpork, "minestuck:crocker_fork", "minestuck:crocker_spoon");
		ModelBakery.addVariantName(Minestuck.cruxiteDowel, "minestuck:dowel_uncarved", "minestuck:dowel_carved");
		ModelBakery.addVariantName(Minestuck.cruxiteArtifact, "minestuck:cruxite_apple");
		ModelBakery.addVariantName(Minestuck.disk, "minestuck:disk_client", "minestuck:disk_server");
		ModelBakery.addVariantName(Minestuck.component, "minestuck:spoon_wood", "minestuck:spoon_silver", "minestuck:chessboard");
		ModelBakery.addVariantName(Minestuck.minestuckBucket, "minestuck:bucket_blood", "minestuck:bucket_oil", "minestuck:bucket_brain_juice");
		ModelBakery.addVariantName(Minestuck.captchaCard, "minestuck:card_empty", "minestuck:card_full", "minestuck:card_punched");
		ModelBakery.addVariantName(Minestuck.ninjaSword, "minestuck:katana");	//To prevent the game to try to load "minestuck:ninja_sword"
		
		String[] str = new String[Minestuck.captchaModus.modusNames.length];
		for(int i = 0; i < str.length; i++)
			str[i] = "minestuck:modus_"+Minestuck.captchaModus.modusNames[i];
		ModelBakery.addVariantName(Minestuck.captchaModus, str);
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
