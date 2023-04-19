package mods.orca.mffs.blocks.base;

import mods.orca.mffs.blocks.base.tile.TileUpgradableMachine;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockUpgradableMachine<TE extends TileUpgradableMachine> extends BlockMachine<TE> {

    public BlockUpgradableMachine(String name) {
        super(name);
    }

    @Override
    public abstract Class<TE> getTileEntityClass();

    @Nullable
    @Override
    public abstract TE createTileEntity(World world, IBlockState state);

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos thisPos, Block fromBlock, BlockPos fromPos) {
        getTileEntity(worldIn, thisPos).findUpgrades(worldIn, thisPos);
    }
}
