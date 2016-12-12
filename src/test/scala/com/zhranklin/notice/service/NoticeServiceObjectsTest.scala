package com.zhranklin.notice.service

import java.util.Date

import org.scalatest._
import org.jsoup.Jsoup

import scala.util.Failure

@Ignore
class NoticeServiceObjectsTest extends FlatSpec with Matchers with GivenWhenThen {

  behavior of "NoticeServiceObjectsTest"

  it should "contain a good serviceList" in {
    val nots = List("水利水电", "高分子", "华西", "化学工程", "灾后")
    val list = NoticeServiceObjects.serviceList.filterNot(service ⇒ nots.exists(service.source.contains))
    list.foreach { service ⇒
      Given(s"a service from the source ${service.source}")
      When("fetching news from it")
      val newsTry = service.notices.take(1).toList.head
      Then("it should return a right news")
      assert(newsTry.isSuccess, s"this news from ${service.source} is invalid for $newsTry")
      val news = newsTry.get
      val text = Jsoup.parse(news.html).body().text()
      info(s"news info: ${news.title}/${news.date} - ${text.take(50)}...")
      if (!(news.html contains "<img") && text.length <= 70) {
        alert(s"news is too short from ${service.source}: $text")
        info(s"the html is: ${news.html}")
      }
      if (news.title.length <= 5) {
        alert(s"title is too short from ${service.source}: ${news.title}")
      }
    }
  }

}
