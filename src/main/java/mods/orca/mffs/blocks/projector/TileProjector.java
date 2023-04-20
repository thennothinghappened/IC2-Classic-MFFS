package mods.orca.mffs.blocks.projector;

import ic2.api.energy.tile.IEnergyEmitter;
import mods.orca.mffs.blocks.base.tile.TileUpgradableMachine;
import mods.orca.mffs.blocks.core.TileCore;
import mods.orca.mffs.blocks.upgrades.IProjectorUpgrade;
import mods.orca.mffs.blocks.upgrades.IUpgrade;
import mods.orca.mffs.blocks.upgrades.camoflage.ICamoUpgrade;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public abstract class TileProjector extends TileUpgradableMachine {

    protected TileCore core;
    protected List<BlockPos> ff = new ArrayList<>();

    public TileProjector() {
        super(0);
    }



    @Override
    public int getSinkTier() {
        if (core == null) return 3;
        return core.getSinkTier();
    }

    @Override
    public double getDemandedEnergy() {
        return 0;
    }

    @Override
    public double getEnergy() {
        if (core == null) return 0;
        return core.getEnergy();
    }

    @Override
    public boolean useEnergy(double amount, boolean simulate) {
        if (core == null) return false;
        return core.useEnergy(amount, simulate);
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        return false;
    }

    @Override
    protected boolean upgradeCompatible(IUpgrade upgrade) {
        return upgrade instanceof IProjectorUpgrade;
    }
}
