package mods.orca.mffs.blocks.projector

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.client.gui.base.GuiWithPlayerInventory
import net.minecraft.entity.player.InventoryPlayer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT)
class ProjectorGui(container: ProjectorGuiContainer, ) : GuiWithPlayerInventory<ProjectorGuiContainer>(
    container,
    "projector",
    MFFSMod.resource("textures/gui/projector.png")
) {
    init {
        ySize = GUI_HEIGHT
    }

    private companion object {
        const val POWER_METER_X = 93
        const val POWER_METER_Y = 30
        const val POWER_METER_WIDTH = 70
        const val POWER_METER_TEXT_X = 10
        const val POWER_METER_TEXT_Y = 30
        const val POWER_METER_TEXT_LINE_HEIGHT = 10
        const val GUI_HEIGHT = 224
    }
}
