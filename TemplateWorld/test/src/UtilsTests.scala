package templateworld
import utest._

object UtilsTests extends TestSuite {

  override def tests: Tests = Tests {
    test("int pair to long"){
      assert(Utils.long2IntPair(Utils.intPair2Long(114514,233333))==(114514,233333))
    }
  }
}
