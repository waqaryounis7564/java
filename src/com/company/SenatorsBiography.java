package com.company;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;


public class SenatorsBiography implements RefreshHandler, Serializable {
    @Override
    public void handleRefresh(final Page page, final URL url, final int seconds) throws IOException {
        final WebWindow window = page.getEnclosingWindow();
        if (window == null) {
            return;
        }
        final WebClient client = window.getWebClient();
        if (page.getUrl().toExternalForm().equals(url.toExternalForm())
                && HttpMethod.GET == page.getWebResponse().getWebRequest().getHttpMethod()) {
            final String msg = "Refresh to " + url + " (" + seconds + "s) aborted by HtmlUnit: "
                    + "Attempted to refresh a page using an ImmediateRefreshHandler "
                    + "which could have caused an OutOfMemoryError "
                    + "Please use WaitingRefreshHandler or ThreadedRefreshHandler instead.";
            throw new RuntimeException(msg);
        }
        client.getPage(window, new WebRequest(url));
    }


    public static void scrapeData() {
        try (WebClient webClient = new WebClient(BrowserVersion.CHROME);
        ) {
            java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getCookieManager().setCookiesEnabled(true);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.setJavaScriptTimeout(10_000);

            HtmlPage page = webClient.getPage("https://www.congress.gov/members?q=%7B%22chamber%22%3A%22Senate%22%7D&searchResultViewType=expanded&pageSize=250&page=1");
            webClient.setJavaScriptTimeout(10_000);
            String response = page.asXml();
            Document document = Jsoup.parse(response);
            Elements ol = document.select("ol>li");
            String served = "";
            for (Element senator : ol) {
                String name = senator.select("span > a").text();
                String state = senator.select("div.quick-search-member > div.member-profile.member-image-exists > span:nth-child(1) > span").text();
                String party = senator.select("div.quick-search-member > div.member-profile.member-image-exists > span:nth-child(2) > span").text();
                Elements duration = senator.select("div.quick-search-member > div.member-profile.member-image-exists > span:nth-child(3) > span > ul>li");
                served = duration.text()+"\t";
                if (duration.size() > 1) {
                    for (Element date : duration) {
                        served += date.select(":nth-child(2)").text();
                    }
                }
                String img = senator.select("div.quick-search-member > div.member-image > img").attr("src");
                System.out.println(name);
                System.out.println(state);
                System.out.println(party);
                System.out.println(served);
                System.out.println("https://www.congress.gov" + img);
                System.out.println("------------------------------------------");
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}


