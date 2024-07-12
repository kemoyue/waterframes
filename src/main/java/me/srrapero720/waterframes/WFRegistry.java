package me.srrapero720.waterframes;

import me.srrapero720.waterframes.client.rendering.DisplayRenderer;
import me.srrapero720.waterframes.common.block.*;
import me.srrapero720.waterframes.common.block.entity.*;
import me.srrapero720.waterframes.common.commands.WaterFramesCommand;
import me.srrapero720.waterframes.common.item.RemoteControl;
import me.srrapero720.waterframes.common.item.data.CodecManager;
import me.srrapero720.waterframes.common.item.data.RemoteData;
import me.srrapero720.waterframes.common.network.packets.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

import static me.srrapero720.waterframes.common.network.DisplayNetwork.*;
import static me.srrapero720.waterframes.WaterFrames.*;

public class WFRegistry {
    /* DATA */
    public static final DataComponentType<RemoteData> REMOTE_DATA = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, resloc("remote"), new DataComponentType.Builder<RemoteData>()
            .persistent(CodecManager.REMOTE_CODEC)
            .networkSynchronized(CodecManager.REMOTE_STREAM_CODEC)
            .build());

    /* BLOCKS */
    public static final DisplayBlock
            FRAME = Registry.register(BuiltInRegistries.BLOCK, resloc("frame"), new FrameBlock()),
            PROJECTOR = Registry.register(BuiltInRegistries.BLOCK, resloc("projector"), new ProjectorBlock()),
            TV = Registry.register(BuiltInRegistries.BLOCK, resloc("tv"), new TvBlock()),
            BIG_TV = Registry.register(BuiltInRegistries.BLOCK, resloc("big_tv"), new BigTvBlock());

    /* ITEMS */
    public static final Item
            REMOTE_ITEM = Registry.register(BuiltInRegistries.ITEM, resloc("remote"), new RemoteControl(new Item.Properties())),
            FRAME_ITEM = Registry.register(BuiltInRegistries.ITEM, resloc("frame"), new BlockItem(FRAME, prop())),
            PROJECTOR_ITEM = Registry.register(BuiltInRegistries.ITEM, resloc("projector"), new BlockItem(PROJECTOR, prop())),
            TV_ITEM = Registry.register(BuiltInRegistries.ITEM, resloc("tv"), new BlockItem(TV, prop())),
            BIG_TV_ITEM = Registry.register(BuiltInRegistries.ITEM, resloc("big_tv"), new BlockItem(BIG_TV, prop()));

    /* TILES */
    public static final BlockEntityType<DisplayTile>
            TILE_FRAME = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, resloc("frame"), tile(FrameTile::new, () -> FRAME)),
            TILE_PROJECTOR = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, resloc("projector"), tile(ProjectorTile::new, () -> PROJECTOR)),
            TILE_TV = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, resloc("tv"), tile(TvTile::new, () -> TV)),
            TILE_BIG_TV = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, resloc("big_tv"), tile(BigTvTile::new, () -> BIG_TV));

    /* TABS */
    public static final CreativeModeTab WATERTAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, resloc("tab"), FabricItemGroup.builder()
            .icon(() -> new ItemStack(FRAME_ITEM))
            .title(Component.translatable("itemGroup.waterframes"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(REMOTE_ITEM);
                output.accept(FRAME_ITEM);
                output.accept(PROJECTOR_ITEM);
                output.accept(TV_ITEM);
                output.accept(BIG_TV_ITEM);
            })
            .build());

    private static BlockEntityType<DisplayTile> tile(BlockEntityType.BlockEntitySupplier<DisplayTile> creator, Supplier<DisplayBlock> block) {
        return BlockEntityType.Builder.of(creator, block.get()).build(null);
    }

    private static Item.Properties prop() {
        return new Item.Properties().stacksTo(16).rarity(Rarity.EPIC);
    }

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> WaterFramesCommand.register(dispatcher));
        NET.registerType(DataSyncPacket.class, DataSyncPacket::new);
        NET.registerType(PermLevelPacket.class, PermLevelPacket::new);
        NET.registerType(ActivePacket.class, ActivePacket::new);
        NET.registerType(LoopPacket.class, LoopPacket::new);
        NET.registerType(MutePacket.class, MutePacket::new);
        NET.registerType(PausePacket.class, PausePacket::new);
        NET.registerType(TimePacket.class, TimePacket::new);
        NET.registerType(VolumePacket.class, VolumePacket::new);
        NET.registerType(VolumeRangePacket.class, VolumeRangePacket::new);
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        BlockEntityRenderers.register(TILE_FRAME, DisplayRenderer::new);
        BlockEntityRenderers.register(TILE_PROJECTOR, DisplayRenderer::new);
        BlockEntityRenderers.register(TILE_TV, DisplayRenderer::new);
        BlockEntityRenderers.register(TILE_BIG_TV, DisplayRenderer::new);
    }

    public static ResourceLocation resloc(String name) {
        return new ResourceLocation(ID, name);
    }

    public static class UnsupportedModException extends UnsupportedOperationException {
        private static final String MSG = "§fMod §6'%s' §fis not compatible with §e'%s'§f. please remove it";
        private static final String MSG_REASON = "§fMod §6'%s' §fis not compatible with §e'%s' §fbecause §c%s §fplease remove it";
        private static final String MSG_REASON_ALT = "§fMod §6'%s' §fis not compatible with §e'%s' §fbecause §c%s §fuse §a'%s' §finstead";

        public UnsupportedModException(String modid) {
            super(String.format(MSG, modid, NAME));
        }

        public UnsupportedModException(String modid, String reason) {
            super(String.format(MSG_REASON, modid, NAME, reason));
        }

        public UnsupportedModException(String modid, String reason, String alternatives) {
            super(String.format(MSG_REASON_ALT, modid, NAME, reason, alternatives));
        }
    }

//    public static class ModPackResources extends PathPackResources {
//        protected final IModFile modFile;
//        protected final String sourcePath;
//
//        public ModPackResources(String name, IModFile modFile, String sourcePath) {
//            super(name, true, modFile.findResource(sourcePath));
//            this.modFile = modFile;
//            this.sourcePath = sourcePath;
//        }
//
//        @NotNull
//        protected Path resolve(String... paths) {
//            String[] allPaths = new String[paths.length + 1];
//            allPaths[0] = this.sourcePath;
//            System.arraycopy(paths, 0, allPaths, 1, paths.length);
//            return this.modFile.findResource(allPaths);
//        }
//    }

}