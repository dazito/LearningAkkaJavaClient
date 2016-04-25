name := """akkademy-db-client"""

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor"     % "2.3.7",
    "com.typesafe.akka" %% "akka-cluster"   % "2.3.7",
    "com.typesafe.akka" %% "akka-contrib"   % "2.3.7",
    "junit"             % "junit"           % "4.12"  % "test",
    "com.novocode"      % "junit-interface" % "0.11"  % "test"
)
