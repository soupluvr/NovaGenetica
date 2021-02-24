package me.orangemonkey68.novageneticae.client;

import io.netty.buffer.Unpooled;
import me.orangemonkey68.novageneticae.NovaGeneticae;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class NovaGeneticaeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        KeyBinding giveAbility = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.novageneticae.debug",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.novageneticae.keybinds"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (giveAbility.wasPressed()){
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeString("novageneticae:eat_grass");
                ClientPlayNetworkHandler net = MinecraftClient.getInstance().getNetworkHandler();
                if (net != null) {
                    client.player.sendMessage(new LiteralText("packet sent"), false);
                    net.getConnection().send(new CustomPayloadC2SPacket(new Identifier(NovaGeneticae.MOD_ID, "give_ability_packet"), new PacketByteBuf(buf)));
                }
            }
        });

    }
}
