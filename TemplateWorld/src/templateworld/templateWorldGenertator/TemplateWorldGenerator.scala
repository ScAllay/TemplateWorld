package templateworld.templateWorldGenertator

import upickle.default.{ReadWriter, read}
import org.allaymc.api.world.generator.WorldGenerator
import org.allaymc.api.world.chunk.Chunk
import java.util.concurrent.CompletableFuture
import org.allaymc.api.world.generator.WorldGeneratorType
import org.allaymc.api.world.Dimension
import org.allaymc.api.server.Server
import os.Path
import org.allaymc.server.world.storage.AllayLevelDBWorldStorage

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

class LevelDBTemplateWorldGenerator(preset: String, config: TemplateConfig.LevelDB)
    extends TemplateWorldGeneratorCommon(preset, (true, true, true))
    with WorldGenerator:
  var storage = new AllayLevelDBWorldStorage(config.path.toNIO)

  override def generateChunk(x: Int, z: Int): CompletableFuture[Chunk] =
    storage.readChunk(x, z, dimension.get.getDimensionInfo())
