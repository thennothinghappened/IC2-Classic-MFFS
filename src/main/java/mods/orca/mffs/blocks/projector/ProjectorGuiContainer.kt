package mods.orca.mffs.blocks.projector

import mods.orca.mffs.container.ContainerWithPlayerInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.EnumFacing
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.SlotItemHandler

class ProjectorGuiContainer(
    inventoryPlayer: InventoryPlayer,
    private val projector: TileFieldProjector,
) : ContainerWithPlayerInventory(inventoryPlayer, 142) {
    init {
        val inventory = projector.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)
            ?: error("Expected projector to have an inventory")

        addSlotToContainer(object : SlotItemHandler(inventory, 0, 97, 120) {
            override fun onSlotChanged() {
                if (hasStack) {
                    // todo
                }

                projector.markDirty()
            }
        })
    }

    override fun detectAndSendChanges() {
        super.detectAndSendChanges()
        val packet = projector.updatePacket ?: return

        listeners
            .filterIsInstance<EntityPlayerMP>()
            .forEach { listener -> listener.connection.sendPacket(packet) }
    }

    override fun canInteractWith(player: EntityPlayer) = !projector.isInvalid
}
