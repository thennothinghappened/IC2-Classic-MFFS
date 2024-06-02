package mods.orca.mffs.blocks.projector;

import mods.orca.mffs.blocks.base.BlockUpgradableMachine;

public abstract class BlockProjector extends BlockUpgradableMachine<TileProjector> {
    public BlockProjector(String name) {
        super(TileProjector.class, name);
    }
}
