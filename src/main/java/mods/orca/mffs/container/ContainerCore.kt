package mods.orca.mffs.container

import mods.orca.mffs.blocks.ModBlocks
import mods.orca.mffs.blocks.core.TileCore
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.EnumFacing
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.SlotItemHandler

class ContainerCore(
    inventoryPlayer: InventoryPlayer,
    private val core: TileCore
) : ContainerPlayerInvBase(inventoryPlayer, 142) {

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

        addSlotToContainer(object : SlotItemHandler(inventory, 0, 97, 120) {

            /**
             * When the player inserts MFFS cards, we convert them into cards linked to this core.
             */
            override fun onSlotChanged() {

                if (hasStack) {

                    ModBlocks.CORE.burnMFFSCard(stack, core.pos)
                        ?.let { putStack(it) }

                }

                core.markDirty()
            }

        })

    }

    override fun detectAndSendChanges() {

        super.detectAndSendChanges()
        val packet = core.updatePacket ?: return

        listeners
            .filterIsInstance<EntityPlayerMP>()
            .forEach { listener -> listener.connection.sendPacket(packet) }

    }

}
