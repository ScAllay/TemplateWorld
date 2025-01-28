package templateworld

import scala.collection.mutable
import org.allaymc.api.server.Server
import org.allaymc.api.world.WorldPool
import org.allaymc.api.world.WorldSettings.WorldSetting
import org.allaymc.api.world.WorldSettings.WorldSetting.DimensionSettings
import templateworld.templateWorldGenertator.TemplateConfig

enum StorageType:
  case LevelDB, ChuckHolder, NonPersistent
  def toRegName =
    this match
      case LevelDB       => "LEVELDB"
      case ChuckHolder   => "MEMORY-ChunkHolder"
      case NonPersistent => "NON-PERSISTENT"

object TemplateWorlds:

  val worlds = mutable.Map[String,(StorageType,TemplateConfig)]()

  private inline def serverWorldPool(using server:Server) =
    server.getWorldPool()

  def newWorld(name: String, storageType: StorageType, template:TemplateConfig)(using Server) =
    worlds.put(name,(storageType,template))
    serverWorldPool.loadWorld(
      name,
      WorldSetting
        .builder()
        .enable(true)
        .storageType(storageType.toRegName)
        .overworld(
          DimensionSettings.builder().generatorType("TEMPLATE").generatorPreset(template.toPresetStr).build()
        )
        .build()
    )
    serverWorldPool.getWorld(name)

  def removeWorld(name:String)(using Server) = 
    worlds.remove(name)
    serverWorldPool.unloadWorld(name)