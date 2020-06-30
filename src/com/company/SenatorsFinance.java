package com.company;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.Html;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SenatorsFinance {
    private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36 Edg/83.0.478.44";

    public static void scrapeData() {
        try (WebClient webClient = new WebClient(BrowserVersion.CHROME);
        ) {
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.waitForBackgroundJavaScript(10000);
            webClient.setJavaScriptTimeout(10000);
            webClient.waitForBackgroundJavaScriptStartingBefore(10000);

            HtmlPage page = webClient.getPage("https://www.congress.gov/members?q={%22chamber%22:%22Senate%22,%22congress%22:%22116%22}&searchResultViewType=expanded&pageSize=250");
            webClient.waitForBackgroundJavaScript(10_000);
            System.out.println(page.getWebResponse().getContentAsString());
            // webClient.waitForBackgroundJavaScript(10000);
            // DomElement chk =  page.querySelector("iframe");
            //HtmlPage pg=chk.click();
            //webClient.waitForBackgroundJavaScript(10000);
//            List<NameValuePair> headers = page.getWebResponse().getResponseHeaders();
//            for (NameValuePair header : headers) {
//                System.out.println(header.getName() + " : " + header.getValue());}
//            Map<String, String> mapheaers = headers.stream().collect(
//                    Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
//            Document doc = Jsoup.connect("https://www.opensecrets.org/personal-finances")
//                    .userAgent(userAgent)
//                    .followRedirects(true)
//                    .ignoreHttpErrors(true)
//                    .headers(mapheaers)
//                    .followRedirects(false)
//                    .timeout(15000).get();
//            System.out.println(doc.body());
                // HtmlPage doc =webClient.getPage("https://www.opensecrets.org/personal-finances");
                //System.out.println(doc.asText());
            //HtmlPage pg = chk.click();
            //String res = pg.asText();
//            Document jPage = Jsoup.parse(res);
//            System.out.println(jPage.html());
            } catch(IOException e){
                e.printStackTrace();
            }

        }
    }

