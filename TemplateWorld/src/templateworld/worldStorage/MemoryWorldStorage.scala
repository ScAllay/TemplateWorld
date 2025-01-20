package templateworld.worldStorage

import org.allaymc.api.world.storage.WorldStorage
import org.allaymc.api.world.chunk.Chunk
import org.allaymc.api.world.DimensionInfo
import org.allaymc.api.world.WorldData
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import org.allaymc.server.world.AllayWorldData
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import templateworld.SerializationedChunk

class MemoryWorldStorage(path: Path) extends WorldStorage:
  var dims = Array.apply(
    new Long2ObjectOpenHashMap[SerializationedChunk],
    new Long2ObjectOpenHashMap[SerializationedChunk],
    new Long2ObjectOpenHashMap[SerializationedChunk]
  )

  var worldData: WorldData = AllayWorldData
    .builder()
    .displayName(path.getName(path.getNameCount() - 1).toString())
    .build();

  override def writeWorldData(worldData: WorldData): Unit =
    this.worldData = worldData

  override def readWorldData(): WorldData = worldData

  override def readChunkSync(chunkX: Int, chunkZ: Int, dimensionInfo: DimensionInfo): Chunk = ???

  override def containChunk(chunkX: Int, chunkZ: Int, dimensionInfo: DimensionInfo): Boolean = ???

  override def writeChunk(chunk: Chunk): CompletableFuture[Void] = ???

  override def writeChunkSync(chunk: Chunk): Unit = ???

  override def readChunk(
      chunkX: Int,
      chunkZ: Int,
      dimensionInfo: DimensionInfo
  ): CompletableFuture[Chunk] = ???
