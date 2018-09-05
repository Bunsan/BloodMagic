package com.wayoftime.bloodmagic.item.sigil;

import com.wayoftime.bloodmagic.core.network.Binding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public interface ISigil {

    @Nonnull
    default EnumActionResult onRightClick(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull EnumHand hand, @Nonnull Binding binding) {
        return EnumActionResult.PASS;
    }

    default EnumActionResult onInteract(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, @Nonnull EnumHand hand, @Nonnull Binding binding) {
        return EnumActionResult.PASS;
    }

    @Nonnegative
    int getCost();

    interface Toggle extends ISigil {

        default void onToggle(boolean active, @Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull EnumHand hand, @Nonnull Binding binding) {

        }

        default void onUpdate(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnegative int itemSlot, boolean isHeld, @Nonnull Binding binding) {

        }
    }

    interface Holding extends ISigil {

        int getSize(ItemStack stack);

        int getEquippedSigil(ItemStack stack);

        NonNullList<ItemStack> getHeldSigils(ItemStack stack);

        void setHeldSigils(ItemStack stack, NonNullList<ItemStack> inventory);
    }
}
