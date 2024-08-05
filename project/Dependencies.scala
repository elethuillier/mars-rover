import sbt.*

object Dependencies {
  object CompilerPlugins {
    val `kind-projector`     = ("org.typelevel" %% "kind-projector"     % "0.13.3").cross(CrossVersion.full)
    val `better-monadic-for` = "com.olegpy"     %% "better-monadic-for" % "0.3.1"
  }

  val `testkit` = "org.scalameta" %% "munit" % "1.0.0"
}
