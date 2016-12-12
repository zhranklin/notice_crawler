package com.zhranklin.notice.service

object InitVals {
  val initVals = Map(
    "计算机学院 - 学院新闻" → ("div#BodyLabel", "td[width=716]:contains(来源)", "/cs/xytz/webinfo/\\d{4}/\\d{2}/\\d{8,}\\.htm", "http://cs.scu.edu.cn/cs/xytz/H9502index_<index>.htm"),
    "新闻来源" → ("内容抓取selector", "日期抓取selector", "新闻url正则表达式", "目录页url"),
    "新闻来源" → ("内容抓取selector", "日期抓取selector", "新闻url正则表达式", "目录页url"),
    "新闻来源" → ("内容抓取selector", "日期抓取selector", "新闻url正则表达式", "目录页url")
  )
}
