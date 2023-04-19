package mods.orca.mffs.blocks.upgrades;

import mods.orca.mffs.blocks.base.tile.TileUpgradableMachine;

public interface IUpgrade {
    void setActive(boolean active);
    boolean compatible(TileUpgradableMachine machine);
}
