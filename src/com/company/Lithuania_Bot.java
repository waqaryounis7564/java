package com.company;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.logging.Logger;

public class Lithuania_Bot {
    private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36 Edg/83.0.478.44";

    public static boolean scrapeData() {
        for (int page = 1; page < 23; page++) {
            try {
                Document doc = Jsoup.connect("https://efdsearch.senate.gov/search/report/data/?draw=1&columns%5B0%5D%5Bdata%5D=0&columns%5B0%5D%5Bname%5D=&columns%5B0%5D%5Bsearchable%5D=true&columns%5B0%5D%5Borderable%5D=true&columns%5B0%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B0%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B1%5D%5Bdata%5D=1&columns%5B1%5D%5Bname%5D=&columns%5B1%5D%5Bsearchable%5D=true&columns%5B1%5D%5Borderable%5D=true&columns%5B1%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B1%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B2%5D%5Bdata%5D=2&columns%5B2%5D%5Bname%5D=&columns%5B2%5D%5Bsearchable%5D=true&columns%5B2%5D%5Borderable%5D=true&columns%5B2%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B2%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B3%5D%5Bdata%5D=3&columns%5B3%5D%5Bname%5D=&columns%5B3%5D%5Bsearchable%5D=true&columns%5B3%5D%5Borderable%5D=true&columns%5B3%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B3%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B4%5D%5Bdata%5D=4&columns%5B4%5D%5Bname%5D=&columns%5B4%5D%5Bsearchable%5D=true&columns%5B4%5D%5Borderable%5D=true&columns%5B4%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B4%5D%5Bsearch%5D%5Bregex%5D=false&order%5B0%5D%5Bcolumn%5D=1&order%5B0%5D%5Bdir%5D=asc&order%5B1%5D%5Bcolumn%5D=0&order%5B1%5D%5Bdir%5D=asc&start=0&length=25&search%5Bvalue%5D=&search%5Bregex%5D=false&report_types=%5B%5D&filer_types=%5B%5D&submitted_start_date=06%2F01%2F2020+00%3A00%3A00&submitted_end_date=&candidate_state=&senator_state=&office_id=&first_name=&last_name=")
                        .userAgent(userAgent)
                        .followRedirects(true)
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .header("Accept", "application/json")
                        .header("Accept-Language", "en-US,en;q=0.9").
                                followRedirects(false).timeout(15000)
                        .post();

                //max data 25 records/page
                //last page for loop
                // System.out.println(doc.select(" div.table-pagination.float-right > ul > li:nth-child(6) > a").text());
//table rows size
                Elements rows = doc.select("table > tbody>tr");
                System.out.println("page number " + page);
                for (int index = 0; index < 25; index++) {
                    Element row = rows.get(index);
                    //Release Day
                    //  System.out.println("DAy:"+row.select("td:nth-child(1)").text());
                    //Release time
                    // System.out.println("Time:"+row.select("td:nth-child(2) > span").text());
                    //headline
                    String headLine = row.select("td:nth-child(3) > a").text();
                    //System.out.println("HeadLine:"+row.select("td:nth-child(3) > a").text());
                    //Headline link
                    //System.out.println("Link:"+" "+"https://nasdaqbaltic.com"+row.select("td:nth-child(3) > a").attr("href"));
                    //companyName
                    //System.out.println("companyName:"+row.select("td:nth-child(5) > span").text());
                    //ticker
                    // System.out.println("Ticker:"+row.select("td:nth-child(5) > span.text12").text());
                    String[] keyWords = new String[]{"vln", "rig", "tln"};


                    String key = "it’s own shares";
                    if (headLine.toLowerCase().contains(key.toLowerCase())) {
                        System.out.println("DAy:" + row.select("td:nth-child(1)").text());
                        System.out.println("Time:" + row.select("td:nth-child(2) > span").text());
                        System.out.println("HeadLine:" + row.select("td:nth-child(3) > a").text());
                        System.out.println("Ticker:" + row.select("td:nth-child(5) > span.text12").text());
                        String ticker = row.select("td:nth-child(5) > span.text12").text();
                        System.out.println("-------");
                        for (int i = 0; i < 3; i++) {
                            if (ticker.toLowerCase().contains(keyWords[i])) {
                                if (keyWords[i].contains("vln")) {
                                    //store in lithuania country table
                                } else if (keyWords[i].contains("rig")) {
                                    //store in latvia country table
                                } else {
                                    // store in estonia country table
                                }

                            }
                        }
                    }
                }

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return true;
    }
}
