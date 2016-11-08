package com.zhranklin.notice.script

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.write

import scala.util.{Try, _}
object Script extends MessageJsonFormats {

  object log {
    def e = Console.out
    def e(msg: String): Unit = e.println(msg)
  }

  def main(args: Array[String]): Unit = {
    val lines = Iterator.continually(Console.in.readLine).takeWhile(_ != null)
    lines map handleCommand map (r ⇒ pretty(parse(write[Response](r)))) foreach println
  }

  def handleCommand(jsonStr: String): Response = Try(parse(jsonStr)) match {
    case Success(json) ⇒ handleJson(json)
    case Failure(e) ⇒
      e.printStackTrace(log.e)
      err(1, e.getMessage)
  }

  def handleJson(json: JValue): Response = {
    val request = json.extract[Request]
    request.handle
  }
}
