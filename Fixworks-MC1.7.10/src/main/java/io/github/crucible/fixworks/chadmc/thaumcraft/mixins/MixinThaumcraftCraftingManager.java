package io.github.crucible.fixworks.chadmc.thaumcraft.mixins;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

import java.util.*;

@Pseudo
@Mixin(value = ThaumcraftCraftingManager.class, remap = false)
public abstract class MixinThaumcraftCraftingManager {

    @Shadow
    private static AspectList capAspects(AspectList sourcetags, int amount) {
        return null;
    }

    @Shadow
    public static AspectList generateTags(Item item, int meta) {
        return null;
    }

    @SuppressWarnings({ "rawtypes", "null" })
    private static AspectList getObjectTagsDummyFunction(ItemStack itemstack, Item item, int meta) {
        AspectList tmp = ThaumcraftApi.objectTags.get(Arrays.asList(item, meta));
        if (tmp == null) {
            Collection<List> col = ThaumcraftApi.objectTags.keySet();
            Iterator<List> i$ = col.iterator();

            while (i$.hasNext()) {
                List l = i$.next();
                if (l.get(0) == item && l.get(1) instanceof int[]) {
                    int[] range = (int[]) l.get(1);
                    Arrays.sort(range);
                    if (Arrays.binarySearch(range, meta) >= 0) {
                        tmp = ThaumcraftApi.objectTags.get(Arrays.asList(item, range));
                        return tmp;
                    }
                }
            }

            tmp = ThaumcraftApi.objectTags.get(Arrays.asList(item, 32767));
            if (tmp == null && tmp == null) {
                if (meta == 32767 && tmp == null) {
                    int index = 0;

                    do {
                        tmp = ThaumcraftApi.objectTags.get(Arrays.asList(item, index));
                        ++index;
                    } while (index < 16 && tmp == null);
                }

                if (tmp == null) {
                    tmp = generateTags(item, meta);
                }
            }
        }

        if (itemstack.getItem() instanceof ItemWandCasting) {
            ItemWandCasting wand = (ItemWandCasting) itemstack.getItem();
            if (tmp == null) {
                tmp = new AspectList();
            }

            tmp.merge(Aspect.MAGIC, (wand.getRod(itemstack).getCraftCost() + wand.getCap(itemstack).getCraftCost()) / 2);
            tmp.merge(Aspect.TOOL, (wand.getRod(itemstack).getCraftCost() + wand.getCap(itemstack).getCraftCost()) / 3);
        }

        if (item != null && item == Items.potionitem) {
            if (tmp == null) {
                tmp = new AspectList();
            }

            tmp.merge(Aspect.WATER, 1);
            ItemPotion ip = (ItemPotion) item;
            List effects = ip.getEffects(itemstack.getItemDamage());
            if (effects != null) {
                if (ItemPotion.isSplash(itemstack.getItemDamage())) {
                    tmp.merge(Aspect.ENTROPY, 2);
                }

                Iterator var5 = effects.iterator();

                while (var5.hasNext()) {
                    PotionEffect var6 = (PotionEffect) var5.next();
                    tmp.merge(Aspect.MAGIC, (var6.getAmplifier() + 1) * 2);
                    if (var6.getPotionID() == Potion.blindness.id) {
                        tmp.merge(Aspect.DARKNESS, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.confusion.id) {
                        tmp.merge(Aspect.ELDRITCH, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.damageBoost.id) {
                        tmp.merge(Aspect.WEAPON, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.digSlowdown.id) {
                        tmp.merge(Aspect.TRAP, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.digSpeed.id) {
                        tmp.merge(Aspect.TOOL, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.fireResistance.id) {
                        tmp.merge(Aspect.ARMOR, var6.getAmplifier() + 1);
                        tmp.merge(Aspect.FIRE, (var6.getAmplifier() + 1) * 2);
                    } else if (var6.getPotionID() == Potion.harm.id) {
                        tmp.merge(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.heal.id) {
                        tmp.merge(Aspect.HEAL, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.hunger.id) {
                        tmp.merge(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.invisibility.id) {
                        tmp.merge(Aspect.SENSES, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.jump.id) {
                        tmp.merge(Aspect.FLIGHT, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.moveSlowdown.id) {
                        tmp.merge(Aspect.TRAP, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.moveSpeed.id) {
                        tmp.merge(Aspect.MOTION, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.nightVision.id) {
                        tmp.merge(Aspect.SENSES, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.poison.id) {
                        tmp.merge(Aspect.POISON, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.regeneration.id) {
                        tmp.merge(Aspect.HEAL, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.resistance.id) {
                        tmp.merge(Aspect.ARMOR, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.waterBreathing.id) {
                        tmp.merge(Aspect.AIR, (var6.getAmplifier() + 1) * 3);
                    } else if (var6.getPotionID() == Potion.weakness.id) {
                        tmp.merge(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);
                    }
                }
            }
        }

        return capAspects(tmp, 64);
    }

    private static final HashMap<String, AspectList> mapOfAspects = new HashMap<String, AspectList>();
    private static final AspectList EMPTY_ASPECT_LIST = new AspectList();

    /**
     * @author EverNife
     * @reason Ao se criar um cache para o mapa de aspectos, se previne uma IMENSA quantidade de lag em alguns casos especificos!
     */
    @Overwrite
    public static AspectList getObjectTags(ItemStack itemstack) {
        Item item;
        int meta;
        try {
            item = itemstack.getItem();
            meta = itemstack.getItemDamage();
        } catch (Exception var8) {
            return null;
        }

        String identifier = item.getUnlocalizedName() + "-" + meta;

        AspectList aspectList = mapOfAspects.getOrDefault(identifier, null);
        if (aspectList == null) {
            aspectList = getObjectTagsDummyFunction(itemstack, item, meta);
            if (aspectList == null) {
                aspectList = EMPTY_ASPECT_LIST;
            }
            mapOfAspects.put(identifier, aspectList);
        }

        return aspectList != EMPTY_ASPECT_LIST ? aspectList : null;
    }
}
