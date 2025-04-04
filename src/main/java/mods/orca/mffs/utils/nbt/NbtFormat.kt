package mods.orca.mffs.utils.nbt

import kotlinx.serialization.*
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.nbt.NBTBase

/**
 * (De)serialize type-safe data as NBT using [kotlinx.serialization]'s custom format support.
 */
@ExperimentalSerializationApi
sealed class NbtFormat(
    val config: NbtConfig,
    override val serializersModule: SerializersModule,
) : SerialFormat {

    fun <T> encode(serializer: SerializationStrategy<T>, value: T): NBTBase {
        val encoder = NbtEncoder(this)
        encoder.encodeSerializableValue(serializer, value)
        return encoder.rootNbt
    }

    fun <T> decode(deserializer: DeserializationStrategy<T>, nbt: NBTBase): T {
        val decoder = NbtDecoder(nbt, this)
        return decoder.decodeSerializableValue(deserializer)
    }

    inline fun <reified T> encode(value: T): NBTBase =
        encode(serializer(), value)

    inline fun <reified T> decode(nbt: NBTBase): T =
        decode(serializer(), nbt)

    /**
     * The default NBT serialization configuration.
     */
    companion object Default : NbtFormat(NbtConfig(), EmptySerializersModule())

}
