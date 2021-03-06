package coffeecatrailway.bedcutter.common.block;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;

import java.util.Optional;

/**
 * @author CoffeeCatRailway
 * Created: 26/10/2020
 * Based on: https://github.com/Ocelot5836/Sonar/blob/1.16.3/src/main/java/io/github/ocelot/sonar/common/item/SpawnEggItemBase.java#L48
 *
 * An interface to insert items in a group after a specified item type
 * <B>NOTE</B>: to make the interface work you must copy the code below!
 *
 * <pre>
 * &#64;Override
 * public void fillItemGroup(ItemGroup group, NonNullList&#60;ItemStack&#62; items)
 * {
 *     ISortInTab.super.fillItemGroup(group, items);
 * }
 * </pre>
 */
public interface ISortInTab extends IItemProvider
{
    default void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        if (group == this.getItemGroup())
        {
            if (items.stream().anyMatch(this::isType))
            {
                Optional<ItemStack> optional = items.stream().filter(this::isType).reduce((a, b) -> b);
                if (optional.isPresent() && items.contains(optional.get()))
                {
                    items.add(items.indexOf(optional.get()) + 1, new ItemStack(this));
                    return;
                }
            }
            items.add(new ItemStack(this));
        }
    }

    /**
     * Is the stack the type of item you want to insert after
     * @param stack Stack to check
     * @return Is the type of item
     */
    boolean isType(ItemStack stack);

    /**
     * The group you want your item(s) in
     * @return Item group (tab)
     */
    ItemGroup getItemGroup();
}
