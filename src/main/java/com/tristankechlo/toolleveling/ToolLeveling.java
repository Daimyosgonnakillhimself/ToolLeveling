package com.tristankechlo.toolleveling;

import com.tristankechlo.toolleveling.client.ClientSetup;
import com.tristankechlo.toolleveling.config.util.ConfigManager;
import com.tristankechlo.toolleveling.config.util.ConfigSyncing;
import com.tristankechlo.toolleveling.init.ModRegistry;
import com.tristankechlo.toolleveling.network.PacketHandler;
import com.tristankechlo.toolleveling.utils.Names;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.common.MinecraftForge;
import net.neoforged.neoforge.event.CreativeModeTabEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.eventbus.api.IEventBus;
import net.neoforged.neoforge.eventbus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ToolLeveling.MOD_ID)
public class ToolLeveling {

    public static final String MOD_ID = "toolleveling";
    private static final Logger LOGGER = LogManager.getLogger();

    public ToolLeveling(IEventBus modEventBus, ModContainer modContainer) {
        ModRegistry.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::populateCreativeTab);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ConfigManager.createConfigFolder();
        ConfigManager.setup();
    }

    @SubscribeEvent
    public void onPlayerJoinEvent(final PlayerLoggedInEvent event) {
        ConfigSyncing.syncAllConfigsToOneClient((ServerPlayer) event.getEntity());
    }

    private void populateCreativeTab(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModRegistry.TLT_ITEM.get());
        }
    }
}
