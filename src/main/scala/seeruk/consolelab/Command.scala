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

package seeruk.consolelab

import scala.collection.mutable
import seeruk.consolelab.input.{Input, InputReader, ValueReader}

/**
 * Example Command
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ExampleCommand(override val input: Input) extends AmbiguousCommand with AcceptsParameters {
  override val name = "example"

  private val source = opt[String]("source", "", desc = Some("Source to clone from"))
  private val dest = opt[String]("dest", ".", desc = Some("Destination to clone to"))
  private val noClean = opt[Boolean]("no-clean", default = false, Some("Don't clean VCS files"))

  override def execute(output: Output, dialog: Dialog): Unit = {
    val clean = !noClean.resolve()

    println("Source: " + source.resolve())
    println("Destination: " + dest.resolve())
    println("Clean: " + clean)
  }
}

abstract class AmbiguousCommand extends Command[Unit] {
  override def execute(output: Output, dialog: Dialog): Unit

  final override def run(output: Output, dialog: Dialog): Int = {
    execute(output, dialog)

    0
  }
}

abstract class UnambiguousCommand extends Command[Int] {
  override def execute(output: Output, dialog: Dialog): Int

  final override def run(output: Output, dialog: Dialog): Int = {
    execute(output, dialog)
  }
}

sealed trait Command[R] {
  val name: String

  def execute(output: Output, dialog: Dialog): R

  def run(output: Output, dialog: Dialog): Int
}

trait AcceptsParameters {
  val input: Input

  private val _parameters: mutable.MutableList[InputParameterDefinition[_]] = mutable.MutableList()

  def parameters: List[InputParameterDefinition[_]] = {
    _parameters.toList
  }

  protected def opt[T: InputReader: ValueReader](name: String, default: T, desc: Option[String] = None): InputParameterDefinition[T] = {
    val opt = new InputOptionDefinition[T](input, name, default, desc)

    _parameters += opt

    opt
  }
}
