package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.network.editmode.ClientEditPackets;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.GristCache;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.EntityItemPickupEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("resource")
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientEditHandler
{
	
	public static void onKeyPressed()
	{
		PacketDistributor.SERVER.noArg().send(new ClientEditPackets.Exit());
	}
	
	@SubscribeEvent
	public static void addToolTip(ItemTooltipEvent event)
	{
		if(!ClientEditmodeData.isInEditmode())
			return;
		
		GristSet have = getGristCache().set();
		
		addToolTip(event.getItemStack(), event.getToolTip(), have);
		
	}
	
	static GristSet itemCost(ItemStack stack, Level level)
	{
		ClientDeployList.Entry deployEntry = ClientDeployList.getEntry(stack);
		if(deployEntry != null)
			return deployEntry.getCost();
		else return GristCostRecipe.findCostForItem(stack, null, false, level);
	}
	
	private static void addToolTip(ItemStack stack, List<Component> toolTip, GristSet have)
	{
		Level level = Objects.requireNonNull(Minecraft.getInstance().level);
		GristSet cost = itemCost(stack, level);
		
		if(cost == null)
		{
			return;
		}
		
		for(GristAmount amount : cost.asAmounts())
		{
			GristType grist = amount.type();
			ChatFormatting color = amount.amount() <= have.getGrist(grist) ? ChatFormatting.GREEN : ChatFormatting.RED;
			toolTip.add(Component.literal(amount.amount() + " ").append(grist.getDisplayName()).append(" (" + have.getGrist(grist) + ")").withStyle(color));
		}
		if(cost.isEmpty())
			toolTip.add(Component.translatable(GuiUtil.FREE).withStyle(ChatFormatting.GREEN));
	}
	
	@SubscribeEvent
	public static void tickEnd(TickEvent.PlayerTickEvent event)
	{
		if(event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END && event.player == Minecraft.getInstance().player && ClientEditmodeData.isInEditmode())
		{
			Player player = event.player;
			
			EditmodeLocations locations = ClientEditmodeData.getLocations();
			if(locations != null)
				locations.limitMovement(player, ClientEditmodeData.getClientLand());
		}
	}
	
	@SubscribeEvent
	public static void onTossEvent(ItemTossEvent event)
	{
		if(event.getEntity().level().isClientSide && event.getPlayer().isLocalPlayer() && ClientEditmodeData.isInEditmode())
		{
			Inventory inventory = event.getPlayer().getInventory();
			ItemStack stack = event.getEntity().getItem();
			ClientDeployList.Entry entry = ClientDeployList.getEntry(stack);
			if(entry != null)
			{
				if(ServerEditHandler.isBlockItem(stack.getItem()) || !getGristCache().canAfford(entry.getCost()))
					event.setCanceled(true);
			}
			if(event.isCanceled())
			{
				AbstractContainerMenu menu = event.getPlayer().containerMenu;
				if(!menu.getCarried().isEmpty())
					menu.setCarried(ItemStack.EMPTY);
				else inventory.setItem(inventory.selected, ItemStack.EMPTY);
				event.getEntity().discard();
			}
		}
	}
	
	@SubscribeEvent
	public static void onItemPickupEvent(EntityItemPickupEvent event)
	{
		if(event.getEntity().level().isClientSide && ClientEditmodeData.isInEditmode() && event.getEntity().equals(Minecraft.getInstance().player))
			event.setCanceled(true);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onRightClickEvent(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getLevel().isClientSide && event.getEntity().isLocalPlayer() && ClientEditmodeData.isInEditmode())
		{
			if(!event.getEntity().canReach(event.getPos(), 0.0) || ClientEditToolDrag.canEditRevise(event.getEntity()))
			{
				event.setCanceled(true);
				return;
			}
			
			Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
			ItemStack stack = event.getEntity().getMainHandItem();
			event.setUseBlock((block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock) ? Event.Result.ALLOW : Event.Result.DENY);
			if(event.getUseBlock() == Event.Result.ALLOW)
				return;
			if(event.getHand().equals(InteractionHand.OFF_HAND) || !ServerEditHandler.isBlockItem(stack.getItem()))
			{
				event.setCanceled(true);
				return;
			}
			
			GristSet cost = itemCost(stack, event.getLevel());
			if(!getGristCache().canAfford(cost))
			{
				if(cost != null)
				{
					event.getEntity().sendSystemMessage(GristCache.createMissingMessage(cost));
				}
				event.setCanceled(true);
			}
			if(event.getUseItem() == Event.Result.DEFAULT)
				event.setUseItem(Event.Result.ALLOW);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onLeftClickEvent(PlayerInteractEvent.LeftClickBlock event)
	{
		if(event.getLevel().isClientSide && event.getEntity().isLocalPlayer() && ClientEditmodeData.isInEditmode())
		{
			if(!event.getEntity().canReach(event.getPos(), 0.0) || ClientEditToolDrag.canEditRecycle(event.getEntity()))
			{
				event.setCanceled(true);
				return;
			}
			
			BlockState block = event.getLevel().getBlockState(event.getPos());
			if(block.getDestroySpeed(event.getLevel(), event.getPos()) < 0 || block.is(MSTags.Blocks.EDITMODE_BREAK_BLACKLIST))
			{
				event.getEntity().sendSystemMessage(Component.literal("You're not allowed to break this block!"));
				event.setCanceled(true);
			} else if(!getGristCache().canAfford(ServerEditHandler.blockBreakCost()))
			{
				event.getEntity().sendSystemMessage(GristCache.createMissingMessage(ServerEditHandler.blockBreakCost()));
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onRightClickAir(PlayerInteractEvent.RightClickItem event)
	{
		if(event.getLevel().isClientSide && event.getEntity().isLocalPlayer() && ClientEditmodeData.isInEditmode())
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onAttackEvent(AttackEntityEvent event)
	{
		if(event.getEntity().level().isClientSide && event.getEntity().isLocalPlayer() && ClientEditmodeData.isInEditmode())
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onInteractEvent(PlayerInteractEvent.EntityInteract event)
	{
		if(event.getEntity().level().isClientSide && event.getEntity().isLocalPlayer() && ClientEditmodeData.isInEditmode())
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onInteractEvent(PlayerInteractEvent.EntityInteractSpecific event)
	{
		if(event.getEntity().level().isClientSide && event.getEntity().isLocalPlayer() && ClientEditmodeData.isInEditmode())
			event.setCanceled(true);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onScreenOpened(ScreenEvent.Opening event)
	{
		if(ClientEditmodeData.isInEditmode() && event.getScreen() instanceof EffectRenderingInventoryScreen<?>)
		{
			event.setCanceled(true);
			PlayerStatsScreen.editmodeTab = PlayerStatsScreen.EditmodeGuiType.DEPLOY_LIST;
			PlayerStatsScreen.openGui(true);
		}
	}
	
	private static ClientPlayerData.ClientCache getGristCache()
	{
		return ClientPlayerData.getGristCache(ClientPlayerData.CacheSource.EDITMODE);
	}
}
