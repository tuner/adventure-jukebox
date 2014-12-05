package o1.adventurejukebox

import org.scalatra.test.specs2._

// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
class AdventureServletSpec extends ScalatraSpec { def is =
  "GET / on AdventureServlet"                     ^
    "should return status 200"                  ! root200^
                                                end

  addServlet(classOf[AdventureServlet], "/*")

  def root200 = get("/") {
    status must_== 200
  }
}
