package cofh.lib.item;

import cofh.lib.api.item.ICoFHItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

import static cofh.lib.util.Constants.TRUE;

public class PickaxeItemCoFH extends PickaxeItem implements ICoFHItem {

    protected Supplier<Boolean> showInGroups = TRUE;

    protected Supplier<CreativeModeTab> displayGroup;

    public PickaxeItemCoFH(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {

        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    public PickaxeItemCoFH setDisplayGroup(Supplier<CreativeModeTab> displayGroup) {

        this.displayGroup = displayGroup;
        return this;
    }

    public PickaxeItemCoFH setShowInGroups(Supplier<Boolean> showInGroups) {

        this.showInGroups = showInGroups;
        return this;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {

        if (!showInGroups.get() || displayGroup != null && displayGroup.get() != null && displayGroup.get() != group) {
            return;
        }
        super.fillItemCategory(group, items);
    }

    @Override
    public Collection<CreativeModeTab> getCreativeTabs() {

        return displayGroup != null && displayGroup.get() != null ? Collections.singletonList(displayGroup.get()) : super.getCreativeTabs();
    }

}
