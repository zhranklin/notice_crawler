package com.zhranklin.notice.service

import org.json4s._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object NoticeServiceObjects {

  trait ServiceBase extends IndexService with FunNoticeFetcher with SelectorUrlService {
    val initVal: ((Document) ⇒ String, (Document) ⇒ String, String, String)
    lazy val (getContent, getDateStr, urlPattern, template) = initVal
  }

  class LawService(title: String, listId: String) extends NoticeService(s"法学院 - $title") with UrlService with IndexService with FunNoticeFetcher {
    val getContent = contentF("div.text")
    val getDateStr = dateF("span:contains(发布时间)")
    val template = "http://law.scu.edu.cn/xjax?arg=8573&arg=<index>&arg=20&arg=list&clazz=PortalArticleAction&method=list"

    def getUrl(id: String) = s"http://law.scu.edu.cn/detail.jsp?portalId=725&cid=8385&nextcid=$listId&aid=$id"

    override def noticeUrlsFromUrl(url: String): Iterable[NoticeEntry] = {
      val jsonStr = Jsoup.connect(url).execute().body()
      val json = jackson.parseJson(jsonStr)
      json.\("data").asInstanceOf[JArray].arr.map(
        jo ⇒ NoticeEntry(getUrl(jo.\("id").values.toString), Some(jo.\("subject").values.toString)))
    }
  }

  val serviceList = List(
    "文新学院 - 学院动态" →
      "http://www.sculj.cn/Special_News.asp?SpecialID=40&SpecialName=%D1%A7%D4%BA%B6%AF%CC%AC&page=<index>",
    "经济学院 - 学院新闻" → "http://sesu.scu.edu.cn/news/list_1_<index>.html",
    "经济学院 - 学院公告" → "http://sesu.scu.edu.cn/gonggao/list_2_<index>.html",
    "计算机学院 - 学术看板" → "http://cs.scu.edu.cn/cs/xsky/xskb/H951901index_<index>.htm",
    "计算机学院 - 学院通知" → "http://cs.scu.edu.cn/cs/xytz/H9502index_<index>.htm",
    "计算机学院 - 学院新闻" → "http://cs.scu.edu.cn/cs/xyxw/H9501index_<index>.htm",
    "计算机学院 - 访谈录" → "http://cs.scu.edu.cn/cs/fwzy/ftl/H951204index_<index>.htm",
    "川大在线" → "http://news.scu.edu.cn/news2012/cdzx/I0201index_<index>.htm",
    "数学学院 - 学院新闻" →"http://math.scu.edu.cn/news.asp?PAGE=<index>",
    "电气信息学院 - 学院通知" → "http://seei.scu.edu.cn/student,p<index>,index.jsp",
    "外国语学院 - 学院公告" → "http://flc2.scu.edu.cn/foreign/a/xueyuangonggao/list_27_<index>.html"
  ).map { tp ⇒
    new NoticeService(tp._1) with UniversalUrlService with UniversalNoticeFetcher with IndexService {
      val template = tp._2
    }
  } ++ List(
    new NoticeService("教务处 - 通知") with ServiceBase {
      val initVal =(selectorF("input[name=news.content]")(_.first.attr("value")), dateF("table[width=900] td:contains(发布时间)"),
        "newsShow.*", "http://jwc.scu.edu.cn/jwc/moreNotice.action?url=moreNotice.action&type=2&keyWord=&pager.pageNow=<index>")},
    new LawService("学院新闻", "8572"),
    new LawService("学院公告", "8573")
  )
}