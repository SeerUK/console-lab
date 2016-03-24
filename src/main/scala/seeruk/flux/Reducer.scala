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
 * Reducers must be pure, i.e. no functions that have side-effects (non-pure functions) should be
 * called. No surprises. No side effects.
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait Reducer[ST <: State] {
  def reduce[A <: Action](state: ST, action: A): ST
}

class CounterReducer extends Reducer[CounterState] {
  override def reduce[A <: Action](state: CounterState, action: A): CounterState = {
    action match {
      case IncreaseAction(amount) => state.copy(value = state.value + amount)
      case DecreaseAction(amount) => state.copy(value = state.value - amount)
      case ResetAction() => state.copy(value = 0)
      case _ => state.copy()
    }
  }
}
