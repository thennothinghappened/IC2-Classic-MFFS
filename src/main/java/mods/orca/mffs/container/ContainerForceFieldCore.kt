package mods.orca.mffs.container

import mods.orca.mffs.blocks.core.TileForceFieldCore
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.EnumFacing
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.SlotItemHandler

/**
 * Container for the [TileForceFieldCore] block.
 */
class ContainerForceFieldCore(
    inventoryPlayer: InventoryPlayer,
    private val core: TileForceFieldCore
) : ContainerBase(inventoryPlayer, 142) {

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
            ?: throw IllegalStateException("Expected Core block to have an inventory, but it wasn't present!")

        addSlotToContainer(object : SlotItemHandler(inventory, 0, 97, 120) {

            /**
             * When the player inserts MFFS cards, we convert them into cards linked to this core.
             */
            override fun onSlotChanged() {

                if (hasStack) {
                    core.linkBlankCardStack(stack)
                        ?.let { putStack(it) }
                }

                core.markDirty()

            }

        })

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
     * We can interact with this container so long as the [TileForceFieldCore] we're attached to remains valid.
     */
    override fun canInteractWith(player: EntityPlayer) =
        !core.isInvalid

}
