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
    val input = getBody(request.body, params.keys)
    if (input.size > 0) {
      val json = JsonParser.parse(input)
      contentType = "application/json"
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
    if (paramNames.size > 0) paramNames.head else body
  }

  private def process(json: JsonAST.JValue): text.Document = {
    import net.liftweb.json.JsonAST._
    import net.liftweb.json.JsonDSL._
    json \\ "a" match {
      case JField("a", JString("lol")) => render(List(1,3))
      case _ => render(json)
    }
  }
}
