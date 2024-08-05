def styled(in: Any): String =
  scala.Console.CYAN + in + scala.Console.RESET

addCommandAlias("c", "compile")
addCommandAlias("ca", "Test / compile")
addCommandAlias("t", "test")
addCommandAlias("r", "run")
addCommandAlias(
  "up2date",
  ";dependencyUpdates; reload plugins; dependencyUpdates"
)
addCommandAlias("prepare", "fix; fmt")
addCommandAlias("fmt", "all scalafmtSbt scalafmtAll")
addCommandAlias("fmtCheck", "all scalafmtSbtCheck scalafmtCheckAll")
addCommandAlias("fix", "scalafixAll")
addCommandAlias("fixCheck", "scalafixAll --check")

onLoadMessage +=
  s"""|
      |───────────────────────────
      |  List of defined ${styled("aliases")}
      |────────┬──────────────────
      |${styled("c")}       │ compile
      |${styled("ca")}      │ compile all
      |${styled("t")}       │ test
      |${styled("r")}       │ run
      |${styled("up2date")} │ dependencyUpdates
      |────────┴──────────────────""".stripMargin
