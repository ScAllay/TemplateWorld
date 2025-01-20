package templateworld.worldStorage

import org.allaymc.api.world.storage.WorldStorage
import org.allaymc.api.world.chunk.Chunk
import org.allaymc.api.world.DimensionInfo
import org.allaymc.api.world.WorldData
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import org.allaymc.server.world.AllayWorldData
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import templateworld.Utils.intPair2Long
import org.allaymc.server.world.chunk.AllayUnsafeChunk
import org.allaymc.api.server.Server

val viod = Void.TYPE.cast(())

class ChuckHolderWorldStorage(path: Path) extends WorldStorage:
  var dims = Array(
    new Long2ObjectOpenHashMap[Chunk],
    new Long2ObjectOpenHashMap[Chunk],
    new Long2ObjectOpenHashMap[Chunk]
  )

  var worldData: WorldData = AllayWorldData
    .builder()
    .displayName(path.getName(path.getNameCount() - 1).toString())
    .build();

  override def writeWorldData(worldData: WorldData) =
    this.worldData = worldData

  override def readWorldData() = worldData

  override def readChunkSync(chunkX: Int, chunkZ: Int, dimensionInfo: DimensionInfo) =
    if containChunk(chunkX, chunkZ, dimensionInfo) then
      dims(dimensionInfo.dimensionId()).get(intPair2Long(chunkX, chunkZ))
    else
      AllayUnsafeChunk
        .builder()
        .newChunk(chunkX, chunkZ, dimensionInfo)
        .toSafeChunk();

  override def readChunk(
      chunkX: Int,
      chunkZ: Int,
      dimensionInfo: DimensionInfo
  ): CompletableFuture[Chunk] =
    CompletableFuture.supplyAsync(
      () => readChunkSync(chunkX, chunkZ, dimensionInfo),
      Server.getInstance().getVirtualThreadPool()
    )

  override def containChunk(chunkX: Int, chunkZ: Int, dimensionInfo: DimensionInfo): Boolean =
    dims(dimensionInfo.dimensionId()).containsKey(intPair2Long(chunkX, chunkZ))

  override def writeChunk(chunk: Chunk): CompletableFuture[Void] =
    CompletableFuture.runAsync(
      () => {
        writeChunkSync(chunk)
      },
      Server.getInstance().getVirtualThreadPool()
    )

  override def writeChunkSync(chunk: Chunk): Unit =
    dims(chunk.getDimensionInfo().dimensionId())
      .put(intPair2Long(chunk.getX(), chunk.getZ()), chunk)
