package zornco.reploidcraftenv;



import java.util.Random;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import zornco.reploidcraftenv.core.CommonProxy;
import zornco.reploidcraftenv.core.Config;
import zornco.reploidcraftenv.core.EventBus;
import zornco.reploidcraftenv.core.TabReploid;
import zornco.reploidcraftenv.lib.Reference;
import zornco.reploidcraftenv.network.PacketHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid=Reference.MOD_ID, name="ReploidEnv", version="0.0.1", acceptedMinecraftVersions = "[1.6,)")
@NetworkMod(channels = { "ReploidEnv" }, clientSideRequired=true, serverSideRequired=false, packetHandler=PacketHandler.class)
public class ReploidCraftEnv {

	// The instance of your mod that Forge uses.
	@Instance(Reference.MOD_ID)
	public static ReploidCraftEnv instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="zornco.reploidcraftenv.client.ClientProxy", serverSide="zornco.reploidcraftenv.core.CommonProxy")
	public static CommonProxy proxy;

	public static CreativeTabs reploidTab = new TabReploid(Reference.MOD_ID);

	public static Logger logger = Logger.getLogger(Reference.MOD_ID);

	public static Random rand = new Random();

	public static Config config = new Config();

	public static EventBus events;
	
	public static Item healthBit;
	public static Item healthByte;
	public static Item weaponBit;
	public static Item weaponByte;
	public static Item healthTank;
	public static Item weaponTank;
	public static Item extraMan;
	public static Item component;
	public static Item reploidPlate;
	public static Item itemHolder;

	public static Item platformPlacer;
	public static Item doorBossItem;

	public static Block spikes;
	public static Block doorBossBlock;
	public static Block blockItemHolder;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger.setParent(FMLLog.getLogger());
		config.loadConfig(event);
		proxy.registerKeyBindingHandler();
		proxy.registerSounds();
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		config.addItems();
		config.addBlocks();
		config.addNames();
		config.recipes.registerRecipes();
		proxy.registerRenderInformation();
		events = new EventBus();
		NetworkRegistry.instance().registerGuiHandler(this, proxy);

		config.registerEntities();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
}