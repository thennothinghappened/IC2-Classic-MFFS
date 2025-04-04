package mods.orca.mffs.utils.nbt

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTPrimitive
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString

/**
 * Decoder for reading data from structured NBT back to a type-safe form.
 */
@ExperimentalSerializationApi
class NbtDecoder(nbt: NBTBase, private val format: NbtFormat) : AbstractDecoder() {

    override val serializersModule: SerializersModule
        get() = format.serializersModule

    private val nbtStack = mutableListOf(StackEntry(nbt))
    private val currentEntry
        get() = nbtStack.last()

    private data class StackEntry(
        val nbt: NBTBase,
        var readIndex: Int = -1
    ) {
        lateinit var descriptor: SerialDescriptor

        /**
         * Read the next element of the structure.
         */
        inline fun <reified T : NBTBase> decode(): T = when (nbt) {
            is NBTTagCompound -> nbt.getTag(descriptor.getElementName(readIndex))
            is NBTTagList -> nbt.get(readIndex)
            else -> nbt
        } as T
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {

        val entry = currentEntry

        val count = when (val nbt = entry.nbt) {
            is NBTTagCompound -> nbt.size
            is NBTTagList -> nbt.tagCount()
            // FIXME: Support for specialised array types.
            else -> 1
        }

        entry.readIndex++
        return if (entry.readIndex < count) entry.readIndex else CompositeDecoder.DECODE_DONE

    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {

        val current = nbtStack.last()

        if (nbtStack.size == 1 && current.readIndex < 0) {
            // We're defining the root structure's descriptor.
            current.descriptor = descriptor
            return this
        }

        //  We're decoding a member structure.
        val childNbt = when (descriptor.kind) {
            is StructureKind.LIST -> current.decode<NBTTagList>()
            else -> current.decode<NBTTagCompound>()
        }

        val child = StackEntry(childNbt).apply { this.descriptor = descriptor }
        nbtStack.add(child)

        return this
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        nbtStack.removeLast()
    }

    override fun decodeString(): String = currentEntry.decode<NBTTagString>().string
    override fun decodeByte(): Byte = currentEntry.decode<NBTPrimitive>().byte
    override fun decodeBoolean(): Boolean = currentEntry.decode<NBTPrimitive>().byte.toInt() != 0
    override fun decodeInt(): Int = currentEntry.decode<NBTPrimitive>().int
    override fun decodeLong(): Long = currentEntry.decode<NBTPrimitive>().long
    override fun decodeShort(): Short = currentEntry.decode<NBTPrimitive>().short
    override fun decodeFloat(): Float = currentEntry.decode<NBTPrimitive>().float
    override fun decodeDouble(): Double = currentEntry.decode<NBTPrimitive>().double
    override fun decodeChar(): Char = currentEntry.decode<NBTTagString>().string.first()
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = enumDescriptor.getElementIndex(currentEntry.decode<NBTTagString>().string)

}
