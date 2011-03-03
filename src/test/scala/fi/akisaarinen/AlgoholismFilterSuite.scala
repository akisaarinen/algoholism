package fi.akisaarinen

import org.scalatra._
import org.scalatra.test.scalatest._
import org.scalatest.matchers._

class AlgoholismFilterSuite extends ScalatraFunSuite with ShouldMatchers {
  addFilter(classOf[AlgoholismFilter], "/*")

  // a = eiväliä
  // b = aikaraja (ms?)
  // c = kamat (e = weights, f = arvo)
  // g = constraintit

  test("GET / returns status 200") {
    get("/") {
      response.getContentType should equal("text/html; charset=utf-8")
      status should equal (200)
    }
  }

  test("GET /foo returns status 404") {
    get("/foo") {
      response.getContentType should equal("text/html; charset=utf-8")
      status should equal (404)
    }
  }

  test("POST / returns status 200 with empty body") {
    post("/") { 
      status should equal (200)
      response.getContentType should equal("application/json; charset=UTF-8")
      body should equal("")
    }
  }

  test("POST / returns fake algorithm reply with correct magic value of 'a'") {
    val data =
    """
{
    "a":"lol",
    "b":6000,
    "c":[
        {"id":1,"e":[10, 20, 30],"f":40},
        {"id":2,"e":[40,50,60],"f":100},
        {"id":3,"e":[30,60,90],"f":200}
    ],
    "g":[99,10,1000]
}
"""


    post("/", data, Map[String,String]()) {
      status should equal (200)
      response.getContentType should equal("application/json; charset=UTF-8")
      body should equal("[\"lol\",\"60000\"]\n")
    }
  }
  /*
  
  test("POST / returns compacted JSON with line change with wrong value of 'a'") {
    val testJson = """{ "a" : "not_funny" }"""
    post("/", testJson, Map[String,String]()) { 
      status should equal (200)
      response.getContentType should equal("application/json; charset=UTF-8")
      body should equal("""{"a":"not_funny"}""" + "\n")
    }
  }

  test("POST / returns compacted JSON with line change for random json") {
    val testJson = """{ "foo" : "bar" }"""
    post("/", testJson, Map[String,String]()) { 
      status should equal (200)
      response.getContentType should equal("application/json; charset=UTF-8")
      body should equal("""{"foo":"bar"}""" + "\n")
    }
  }
  */

  test("POST /foo returns status 404") {
    post("/foo") {
      response.getContentType should equal("text/html; charset=utf-8")
      status should equal (404)
    }
  }
}
