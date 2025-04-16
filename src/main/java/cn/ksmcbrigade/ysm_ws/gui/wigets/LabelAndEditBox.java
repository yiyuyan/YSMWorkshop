package cn.ksmcbrigade.ysm_ws.gui.wigets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class LabelAndEditBox extends EditBox {

    public int color = Color.WHITE.getRGB();

    public LabelAndEditBox color(int c){
        this.color = c;
        return this;
    }

    public LabelAndEditBox(int x, int y,int w,int h, Component text) {
        super(Minecraft.getInstance().font,x,y,w,h,text);
        this.setMaxLength(512);
        this.setCanLoseFocus(true);
    }

    @Override
    public void setValue(@NotNull String p_94145_) {
        //System.out.println(p_94145_);
        super.setValue(p_94145_);
        //System.out.println(p_94145_);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
        super.renderWidget(guiGraphics, i, i1, v);
        guiGraphics.drawString(Minecraft.getInstance().font,this.getMessage(),this.getX()-Minecraft.getInstance().font.width(this.getMessage())-2,this.getY(), this.color);
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }
}
