package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {
	trait TestTrees {
		val t1 = Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5)
		val t2 = Fork(Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5), Leaf('d',4), List('a','b','d'), 9)
	}


  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }


  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('a','b','d'))
    }
  }


  test("string2chars(\"hello, world\")") {
    assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
  }


  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e',1), Leaf('t',2), Leaf('x',3)))
  }


  test("combine of some leaf list") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(combine(leaflist) === List(Fork(Leaf('e',1),Leaf('t',2),List('e', 't'),3), Leaf('x',4)))
  }


//  test("decode and encode a very short text should be identity") {
//    new TestTrees {
//      assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
//    }
//  }
}

class Part1Suite extends FunSuite {
  test("weight of leaf") {
    assert(weight(Leaf('c', 1)) === 1)
  }

  test("weight of fork") {
    assert(weight(Fork(Leaf('c', 1), Leaf('d', 1), List('d', 'c'), 2)) === 2)
  }

  // Not finished, Come back and finish these off!!
}

class Part2Suite extends FunSuite {
  test("times ftn") {
   assert(times(List('a', 'b', 'c', 'a')) === List(('a', 2), ('b', 1), ('c', 1)))
  }

  test("makeOrderedLeafList ftn") {
    assert(makeOrderedLeafList(List(('a', 2), ('b', 1), ('c', 1))) === List(Leaf('c', 1), Leaf('b', 1), Leaf('a', 2)))
  }

  test("combine ftn") {
    assert(combine(List(Leaf('c', 1), Leaf('b', 1), Leaf('a', 2))) === List(Fork(Leaf('c', 1), Leaf('b', 1), List('c', 'b'), 2), Leaf('a', 2)))
  }

  test("create tree") {
    assert(createCodeTree(List('a', 'b', 'c', 'a')) === Fork(Fork(Leaf('c',1),Leaf('b',1),List('c', 'b'),2),Leaf('a',2),List('a', 'c', 'b'),4))
  }
}

