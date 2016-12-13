package util;

import java.util.ArrayList;
import java.util.Iterator;

import javax.management.NotificationEmitter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zhranklin.notice.service.NoticeEntry;
import com.zhranklin.notice.service.UrlService;

import scala.Option;
import scala.collection.immutable.HashMap;

interface jcfytzUrlService extends UrlService {
	@Override
	default scala.collection.Iterable<NoticeEntry> noticeUrlsFromUrl(String url) {
		ArrayList<NoticeEntry> list = new ArrayList<NoticeEntry>();
		Iterator<NoticeEntry> noticeEntries = new Iterator<NoticeEntry>() {

			String starturl = "http://jcfy.scu.edu.cn/NewsSubClass.aspx?NewsSubClassId=1";
			String indexurl = "http://jcfy.scu.edu.cn/";
			Document soup = Jsoup.parse(HttpUtil.sendGet(starturl, "utf-8"));
			int page = 1;
			int now = 0;

			@Override
			public boolean hasNext() {
				if (soup.select("#aspnetForm > div.content > div.contain > div.listr.right > ul > li:nth-child("
						+ (now + 1) + ") > a").size() != 0 || canGoToPage(page)) {
					return true;
				}
				return false;
			}

			@Override
			// public NoticeEntry next() {
			// NoticeEntry notice = null;
			public NoticeEntry next() {
				NoticeEntry notice = null;
				Element newNotice;
				Elements tmp = soup
						.select("#aspnetForm > div.content > div.contain > div.listr.right > ul > li:nth-child("
								+ (now + 1) + ") > a");

				if (tmp.size() != 0) {
					newNotice = tmp.get(0);
					notice = new NoticeEntry(indexurl + newNotice.attr("href"),
							scala.Option.apply(newNotice.attr("title")));
				} else {
					goToPage(++page);
					now = 1;
					tmp = soup.select("#aspnetForm > div.content > div.contain > div.listr.right > ul > li:nth-child("
							+ (now) + ") > a");
					if (tmp.size() != 0) {
						newNotice = tmp.get(0);
						notice = new NoticeEntry(indexurl + newNotice.attr("href"),
								scala.Option.apply(newNotice.attr("title")));

					}
				}
				now += 1;
				return notice;
			}

			public Boolean canGoToPage(int page) {
				if (soup.select("#ctl00_MainContent_pagerNewsList > a:nth-child(10)").get(0).attr("disabled")
						.equals("disabled"))
					return false;
				else
					return true;
			}

			public void goToPage(int page) {
				HashMap<String, String> map = new HashMap<>();
				map.put("__EVENTTARGET", "ctl00$MainContent$pagerNewsList");
				map.put("__EVENTARGUMENT", "" + page );
				map.put("__VIEWSTATE", soup.select("#__VIEWSTATE").get(0).attr("value"));
				soup = Jsoup.parse(forPost.sendPost(starturl, "utf-8", map));

			}
		};
		return scala.collection.JavaConverters.iterableAsScalaIterable(noticeEntries);
	}
}

interface jcfyxwUrlService extends UrlService {
	@Override
	default scala.collection.Iterable<NoticeEntry> noticeUrlsFromUrl(String url) {
		ArrayList<NoticeEntry> list = new ArrayList<NoticeEntry>();
		Iterator<NoticeEntry> noticeEntries = new Iterator<NoticeEntry>() {

			String starturl = "http://jcfy.scu.edu.cn/NewsSubClass.aspx?NewsSubClassId=2";
			String indexurl = "http://jcfy.scu.edu.cn/";
			Document soup = Jsoup.parse(HttpUtil.sendGet(starturl, "utf-8"));
			int page = 1;
			int now = 0;

			@Override
			public boolean hasNext() {
				if (soup.select("#aspnetForm > div.content > div.contain > div.listr.right > ul > li:nth-child("
						+ (now + 1) + ") > a").size() != 0 || canGoToPage(page)) {
					return true;
				}
				return false;
			}

			@Override
			// public NoticeEntry next() {
			// NoticeEntry notice = null;
			public String next() {
				NoticeEntry notice = null;
				Element newNotice;
				Elements tmp = soup
						.select("#aspnetForm > div.content > div.contain > div.listr.right > ul > li:nth-child("
								+ (now + 1) + ") > a");

				if (tmp.size() != 0) {
					newNotice = tmp.get(0);
					// notice = new NoticeEntry(indexurl +
					// newNotice.attr("href"),
					// scala.Option.apply(newNotice.attr("title")));
					notice = new NoticeEntry(indexurl + newNotice.attr("href"),
							scala.Option.apply(newNotice.attr("title")));
				} else {
					goToPage(++page);
					now = 1;
					tmp = soup.select("#aspnetForm > div.content > div.contain > div.listr.right > ul > li:nth-child("
							+ (now) + ") > a");
					if (tmp.size() != 0) {
						newNotice = tmp.get(0);
						// notice = new NoticeEntry(indexurl +
						// newNotice.attr("href"),
						// scala.Option.apply(newNotice.attr("title")));
						notice = new NoticeEntry(indexurl + newNotice.attr("href"),
								scala.Option.apply(newNotice.attr("title")));

					}
				}
				now += 1;
				return notice;
			}

			public Boolean canGoToPage(int page) {
				if (soup.select("#ctl00_MainContent_pagerNewsList > a:nth-child(8)").get(0).attr("disabled")
						.equals("disabled"))
					return false;
				else
					return true;
			}

			public void goToPage(int page) {
				HashMap<String, String> map = new HashMap<>();
				map.put("__EVENTTARGET", "ctl00$MainContent$pagerNewsList");
				map.put("__EVENTARGUMENT", ""+page);
				map.put("__VIEWSTATE", soup.select("#__VIEWSTATE").get(0).attr("value"));
				soup = Jsoup.parse(forPost.sendPost(starturl, "utf-8", map));

			}
		};
		return scala.collection.JavaConverters.iterableAsScalaIterable(noticeEntries);
	}
}

interface saaxwUrlService extends UrlService {
	@Override
	default scala.collection.Iterable<NoticeEntry> noticeUrlsFromUrl(String url) {
		ArrayList<NoticeEntry> list = new ArrayList<NoticeEntry>();
		Iterator<NoticeEntry> noticeEntries = new Iterator<NoticeEntry>() {

			String starturl = "http://saa.scu.edu.cn/xueyuangk/News.aspx";
			String indexurl = "http://saa.scu.edu.cn/";
			Document soup = Jsoup.parse(HttpUtil.sendGet(starturl, "utf-8"));
			int page = 1;
			int now = 0;

			public boolean hasNext() {
				if (soup.select("#DataList1 > tbody > tr:nth-child(" + (now + 1)
						+ ") > td > table > tbody > tr > td:nth-child(1) > a").size() != 0) {
					return true;
				}
				return false;
			}

			public String next() {
				NoticeEntry notice = null;
				Element newNotice;
				Elements tmp = soup.select("#DataList1 > tbody > tr:nth-child(" + (now + 1)
						+ ") > td > table > tbody > tr > td:nth-child(1) > a");

				if (tmp.size() != 0) {
					newNotice = tmp.get(0);
					notice = new NoticeEntry(indexurl + newNotice.attr("href"), newNotice.select("span").get(0).text());

				}
				now += 1;
				return notice;
			}

		};
		return scala.collection.JavaConverters.iterableAsScalaIterable(noticeEntries);
	}
}

interface saatzUrlService extends UrlService {
	@Override
	default scala.collection.Iterable<NoticeEntry> noticeUrlsFromUrl(String url) {
		ArrayList<NoticeEntry> list = new ArrayList<NoticeEntry>();
		Iterator<NoticeEntry> noticeEntries = new Iterator<NoticeEntry>() {

			String starturl = "http://saa.scu.edu.cn/tongzhigg/tongzhigg.aspx";
			String indexurl = "http://saa.scu.edu.cn/";
			Document soup = Jsoup.parse(HttpUtil.sendGet(starturl, "utf-8"));
			int page = 1;
			int now = 0;

			public boolean hasNext() {
				if (soup.select("#DataList1 > tbody > tr:nth-child(" + (now + 1)
						+ ") > td > table > tbody > tr > td:nth-child(1) > a").size() != 0) {
					return true;
				}
				return false;
			}

			public String next() {
				NoticeEntry notice = null;
				Element newNotice;
				Elements tmp = soup.select("#DataList1 > tbody > tr:nth-child(" + (now + 1)
						+ ") > td > table > tbody > tr > td:nth-child(1) > a");

				if (tmp.size() != 0) {
					newNotice = tmp.get(0);
					notice = new NoticeEntry(indexurl + newNotice.attr("href"), newNotice.select("span").get(0).text());

				}
				now += 1;
				return notice;
			}

		};
		return scala.collection.JavaConverters.iterableAsScalaIterable(noticeEntries);
	}
}
interface msexwUrlService extends UrlService {
	  @Override
	  default scala.collection.Iterable<NoticeEntry> noticeUrlsFromUrl(String url) {
		ArrayList<NoticeEntry> list = new ArrayList<NoticeEntry>();
	    Iterator<NoticeEntry> noticeEntries = new Iterator<NoticeEntry>() {

			String starturl = "http://mse.scu.edu.cn/list.aspx?t=71";
			String indexurl = "http://mse.scu.edu.cn/";
			Document soup = Jsoup.parse(HttpUtil.sendGet(starturl, "utf-8"));
			int page = 1;
			int now = 0;

			@Override
			public boolean hasNext() {
				if (soup.select("#allbody > div.mbox > div.box1 > div.xwbox > ul:nth-child("+(now+1)+") > li.xw1 > a") 
						.size()!=0 || canGoToPage(page)) {
					return true;
				}
				return false;
			}

			@Override
			//public NoticeEntry next() {
			//	NoticeEntry notice = null;
			public NoticeEntry next() {
				NoticeEntry notice = null;
				Element newNotice; 
				Elements tmp = soup.select("#allbody > div.mbox > div.box1 > div.xwbox > ul:nth-child("+(now+1)+") > li.xw1 > a");
					
				if (tmp.size()!=0) {
					newNotice = tmp.get(0);
					notice = new NoticeEntry(indexurl + newNotice.attr("href"),
							scala.Option.apply(newNotice.text()));
				} else {
					goToPage(++page);
					now = 1;
					tmp= soup.select("#allbody > div.mbox > div.box1 > div.xwbox > ul:nth-child("+(now+1)+") > li.xw1 > a");
					if (tmp.size()!=0) {
						newNotice  = tmp.get(0);
						notice = new NoticeEntry(indexurl + newNotice.attr("href"),
								scala.Option.apply(newNotice.text()));

					}
				}
				now +=1;
				return notice;
			}

			public Boolean canGoToPage(int page) {
				if (soup.select("#AspNetPager1 > div:nth-child(2) > a:nth-child(11)").get(0).attr("disabled")
						.equals("disabled"))
					return false;
				else
					return true;
			}

			public void goToPage(int page) {
				HashMap<String, String> map = new HashMap<>();
				map.put("__EVENTTARGET", "AspNetPager1");
				map.put("__EVENTARGUMENT", "" + page) ;
				map.put("__VIEWSTATE", soup.select("#__VIEWSTATE").get(0).attr("value"));
				soup = Jsoup.parse(forPost.sendPost(starturl, "utf-8", map));

			}
		};
	    return scala.collection.JavaConverters.iterableAsScalaIterable(noticeEntries);
	  }
	}
interface msetzUrlService extends UrlService {
	  @Override
	  default scala.collection.Iterable<NoticeEntry> noticeUrlsFromUrl(String url) {
		ArrayList<NoticeEntry> list = new ArrayList<NoticeEntry>();
	    Iterator<NoticeEntry> noticeEntries = new Iterator<NoticeEntry>() {

			String starturl = "http://mse.scu.edu.cn/list.aspx?t=71";
			String indexurl = "http://mse.scu.edu.cn/";
			Document soup = Jsoup.parse(HttpUtil.sendGet(starturl, "utf-8"));
			int page = 1;
			int now = 0;

			@Override
			public boolean hasNext() {
				if (soup.select("#allbody > div.mbox > div.box1 > div.xwbox > ul:nth-child("+(now+1)+") > li.xw1 > a") 
						.size()!=0 || canGoToPage(page)) {
					return true;
				}
				return false;
			}

			@Override
			//public NoticeEntry next() {
			//	NoticeEntry notice = null;
			public NoticeEntry next() {
				NoticeEntry notice = null;
				Element newNotice; 
				Elements tmp = soup.select("#allbody > div.mbox > div.box1 > div.xwbox > ul:nth-child("+(now+1)+") > li.xw1 > a");
					
				if (tmp.size()!=0) {
					newNotice = tmp.get(0);
					notice = new NoticeEntry(indexurl + newNotice.attr("href"),
							scala.Option.apply(newNotice.text()));
				} else {
					goToPage(++page);
					now = 1;
					tmp= soup.select("#allbody > div.mbox > div.box1 > div.xwbox > ul:nth-child("+(now+1)+") > li.xw1 > a");
					if (tmp.size()!=0) {
						newNotice  = tmp.get(0);
						notice = new NoticeEntry(indexurl + newNotice.attr("href"),
								scala.Option.apply(newNotice.text()));

					}
				}
				now +=1;
				return notice;
			}

			public Boolean canGoToPage(int page) {
				if (soup.select("#AspNetPager1 > div:nth-child(2) > a:nth-child(11)").get(0).attr("disabled")
						.equals("disabled"))
					return false;
				else
					return true;
			}

			public void goToPage(int page) {
				HashMap<String, String> map = new HashMap<>();
				map.put("__EVENTTARGET", "AspNetPager1");
				map.put("__EVENTARGUMENT", "" + page);
				map.put("__VIEWSTATE", soup.select("#__VIEWSTATE").get(0).attr("value"));
				soup = Jsoup.parse(forPost.sendPost(starturl, "utf-8", map));

			}
		};
	    return scala.collection.JavaConverters.iterableAsScalaIterable(noticeEntries);
	  }
	}

