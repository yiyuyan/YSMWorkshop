package cn.ksmcbrigade.ysm_ws.mixin;

import cn.ksmcbrigade.ysm_ws.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static cn.ksmcbrigade.ysm_ws.YSMWorkshop.*;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "stop",at = @At("HEAD"))
    public void stop(CallbackInfo ci){
        stop = true;
    }

    @Inject(method = "<init>",at = @At("TAIL"))
    public void init(GameConfig p_91084_, CallbackInfo ci){
        new Thread(()->{
            while (!stop){
                try {
                    if(Utils.moveYSGPContentFiles(tempYSMDownloaded.getAbsolutePath(),ysmCustom.getAbsolutePath())>0){
                        reload_YSM();
                    }
                    Thread.sleep(2500L);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
