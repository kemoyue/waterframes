package me.srrapero720.waterframes.common.compat.videoplayer;

import com.github.NGoedix.videoplayer.client.ClientHandler;
import me.srrapero720.waterframes.WaterFrames;
import net.minecraft.client.Minecraft;

public class VPCompat {

    public static boolean installed() {
        return WaterFrames.isInstalled("videoplayer");
    }

    public static void playVideo(String url, int volume, boolean controls) {
        if (!installed()) return;

        ClientHandler.openVideo(Minecraft.getInstance(), url, volume, controls);
    }
}
