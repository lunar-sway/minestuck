package com.mraof.minestuck.item;

import static com.mraof.minestuck.MinestuckConfig.artifactRange;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MinestuckCriteriaTriggers;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Entryfier;
import com.mraof.minestuck.util.Entryfier.EntryPrepResult;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class ItemCruxiteArtifact extends Item implements Teleport.ITeleporter
{
	private Entryfier entryfier;
	
	public ItemCruxiteArtifact() 
	{
		this.setCreativeTab(TabMinestuck.instance);
		setUnlocalizedName("cruxiteArtifact");
		this.maxStackSize = 1;
		setHasSubtypes(true);
		entryfier = new Entryfier();
	}
	
	public void onArtifactActivated(EntityPlayer player)
	{
		try
		{
			if(!player.world.isRemote && player.world.provider.getDimension() != -1)
			{
				if(!SburbHandler.shouldEnterNow(player))
					return;
				
				SburbConnection c = SkaianetHandler.getMainConnection(IdentifierHandler.encode(player), true);
				
				//Only preforms Entry if you have no connection, haven't Entered, or you're not in a Land and additional Entries are permitted.
				if(c == null || !c.enteredGame() || !MinestuckConfig.stopSecondEntry && !MinestuckDimensionHandler.isLandDimension(player.world.provider.getDimension()))
				{
					if(!canModifyEntryBlocks(player.world, player))
					{
						player.sendMessage(new TextComponentString("You are not allowed to enter here."));
						return;
					}
					
					if(c != null && c.enteredGame())
					{
						World newWorld = player.getServer().getWorld(c.getClientDimension());
						if(newWorld == null)
						{
							return;
						}
						
						//Teleports the player to their home in the Medium, without any bells or whistles.
						BlockPos pos = newWorld.provider.getRandomizedSpawnPoint();
						Teleport.teleportEntity(player, c.getClientDimension(), null, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F);
						
						return;
					}
					
					//Teleportation code is now called from enterMedium(), which is called from createLand.
					//createLand will return -1 if Entry fails for any reason, including the teleporter being null or returning false in prepareDestination().
					//Whatever the problem is, relevant information should be printed to the console.
					if(LandAspectRegistry.createLand(player, this) == -1)
					{
						player.sendMessage(new TextComponentString("Something went wrong creating your Land. More details in the server console."));
					}
					else
					{
						c = SburbHandler.getConnectionForDimension(player.dimension);
						if(c != null && c.getClientIdentifier().equals(IdentifierHandler.encode(player)))
						{
							MinestuckCriteriaTriggers.CRUXITE_ARTIFACT.trigger((EntityPlayerMP) player);
							MinestuckPlayerTracker.sendLandEntryMessage(player);
						} else
						{
							player.sendMessage(new TextComponentString("Entry failed!"));
						}
					}
					
					return;
				}
			}
		} catch(Exception e)
		{
			Debug.logger.error("Exception when "+player.getName()+" tried to enter their land.", e);
			player.sendMessage(new TextComponentString("[Minestuck] Something went wrong during entry. "+ (Minestuck.isServerRunning?"Check the console for the error message.":"Notify the server owner about this.")).setStyle(new Style().setColor(TextFormatting.RED)));
		}
	}
	
	private static boolean canModifyEntryBlocks(World world, EntityPlayer player)
	{
		int x = (int) player.posX;
		if(player.posX < 0) x--;
		int y = (int) player.posY;
		int z = (int) player.posZ;
		if(player.posZ < 0) z--;
		for(int blockX = x - artifactRange; blockX <= x + artifactRange; blockX++)
		{
			int zWidth = (int) Math.sqrt(artifactRange * artifactRange - (blockX - x) * (blockX - x));
			for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
				if(!world.isBlockModifiable(player, new BlockPos(blockX, y, blockZ)))
					return false;
		}
		
		return true;
	}

	@Override
	public boolean prepareDestination(BlockPos pos, Entity entity, WorldServer worldserver)
	{
		TextComponentString errorMessage = null;
		switch(entryfier.prepareDestination(pos, entity, worldserver))
		{
		case success:			return true;
		case notInCreative:		errorMessage = new TextComponentString("You are not allowed to move command blocks.");				break;
		case stealingIsWrong:	errorMessage = new TextComponentString("You are not allowed to move other players' computers.");	break;
		case noComputer:		errorMessage = new TextComponentString("There is no computer in range.");							break;
		default:				break;
		}
		
		if(errorMessage != null)
		{
			((EntityPlayerMP) entity).sendStatusMessage(errorMessage, false);
		}
		
		return false;
	}

	@Override
	public void finalizeDestination(Entity entity, WorldServer worldserver, WorldServer worldserver1)
	{
		entryfier.finalizeDestination(entity, worldserver, worldserver1);
	}
}