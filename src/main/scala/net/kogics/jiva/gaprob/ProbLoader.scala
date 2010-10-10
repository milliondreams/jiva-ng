/*
 * Copyright (C) 2007 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License 
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */

package net.kogics.jiva.gaprob

import scala.tools.nsc.{Global, Settings, GenericRunnerSettings}
import scala.tools.nsc.reporters.ConsoleReporter
import java.net.{URL, URLClassLoader}
import java.lang.reflect.{Method, Modifier}
import java.io.File

object ProbLoader {
  
  def load(fname0: String) : GaProblem[AnyRef] = {
    val classesDir = new File("./work")
    
    if (!classesDir.exists) classesDir.mkdir
    
    val probfile = new File(fname0)
    val cname = probfile.getName.substring(0, probfile.getName.indexOf('.'))
    val probcfile = new File(classesDir, cname + ".class")
    
    if (probfile.lastModified > probcfile.lastModified) {
      println("Processing file: " + fname0)

      val fname = List(fname0)
      val settings = new GenericRunnerSettings(Console.println _)
      settings.classpath.value = "./target/jiva.jar"
      settings.outdir.value = classesDir.getPath
      settings.deprecation.value = true // enable detailed  deprecation warnings
      settings.unchecked.value = true // enable detailed unchecked warnings
//      settings.Xgenerics.value = true
      settings.target.value = "jvm-1.5"

      val reporter = new ConsoleReporter(settings)

      val compiler = new Global(settings, reporter)
      (new compiler.Run).compile(fname)

      reporter.printSummary
      if (reporter.hasErrors || reporter.WARNING.count > 0)
        {
          println("Problem Compiling GAProb")
          throw new RuntimeException("Problem Compiling GAProb")
        }
    }

    val urls = Array[URL](classesDir.toURL)
    val loader = new URLClassLoader(urls, Thread.currentThread.getContextClassLoader)
    println("Loading: " + cname)
    val clazz = loader.loadClass(cname)
    getProblem(clazz.newInstance.asInstanceOf[AnyRef])
    
  }
  
  def getProblem(obj: AnyRef): GaProblem[AnyRef] = {
    var problem: GaProblem[AnyRef] = obj match  {
    case gap: GaProblem[_] => gap.asInstanceOf[GaProblem[AnyRef]]
    case _ => {val m = obj.getClass.getMethod("gaProblem")
               m.invoke(obj).asInstanceOf[GaProblem[AnyRef]]}
    }
    problem
  }
}
