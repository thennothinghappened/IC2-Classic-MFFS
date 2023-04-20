package mods.orca.mffs.blocks.upgrades;

import mods.orca.mffs.blocks.base.tile.TileUpgradableMachine;

import javax.annotation.Nullable;

public interface IUpgrade {
    void setActive(boolean active);
    @Nullable
    TileUpgradableMachine getOwner();
    void setOwner(@Nullable TileUpgradableMachine owner);
}
