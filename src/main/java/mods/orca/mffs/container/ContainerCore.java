package mods.orca.mffs.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.orca.mffs.blocks.ModBlocks;
import mods.orca.mffs.blocks.core.TileCore;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ContainerCore extends ContainerPlayerInvBase {

    private final TileCore core;

    public ContainerCore(
        @NotNull InventoryPlayer inventoryPlayer,
        @NotNull final TileCore core
    ) {

        super(inventoryPlayer, 142);
        this.core = core;

        IItemHandler inventory = core.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

        addSlotToContainer(new SlotItemHandler(inventory, 0, 97, 120) {

            @Override
            public void onSlotChanged() {
                if (getHasStack()) {

                    // They've put an MFFS card in.
                    // We run on server and client as otherwise client doesn't know it changed until we pull it out again.
                    ItemStack burnt = ModBlocks.CORE.burnMFFSCard(getStack(), core.getPos());

                    if (burnt != null) {
                        putStack(burnt);
                    }
                }

                core.markDirty();
            }

        });

    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        SPacketUpdateTileEntity packet = core.getUpdatePacket();

        if (packet == null) {
            return;
        }

        for (IContainerListener listener : listeners) {
            if (listener instanceof EntityPlayerMP) {
                ((EntityPlayerMP) listener).connection.sendPacket(packet);
            }
        }
    }

    public double getPower() {
        return core.getEnergy();
    }

    public double getMaxPower() {
        return core.maxEnergy;
    }

    public double getPowerPercentage() {
        return getPower() / getMaxPower();
    }
}
