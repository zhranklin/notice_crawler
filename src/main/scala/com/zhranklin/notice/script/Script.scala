package com.zhranklin.notice.script

import java.io.File

import com.zhranklin.notice.Logging
import org.json4s.jackson.JsonMethods.parse
import org.json4s.jackson.Serialization.{write, writePretty}

import scala.util.{Try, _}
object Script extends MessageJsonFormats with Logging {
  if (System.getProperties.getProperty("logpath") != null){
    val pwd = System.getenv("PWD")
    log.i(s"logpath未设定, 使用当前目录, 即: $pwd")
  }

  val pretty = java.lang.Boolean.parseBoolean(System.getProperty("pretty", "true"))
  log.i(s"pretty is set to: $pretty")
  val respWrite: (Response) ⇒ String = if (pretty) writePretty[Response] else write[Response]
  val response = respWrite andThen println

  def main(args: Array[String]): Unit = try {
    work()
  } catch {
    case e: Exception ⇒ response(err.UNKNOWN_ERROR)
  }

  def work() = {
    val lines = Iterator.continually(Console.in.readLine).takeWhile(_ != null)
    lines map handleCommand foreach response
  }

  def handleCommand(jsonStr: String): Response = Try(parse(jsonStr)).map(_.extract[Request]) match {
    case Success(r) ⇒ r.handle
    case Failure(e) ⇒
      log.e(e.getMessage, e)
      err.JSON_FORMAT_ERR(e)
  }
}
