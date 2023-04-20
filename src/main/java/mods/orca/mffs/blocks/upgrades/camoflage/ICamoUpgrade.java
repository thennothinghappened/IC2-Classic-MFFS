package mods.orca.mffs.blocks.upgrades.camoflage;

import mods.orca.mffs.blocks.upgrades.IProjectorUpgrade;
import mods.orca.mffs.blocks.upgrades.IUpgrade;
import net.minecraft.block.Block;

import javax.annotation.Nullable;

public interface ICamoUpgrade extends IProjectorUpgrade {
    @Nullable
    Block getBlockMask();
}
