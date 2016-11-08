package com.zhranklin.notice

import java.net.{URLDecoder, URLEncoder}

import scala.collection.convert.{DecorateAsJava, DecorateAsScala}

trait Util extends DecorateAsJava with DecorateAsScala {
  val encode = URLEncoder.encode(_: String, "utf-8")
  val decode = URLDecoder.decode(_: String, "utf-8")
  def getToken = System.getenv("PSW")

  object <> {
    def i[I](implicit i: I) = i
  }

  implicit class Infix[A](o: A) {
    def apply(i: <>.type) = o
  }
}

object Util extends Util

trait JsoupUtil extends Util {
  type Element = org.jsoup.nodes.Element
  type Document = org.jsoup.nodes.Document
  type Elements = org.jsoup.select.Elements
  implicit class RichElement(element: Element) {
    def href = element.attr("href")
    def absHref = element.attr("abs:href")
  }

  type Date = java.util.Date
  type Try[E] = scala.util.Try[E]
}

trait DateUtils {
  import scala.collection.JavaConverters._
  val du = new {
    val splitters = List("-", "\\.", "/")
    val splittersOr = splitters mkString "|"
    val d2 = "\\d{1,2}"
    val dPattern = String.join("|", splitters.map(s ⇒ s"(?:(?:\\d{4}$s)?$d2$s$d2(?:\\s+$d2:$d2(?::$d2)?)?)").asJava)
    val dMatch = s".*?(?<!\\d)($dPattern)(?!\\d).*?".r

  }
}

