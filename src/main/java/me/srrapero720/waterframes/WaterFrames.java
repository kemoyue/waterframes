package me.srrapero720.waterframes;

import me.srrapero720.waterframes.common.block.entity.DisplayTile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaterFrames implements ModInitializer {
    // TOOLS
    public static final String ID = "waterframes";
    public static final String NAME = "WATERFrAMES";
    public static final String PREFIX = "§6§l[§r§bWATERF§3r§bAMES§6§l]: §r";
    public static final Logger LOGGER = LogManager.getLogger(ID);
    public static final long SYNC_TIME = 1000L;
    private static int SERVER_OP_LEVEL = -1;

    @Override
    public void onInitialize() {
        WFConfig.init();
        WFRegistry.init();
        DisplayTile.initCommon();
    }

    public static boolean isInstalled(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static boolean isInstalled(String... mods) {
        for (String id: mods) {
            if (FabricLoader.getInstance().isModLoaded(id)) {
                return false;
            }
        }
        return true;
    }

    public static int getServerOpPermissionLevel(Level level) {
        if (level != null && !level.isClientSide) {
            SERVER_OP_LEVEL = level.getServer().getOperatorUserPermissionLevel();
        }
        return SERVER_OP_LEVEL;
    }

    public static void setOpPermissionLevel(int level) {
        SERVER_OP_LEVEL = level;
    }

    @Environment(EnvType.CLIENT)
    public static float deltaFrames() { return Minecraft.getInstance().isPaused() ? 1.0F : Minecraft.getInstance().getFrameTime(); }
}