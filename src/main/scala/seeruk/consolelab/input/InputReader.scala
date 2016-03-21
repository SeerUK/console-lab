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
 * Input Parser
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputReader(input: List[InputParameter]) {
  def stringArg(name: String, default: Option[String] = None, desc: Option[String] = None): String = {
    name
  }

  def boolArg(name: String, default: Option[Boolean] = None, desc: Option[String] = None): Boolean = {
    false
  }

  def boolOpt(name: String, default: Option[Boolean] = None, desc: Option[String] = None): Boolean = {
    false
  }
}
