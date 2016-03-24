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
class ExampleCommand(val definition: InputDefinition) extends AmbiguousCommand[ExampleCommandInputModel] {
  override val name = "example"

  private lazy val (_, source) = definition.mkArg[String]("source", "", desc = Some("Source to clone from"))
  private lazy val (_, dest) = definition.mkArg[String]("dest", ".", desc = Some("Destination to clone to"))
  private lazy val (_, noClean) = definition.mkOpt[Boolean]("no-clean", default = false, Some("Don't clean VCS files"))

  override def execute(input: ExampleCommandInputModel, output: Output, dialog: Dialog): Unit = {
    val clean = !input.noClean

    println("Source: " + input.source)
    println("Destination: " + input.dest)
    println("Clean: " + clean)
  }

  def definitions() = List(source, dest, noClean)

  override protected def resolve(input: Input): ExampleCommandInputModel = {
    new ExampleCommandInputModel(
      source.resolve(input),
      dest.resolve(input),
      noClean.resolve(input)
    )
  }
}

case class ExampleCommandInputModel(
    source: String,
    dest: String,
    noClean: Boolean)
  extends InputModel



abstract class AmbiguousCommand[I <: InputModel] extends Command[I, Unit] {
  override def execute(input: I, output: Output, dialog: Dialog): Unit

  final override def run(input: Input, output: Output, dialog: Dialog): Int = {
    execute(resolve(input), output, dialog)

    0
  }
}

abstract class UnambiguousCommand[I <: InputModel] extends Command[I, Int] {
  override def execute(input: I, output: Output, dialog: Dialog): Int

  final override def run(input: Input, output: Output, dialog: Dialog): Int = {
    execute(resolve(input), output, dialog)
  }
}

sealed trait Command[I <: InputModel, R] {
  val name: String

  def execute(input: I, output: Output, dialog: Dialog): R
  def run(input: Input, output: Output, dialog: Dialog): Int
  protected def resolve(input: Input): I
}

trait InputModel
