package objsets

import TweetReader._

// A class to represent tweets.
class Tweet(val user: String, val text: String, val retweets: Int) {
  override def toString: String =
    "User: " + user + "\n" +
    "Text: " + text + " [" + retweets + "]"
}

/** Set of "Tweets" as BST. There is an invariant which always holds: for every branch `b`,
  * all elements in the left subtree are smaller than the tweet at `b`. The elements in the right subtree are
  * larger. In this implementation, the equality / order of tweets is based on the tweet's text
  * (see `def incl`). Hence, a `TweetSet` could not contain two tweets with the same
  * text from different users.
  */
abstract class TweetSet {

    def isEmpty: Boolean
  /**
   * This method takes a predicate and returns a subset of all the elements
   * in the original set for which the predicate is true.
   */
    def filter(p: Tweet => Boolean): TweetSet = filterAcc(p, new Empty)

  /**
   * This is a helper method for `filter` that propagetes the accumulated tweets.
   */
    def filterAcc(p: Tweet => Boolean, acc: TweetSet): TweetSet

  /**
   * Returns a new `TweetSet` that is the union of `TweetSet`s `this` and `that`.
   */
    def union(that: TweetSet): TweetSet = that.filterAcc(t => true, this)
  
  /**
   * Returns the tweet from this set which has the greatest retweet count.
   *
   * Calling `mostRetweeted` on an empty set will throw an exception of
   * type `java.util.NoSuchElementException`.
   */
    def mostRetweeted: Tweet

  /**
   * Returns a list containing all tweets of this set, sorted by retweet count
   * in descending order. In other words, the head of the resulting list should
   * have the highest retweet count.
   *
   * Hint: the method `remove` on TweetSet will be very useful.
   * Question: Should we implement this method here, or should it remain abstract
   * and be implemented in the subclasses?
   */
    def descendingByRetweet: TweetList = {
      if(isEmpty) Nil
      else new Cons(this.mostRetweeted, this.remove(this.mostRetweeted).descendingByRetweet)
    }


  /**
   * The following methods are already implemented
   */

  /**
   * Returns a new `TweetSet` which contains all elements of this set, and the
   * the new element `tweet` in case it does not already exist in this set.
   *
   * If `this.contains(tweet)`, the current set is returned.
   */
    def incl(tweet: Tweet): TweetSet

  /**
   * Returns a new `TweetSet` which excludes `tweet`.
   */
    def remove(tweet: Tweet): TweetSet

  /**
   * Tests if `tweet` exists in this `TweetSet`.
   */
    def contains(tweet: Tweet): Boolean

  /**
   * This method takes a function and applies it to every element in the set.
   */
    def foreach(f: Tweet => Unit): Unit
}

class Empty extends TweetSet {

    def isEmpty: Boolean = true

    def filterAcc(p: Tweet => Boolean, acc: TweetSet): TweetSet = acc

    def mostRetweeted: Tweet = throw new java.util.NoSuchElementException("Empty.mostRetweeted")

    // PreImplemented ----------------------------------
    def contains(tweet: Tweet): Boolean = false

    def incl(tweet: Tweet): TweetSet = new NonEmpty(tweet, new Empty, new Empty)

    def remove(tweet: Tweet): TweetSet = this

    def foreach(f: Tweet => Unit): Unit = ()
}

class NonEmpty(elem: Tweet, left: TweetSet, right: TweetSet) extends TweetSet {

    def isEmpty: Boolean = false

    def filterAcc(p: Tweet => Boolean, acc: TweetSet): TweetSet = {
      if (p(elem)) left.filterAcc(p, right.filterAcc(p, acc incl elem))
      else left.filterAcc(p, right.filterAcc(p, acc))
    }

    def mostRetweeted: Tweet = {
      // returns the Tweet containing more retweets
      def maxTweet(t1: Tweet, t2: Tweet):Tweet = if (t1.retweets > t2.retweets) t1 else t2

      // main function logic
      if (right.isEmpty && left.isEmpty) elem // Only Node
      else if (right.isEmpty) maxTweet(left.mostRetweeted, elem) // Recurse left subtree
      else if (left.isEmpty) maxTweet(right.mostRetweeted, elem) // Recurse right subtree
      else maxTweet(left.mostRetweeted, maxTweet(right.mostRetweeted, elem))
    }

    // PreImplemented ----------------------------------
    def contains(x: Tweet): Boolean =
      if (x.text < elem.text) left.contains(x)
      else if (elem.text < x.text) right.contains(x)
      else true

    def incl(x: Tweet): TweetSet = {
      if (x.text < elem.text) new NonEmpty(elem, left.incl(x), right)
      else if (elem.text < x.text) new NonEmpty(elem, left, right.incl(x))
      else this
    }

    def remove(tw: Tweet): TweetSet =
      if (tw.text < elem.text) new NonEmpty(elem, left.remove(tw), right)
      else if (elem.text < tw.text) new NonEmpty(elem, left, right.remove(tw))
      else left.union(right)

    def foreach(f: Tweet => Unit): Unit = {
      f(elem)
      left.foreach(f)
      right.foreach(f)
    }
}

trait TweetList {
    def head: Tweet
    def tail: TweetList
    def isEmpty: Boolean
    def foreach(f: Tweet => Unit): Unit =
      if (!isEmpty) {
        f(head)
        tail.foreach(f)
      }
}

object Nil extends TweetList {
    def head = throw new java.util.NoSuchElementException("head of EmptyList")
    def tail = throw new java.util.NoSuchElementException("tail of EmptyList")
    def isEmpty = true
}

class Cons(val head: Tweet, val tail: TweetList) extends TweetList {
    def isEmpty = false
}


object GoogleVsApple {
    val google = List("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus")
    val apple = List("ios", "iOS", "iphone", "iPhone", "ipad", "iPad")

    lazy val googleTweets: TweetSet = allTweets.filter(t => google.exists(ele => t.text.contains(ele)))
    lazy val appleTweets: TweetSet = allTweets.filter(t => apple.exists(ele => t.text.contains(ele)))
  
  /**
   * A list of all tweets mentioning a keyword from either apple or google,
   * sorted by the number of retweets.
   */
    lazy val trending: TweetList = (googleTweets union appleTweets).descendingByRetweet
  }

object Main extends App {
  // Print the trending tweets
    GoogleVsApple.trending foreach println
}
