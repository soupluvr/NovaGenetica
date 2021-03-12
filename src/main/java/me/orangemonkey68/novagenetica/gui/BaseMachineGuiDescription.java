package me.orangemonkey68.novagenetica.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.blockentity.BaseMachineBlockEntity;
import me.orangemonkey68.novagenetica.gui.widget.WPowerBar;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.LiteralText;

import java.util.Objects;

public class BaseMachineGuiDescription extends SyncedGuiDescription {
    private final int outputSlotIndex;

    public BaseMachineGuiDescription(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx, int syncedProperties, int invSize, int guiSizeX, int guiSizeY, int outputSlotIndex) {
        super(type, syncId, playerInventory, getBlockInventory(ctx, invSize), getBlockPropertyDelegate(ctx, syncedProperties + 4));
        this.outputSlotIndex = outputSlotIndex;

        setTitleAlignment(HorizontalAlignment.CENTER);

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(guiSizeX, guiSizeY);

        WBar progressBar = new WBar(NovaGenetica.progressBarBackground, NovaGenetica.progressBarComplete, BaseMachineBlockEntity.PROGRESS_INDEX, BaseMachineBlockEntity.MAX_PROGRESS_INDEX, WBar.Direction.RIGHT);
        root.add(progressBar, 4, 2);

        WPowerBar powerBar = new WPowerBar(NovaGenetica.powerBarBackground, NovaGenetica.powerBarComplete, BaseMachineBlockEntity.STORED_POWER, BaseMachineBlockEntity.MAX_STORED_POWER, WBar.Direction.UP);
        powerBar.withTooltip(new LiteralText(propertyDelegate.get(BaseMachineBlockEntity.STORED_POWER) + "/" + NovaGenetica.getConfig().machineConfig.maxStoredPower));
        root.add(powerBar, 0, 1, 1, 3);

        WItemSlot outputSlot = new WItemSlot(blockInventory, outputSlotIndex, 1, 1, true);
        outputSlot.setInsertingAllowed(false);
        root.add(outputSlot, 6, 2);
        root.add(this.createPlayerInventoryPanel(), 0, 5 );

        root.validate(this);
    }



    int getOutputSlotIndex(){
        return outputSlotIndex;
    }
}
