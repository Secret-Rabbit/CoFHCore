package cofh.lib.item.impl;

import cofh.core.util.ProxyUtils;
import cofh.lib.item.IColorableItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static cofh.lib.util.constants.Constants.TRUE;

public class SpawnEggItemCoFH extends ForgeSpawnEggItem implements IColorableItem {

    protected BooleanSupplier showInGroups = TRUE;

    public SpawnEggItemCoFH(Supplier<EntityType<? extends Mob>> typeSupIn, int primaryColorIn, int secondaryColorIn, Properties builder) {

        super(typeSupIn, primaryColorIn, secondaryColorIn, builder);

        ProxyUtils.registerColorable(this);
    }

    public SpawnEggItemCoFH setShowInGroups(BooleanSupplier showInGroups) {

        this.showInGroups = showInGroups;
        return this;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {

        if (!showInGroups.getAsBoolean()) {
            return;
        }
        super.fillItemCategory(group, items);
    }

    @OnlyIn (Dist.CLIENT)
    public int getColor(ItemStack item, int colorIndex) {

        return getColor(colorIndex);
    }

}
