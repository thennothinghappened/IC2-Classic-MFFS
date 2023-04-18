package mods.orca.mffs.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockMachine<TE extends TileEntity> extends BlockTileEntity<TE> {
    public BlockMachine(String name) {
        super(name, Material.IRON, true);

        setHardness(3F);
        setResistance(50F);
        setSoundType(SoundType.METAL);
    }

    @Override
    public abstract Class<TE> getTileEntityClass();

    @Nullable
    @Override
    public abstract TE createTileEntity(World world, IBlockState state);
}
