package com.mraof.minestuck.strife;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.KindAbstratusType;
import com.mraof.minestuck.player.StrifePortfolioData;
import com.mraof.minestuck.player.StrifeSpecibus;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Specibus evolutions.
 *
 * <p>An evolution turns a specibus kind into a stronger/weirder one when one of its weapons breaks
 * (for example Bladekind into 1/2 Bladekind). Every player can perform each evolution only once, which also
 * grants the matching advancement. After that, as long as the player owns the evolved specibus,
 * every breaking weapon of the old kind turns into its evolved counterpart instead of disappearing,
 * keeping enchantments, custom name and all other item components.</p>
 *
 * <p>New evolutions can be added by extending {@link #EVOLUTIONS}.</p>
 */
public final class StrifeEvolution
{
	/**
	 * @param id         unique id that is stored in the player data once the evolution has happened
	 * @param fromKind   the kind whose weapons evolve
	 * @param toKind     the kind that the specibus evolves into
	 * @param converter  finds the evolved counterpart for a broken weapon, or null if it has none
	 * @param onEvolve   called (once per player) when the specibus itself evolves, used for advancements
	 */
	public record Evolution(String id, String fromKind, String toKind, Function<ItemStack, Item> converter,
	                        Consumer<ServerPlayer> onEvolve)
	{
	}
	
	public static final Evolution BLADEKIND = new Evolution("bladekind", KindAbstratusList.SWORD, KindAbstratusList.HALF_SWORD,
			StrifeEvolution::halfBlade, player -> MSCriteriaTriggers.BLADEKIND_BREAK.get().trigger(player));
	
	public static final List<Evolution> EVOLUTIONS = List.of(BLADEKIND);
	
	/**
	 * Stacks that vanilla wiped right after our event handler ran. They are put back on the next player tick.
	 */
	private static final Map<UUID, Pending> PENDING = new HashMap<>();
	
	private record Pending(InteractionHand hand, ItemStack stack)
	{
	}
	
	/**
	 * Finds the "half_" version of a blade. For example minestuck:katana -> minestuck:half_katana.
	 * The half item has to be part of the half sword kind tag.
	 */
	@Nullable
	private static Item halfBlade(ItemStack stack)
	{
		if(!stack.is(MSTags.Items.KIND_SWORD)) return null;
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
		ResourceLocation halfId = Minestuck.id("half_" + id.getPath());
		
		return BuiltInRegistries.ITEM.getOptional(halfId)
				.filter(item -> new ItemStack(item).is(MSTags.Items.KIND_HALF_SWORD))
				.orElse(null);
	}
	
	/**
	 * Creates the evolved version of a weapon. All item components (enchantments, custom name, lore, trims, ...)
	 * are copied.
	 */
	public static ItemStack convert(ItemStack from, Item to)
	{
		ItemStack result = new ItemStack(to, 1);
		result.applyComponents(from.getComponentsPatch());
		result.remove(MSItemComponents.STRIFE_ASSIGNED.get());
		result.setDamageValue(0);
		return result;
	}
	
	/**
	 * Called when a player's main hand item broke.
	 *
	 * @param broken a copy of the stack as it was before it broke
	 */
	public static void onItemBroken(ServerPlayer player, ItemStack broken)
	{
		if(!MinestuckConfig.SERVER.strifeEvolution.get()) return;
		
		for(Evolution evolution : EVOLUTIONS)
		{
			Item toItem = evolution.converter().apply(broken);
			if(toItem == null) continue;
			
			StrifePortfolioData data = StrifePortfolioHandler.getData(player);
			boolean wasArmed = data.isArmed() && StrifePortfolioHandler.isAssigned(broken);
			int evolvedIndex = data.findSpecibusIndex(evolution.toKind());
			
			if(evolvedIndex >= 0)
			{
				continueAsEvolvedWeapon(player, data, evolution, broken, toItem, evolvedIndex, wasArmed);
				return;
			}
			
			if(!wasArmed || data.hasCompletedEvolution(evolution.id())) return;
			
			StrifeSpecibus specibus = data.getSelectedSpecibus();
			if(specibus == null || !evolution.fromKind().equals(specibus.getAbstratusName())) return;
			
			evolveSpecibus(player, data, evolution, specibus, broken, toItem);
			return;
		}
	}
	
	private static void evolveSpecibus(ServerPlayer player, StrifePortfolioData data, Evolution evolution, StrifeSpecibus specibus, ItemStack broken, Item toItem)
	{
		KindAbstratusType oldType = specibus.getKindAbstratus();
		KindAbstratusType newType = KindAbstratusList.getTypeFromName(evolution.toKind());
		if(newType == null) return; // nothing to evolve into, so the specibus stays untouched
		Component oldName = specibus.getDisplayName();
		
		List<ItemStack> keep = new ArrayList<>();
		List<ItemStack> returned = new ArrayList<>();
		for(ItemStack stack : specibus.getContents())
		{
			Item converted = evolution.converter().apply(stack);
			ItemStack candidate = converted != null ? convert(stack, converted) : stack;
			if(newType.partOf(candidate))
				keep.add(candidate);
			else
				returned.add(stack);
		}
		
		specibus.getContents().clear();
		specibus.switchKindAbstratus(evolution.toKind());
		specibus.getContents().addAll(keep);
		returned.forEach(stack -> StrifePortfolioHandler.giveOrDrop(player, stack));
		
		ItemStack evolved = convert(broken, toItem);
		evolved.set(MSItemComponents.STRIFE_ASSIGNED.get(), Unit.INSTANCE);
		setHandNow(player, evolved);
		
		data.setSelectedWeaponIndex(data.armedWeaponSlot(specibus.getContents().size()));
		data.completeEvolution(evolution.id());
		
		Component newName = specibus.getDisplayName();
		player.displayClientMessage(Component.translatable("status.strife.evolved", oldType != null ? oldName : Component.empty(), newName), true);
		evolution.onEvolve().accept(player);
		StrifePortfolioHandler.syncToClient(player);
	}
	
	private static void continueAsEvolvedWeapon(ServerPlayer player, StrifePortfolioData data, Evolution evolution, ItemStack broken, Item toItem, int evolvedIndex, boolean wasArmed)
	{
		ItemStack evolved = convert(broken, toItem);
		
		if(wasArmed)
		{
			StrifeSpecibus target = data.getPortfolio()[evolvedIndex];
			int capacity = StrifePortfolioHandler.getStrifeDeckCapacity(player);
			
			if(target != null && (capacity < 0 || target.getContents().size() < capacity))
			{
				evolved.set(MSItemComponents.STRIFE_ASSIGNED.get(), Unit.INSTANCE);
				data.setSelectedSpecibusIndex(evolvedIndex);
				data.setSelectedWeaponIndex(target.getContents().size());
			} else
			{
				// The evolved deck is full, so the weapon just becomes a normal item in the hand
				data.setArmed(false);
			}
		}
		
		setHandNow(player, evolved);
		player.displayClientMessage(Component.translatable("status.strife.evolvedWeapon", broken.getHoverName(), evolved.getHoverName()), true);
		StrifePortfolioHandler.syncToClient(player);
	}
	
	private static void setHandNow(ServerPlayer player, ItemStack stack)
	{
		player.setItemInHand(InteractionHand.MAIN_HAND, stack);
		PENDING.put(player.getUUID(), new Pending(InteractionHand.MAIN_HAND, stack));
	}
	
	public static void applyPending(ServerPlayer player)
	{
		Pending pending = PENDING.remove(player.getUUID());
		if(pending == null) return;
		
		if(player.getItemInHand(pending.hand()).isEmpty())
			player.setItemInHand(pending.hand(), pending.stack());
	}
	
	public static void clearPending(ServerPlayer player)
	{
		PENDING.remove(player.getUUID());
	}
}
