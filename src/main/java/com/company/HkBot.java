package com.company;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HkBot {
    private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36 Edg/83.0.478.44";

    public static void scrapeData() throws IOException, ParseException {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        String result =String.valueOf( cal.getTime());
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date out=dateFormat.parse(result);
        String fromDate=new SimpleDateFormat("yyyyMMdd").format(out);
        String now = String.valueOf(new Date());
        Date to=dateFormat.parse(now);
        String toDate=new SimpleDateFormat("yyyyMMdd").format(to);



//https://www1.hkexnews.hk/search/titlesearch.xhtml?lang=en
        Connection.Response response = Jsoup.connect("https://www1.hkexnews.hk/search/titleSearchServlet.do?sortDir=0&sortByOptions=DateTime&category=0&market=SEHK&stockId=-1&documentType=-1&fromDate=20200511&toDate=20200611&title=&searchType=1&t1code=50000&t2Gcode=-2&t2code=-2&rowRange=400&lang=E")
                .userAgent(userAgent)
                .followRedirects(true)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .header("Accept", "application/json")
                .header("Accept-Language", "en-US,en;q=0.9").
                        followRedirects(false).timeout(15000)
                .method(Connection.Method.GET)
                .execute();


        JSONObject res = new JSONObject(response.body());
        String val = String.valueOf(res.get("result"));

        JSONArray parser = new JSONArray(val);
        System.out.println(parser.getJSONObject(1));
//
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.MONTH, -1);
//        Date result = cal.getTime();
//        System.out.println(result);
//
        for (int index = 0; index < 5; index++) {
            System.out.println("File url" + " https://www1.hkexnews.hk" + parser.getJSONObject(index).getString("FILE_LINK"));
            System.out.println("stock code" + " " + parser.getJSONObject(index).getString("STOCK_CODE"));
            System.out.println("Heading" + " " + parser.getJSONObject(index).getString("LONG_TEXT").replace(" &#x5b;", "[").replace("&#x5d;", "]"));
            System.out.println("Release time" + " " + parser.getJSONObject(index).getString("DATE_TIME"));
            System.out.println("CompanyNAme" + " " + parser.getJSONObject(index).getString("STOCK_NAME"));
        }
    }


}
