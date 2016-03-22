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

sealed trait InputReader[A] { self =>
  def read: (String, A, List[InputParameter]) => A
}

object InputReader {
  def read[A: ValueReader](f: (String, A, List[InputParameter]) => A): InputReader[A] = new InputReader[A] {
    val read = f
  }

  implicit val readStringInputOption: InputReader[String] =
    read((name: String, default: String, input: List[InputParameter]) => {
      val reader = implicitly[ValueReader[String]]

      input
        .collect { case option: InputOption => option }
        .find(_.token == name) match {
          case Some(option) => option.value match {
            case Some(value) => reader.reads(value)
            case _ => default
          }
          case _ => default
        }
    })

  implicit val readBoolInputOption: InputReader[Boolean] =
    read((name: String, default: Boolean, input: List[InputParameter]) => {
      val reader = implicitly[ValueReader[Boolean]]

      input
        .collect { case option: InputOption => option }
        .find(_.token == name) match {
          case Some(option) => option.value match {
            case Some(value) => reader.reads(value)
            case _ => true // Boolean options without values (i.e. flags), are true if present
          }

          // Seems a little bit odd to allow a different default other than false here, give that if
          // the only way this option could be false in that case is if someone passed something that
          // was read as false as a value (i.e. --foo=0, --foo=false, --foo=n, --foo=no, etc). On the
          // other hand, this does allow the developer to have more control, even if it is a little
          // counter-intuitive...
          case _ => default
        }
    })
}

sealed trait ValueReader[A] { self =>
  def reads: String => A
}

object ValueReader {
  def reads[A](f: String => A): ValueReader[A] = new ValueReader[A] {
    val reads = f
  }

  implicit val intRead: ValueReader[Int] = reads { _.toInt }
  implicit val stringRead: ValueReader[String] = reads { identity }
  implicit val doubleRead: ValueReader[Double] = reads { _.toDouble }
  implicit val boolRead: ValueReader[Boolean] = reads {
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
