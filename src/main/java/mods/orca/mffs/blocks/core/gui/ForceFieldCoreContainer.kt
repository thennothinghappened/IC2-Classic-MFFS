package mods.orca.mffs.blocks.core.gui

import mods.orca.mffs.blocks.core.ForceFieldCoreTile
import mods.orca.mffs.container.ContainerWithPlayerInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.EnumFacing
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.SlotItemHandler

/**
 * Container for the [mods.orca.mffs.blocks.core.ForceFieldCoreTile] block.
 */
class ForceFieldCoreContainer(inventoryPlayer: InventoryPlayer, private val core: ForceFieldCoreTile)
    : ContainerWithPlayerInventory(inventoryPlayer, 142) {

    /**
     * The amount of energy the core has.
     */
    val energy: Double
        get() = core.energy

    /**
     * The maximum amount of energy the core can store.
     */
    val maxEnergy: Double
        get() = core.maxEnergy

    /**
     * The percentage of filled energy in the core.
     */
    val energyPercent: Double
        get() = energy / maxEnergy

    init {
        val inventory = core.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)
            ?: error("Expected Core block to have an inventory, but it wasn't present!")

        addSlotToContainer(SlotItemHandler(inventory, 0, 97, 120))
    }

    /**
     * Notify players viewing this container of updates to the core, such as the energy level changing, or inventory
     * slot updates.
     */
    override fun detectAndSendChanges() {
        super.detectAndSendChanges()
        val packet = core.updatePacket ?: return

        listeners
            .filterIsInstance<EntityPlayerMP>()
            .forEach { listener -> listener.connection.sendPacket(packet) }
    }

    /**
     * We can interact with this container so long as the [ForceFieldCoreTile] we're attached to remains valid.
     */
    override fun canInteractWith(player: EntityPlayer) =
        !core.isInvalid
}
