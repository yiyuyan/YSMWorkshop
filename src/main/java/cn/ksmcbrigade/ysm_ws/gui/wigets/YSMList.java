package cn.ksmcbrigade.ysm_ws.gui.wigets;

import cn.ksmcbrigade.ysm_ws.Config;
import cn.ksmcbrigade.ysm_ws.UicodeBackslashU;
import cn.ksmcbrigade.ysm_ws.YSMWorkshop;
import cn.ksmcbrigade.ysm_ws.gui.DataMoreScreen;
import cn.ksmcbrigade.ysm_ws.gui.WorkShop;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class YSMList extends ContainerObjectSelectionList<YSMList.Entry> {

    public final Screen last;

    public YSMList(WorkShop p_345102_, Minecraft p_346132_) {
        super(p_346132_, p_345102_.width/2, p_345102_.layout.getContentHeight(), p_345102_.layout.getHeaderHeight(), 20);
        this.last = p_345102_;
        String ls = getNet("http://7ufqxh0.xghost.sheng17.site/record.json");
        YSMWorkshop.LOGGER.info("Got from the Internet: ");
        YSMWorkshop.LOGGER.info(ls);
        if(ls!=null){
            if(ls.contains("\n")){
                for (String string : ls.split("\n")) {
                    addEntryToTop(new Entry(JsonParser.parseString(string).getAsJsonObject()));
                }
            }
            else{
                addEntry(new Entry(JsonParser.parseString(ls).getAsJsonObject()));
            }
        }
    }

    public void refreshEntries() {
        this.children().forEach(Entry::refreshEntry);
    }


    @Override
    public void clearEntries() {
        super.clearEntries();
    }

    @Override
    public void addEntryToTop(@NotNull Entry p_239858_) {
        super.addEntryToTop(p_239858_);
    }

    public int getRowWidth() {
        return 340;
    }

    public Entry build(JsonObject object){
        return new Entry(object);
    }

    public class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        public final JsonObject data;
        private final Minecraft MC = Minecraft.getInstance();

        private final String name;
        private final Button changeButton;
        private final Button moreButton;

        public Entry(JsonObject data) {
            this.data = data;
            this.name = UicodeBackslashU.unicodeToCn(this.data.get("name").getAsString());
            this.changeButton = Button.builder(Component.literal("Download"), (p_345593_) -> {
                String lanzou = this.data.get("lanzou_url").getAsString()+"?pwd="+this.data.get("lanzou_pwd").getAsString();
                if(Config.DIRECT_DOWNLOAD.getAsBoolean()){
                    try {
                        String url = Config.DOWNLOAD_API.get()+"?url="+this.data.get("lanzou_url").getAsString()+"&pwd="+this.data.get("lanzou_pwd").getAsString()+"&type=down";
                        YSMWorkshop.LOGGER.info("Built a download url.");
                        YSMList.this.downloadNet(this.name,url,data.get("ysm").getAsBoolean());
                        YSMWorkshop.reload_YSM();
                        MC.getToasts().addToast(new SystemToast(SystemToast.SystemToastId.NARRATOR_TOGGLE,Component.literal("Successful!"),null));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        MC.getToasts().addToast(new SystemToast(SystemToast.SystemToastId.CHUNK_SAVE_FAILURE,Component.literal("Can't download the file,please copy the url to the browser to download"),null));
                        MC.keyboardHandler.setClipboard(lanzou);
                    }
                }
                else{
                    try {
                        Runtime.getRuntime().exec(new String[]{"cmd.exe","/c","start",lanzou});
                        MC.getToasts().addToast(new SystemToast(SystemToast.SystemToastId.NARRATOR_TOGGLE,Component.literal("Successful!"),null));
                    } catch (IOException e) {
                        e.printStackTrace();
                        MC.getToasts().addToast(new SystemToast(SystemToast.SystemToastId.CHUNK_SAVE_FAILURE,Component.literal("Can't open the browser,the url was copy to the the clipboard."),null));
                        MC.keyboardHandler.setClipboard(lanzou);
                    }
                }
            }).bounds(0, 0, 75, 20).build();

            this.moreButton = Button.builder(Component.literal("More"), (p_346334_) -> Minecraft.getInstance().setScreen(new DataMoreScreen(YSMList.this.last,this.data))).bounds(0, 0, 50, 20).build();
            this.refreshEntry();
        }

        @Override
        public void render(@NotNull GuiGraphics p_345065_, int p_345504_, int p_345678_, int p_344740_, int p_345885_, int p_344888_, int p_345213_, int p_344829_, boolean p_346415_, float p_345934_) {
            int i = YSMList.this.getScrollbarPosition() - this.changeButton.getWidth() - 10;
            int j = p_345678_ - 2;
            this.moreButton.setPosition(i, j);
            this.moreButton.render(p_345065_, p_345213_, p_344829_, p_345934_);
            int k = i - 5 - this.changeButton.getWidth();
            this.changeButton.setPosition(k, j);
            this.changeButton.render(p_345065_, p_345213_, p_344829_, p_345934_);
            p_345065_.drawString(YSMList.this.minecraft.font, this.name, p_344740_, p_345678_ + p_344888_ / 2 - 4, -1);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of();
        }

        void refreshEntry() {

        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(this.changeButton,this.moreButton);
        }
    }

    public File downloadNet(String saveName, String urlS,boolean ysm) throws MalformedURLException {
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL(urlS);

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream("config\\yes_steve_model\\custom\\"+saveName.replace(" ","_")+"."+(ysm?"ysm":"zip"));

            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File("config\\yes_steve_model\\custom\\"+saveName.replace(" ","_")+".ysm");
    }

    public static String getNet(String urlS){
        try {
            URL url = new URL(urlS);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(180);
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line + "\n");
            }
            reader.close();
            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
