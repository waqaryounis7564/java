package com.company;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermData {
    private int count = 0;
    private  static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36 Edg/83.0.478.44";

    public static boolean scrapeData() {
        String key = getKey();
        List<String> issuers = getIssuer();
        String issuer = "";
        for (int index = 0; index < issuers.size(); index++) {
            System.out.println("key is' '" + key);
            issuer = issuers.get(index);
            System.out.println("index is ' ':" + index + "' ' & issuer is:' '" + issuer);
            if (issuer.isEmpty()) continue;
            String[] data1 = getPermId(key, issuer);
            if (data1 == null) {
                continue;
            }
            String ticker = data1[0];
            String permId = data1[1];
            String permUrl = data1[2];
            String[] data2 = getPermStatus(key, permId);
            if (data2 == null) {
                continue;
            }
            String status = data2[0];
            String isPblic = data2[1];
            System.out.println("Url" + " " + permUrl);
            System.out.println("permId" + " " + permId);
            System.out.println("Staus:" + " " + status);
            System.out.println("Ispublic:" + " " + isPblic);
            System.out.println("ticker:" + " " + ticker);
            System.out.println("--------");
        }
return true;
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
                    .method(Connection.Method.GET).timeout(1000000)
                    .execute();
            JSONObject data = new JSONObject(response.body());
            key = data.getString("consumerKey");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return key;
    }

    private static String[] getPermStatus(String consumerKey, String permId) {
        if (permId == null) return null;
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
                    .header("X-AG-Access-Token", consumerKey).timeout(1000000)
                    .execute();
            JSONObject result = new JSONObject(response.body());
            String status = String.valueOf(result.getJSONArray("Status").get(0));
            String isPblic = String.valueOf(result.getJSONArray("Public").get(0));
            data[0] = status;
            data[1] = isPblic;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return data;
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
                    .header("X-AG-Access-Token", consumerKey).timeout(1000000)
                    .execute();
            String res = response.body();
            JSONObject announcements = new JSONObject(response.body());
            String test = "";
            JSONArray jsonArr = announcements.getJSONObject("result").getJSONObject("organizations").getJSONArray("entities");
            if (response.statusCode() == 400 || jsonArr.length() == 0) {
                return null;
            }
            String permUrl = jsonArr.getJSONObject(0).getString("@id");
            if (jsonArr.getJSONObject(0).has("primaryTicker")) {
                ticker = jsonArr.getJSONObject(0).getString("primaryTicker");
            }
            permId = permUrl.substring(21);
            data[0] = ticker;
            data[1] = permId;
            data[2] = permUrl;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return data;
    }

    private static List<String> getIssuer() {
        String[] list = new String[]{"issuer",
                "Little Star Partnership",
                "Buhler Telferner Partnership",
                "Starboard Corporation - Commercial Real Estate",
                "McKinney Texas Indpt Sch Dist Rate",
                "CP Kent Place Member LLC",
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
}
