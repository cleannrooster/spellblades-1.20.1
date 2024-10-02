package com.spellbladenext.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.spellbladenext.Spellblades;
import com.spellbladenext.items.TabulaRasa;
import com.spellbladenext.items.ThesisBook;
import dev.emi.trinkets.api.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.spell_engine.api.spell.SpellContainer;
import net.spell_engine.compat.TrinketsCompat;
import net.spell_engine.internals.SpellContainerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(SpellContainerHelper.class)
public class SpellContainerMixin {
    @ModifyReturnValue(at = @At("TAIL"), method = "getEquipped")
    private static SpellContainer getEquippedMIX(SpellContainer container, ItemStack proxyContainer, PlayerEntity player) {
        if(!Spellblades.config.tab && !TrinketsApi.getTrinketComponent(player).isEmpty()){
            if(TrinketsApi.getTrinketComponent(player).get().isEquipped(Spellblades.TABULARASA)){
                return null;
            }
        }
        if(container != null&&container.spell_ids != null && container.spell_ids.contains("spellbladenext:thesis") && SpellContainerHelper.containerFromItemStack(player.getMainHandStack()) != null) {
            List<String> listcontainer = SpellContainerHelper.containerFromItemStack(player.getMainHandStack()).spell_ids;
            Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
            if (!component.isEmpty() && proxyContainer != null ) {
                TrinketComponent trinketComponent = (TrinketComponent) component.get();
                LinkedHashSet<ItemStack> items = new LinkedHashSet();
                TrinketInventory spellBookSlot = (TrinketInventory) ((Map) trinketComponent.getInventory().get("charm")).get("spell_book");
                items.add(spellBookSlot.getStack(0));
                trinketComponent.getAllEquipped().forEach((pair) -> {
                    items.add((ItemStack) pair.getRight());
                });
                Iterator var8 = items.iterator();
                LinkedHashSet<String> collectedSpellIds = new LinkedHashSet();

                while(var8.hasNext()) {
                    ItemStack stack = (ItemStack)var8.next();
                    if (!stack.isEmpty()) {
                        SpellContainer container2 = SpellContainerHelper.containerFromItemStack(stack);
                        if (container2 != null && container2.isValid() ) {
                            collectedSpellIds.addAll(container2.spell_ids);
                        }
                    }
                }
                container.spell_ids = collectedSpellIds.stream().toList();
            }
        }
        return container;
    }
}