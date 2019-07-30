lazy val root = (project in file(".")).
  settings(
    name := "TcpDemo",
    version := "1.0",
    scalaVersion := "2.12.7",
    mainClass in Compile := Some("RunApp")
  )
