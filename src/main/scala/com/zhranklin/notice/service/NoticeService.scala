package com.zhranklin.notice.service

import java.net.SocketTimeoutException
import java.util.Date

import scala.util._

trait IndexService {
  val template: String
  protected def firstIndex: Int = 1
  protected def valueStream(i: Int): Stream[Int] = i #:: valueStream(i + 1)
  protected def indexNums: Iterable[Any] = valueStream(firstIndex)
  protected def interpolate(value: Any): String = template.replaceAll("<index>", value.toString)
  def indexUrls: Iterable[String] = indexNums map interpolate
}

case class Notice(url: String, title: String, html: String, date: Date)
case class NoticeEntry(url: String, title: Option[String] = None)

abstract class NoticeService(val source: String) extends UrlService with IndexService with NoticeFetcher {
  def getUrls: Iterable[Try[NoticeEntry]] = indexUrls.map (i ⇒ Try(noticeUrlsFromUrl(i))).flatMap {
    case Success(urls) ⇒ urls map Success.apply
    case Failure(t) ⇒ Iterable(Failure(t))
  }
  def notices: Iterable[Try[Notice]] = getUrls.map (_.flatMap(u ⇒ Try(fetch(u))))
  def noticesWithErr(limit: Int, offset: Int): (List[Notice], List[Throwable]) = {
    val (succ, err) = notices.slice(offset, offset + limit).toList.partition(_.isSuccess)
    val successes = succ.asInstanceOf[List[Success[Notice]]].map(_.value)
    val failures = err.asInstanceOf[List[Failure[Throwable]]].map(_.exception)
    log.i(failures map (e ⇒ s"${e.getClass.getSimpleName}: ${e.getMessage}") mkString "\n")
    (successes, failures)
  }
}
