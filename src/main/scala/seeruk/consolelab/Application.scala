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

import seeruk.consolelab.input.{Input, InputDefinition}

/**
 * Application
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
case class Application(
    definition: InputDefinition,
    input: Input,
    commands: List[Command[_, _]] = List()) {

  val (definition1, commandName) = definition.mkArg[String]("command_name", "list", Some("The command to run"))
  val (definition2, help) = definition1.mkOpt[Boolean]("help", false, Some("Displays command usage"))

  def run(): Int = {
    val maybeCommand = commands.find(_.name == commandName.resolve(input))

    maybeCommand match {
      case Some(command) =>
        command.run(input, new Output(), new Dialog())
      case _ => 1
    }
  }

  def withCommand(command: Command[_, _]): Application = {
    copy(definition, input, commands :+ command)
  }

  def withCommands(commands: List[Command[_, _]]): Application = {
    copy(definition, input, commands ++ commands)
  }
}
