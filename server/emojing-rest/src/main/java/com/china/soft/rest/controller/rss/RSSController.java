package com.china.soft.rest.controller.rss;

import com.china.soft.commons.utils.response.CommonResponse;
import com.china.soft.rss.RssParser;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.china.soft.commons.utils.response.CommonResponse.success;

@RestController
@RequestMapping("/rss")
public class RSSController {

    @GetMapping("/list")
    @PermitAll
    public CommonResponse<List<HashMap<String, Object>>> list() throws Exception {
       List<HashMap<String, Object>> results = new ArrayList<>();
        String rssUrl = "https://blog.zhheo.com/rss.xml";
        SyndFeed feed = RssParser.parseRss(rssUrl);
        List<SyndEntry> entries = feed.getEntries();
        for (int i = 0; i < Math.min(6, entries.size()); i++) {
            HashMap<String, Object> map = new HashMap<>();
            SyndEntry entry = entries.get(i);
            map.put("id", (i+1));
            map.put("title", entry.getTitle());
            map.put("link", entry.getLink());
            map.put("pubDate", entry.getPublishedDate());
            results.add(map);
        }
        return success(results);
    }


}
