import scala.util.Try

lazy val baseSettings = Seq(
  scalaVersion                           := "2.13.14",
  Compile / doc / sources                := Seq.empty,
  Compile / packageDoc / publishArtifact := false,
  scalacOptions ++= Seq(
    "-Ymacro-annotations"
  ),
  // Uncomment to remove fatal warnings
  //  scalacOptions --= Seq(
  //    "-Xfatal-warnings"
  //  ),
  addCompilerPlugin(Dependencies.CompilerPlugins.`kind-projector`),
  addCompilerPlugin(Dependencies.CompilerPlugins.`better-monadic-for`)
)

lazy val scalafixSettings = Seq(
  semanticdbEnabled := true, // enable SemanticDB
  semanticdbOptions += "-P:semanticdb:synthetics:on",
  semanticdbVersion := scalafixSemanticdb.revision, // use Scalafix compatible version
  ThisBuild / scalafixDependencies ++= List(
    "com.github.liancheng" %% "organize-imports" % "0.6.0",
    "com.github.vovapolu"  %% "scaluzzi"         % "0.1.23"
  )
)

lazy val buildInfoSettings = {
  import sbtbuildinfo.BuildInfoKey.action

  Seq(
    buildInfoKeys := Seq[BuildInfoKey](
      name,
      version,
      scalaVersion,
      sbtVersion,
      action("lastCommitHash") {
        import scala.sys.process._
        // if the build is done outside of a git repository, we still want it to succeed
        Try("git rev-parse HEAD".!!.trim).getOrElse("?")
      }
    ),
    buildInfoOptions += BuildInfoOption.BuildTime,
    buildInfoOptions += BuildInfoOption.ToJson,
    buildInfoOptions += BuildInfoOption.ToMap,
    buildInfoObject := "BuildInfo"
  )
}

lazy val testSettings = Seq(
  testFrameworks += new TestFramework("munit.Framework")
)

lazy val `root` = project
  .in(file("."))
  .enablePlugins(BuildInfoPlugin)
  .enablePlugins(JavaAppPackaging)
  .settings(baseSettings)
  .settings(testSettings)
  .settings(scalafixSettings)
  .settings(buildInfoSettings)
  .settings(preCommitSettings)
  .settings(
    name       := "mars-rover",
    run / fork := true
  )

Global / onChangedBuildSource := ReloadOnSourceChanges
Global / cancelable           := true

// Install pre-commit hook
lazy val installPreCommit = taskKey[Unit]("Install pre-commit hook")

lazy val startupTransition: State => State = { s: State =>
  "installPreCommit" :: s
}

lazy val preCommitSettings = Seq(
  installPreCommit := {
    import scala.sys.process._
    import scala.util._

    val log = streams.value.log
    log.info("Install pre-commit hook")

    Try("pre-commit install".!!) match {
      case Failure(_) => log.error("error install pre-commit hook. Please ensure pre-commit is installed")
      case Success(_) => log.info("pre-commit hook properly installed")
    }
  }
)

Global / onLoad := {
  val old = (Global / onLoad).value
  // compose the new transition on top of the existing one
  // in case your plugins are using this hook.
  startupTransition.compose(old)
}
