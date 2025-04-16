package cn.ksmcbrigade.ysm_ws.mixin;

import cn.ksmcbrigade.ysm_ws.gui.WorkShop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component p_96550_) {
        super(p_96550_);
    }

    @Inject(method = "init",at = @At("TAIL"))
    public void init(CallbackInfo ci){
        this.addRenderableWidget(Button.builder(Component.literal("YSMWorkShop"),(b)-> Minecraft.getInstance().setScreen(new WorkShop(this))).width(100).pos(Minecraft.getInstance().getWindow().getGuiScaledWidth()-102,Minecraft.getInstance().getWindow().getGuiScaledHeight()-22).build());
    }
}
