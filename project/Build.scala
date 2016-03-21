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

import sbt.Keys._
import sbt.{Build => BaseBuild, _}

/**
 * Main build file
 *
 * @author Elliot Wright <elliot@elliotwright.co>
 */
object Build extends BaseBuild {

  import Dependencies._

  val projectVersion = "0.0.0-SNAPSHOT"

  lazy val commonSettings = Seq(
    organization := "seeruk",
    publishMavenStyle := true,
    publishTo := Some(Resolver.file("release", new File("./release"))),
    resolvers ++= Dependencies.repositories,
    scalaVersion := "2.11.8",
    version := projectVersion,
    testOptions in Test += Tests.Argument("-oDI")
  )

  lazy val consoleLab = (project in file("."))
    .settings(commonSettings: _*)
    .settings(name := "console-lab")
    .settings(libraryDependencies ++=
      compile(chroma, jLine, scalaXml) ++
      test(mockito, scalaTest)
    )
}
