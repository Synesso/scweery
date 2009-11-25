import sbt._

class ScweeryProject(info: ProjectInfo) extends DefaultProject(info) with AutoCompilerPlugins {
  override def useMavenConfigurations = true

  val specsRepo = "Specs Repository" at "http://specs.googlecode.com/svn/maven2/"

  val specs = "org.scala-tools.testing" % "specs" % "1.5.0" % "test->default"
  val mockito = "org.mockito" % "mockito-core" % "1.7" % "test->default"

  val sxr = compilerPlugin("org.scala-tools.sxr" % "sxr_2.7.5" % "0.2.3")

  override def compileOptions =
    CompileOption("-P:sxr:base-directory:" + mainScalaSourcePath.asFile.getAbsolutePath) ::
            super.compileOptions.toList
}
