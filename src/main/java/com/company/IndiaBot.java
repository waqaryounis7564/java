package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class IndiaBot {
    public static void scrapeData() {


        String url = "https://api.bseindia.com/BseIndiaAPI/api/AnnGetData/w?strCat=-1&strPrevDate=20200715&strScrip=&strSearch=P&strToDate=20200715&strType=C";
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //System.out.println(response.toString());
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("Table");
            Calendar mycal = new GregorianCalendar(2020, 7, 1);
String line="share dealing;shares dealing;acquisition of shares;own securities;purchase of shares;own shares buyback;buy back;buyback;treasury share;treasury stock;buy-back;share repurchase;transaction in own securities;transaction in own shares;own shares;shares buy-back;shares buyback;own;treasury;repurchase;buyback program;dealing in company shares;dealing in company share;shares purchase;share purchase;tender offer;purchase";
        String[] keyWords= line.split(";");
            for (String word:keyWords) {
                System.out.println(word);
            }
//            Calendar cal = Calendar.getInstance();
//            int month = cal.get(Calendar.MONTH)+1;
//            int year = cal.get(Calendar.YEAR);
//            cal.clear();
//            cal.set(Calendar.YEAR, year);
//
//            for (int currentMonth = month; currentMonth > 0; currentMonth--) {
//
//                cal.set(Calendar.MONTH, currentMonth);
//                YearMonth yearMonthObject = YearMonth.of(year, currentMonth);
//                int days = yearMonthObject.lengthOfMonth();
//                //first day :
//                for (int currentDate = days; currentDate > 0; currentDate--) {
//
//                    System.out.println("currentDate=" + currentDate);
//                    System.out.println("currentMonth=" + currentMonth);
//                    System.out.println("current year" + year);
//                    System.out.println("----");
//
//                    if (currentMonth == 1 && year == 2020) {
//                        currentMonth = 12;
//                        year = 2019;
//                    }
//                    if (currentMonth == 1 && year == 2019) break;
//                }
//            }


//            for (Object object : jsonArray) {
//                JSONObject jsonObject1 = new JSONObject(object.toString());
//
//                String newsId = jsonObject1.getString("NEWSID");
//                System.out.println(newsId);
//                int companyId = jsonObject1.getInt("SCRIP_CD");
//                System.out.println(companyId);
//                String headline = jsonObject1.getString("NEWSSUB");
//                System.out.println("HeadLine " + " " + headline);
//                String table = jsonObject1.getString("MORE");
//                System.out.println("table " + table);
//                String date = jsonObject1.getString("NEWS_DT");
//                System.out.println("date " + date);
//                String pdf = jsonObject1.getString("ATTACHMENTNAME");
//                String pdfLink;
//                if (pdf.equals(" ")) {
//                    pdfLink = table;
//                }
//                pdfLink = "https://www.bseindia.com/corporates/xml-data/corpfiling/AttachLive/" + pdf;
//                System.out.println(pdfLink);
//
//                System.out.println("-------");
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

