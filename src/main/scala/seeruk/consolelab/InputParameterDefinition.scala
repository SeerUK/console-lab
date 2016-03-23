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

import seeruk.consolelab.input.{Input, InputArgumentReader, InputOptionReader, ValueReader}

sealed trait InputParameterDefinition[T] {
  val name: String
  val default: T
  val description: Option[String]
  val input: Input

  def resolve(): T
}

final class InputArgumentDefinition[T: InputArgumentReader: ValueReader](
    override val input: Input,
    override val name: String,
    override val default: T,
    override val description: Option[String],
    val index: Int)
  extends InputParameterDefinition[T] {

  private var resolved: Option[T] = None

  override def resolve(): T = {
    resolved match {
      case Some(value) => value
      case _ =>
        resolved = Some(input.readArgument[T](name, default, index))
        resolve()
    }
  }
}

final class InputOptionDefinition[T: InputOptionReader: ValueReader](
    override val input: Input,
    override val name: String,
    override val default: T,
    override val description: Option[String])
  extends InputParameterDefinition[T] {

  private var resolved: Option[T] = None

  override def resolve(): T = {
    resolved match {
      case Some(value) => value
      case _ =>
        resolved = Some(input.readOption[T](name, default))
        resolve()
    }
  }
}
