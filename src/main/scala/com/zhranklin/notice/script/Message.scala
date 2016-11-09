package com.zhranklin.notice.script

import java.time.LocalDateTime

import com.zhranklin.notice.service.NoticeServiceObjects.serviceList

abstract class Message

abstract class Response extends Message

abstract class Request extends Message {
  def handle: Response
}

case class err(code: Int, msg: String) extends Response

object err {
  val UNKNOWN_ERROR = err(0, "unknown error")
  def JSON_FORMAT_ERR(e: Throwable) = err(1, e.getMessage)
  val SOURCE_NOT_FOUND = err(2, "source not found")
}

import err._

case class succ(result: Map[String, Any]) extends Response

case class listsources() extends Request {
  def handle = succ(Map("sources" → serviceList.map{s ⇒
    Map("name" → s.source,
      "index" → s.template.split("/").take(3).mkString("/"),
      "school" → s.source.split("[ -]")(0))
  }))
}

case class getnews(source: String, date: Option[LocalDateTime]) extends Request {
  def handle = (for {
    service ← serviceList.find(_.source == source)
    notices = service.notices().take(5).toList
  } yield succ(Map("news" → notices))).getOrElse(SOURCE_NOT_FOUND)
}
