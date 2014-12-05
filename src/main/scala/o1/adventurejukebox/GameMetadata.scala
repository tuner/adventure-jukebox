package o1.adventurejukebox

/*
 * Copyright (c) 2014 Kari Lavikka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.io.File
import java.net.URLClassLoader
import java.util.Properties

import o1.adventure.Game

import scala.util.{Failure, Success, Try}

/**
 * @author Kari Lavikka
 */
class GameMetadata(jar: File) {

  val classLoader = new URLClassLoader(Array(jar.toURI.toURL), getClass.getClassLoader)

  val props = new Properties
  props.load(classLoader.getResourceAsStream("metadata.properties"))

  def newInstance(): Try[Game] = {
    try {
      Success(classLoader.loadClass(gameClass).newInstance().asInstanceOf[Game])

    } catch {
      case e: Exception => Failure(e)
    }
  }

  def filename = jar.getName
  def gameClass = props.getProperty("gameClass")
  def title = props.getProperty("title")
  def authors = props.getProperty("authors")
  def year = props.getProperty("year")

}
