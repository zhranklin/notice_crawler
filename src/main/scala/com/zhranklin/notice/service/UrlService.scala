package com.zhranklin.notice.service

import com.zhranklin.notice.JsoupUtil
import com.zhranklin.notice.Util._
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

trait UrlService {
  def noticeUrlsFromUrl(url: String): Iterable[NoticeEntry]
}

trait AbstractUrlService extends UrlService {
  type E
  protected def rawUrlsFromDoc(doc: Document): Iterable[E]
  protected def extractFromRawUrl(a: E): NoticeEntry
  def noticeUrlsFromUrl(url: String): Iterable[NoticeEntry] = {
    val doc = Jsoup.connect(url).get
    rawUrlsFromDoc(doc) map extractFromRawUrl
  }
}

trait SelectorUrlService
  extends AbstractUrlService {
  val urlPattern: String
  type E = Element
  def rawUrlsFromDoc(doc: Document) = doc.select(s"a[href~=$urlPattern]").asScala
  def extractFromRawUrl(a: Element) = NoticeEntry(a.attr("abs:href"), Some(a.text))
}

object UniversalUrlService extends UniversalUrlService

trait UniversalUrlService extends UrlService with JsoupUtil {

  def noticeUrlsFromUrl(indexUrl: String) = {
    println(s"index: $indexUrl")
    def getPostFix(url: String) = """(?<=\.)\w+$""".r.findFirstIn(url).getOrElse("")
    def properGroup(urls: Seq[Element], pre: Int) = {
      val counts = groupByPreFix(urls, pre).map(_._2.size)
      counts.count(_ > 5) - (urls.size - counts.max) / 18
    }
    def tryGroup(urls: Seq[Element]) = 1 to 200 takeWhile {properGroup(urls, _) > 0} last
    def groupByPreFix(urls: Seq[Element], pre: Int) = urls.groupBy(e ⇒ (e.href.take(pre), getPostFix(e.href)))
    def longEnough(urls: Seq[Element]) = urls.map(_.text.length).sum / urls.size.asInstanceOf[Double] > 7
    val doc = Jsoup.connect(indexUrl).get
    doc.body.select("a[href]").asScala.map(a ⇒ a.html(a.text))
    doc.body select "*:not(:has(a[href]))" select "*:not(a[href])" remove
    val urls = doc.select("*:last-of-type:nth-of-type(n+5)").asScala
      .flatMap(_.parent.select("a[href]").asScala)
      .filterNot(_.attr("href").endsWith("/"))
    try {
      val urlsLongEnough = groupByPreFix(urls, tryGroup(urls)).values.filter(longEnough).flatten.toSeq
      val preLen = tryGroup(urlsLongEnough)
      val shrunkenPreLen = "^.*?(?=\\d+$)".r.findFirstIn(urlsLongEnough.head.href.take(preLen)).map(_.length).getOrElse(preLen)
      val ret = groupByPreFix(urlsLongEnough, shrunkenPreLen).values
        .filter(_.size > 5).flatten.map(e ⇒ NoticeEntry(e.absHref, Some(e.text)))
      println(s"*****\nret: $ret\n*****")
      ret
    } catch {
      case e: UnsupportedOperationException ⇒ Nil
      case e: Exception ⇒
        e.printStackTrace(Console.out)
        Nil
    }
  }
}
