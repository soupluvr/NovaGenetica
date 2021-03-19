package me.orangemonkey68.novagenetica.helper;

import me.orangemonkey68.novagenetica.NovaGenetica;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

/**

 */
@Environment(EnvType.CLIENT)
public class ColorHelper {
    private static final Map<Identifier, LivingEntityRenderer<?, ?>> ENTITY_RENDERER_MAP = new HashMap<>();
    private static final Map<Identifier, Integer> TEXTURE_COLOR_CACHE = new HashMap<>();
    public static final int BAD_RETURN = 0xFFFFFF00;


    public static void init(){
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper) -> {
            ENTITY_RENDERER_MAP.put(Registry.ENTITY_TYPE.getId(entityType), entityRenderer);
        });
    }

    /**
     * Gets the average color of the texture of the entity.
     * @param entity the entity used to look up and retrieve the texture
     * @return an integer in {@code 0xRRGGBB} format
     */
    public static int getEntityColor(@NotNull LivingEntity entity){
        EntityType<?> type = entity.getType();
        Identifier entityId = Registry.ENTITY_TYPE.getId(type);

        //return the cached color if present
        if(TEXTURE_COLOR_CACHE.containsKey(entityId)){
            return TEXTURE_COLOR_CACHE.get(entityId);
        } else {
            Path path = getTexturePath(entity);

            if(path != null){
                File imageFile = new File(path.toUri());

                try {
                    FileInputStream inputStream = new FileInputStream(imageFile);
                    NativeImage nativeImage = NativeImage.read(inputStream);


                    //final color
                    return averageColor(nativeImage);

                } catch (IOException e) {
                    NovaGenetica.LOGGER.error(e);
                }
            }
        }

        return BAD_RETURN;
    }

    private static Path getTexturePath(Entity entity){
        EntityType<?> entityType = entity.getType();
        Identifier entityId = Registry.ENTITY_TYPE.getId(entityType);

        LivingEntityRenderer<?, ?> renderer = ENTITY_RENDERER_MAP.get(entityId);
        Identifier textureId = checkTexturePath(renderer.getTexture(entityCaptureHelper(entity)));

        Optional<ModContainer> opt = FabricLoader.getInstance().getModContainer(textureId.getNamespace());

        return opt.map(modContainer -> modContainer.getPath(textureId.getPath())).orElse(null);
    }

    private static Identifier checkTexturePath(Identifier id){

        if(!id.getNamespace().equals("minecraft")){
            return id;
        } else {
            String namespace = "novagenetica";
            String path = "assets/novagenetica/textures/minecraft_repack" + id.getPath().substring(8);

            NovaGenetica.LOGGER.info(id.toString());
            NovaGenetica.LOGGER.info(path);


            return new Identifier(namespace, path);
        }
    }


    /**
     *
     * @param image the image to get the average color of
     * @return the average color in <b>RGB</b> format
     * @throws IllegalArgumentException if image {@link net.minecraft.client.texture.NativeImage.Format} is not {@code ABGR}
     */
    @SuppressWarnings("PointlessBitwiseExpression")
    public static int averageColor(NativeImage image) throws IllegalArgumentException{

        if(image.getFormat() != NativeImage.Format.ABGR) throw new IllegalArgumentException("NativeImage format must be ABGR");

        int r = 0;
        int g = 0;
        int b = 0;
        int count = 0;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                NovaGenetica.LOGGER.info(image.getPixelOpacity(x, y));

                if(!image.getFormat().hasOpacityChannel() || image.getPixelOpacity(x, y) == -1){
                    int color = image.getPixelColor(x, y);

                    r += ((color >> 0) & 0xFF) * ((color >> 0) & 0xFF);
                    g += ((color >> 8) & 0xFF) * ((color >> 8) & 0xFF);
                    b += ((color >> 16) & 0xFF) * ((color >> 16) & 0xFF);

                    count++;
                }
            }
        }

        return
                (((int) Math.round(Math.sqrt(r/(float) count))) << 16) |
                (((int)Math.round(Math.sqrt(g/(float) count))) << 8)   |
                (((int)Math.round(Math.sqrt(b/(float) count))) << 0);

    }

    //This is kind of a sketchy cast, but I'm sure that won't cause issues in the future
    @SuppressWarnings("unchecked")
    private static <T extends Entity> T entityCaptureHelper(Entity entity){
        return (T) entity;
    }
}
