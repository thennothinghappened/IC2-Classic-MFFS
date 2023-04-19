package mods.orca.mffs.blocks.core;

import mods.orca.mffs.blocks.base.tile.TileUpgradableMachine;

public class TileCore extends TileUpgradableMachine {

    @Override
    public double getDemandedEnergy() {
        return 10000000;
    }

    @Override
    public int getSinkTier() {
        return 3;
    }
}
