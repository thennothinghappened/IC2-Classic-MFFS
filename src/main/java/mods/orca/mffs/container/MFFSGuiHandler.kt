package mods.orca.mffs.container

import mods.orca.mffs.MFFSMod
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
    enum class Gui {
        Core,
        CamouflageUpgrade
    }

    /**
     * Shorthand for opening a GUI for the given player, without having to specify our mod ID, or
     * use the ordinal value of the GUI in question.
     *
     * @param player The player to open the GUI for.
     * @param gui The GUI to open.
     * @param world The world this happened in.
     * @param pos The position of the tile the GUI is being opened from.
     */
    fun openGui(
        player: EntityPlayer,
        gui: Gui,
        world: World,
        pos: BlockPos
    ) = player.openGui(MFFSMod.instance, gui.ordinal, world, pos.x, pos.y, pos.z)

    override fun getServerGuiElement(
        id: Int,
        player: EntityPlayer,
        world: World,
        x: Int,
        y: Int,
        z: Int
    ): Container? = when (id) {

        Gui.Core.ordinal -> world.getTileEntity(BlockPos(x, y, z))
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

        Gui.Core.ordinal -> getServerGuiElement(id, player, world, x, y, z)
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
