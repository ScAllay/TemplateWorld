package templateworld

import org.allaymc.api.world.chunk.Chunk
import org.allaymc.server.world.chunk.AllayUnsafeChunk

case class SerializationedChunk(
)

object SerializationedChunk {
  def formChunk(chunk:Chunk) =
    chunk.batchProcess( chunk => {
        chunk.asInstanceOf[AllayUnsafeChunk].getSection(1)
    })

}