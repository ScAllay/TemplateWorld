package templateworld.templateWorldGenertator

import os.Path
import scala.util.{Try, Success, Failure}

enum TemplateConfig:
  case LevelDB(path: Path)

object TemplateConfig:
  case class TemplateConfigException(message: String) extends Exception(message)

  def preaseString(str: String): Try[TemplateConfig] =
    if (str.startsWith("leveldb:")) {
      Try { LevelDB(os.RelPath(str.split(":")(1)).resolveFrom(os.pwd)) }
    } else {
      Failure(TemplateConfigException("unkown template type"))
    }
