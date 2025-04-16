package cn.ksmcbrigade.ysm_ws.gui;

import cn.ksmcbrigade.ysm_ws.gui.wigets.Label;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

public class DataMoreScreen extends OptionsSubScreen {

    private final JsonObject data;

    public DataMoreScreen(Screen last, JsonObject data) {
        super(last, Minecraft.getInstance().options,Component.literal("YSMMore"));
        this.data = data;
    }

    @Override
    protected void addOptions() {

    }

    @Override
    protected void addContents() {
        LinearLayout linearLayout = new LinearLayout(0,0, LinearLayout.Orientation.VERTICAL);
        for (String string : data.keySet()) {
            try {
                linearLayout.addChild(new Label(0,0,Component.literal(string+": "+data.get(string).getAsString())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.layout.addToContents(linearLayout);
    }
}
