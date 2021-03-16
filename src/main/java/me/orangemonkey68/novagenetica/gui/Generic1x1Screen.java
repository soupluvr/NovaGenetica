package me.orangemonkey68.novagenetica.gui;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class Generic1x1Screen extends CottonInventoryScreen<Generic1x1GuiDescription> {
    public Generic1x1Screen(Generic1x1GuiDescription description, PlayerInventory inv, Text title) {
        super(description, inv.player, title);
    }
}
