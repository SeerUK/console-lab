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

import seeruk.consolelab.input._

/**
 * Example Command
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ExampleCommand(val input: Input) extends AmbiguousCommand {
  override val name = "example"

  private val source = input.arg[String]("source", "", desc = Some("Source to clone from"))
  private val dest = input.arg[String]("dest", ".", desc = Some("Destination to clone to"))
  private val noClean = input.opt[Boolean]("no-clean", default = false, Some("Don't clean VCS files"))

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
