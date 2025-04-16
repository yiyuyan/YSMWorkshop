package cn.ksmcbrigade.ysm_ws;

import com.elfmcys.yesstevemodel.OOoOOO00OoO0OoO000ooo00o;
import com.elfmcys.yesstevemodel.o0oO000oOOOO00o0OO0oooo0;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

import java.io.File;
import java.util.concurrent.TimeUnit;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(YSMWorkshop.MODID)
public class YSMWorkshop {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "ysm_ws";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static boolean stop = false;
    public static final File tempYSMDownloaded = new File("ysm_workshop");
    public static final File ysmCustom = new File("config/yes_steve_model/custom/");

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public YSMWorkshop(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        tempYSMDownloaded.mkdir();
        LOGGER.warn(ModLoadingContext.get().getActiveContainer().getModInfo().getDescription());
        LOGGER.info("YSMWorkShop Loaded.");
    }

    public static void reload_YSM(){
            StopWatch var1 = StopWatch.createStarted();
            Minecraft MC = Minecraft.getInstance();
            boolean var2 = o0oO000oOOOO00o0OO0oooo0.oOO0OOoOOOO0OoooOO0oO000((var2x) -> {
                if (var2x.Ooo000oOo0000oo00o00o000() != null) {
                    MC.getToasts().addToast(new SystemToast(SystemToast.SystemToastId.NARRATOR_TOGGLE,OOoOOO00OoO0OoO000ooo00o.oOO0OOoOOOO0OoooOO0oO000(var2x.Ooo000oOo0000oo00o00o000()),null));
                }

                if (var2x.oOO0OOoOOOO0OoooOO0oO000()) {
                    MC.getToasts().addToast(new SystemToast(SystemToast.SystemToastId.NARRATOR_TOGGLE,Component.translatable("message.yes_steve_model.model.reload.complete", (double)var1.getTime(TimeUnit.MICROSECONDS) / 1000.0),null));
                    var1.reset();
                    var1.start();
                }

            }, (var2x) -> {
                var1.stop();
                if (!var2x.oOO0OOoOOOO0OoooOO0oO000()) {
                    MC.getToasts().addToast(new SystemToast(SystemToast.SystemToastId.NARRATOR_TOGGLE,OOoOOO00OoO0OoO000ooo00o.oOO0OOoOOOO0OoooOO0oO000(var2x.Ooo000oOo0000oo00o00o000()),null));
                } else if (!var2x.OOo00OoO0000OOO00ooOOOO0().isEmpty()) {

                    for (Component var4 : var2x.OOo00OoO0000OOO00ooOOOO0().values()) {
                        MC.getToasts().addToast(new SystemToast(SystemToast.SystemToastId.NARRATOR_TOGGLE, OOoOOO00OoO0OoO000ooo00o.oOO0OOoOOOO0OoooOO0oO000(var4), null));
                    }

                    if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
                        MC.getToasts().addToast(new SystemToast(SystemToast.SystemToastId.NARRATOR_TOGGLE,Component.translatable("message.yes_steve_model.model.sync.complete", (double)var1.getTime(TimeUnit.MICROSECONDS) / 1000.0), null));
                    }
                }

            });
            if (!var2) {
                MC.getToasts().addToast(new SystemToast(SystemToast.SystemToastId.CHUNK_SAVE_FAILURE,Component.translatable("message.yes_steve_model.model.reload.in_progress"),null));
            }
    }
}
