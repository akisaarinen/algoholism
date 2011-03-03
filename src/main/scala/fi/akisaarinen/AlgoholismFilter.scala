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

    val name = getString(json, "name")
    val timeoutMs = getInt(json, "timeout")
    val items = getItemList(json, "contents")
    val capacity = getIntList(json, "capacity")

    val input = Input(name, timeoutMs, items, capacity)

    render(calculateBestAnswer(input))
  }

  private def calculateBestAnswer(input: Input): List[Int] = {
    return input.items.filter { item =>
      item.id != 2
    }.map(_.id)
  }

  private def getString(json: JsonAST.JValue, field: String): String = {
    import net.liftweb.json.JsonAST._
    import net.liftweb.json.JsonDSL._
    json \\ field match {
      case JField(_, JString(value)) => value.toString
    }
  }

  private def getInt(json: JsonAST.JValue, field: String): Int = {
    import net.liftweb.json.JsonAST._
    import net.liftweb.json.JsonDSL._
    json \\ field match {
      case JField(_, JInt(value)) => value.toInt
    }
  }

  private def getItemList(json: JsonAST.JValue, field: String): List[Item] = {
    import net.liftweb.json.JsonAST._
    import net.liftweb.json.JsonDSL._
    json \\ field match {
      case JField(_, JArray(items)) => items.map { item =>
        item match {
          case JObject(foo) => {
            val id = getInt(foo, "id")
            val weight = getIntList(foo, "weight")
            val value = getInt(foo, "value")
            Item(id, value, weight)
          }
        }
      }
    }
  }

  private def getIntList(json: JsonAST.JValue, field: String): List[Int] = {
    import net.liftweb.json.JsonAST._
    import net.liftweb.json.JsonDSL._
    json \\ field match {
      case JField(_, JArray(items)) => items.map { item =>
        item match {
          case JInt(value) => value.toInt
        }
      }
    }
  }
}
