/**
 * This file is part of the "SeerUK/console-lab" project.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the LICENSE is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package seeruk.fpinscala

import scala.annotation.tailrec

trait RNG {
  def nextInt: (Int, RNG)
}

case class SimpleRNG(seed: Long) extends RNG {
  override def nextInt: (Int, RNG) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRNG = SimpleRNG(newSeed)
    val n = (newSeed >>> 16).toInt
    (n, nextRNG)
  }
}

object RNG {
  def positiveInt(rng: RNG): (Int, RNG) = {
    // Generate a random integer
    val (i, rng2) = rng.nextInt
    // If the result is less than 0 we need to make it positive
    // To prevent runtime errors due to exceeding the maximum size of an Int we add one to `i` if
    // it's less than 0. This is because if `i` equal to Int.MinValue the absolute value of that
    // negative value would be greater than Int.MaxValue by 1. So by adding one, we ensure it would
    // only be able to actually result in Int.MaxValue if `i` were Int.MinValue
    // We multiply by -1 to get the absolute value of `i`.
    val pos = if (i < 0) (i + 1) * -1 else i
    (pos, rng2)
  }

  def positiveDouble(rng: RNG): (Double, RNG) = {
    // We want a positive int because negative ints will result in a negative double result
    val (i, rng2) = positiveInt(rng)
    // Int.MaxValue is used because `i` could be anything from 0 -> Int.MaxValue
    // The case to double must be made for at least one of the values so that the result is a double
    // 1 is added to the maxvalue to ensure that if `i` is Int.MaxValue the result will still be
    // less than 1.
    val d = i / (Int.MaxValue.toDouble + 1)
    (d, rng2)
  }

  def positiveIntDoublePair(rng: RNG): ((Int, Double), RNG) = {
    val (i, rng2) = positiveInt(rng)
    val (d, rng3) = positiveDouble(rng2)
    ((i, d), rng3)
  }

  def positionDoubleIntPair(rng: RNG): ((Double, Int), RNG) = {
    val ((i, d), rng2) = positiveIntDoublePair(rng)
    ((d, i), rng2)
  }

  def positiveDoubleTrio(rng: RNG): ((Double, Double, Double), RNG) = {
    val (d1, rng2) = positiveDouble(rng)
    val (d2, rng3) = positiveDouble(rng2)
    val (d3, rng4) = positiveDouble(rng3)
    ((d1, d2, d3), rng4)
  }

  // Standard recursion method
  def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
    if (count <= 0) {
      (List(), rng)
    } else {
      val (i, rng2) = rng.nextInt
      val (is, rng3) = ints(count - 1)(rng2)
      (i :: is, rng3)
    }
  }

  // Tail recursion method
  def ints2(count: Int)(rng: RNG): (List[Int], RNG) = {
    @tailrec
    def recurse(count: Int, rng: RNG, is: List[Int]): (List[Int], RNG) = {
      if (count <= 0) {
        (is, rng)
      } else {
        val (i, rng2) = rng.nextInt
        recurse(count - 1, rng2, i :: is)
      }
    }

    recurse(count, rng, List())
  }
}
