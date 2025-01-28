package templateworld.templateWorldGenertator

import scala.collection.mutable
import java.util.concurrent.CompletableFuture
import org.allaymc.api.world.generator.{WorldGenerator,WorldGeneratorType}
import org.allaymc.api.world.chunk.Chunk
import org.allaymc.api.world.Dimension
import org.allaymc.api.server.Server
import org.allaymc.server.world.storage.AllayLevelDBWorldStorage
import org.allaymc.server.world.chunk.AllayChunkBuilder
import os.Path
import upickle.default.{ReadWriter, read}

trait TemplateWorldGeneratorCommon(preset: String, availableDim: (Boolean, Boolean, Boolean)):
  var dimension: Option[Dimension] = None
  def getName = "TEMPLATE"
  def getPreset = preset
  def getType = WorldGeneratorType.INFINITE
  def getDimension(): Dimension | Null =
    dimension.getOrElse(null)
  def setDimension(dimension: Dimension): Unit =
    if !this.dimension.isEmpty then throw IllegalStateException("Dimension already set")
    if !availableDim.toList(dimension.getDimensionInfo().dimensionId()) then
      throw new IllegalArgumentException(
        s"the dimansion ${dimension.getDimensionInfo().dimensionId()} is not support"
      )
    this.dimension = Some(dimension)

object TemplateWorldGenerator:
  def apply(preset: String) =
    val config = TemplateConfig.preaseString(preset).get
    config match
      case config: TemplateConfig.LevelDB => LevelDBTemplateWorldGenerator(preset, config)

object LevelDBTemplateWorldGenerator:
  val storages = mutable.Map[Path, AllayLevelDBWorldStorage]()
  def getPaths = storages.keys
  def release(path: Path) =
    storages.get(path) match
      case Some(value) =>
        value.shutdown()
        storages.remove(path)
      case None => ()

class LevelDBTemplateWorldGenerator(preset: String, config: TemplateConfig.LevelDB)
    extends TemplateWorldGeneratorCommon(preset, (true, true, true))
    with WorldGenerator:
  var storage =
    LevelDBTemplateWorldGenerator.storages.getOrElseUpdate(
      config.path,
      new AllayLevelDBWorldStorage(config.path.toNIO)
    )

  override def generateChunk(x: Int, z: Int): CompletableFuture[Chunk] =
    config.slice match
      case None => storage.readChunk(x, z, dimension.get.getDimensionInfo())
      case Some(s) =>
        if x > s.dx || z > s.dz then
          CompletableFuture.supplyAsync(
            () =>
              AllayChunkBuilder()
                .chunkX(x)
                .chunkZ(z)
                .dimensionInfo(dimension.get.getDimensionInfo())
                .build()
                .toSafeChunk(),
            Server.getInstance().getVirtualThreadPool()
          )
        else storage.readChunk(x + s.x, z + s.z, dimension.get.getDimensionInfo())
