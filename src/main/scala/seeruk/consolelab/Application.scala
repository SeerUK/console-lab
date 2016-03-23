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

import seeruk.consolelab.input.Input

/**
 * Application
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
case class Application(
    input: Input,
    commands: List[Command[_]] = List()) {

  val commandName = input.arg[String]("command_name", "list", Some("The command to run"))
  val help = input.opt[Boolean]("help", false, Some("Displays command help"))
  val plain = input.opt[Boolean]("plain", false, Some("Don't style output"))
  val quiet = input.opt[Boolean]("quiet", false, Some("Decreases the verbosity of output"))
  val verbosity = input.opt[Int]("verbosity", 0, Some("Increases the verbosity of output"))
  val version = input.opt[Boolean]("version", false, Some("Displays the application version"))

  def run(): Int = {
    val maybeCommand = commands.find(_.name == commandName.resolve())
    val cmdName = commandName.resolve()

    println("Help? " + help.resolve())
    println("Plain? " + plain.resolve())
    println("Quiet? " + quiet.resolve())
    println("Verbosity? " + verbosity.resolve())
    println("Version? " + version.resolve())

    maybeCommand match {
      case Some(command) =>
        command.run(new Output(), new Dialog())
      case _ => 1
    }
  }

  def withCommand(command: Command[_]): Application = {
    copy(input, commands :+ command)
  }

  def withCommands(commands: List[Command[_]]): Application = {
    copy(input, commands ++ commands)
  }
}
