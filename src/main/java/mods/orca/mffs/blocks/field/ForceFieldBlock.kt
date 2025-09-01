package mods.orca.mffs.blocks.field

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.WorldFieldManager
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.DamageSource
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@Suppress("OVERRIDE_DEPRECATION")
object ForceFieldBlock : Block(Material.BARRIER) {
    private const val NAME = "forcefield"

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

    override fun breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        val fieldManager = WorldFieldManager.getOrNull(world) ?: return
        val owner = fieldManager.getOwnerId(pos)

        if (owner != null) {
            // Re-generate the field!
            world.setBlockState(pos, state)
        } else {
            super.breakBlock(world, pos, state)
        }
    }

    override fun getCollisionBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB? {
        // FIXME: zapper shouldn't always be on!
        if (true) {
            return AxisAlignedBB(0.01, 0.01, 0.01, 0.99, 0.99, 0.99)
        } else {
            return super.getCollisionBoundingBox(state, source, pos)
        }
    }

    override fun onEntityCollision(world: World, pos: BlockPos, state: IBlockState, entity: Entity) {
        // FIXME: zapper shouldn't always be on!
        if (world.isRemote) {
            return
        }
        
        if (entity is EntityLivingBase) {
            entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 1f)
        }
    }
}
