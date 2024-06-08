package mods.orca.mffs.blocks.core

import ic2.api.energy.tile.IEnergyEmitter
import mods.orca.mffs.blocks.base.tile.TileMachine
import mods.orca.mffs.items.ItemFrequencyCard
import mods.orca.mffs.items.ItemFrequencyCardBlank
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler

/**
 * The Tile entity for the force-field core, which stores the power and on/off state of the assembly.
 */
class TileForceFieldCore : TileMachine(10000000.0) {

    /**
     * The inventory for the core, which simply contains a slot for linking frequency cards to this core.
     */
    private val inventory = object : ItemStackHandler(1) {

        override fun isItemValid(slot: Int, stack: ItemStack) =
            stack.item is ItemFrequencyCardBlank

        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            return if (isItemValid(slot, stack)) super.insertItem(slot, stack, simulate) else stack
        }

    }

    /**
     * Converts a stack of blank MFFS cards into an equivalent amount of 'burnt' cards which the projector
     * can use to figure out where this Core is.
     *
     * @param stack Incoming blank cards
     * @return A 'burnt' MFFS card that points to this block position to put into a projector.
     */
    fun linkBlankCardStack(stack: ItemStack): ItemStack? {

        if (stack.isEmpty) {
            return null
        }

        if (stack.item !is ItemFrequencyCardBlank) {
            return null
        }

        val nbt = NBTTagCompound().apply {
            setIntArray(ItemFrequencyCard.NBTKey.CorePos.name, intArrayOf(pos.x, pos.y, pos.z))
        }

        return ItemStack(ItemFrequencyCard, stack.count, 0)
            .apply { tagCompound = nbt }

    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        compound.setTag(NBT_KEY_INVENTORY, inventory.serializeNBT())
        return super.writeToNBT(compound)
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        inventory.deserializeNBT(compound.getCompoundTag(NBT_KEY_INVENTORY))
        super.readFromNBT(compound)
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?) =
        when {
            capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> true
            else -> super.hasCapability(capability, facing)
        }

    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T? =
        when {

            capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> {
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory)
            }

            else -> {
                super.getCapability(capability, facing)
            }

        }

    /**
     * Accept blank frequency cards piped into the machine. Why you'd want to automate putting blanks into a core I
     * frankly have no idea, but hey. You can!
     */
    override fun isValidInput(itemStack: ItemStack?): Boolean =
        itemStack
            ?.takeUnless { it.isEmpty }
            ?.let { it.item is ItemFrequencyCardBlank }
            ?: false

    override fun acceptsEnergyFrom(emitter: IEnergyEmitter?, facing: EnumFacing?) =
        true

    override fun getSinkTier(): Int = 3

    private companion object {
        const val NBT_KEY_INVENTORY = "inventory"
    }

}
