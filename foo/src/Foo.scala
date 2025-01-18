package foo
import org.allaymc.api.plugin.Plugin

class MyPlugin extends Plugin {
  override def onLoad(): Unit = 
    pluginLogger.info("onLoad")
  override def onEnable(): Unit = 
    pluginLogger.info("onEnable")
  override def onDisable(): Unit = 
    pluginLogger.info("onDisable")
}