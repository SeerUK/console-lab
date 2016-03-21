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

import sbt._

/**
 * Main Dependencies File
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object Dependencies {
  val repositories = Seq(
    "Eidolon Pull Repo" at "https://repo.eidolonframework.com/"
  )

  def compile(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "compile")
  def container(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "container")
  def provided(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "provided")
  def runtime(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "runtime")
  def test(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "test")

  val chroma = "eidolon" %% "chroma" % "1.0.0"
  val jLine = "jline" % "jline" % "2.13"
  val mockito = "org.mockito" % "mockito-core" % "1.10.19"
  val scalaTest = "org.scalatest" %% "scalatest" % "2.2.6"
  val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.5"
}
