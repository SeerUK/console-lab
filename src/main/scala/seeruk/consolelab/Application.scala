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

import seeruk.consolelab.input.{Input, InputArgument}

/**
 * Application
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
case class Application(input: Input, commands: List[Command[_]] = List()) {
  def run(): Int = {
    val maybeCommand = for {
      commandArg <- input.input.collect({ case arg: InputArgument => arg }).headOption
      command <- commands.find(_.name == commandArg.token)
    } yield command

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
