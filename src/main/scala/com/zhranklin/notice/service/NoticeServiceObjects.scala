package com.zhranklin.notice.service

import org.json4s._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object NoticeServiceObjects {

  trait ServiceBase extends IndexService with FunNoticeFetcher with SelectorUrlService {
    val initVal: ((Document) ⇒ String, (Document) ⇒ String, String, String)
    lazy val (getContent, getDateStr, urlPattern, template) = initVal
  }

  class LawService(title: String, listId: String) extends NoticeService(s"法学院 - $title") with FunNoticeFetcher {
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
    "文新学院 - 学院动态" → "http://www.sculj.cn/Special_News.asp?SpecialID=40&SpecialName=%D1%A7%D4%BA%B6%AF%CC%AC&page=<index>",
    "经济学院 - 学院新闻" → "http://sesu.scu.edu.cn/news/list_1_<index>.html",
    "经济学院 - 学院公告" → "http://sesu.scu.edu.cn/gonggao/list_2_<index>.html",
    "计算机学院 - 学术看板" → "http://cs.scu.edu.cn/cs/xsky/xskb/H951901index_<index>.htm",
    "计算机学院 - 学院通知" → "http://cs.scu.edu.cn/cs/xytz/H9502index_<index>.htm",
    "计算机学院 - 学院新闻" → "http://cs.scu.edu.cn/cs/xyxw/H9501index_<index>.htm",
    "计算机学院 - 访谈录" → "http://cs.scu.edu.cn/cs/fwzy/ftl/H951204index_<index>.htm",
    "川大在线" → "http://news.scu.edu.cn/news2012/cdzx/I0201index_<index>.htm",
    "数学学院 - 学院新闻" →"http://math.scu.edu.cn/news.asp?PAGE=<index>",
    "电气信息学院 - 学院通知" → "http://seei.scu.edu.cn/student,p<index>,index.jsp",
    "外国语学院 - 学院公告" → "http://flc2.scu.edu.cn/foreign/a/xueyuangonggao/list_27_<index>.html",
    "建筑与环境学院 - 通知公告" → "http://acem.scu.edu.cn/news.php?cid=88&page=<index>",
    "建筑与环境学院 - 学院新闻" → "http://acem.scu.edu.cn/news.php?cid=87&page=<index>",
    "建筑与环境学院 - 招生就业" → "http://acem.scu.edu.cn/news.php?cid=90&page=<index>",
    "建筑与环境学院 - 学术动态" → "http://acem.scu.edu.cn/news.php?cid=89&page=<index>",
    "水利水电学院 - 学院新闻" → "http://cwrh.scu.edu.cn/news.aspx?mid=18&page=<index>",
    "水利水电学院 - 学院通知" → "http://cwrh.scu.edu.cn/news.aspx?mid=102&page=<index>",
    "化学工程学院 - 新闻动态" → "http://ce.scu.edu.cn/info/news/?p=<index>",
    "化学工程学院 - 通知公告" → "http://ce.scu.edu.cn/info/notice/?p=<index>",
    "化学工程学院 - 学术动态" → "http://ce.scu.edu.cn/info/academic/?p=<index>",
    "轻纺与食品学院 - 学院新闻" → "http://qfsp.scu.edu.cn/index.aspx?menuid=25&type=article&lanmuid=70&page=<index>&language=cn",
    "轻纺与食品学院 - 信息通知" → "http://qfsp.scu.edu.cn/index.aspx?menuid=25&type=article&lanmuid=71&page=<index>&language=cn",
    "轻纺与食品学院 - 学生工作" → "http://qfsp.scu.edu.cn/index.aspx?menuid=25&type=article&lanmuid=72&page=<index>&language=cn",
    "轻纺与食品学院 - 学术看板" → "http://qfsp.scu.edu.cn/index.aspx?menuid=25&type=article&lanmuid=73&page=<index>&language=cn",
    "轻纺与食品学院 - 招生就业" → "http://qfsp.scu.edu.cn/index.aspx?menuid=25&type=article&lanmuid=74&page=<index>&language=cn",
    "轻纺与食品学院 - 工会之窗" → "http://qfsp.scu.edu.cn/index.aspx?menuid=25&type=article&lanmuid=84&page=<index>&language=cn",
    "高分子科学与工程学院 - 综合新闻" → "http://cpse.scu.edu.cn/news/news-list.php?id=2&type_id=1&page=<index>",
    "高分子科学与工程学院 - 通知公告" → "http://cpse.scu.edu.cn/news/news-list.php?id=40&type_id=1&page=<index>",
    "高分子科学与工程学院 - 教学科研" → "http://cpse.scu.edu.cn/news/news-list.php?id=188&type_id=1&page=<index>",
    "高分子科学与工程学院 - 学工动态" → "http://cpse.scu.edu.cn/news/news-list.php?id=169&type_id=168&page=<index>",
    "华西基础医学与法医学院 - 学院新闻（无index）" → "http://jcfy.scu.edu.cn/NewsSubClass.aspx?NewsSubClassId=1",
    "华西基础医学与法医学院 - 学院通知（无index）" → "http://jcfy.scu.edu.cn/NewsSubClass.aspx?NewsSubClassId=2",
    "华西公共卫生学院(华西第四医院)-新闻" → "http://www.hxsiyuan.cn/news/list.php?page=<index>&cid=15",
    "华西公共卫生学院(华西第四医院)-通知" → "http://www.hxsiyuan.cn/news/list.php?page=<index>&cid=16",
    "华西药学院 - 学院新闻" → "http://pharmacy.scu.edu.cn/newslist.aspx?id=14&page=<index>",
    "华西药学院 - 公示公告" → "http://pharmacy.scu.edu.cn/newslist.aspx?id=13&page=<index>",
    "公共管理学院 - 新闻动态" → "http://ggglxy.scu.edu.cn/index.php?c=special&sid=1&page=<index>",
    "公共管理学院 - 通知公告" → "http://ggglxy.scu.edu.cn/index.php?c=article&a=type&tid=76&page=<index>",
    "商学院 - 新闻" → "http://bs.scu.edu.cn/index.php?m=fcontent&a=index&catid=42&page=<index>",
    "商学院 - 通知" → "http://bs.scu.edu.cn/index.php?m=fcontent&a=index&catid=43&page=<index>",
    "马克思主义学院 - 通知公示" → "http://www.scu.edu.cn/mkszy/tz/I1409index_<index>.htm",
    "马克思主义学院 - 学院快讯" → "http://www.scu.edu.cn/mkszy/xykx/I1410index_<index>.htm",
    "灾后重建与管理学院 - 学院新闻" → "http://idmr.scu.edu.cn/xyxw/index_<index>.jhtml",
    "物理科学与技术学院（核科学与工程技术学院） 新闻" → "http://physics.scu.edu.cn/News/index.asp?ClassID=3&page=<index>",
    "物理科学与技术学院（核科学与工程技术学院）通知" → "http://physics.scu.edu.cn/News/index.asp?ClassID=1&page=<index>",
    "化学学院 - 新闻" → "http://chem.scu.edu.cn/test/news/listContent/%E7%BB%BC%E5%90%88%E6%96%B0%E9%97%BB?page=<index>",
    "化学学院 - 通知" → "http://chem.scu.edu.cn/test/News/ListContent/%E5%AD%A6%E9%99%A2%E9%80%9A%E7%9F%A5?page=<index>",
    "生命科学学院 - 新闻" → "http://life.scu.edu.cn/webList.asp?type=news&page=<index>",
    "生命科学学院 - 通知" → "http://life.scu.edu.cn/webList.asp?type=notice&page=<index>",
    "电子信息学院 - 新闻" → "http://eie.scu.edu.cn/AllNewsList.aspx?tid=1&page=<index>",
    "电子信息学院 - 通知" → "http://eie.scu.edu.cn/AllNewsList.aspx?tid=2&page=<index>",
    "制造科学与工程 - 新闻" → "http://msec.scu.edu.cn/list-113-<index>.html",
    "制造科学与工程 - 公告" → "http://msec.scu.edu.cn/list-114-<index>.html"
  ).map { tp ⇒
    new NoticeService(tp._1) with UniversalUrlService with UniversalNoticeFetcher with IndexService {
      val template = tp._2
    }
  } ++ List(
    new NoticeService("旅游学院 - 通知") with UniversalUrlService with UniversalNoticeFetcher {
      val template = "http://historytourism.scu.edu.cn/tourism/news/gonggaotongzhi/index_<index>.html"
      override val first = template.replaceAll("_<index>", "")
    },
    new NoticeService("旅游学院 - 新闻") with UniversalUrlService with UniversalNoticeFetcher {
      val template = "http://historytourism.scu.edu.cn/tourism/news/xueyuandongtai/index_<index>.html"
      override val first = template.replaceAll("_<index>", "")
    },
    new NoticeService("教务处 - 通知") with ServiceBase {
      val initVal =(selectorF("input[name=news.content]")(_.first.attr("value")), dateF("table[width=900] td:contains(发布时间)"),
        "newsShow.*", "http://jwc.scu.edu.cn/jwc/moreNotice.action?url=moreNotice.action&type=2&keyWord=&pager.pageNow=<index>")},
    new LawService("学院新闻", "8572"),
    new LawService("学院公告", "8573")
  )
}