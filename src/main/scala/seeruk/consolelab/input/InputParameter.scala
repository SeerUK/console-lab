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
 * Input Parameter
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait InputParameter {
  val token: String
}

case class InputArgument(token: String) extends InputParameter
case class InputOption(token: String, value: Option[String]) extends InputParameter
