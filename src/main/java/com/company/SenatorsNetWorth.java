package com.company;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.logging.Logger;


public class SenatorsNetWorth {
        private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36 Edg/83.0.478.44";

        public static boolean scrapeData() {
            try {
                int totalPages=10;
                for (int pageCount=2;pageCount<totalPages;pageCount++){
                    Document doc = Jsoup.connect("https://247wallst.com/special-report/2019/10/09/76-richest-members-of-congress/"+pageCount+"/")
                            .userAgent(userAgent)
                            .followRedirects(true)
                            .ignoreHttpErrors(true)
                            .ignoreContentType(true)
                            .header("Accept", "application/json")
                            .header("Accept-Language", "en-US,en;q=0.9")
                            .followRedirects(false).timeout(15000)
                            .get();

                    Elements div=doc.select("div.pubentry-bullets");
                    for (Element pTag:div) {
                        String description=pTag.select("p>span:nth-child(1)").text();
                        String rank =description.split(". ")[0];
                        System.out.println(rank);
                        int startIndex=description.indexOf(".",description.indexOf(".")+1) ;
                        int endIndex=description.indexOf("of");
                        String fullName=description.substring(startIndex+1,endIndex);
                        System.out.println(fullName);
                        System.out.println(pTag.select("p>span:nth-child(3)").text().substring(18));
                        System.out.println(pTag.select("p>span:nth-child(5)").text().substring(20));
                        System.out.println(pTag.select("p>span:nth-child(7)").text().substring(19));
                        System.out.println(pTag.select("p>span:nth-child(9)").text().substring(20));
                        System.out.println("---------------------");
                    }


                }

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            return true;
        }
    }




