package com.zhranklin.notice.service

import com.zhranklin.notice.{JsoupUtil, Logging}
import com.zhranklin.notice.Util._
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

trait UrlService extends Logging {
  def noticeUrlsFromUrl(url: String): Iterable[NoticeEntry]
}

trait AbstractUrlService extends UrlService with JsoupUtil {
  type E
  protected def rawUrlsFromDoc(doc: Document): Iterable[E]
  protected def extractFromRawUrl(a: E): NoticeEntry
  def noticeUrlsFromUrl(url: String): Iterable[NoticeEntry] = {
    val doc = getPage(url)
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

  case class RichUrl(url: String, absUrl: String, title: String)
  val filterNot = (_: RichUrl).url.endsWith("/")

  def noticeUrlsFromUrl(indexUrl: String) = {
    log.t(s"index: $indexUrl")
    def getPostFix(url: String) = """(?<=\.)\w+$""".r.findFirstIn(url).getOrElse("") //获取文件后缀名
    def properGroup(urls: Seq[RichUrl], pre: Int) = {
      val counts = groupByPreFix(urls, pre).map(_._2.size) //counts就是经过分组之后每组的个数

      //如果当前的分组方式最终被选中, 那么链接数大于5的组所包含的链接, 都会作为最后选出的链接
      //下面的公式表示链接个数大于5的组的个数 - (总url数 - 链接最多的组的链接数数)/18
      //这个数值越小, 表示链接聚集程度越低, 当小于等于0的时候, 就被认为这个分组方式过于分散, pre过大
      counts.count(_ > 5) - (urls.size - counts.max) / 18
    }

    //从1到200迭代(作为pre), 直到properGroup的结果不大于0(properGroup显然是越来越小的), 去前一次的结果(也就是大于0且最小)
    def tryGroup(urls: Seq[RichUrl]) = 1 to 200 takeWhile {properGroup(urls, _) > 0} last

    /*
    example:
      urls: List("aaa.a", "aab.a", "aac.a", "aaaa.a", "aaaa.b", "aac.a", "aac.b")
      prefix = 2:
        ((aa,b),List(aaaa.b, aac.b))
        ((aa,a),List(aaa.a, aab.a, aac.a, aaaa.a, aac.a))
      prefix = 3:
        ((aa,b),List(aaaa.b, aac.b))
        ((aa,a),List(aaa.a, aab.a, aac.a, aaaa.a, aac.a))
      prefix = 4:
        ((aaaa,a),List(aaaa.a))
        ((aaa.,a),List(aaa.a))
        ((aaaa,b),List(aaaa.b))
        ((aac.,a),List(aac.a, aac.a))
        ((aac.,b),List(aac.b))
        ((aab.,a),List(aab.a))
     */
    def groupByPreFix(urls: Seq[RichUrl], pre: Int) = urls.groupBy(e ⇒ (e.url.take(pre), getPostFix(e.url)))

    //一组url的平均标题长度是否大于7
    def longEnough(urls: Seq[RichUrl]) = urls.map(_.title.length).sum / urls.size.asInstanceOf[Double] > 7

    //process body
    val doc = getPage(indexUrl) //获取页面DOM
    doc.body.select("a[href]").asScala.map(a ⇒ a.html(a.text)) //将所有链接的内容换成纯文本
    doc.body select "*:not(:has(a[href]))" select "*:not(a[href])" remove //将内部不包含链接的, 且本身不是链接的节点删除
    //至此, 文档的所有叶子节点都是<a>标签, 且所有的<a>标签都是叶子节点
    val urls = doc.select("*:last-of-type:nth-of-type(n+5)").asScala //筛选, 条件为 是兄弟节点中的最后一个, 且为兄弟节点中的第n+5个(n>0)
      .flatMap(_.parent.select("a[href]").asScala) //扩展成刚才所选节点的父节点内, 所包含的所有<a>, 如此就剔除了所有零散的链接
      .map(e ⇒ RichUrl(e.href, e.absHref, e.text)) //转换成RichUrl, e.text就是链接的文本, 也就是标题
      .filterNot(filterNot) //去掉所有'/'结尾的链接
      .map(u ⇒ (u.url, u)) //转换成(url(String), RichUrl)的元组
      .toMap.values.toList //toMap(scala中的约定: 二元组作为键值对可转换成Map)之后就可以去掉重复的url, 以及url相同, 标题不同的<a>
    log.t(s"candidates: ${urls.map(_.absUrl).mkString("\n")}")

    //先经过第一次tryGroup, 获得一次最优化结果分组, 然后只保留链接平均标题长度大于7的组, 取这些组的链接作为结果
    val urlsLongEnough = groupByPreFix(urls, tryGroup(urls)).values.filter(longEnough).flatten.toSeq

    //再次尝试选出Group
    val preLen = tryGroup(urlsLongEnough)

    //大致意思就是, 比如选出的前缀长度为6, 结果很多链接都是id=7位数字(例: id=1234567)
    //那么此时作为分组依据的前缀就是id=123, 这显然不合理, 应该取id=作为前缀, 也就是说, preLen应该为3(也就是shrunkenPreLen)
    val shrunkenPreLen = "^.*?(?=\\d+$)".r.findFirstIn(urlsLongEnough.head.url.take(preLen)).map(_.length).getOrElse(preLen)
    val ret = groupByPreFix(urlsLongEnough, shrunkenPreLen).values //使用shrunkenPreLen作为前缀的位数, 进行分组
      .filter(_.size > 5).flatten.map(u ⇒ NoticeEntry(u.absUrl, Some(u.title)))
    log.t(s"ret: $ret")
    ret
  }
}
