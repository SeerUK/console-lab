/**
 * This file is part of the "console-lab" project.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the LICENSE is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package seeruk.consolelab

/**
 * Output
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class Output {
  def write(message: String): Unit = {
    print(message)
  }

  def writeln(message: String): Unit = {
    println(message)
  }
}
