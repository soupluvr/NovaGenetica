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

public class GeneExtractorGuiDescription extends SyncedGuiDescription {

    public GeneExtractorGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx) {
        super(NovaGenetica.GENE_EXTRACTOR_SCREEN_HANDLER, syncId, playerInventory, getBlockInventory(ctx, 2), getBlockPropertyDelegate(ctx, 4));
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 180);
        root.add(this.createPlayerInventoryPanel(), 0, 5);

        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);

        WItemSlot outputSlot = new WItemSlot(blockInventory, 1, 1, 1, true);
        outputSlot.setInsertingAllowed(false);

        root.add(itemSlot, 2, 2);
        root.add(outputSlot, 6, 2);

        WBar progressBar = new WBar(NovaGenetica.progressBarBackground, NovaGenetica.progressBarComplete, BaseMachineBlockEntity.PROGRESS_INDEX, BaseMachineBlockEntity.MAX_PROGRESS_INDEX, WBar.Direction.RIGHT);
        root.add(progressBar, 4, 2);

        WPowerBar powerBar = new WPowerBar(NovaGenetica.powerBarBackground, NovaGenetica.powerBarComplete, BaseMachineBlockEntity.STORED_POWER, BaseMachineBlockEntity.MAX_STORED_POWER, WBar.Direction.UP);
//        powerBar.withTooltip(new LiteralText(propertyDelegate.get(BaseMachineBlockEntity.STORED_POWER) + "/" + NovaGenetica.getConfig().machineConfig.maxStoredPower));
        root.add(powerBar, 0, 1, 1, 3);


        root.validate(this);
    }


}