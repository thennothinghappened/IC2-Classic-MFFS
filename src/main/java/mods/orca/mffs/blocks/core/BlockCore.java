package mods.orca.mffs.blocks.core;

import mods.orca.mffs.MFFSMod;
import mods.orca.mffs.blocks.base.BlockUpgradableMachine;
import mods.orca.mffs.blocks.upgrades.IBlockUpgrade;
import mods.orca.mffs.items.ItemFreqcard;
import mods.orca.mffs.items.ModItems;
import mods.orca.mffs.util.handlers.ModGuiHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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

    /**
     * Converts a stack of blank MFFS cards into an equivalent amount of 'burnt' cards which the projector
     * can use to figure out where this Core is.
     * @param stack Incoming blank cards
     * @param pos Position of this block
     * @return A 'burnt' MFFS card that points to this block position to put into a projector.
     */
    @Nullable
    public ItemStack burnMFFSCard(ItemStack stack, BlockPos pos) {
        if (stack.isEmpty()) {
            return null;
        }

        if (stack.getItem() != ModItems.FREQCARD_BLANK) {
            return null;
        }

        ItemStack burnt = new ItemStack(ModItems.FREQCARD, stack.getCount(), 0);
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setIntArray(ItemFreqcard.KEY_CORE_POS, new int[]{pos.getX(), pos.getY(), pos.getZ()});

        burnt.setTagCompound(nbt);

        return burnt;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        ItemStack heldItem = playerIn.getHeldItem(hand);
        ItemStack burntCards = burnMFFSCard(heldItem, pos);

        // they clicked with a blank MFFS card, so lets give them burnt one(s)
        if (burntCards != null) {
            if (!worldIn.isRemote) {
                playerIn.setHeldItem(hand, burntCards);
            }
            return true;
        }

        // if the player is trying to place an upgrade, don't get in the way
        if (heldItem.getItem() instanceof ItemBlock) {
            if (((ItemBlock) heldItem.getItem()).getBlock() instanceof IBlockUpgrade) {
                return false;
            }
        }

        playerIn.openGui(MFFSMod.instance, ModGuiHandler.GuiId.Core.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());

        return true;
    }
}
