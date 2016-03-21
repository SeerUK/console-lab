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
class InputParser {
  /**
   * Parse the given list of input arguments and options into object representations
   *
   * @param input The list of input strings
   * @return The parsed input options
   */
  def parse(input: List[String]): List[InputParameter] = {
    input
      .takeWhile(_ != "--")
      .map({
        case option if option.startsWith("--") =>
          parseLongOption(option)
        case option if option.startsWith("-") && option != "-" =>
          parseShortOption(option)
        case argument =>
          parseArgument(argument)
      })
  }

  /**
   * Parse a long option from a string representation into an object
   *
   * @param option The option
   * @return a parsed input option with an assigned name and potentially a value
   *
   * @throws RuntimeException if the option is somehow in an invalid format
   */
  @throws[RuntimeException]
  private def parseLongOption(option: String): InputOption = {
    val (name, value) = option
      .drop(2)
      .split("=", 2) match {
      case Array(name: String, value: String) =>
        (name, Some(value))
      case Array(name: String) =>
        (name, None)
      case _ =>
        // This should not be reachable, but if there's some kind of critical failure:
        throw new RuntimeException("Invalid parameter specified: '%s'".format(option))
    }

    new InputOption(name, value)
  }

  /**
   * Parse a short option from a string representation into an object
   *
   * @param option The option
   * @return a parsed input option with an assigned name and potentially a value
   *
   * @throws RuntimeException if the option is somehow in an invalid format
   */
  @throws[RuntimeException]
  private def parseShortOption(option: String): InputOption = {
    val (name, value) = option
      .drop(1)
      .split("=", 2) match {
      case Array(name: String, value: String) =>
        (name, Some(value))
      case Array(name: String) =>
        (name, None)
      case _ =>
        // This should not be reachable, but if there's some kind of critical failure:
        throw new RuntimeException("Invalid parameter specified: '%s'".format(option))
    }

    new InputOption(name, value)
  }

  /**
   * Parse an input argument from a string representation into an object
   *
   * @param argument The argument value
   * @return a parsed input argument with an assigned value
   */
  private def parseArgument(argument: String): InputArgument = {
    new InputArgument(argument)
  }
}
