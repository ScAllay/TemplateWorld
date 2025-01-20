package templateworld.worldStorage

import org.allaymc.server.world.storage.AllayLevelDBWorldStorage
import java.nio.file.Path

object NonPersistentWorldStorage {
    def apply(path:Path) = 
        os.remove.all(os.Path(path.toAbsolutePath()))
        new NonPersistentWorldStorage(path)
}

class NonPersistentWorldStorage private (path:Path) extends AllayLevelDBWorldStorage(path) {
    override def shutdown(): Unit = 
        super.shutdown()
        os.remove.all(os.Path(path.toAbsolutePath()))
}

