package me.orangemonkey68.novagenetica.mixin;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.gui.Generic1x1GuiDescription;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "inventoryTick", at = @At("TAIL"))
    void inject(World world, Entity entity, int slot, boolean selected, CallbackInfo ci){
        if(!world.isClient){
            if(entity instanceof ServerPlayerEntity){
                ServerPlayerEntity player = (ServerPlayerEntity) entity;
                if(player.currentScreenHandler instanceof Generic1x1GuiDescription){
                    BlockPos pos = ((Generic1x1GuiDescription) player.currentScreenHandler).getBlockPos();
                    if(world.getBlockState(pos).getBlock() == NovaGenetica.GENE_EXTRACTOR_BLOCK){
                        CompoundTag tag = ((ItemStack)(Object)this).getTag();
                        if(tag != null && tag.contains("identified") && tag.contains("ability")){
                            Ability ability = NovaGenetica.ABILITY_REGISTRY.get(new Identifier(tag.getString("ability")));
                            if(tag.getBoolean("identified") && ability != null){
                                sett
                            }
                        }
                    }
                }
            }
        }
    }
}
