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

sealed trait InputArgumentReader[A] { self =>
  def read: (String, A, List[InputArgument], Int) => A
}

sealed trait InputOptionReader[A] { self =>
  def read: (String, A, List[InputOption]) => A
}

object InputArgumentReader {
  type ReaderFunc[A] = (String, A, List[InputArgument], Int) => A

  def read[A: ValueReader](f: (String, A, List[InputArgument], Int) => A): InputArgumentReader[A] = new InputArgumentReader[A] {
    val read = f
  }

  implicit val readBool: InputArgumentReader[Boolean] = read[Boolean](reader())
  implicit val readDouble: InputArgumentReader[Double] = read[Double](reader())
  implicit val readInt: InputArgumentReader[Int] = read[Int](reader())
  implicit val readString: InputArgumentReader[String] = read[String](reader())

  private def reader[A: ValueReader](): ReaderFunc[A] = {
    (name: String, default: A, input: List[InputArgument], index: Int) => {
      val reader = implicitly[ValueReader[A]]
      val maybeArg = input.lift(index)

      maybeArg match {
        case Some(argument) => reader.read(argument.token)
        case _ => default
      }
    }
  }
}

object InputOptionReader {
  type ReaderFunc[A] = (String, A, List[InputOption]) => A

  def read[A: ValueReader](f: (String, A, List[InputOption]) => A): InputOptionReader[A] = new InputOptionReader[A] {
    val read = f
  }

  implicit val readBool: InputOptionReader[Boolean] = read[Boolean](reader(emptyVal = Some(true)))
  implicit val readDouble: InputOptionReader[Double] = read[Double](reader())
  implicit val readInt: InputOptionReader[Int] = read[Int](reader())
  implicit val readString: InputOptionReader[String] = read[String](reader())

  private def reader[A: ValueReader](emptyVal: Option[A] = None): ReaderFunc[A] = {
    (name: String, default: A, input: List[InputOption]) => {
      val reader = implicitly[ValueReader[A]]
      val maybeOpt = input.find(_.token == name)

      maybeOpt match {
        case Some(option) => option.value match { // Option present in input
          case Some(value) => reader.read(value) // Actual option value (i.e. --foo=value)
          case _ => emptyVal match { // No value present
            case Some(value) => value // Defined empty value
            case _ => default // No empty value defined, use default
          }
        }
        case _ => default // Option not present in input
      }
    }
  }
}

sealed trait ValueReader[A] { self =>
  def read: String => A
}

object ValueReader {
  def read[A](f: String => A): ValueReader[A] = new ValueReader[A] {
    val read = f
  }

  implicit val intRead: ValueReader[Int] = read { _.toInt }
  implicit val stringRead: ValueReader[String] = read { identity }
  implicit val doubleRead: ValueReader[Double] = read { _.toDouble }
  implicit val boolRead: ValueReader[Boolean] = read {
    _.toLowerCase match {
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
}
