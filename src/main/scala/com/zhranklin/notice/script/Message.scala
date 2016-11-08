package com.zhranklin.notice.script

import com.zhranklin.notice.service._
import java.time.LocalDateTime
import NoticeServiceObjects.serviceList

abstract class Message

abstract class Response extends Message

abstract class Request extends Message {
  def handle: Response
}

case class err(code: Int, msg: String) extends Response

case class succ(result: Map[String, Any]) extends Response



case class listsources() extends Request {
  def handle = succ(Map("sources" → serviceList.map(_.source)))
}

case class getnews(source: String, date: Option[LocalDateTime]) extends Request {
  def handle = succ(Map("news" → serviceList.filter(_.source == source).head
      .notices().take(5).toList
  ))
}
