import sbt._

object Dependencies {
  object CompilerPlugins {
    val `kind-projector`     = ("org.typelevel" %% "kind-projector"     % "0.13.2").cross(CrossVersion.full)
    val `better-monadic-for` = "com.olegpy"     %% "better-monadic-for" % "0.3.1"
  }
}