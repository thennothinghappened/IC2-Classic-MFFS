package mods.orca.mffs.blocks.base.tile;

import mods.orca.mffs.blocks.upgrades.IUpgrade;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class TileUpgradableMachine extends TileMachine {

    public List<IUpgrade> upgrades = new ArrayList<>();
    protected ItemStackHandler inventory;

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        //noinspection unchecked
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
    }

    public void findUpgrades(World world, BlockPos thisPos) {
        deactivateUpgrades();
        upgrades.clear();

        for (EnumFacing direction : EnumFacing.VALUES) {
            TileEntity te = world.getTileEntity(thisPos.offset(direction));

            // check valid first
            if (te == null || te.isInvalid()) {
                continue;
            }

            // is it an upgrade?
            if (!(te instanceof IUpgrade)) {
                continue;
            }

            // is it compatible with this machine?
            if (!((IUpgrade) te).compatible(this)) {
                continue;
            }

            // add it to our list of upgrades
            upgrades.add((IUpgrade) te);
        }

        activateUpgrades();
    }

    private void deactivateUpgrades() {
        upgrades.forEach(upgrade -> upgrade.setActive(false));
    }

    protected void activateUpgrades() {
        upgrades.forEach(upgrade -> upgrade.setActive(true));
    }


}
