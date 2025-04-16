package cn.ksmcbrigade.ysm_ws;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue DIRECT_DOWNLOAD = BUILDER.comment("Use the API direct download hte ysm file instead of opening the browser").define("direct_download", false);
    public static final ModConfigSpec.ConfigValue<String> DOWNLOAD_API = BUILDER.comment("must be <xx_url>?url=<lanzou_url>&pwd=<lanzou_pwd>&type=down").define("api","https://api.hanximeng.com/lanzou/");
    //public static final ModConfigSpec.BooleanValue AUTO_APPLY = BUILDER.comment("Auto try to apply the ysm file when you downloaded a file").define("auto_apply", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
