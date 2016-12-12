# 第五周开发报告
## 本周所做工作

本周对上周总结出的一些爬取不了的网站做了单独的编码与整合, 经过调试, 到目前为止, 都能正常工作

以华西基础医学与法医学进行举例，以下是相关接口的代码:

```java
    interface UrlServiceTest extends UrlService {
      @Override
      default scala.collection.Iterable<NoticeEntry> noticeUrlsFromUrl(String url) {
      ArrayList<NoticeEntry> list = new ArrayList<NoticeEntry>();
      String html = HttpUtil.sendGet("http://jcfy.scu.edu.cn/NewsSubClass.aspx?NewsSubClassId=1", "utf-8");
      System.out.println(html);
        Iterator<NoticeEntry> noticeEntries = new Iterator<NoticeEntry>() {

        String starturl = "http://jcfy.scu.edu.cn/NewsSubClass.aspx?NewsSubClassId=1";
        String indexurl = "http://jcfy.scu.edu.cn/";
        Document soup = Jsoup.parse(HttpUtil.sendGet(starturl, "utf-8"));
        int page = 1;
        int now = 1;

        @Override
        public boolean hasNext() {
          if (soup.select("#aspnetForm > div.content > div.contain > div.listr.right > ul > li:nth-child("
              + (now + 1) + ") > a").size()==0 || canGoToPage(page)) {
            return true;
          }
          return false;
        }

        @Override
        public NoticeEntry next() {
          NoticeEntry notice = null;
          Element newNotice; 
          Elements tmp = soup.select("#aspnetForm > div.content > div.contain > div.listr.right > ul > li:nth-child("+ (now + 1) + ") > a");
            
          if (tmp.size()!=0) {
            newNotice = tmp.get(0);
            notice = new NoticeEntry(indexurl + newNotice.attr("href"),
                scala.Option.apply(newNotice.attr("title")));
          } else {
            goToPage(++page);
            now = 1;
            tmp= soup.select("#aspnetForm > div.content > div.contain > div.listr.right > ul > li:nth-child("
              + (now + 1) + ") > a");
            if (tmp.size()!=0) {
              newNotice  = tmp.get(0);
              notice = new NoticeEntry(indexurl + newNotice.attr("href"),
                  scala.Option.apply(newNotice.attr("title")));

            }
          }
          now +=1;
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
          map.put("__EVENTARGUMENT", "" + (page + 1));
          map.put("__VIEWSTATE", soup.select("#__VIEWSTATE").get(0).attr("value"));
          soup = Jsoup.parse(forPost.sendPost(starturl, "utf-8", map));

        }
      };
        return scala.collection.JavaConverters.iterableAsScalaIterable(noticeEntries);
      }
    }
```

## 说明
在以上代码中，我们实现了相关的接口的默认方法，在接口中，实现了一个iterator,该iterator能判断是否由下一条记录并且可以通过调用next得到下一条记录。  
通过实现next()，我们可以从next()得到对应的url以及标题，供我们下一级爬虫进行使用。
代码中用到了分别get和post方法，应用于这类网站第一次需要get 之后需要使用post的现象。  
由于该类网站对反爬虫有设防，故我们ip经常被封，这影响了我们进度。  
像类似于该网站，我们对其他网站也进行了爬取，初步完成了当前的目标。
