package com.zhranklin.notice.script

import java.net.SocketTimeoutException
import java.time.LocalDateTime

import com.zhranklin.notice.Util._
import com.zhranklin.notice.service.{Notice, NoticeEntry}
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
  def SOCKET_TIMEOUT(e: TimeoutException) = err(3, e.url)
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

case class getnews(source: String, limit: Option[Int], offset: Option[Int], date: Option[LocalDateTime]) extends Request {
  def handle = (for {
    service ← serviceList.find(_.source == source)
    (notices, errs) = service.noticesWithErr(limit.getOrElse(10), offset.getOrElse(0))
  } yield succ(Map(
    "news" → notices,
    "warn" → errs.filter(_.isInstanceOf[TimeoutException]).asInstanceOf[List[TimeoutException]].map(err.SOCKET_TIMEOUT))
  )).getOrElse(SOURCE_NOT_FOUND)
}

case class fetch_test() extends Request {
  def handle = succ(Map("test" → serviceList.map{ service ⇒
    val succ(mp) = getnews(service.source, None, None, None).handle
    mp ++ Map("news" → (mp("news").asInstanceOf[List[Notice]] ++ (1 to 10 map {i ⇒ Map("a" → "b", "c" → "d")})),
      "source" → service.source)
  }))
}

case class fetchnews(source: String, url: String) extends Request {
  def handle = serviceList
    .find(_.source == source)
    .map(_.fetch(NoticeEntry(url, None)))
    .map((n: Notice) ⇒ succ(Map("news" → n)))
    .getOrElse(SOURCE_NOT_FOUND)
}
