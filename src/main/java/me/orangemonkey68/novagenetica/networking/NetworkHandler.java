package me.orangemonkey68.novagenetica.networking;

import io.netty.util.ReferenceCountUtil;
import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.helper.TextureHelper;
import me.orangemonkey68.novagenetica.helper.item.ItemHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

public class NetworkHandler {
    private static Identifier GET_MOB_FLAKES = new Identifier(NovaGenetica.MOD_ID, "get_mob_flakes");
    private static Identifier SEND_MOB_FLAKES = new Identifier(NovaGenetica.MOD_ID, "send_mob_flakes");


//    @Environment(EnvType.CLIENT)
    public static void initClient(){
        ClientPlayNetworking.registerGlobalReceiver(GET_MOB_FLAKES, (client, handler, buf, responseSender) -> {
            int entityId = buf.readInt();
            int hand = buf.readInt();
            NovaGenetica.LOGGER.info("Received message, generating stack");
//            buf.release(); //releases buf from memory
            client.execute(() -> {
                ClientWorld world = client.world;
                if(world != null){
                    Entity entity = world.getEntityById(entityId);
                    if(entity instanceof LivingEntity){
                        LivingEntity livingEntity = (LivingEntity) entity;

                        PacketByteBuf responseBuf = PacketByteBufs.create();
                        responseBuf.writeInt(entityId); //always a good value

                        //defaults to bad value, reassigned if good value is present
                        int color = -1;

                        Optional<Identifier> texOpt = TextureHelper.getEntityTexture(livingEntity);
                        if(texOpt.isPresent()){
                            Identifier tex = texOpt.get();
                            color = TextureHelper.getAverageTextureColor(tex);
                        }

                        ItemStack stack = ItemHelper.getMobFlakes(Registry.ENTITY_TYPE.getId(entity.getType()), color);
                        responseBuf.writeItemStack(stack);
                        responseBuf.writeInt(hand);

                        NovaGenetica.LOGGER.info(responseBuf.refCnt());
                        responseSender.sendPacket(SEND_MOB_FLAKES, responseBuf);
                        NovaGenetica.LOGGER.info("Response sent");
                    }
                }
            });
        });
        NovaGenetica.LOGGER.info("Client packets registered");
    }

//    @Environment(EnvType.SERVER)
    public static void initServer(){
        ServerPlayNetworking.registerGlobalReceiver(SEND_MOB_FLAKES, (server, player, handler, buf, responseSender) -> {
            NovaGenetica.LOGGER.info("Received response");

            ReferenceCountUtil.retain(buf);
            int entityId = buf.readInt();
            ItemStack stack = buf.readItemStack();
            Hand hand = Hand.values()[buf.readInt()];

            server.execute(() -> {
                ServerWorld world = player.getServerWorld();
                if (world != null) {
                    Entity entity = world.getEntityById(entityId);
                    if (entity instanceof LivingEntity) {
                        DamageSource damageSource = DamageSource.player(player);
                        if(!entity.isInvulnerableTo(damageSource)){ //only try if the entity isn't invulnerable to the player
                            if(entity.damage(DamageSource.player(player), 1)){ //only succeed if the entity takes damage
                                entity.dropStack(stack); // drop stack
                                player.getStackInHand(hand).damage(1, player, callback -> callback.sendToolBreakStatus(hand)); // damage tool
                            }


                        }
                    }
                }
            });
        });
        NovaGenetica.LOGGER.info("Server packets registered");
    }

//    @Environment(EnvType.SERVER)
    public static void dropMobFlakes(int entityId, ServerPlayerEntity player, Hand hand){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entityId);
        buf.writeInt(hand.ordinal());
        NovaGenetica.LOGGER.info("Asking for stack");

        ServerPlayNetworking.send(player, GET_MOB_FLAKES, buf);
    }
}
