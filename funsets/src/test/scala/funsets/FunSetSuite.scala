package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */
  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * Put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1: Set  = singletonSet(1)
    val s2: Set = singletonSet(2)
    val s3: Set = singletonSet(3)
    val isEven: Int => Boolean = x => x%2 == 0
    val isOdd: Int => Boolean = x => x%2 == 1
    val setOf12: Set = union(s1, s2)
    val setOf23: Set = union(s2, s3)
    val setOf123: Set = union(setOf12, singletonSet(3))
  }

  test("singletonSet(1) contains 1") {
    new TestSets {
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      assert(contains(setOf12, 1), "Union 1")
      assert(contains(setOf12, 2), "Union 2")
      assert(!contains(setOf12, 3), "Union 3")
    }
  }

  test("intersect") {
    new TestSets {
      assert(contains(intersect(setOf12, setOf23), 2), "Intersect - Single element")
      assert(contains(intersect(setOf123, setOf23), 2), "Intersect - Multiple elements #1")
      assert(contains(intersect(setOf123, setOf23), 3), "Intersect - Multiple elements #2")
    }
  }

  test("diff") {
    new TestSets {
      assert(contains(diff(s1, s2), 1), "diff works for sets of single elements")
      assert(contains(diff(s2, s1), 2), "diff is one-directional")
      printSet(diff(s1, s1)) // how to represent empty set?
    }
  }

  test("filter") {
    new TestSets {
      assert(contains(filter(setOf123, isEven), 2), "Even number 2 filtered out")
      assert(contains(filter(setOf123, isOdd), 1), "Odd number 1 filtered out")
      assert(contains(filter(setOf123, isOdd), 3), "Odd number 3 filtered out")
    }
  }

  test("forall") {
    new TestSets {
      assert(forall(setOf123, isEven) === false, "Set of 123 is not all even")
      assert(forall(union(singletonSet(1), singletonSet(3)), isOdd) === true, "Set of 13 is all odd")
    }
  }

  test("exists") {
    new TestSets {
      assert(exists(setOf123, isEven) === true, "Exists even in set of 123")
      assert(exists(union(singletonSet(1), singletonSet(3)), isEven) === false, "None of set 13 is even")
    }
  }

  test("map") {
    new TestSets {
      assert(contains(map(setOf12, x => x + 3), 4) , "Map works")
    }
  }
}
