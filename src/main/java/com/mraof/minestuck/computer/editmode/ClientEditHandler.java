package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.client.ClientDimensionData;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.weapon.PogoEffect;
import com.mraof.minestuck.network.ClientEditPacket;
import com.mraof.minestuck.network.EditmodeFillPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.MSCapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientEditHandler
{
	static boolean activated;
	
	static int centerX, centerZ;
	
	public static String client;
	
	/**
	 * Used to tell if the client is in edit mode or not.
	 */
	public static boolean isActive() {
		return activated;
	}
	
	public static void onKeyPressed()
	{
		ClientEditPacket packet = ClientEditPacket.exit();
		MSPacketHandler.sendToServer(packet);
	}
	
	public static void onClientPackage(String target, int posX, int posZ, CompoundTag deployList)
	{
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if(target != null) {	//Enable edit mode
			activated = true;
			centerX = posX;
			centerZ = posZ;
			client = target;
		}
		else if(deployList == null)	//Disable edit mode
		{
			player.fallDistance = 0;
			activated = false;
		}
		if(deployList != null)
		{
			ClientDeployList.load(deployList);
		}
	}
	
	@SubscribeEvent
	public static void addToolTip(ItemTooltipEvent event)
	{
		if(!isActive())
			return;
		
		GristSet have = ClientPlayerData.getClientGrist();
		
		addToolTip(event.getItemStack(), event.getToolTip(), have);
		
	}
	
	private static GristSet itemCost(ItemStack stack, Level level)
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
		
		for(GristAmount amount : cost.getAmounts())
		{
			GristType grist = amount.getType();
			ChatFormatting color = amount.getAmount() <= have.getGrist(grist) ? ChatFormatting.GREEN : ChatFormatting.RED;
			toolTip.add(new TextComponent(amount.getAmount()+" ").append(grist.getDisplayName()).append(" ("+have.getGrist(grist) + ")").withStyle(color));
		}
		if(cost.isEmpty())
			toolTip.add(new TranslatableComponent(GuiUtil.FREE).withStyle(ChatFormatting.GREEN));
	}
	
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || event.phase == TickEvent.Phase.END)
			return;
		
		Player player = mc.player;
		IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY, null).orElse(new EditTools());
		boolean isDragging = cap.isEditDragging();
		boolean isDown = mc.options.keyUse.isDown();
		
		if (isDown)
		{
			if(!canEditDrag(player))
			{
				cap.setEditDragging(false);
				cap.setEditPos1(null);
				cap.setEditPos2(null);
				return;
			}
			
			if (!isDragging)
			{
				BlockHitResult blockHit = getPlayerPOVHitResult(player.getLevel(), player);
				if (blockHit.getType() == BlockHitResult.Type.BLOCK)
				{
					cap.setEditPos1(BlockPos.of(blockHit.getBlockPos().offset(player.level.getBlockState(blockHit.getBlockPos()).getMaterial().isReplaceable() ? 0 : 1, blockHit.getDirection())));
					cap.setEditTraceHit(blockHit.getLocation());
					cap.setEditTraceDirection(blockHit.getDirection());
				}
			}
			
			if (cap.getEditPos1() != null) {
				
				BlockHitResult blockHit = getPlayerPOVHitResult(player.getLevel(), player);;
				BlockPos pos2;
				if (blockHit.getBlockPos() == null) {
					Vec3 Vec3 = player.getEyePosition();
					Vec3 Vec31 = player.getLookAngle();
					Vec3 Vec32 = Vec3.add(Vec31.x * mc.player.getReachDistance(), Vec31.y * mc.player.getReachDistance(), Vec31.z * mc.player.getReachDistance());
					pos2 = new BlockPos(Vec32.x, Vec32.y, Vec32.z);
				} else pos2 = BlockPos.of(blockHit.getBlockPos().offset(player.level.getBlockState(blockHit.getBlockPos()).getMaterial().isReplaceable() ? 0 : 1, blockHit.getDirection()));
				
				BlockPos pos1 = cap.getEditPos1();
				ItemStack stack = (!player.getMainHandItem().isEmpty() ? player.getMainHandItem() : player.getOffhandItem());
				
				if((Math.max(pos1.getX(),pos2.getX())-Math.min(pos1.getX(), pos2.getX())+1)*
						(Math.max(pos1.getY(),pos2.getY())-Math.min(pos1.getY(), pos2.getY())+1)*
						(Math.max(pos1.getZ(),pos2.getZ())-Math.min(pos1.getZ(), pos2.getZ())+1) <= (player.isCreative() ? 256 : stack.getCount()))
					cap.setEditPos2(pos2);
			}
		} else if (isDragging)
		{
			if (cap.getEditPos1() != null)
			{
				MSPacketHandler.sendToServer(new EditmodeFillPacket(cap.getEditPos1(), cap.getEditPos2(), cap.getEditTraceHit(), cap.getEditTraceDirection()));
			}
			cap.setEditPos1(null);
			cap.setEditPos2(null);
		}
		
		cap.setEditDragging(isDown);
	}
	
	private static boolean canEditDrag(Player player)
	{
		return (((player.level.isClientSide() ? ServerEditHandler.getData(player) != null : ClientEditHandler.isActive()) && !DeployList.containsItemStack(player.getItemInHand(player.getMainHandItem().isEmpty() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND), ServerEditHandler.getData(player).connection, player.level))
				&& ((player.getMainHandItem().getItem() instanceof BlockItem) || (player.getOffhandItem().getItem() instanceof BlockItem)));
	}
	
	//based on the Item class function of the same name
	private static BlockHitResult getPlayerPOVHitResult(Level level, Player playerEntity)
	{
		float xRot = playerEntity.getXRot();
		float yRot = playerEntity.getYRot();
		Vec3 eyeVec = playerEntity.getEyePosition(1.0F);
		float f2 = Mth.cos(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = Mth.sin(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = -Mth.cos(-xRot * ((float) Math.PI / 180F));
		float yComponent = Mth.sin(-xRot * ((float) Math.PI / 180F));
		float xComponent = f3 * f4;
		float zComponent = f2 * f4;
		double reachDistance = playerEntity.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
		Vec3 endVec = eyeVec.add((double) xComponent * reachDistance, (double) yComponent * reachDistance, (double) zComponent * reachDistance);
		return level.clip(new ClipContext(eyeVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, playerEntity));
	}
	
	@SubscribeEvent
	public static void tickEnd(TickEvent.PlayerTickEvent event)
	{
		if(event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END && event.player == Minecraft.getInstance().player && isActive())
		{
			Player player = event.player;
			
			double range = ClientDimensionData.isLand(player.level.dimension()) ? MinestuckConfig.SERVER.landEditRange.get() : MinestuckConfig.SERVER.overworldEditRange.get();
			
			ServerEditHandler.updatePosition(player, range, centerX, centerZ);
		}
	}
	
	@SubscribeEvent
	public static void onTossEvent(ItemTossEvent event)
	{
		if(event.getEntity().level.isClientSide && event.getPlayer().isLocalPlayer() && isActive())
		{
			Inventory inventory = event.getPlayer().getInventory();
			ItemStack stack = event.getEntityItem().getItem();
			ClientDeployList.Entry entry = ClientDeployList.getEntry(stack);
			if(entry != null)
			{
				if(ServerEditHandler.isBlockItem(stack.getItem()) || !GristHelper.canAfford(ClientPlayerData.getClientGrist(), entry.getCost()))
					event.setCanceled(true);
			}
			if(event.isCanceled())
			{
				AbstractContainerMenu menu = event.getPlayer().containerMenu;
				if(!menu.getCarried().isEmpty())
					menu.setCarried(ItemStack.EMPTY);
				else inventory.setItem(inventory.selected, ItemStack.EMPTY);
				event.getEntityItem().discard();
			}
		}
	}
	
	@SubscribeEvent
	public static void onItemPickupEvent(EntityItemPickupEvent event) {
		if(event.getEntity().level.isClientSide && isActive() && event.getPlayer().equals(Minecraft.getInstance().player))
			event.setCanceled(true);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onRightClickEvent(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getWorld().isClientSide && event.getPlayer().isLocalPlayer() && isActive())
		{
			Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
			ItemStack stack = event.getPlayer().getMainHandItem();
			event.setUseBlock((block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock) ? Event.Result.ALLOW : Event.Result.DENY);
			if(event.getUseBlock() == Event.Result.ALLOW)
				return;
			if(event.getHand().equals(InteractionHand.OFF_HAND) || !ServerEditHandler.isBlockItem(stack.getItem()))
			{
				event.setCanceled(true);
				return;
			}
			
			GristSet cost = itemCost(stack, event.getWorld());
			if(!GristHelper.canAfford(ClientPlayerData.getClientGrist(), cost))
			{
				if(cost != null)
				{
					event.getPlayer().sendMessage(cost.createMissingMessage(), Util.NIL_UUID);
				}
				event.setCanceled(true);
			}
			if(event.getUseItem() == Event.Result.DEFAULT)
				event.setUseItem(Event.Result.ALLOW);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onLeftClickEvent(PlayerInteractEvent.LeftClickBlock event)
	{
		if(event.getWorld().isClientSide && event.getPlayer().isLocalPlayer() && isActive())
		{
			BlockState block = event.getWorld().getBlockState(event.getPos());
			if(block.getDestroySpeed(event.getWorld(), event.getPos()) < 0 || block.getMaterial() == Material.PORTAL
					|| ClientPlayerData.getClientGrist().getGrist(GristTypes.BUILD) <= 0)
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onRightClickAir(PlayerInteractEvent.RightClickItem event)
	{
		if(event.getWorld().isClientSide && event.getPlayer().isLocalPlayer() && isActive())
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onAttackEvent(AttackEntityEvent event)
	{
		if(event.getEntity().level.isClientSide && event.getPlayer().isLocalPlayer() && isActive())
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onInteractEvent(PlayerInteractEvent.EntityInteract event)
	{
		if(event.getEntity().level.isClientSide && event.getPlayer().isLocalPlayer() && isActive())
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onInteractEvent(PlayerInteractEvent.EntityInteractSpecific event)
	{
		if(event.getEntity().level.isClientSide && event.getPlayer().isLocalPlayer() && isActive())
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event)
	{
		if(event.getWorld().isClientSide())
			activated = false;
	}
	
	@SubscribeEvent(priority=EventPriority.HIGH)
	public static void onScreenOpened(ScreenOpenEvent event)
	{
		if(isActive() && event.getScreen() instanceof EffectRenderingInventoryScreen<?>)
		{
				event.setCanceled(true);
				PlayerStatsScreen.editmodeTab = PlayerStatsScreen.EditmodeGuiType.DEPLOY_LIST;
				PlayerStatsScreen.openGui(true);
		}
	}
}