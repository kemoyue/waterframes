package me.srrapero720.waterframes.core;

import me.srrapero720.waterframes.WaterFrames;
import me.srrapero720.waterframes.client.displays.VideoDisplay;
import me.srrapero720.waterframes.client.renderer.FramesRenderer;
import me.srrapero720.waterframes.client.renderer.ProjectorRenderer;
import me.srrapero720.waterframes.common.packets.ActionPacket;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


public class WaterEvents {
    public static void init(IEventBus bus) {
        bus.addListener(Client::init);
        bus.addListener(Common::init);
    }


    @Mod.EventBusSubscriber(value = { Dist.CLIENT, Dist.DEDICATED_SERVER }, modid = WaterFrames.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    private static class Common {
        private static void init(FMLCommonSetupEvent event) { common(); }
        private static void common() { WaterFrames.NETWORK.registerType(ActionPacket.class, ActionPacket::new); }
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = WaterFrames.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    @OnlyIn(Dist.CLIENT)
    private static class Client {
        private static void init(FMLClientSetupEvent event) {
            client();
        }

        @OnlyIn(Dist.CLIENT)
        private static void client() {
            BlockEntityRenderers.register(WaterRegistry.TILE_FRAME.get(), (x) -> new FramesRenderer());
            BlockEntityRenderers.register(WaterRegistry.TILE_PROJECTOR.get(), (x) -> new ProjectorRenderer());
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void onRenderTickEvent(TickEvent.RenderTickEvent event) {
            if (event.phase == TickEvent.Phase.END) VideoDisplay.tick();
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void onClientTickEvent(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) VideoDisplay.tick();
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void onUnloadingLevel(WorldEvent.Unload unload) {
            if (unload.getWorld() != null && unload.getWorld().isClientSide()) VideoDisplay.unload();
        }
    }
}