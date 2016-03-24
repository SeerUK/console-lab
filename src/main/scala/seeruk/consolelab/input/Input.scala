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

package seeruk.consolelab.input

/**
 * Input
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class Input(val parameters: List[InputParameter]) {
  lazy val arguments = parameters.collect { case opt: InputArgument => opt }
  lazy val options = parameters.collect { case opt: InputOption => opt }

  def readArgument[A: InputArgumentReader](name: String, default: A, index: Int): A = {
    implicitly[InputArgumentReader[A]].read(name, default, arguments, index)
  }

  def readOption[A: InputOptionReader](name: String, default: A): A = {
    implicitly[InputOptionReader[A]].read(name, default, options)
  }
}
