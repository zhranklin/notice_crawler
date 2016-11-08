# 第一周进度报告
## 本周已完成

- 网站的调查工作已经完成
- 与服务器组的接口协商有了初步进展
- 已经完成了原有代码的迁移(见:[src文件夹](../src)), 初具规模的完成了脚本工具

## 脚本工具建模

为方便使用, 脚本拟写成命令行交互的模式, 调用时, 使用管道即可。
输入的每一行为一个JSON对象, 代表一个命令,
在程序处理完毕后, 会输出一个JSON对象, 里面包含了执行结果。

### 输入

输入的JSON对象满足以下格式:

```json
{
  "type" : "some_type",
  "attr1": "value1",
  "attr2": "value2"
}
```

### 输出

输出满足以下格式;

```json
{
  "type" : "succ",
  "result" : {
    "attr1": "value1",
    "attr2": "value2"
  }
}
```

### 已实现指令

目前有两种指令可以使用:

### listsources

仅指明tupe属性为listsources即可

```json
{
  "type" : "listsources"
}
```

输出:

```json
{
  "type" : "succ",
  "result" : {
    "sources" : [ "文新学院 - 学院动态", "经济学院 - 学院新闻", "经济学院 - 学院公告", "计算机学院 - 学术看板", "计算机学院 - 学院通知", "计算机学院 - 学院新闻", "计算机学院 - 访谈录", "川大在线", "数学学院 - 学院新闻", "电气信息学院 - 学院通知", "外国语学院 - 学院公告", "教务处 - 通知", "法学院 - 学院新闻", "法学院 - 学院公告" ]
  }
}
```

### getnews

可以指定新闻来源,
以及可选的limit(为方便测试, 默认为5), date(表示在该日期之后)等参数:

```json
{
  "type": "getnews",
  "source":"计算机学院 - 学院新闻"
}
```

返回的格式如下(标题显示还有问题, 日后修改):

```json
{
  "type" : "succ",
  "result" : {
    "news" : [ {
      "url" : "http://news.scu.edu.cn/news2012/cdzx/webinfo/2016/11/1478149586447358.htm",
      "title" : "四川大学第三届“卓越教学奖”、第二...",
      "html" : "<td valign=\"top\" width=\"719\" background=\"/news2012/lib/images/zenwen1105_14.jpg\" colspan=\"2\" height=\"828\" alt=\"\"><br>\n <table class=\"pcenter_t17\" cellspacing=\"0\" cellpadding=\"0\" width=\"90%\" align=\"center\" border=\"0\">\n  <tbody>\n   <tr>\n    <td class=\"hangjv\" id=\"zoom\" valign=\"top\" colspan=\"2\" td><br>　 \n     <div id=\"BodyLabel\">\n      <div align=\"center\">\n       <img alt=\"\" src=\"http://news.scu.edu.cn/news2012/rootimages/2016/11/08/1478149586447358-1478149586449190.jpg\" align=\"center\" border=\"0\">\n      </div>\n      <div align=\"left\">\n       &nbsp;\n      </div>\n      <div align=\"left\">\n       　　四川大学第三届“卓越教学奖”、第二届“星火校友奖教金”和第二届“五粮春青年教师优秀教学奖”全校师生网络投票活动正式开启，投票时间从2016年11月8日中午12:00起至11月15日中午12:00止。\n      </div>\n      <div align=\"left\">\n       &nbsp;\n      </div>\n      <div align=\"left\">\n       　　学校评审专家委员会在第二轮评选中共遴选出第三届“卓越教学奖”、第二届“星火校友奖教金”和第二届“五粮春青年教师优秀教学奖”61名候选人进入网络投票环节，其中：“卓越教学奖”、“星火校友奖教金”、“五粮春青年教师优秀教学奖”分别占15名、23名和23名。\n      </div>\n      <div align=\"left\">\n       &nbsp;\n      </div>\n      <div align=\"left\">\n       　　第三届“卓越教学奖”、第二届“星火校友奖教金”和第二届“五粮春青年教师优秀教学奖”第一轮评选活动结束后，学校主页开辟了专栏，同时与四川大学报、川大微博、川大微信等多个校园媒体平台聚合联动，对120位候选教师在本科教学、教学研究、教学改革、创新实践、学术成效等方面取得的优秀成果，以及他们在关爱学生、为人师表、爱岗敬业、无私奉献等方面的先进事迹进行集中宣传展示。\n      </div>\n      <div align=\"left\">\n       &nbsp;\n      </div>\n      <div align=\"left\">\n       　　学校评审专家委员深入学院、课堂，征求教师、学生和有关单位意见，对120名候选人进行深入考察，在通过听课、走访、调研和师生座谈等方式深入了解入围候选人相关情况的基础上召开了评审专家委员会第二次会议。专家们认真听取了各学科组近期深入学院、课堂征求师生及有关方面意见的详细情况，并通过现场交流和查阅资料，对120位入围候选人在教学研究、教学改革、创新实践、学术成就等方面成果，以及关爱学生、为人师表、爱岗敬业、无私奉献等方面事迹进行了全面认识和评价。最后通过分组讨论、大会评议、投票表决等环节，遴选出61名教师进入网络投票环节，接受全校师生的网上投票。\n      </div>\n      <div align=\"left\">\n       &nbsp;\n      </div>\n      <div align=\"left\">\n       　　欢迎全校师生登陆四川大学第三届“卓越教学奖”、第二届“星火校友奖教金”和第二届“五粮春青年教师优秀教学奖”投票网站（\n       <a href=\"http://et.scu.edu.cn/\">http://et.scu.edu.cn/</a>），踊跃参与全校网络票选活动，为自己心目中的优秀教师投上神圣的一票。\n      </div>\n      <div align=\"left\">\n       &nbsp;\n      </div>\n      <div align=\"left\">\n       　　投票时间：2016年11月8日中午12:00起至11月15日中午12:00止\n      </div>\n      <div align=\"left\">\n       &nbsp;\n      </div>\n      <div align=\"left\">\n       　　投票网址：\n       <a href=\"http://et.scu.edu.cn/\">http://et.scu.edu.cn/</a>\n      </div>\n      <div align=\"left\">\n       &nbsp;\n      </div>\n      <div align=\"left\">\n       　　投票规则：\n      </div>\n      <div align=\"left\">\n       &nbsp;\n      </div>\n      <div align=\"left\">\n       　　1. 全校师生投票须通过统一身份认证登录，即通过“一卡通”号登录。\n      </div>\n      <div align=\"left\">\n       &nbsp;\n      </div>\n      <div align=\"left\">\n       　　2. 每位投票人最多可投11位“卓越教学奖”候选人、16位“星火校友奖教金”候选人和15位“五粮春青年教师优秀教学奖”候选人，每人限投1次。\n      </div>\n      <div align=\"left\">\n       &nbsp;\n      </div>\n      <div align=\"left\">\n       　　监督邮箱：\n       <a href=\"mailto:jwc@scu.edu.cn\">jwc@scu.edu.cn</a>\n      </div>\n      <div id=\"网站群管理\"></div>\n     </div></td>\n   </tr>\n  </tbody>\n </table><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"http://e.weibo.com/u/3624694175\" target=\"_blank\"><img src=\"http://news.scu.edu.cn/news2012/rootimages/2014/04/01/1390026133621224.jpg\" border=\"0\"></a><img src=\"http://news.scu.edu.cn/news2012/rootimages/2014/06/27/1402363146991499.jpg\" border=\"0\"><img src=\"http://news.scu.edu.cn/news2012/1443489043291696.jpg\" border=\"0\"></p>\n <table cellspacing=\"0\" cellpadding=\"0\" width=\"90%\" align=\"center\" border=\"0\">\n  <tbody>\n   <tr>\n    <td valign=\"top\" align=\"right\"><br>【<a href=\"\"><font color=\"#808080\">大</font></a> <a href=\"\"><font color=\"#808080\">中</font></a> <a href=\"\"><font color=\"#808080\">小</font></a>】【<a onclick=\"javascript:window.print()\" href=\"http://news.scu.edu.cn/#\">打印本文</a>】【<a href=\"\">关闭窗口</a>】 <br></td>\n    <td valign=\"top\" align=\"right\">&nbsp;</td>\n   </tr>\n  </tbody>\n </table><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"http://e.weibo.com/u/3624694175\" target=\"_blank\"></a> <p>&nbsp;</p></td>",
      "date" : "2016-11-08T09:16:00Z"
    }, {
      "url" : "http://news.scu.edu.cn/news2012/cdzx/webinfo/2016/11/1478149586041276.htm",
      "title" : "德国慕尼黑工业大学交响乐团“秋之韵...",
      "html" : "<td valign=\"top\" width=\"719\" background=\"/news2012/lib/images/zenwen1105_14.jpg\" colspan=\"2\" height=\"828\" alt=\"\"><br>\n <table class=\"pcenter_t17\" cellspacing=\"0\" cellpadding=\"0\" width=\"90%\" align=\"center\" border=\"0\">\n  <tbody>\n   <tr>\n    <td class=\"hangjv\" id=\"zoom\" valign=\"top\" colspan=\"2\" td><br>　 \n     <div id=\"BodyLabel\">\n      <div align=\"center\">\n       <img border=\"1\" alt=\"\" src=\"http://news.scu.edu.cn/news2012/rootimages/2016/11/07/1478149586041276-1478149586045816.jpg\" align=\"center\">\n      </div>\n      <div>\n       &nbsp;\n      </div>\n      <div>\n       　　11月4日晚，德国慕尼黑工业大学交响乐团“秋之韵”音乐会在四川大学江安体育馆举行。四川大学党委副书记李向成教授以及学生工作部、国际合作与交流处、艺术学院等相关单位工作人员和千名师生一起观赏了这场来自慕尼黑的音乐盛宴。\n      </div>\n      <div>\n       &nbsp;\n      </div>\n      <p align=\"center\"><img border=\"1\" alt=\"\" src=\"http://news.scu.edu.cn/news2012/rootimages/2016/11/07/1478149586041276-1478149586046407.jpg\" align=\"center\"></p>\n      <div>\n       &nbsp;\n      </div>\n      <div>\n       　　慕尼黑工业大学交响乐团的34名师生代表为现场听众们带来了四首古典曲目：《巴赫：G小调组曲》、《加勒比海盗》、《贝多芬：第6号交响曲》（第一乐章）、《亨德尔：圣塞西莉亚日颂》，他们精湛的技艺和演奏赢得了观众们的阵阵掌声。随后，四川大学学生交响乐团为大家带来《沃尔塔瓦河》这一经典乐曲。最后，音乐会在《我爱你中国》的熟悉旋律中完美落幕。在本场音乐会中，东西方文化的碰撞和交流给川大师生带来了一个难忘的夜晚。\n      </div>\n      <div>\n       &nbsp;\n      </div>\n      <p align=\"center\"><img border=\"1\" alt=\"\" src=\"http://news.scu.edu.cn/news2012/rootimages/2016/11/07/1478149586041276-1478149586047150.jpg\" align=\"center\"></p>\n      <div>\n       &nbsp;\n      </div>\n      <div>\n       　　演出结束后，校党委副书记李向成教授向四川大学学生交响乐团演奏人员颁发了证书，向慕尼黑工业大学交响乐团团长颁发证书并赠送纪念礼物，代表了四川大学全体师生对慕尼黑工业大学交响乐团的欢迎，也象征着两校的友谊。\n      </div>\n      <div>\n       &nbsp;\n      </div>\n      <p align=\"center\"><img border=\"1\" alt=\"\" src=\"http://news.scu.edu.cn/news2012/rootimages/2016/11/07/1478149586041276-1478149586048644.jpg\" align=\"center\"></p>\n      <div>\n       &nbsp;\n      </div>\n      <div>\n       　　新闻背景：慕尼黑工业大学交响乐团主要由在读学生、教授、工作人员以及校友组成，已成立20周年。自2008年起，该乐团开始与慕尼黑交响乐团合作，并于每年圣诞节在慕尼黑交响乐团演出厅举办一年一度的传统交流音乐会。\n      </div>\n      <div id=\"网站群管理\"></div>\n     </div></td>\n   </tr>\n  </tbody>\n </table><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"http://e.weibo.com/u/3624694175\" target=\"_blank\"><img src=\"http://news.scu.edu.cn/news2012/rootimages/2014/04/01/1390026133621224.jpg\" border=\"0\"></a><img src=\"http://news.scu.edu.cn/news2012/rootimages/2014/06/27/1402363146991499.jpg\" border=\"0\"><img src=\"http://news.scu.edu.cn/news2012/1443489043291696.jpg\" border=\"0\"></p>\n <table cellspacing=\"0\" cellpadding=\"0\" width=\"90%\" align=\"center\" border=\"0\">\n  <tbody>\n   <tr>\n    <td valign=\"top\" align=\"right\"><br>【<a href=\"\"><font color=\"#808080\">大</font></a> <a href=\"\"><font color=\"#808080\">中</font></a> <a href=\"\"><font color=\"#808080\">小</font></a>】【<a onclick=\"javascript:window.print()\" href=\"http://news.scu.edu.cn/#\">打印本文</a>】【<a href=\"\">关闭窗口</a>】 <br></td>\n    <td valign=\"top\" align=\"right\">&nbsp;</td>\n   </tr>\n  </tbody>\n </table><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"http://e.weibo.com/u/3624694175\" target=\"_blank\"></a> <p>&nbsp;</p></td>",
      "date" : "2016-11-07T03:17:00Z"
    }, {
      "url" : "http://news.scu.edu.cn/news2012/cdzx/webinfo/2016/11/1478149586038727.htm",
      "title" : "教育部2016年“基础学科拔尖学生...",
      "html" : "<td valign=\"top\" width=\"719\" background=\"/news2012/lib/images/zenwen1105_14.jpg\" colspan=\"2\" height=\"828\" alt=\"\"><br>\n <table class=\"pcenter_t17\" cellspacing=\"0\" cellpadding=\"0\" width=\"90%\" align=\"center\" border=\"0\">\n  <tbody>\n   <tr>\n    <td class=\"hangjv\" id=\"zoom\" valign=\"top\" colspan=\"2\" td><br>　 \n     <div id=\"BodyLabel\">\n      <div>\n       　　11月5日上午，教育部2016年“基础学科拔尖学生培养试验计划”经验交流会在四川大学望江校区明德楼401会议室举行。四川大学副校长步宏教授、\n       <a name=\"OLE_LINK2\">教育部高等教育司理工处处长吴爱华</a>及19所“基础学科拔尖学生培养试验计划”的高校代表参加了交流会。\n      </div>\n      <div>\n       &nbsp;\n      </div>\n      <div>\n       　　四川大学副校长步宏教授在致辞中对参会代表的到来表示欢迎，在简要介绍四川大学基本概况后，对四川大学近年来“基础学科拔尖学生培养试验计划”的工作情况做了简要汇报。他谈到，在教育部的指导下，四川大学以吸引学生专注基础学科、培养优秀学生、推进高等教育教学改革和“双一流”建设为目标，探索基础学科教育教学改革，在“双特生”培养、小班化教学、学术社团开展等方面取得了很大成效，积累了一定经验。步宏副校长希望各高校通过此次交流会进一步加强关于基础学科教育教学改革的经验学习，促进“基础学科拔尖学生培养试验计划”的实施和发展。\n      </div>\n      <div>\n       &nbsp;\n      </div>\n      <p align=\"center\"><img border=\"1\" alt=\"\" src=\"http://news.scu.edu.cn/news2012/rootimages/2016/11/07/1478149586038727-1478149586110844.jpg\" align=\"center\"></p>\n      <div>\n       &nbsp;\n      </div>\n      <div>\n       　　教育部高等教育司理工处\n       <a name=\"OLE_LINK3\">吴爱华处长</a>在致辞中对四川大学“基础学科拔尖学生培养试验计划”的建设成效给予高度肯定，对目前“基础学科拔尖学生培养试验计划”的发展状况进行了介绍。他指出，教育部高度重视高等教育工作，在国际社会更加注重理工科教育、国家大力支持科技创新、创新创业教育进一步深化的新形势下，遵循高等教育发展规律、加强高校人才培养以适应社会经济的发展势在必行。吴爱华处长希望各高校能够对\n       <a name=\"OLE_LINK1\">“基础学科拔尖学生培养试验计划”</a>课题进行深入研究，进一步解放思想，深化改革，加强国际合作，构建具有中国特色的基础学科培养体系，发挥其引领、示范作用。\n      </div>\n      <div>\n       &nbsp;\n      </div>\n      <p align=\"center\"><img border=\"1\" alt=\"\" src=\"http://news.scu.edu.cn/news2012/rootimages/2016/11/07/1478149586038727-1478149586111356.jpg\" align=\"center\"></p>\n      <div>\n       &nbsp;\n      </div>\n      <div>\n       　　开幕式后，复旦大学、吉林大学、四川大学、中山大学、北京航空航天大学等高校代表分别分享了“基础学科拔尖学生培养试验计划”的实施经验，围绕“如何发挥‘拔尖计划’的引领、示范和辐射作用，深化高等教育教学改革，提高拔尖创新人才培养水平”进行了现场讨论。清华大学、浙江大学、吉林大学、兰州大学、厦门大学对“基础学科拔尖学生培养试验计划”2016年度研究课题的进展情况及阶段成果进行了汇报交流。\n      </div>\n      <div>\n       &nbsp;\n      </div>\n      <div>\n       &nbsp;\n      </div>\n      <div>\n       &nbsp;\n      </div>\n      <div id=\"网站群管理\"></div>\n     </div></td>\n   </tr>\n  </tbody>\n </table><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"http://e.weibo.com/u/3624694175\" target=\"_blank\"><img src=\"http://news.scu.edu.cn/news2012/rootimages/2014/04/01/1390026133621224.jpg\" border=\"0\"></a><img src=\"http://news.scu.edu.cn/news2012/rootimages/2014/06/27/1402363146991499.jpg\" border=\"0\"><img src=\"http://news.scu.edu.cn/news2012/1443489043291696.jpg\" border=\"0\"></p>\n <table cellspacing=\"0\" cellpadding=\"0\" width=\"90%\" align=\"center\" border=\"0\">\n  <tbody>\n   <tr>\n    <td valign=\"top\" align=\"right\"><br>【<a href=\"\"><font color=\"#808080\">大</font></a> <a href=\"\"><font color=\"#808080\">中</font></a> <a href=\"\"><font color=\"#808080\">小</font></a>】【<a onclick=\"javascript:window.print()\" href=\"http://news.scu.edu.cn/#\">打印本文</a>】【<a href=\"\">关闭窗口</a>】 <br></td>\n    <td valign=\"top\" align=\"right\">&nbsp;</td>\n   </tr>\n  </tbody>\n </table><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"http://e.weibo.com/u/3624694175\" target=\"_blank\"></a> <p>&nbsp;</p></td>",
      "date" : "2016-11-07T03:10:00Z"
    }, {
      "url" : "http://news.scu.edu.cn/news2012/cdzx/webinfo/2016/11/1478149585777170.htm",
      "title" : "四川大学召开党委中心组（扩大）学习...",
      "html" : "<td valign=\"top\" width=\"719\" background=\"/news2012/lib/images/zenwen1105_14.jpg\" colspan=\"2\" height=\"828\" alt=\"\"><br>\n <table class=\"pcenter_t17\" cellspacing=\"0\" cellpadding=\"0\" width=\"90%\" align=\"center\" border=\"0\">\n  <tbody>\n   <tr>\n    <td class=\"hangjv\" id=\"zoom\" valign=\"top\" colspan=\"2\" td><br>　 \n     <div id=\"BodyLabel\">\n      <div align=\"center\">\n       <img border=\"0\" alt=\"\" src=\"http://news.scu.edu.cn/news2012/rootimages/2016/11/04/1478149585777170-1478149585779211.jpg\" align=\"center\">\n      </div>\n      <div align=\"left\">\n       &nbsp;\n      </div>\n      <div id=\"网站群管理\">\n       <div align=\"left\">\n        　　11月4日，四川大学党委书记王建国主持召开党委中心组（扩大）学习会，传达学习党的十八届六中全会精神，对全校学习宣传贯彻六中全会精神作出部署，提出要求。全体在校校领导，校长助理，机关各部处及业务单位主要负责人，各基层党委（总支）书记，教师代表及学生代表参加了会议。\n       </div>\n       <div align=\"left\">\n        &nbsp;\n       </div>\n       <div align=\"left\">\n        　　王建国书记在传达了党的十八届六中全会基本情况后指出，党的十八届六中全会是在我国进入全面建成小康社会决胜阶段召开的一次具有里程碑意义的会议，全会专题研究全面从严治党，主题十分重大、内容非常充实、成果十分丰硕，要深刻领会全会的重大意义和精神实质，准确把握《准则》、《条例》的重要内容和基本精神。他强调，深入学习宣传贯彻十八届六中全会精神是四川大学当前的首要政治任务，要按照党中央安排部署和教育部党组的要求，全面深入学习贯彻六中全会精神，牢固树立“四个意识”，切实把学习贯彻全会精神的过程转化为指导工作、推动发展的具体实践，全面加强学校党建工作，全面加快世界一流大学建设步伐，不断开创学校各项事业发展新局面，以优异成绩迎接党的十九大召开。\n       </div>\n       <div align=\"left\">\n        &nbsp;\n       </div>\n       <div align=\"left\">\n        　　王建国书记对学校学习宣传贯彻六中全会精神作了部署，提出了要求。一是开展学习研讨，把党员干部和师生的思想和行动统一到全会精神上来。全校各级领导班子和领导干部要带头学，党支部要组织全面学，组织全体师生认真学，把集中学习与个人自学、通读文件与集体研讨、理论学习与工作实践紧密结合起来，切实做到学以致用、用以促学，进一步增强“四个意识”，特别是核心意识、看齐意识，更加紧密地团结在以习近平同志为核心的党中央周围，坚定不移维护党中央权威和党中央集中统一领导。二是加强宣传研究，迅速掀起学习贯彻全会精神热潮。要精心组织宣传报道，营造良好的学习氛围；要认真开展系列专题座谈；要充分发挥宣讲团的宣讲作用；要深入开展理论研究。三是把学习贯彻全会精神与中心工作紧密结合，全面推动学校事业发展。要将学习贯彻全会精神与加强和改进学校党的建设、加快推进“双一流”建设和“双创”工作、深入开展“两学一做”学习教育、推进各项整改落实紧密结合起来。四是切实加强对全会精神学习宣传和贯彻落实的组织领导。成立由校党委书记及校长担任组长、其他校领导为成员的学习宣传贯彻六中全会精神领导小组，全面领导全校学习宣传贯彻工作，全面确保对六中全会精神的学习宣传贯彻落实落细落小，取得实效。\n       </div>\n       <div align=\"left\">\n        &nbsp;\n       </div>\n       <div align=\"left\">\n        　　谢和平校长在会上的讲话中强调，十八届六中全会专题研究了全面从严治党这一重大问题，审议通过的《准则》和《条例》两个重要文件，是新时期推进全面从严治党、开创党的建设新局面的行动纲领。这与学校每个基层党组织、每个支部、每个干部、每个党员都密切相关，具有十分重大的意义。随后，他围绕“学习六中全会精神要学什么”以及“贯彻落实六中全会精神要做什么”两方面畅谈了自己对学习贯彻十八届六中全会的心得体会，希望全体党员干部在学习六中全会精神时要深刻领会把握四个方面的重要内容。一是深刻领会和把握确立习近平总书记作为“全党核心”的重大意义。二是深刻领会和把握新形势下加强和规范党内政治生活的基本遵循。三是深刻领会和把握加强党内监督的基本要求。四是深刻领会和把握领导干部特别是高级干部是严肃党内政治生活、加强党内监督的重点对象。\n       </div>\n       <div align=\"left\">\n        &nbsp;\n       </div>\n       <div align=\"left\">\n        　　谢和平校长强调，学习贯彻落实六中全会精神，要以六中全会精神为指引，结合学校的工作实际，着力抓好全面加强学校党的建设这个伟大工程，着力干好全面推进世界一流大学建设这个伟大事业，真正做到在全面从严治党上合格，在学习贯彻中央治国理政新理念、新思想、新战略上合格，在规范党员的行为作风上合格，在推动学校改革发展稳定各项工作上合格。他希望校院两级领导班子和全体党员干部在学习贯彻落实六中全会精神时能做好“八个结合”，即：把学习贯彻全会精神与对《准则》《条例》相关要求的宣讲研讨、对照排查、整改工作结合起来，与做好迎接中央巡视的准备工作结合起来，与加强干部队伍建设结合起来，与筹备学校第八次党代会结合起来，与“两学一做”学习教育结合起来，与迎接党的十九大召开、营造良好氛围结合起来，与加快推进国家“双创”示范基地建设结合起来，与推进落实省市校共建世界一流大学的具体工作结合起来。\n       </div>\n       <div>\n        &nbsp;\n       </div>\n       <div>\n        　　会上，马克思主义学院院长蒋永穆以《牢固树立“四个意识”&nbsp; 奋力推进马克思主义学院建设》为题，历史文化学院教授陈廷湘以《维护党中央权威&nbsp; 做合格共产党员》为题，华西临床医学院党委副书记、纪委书记李正赤以《严字当头——党的十八届六中全会精神学习体会》为题，\n        <a name=\"_GoBack\"></a>化学学院党委书记王智猛以《深入贯彻落实十八届六中全会精神&nbsp;&nbsp; 努力开创学院党建工作新局面》为题，校党委宣传部部长高中伟以《中国共产党党的建设新的里程碑》为题，学生代表、化学工程学院2015级博士研究生奚月恒以《学习十八届六中全会精神&nbsp; 做一名优秀的青年学生党员》为题，结合个人工作及学习实际，畅谈了学习六中全会精神的心得体会。\n       </div>\n      </div>\n     </div></td>\n   </tr>\n  </tbody>\n </table><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"http://e.weibo.com/u/3624694175\" target=\"_blank\"><img src=\"http://news.scu.edu.cn/news2012/rootimages/2014/04/01/1390026133621224.jpg\" border=\"0\"></a><img src=\"http://news.scu.edu.cn/news2012/rootimages/2014/06/27/1402363146991499.jpg\" border=\"0\"><img src=\"http://news.scu.edu.cn/news2012/1443489043291696.jpg\" border=\"0\"></p>\n <table cellspacing=\"0\" cellpadding=\"0\" width=\"90%\" align=\"center\" border=\"0\">\n  <tbody>\n   <tr>\n    <td valign=\"top\" align=\"right\"><br>【<a href=\"\"><font color=\"#808080\">大</font></a> <a href=\"\"><font color=\"#808080\">中</font></a> <a href=\"\"><font color=\"#808080\">小</font></a>】【<a onclick=\"javascript:window.print()\" href=\"http://news.scu.edu.cn/#\">打印本文</a>】【<a href=\"\">关闭窗口</a>】 <br></td>\n    <td valign=\"top\" align=\"right\">&nbsp;</td>\n   </tr>\n  </tbody>\n </table><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"http://e.weibo.com/u/3624694175\" target=\"_blank\"></a> <p>&nbsp;</p></td>",
      "date" : "2016-11-04T10:30:00Z"
    }, {
      "url" : "http://news.scu.edu.cn/news2012/cdzx/webinfo/2016/11/1478149585770289.htm",
      "title" : "谢和平校长为全校党员干部作“两学一...",
      "html" : "<td valign=\"top\" width=\"719\" background=\"/news2012/lib/images/zenwen1105_14.jpg\" colspan=\"2\" height=\"828\" alt=\"\"><br>\n <table class=\"pcenter_t17\" cellspacing=\"0\" cellpadding=\"0\" width=\"90%\" align=\"center\" border=\"0\">\n  <tbody>\n   <tr>\n    <td class=\"hangjv\" id=\"zoom\" valign=\"top\" colspan=\"2\" td><br>　 \n     <div id=\"BodyLabel\">\n      <div align=\"center\">\n       <img alt=\"\" src=\"http://news.scu.edu.cn/news2012/rootimages/2016/11/04/1478149585770289-1478149585773290.jpg\" align=\"center\" border=\"0\">\n      </div>\n      <div align=\"left\">\n       &nbsp;\n      </div>\n      <div align=\"left\">\n       　　11月4日下午，四川大学“两学一做”学习教育第三场党课报告会在望江校区笃行楼报告厅举行，校长谢和平同志为学校党员干部作报告。全体在校校领导、校长助理，全体中层干部，学校“两学一做”学习教育宣讲团成员，科级干部代表，教职工党支部书记代表，辅导员代表参加报告会。报告会由校党委常务副书记罗中枢同志主持。\n      </div>\n      <div align=\"left\">\n       &nbsp;\n      </div>\n      <div align=\"left\">\n       　　报告会上，谢和平校长以“深入开展‘两学一做’学习教育 加快推进世界一流大学建设”为主题，从全面推进从严治党、推进学校改革发展和建设世界一流大学的战略高度，阐述了开展“两学一做”学习教育的重大意义和现实价值。他强调，全体党员干部要充分认识到“两学一做”学习教育，学是基础。他重点结合自己对习近平总书记在“科技三会”、哲学社会科学座谈会、全国卫生与健康大会上的重要讲话精神的学习和思考，特别是对刚刚结束的以全面推进从严治党为主题的十八届六中全会精神的学习和思考，深刻阐述了党和国家尤其是习近平总书记对高等教育事业发展提出的一系列新观点、新论断、新要求。\n      </div>\n      <div>\n       &nbsp;\n      </div>\n      <div>\n       　　谢和平校长紧密联系学校实际，把“两学一做”学习教育与学校建设世界一流大学的发展目标即“川大梦”相结合。他强调，“两学一做”关键在做。他对四川大学未来一段时间应该怎么建、川大人应该怎么做等重大战略问题畅谈了高屋建瓴的见解与构想，围绕着“川大应该建设世界一流大学”、“川大人应该做合格党员”两个方面展开深入的阐释。谢校长希望四川大学每一位党员、干部都要充分认识到：讲厚道是一种大智慧、真聪明，懂尊重是一种品德，知敬畏是一种高境界。要努力做一名“四讲四有”的合格党员，努力做一个利他的人，做一个关心别人比关心自己多一点的好人，做一个“讲厚道、懂尊重、知敬畏”的人。\n      </div>\n      <div>\n       &nbsp;\n      </div>\n      <div>\n       　　罗中枢常务副书记在总结时指出，谢和平校长的党课报告对习近平总书记系列重要讲话精神做了全面准确的把握和高度凝练的概括，报告还紧密结合高等教育实际、川大实际，对学校未来改革发展做了战略谋划，讲得深入透彻，具有很强的前瞻性和战略高度，对我们扎实开展“两学一做”学习教育、学习贯彻十八届六中全会精神具有很强的指导性和促进作用。他强调，学校各级党组织和全体党员、尤其是领导干部，要不断加强党性修养，坚定理想信念，牢固树立忠诚于党、干净做事、敢于担当的党员意识，认真做好“两学一做”学习教育各项后续工作。\n      </div>\n      <div>\n       &nbsp;\n      </div>\n      <div>\n       &nbsp;\n      </div>\n      <div id=\"网站群管理\"></div>\n     </div></td>\n   </tr>\n  </tbody>\n </table><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"http://e.weibo.com/u/3624694175\" target=\"_blank\"><img src=\"http://news.scu.edu.cn/news2012/rootimages/2014/04/01/1390026133621224.jpg\" border=\"0\"></a><img src=\"http://news.scu.edu.cn/news2012/rootimages/2014/06/27/1402363146991499.jpg\" border=\"0\"><img src=\"http://news.scu.edu.cn/news2012/1443489043291696.jpg\" border=\"0\"></p>\n <table cellspacing=\"0\" cellpadding=\"0\" width=\"90%\" align=\"center\" border=\"0\">\n  <tbody>\n   <tr>\n    <td valign=\"top\" align=\"right\"><br>【<a href=\"\"><font color=\"#808080\">大</font></a> <a href=\"\"><font color=\"#808080\">中</font></a> <a href=\"\"><font color=\"#808080\">小</font></a>】【<a onclick=\"javascript:window.print()\" href=\"http://news.scu.edu.cn/#\">打印本文</a>】【<a href=\"\">关闭窗口</a>】 <br></td>\n    <td valign=\"top\" align=\"right\">&nbsp;</td>\n   </tr>\n  </tbody>\n </table><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"http://e.weibo.com/u/3624694175\" target=\"_blank\"></a> <p>&nbsp;</p></td>",
      "date" : "2016-11-04T10:10:00Z"
    } ]
  }
}
```

### 错误处理
如果发生了错误, 则返回如下格式的消息

```json
{
  "type" : "err",
  "code" : 1,
  "msg" : "Unexpected end-of-input: was expecting closing '\"' for name\n at [Source: {\"; line: 1, column: 5]"
}
```

### 注意

以上所有示例都经过格式化, 实际使用时, 必须输入单行的JSON对象
输出也保证一定是一行一个对象

## 接下来的工作

- 继续与服务器组沟通, 设计一个晚辈的借口
- 完善新闻网站的收集工作
- 继续设计与开发命令行工具的指令
