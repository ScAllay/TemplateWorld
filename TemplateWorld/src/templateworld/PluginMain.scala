package templateworld

import org.allaymc.api.plugin.Plugin
import org.allaymc.api.server.Server
import org.allaymc.api.registry.Registries
import templateworld.worldStorage.ChuckHolderWorldStorage
import templateworld.worldStorage.NonPersistentWorldStorage
import templateworld.templateWorldGenertator.TemplateWorldGenerator

class PluginMain extends Plugin {
  override def onLoad(): Unit = 
    println(os.pwd)
    // Registries.WORLD_STORAGE_FACTORIES.register("MEMORY",MemoryWorldStorage.apply)
    Registries.WORLD_STORAGE_FACTORIES.register("MEMORY-ChunkHolder",ChuckHolderWorldStorage.apply)
    Registries.WORLD_STORAGE_FACTORIES.register("NON-PERSISTENT",NonPersistentWorldStorage.apply)
    Registries.WORLD_GENERATOR_FACTORIES.register("TEMPLATE",TemplateWorldGenerator.apply)
    pluginLogger.info("TemplateWorld loaded")
  override def onEnable(): Unit = ()
    // pluginLogger.info("onEnable")
  override def onDisable(): Unit = ()
    // pluginLogger.info("onDisable")
}