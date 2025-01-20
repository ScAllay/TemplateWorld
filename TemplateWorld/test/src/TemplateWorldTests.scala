package templateworld.templateWorldGenertator
import utest._
import templateworld.templateWorldGenertator.TemplateConfig.LevelDB
import templateworld.templateWorldGenertator.TemplateConfig.TemplateConfigException
import scala.util.Failure

object TemplateWorldTests extends TestSuite {

  override def tests = Tests {}
}

object TemplateConfigTests extends TestSuite {
  override def tests = Tests {
    test("leveldb"){
      val config = TemplateConfig.preaseString("leveldb:template/test")
      assert(config.get == LevelDB(os.pwd / "template" / "test"))
    }

    test("unsupport"){
      assertMatch(TemplateConfig.preaseString("fuckdb:template/test")){
        case Failure(TemplateConfigException("unkown template type")) =>
      }
    }
  }
}