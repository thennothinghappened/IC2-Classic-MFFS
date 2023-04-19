package mods.orca.mffs.blocks.upgrades.camoflage;

import mods.orca.mffs.MFFSMod;
import mods.orca.mffs.blocks.base.BlockMachine;
import mods.orca.mffs.util.handlers.ModGuiHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockUpgradeCamo extends BlockMachine<TileUpgradeCamo> {
    public BlockUpgradeCamo() {
        super("upgrade_camo");
    }

    @Override
    public Class<TileUpgradeCamo> getTileEntityClass() {
        return TileUpgradeCamo.class;
    }

    @Nullable
    @Override
    public TileUpgradeCamo createTileEntity(World world, IBlockState state) {
        return new TileUpgradeCamo();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }

        TileUpgradeCamo tile = getTileEntity(worldIn, pos);
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
        ItemStack heldItem = playerIn.getHeldItem(hand);

        if (!playerIn.isSneaking()) {
            if (heldItem.isEmpty()) {
                playerIn.setHeldItem(hand, itemHandler.extractItem(0, 1, false));
            } else {
                playerIn.setHeldItem(hand, itemHandler.insertItem(0, heldItem, false));
            }
            tile.markDirty();
        } else {
            playerIn.openGui(MFFSMod.instance, ModGuiHandler.CAMOFLAGE_UPGRADE, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileUpgradeCamo tile = getTileEntity(worldIn, pos);
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        ItemStack stack = itemHandler.getStackInSlot(0);

        if (!stack.isEmpty()) {
            EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
            worldIn.spawnEntity(item);
        }

        super.breakBlock(worldIn, pos, state);
    }
}
