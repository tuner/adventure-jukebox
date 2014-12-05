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

import o1.adventure.Game
import org.scalatra.ScalatraServlet

import scala.util.{Failure, Success}

class AdventureServlet(games: Map[String, GameMetadata]) extends ScalatraServlet {

  private val title = "Adventure Jukebox"
  private val gameKey = "game"
  private val adventurePath = "" // TODO: Replace with ServletContextPath. Have to figure out where it should be defined...

  private def gameInstance = this.session.get(gameKey).map(_.asInstanceOf[Game])

  before() {
    contentType = "text/html; charset=utf-8"
    response.addHeader("Cache-control", "no-cache")
  }

  notFound {
    // remove content type in case it was set through an action
    contentType = null

    serveStaticResource() getOrElse resourceNotFound()
  }

  /**
   * Creates the web page footer
   */
  private def gameFooter(game: Game) = {
    <form action={adventurePath + "/"} id="runnerForm" method="post">
      <input type="hidden" name="runnerCommand" value="quit" />
      <input type="submit" value={ if (game.isOver) "Start over" else "End game" } />
    </form>

      <footer>
        <div id="authors">
          Author(s): {game.authors} - {game.year}
        </div>
      </footer>
  }

  private val forkMe = {
    <a href="https://github.com/tuner/adventure-jukebox">
      <img style="position: absolute; top: 0; right: 0; border: 0;"
           src="https://camo.githubusercontent.com/38ef81f8aca64bb9a64448d0d70f1308ef5341ab/68747470733a2f2f73332e616d617a6f6e6177732e636f6d2f6769746875622f726962626f6e732f666f726b6d655f72696768745f6461726b626c75655f3132313632312e706e67"
           alt="Fork me on GitHub"
           data-canonical-src="https://s3.amazonaws.com/github/ribbons/forkme_right_darkblue_121621.png" />
      </a>
      }

  /**
   * Templating method
   */
  private def wrapContent(title: String, content: scala.xml.NodeSeq) = {
    val wrapped =

    <html>
      <head>
        <title>{title}</title>
        <link href={adventurePath + "/adventure.css"} rel="stylesheet"/>
      </head>
      <body>
        <h1>{title}</h1>
        {content}
      </body>
    </html>

    val w = new java.io.StringWriter()
    xml.XML.write(w, wrapped, "UTF-8", xmlDecl = false, doctype =
      xml.dtd.DocType("html"))
    w.toString
  }

  get(adventurePath + "/load/:jar") {
    games(params.get("jar").get).newInstance() match {
      case Success(game) =>
        session += gameKey -> game

        response.sendRedirect("/")

      case Failure(e) =>
        wrapContent(title,
          <div>Can't instantiate: {params.get("jar").get}</div>
        )
    }
  }

  get(adventurePath + "/") {

    gameInstance match {
      case None =>
        wrapContent(title,
          <div>{forkMe}</div>
          <p>
            Adventure Jukebox is a web interface for text adventure games. It was inspired by an
            <a href="https://greengoblin.cs.hut.fi/o1_s2014/course/k10/osa02.html">assignment</a> on
            Aalto University's programming course.
          </p>

          <h2>Be brave and choose an adventure below!</h2>

            <ul id="game-list">
              {games.values.map(n =>
              <li class="game-item">
                <a href={adventurePath + "/load/" + n.filename}>
                  <span class="title">{n.title}</span>
                  <span class="authors">{n.authors}</span>
                  <span class="year">{n.year}</span>
                </a>
              </li>
            )}
            </ul>
            <footer>
              <div id="authors">&copy; Kari Lavikka 2014</div>
            </footer>
        )

      case Some(game) =>
        wrapContent(game.title,
          <main>
            <pre>{game.welcomeMessage}</pre>
            <form action={adventurePath + "/"} method="post">
              <input type="submit" value="Begin adventure"/>
            </form>
          </main>
          ++ gameFooter(game)
        )
    }
  }

  post(adventurePath + "/") {
    if (params.get("runnerCommand").contains("quit")) {
      enrichSession(session) -= gameKey
      enrichResponse(response).redirect(adventurePath)

    } else {

      val game = gameInstance.get

      val actionResponse = params.get("command").map(game.playTurn)

      wrapContent(game.title,
        <main>
          <h2>{game.currentLocationName}</h2>

          <pre id="location">{game.currentLocationDescription}</pre>

          {
          if (actionResponse.isDefined)
            <pre id="response">{actionResponse.get}</pre>
          else
            xml.NodeSeq.Empty
          }

          {
          if (game.isOver)
            <pre class={ if (game.isComplete) "complete" else "incomplete" }>{game.goodbyeMessage}</pre>
          }
        </main>

          <form action={adventurePath + "/"} id="commandForm" method="post">
            {
            if (!game.isOver)
                <input type="text" name="command" size="50" autofocus="autofocus"/>
                  <input type="submit" value="Perform"/>
            else
                <input type="text" name="command" size="50" disabled="disabled"/>
                  <input type="submit" value="Perform" disabled="disabled"/>
            }
          </form>

       ++ gameFooter(game)
      )
    }
  }
  
}
