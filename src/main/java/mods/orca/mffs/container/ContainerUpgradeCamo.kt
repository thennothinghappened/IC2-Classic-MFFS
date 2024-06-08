package mods.orca.mffs.container

//import mods.orca.mffs.blocks.upgrades.camoflage.TileUpgradeCamo
//import net.minecraft.entity.player.InventoryPlayer
//import net.minecraft.util.EnumFacing
//import net.minecraftforge.items.CapabilityItemHandler
//import net.minecraftforge.items.SlotItemHandler
//import javax.annotation.Nonnull
//
//class ContainerUpgradeCamo(
//    inventoryPlayer: InventoryPlayer,
//    upgradeCamo: TileUpgradeCamo
//) : ContainerPlayerInvBase(inventoryPlayer, 58) {
//
//    init {
//
//        val inventory = upgradeCamo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)
//            ?: throw IllegalStateException("Camo Upgrade expected to have an inventory, but no capability was registered!!")
//
//        addSlotToContainer(object : SlotItemHandler(inventory, 0, 80, 22) {
//
//            override fun onSlotChanged() {
//                upgradeCamo.markDirty()
//            }
//
//        })
//
//    }
//
//}
