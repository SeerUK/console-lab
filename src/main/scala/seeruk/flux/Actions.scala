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
 * Actions are payloads of information that send data from an application's code to a store. They
 * should be the only source of information for the store. They should only be sent via the store's
 * dispatch method.
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
trait Actions
trait Action

case class IncreaseAction(amount: Int) extends Action
case class DecreaseAction(amount: Int) extends Action
case class ResetAction() extends Action

object CounterActions extends Actions {
  def increaseCounter(amount: Int): IncreaseAction = IncreaseAction(amount)
  def decreaseCounter(amount: Int): DecreaseAction = DecreaseAction(amount)
  def resetCounter(): ResetAction = ResetAction()
}
