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
import net.minecraft.util.Identifier;

public class Generic1x1GuiDescription extends SyncedGuiDescription {
    public static Identifier progressBarBackground = new Identifier(NovaGenetica.MOD_ID, "textures/gui/progress_bar_background.png");
    public static Identifier progressBarComplete = new Identifier(NovaGenetica.MOD_ID, "textures/gui/progress_bar_complete.png");
    public static Identifier powerBarBackground = new Identifier(NovaGenetica.MOD_ID, "textures/gui/power_bar_background.png");
    public static Identifier powerBarComplete = new Identifier(NovaGenetica.MOD_ID, "textures/gui/power_bar_complete.png");

    public Generic1x1GuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx){
        super(NovaGenetica.GENERIC_1X1_SCREEN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(ctx, 2), getBlockPropertyDelegate(ctx, 4));
        WGridPanel root = new WGridPanel();
        setRootPanel(root);

        setTitleAlignment(HorizontalAlignment.CENTER);

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
