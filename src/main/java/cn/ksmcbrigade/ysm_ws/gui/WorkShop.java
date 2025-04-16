package cn.ksmcbrigade.ysm_ws.gui;

import cn.ksmcbrigade.ysm_ws.YSMWorkshop;
import cn.ksmcbrigade.ysm_ws.gui.wigets.Label;
import cn.ksmcbrigade.ysm_ws.gui.wigets.YSMList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;
import net.neoforged.fml.i18n.MavenVersionTranslator;

import java.net.URLEncoder;

public class WorkShop extends OptionsSubScreen {

    private final Minecraft MC = Minecraft.getInstance();
    private final LinearLayout linearLayout = new LinearLayout(Minecraft.getInstance().getWindow().getGuiScaledWidth()/4-10,layout.getY()-layout.getContentHeight()/2+layout.getHeaderHeight()+35, LinearLayout.Orientation.VERTICAL);
    private final LinearLayout linearLayout1 = new LinearLayout(linearLayout.getX()+linearLayout.getWidth()+12,MC.getWindow().getGuiScaledHeight()-this.layout.getHeaderHeight()-this.layout.getContentHeight(), LinearLayout.Orientation.HORIZONTAL);
    private final EditBox box = new EditBox(MC.font,0,0,80,20,Component.literal("SEARCH_BOX"));
    private Checkbox G,PG13;
    public boolean w = false;
    public boolean n = false;

    public YSMList list = new YSMList(this,Minecraft.getInstance());

    public WorkShop(Screen last) {
        super(last, Minecraft.getInstance().options,Component.literal("YSM WorkShop"));
        n = !MC.options.fullscreen().get();
        MC.options.fullscreen().set(false);
        try {
            Thread.sleep(3L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MC.options.fullscreen().set(true);
    }

    @Override
    protected void init() {
        super.init();
        //linearLayout.visitWidgets(this::addRenderableWidget);
        //linearLayout1.visitWidgets(this::addRenderableWidget);
    }

    @Override
    protected void addOptions() {
    }

    @Override
    protected void addFooter() {
        LinearLayout linearLayout2 = new LinearLayout(0,0, LinearLayout.Orientation.HORIZONTAL);
        linearLayout2.addChild(Button.builder(Component.literal("UpdateYourYSMFile"),(o)->Minecraft.getInstance().setScreen(new UpdateScreen(this))).width(100).build());
        linearLayout2.addChild(Button.builder(Component.literal("Back"),(o)->this.onClose()).width(100).build());
        this.layout.addToFooter(linearLayout2);
    }

    @Override
    public void addContents() {
        super.addContents();

        LinearLayout mainLayout = new LinearLayout(0,0, LinearLayout.Orientation.HORIZONTAL);

        Minecraft MC = Minecraft.getInstance();
        box.setCanLoseFocus(true);
        box.setFocused(true);
        box.setHint(Component.literal("Search"));
        box.setWidth(80);

        EditBox tags = new EditBox(MC.font,0,0,80,20,Component.literal("TAGS"));
        tags.setEditable(true);
        tags.setCanLoseFocus(true);
        tags.setValue(MavenVersionTranslator.artifactVersionToString(ModList.get().getModContainerById("yes_steve_model").get().getModInfo().getVersion()));
        tags.setHint(Component.literal("Tag(s)"));

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

        Button searchButton = Button.builder(Component.literal("Search"),(on)->{
            StringBuilder builder = new StringBuilder("http://7ufqxh0.xghost.sheng17.site/api.php?type=search&s=");
            builder.append(URLEncoder.encode(box.getValue()).replace("%2C",","));
            builder.append("&st=");
            if(G.selected()){
                builder.append("g");
            }
            else if(PG13.selected()){
                builder.append("pg13");
            }
            builder.append("&tag=").append(URLEncoder.encode(tags.getValue()).replace("%2C",","));
            YSMWorkshop.LOGGER.info("Getting {}", builder);
            String d = YSMList.getNet(builder.toString());
            if(d!=null){
                YSMWorkshop.LOGGER.info("Got information from the Internet: ");
                YSMWorkshop.LOGGER.info(d);
                list.clearEntries();
                JsonArray array = JsonParser.parseString(d).getAsJsonArray();
                for (JsonElement jsonElement : array) {
                    if(jsonElement.isJsonObject()){
                        list.addEntryToTop(this.list.build(jsonElement.getAsJsonObject()));
                    }
                }
            }
        }).width(80).build();

        linearLayout.addChild(box);
        linearLayout.addChild(searchButton);

        linearLayout.addChild(new Label(0,0,Component.literal("Age grading:")));
        linearLayout.addChild(G);
        linearLayout.addChild(PG13);

        linearLayout.addChild(new Label(0,0,Component.literal("Tag(s):")));
        linearLayout.addChild(tags);

        mainLayout.addChild(linearLayout);
        mainLayout.addChild(list);

        this.layout.addToContents(mainLayout);
    }

    @Override
    protected void repositionElements() {
        super.repositionElements();
        /*this.linearLayout.arrangeElements();
        this.linearLayout1.arrangeElements();*/
        this.list.updateSize(this.width, this.layout);
    }

    @Override
    public void onClose() {
        if(n) Minecraft.getInstance().options.fullscreen().set(false);
        super.onClose();
    }

    @Override
    public void tick() {
        if(!Minecraft.getInstance().options.fullscreen().get())Minecraft.getInstance().options.fullscreen().set(true);
    }
}
