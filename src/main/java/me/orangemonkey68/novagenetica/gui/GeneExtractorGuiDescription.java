package me.orangemonkey68.novagenetica.gui;

import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import me.orangemonkey68.novagenetica.NovaGenetica;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class GeneExtractorGuiDescription extends BaseMachineGuiDescription {

    public GeneExtractorGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx) {
        super(NovaGenetica.GENE_EXTRACTOR_SCREEN_HANDLER, syncId, playerInventory, ctx, 0, 2, 150, 180, 1);
        WGridPanel root = (WGridPanel) getRootPanel();

        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
        root.add(itemSlot, 2, 2);

//        WItemSlot outputSlot = new WItemSlot(blockInventory, 1, 1, 1, true);
//        outputSlot.setInsertingAllowed(false);
//        root.add(outputSlot, 6, 2);


        root.validate(this);
    }


}