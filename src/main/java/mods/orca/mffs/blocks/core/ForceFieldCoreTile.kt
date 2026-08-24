package mods.orca.mffs.blocks.core

import ic2.api.energy.tile.IEnergyEmitter
import mods.orca.mffs.CoreId
import mods.orca.mffs.WorldFieldManager
import mods.orca.mffs.blocks.base.tile.TileMachine
import mods.orca.mffs.items.ItemFrequencyCard
import mods.orca.mffs.items.ItemFrequencyCardBlank
import mods.orca.mffs.registry.Registry
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler

/**
 * The Tile entity for the force-field core, which stores the power and on/off state of the assembly.
 */
class ForceFieldCoreTile : TileMachine(10000000.0) {
    /**
     * Unique ID of this core.
     */
    val coreId: CoreId
        get() = WorldFieldManager.get(world).getCoreId(this)

    /**
     * The inventory for the core, which simply contains a slot for linking frequency cards to this core.
     */
    private val inventory = object : ItemStackHandler(1) {
        override fun isItemValid(slot: Int, stack: ItemStack) =
            stack.item === Registry.Items.frequencyCardBlank

        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            return if (isItemValid(slot, stack)) super.insertItem(slot, stack, simulate) else stack
        }

        override fun onContentsChanged(slot: Int) {
            if (world.isRemote) {
                return
            }

            val stack = getStackInSlot(slot)
            val linkedCardStack = linkBlankCardStack(stack)

            if (linkedCardStack != null) {
                // We specifically avoid `setStackInSlot()` or we'll call this method pointlessly a 2nd time.
                stacks[slot] = linkedCardStack
                markDirty()
            }
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
        if (stack.item !== Registry.Items.frequencyCardBlank) {
            return null
        }

        return ItemStack(Registry.Items.frequencyCard, stack.count, 0).apply {
            if (!world.isRemote) {
                tagCompound = NBTTagCompound().apply {
                    setInteger(ItemFrequencyCard.NBTKey.CoreId.name, coreId.value)
                }
            }
        }
    }

    fun onDestroy() {
        if (!hasWorld() || world.isRemote) {
            return
        }

        WorldFieldManager.get(world).removeCore(this)
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        compound.setTag(NBT_KEY_INVENTORY, inventory.serializeNBT())
        return super.writeToNBT(compound)
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        inventory.deserializeNBT(compound.getCompoundTag(NBT_KEY_INVENTORY))
        super.readFromNBT(compound)
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?) = when {
        capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> true
        else -> super.hasCapability(capability, facing)
    }

    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T? = when {
        capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory)
        else -> super.getCapability(capability, facing)
    }

    /**
     * Accept blank frequency cards piped into the machine. Why you'd want to automate putting blanks into a core I
     * frankly have no idea, but hey. You can!
     */
    override fun isValidInput(itemStack: ItemStack): Boolean =
        itemStack.item === Registry.Items.frequencyCardBlank

    // We don't directly accept energy, it can only be input by the EU injector (presumably this was originally a cross-
    // compatibility thing in MFFS, but I'm going for nostalgia!)
    override fun acceptsEnergyFrom(emitter: IEnergyEmitter, facing: EnumFacing) = false

    override fun getSinkTier(): Int = IC2_TIER

    companion object {
        private const val NBT_KEY_INVENTORY = "inventory"
        const val IC2_TIER = 3
    }
}
