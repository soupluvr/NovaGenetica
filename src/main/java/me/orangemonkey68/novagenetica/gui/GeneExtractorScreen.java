package me.orangemonkey68.novagenetica.gui;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class GeneExtractorScreen extends CottonInventoryScreen<GeneExtractorGuiDescription> {
    public GeneExtractorScreen(GeneExtractorGuiDescription description, PlayerEntity player, Text title) {
        super(description, player, title);
    }
}
