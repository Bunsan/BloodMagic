package WayofTime.alchemicalWizardry.item;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.NBTHolder;
import WayofTime.alchemicalWizardry.api.iface.IBindable;
import WayofTime.alchemicalWizardry.api.orb.BloodOrb;
import WayofTime.alchemicalWizardry.api.orb.IBloodOrb;
import WayofTime.alchemicalWizardry.api.registry.OrbRegistry;
import WayofTime.alchemicalWizardry.api.util.helper.NetworkHelper;
import WayofTime.alchemicalWizardry.api.util.helper.PlayerHelper;
import com.google.common.base.Strings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBloodOrb extends ItemBindable implements IBloodOrb, IBindable {

    public ItemBloodOrb() {
        setUnlocalizedName(AlchemicalWizardry.MODID + ".orb.");
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + OrbRegistry.getOrb(stack.getItemDamage()).getName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubItems(Item id, CreativeTabs creativeTab, List list) {
        for (int i = 0; i < OrbRegistry.getSize(); i++)
            list.add(new ItemStack(id, 1, i));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        super.onItemRightClick(stack, world, player);

        if (world == null)
            return stack;

        double posX = player.posX;
        double posY = player.posY;
        double posZ = player.posZ;
        world.playSoundEffect((double) ((float) posX + 0.5F), (double) ((float) posY + 0.5F), (double) ((float) posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
//        SpellHelper.sendIndexedParticleToAllAround(world, posX, posY, posZ, 20, world.provider.getDimensionId(), 4, posX, posY, posZ);

        if(PlayerHelper.isFakePlayer(player))
            return stack;

        if (Strings.isNullOrEmpty(stack.getTagCompound().getString(NBTHolder.NBT_OWNER)))
            return stack;

        if (world.isRemote)
            return stack;

        if(stack.getTagCompound().getString(NBTHolder.NBT_OWNER).equals(PlayerHelper.getUsernameFromPlayer(player)))
            NetworkHelper.setMaxOrbToMax(stack.getTagCompound().getString(NBTHolder.NBT_OWNER), getOrbLevel(stack.getItemDamage()));

        NetworkHelper.addCurrentEssenceToMaximum(stack.getTagCompound().getString(NBTHolder.NBT_OWNER), 200, getMaxEssence(stack.getItemDamage()));
        hurtPlayer(player, 200);
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {
        tooltip.add(StatCollector.translateToLocal("tooltip.AlchemicalWizardry.orb.desc"));

        if (advanced)
            tooltip.add(String.format(StatCollector.translateToLocal("tooltip.AlchemicalWizardry.orb.owner"), getOrb(stack.getItemDamage()).getOwner()));

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        return stack;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    // IBindable

    @Override
    public boolean onBind(EntityPlayer player, ItemStack stack) {
        return true;
    }

    // IBloodOrb

    @Override
    public BloodOrb getOrb(int meta) {
        return OrbRegistry.getOrb(meta);
    }

    @Override
    public int getMaxEssence(int meta) {
        return OrbRegistry.getOrb(meta).getCapacity();
    }

    @Override
    public int getOrbLevel(int meta) {
        return OrbRegistry.getOrb(meta).getTier();
    }
}