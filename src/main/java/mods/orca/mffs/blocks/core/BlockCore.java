package mods.orca.mffs.blocks.core;

import mods.orca.mffs.blocks.base.BlockUpgradableMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCore extends BlockUpgradableMachine<TileCore> {
    public BlockCore() {
        super("core");
    }

    @Override
    public Class<TileCore> getTileEntityClass() {
        return TileCore.class;
    }

    @Nullable
    @Override
    public TileCore createTileEntity(World world, IBlockState state) {
        return new TileCore();
    }
}
