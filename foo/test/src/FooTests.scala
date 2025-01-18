package foo

import utest._

object FooTests extends TestSuite {

  override def tests: Tests = Tests {
    assert(1+1==2)
  }
}
