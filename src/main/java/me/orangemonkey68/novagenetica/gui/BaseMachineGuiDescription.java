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
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.Objects;

public abstract class BaseMachineGuiDescription extends SyncedGuiDescription {
    public static Identifier progressBarBackground = new Identifier(NovaGenetica.MOD_ID, "textures/gui/progress_bar_background.png");
    public static Identifier progressBarComplete = new Identifier(NovaGenetica.MOD_ID, "textures/gui/progress_bar_complete.png");
    public static Identifier powerBarBackground = new Identifier(NovaGenetica.MOD_ID, "textures/gui/power_bar_background.png");
    public static Identifier powerBarComplete = new Identifier(NovaGenetica.MOD_ID, "textures/gui/power_bar_complete.png");


    public BaseMachineGuiDescription(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx, int syncedProperties, int invSize) {
        super(type, syncId, playerInventory, getBlockInventory(ctx, invSize), getBlockPropertyDelegate(ctx, syncedProperties + 4));

        setTitleAlignment(HorizontalAlignment.CENTER);

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
    }

    public void addPlayerInv(int x, int y){
        ((WGridPanel)getRootPanel()).add(this.createPlayerInventoryPanel(), x, y);
    }

    public void addPowerBar(int x, int y, int width, int height){
        ((WGridPanel)getRootPanel()).add(new WPowerBar(
                powerBarBackground,
                powerBarComplete,
                BaseMachineBlockEntity.STORED_POWER,
                BaseMachineBlockEntity.MAX_STORED_POWER, WBar.Direction.UP
        ), x, y, width, height);
    }

    public void addProgressBar(int x, int y){
        ((WGridPanel)getRootPanel()).add(new WBar(
                progressBarBackground,
                progressBarComplete,
                BaseMachineBlockEntity.PROGRESS_INDEX,
                BaseMachineBlockEntity.MAX_PROGRESS_INDEX,
                WBar.Direction.RIGHT
        ), x, y);
    }

    public void addItemSlot(int x, int y, int index){
        ((WGridPanel)getRootPanel()).add(WItemSlot.of(blockInventory, index), x, y);
    }

    public void addItemSlot(int x, int y, WItemSlot slot){
        ((WGridPanel)getRootPanel()).add(slot, x ,y);
    }
}
