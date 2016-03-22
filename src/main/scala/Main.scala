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

import seeruk.consolelab.ApplicationModule

/**
 * Main
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object Main extends App {
  val appModule = new ApplicationModule(args)

  println("Application run finished with status %d".format(appModule.application.run()))
}
