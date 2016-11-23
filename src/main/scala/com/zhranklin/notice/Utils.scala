package com.zhranklin.notice

import java.net._

import com.typesafe.scalalogging.LazyLogging
import org.jsoup.Jsoup
import org.slf4j.Marker

import scala.collection.Traversable
import scala.collection.convert.{DecorateAsJava, DecorateAsScala}

trait Util extends DecorateAsJava with DecorateAsScala {
  val encode = URLEncoder.encode(_: String, "utf-8")
  val decode = URLDecoder.decode(_: String, "utf-8")

  def getToken = System.getenv("PSW")

  case class TimeoutException(url: String, attempt: Int) extends SocketTimeoutException {
    override def getMessage = s"fetching from $url time out after $attempt attempts"
  }

  object <> {
    def i[I](implicit i: I) = i
  }

  implicit class Infix[A](o: A) {
    def apply(i: <>.type) = o
  }

}

object JavaCompact {
  import scala.collection.JavaConverters._
  def toJavaIterable[T](ji: java.lang.Iterable[T]): Iterable[T] = ji.asScala
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

  val MAX_ATTEMPT = 5
  def getPage(url: String, attempt: Int = 0): Document = try {
    Jsoup.connect(url).get
  } catch {
    case _: SocketTimeoutException if attempt < MAX_ATTEMPT ⇒ getPage(url, attempt + 1)
    case _: SocketTimeoutException ⇒ throw TimeoutException(url, attempt)
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

trait Logging extends LazyLogging {

  object log {
    def e(message: String) = logger.error(message)
    def e(message: String, cause: Throwable) = logger.error(message, cause)
    def e(marker: Marker, message: String) = logger.error(marker, message)
    def e(marker: Marker, message: String, cause: Throwable) = logger.error(marker, message, cause)
    def w(message: String) = logger.warn(message)
    def w(message: String, cause: Throwable) = logger.warn(message, cause)
    def w(marker: Marker, message: String) = logger.warn(marker, message)
    def w(marker: Marker, message: String, cause: Throwable) = logger.warn(marker, message, cause)
    def i(message: String) = logger.info(message)
    def i(message: String, cause: Throwable) = logger.info(message, cause)
    def i(marker: Marker, message: String) = logger.info(marker, message)
    def i(marker: Marker, message: String, cause: Throwable) = logger.info(marker, message, cause)
    def d(message: String) = logger.debug(message)
    def d(message: String, cause: Throwable) = logger.debug(message, cause)
    def d(marker: Marker, message: String) = logger.debug(marker, message)
    def d(marker: Marker, message: String, cause: Throwable) = logger.debug(marker, message, cause)
    def t(message: String) = logger.trace(message)
    def t(message: String, cause: Throwable) = logger.trace(message, cause)
    def t(marker: Marker, message: String) = logger.trace(marker, message)
    def t(marker: Marker, message: String, cause: Throwable) = logger.trace(marker, message, cause)
  }
}
