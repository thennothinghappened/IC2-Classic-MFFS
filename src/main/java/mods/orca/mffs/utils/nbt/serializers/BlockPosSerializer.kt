package mods.orca.mffs.utils.nbt.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import net.minecraft.util.math.BlockPos

object BlockPosSerializer : KSerializer<BlockPos> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("net.minecraft.util.math.BlockPos") {
            element<Int>("x")
            element<Int>("y")
            element<Int>("z")
        }

    override fun serialize(encoder: Encoder, value: BlockPos) {
        encoder.encodeStructure(descriptor) {
            encodeIntElement(descriptor, 0, value.x)
            encodeIntElement(descriptor, 1, value.y)
            encodeIntElement(descriptor, 2, value.z)
        }
    }

    override fun deserialize(decoder: Decoder): BlockPos = decoder.decodeStructure(descriptor) {
        var x: Int? = null
        var y: Int? = null
        var z: Int? = null

        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> x = decodeIntElement(descriptor, index)
                1 -> y = decodeIntElement(descriptor, index)
                2 -> z = decodeIntElement(descriptor, index)
                CompositeDecoder.DECODE_DONE -> break
                else -> throw SerializationException("Invalid index $index in decoding $descriptor, expected the range 0, 1, 2")
            }
        }

        requireNotNull(x)
        requireNotNull(y)
        requireNotNull(z)
        BlockPos(x, y, z)
    }
}