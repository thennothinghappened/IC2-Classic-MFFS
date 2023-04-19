package mods.orca.mffs.blocks.base.tile;

import mods.orca.mffs.blocks.upgrades.IUpgrade;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class TileUpgradableMachine extends TileMachine {

    public List<IUpgrade> upgrades = new ArrayList<>();

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
