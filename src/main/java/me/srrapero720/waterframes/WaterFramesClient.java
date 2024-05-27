package me.srrapero720.waterframes;

import me.srrapero720.waterframes.common.block.entity.DisplayTile;
import net.fabricmc.api.ClientModInitializer;
import team.creative.creativecore.client.CreativeCoreClient;

import static me.srrapero720.waterframes.WaterFrames.ID;

public class WaterFramesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        WFRegistry.initClient();
        CreativeCoreClient.registerClientConfig(ID);
        DisplayTile.initClient();
    }
}
