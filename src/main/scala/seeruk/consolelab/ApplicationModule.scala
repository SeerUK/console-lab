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

import seeruk.consolelab.input.{Input, InputParser}

/**
 * ApplicationModule
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ApplicationModule(args: Array[String]) {
  lazy val application = new Application(input)
    .withCommand(exampleCommand)

  lazy val exampleCommand = new ExampleCommand(input)

  lazy val input = new Input(parser.parse(args.toList))

  lazy val parser = new InputParser()
}
