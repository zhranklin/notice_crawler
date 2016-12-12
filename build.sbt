organization  := "com.zhranklin.blog"

version       := "0.1"

scalaVersion  := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-target:jvm-1.8")
javacOptions := Seq("-encoding", "utf8", "-source" , "1.8" , "-target" , "1.8" )

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= {
  Seq(
    "org.json4s"               %%  "json4s-jackson"          % "3.4.2",
    "org.scalatest"            %%  "scalatest"               % "3.0.0"     % "test",
    "org.jsoup"                %   "jsoup"                   % "1.9.2",
    "ch.qos.logback"           %   "logback-classic"         % "1.1.7",
    "com.typesafe.scala-logging"%% "scala-logging"           % "3.5.0",
    "org.scalaz"               %%  "scalaz-core"             % "7.2.7"
  )
}

Revolver.settings
mainClass in assembly := Some("com.zhranklin.notice.script.Script")
