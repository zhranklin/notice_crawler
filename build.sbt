organization  := "com.zhranklin.blog"

version       := "0.1"

scalaVersion  := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-Yopt:_")

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val akka_httpV = "3.0.0-RC1"
  Seq(
    "org.json4s"          %%  "json4s-jackson"      % "3.4.+",
    "org.scalatest"       %%  "scalatest"           % "3.0.+"     % "test",
    "org.jsoup"           %   "jsoup"               % "1.9.2",
    "org.slf4j"           %   "slf4j-simple"        % "1.7.21",
    "org.apache.logging.log4j" % "log4j-core" % "2.7"
  )
}

Revolver.settings
mainClass in assembly := Some("com.zhranklin.homepage.Boot")
