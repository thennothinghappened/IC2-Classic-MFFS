package mods.orca.mffs.blocks.base;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class BlockMachine<TE extends TileEntity> extends BlockTileEntity<TE> {
    public BlockMachine(String name) {
        super(name, Material.IRON, true);

        setHardness(3F);
        setResistance(50F);
        setSoundType(SoundType.METAL);
        setRegisterItem();
    }

    @Override
    public abstract Class<TE> getTileEntityClass();

    @Nullable
    @Override
    public abstract TE createTileEntity(World world, IBlockState state);

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        super.onBlockAdded(worldIn, pos, state);
    }
}
