package cn.ksmcbrigade.ysm_ws.gui;

import cn.ksmcbrigade.ysm_ws.YSMWorkshop;
import cn.ksmcbrigade.ysm_ws.gui.wigets.Label;
import cn.ksmcbrigade.ysm_ws.gui.wigets.LabelAndEditBox;
import cn.ksmcbrigade.ysm_ws.gui.wigets.YSMList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;
import net.neoforged.fml.i18n.MavenVersionTranslator;

import java.net.URLEncoder;

public class UpdateScreen extends OptionsSubScreen {

    public Checkbox G,PG13,YSM;
    public boolean w;

    private final LabelAndEditBox name = new LabelAndEditBox(0,0,100,20,Component.literal("Name*:")),
            authors = new LabelAndEditBox(0,0,100,20,Component.literal("Author(s):")),
            tag = new LabelAndEditBox(0,0,100,20,Component.literal("Tag(s):")),
            context = new LabelAndEditBox(0,0,100,20,Component.literal("Context:")),
            lanzou_url = new LabelAndEditBox(0,0,100,20,Component.literal("The lanzou cloud url*:")),
            lanzou_pwd = new LabelAndEditBox(0,0,100,20,Component.literal("The lanzou cloud url's password:"));

    public UpdateScreen(Screen last) {
        super(last, Minecraft.getInstance().options,Component.literal("Update YSM File"));
    }

    @Override
    protected void addOptions() {

    }

    @Override
    protected void addContents() {
        Minecraft MC = Minecraft.getInstance();
        LinearLayout linearLayout = new LinearLayout(0,0, LinearLayout.Orientation.VERTICAL);
        G = Checkbox.builder(Component.literal("G"), MC.font).selected(true).onValueChange((box,b)->{
            if(b && !w){
                w = true;
                PG13.onPress();
                w = false;
            }
        }).build();
        PG13 = Checkbox.builder(Component.literal("PG13"),MC.font).onValueChange((box,b)->{
            if(b && !w){
                w = true;
                G.onPress();
                w = false;
            }
        }).build();
        YSM = Checkbox.builder(Component.literal("YSM File"),MC.font).selected(true).build();

        tag.setValue(MavenVersionTranslator.artifactVersionToString(ModList.get().getModContainerById("yes_steve_model").get().getModInfo().getVersion()));

        lanzou_pwd.setMaxLength(128);
        lanzou_pwd.setMaxLength(64);
        tag.setMaxLength(128);
        name.setMaxLength(128);
        context.setMaxLength(512);
        authors.setMaxLength(128);

        linearLayout.addChild(name);
        linearLayout.addChild(authors);
        linearLayout.addChild(tag);
        linearLayout.addChild(lanzou_url);
        linearLayout.addChild(lanzou_pwd);
        linearLayout.addChild(context);

        linearLayout.addChild(YSM);
        linearLayout.addChild(new Label(0,0,Component.literal("Age grading:")));

        LinearLayout linearLayout1 = new LinearLayout(0,0, LinearLayout.Orientation.HORIZONTAL);

        linearLayout1.addChild(G);
        linearLayout1.addChild(PG13);

        linearLayout.addChild(linearLayout1);

        this.layout.addToContents(linearLayout);
    }

    @Override
    protected void addFooter() {
        LinearLayout linearLayout2 = new LinearLayout(0,0, LinearLayout.Orientation.HORIZONTAL);
        linearLayout2.addChild(Button.builder(Component.literal("Update"), (p_345997_) -> {
            StringBuilder builder = new StringBuilder("http://7ufqxh0.xghost.sheng17.site/api.php?type=add&name=");
            builder.append(URLEncoder.encode(name.getValue()).replace("%2C",","));
            builder.append("&author=").append(URLEncoder.encode(authors.getValue()).replace("%2C",","));
            builder.append("&tag=").append(URLEncoder.encode(tag.getValue()).replace("%2C",","));
            builder.append("&st=");
            if(G.selected()){
                builder.append("g");
            }
            else if(PG13.selected()){
                builder.append("pg13");
            }
            builder.append("&context=").append(URLEncoder.encode(context.getValue()).replace("%2C",","));
            builder.append("&lanzou_url=").append(lanzou_url.getValue());
            builder.append("&lanzou_pwd=").append(lanzou_pwd.getValue());
            builder.append("&ysm=").append(YSM.selected());
            YSMWorkshop.LOGGER.info("Built a url: {}",builder);
            String context = YSMList.getNet(builder.toString());
            if(context!=null){
                Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToast.SystemToastId.NARRATOR_TOGGLE,Component.literal(context),null));
                this.onClose();
            }
            else{
                Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToast.SystemToastId.FILE_DROP_FAILURE,Component.literal("The return is error!"),null));
            }

        }).width(100).build());
        linearLayout2.addChild(Button.builder(CommonComponents.GUI_CANCEL, (p_345997_) -> this.onClose()).width(100).build());
        this.layout.addToFooter(linearLayout2);
    }
}
