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
  import InputReader._

  def stringArg(name: String, default: Option[String] = None, desc: Option[String] = None): String = {
    name
  }

  def boolOpt(name: String, default: Boolean = false, desc: Option[String] = None): Boolean = {
    input
      .collect { case option: InputOption => option }
      .find(_.token == name) match {
        case Some(option) => option.value match {
          case Some(value) => readBoolean(value)
          case _ => true // Boolean options without values (i.e. flags), are true if present
        }
        case _ => default
      }
  }
}

object InputReader {
  private def readInt(input: String): Int = input.toInt
  private def readString(input: String): String = input
  private def readDouble(input: String): Double = input.toDouble
  private def readBoolean(input: String): Boolean = input.toLowerCase match {
    case "true" => true
    case "false" => false
    case "y" => true
    case "n" => false
    case "yes" => true
    case "no" => false
    case "1" => true
    case "0" => false
    case invalidInput =>
      // @todo: BooleanFormatException? (Similar to if toInt throws)
      throw new Exception(s"'$invalidInput' is not a boolean.")
  }
}
