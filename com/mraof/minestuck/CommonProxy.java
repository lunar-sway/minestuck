package com.mraof.minestuck;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
	public static String MINESTUCKFOLDER = "/com/mraof/minestuck/";
    
    
    // Client stuff
    public static void registerRenderers()
    {
            // Nothing here as the server doesn't render graphics!
    }
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
    {
    	return null;
    }
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
    {
		return null;
	}
    
}