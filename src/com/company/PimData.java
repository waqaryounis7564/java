package com.company;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PimData {
    private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36 Edg/83.0.478.44";

    public static boolean scrapeData() {
        String key = getKey();
        List<String> issuer = getIssuer();
        for (int index = 0; index < issuer.size(); index++) {
            String[] data1 = getPermId(key, issuer.get(index));
            if (data1 == null) continue;
            String ticker = data1[0];
            String permId = data1[1];
            String permUrl = data1[2];

            String[] data2 = getPermStatus(key, permId);
            String status = data2[0];
            String isPblic = data2[1];
            //       writeToDB(permUrl, permId, ticker, status, isPblic);
            //     count++;
            System.out.println("Url" + " " + permUrl);
            System.out.println("permId" + " " + permId);
            System.out.println("Staus:" + " " + status);
            System.out.println("Ispublic:" + " " + isPblic);
            System.out.println("ticker:" + " " + ticker);
            System.out.println("--------");
        }
//        System.out.println(count);
        return true;
    }

    private static List<String> getIssuer() {
        String[] list=new String[]{"issuer",
                "Little Star Partnership",
                "Buhler Telferner Partnership",
                "Starboard Corporation - Commercial Real Estate",
                "McKinney Texas Indpt Sch Dist Rate",
                "CP Kent Place Member LLC" ,
                "",
                "",
                "United Technologies Corp. (Exchanged) Raytheon  (Received)",
                "BBT (Exchanged) Truist Financial Corporation (Received)",
                "JPMorgan Chase & Co.",
                "Microsoft Corporation",
                "Johnson & Johnson",
                "Abbott Laboratories",
                "Comcast Corporation",
                "Citigroup Inc.",
                "UBS Contingent Autocall on SLB 90281C708 Rate",
                "UBS Contingent Autocall on SLB 90281C708 Rate",
                "QUALCOMM Incorporated",
                "Skyworks Solutions, Inc.",
                "ACAP Strategic Fund",
                "PGIM Jennison Global Opportunities Fund-Class A",
                "Verizon Communications Inc.",
                "SPDR S&P 500 ETF Trust",
                "Sprott Physical Gold and Silver Trust",
                "The Walt Disney ",
                "Skyworks Solutions, Inc.",
                "CVS Health Corporation",
                "PayPal Holdings, Inc.",
                "AQR Long-Short Equity",
                "Vodafone Group Plc",
                "Marathon Petroleum Corporation",
                "Wells Fargo & ",
                "Cisco Systems, Inc.",
                "AbbVie Inc.",
                "JPMorgan Chase & Co."};
        return new ArrayList<>(Arrays.asList(list));
    }

    private static String getKey() {
        String key = "";
        try {
            Connection.Response response = Jsoup.connect("https://permid.org/api/mdaas/ws/auth/getTempToken")
                    .userAgent(userAgent)
                    .followRedirects(true)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .header("Accept", "application/json")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .followRedirects(false)
                    .method(Connection.Method.GET).timeout(15000)
                    .execute();
            JSONObject data = new JSONObject(response.body());
            key = data.getString("consumerKey");
        } catch (IOException ex) {
            System.out.println("error");
        }
        return key;
    }

    private static String[] getPermId(String consumerKey, String issuer) {
        String[] data = new String[3];
        String ticker = "not Available";
        String permId = "";
        try {
            Connection.Response response = Jsoup
                    .connect("https://permid.org/api/search?&num=5&q=" + issuer + "&selected=all&start=1")
                    .userAgent(userAgent)
                    .method(Connection.Method.GET)
                    .header("Content-Type", "application/json")
                    .followRedirects(true)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .header("Accept", "application/json")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .followRedirects(false)
                    .header("X-AG-Access-Token", consumerKey)
                    .timeout(15000)
                    .execute();
            if (response.statusCode() == 400) {
                return null;
            }
            JSONObject announcements = new JSONObject(response.body());
            JSONArray jsonArr = announcements.getJSONObject("result").getJSONObject("organizations").getJSONArray("entities");
            if (jsonArr.length() == 0) {
                return null;
            } else {
                String permUrl = jsonArr.getJSONObject(0).getString("@id");
                if (jsonArr.getJSONObject(0).has("primaryTicker")) {
                    ticker = jsonArr.getJSONObject(0).getString("primaryTicker");
                }
                permId = permUrl.substring(21);
                data[0] = ticker;
                data[1] = permId;
                data[2] = permUrl;
            }

        } catch (IOException ex) {
            System.out.println("error");
        }
        return data;
    }

    private static String[] getPermStatus(String consumerKey, String permId) {
        String[] data = new String[2];
        String tickerUrl = "https://permid.org/api/mdaas/getEntityById/" + permId;
        try {
            Connection.Response response = Jsoup.connect(tickerUrl)
                    .userAgent(userAgent)
                    .method(Connection.Method.GET)
                    .header("Content-Type", "application/json")
                    .followRedirects(true)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .header("Accept", "application/json")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .followRedirects(false)
                    .header("X-AG-Access-Token", consumerKey)
                    .execute();
            JSONObject result = new JSONObject(response.body());
            String status = String.valueOf(result.getJSONArray("Status").get(0));
            String isPblic = String.valueOf(result.getJSONArray("Public").get(0));
            data[0] = status;
            data[1] = isPblic;
        } catch (IOException ex) {
            System.out.println("error");
        }
        return data;
    }

}

//direct code for pimData
//        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36 Edg/83.0.478.44";
//        List<String> issuer = new ArrayList<>();
//        issuer.add("Starbucks Corporation");
//
//        issuer.add("cvlu");
////        issuer.add("Microsoft Corporation");
//        issuer.add("HP Inc.");
////        issuer.add("iShares MSCI Emerging Markets ETF");
////        issuer.add("WESTBRAND, INC. BANK HOLDING COMPANY STOCK");
////        issuer.add("Vanguard S&P Mid-cap 400 ETF");
////        issuer.add("US Treasury Note 1.25% Dtd 10/31/2013 Due 10/31/2018");
////        issuer.add("Kimberly-Clark Corporation (NYSE)");
//        try {
//            Connection.Response key = Jsoup.connect("https://permid.org/api/mdaas/ws/auth/getTempToken")
//                    .userAgent(userAgent)
//                    .followRedirects(true)
//                    .ignoreHttpErrors(true)
//                    .ignoreContentType(true)
//                    .header("Accept", "application/json")
//                    .header("Accept-Language", "en-US,en;q=0.9")
//                    .followRedirects(false)
//                    .method(Connection.Method.GET)
//                    .execute();
//            JSONObject res = new JSONObject(key.body());
//            String consumerKey = res.getString("consumerKey");
//            for (int index = 0; index < issuer.size(); index++) {
//                Connection.Response response = Jsoup
//                        .connect("https://permid.org/api/search?&num=5&q=" + issuer.get(index) + "&selected=all&start=1")
//                        .userAgent(userAgent)
//                        .method(Connection.Method.GET)
//                        .header("Content-Type", "application/json")
//                        .followRedirects(true)
//                        .ignoreHttpErrors(true)
//                        .ignoreContentType(true)
//                        .header("Accept", "application/json")
//                        .header("Accept-Language", "en-US,en;q=0.9")
//                        .followRedirects(false)
//                        .header("X-AG-Access-Token", consumerKey)
//                        .timeout(5000)
//                        .execute();
//                if (response.statusCode() == 400) continue;
//                JSONObject announcements = new JSONObject(response.body());
//                JSONArray obj = announcements.getJSONObject("result").getJSONObject("organizations").getJSONArray("entities");
//                if (obj.length() == 0) continue;
//                String permUrl = obj.getJSONObject(0).getString("@id");
//                String ticker = "N.a";
//                if ((announcements.getJSONObject("result").getJSONObject("organizations").getJSONArray("entities").getJSONObject(0).has("primaryTicker"))) {
//                    ticker = announcements.getJSONObject("result").getJSONObject("organizations").getJSONArray("entities").getJSONObject(0).getString("primaryTicker");
//                }
//                String permId = permUrl.substring(21);
//                String tickerUrl = "https://permid.org/api/mdaas/getEntityById/" + permId;
//                Connection.Response response1 = Jsoup.connect(tickerUrl)
//                        .userAgent(userAgent)
//                        .method(Connection.Method.GET)
//                        .header("Content-Type", "application/json")
//                        .followRedirects(true)
//                        .ignoreHttpErrors(true)
//                        .ignoreContentType(true)
//                        .header("Accept", "application/json")
//                        .header("Accept-Language", "en-US,en;q=0.9")
//                        .followRedirects(false)
//                        .header("X-AG-Access-Token", consumerKey)
//                        .execute();
//                JSONObject result = new JSONObject(response1.body());
//                String status = String.valueOf(result.getJSONArray("Status").get(0));
//                String isPblic = String.valueOf(result.getJSONArray("Public").get(0));
//                System.out.println("Url" + " " + permUrl);
//                System.out.println("permId" + " " + permId);
//                System.out.println("Staus:" + " " + status);
//                System.out.println("Ispublic:" + " " + isPblic);
//                System.out.println("ticker:" + " " + ticker);
//                System.out.println("--------");
//            }
//        } catch (IOException ex) {
//            System.out.println("issue");
//        }


//avoid git pull changes permdata

//package com.x2iq.senatorsData.service;
//
//        import com.x2iq.senatorsData.repo.SenatorsRepo;
//        import org.json.JSONArray;
//        import org.json.JSONObject;
//        import org.jsoup.Connection;
//        import org.jsoup.Jsoup;
//        import org.slf4j.Logger;
//        import org.slf4j.LoggerFactory;
//        import org.springframework.beans.factory.annotation.Autowired;
//        import org.springframework.stereotype.Service;
//
//        import java.io.IOException;
//        import java.util.List;
//
//@Service
// public class SenatorsPermData {
//    private SenatorsRepo senatorsRepo;
//    private int count = 0;
//    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36 Edg/83.0.478.44";
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    SenatorsPermData(SenatorsRepo senatorsRepo) {
//        this.senatorsRepo = senatorsRepo;
//    }
//
//    public boolean scrapeData() {
//        String key = getKey();
//        List<String> issuers = getIssuer();
//        String issuer = "";
//        for (int index = 0; index < issuers.size(); index++) {
//            logger.info("key is' '"+key);
//            issuer = issuers.get(index);
//            logger.info("index is ' ':"+index+"' ' & issuer is:' '"+issuer);
//            if (issuer.isEmpty()) continue;
//            String[] data1 = getPermId(key, issuer);
//            if (data1 == null) {
//                continue;
//            }
//            String ticker = data1[0];
//            String permId = data1[1];
//            String permUrl = data1[2];
//
//            String[] data2 = getPermStatus(key, permId);
//            if (data2 == null) {
//                continue;
//            }
//            String status = data2[0];
//            String isPblic = data2[1];
//            writeToDB(permUrl, permId, ticker, status, isPblic);
//            count++;
//            logger.info("count#' '"+count);
//            if (index % 100 == 0) {
//                key = getKey();
//                logger.info("calling get key method index:" + " " + index + "key is ' ': " + key);
//            }
//
//
////      System.out.println("Url" + " " + permUrl);
////      System.out.println("permId" + " " + permId);
////      System.out.println("Staus:" + " " + status);
////      System.out.println("Ispublic:" + " " + isPblic);
////      System.out.println("ticker:" + " " + ticker);
////      System.out.println("--------");
//        }
//        System.out.println(count);
//        return true;
//    }
//
//    private List<String> getIssuer() {
//        return senatorsRepo.getIssuers();
//    }
//
//    private String getKey() {
//        String key = "";
//        try {
//            Connection.Response response = Jsoup.connect("https://permid.org/api/mdaas/ws/auth/getTempToken")
//                    .userAgent(userAgent)
//                    .followRedirects(true)
//                    .ignoreHttpErrors(true)
//                    .ignoreContentType(true)
//                    .header("Accept", "application/json")
//                    .header("Accept-Language", "en-US,en;q=0.9")
//                    .followRedirects(false)
//                    .method(Connection.Method.GET).timeout(1000000)
//                    .execute();
//            JSONObject data = new JSONObject(response.body());
//            key = data.getString("consumerKey");
//        } catch (IOException ex) {
//            logger.error(ex.getMessage());
//        }
//        return key;
//    }
//
//    private String[] getPermId(String consumerKey, String issuer) {
//        String[] data = new String[3];
//        String ticker = "not Available";
//        String permId = "";
//        try {
//            Connection.Response response = Jsoup
//                    .connect("https://permid.org/api/search?&num=5&q=" + issuer + "&selected=all&start=1")
//                    .userAgent(userAgent)
//                    .method(Connection.Method.GET)
//                    .header("Content-Type", "application/json")
//                    .followRedirects(true)
//                    .ignoreHttpErrors(true)
//                    .ignoreContentType(true)
//                    .header("Accept", "application/json")
//                    .header("Accept-Language", "en-US,en;q=0.9")
//                    .followRedirects(false)
//                    .header("X-AG-Access-Token", consumerKey).timeout(1000000)
//                    .execute();
//            String res = response.body();
//            JSONObject announcements = new JSONObject(response.body());
//            String test = "";
//            JSONArray jsonArr = announcements.getJSONObject("result").getJSONObject("organizations").getJSONArray("entities");
//            if (response.statusCode() == 400 || jsonArr.length() == 0) {
//                return null;
//            }
//            String permUrl = jsonArr.getJSONObject(0).getString("@id");
//            if (jsonArr.getJSONObject(0).has("primaryTicker")) {
//                ticker = jsonArr.getJSONObject(0).getString("primaryTicker");
//            }
//            permId = permUrl.substring(21);
//            data[0] = ticker;
//            data[1] = permId;
//            data[2] = permUrl;
//        } catch (IOException ex) {
//            logger.error(ex.getMessage());
//        }
//        return data;
//    }
//
//    private String[] getPermStatus(String consumerKey, String permId) {
//        if (permId == null) return null;
//        String[] data = new String[2];
//        String tickerUrl = "https://permid.org/api/mdaas/getEntityById/" + permId;
//        try {
//            Connection.Response response = Jsoup.connect(tickerUrl)
//                    .userAgent(userAgent)
//                    .method(Connection.Method.GET)
//                    .header("Content-Type", "application/json")
//                    .followRedirects(true)
//                    .ignoreHttpErrors(true)
//                    .ignoreContentType(true)
//                    .header("Accept", "application/json")
//                    .header("Accept-Language", "en-US,en;q=0.9")
//                    .followRedirects(false)
//                    .header("X-AG-Access-Token", consumerKey).timeout(1000000)
//                    .execute();
//            JSONObject result = new JSONObject(response.body());
//            String status = String.valueOf(result.getJSONArray("Status").get(0));
//            String isPblic = String.valueOf(result.getJSONArray("Public").get(0));
//            data[0] = status;
//            data[1] = isPblic;
//        } catch (IOException ex) {
//            logger.error(ex.getMessage());
//        }
//        return data;
//    }
//
//    private void writeToDB(String permUrl, String permId, String ticker, String status, String isPublic) {
//        senatorsRepo.insertPermData(permUrl, permId, ticker, status, isPublic);
//    }
//
//}

//REpo methods for perm

//    int insertPermData(String permUrl,String permid,String ticker,String status,String isPublic) {
//
//        String insertQuery = """INSERT INTO "Senators_PermId_Data" ("permUrl","permId", "ticker", "status","isPublic","rowCreation")
//                                                      VALUES (?,?, ?, ?,?, now())
//                                                      ON CONFLICT ("permId") DO NOTHING;"""
//        jdbcTemplate.update(insertQuery, permUrl, permid, ticker, status,isPublic)
//    }


//    List<String> getIssuers() {
//        List<String> issuerData=new ArrayList<>();
//
//        String query = '''SELECT issuer FROM "senators_Data"'''
//        issuerData = jdbcTemplate.queryForList(query, String.class)
//
//        return issuerData;
//
//    }

// controller method for perm

//    @GetMapping("/senatorpermdata")
//    public boolean senatorpermdata() {
//        return senatorsPermData.scrapeData();
//    }