package mods.orca.mffs.utils.nbt

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagByte
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagDouble
import net.minecraft.nbt.NBTTagFloat
import net.minecraft.nbt.NBTTagInt
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagLong
import net.minecraft.nbt.NBTTagShort
import net.minecraft.nbt.NBTTagString

/**
 * Encoder for writing data as structured NBT that can be saved or sent across the network.
 */
@ExperimentalSerializationApi
class NbtEncoder(private val format: NbtFormat) : AbstractEncoder() {

    override val serializersModule: SerializersModule
        get() = format.serializersModule

    private val nbtStack = mutableListOf<NBTBase>()
    private val keyStack = mutableListOf<String>()
    private val indexStack = mutableListOf<Int>()

    private val currentStructureOrNull
        get() = nbtStack.lastOrNull()

    val rootNbt
        get() = nbtStack.first()

    /**
     * Encode the given [nbt]. If there is no current NBT, then this must be the top-level element.
     */
    private fun encode(nbt: NBTBase) {

        when (val current = currentStructureOrNull) {

            // We're adding a key/value pair to the current map.
            is NBTTagCompound -> current.setTag(keyStack.removeLast(), nbt)

            // We're adding an element to a list.
            is NBTTagList -> {
                val index = indexStack.removeLast()

                if (index >= current.tagCount()) {
                    current.appendTag(nbt)
                } else {
                    current.set(indexStack.removeLast(), nbt)
                }
            }

            // No action required - this becomes the root node.
            null -> {}

            // We've been asked to encode data into a non-structural node - i.e., one with a singular member.
            //
            // This doesn't make much sense, since to get here the caller must have attempted to encode more than one
            // element into a single-element node.
            //
            // This is not an outright error, as we might use this path in the future for the specialised array types,
            // for instance. I'm not yet sure how that might look.
            else -> TODO("Cannot currently encode non-structural data at the top-level")

        }

        if (nbt is NBTTagCompound || nbt is NBTTagList) {
            // We're opening a new structure.
            nbtStack.add(nbt)
        }

    }

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        // This method gives us a chance to look at the descriptor so we can snatch the key name or list index.
        when (currentStructureOrNull) {
            is NBTTagCompound -> keyStack.add(descriptor.getElementName(index))
            is NBTTagList -> indexStack.add(index)
            else -> Unit
        }
        return true
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {

        when (descriptor.kind) {
            is StructureKind.LIST -> encode(NBTTagList())
            else -> encode(NBTTagCompound())
        }

        return this
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        if (nbtStack.size > 1) {
            nbtStack.removeLast()
        }
    }

    override fun encodeNull() {
        encode(NBTTagNull)
    }

    override fun encodeString(value: String) {
        // Reserve `@something...` for sentinels (namely `@null`)
        val string = if (value.startsWith('@')) "@$value" else value
        encode(NBTTagString(string))
    }

    override fun encodeByte(value: Byte) = encode(NBTTagByte(value))
    override fun encodeBoolean(value: Boolean) = encode(NBTTagByte(if (value) 1 else 0))
    override fun encodeInt(value: Int) = encode(NBTTagInt(value))
    override fun encodeLong(value: Long) = encode(NBTTagLong(value))
    override fun encodeShort(value: Short): Unit = encode(NBTTagShort(value))
    override fun encodeFloat(value: Float): Unit = encode(NBTTagFloat(value))
    override fun encodeDouble(value: Double): Unit = encode(NBTTagDouble(value))
    override fun encodeChar(value: Char): Unit = encode(NBTTagString(value.toString()))
    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int): Unit = encode(NBTTagString(enumDescriptor.getElementName(index)))

}
