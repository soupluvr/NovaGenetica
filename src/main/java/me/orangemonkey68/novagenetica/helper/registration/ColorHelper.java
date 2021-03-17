package me.orangemonkey68.novagenetica.helper.registration;

import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ColorHelper {
    private static Map<Identifier, LivingEntityRenderer<?, ?>> ENTITY_RENDERER_MAP = new HashMap<>();
    private static Map<Identifier, Integer> TEXTURE_COLOR_CACHE = new HashMap<>();


    public static void init(){
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper) -> {
            ENTITY_RENDERER_MAP.put(Registry.ENTITY_TYPE.getId(entityType), entityRenderer);
        });
    }

    public static int getColor(LivingEntity entity){
        EntityType<?> type = entity.getType();
        Identifier entityId = Registry.ENTITY_TYPE.getId(entity);

        //return the cached color if present
        if(TEXTURE_COLOR_CACHE.containsKey(entityId)){
            return TEXTURE_COLOR_CACHE.get(entityId);
        } else {
            LivingEntityRenderer<?, ?> renderer = ENTITY_RENDERER_MAP.get(entityId);
            Identifier textureId = renderer.getTexture(entityCaptureHelper(entity));
            Optional<ModContainer> opt = FabricLoader.getInstance().getModContainer(textureId.getNamespace());

            //TODO: finish file retrieval
            opt.ifPresent(modContainer -> {
                Path path = modContainer.getPath(textureId.getPath()).toAbsolutePath();
            });
        }


    }

    private static <T extends Entity> T entityCaptureHelper(Entity entity){
        return (T) entity;
    }
}
