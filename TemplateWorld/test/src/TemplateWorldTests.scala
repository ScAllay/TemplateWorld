package templateworld.templateWorldGenertator
import utest._
import templateworld.templateWorldGenertator.TemplateConfig.LevelDB
import templateworld.templateWorldGenertator.TemplateConfig.TemplateConfigException
import scala.util.Failure

object TemplateConfigTests extends TestSuite {
  override def tests = Tests {
    test("leveldb") {
      val config = TemplateConfig.preaseString("leveldb:template/test")
      assert(config.get == LevelDB(os.pwd / "template" / "test", None))
    }

    test("leveldb-with-slice") {
      val config = TemplateConfig.preaseString("leveldb:template/test;slice=3,5,10,10")
      assert(config.get == LevelDB(os.pwd / "template" / "test", Some(3, 5, 10, 10)))
    }

    test("unsupport") {
      assertMatch(TemplateConfig.preaseString("fuckdb:template/test")) {
        case Failure(TemplateConfigException("unkown template type")) =>
      }
    }

    test("error agr style") {
      assertMatch(TemplateConfig.preaseString("fuckdb:template/test;arg111")) { case Failure(e) =>
        ()
      }
    }

    test("config to str") {
      val config = LevelDB(os.pwd / "template" / "test", Some(3, 5, 10, 10))
      assert(TemplateConfig.preaseString(config.toPresetStr).get == config)
    }

    test("config to str2") {
      val config = LevelDB(os.pwd / "template" / "test", None)
      assert(TemplateConfig.preaseString(config.toPresetStr).get == config)
    }
  }
}
