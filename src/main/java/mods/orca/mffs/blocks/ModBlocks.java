package mods.orca.mffs.blocks;

import mods.orca.mffs.blocks.base.BlockBase;
import mods.orca.mffs.blocks.core.BlockCore;
import mods.orca.mffs.blocks.upgrades.camoflage.BlockUpgradeCamo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {

    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block FORCEFIELD = new BlockBase("forcefield", Material.GLASS).setBlockLayer(BlockRenderLayer.CUTOUT).setBlockUnbreakable();
    public static final Block CORE = new BlockCore();
    public static final Block UPGRADE_CAMOFLAGE = new BlockUpgradeCamo();
}
