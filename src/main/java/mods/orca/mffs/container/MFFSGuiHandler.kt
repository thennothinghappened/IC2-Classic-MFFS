package mods.orca.mffs.container

import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import mods.orca.mffs.blocks.core.TileForceFieldCore
import mods.orca.mffs.client.gui.GuiForceFieldCore
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

object MFFSGuiHandler : IGuiHandler {

    /**
     * Unique, serializable (by [ordinal]) identifier for each GUI screen.
     */
    enum class GuiId {
        Core,
        CamouflageUpgrade
    }

    override fun getServerGuiElement(
        id: Int,
        player: EntityPlayer,
        world: World,
        x: Int,
        y: Int,
        z: Int
    ): Container? = when (id) {

        GuiId.Core.ordinal -> world.getTileEntity(BlockPos(x, y, z))
            ?.let { it as? TileForceFieldCore }
            ?.let { ContainerForceFieldCore(player.inventory, it) }

//        GuiId.CamouflageUpgrade.ordinal -> ContainerUpgradeCamo(
//            player.inventory,
//            world.getTileEntity(BlockPos(x, y, z)) as? TileCore
//        )

        else -> null

    }

    @SideOnly(Side.CLIENT)
    override fun getClientGuiElement(
        id: Int,
        player: EntityPlayer,
        world: World,
        x: Int,
        y: Int,
        z: Int
    ): GuiContainer? = when (id) {

        GuiId.Core.ordinal -> getServerGuiElement(id, player, world, x, y, z)
            ?.let { it as? ContainerForceFieldCore }
            ?.let { GuiForceFieldCore(it, player.inventory) }

//        GuiId.CamouflageUpgrade.ordinal -> getServerGuiElement(id, player, world, x, y, z)
//            ?.let { container ->
//                GuiUpgradeCamo(
//                    container,
//                    player.inventory
//                )
//            }

        else -> null

    }

}
