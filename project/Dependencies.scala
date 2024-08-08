import sbt.*

object Dependencies {
  object CompilerPlugins {
    val `kind-projector`     = ("org.typelevel" %% "kind-projector"     % "0.13.3").cross(CrossVersion.full)
    val `better-monadic-for` = "com.olegpy"     %% "better-monadic-for" % "0.3.1"
  }

  object Versions {
    val pureconfig      = "0.17.7"
    val enumeratum      = "1.7.3"
    val cats            = "3.5.4"
    val munitCatsEffect = "2.0.0"
  }

  val core: Seq[ModuleID] = Seq(
    "com.github.pureconfig" %% "pureconfig"  % Versions.pureconfig,
    "com.beachape"          %% "enumeratum"  % Versions.enumeratum,
    "org.typelevel"         %% "cats-effect" % Versions.cats
  )

  val `testkit` = "org.typelevel" %% "munit-cats-effect" % Versions.munitCatsEffect
}
