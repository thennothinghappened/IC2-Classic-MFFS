package mods.orca.mffs.blocks.projector

import kotlinx.serialization.Serializable
import net.minecraft.util.math.Vec3i
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A signed-distance function producing a given field shape.
 */
@Serializable
sealed interface FieldPerimeterSdf {

    /**
     * Calculate the signed distance to the edge of this field shape.
     */
    operator fun invoke(x: Int, y: Int, z: Int): Double

    /**
     * Half of the length, width and height of the field. Determines the area that encompasses the SDF.
     */
    val halfSize: Vec3i

    @Serializable
    data class Sphere(val radius: Int = 5) : FieldPerimeterSdf {

        override fun invoke(x: Int, y: Int, z: Int): Double = sqrt(
            x.toDouble().pow(2) +
                    y.toDouble().pow(2) +
                    z.toDouble().pow(2)
        ) - radius.toDouble()

        override val halfSize: Vec3i
            get() = Vec3i(radius, radius, radius)

    }

    @Serializable
    data class Cube(val radius: Int = 5) : FieldPerimeterSdf {

        override fun invoke(x: Int, y: Int, z: Int): Double = maxOf(x, y, z).toDouble() - radius.toDouble()

        override val halfSize: Vec3i
            get() = Vec3i(radius, radius, radius)

    }

}