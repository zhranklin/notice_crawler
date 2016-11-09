package com.zhranklin.notice.service

import java.util.Date

import com.zhranklin.notice._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import scala.util.Try

trait NoticeFetcher extends Util with DateUtils with Logging {
  protected def parse(doc: Document, title: Option[String]): Notice
  protected def exDate(arg: String) = {
    val du.dMatch(dStr) = arg
    new Date(dStr replaceAll (du.splittersOr, "/"))
  }
  private def absLink(doc: Document) = {
    List("href", "src").foreach(n ⇒ doc.select(s"[$n]").asScala.map(l ⇒ l.attr(n, l.attr(s"abs:$n"))))
    doc
  }
  def fetch(entry: NoticeEntry): Notice = parse(absLink(Jsoup.connect(entry.url).get), entry.title)
}

trait FunNoticeFetcher extends NoticeFetcher {
  def selectorF(selector: String)(Then: Elements ⇒ String) = ((_: Document).select(selector)).andThen(Then)
  def dateF(selector: String) = selectorF(selector)(_.first.text)
  def contentF(selector: String) = selectorF(selector)(_.first.html)
  val getContent: Document ⇒ String
  val getDateStr: Document ⇒ String
  def parse(doc: Document, title: Option[String]) = {
    log.t(doc.baseUri)
    Notice(doc.baseUri, title.getOrElse(doc.title), getContent(doc), exDate(getDateStr(doc)))
  }
}

trait UniversalNoticeFetcher extends NoticeFetcher {
  import UniversalUrlService._
  def disLink(doc: Document) = {
    val urls =  noticeUrlsFromUrl(doc.baseUri).map(_.url)
    urls.foreach { u ⇒
      val sel = s"""a[abs:href="$u"]"""
      val e = doc.select(sel).first
      val text = e.text.replaceAll("\\s+", "")
      var p = e
      while(p.parent.text.replaceAll("\\s+", "") == text) p = p.parent
      p.remove()
    }
  }
  def getTime(e: Element): Option[(Date, Element, Int)] = Try(du.dMatch.unapplySeq(e.text).get.head)
    .map(_.replaceAll(du.splittersOr, "/"))
    .map(new Date(_)).filter(i ⇒ e.text.replaceAll("\\s+", "").length < 60).toOption
    .map ((_, e, List("更新", "阅读", "来源", "时间", "编辑", "责任").count(e.text.contains) * 100 + e.select("*").size))

  def properTime(doc: Document) = doc.select("*").asScala.toIterator.flatMap(getTime).maxBy(_._3) //TODO: 这里的重构未检查

  def parse(doc: Document, title: Option[String]) = {
    disLink(doc)
    val time = properTime(doc)
    val html = time._2.siblingElements.asScala.maxBy(_.text.length).html
    Notice(doc.baseUri, title.getOrElse(doc.title), html, time._1)
  }
}