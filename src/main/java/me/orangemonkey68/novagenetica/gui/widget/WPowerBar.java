package me.orangemonkey68.novagenetica.gui.widget;

import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import me.orangemonkey68.novagenetica.blockentity.BaseMachineBlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class WPowerBar extends WBar {
    public WPowerBar(Texture bg, Texture bar, int field, int maxfield) {
        super(bg, bar, field, maxfield);
    }

    public WPowerBar(Texture bg, Texture bar, int field, int maxfield, Direction dir) {
        super(bg, bar, field, maxfield, dir);
    }

    public WPowerBar(Identifier bg, Identifier bar, int field, int maxfield) {
        super(bg, bar, field, maxfield);
    }

    public WPowerBar(Identifier bg, Identifier bar, int field, int maxfield, Direction dir) {
        super(bg, bar, field, maxfield, dir);
    }

    @Override
    public void tick() {
        super.tick();
        //update tooltip every tick
        super.tooltipTextComponent = new LiteralText(properties.get(BaseMachineBlockEntity.STORED_POWER) + "/" + properties.get(BaseMachineBlockEntity.MAX_STORED_POWER));
    }
}
