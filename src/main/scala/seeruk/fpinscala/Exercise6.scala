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
      val (i, rng2) = positiveInt(rng)
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
        val (i, rng2) = positiveInt(rng)
        recurse(count - 1, rng2, i :: is)
      }
    }

    recurse(count, rng, List())
  }

  type Rand[+A] = RNG => (A, RNG)

  val int: Rand[Int] = _.nextInt

  // Pass through state without using it. This can be useful for composing things that are just
  // setting up the type for use in another function. Take a value, and make it into a function that
  // takes an RNG instance.
  def unit[A](a: A): Rand[A] =
    rng => (a, rng)

  // Slightly more complex signature. We take a Rand[A], and produce a Rand[B]. To accomplish this,
  // map allows us to expose the actual value of type A to another callback so that it can be dealt
  // with on it's own, without having to deal with transforming the state too, then automatically
  // returning it as a Rand[B]
  def map[A, B](s: Rand[A])(f: A => B): Rand[B] = {
    rng => {
      val (a, rng2) = s(rng) // Get the A
      (f(a), rng2) // Apply the transformation to A, to get B and the new state
    }
  }

  def positiveEvenInt: Rand[Int] = {
    // If the random number is odd then i % 2 will be 1
    // If the random number is even then i % 2 will be 0
    // Given this, we will always get an even number, and it will always be positive. Only positive
    // numbers will be generated, and so the first odd number we can find is 1, and 1 - 1 = 0
    map(positiveInt)(i => i - (i % 2))
  }

  def positiveEvenIntString: Rand[String] = {
    map(positiveInt)(_.toString)
  }

  // Using the above map function, we can more "elegantly" rewrite positiveDouble using some
  // composition. The code is definitely more succint, you still see all of the necessary logic,
  // so if you understand map, and positiveInt, then you should be able to understand this without
  // the additional noise of dealing with the RNG instance.
  def positiveDoubleViaMap(rng: RNG): Rand[Double] = {
    map(positiveInt)(_ / (Int.MaxValue.toDouble + 1))
  }

  // So, this looks far more complex than map, but it effectively works the same way. This time we
  // take A and B, and then transform them both into a C. This will probably be easier to understand
  // with some examples...
  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = {
    rng => {
      val (a, rng2) = ra(rng)
      val (b, rng3) = rb(rng2)
      (f(a, b), rng3)
    }
  }

  // A = A
  // B = B
  // C = (A, B)
  def both[A, B](ra: Rand[A], rb: Rand[B]): Rand[(A, B)] = {
    // For A, and B, take them and return a tuple of type (A, B). The extra brackets can make this
    // look a little more confusing.
    map2(ra, rb)((_, _))
  }

  val randIntDouble: Rand[(Int, Double)] =
    both(positiveInt, positiveDouble)

  val randDoubleInt: Rand[(Double, Int)] =
    both(positiveDouble, positiveInt)

  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] = {
    // We have a list of functions, `fs`. We need to loop over those, and produce a single value.
    // In other words, we need to reduce that list to one value, probably with an initial state.
    // The initial state needs to be of the same type as the return type, as the accumulated value
    // is what should be returned in the end when the looping is done. This means, foldLeft or
    // foldRight would be suited for this task.
    // Remember above in unit we made something that can take a value, and turn it into a Rand[A].
    // We want to create a starting point from a pretty empty value of the right type, i.e. a
    // List[A]. Given that, we can use unit to accomplish this.
    // From there, we need to provide some kind of function that accumulates all of the values from
    // the list of functions into a new List of the values. So, we'll need to call those functions.
    // The accumulator, and the current list item will both be Rand[_] instances, and so could be
    // combined using map2. map2 needs a transformation function which will, in this case, take a
    // List[A], and add an A to it. The result at the end will be the List[A] with all of the
    // values in.
    fs.foldLeft(unit(List[A]()))((acc, f) => map2(acc, f)(_ :+ _))
  }

  // Compare this newly composed function against `ints` above. This returns the same type of data,
  // but uses small, reusable components (in this case functions) to build this in a very modular
  // way. We know that sequence takes a list of Rand[A] and produces a Rand[List[A]]. Since we know
  // that Rand[_] is a function that takes rng, we now know we only have to deal with passing an
  // RNG instance in once, and we never see the word again in this function.
  def intSequence(count: Int): Rand[List[Int]] =
    sequence(List.fill(count)(positiveInt))
}
