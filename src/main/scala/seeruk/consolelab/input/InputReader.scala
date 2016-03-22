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
  def read: (String, A, List[InputArgument]) => A
}

sealed trait InputOptionReader[A] { self =>
  def read: (String, A, List[InputOption]) => A
}

object InputArgumentReader {
  private var idx = 0

  def read[A: ValueReader](f: (String, A, List[InputArgument]) => A): InputArgumentReader[A] = new InputArgumentReader[A] {
    val read = f
  }

  implicit val readString: InputArgumentReader[String] =
    read((name: String, default: String, input: List[InputArgument]) => {
      val reader = implicitly[ValueReader[String]]
      val maybeArg = input.lift(idx)

      idx = idx + 1

      maybeArg match {
        case Some(argument) => reader.read(argument.token)
        case _ => default
      }
    })

  implicit val readBool: InputArgumentReader[Boolean] =
    read((name: String, default: Boolean, input: List[InputArgument]) => {
      val reader = implicitly[ValueReader[Boolean]]

      reader.read("false")
    })
}

object InputOptionReader {
  def read[A: ValueReader](f: (String, A, List[InputOption]) => A): InputOptionReader[A] = new InputOptionReader[A] {
    val read = f
  }

  implicit val readString: InputOptionReader[String] =
    read((name: String, default: String, input: List[InputOption]) => {
      val reader = implicitly[ValueReader[String]]

      input
        .find(_.token == name) match {
          case Some(option) => option.value match {
            case Some(value) => reader.read(value)
            case _ => default
          }
          case _ => default
        }
    })

  implicit val readBool: InputOptionReader[Boolean] =
    read((name: String, default: Boolean, input: List[InputOption]) => {
      val reader = implicitly[ValueReader[Boolean]]

      input
        .find(_.token == name) match {
          case Some(option) => option.value match {
            case Some(value) => reader.read(value)
            case _ => true // Boolean options without values (i.e. flags), are true if present
          }
          case _ => default
        }
    })
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
