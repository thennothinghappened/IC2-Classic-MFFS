package mods.orca.mffs.blocks.field

import mods.orca.mffs.MFFSMod
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@Suppress("OVERRIDE_DEPRECATION")
object ForceFieldBlock : Block(Material.BARRIER) {

    private const val NAME = "forcefield"
//    private val FORCEFIELD_TYPE: PropertyEnum<ForceFieldTyps> = PropertyEnum.create("type", ForceFieldTyps::class.java)

    init {
        setRegistryName(NAME)
        setTranslationKey(MFFSMod.translationKey(NAME))
        setBlockUnbreakable()
    }

    @SideOnly(Side.CLIENT)
    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }

    override fun isOpaqueCube(state: IBlockState): Boolean {
        return false
    }

    override fun isFullCube(state: IBlockState): Boolean {
        return false
    }

//    override fun hasTileEntity(state: IBlockState): Boolean = true
//    override fun createTileEntity(world: World, state: IBlockState): ForceFieldTile? {
//
//        return null
//    }

}

class ForceFieldTile : TileEntity() {



}
