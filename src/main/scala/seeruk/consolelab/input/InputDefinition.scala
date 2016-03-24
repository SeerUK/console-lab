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

import seeruk.consolelab.{InputArgumentDefinition, InputOptionDefinition, InputParameterDefinition}

/**
 * Input Definition
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
class InputDefinition(val definitions: List[InputParameterDefinition[_]] = List()) {
  def mkArg[T: InputArgumentReader: ValueReader](name: String, default: T, desc: Option[String] = None): (InputDefinition, InputArgumentDefinition[T]) = {
    val args = definitions.collect { case arg: InputArgumentDefinition[_] => arg }
    val arg = new InputArgumentDefinition[T](name, default, desc, args.size)

    (new InputDefinition(definitions :+ arg), arg)
  }

  def mkOpt[T: InputOptionReader: ValueReader](name: String, default: T, desc: Option[String] = None): (InputDefinition, InputOptionDefinition[T]) = {
    val opt = new InputOptionDefinition[T](name, default, desc)

    (new InputDefinition(definitions :+ opt), opt)
  }
}
