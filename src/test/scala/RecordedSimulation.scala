

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object Newest {
        val newest = exec(http("newest")
          .get("/search/api/v5.0/feed/newest").check(status.is(200)));
}

object NewestAudios {
        val newest = exec(http("newest audios")
          .get("/search/api/v5.0/feed/newest?type=soundrecording").check(status.is(200)));
}

object CPKWget {
        val headers = Map(
                "Accept" -> "application/json, text/plain, */*")
        val get = exec(http("CPK")
          .get("/search/api/v5.0/search?q=fedora.model%3Amonograph+AND+modified_date%3A%5B2015-01-01T00%3A00%3A00Z+TO+2015-01-16T00%3A00%3A00Z%5D&fl=PID")
          .check(status.is(200)));
}


class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://krameriustest.mzk.cz")
		.inferHtmlResources()


	val scn = scenario("RecordedSimulation").exec(Newest.newest, NewestAudios.newest, CPKWget.get)

	setUp(scn.inject(rampUsers(1500) over (15 seconds))).protocols(httpProtocol)
}