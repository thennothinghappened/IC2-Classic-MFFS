package mods.orca.mffs.container

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.blocks.core.gui.ForceFieldCoreContainer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import mods.orca.mffs.blocks.projector.ProjectorGui
import mods.orca.mffs.blocks.projector.ProjectorGuiContainer
import mods.orca.mffs.blocks.core.gui.ForceFieldCoreGui
import mods.orca.mffs.registry.Registry
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
        Projector,
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
        Gui.Core.ordinal -> Registry.Blocks.forceFieldCore.getTileEntity(world, BlockPos(x, y, z))?.let {
            ForceFieldCoreContainer(player.inventory, it)
        }

        Gui.Projector.ordinal -> Registry.Blocks.fieldProjector.getTileEntity(world, BlockPos(x, y, z))?.let {
            ProjectorGuiContainer(player.inventory, it)
        }

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
    ): GuiContainer? {
        val container = getServerGuiElement(id, player, world, x, y, z) ?: return null

        return when (id) {
            Gui.Core.ordinal -> ForceFieldCoreGui(container as ForceFieldCoreContainer)
            Gui.Projector.ordinal -> ProjectorGui(container as ProjectorGuiContainer)
            else -> error("Mismatched GUI container list")
        }
    }

}
