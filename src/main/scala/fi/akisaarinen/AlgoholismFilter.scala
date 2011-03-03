package fi.akisaarinen

import org.scalatra._
import java.net.URL
import net.liftweb.json.JsonAST
import net.liftweb.json.JsonDSL
import net.liftweb.json.JsonParser

class AlgoholismFilter extends ScalatraFilter {

  get("/") {
    <html>
      <body>
        <h1>Use POST</h1>
      </body>
    </html>
  }
  
  post("/") {
    contentType = "application/json"

    val input = getBody(request.body, params.keys)
    if (input.size > 0) {
      val json = JsonParser.parse(input)
      JsonDSL.compact(process(json)) + "\n"
    } else {
      ""
    }
  }

  notFound {
    response.setStatus(404)
    <html>
      <body>
        <h1>404</h1>
      </body>
    </html>
  }


  private def getBody(body: String, paramNames: Iterable[String]): String = {
    // Sometimes scalatra seems to eat the body and put the content to first
    // key of the map. I didn't want to invest time on finding out why, this'll do :-)
    if (paramNames.size > 0) paramNames.head else body
  }

  private def process(json: JsonAST.JValue): text.Document = {
    import net.liftweb.json.JsonAST._
    import net.liftweb.json.JsonDSL._

    val name = getField(json, "a")
    val timeoutMs = getField(json, "b")

    render(List(name, timeoutMs))
  }

  private def getField(json: JsonAST.JValue, field: String): String = {
    import net.liftweb.json.JsonAST._
    import net.liftweb.json.JsonDSL._
    json \\ field match {
      case JField(_, JString(value)) => value.toString
    }
  }
}
