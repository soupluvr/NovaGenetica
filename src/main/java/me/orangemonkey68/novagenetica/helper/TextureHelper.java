package me.orangemonkey68.novagenetica.helper;

import me.orangemonkey68.novagenetica.NovaGenetica;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**

 */
@Environment(EnvType.CLIENT)
public class TextureHelper {
    private static final Map<Identifier, LivingEntityRenderer<?, ?>> ENTITY_RENDERER_MAP = new HashMap<>();
    private static final Map<Identifier, Integer> TEXTURE_COLOR_CACHE = new HashMap<>();
    public static final int BAD_RETURN = -1;


    @SuppressWarnings("CodeBlock2Expr")
    public static void init(){
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper) -> {
            ENTITY_RENDERER_MAP.put(Registry.ENTITY_TYPE.getId(entityType), entityRenderer);
        });

        NovaGenetica.LOGGER.info("Entity renderer callback registered");
    }

    /**
     * Gets the average color of the texture of the entity.
     * @param textureId the Identifier of the texture used.
     * @return an integer in {@code 0xRRGGBB} format
     */
    public static int getAverageTextureColor(@NotNull Identifier textureId){
        //Return cached option if possible
        if(getCachedColor(textureId).isPresent()){
            return getCachedColor(textureId).get();
        }

        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
        try {
            InputStream inputStream = resourceManager.getResource(textureId).getInputStream();
            NativeImage image = NativeImage.read(inputStream);

            int color = averageColor(image);
            TEXTURE_COLOR_CACHE.put(textureId, color == -1 ? 0xFFFFFF : color);

            return averageColor(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static Optional<Identifier> getEntityTexture(LivingEntity entity){
        Identifier entityId = Registry.ENTITY_TYPE.getId(entity.getType());
        if(ENTITY_RENDERER_MAP.containsKey(entityId)){
            return Optional.of(ENTITY_RENDERER_MAP.get(entityId).getTexture(entityCaptureHelper(entity)));
        }
        return Optional.empty();
    }

    /**
     *
     * @param image the image to get the average color of
     * @return the average color in <b>RGB</b> format
     * @throws IllegalArgumentException if image {@link net.minecraft.client.texture.NativeImage.Format} is not {@code ABGR}
     */
    @SuppressWarnings("PointlessBitwiseExpression")
    public static int averageColor(NativeImage image){

        if(image.getFormat() != NativeImage.Format.ABGR){
            NovaGenetica.LOGGER.error(new IllegalArgumentException("NativeImage format must be ABGR"));
            return -1;
        }

        int r = 0;
        int g = 0;
        int b = 0;
        int count = 0;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
//                NovaGenetica.LOGGER.info(image.getPixelOpacity(x, y));

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

    public static Optional<Integer> getCachedColor(Identifier id){
//        return Optional.of(TEXTURE_COLOR_CACHE.get(id));
        if(TEXTURE_COLOR_CACHE.containsKey(id)){
            return Optional.of(TEXTURE_COLOR_CACHE.get(id));
        } else
            return Optional.empty();
    }

    public static void cacheColor(Identifier id, int color){
        TEXTURE_COLOR_CACHE.putIfAbsent(id, color);
    }
}
