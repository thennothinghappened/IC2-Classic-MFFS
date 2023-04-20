package mods.orca.mffs.blocks.base;

import ic2.api.tile.IWrenchable;
import mods.orca.mffs.blocks.upgrades.camoflage.TileUpgradeCamo;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockMachine<TE extends TileEntity> extends BlockTileEntity<TE> implements IWrenchable {
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

    // none of our machines have a direction
    @Override
    public EnumFacing getFacing(World world, BlockPos pos) {
        return null;
    }

    @Override
    public boolean setFacing(World world, BlockPos pos, EnumFacing newDirection, EntityPlayer player) {
        return false;
    }

    @Override
    public boolean wrenchCanRemove(World world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    protected void getExtraDrops(NonNullList<ItemStack> drops, World world, BlockPos pos, IBlockState state) {}

    // Add a chance for the machine to drop things like its internal inventory.
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        NonNullList<ItemStack> extraDrops = NonNullList.create();
        getExtraDrops(extraDrops, world, pos, state);

        extraDrops.forEach(drop -> {
            EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), drop);
            world.spawnEntity(item);
        });

        super.breakBlock(world, pos, state);
    }

    // our wrench drops are the same as if the block is destroyed normally.
    @Override
    public List<ItemStack> getWrenchDrops(World world, BlockPos pos, IBlockState state, TileEntity te, EntityPlayer player, int fortune) {
        NonNullList<ItemStack> drops = NonNullList.create();
        getDrops(drops, world, pos, state, fortune);

        return drops;
    }
}
