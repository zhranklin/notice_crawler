package com.zhranklin.notice.service

import java.util.Date

import org.jsoup.Jsoup

import scala.collection.JavaConverters._
import scala.util._

trait IndexService {
  val template: String
  def first = rawIndices.head
  protected def firstIndex: Int = 1
  protected def valueStream(i: Int): Stream[Int] = i #:: valueStream(i + 1)
  protected def indexNums: Iterable[Any] = valueStream(firstIndex)
  protected def interpolate(value: Any): String = template.replaceAll("<index>", value.toString)
  def rawIndices: Iterable[String] = indexNums map interpolate
  def indexUrls: Iterable[String] = Stream(first) ++ rawIndices.drop(1)
}

case class Notice(url: String, title: String, html: String, date: Date) {
  def widthlessHtml = {
    val doc = Jsoup.parse(html)
    doc.select("*[width]").asScala.map(_.removeAttr("width"))
    doc.select("*[height]").asScala.map(_.removeAttr("height"))
    doc.toString
  }
  def stylelessHtml = {
    val doc = Jsoup.parse(html)
    doc.select("*[width]").asScala.map(_.removeAttr("width"))
    doc.select("*[height]").asScala.map(_.removeAttr("height"))
    doc.select("*[style]").asScala.map(_.removeAttr("style"))
    doc.toString
  }
  def imgs = Jsoup.parse(html).select("img[src]").asScala.map(_.attr("src"))
}
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
    failures.groupBy(_.getClass.getSimpleName).map(_._2.head).foreach(t ⇒ log.i(s"error when fetching news", t))
    (successes, failures)
  }
}
