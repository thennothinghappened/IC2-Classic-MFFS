package mods.orca.mffs.util.handlers

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import mods.orca.mffs.blocks.core.TileCore
import mods.orca.mffs.blocks.upgrades.camoflage.TileUpgradeCamo
import mods.orca.mffs.client.gui.GuiCore
import mods.orca.mffs.client.gui.GuiUpgradeCamo
import mods.orca.mffs.container.ContainerCore
import mods.orca.mffs.container.ContainerUpgradeCamo
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

class ModGuiHandler : IGuiHandler {

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

        GuiId.Core.ordinal -> ContainerCore(
            player.inventory,
            world.getTileEntity(BlockPos(x, y, z)) as TileCore
        )

        GuiId.CamouflageUpgrade.ordinal -> ContainerUpgradeCamo(
            player.inventory,
            world.getTileEntity(BlockPos(x, y, z)) as TileUpgradeCamo
        )

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

        GuiId.Core.ordinal -> GuiCore(
            getServerGuiElement(id, player, world, x, y, z),
            player.inventory
        )

        GuiId.CamouflageUpgrade.ordinal -> GuiUpgradeCamo(
            getServerGuiElement(id, player, world, x, y, z),
            player.inventory
        )

        else -> null

    }

}
