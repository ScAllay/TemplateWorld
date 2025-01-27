package templateworld.templateWorldGenertator

import os.Path
import scala.util.{Try, Success, Failure}
import upickle.default.*

enum TemplateConfig:
  case LevelDB(path: Path, slice: Option[(x: Int, dx: Int, z: Int, dz: Int)]=None)
  def toPresetStr = this match
    case LevelDB(path, Some(slice)) =>
      val (x,dx,z,dz) = slice
      s"leveldb:${path.relativeTo(os.pwd).toString};slice=$x,$dx,$z,$dz"
    case LevelDB(path,None) => s"leveldb:${path.relativeTo(os.pwd).toString}"

object TemplateConfig:
  case class TemplateConfigException(message: String) extends Exception(message)

  def preaseString(str: String): Try[TemplateConfig] =
    val typ = str.split(":")(0)
    var rest = str.split(":", 2)(1)
    val path = str.split(":", 2)(1).split(";", 2)(0)
    var args = Try {
      val argstr = str
        .split(":", 2)(1)
        .split(";", 2)
      if argstr.length == 1 then Map()
      else
        argstr(1)
          .split(";")
          .map(i =>
            if i.indexOf("=") == -1 then throw TemplateConfigException(s"Invalid option: ${i}")
            val kv = i.split("=", 2)
            kv(0) -> kv(1)
          )
          .toMap
    }
    typ match
      // leveldb:<path>[;slice=<x:int>,<dx:int>,<z:int>,<dz:int>]
      case "leveldb" =>
        Try {
          LevelDB(
            os.RelPath(path).resolveFrom(os.pwd), {
              args.getOrElse(Map()).get("slice") match
                case None => None
                case Some(value) =>
                  val nums = value.split(",").map(_.toInt)
                  Some(nums(0), nums(1), nums(2), nums(3))
            }
          )
        }
      case _ => Failure(TemplateConfigException("unkown template type"))
