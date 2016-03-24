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

package seeruk.flux

/**
 * Store
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait Store[ST <: State] {
  val initialState: ST
  val reducers: List[Reducer[_]]

  private var _state: ST = initialState

  final def state = _state
  final def dispatch[A <: Action](action: A): Unit = {
    _state = reducers.foldLeft(_state)((accumulator, reducer) => {
      reducer match {
        case reducer: Reducer[ST] => reducer.reduce(accumulator, action)
        case _ => accumulator
      }
    })
  }
}

final case class CounterStore(
    reducers: List[Reducer[_]],
    initialState: CounterState)
  extends Store[CounterState]

trait State

case class CounterState(value: Int) extends State
