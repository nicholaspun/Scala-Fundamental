package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
    def pascal(c: Int, r: Int): Int = {
      if (c > r + 1 || c < 0 || r < 0) return -1 // Error checking

      // Main logic:
      if (c == 0 || c == r) 1 else pascal(c, r-1) + pascal(c-1,r-1)
    }
  
  /**
   * Exercise 2
   */
    def balance(chars: List[Char]): Boolean = {
      balance_acc(chars, 0)
    }

    def balance_acc(chars: List[Char], count: Int): Boolean = {
      if (count < 0) return false // More ')' than '(' or started with ')'
      if (chars.isEmpty) return count == 0 // Base Case

      // '(' increments count and ')' decrements count
      if (chars.head == '(') balance_acc(chars.tail, count + 1)
      else if (chars.head == ')') balance_acc(chars.tail, count - 1)
      else balance_acc(chars.tail, count)
    }
  
  /**
   * Exercise 3
   */
    def countChange(money: Int, coins: List[Int]): Int = {
      if (money  == 0) {
        1
      }
      else if (money > 0 && coins.nonEmpty) {
        countChange(money - coins.head, coins) + countChange(money, coins.tail)
      }
      else {
        0
      }
    }
  }
