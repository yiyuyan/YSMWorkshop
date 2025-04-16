package cn.ksmcbrigade.ysm_ws.gui.wigets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Label extends AbstractWidget {

    public int color = Color.WHITE.getRGB();

    public Label color(int c){
        this.color = c;
        return this;
    }

    public Label(int x, int y, Component text) {
        super(x,y,Minecraft.getInstance().font.width(text) , Minecraft.getInstance().font.lineHeight, text);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
        guiGraphics.drawString(Minecraft.getInstance().font,this.getMessage(),this.getX(),this.getY(), this.color);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }
}
