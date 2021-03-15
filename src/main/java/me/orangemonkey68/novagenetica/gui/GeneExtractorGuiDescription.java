package me.orangemonkey68.novagenetica.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.blockentity.BaseMachineBlockEntity;
import me.orangemonkey68.novagenetica.gui.widget.WPowerBar;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.LiteralText;

public class GeneExtractorGuiDescription extends BaseMachineGuiDescription {

    public GeneExtractorGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx) {
        super(NovaGenetica.GENE_EXTRACTOR_SCREEN_HANDLER, syncId, playerInventory, ctx, 0, 2);
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 180);

        //add power and progress bars
        addProgressBar(4, 2);
        addPowerBar(0, 1, 1, 3);

        //add input and output slots
        addItemSlot(2, 2, 0);
        addItemSlot(6, 2, new WItemSlot(blockInventory, 1, 1, 1, true));

        addPlayerInv(0, 5);

        root.validate(this);
    }


}