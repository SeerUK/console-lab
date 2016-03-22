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
class Input(val input: List[InputParameter]) {
  def read[A: InputReader](name: String, default: A): A = {
    implicitly[InputReader[A]].read(name, default, input)
  }
}
