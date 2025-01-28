# Template World

> [!IMPORTANT] Unstable!
> 
> This plugin is under development and break changes may occur.

Non-persistent world storage and world generation template functionality for allay

## Non-persistent world storage

It porvide two world storage type.

### `MEMORY-ChunkHolder`  

A world storage implementation for storing Chunk objects directly, recommended for small, well-utilized maps.

### `NON-PERSISTENT`

A leafldb-based world store that deletes itself when the world is closed, you can safely use it in scenarios where you need a temporary world that performs the same as a regular leafldb world.

## Template-based world generator

It is registered as a world generator `TEMPLATE`.

It will read worlds based on preset and act as a world generator to generate the read world.

### Supported preset

#### LevelDB

`leveldb:<path>[;slice=<x:int>,<dx:int>,<z:int>,<dz:int>]`

- `<path>` is the source from which it reads the world, and should be a relative path from the allay root directory.
- `slice=<x:int>,<dx:int>,<z:int>,<dz:int>` (optional) Chunks read from a specific range will be used as the source, out-of-range Chunks will be generated as empty Chunks, and the generator will offset the origin of the sliced area to the world origin.

## Plugin API

This plugin provides a number of APIs to allow other plugins to quickly create template-based worlds. These APIs are currently only available in scala.

Dependencies are distributed using jitpack, please add it to your repositories list. (Allay itself is being distributed through this platform, so you don't need to do anything extra if you've added the repositories list for allay correctly!)

`ivy"com.github.scallay::TemplateWorld:<version>`

> [!IMPORTANT] This plugin uses experimental features of scala
> 
> it use namedTuples for a better api shape
