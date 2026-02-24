package com.china.soft.rss;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.net.URL;
import java.util.List;

public class RssParser {

    public static SyndFeed parseRss(String rssUrl) throws Exception {
        SyndFeedInput input = new SyndFeedInput();
        input.setPreserveWireFeed(true); // 保留原始Feed信息

        // 读取远程RSS并解析
        try (XmlReader reader = new XmlReader(new URL(rssUrl))) {
            return input.build(reader);
        } catch (FeedException e) {
            throw new RuntimeException("RSS解析失败", e);
        }
    }

    public static void main(String[] args) {
        // 测试解析知乎每日精选RSS
        String rssUrl = "https://blog.zhheo.com/rss.xml";
        try {
            SyndFeed feed = parseRss(rssUrl);
            // 输出Feed基本信息
            System.out.println("RSS标题：" + feed.getTitle());
            System.out.println("RSS链接：" + feed.getLink());
            System.out.println("RSS描述：" + feed.getDescription());

            // 输出前5条文章
            List<SyndEntry> entries = feed.getEntries();
            for (int i = 0; i < Math.min(5, entries.size()); i++) {
                SyndEntry entry = entries.get(i);
                System.out.println("\n第" + (i+1) + "篇文章：");
                System.out.println("标题：" + entry.getTitle());
                System.out.println("链接：" + entry.getLink());
                System.out.println("发布时间：" + entry.getPublishedDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}