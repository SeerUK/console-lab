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

import seeruk.consolelab.input.{Input, InputReader, ValueReader}

sealed trait InputParameterDefinition[T] {
  val input: Input
  val name: String
  val default: T
  val description: Option[String]

  def resolve(): T
}

final class InputOptionDefinition[T: InputReader: ValueReader](
    override val input: Input,
    override val name: String,
    override val default: T,
    override val description: Option[String])
  extends InputParameterDefinition[T] {

  override def resolve(): T = {
    input.read[T](name, default)
  }
}
