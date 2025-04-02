package mods.orca.mffs.utils.nbt

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.EmptySerializersModule
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

@ExperimentalSerializationApi
fun encodeToNbtCompound()

@ExperimentalSerializationApi
private class NbtEncoder private constructor(structure: Structure) : AbstractEncoder() {

    override val serializersModule: SerializersModule = EmptySerializersModule()
    private val structStack = mutableListOf(structure)

    private val keyStack = mutableListOf<String>()
    private val indexStack = mutableListOf<Int>()

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        when (structStack.last()) {
            is Structure.Compound -> keyStack.add(descriptor.getElementName(index))
            is Structure.List -> indexStack.add(index)
        }

        return true
    }

    private fun encode(value: NBTBase) = when (val current = structStack.last()) {
        is Structure.Compound -> current.nbt.setTag(keyStack.removeLast(), value)
        is Structure.List -> current.nbt.set(indexStack.removeLast(), value)
    }

    override fun encodeString(value: String) = encode(NBTTagString(value))
    override fun encodeByte(value: Byte) = encode(NBTTagByte(value))
    override fun encodeBoolean(value: Boolean) = encode(NBTTagByte(if (value) 1 else 0))
    override fun encodeInt(value: Int) = encode(NBTTagInt(value))
    override fun encodeLong(value: Long) = encode(NBTTagLong(value))
    override fun encodeShort(value: Short): Unit = encode(NBTTagShort(value))
    override fun encodeFloat(value: Float): Unit = encode(NBTTagFloat(value))
    override fun encodeDouble(value: Double): Unit = encode(NBTTagDouble(value))
    override fun encodeChar(value: Char): Unit = encode(NBTTagString(value.toString()))
    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int): Unit = encode(NBTTagString(enumDescriptor.getElementName(index)))

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        val next = when (descriptor.kind) {
            StructureKind.MAP -> Structure.Compound()
            StructureKind.LIST -> Structure.List()
            else -> TODO("Structure type for $descriptor not yet implemented!")
        }

        structStack.add(next)
        return this
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        val child = structStack.removeLast()
        when (val current = structStack.last()) {
            is Structure.Compound -> current.nbt.setTag(descriptor.serialName, child.nbt)
            is Structure.List -> current.nbt.appendTag(child.nbt)
        }
    }

    sealed interface Structure {

        val nbt: NBTBase

        data class Compound(override val nbt: NBTTagCompound = NBTTagCompound()) : Structure
        data class List(override val nbt: NBTTagList = NBTTagList()) : Structure

    }

}
