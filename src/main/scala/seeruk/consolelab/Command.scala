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

import seeruk.consolelab.input.InputReader

/**
 * Example Command
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class ExampleCommand(val rdr: InputReader) extends Command with Parameters {
  override val name = "Example"

  private lazy val source = rdr.stringArg("source")
  private lazy val dest = rdr.stringArg("destination")
  private lazy val clean = !rdr.boolOpt("no-clean")

  def run(dialog: Dialog, output: Output): Unit = {
    println(source)
    println(dest)
    println(clean)
  }
}

trait Command {
  val name: String
}

trait Parameters {
  val rdr: InputReader
}
