import com.typesafe.config.ConfigFactory

name := "play-design-exercise"

lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := "2.12.6",
  javaOptions in Test += "-Dconfig.file=conf/test.conf"
)

lazy val scalikejdbcVersion = "3.2.2"

lazy val postgres = "org.postgresql" % "postgresql" % "42.2.2"

lazy val deps = Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "com.typesafe" % "config" % "1.3.2",
  "org.typelevel" %% "cats-core" % "1.1.0",
  postgres,
  "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcVersion,
  "org.scalikejdbc" %% "scalikejdbc-config"  % scalikejdbcVersion,
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.6.0-scalikejdbc-3.2",
  "org.scalikejdbc" %% "scalikejdbc-test"   % scalikejdbcVersion   % "test"
)

lazy val root = (project in file("."))
  .enablePlugins(FlywayPlugin)
  .settings(
    version := "0.1",
    scalaVersion := "2.12.6",
    libraryDependencies += postgres
  )

lazy val byGuice =
  (project in file("by-guice"))
    .settings(commonSettings)
    .settings(libraryDependencies ++= deps)
    .enablePlugins(PlayScala)

lazy val freeMonad =
  (project in file("free-monad"))
    .settings(commonSettings)
    .settings(libraryDependencies ++= deps)
    .enablePlugins(PlayScala)

lazy val conf = ConfigFactory.parseFile(new File("src/main/resources/application.conf"))
lazy val testConf = ConfigFactory.parseFile(new File("src/main/resources/test.conf"))

flywayLocations += "filesystem:conf/db/migration"
flywayUrl := conf.getString("db.default.url")
flywayUser := conf.getString("db.default.username")
flywayPassword := conf.getString("db.default.password")
flywayLocations in Test += "filesystem:conf/db/migration"
flywayUrl in Test := testConf.getString("db.default.url")
flywayUser in Test := testConf.getString("db.default.username")
flywayPassword in Test := testConf.getString("db.default.password")