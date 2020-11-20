package shcard.shcard.model.util;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SearchLocation {

    private static NaverKey naver = new NaverKey();
    private static String keyId = naver.getKeyId();
    private static String key = naver.getKey();

    public static double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return dist;
    }


    // This function converts decimal degrees to radians
    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    public static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static String[] searchXY(String query) throws UnsupportedEncodingException {
        String addr = URLEncoder.encode(query, "utf-8");
        String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr;

        String x = "";
        String y = "";

        try {
            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", keyId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", key);
            conn.setDoOutput(true);

            InputStream is = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is), 8 * 1024);

            String line = null;
            StringBuffer buff = new StringBuffer();

            while ((line = in.readLine()) != null) {
                buff.append(line + "\n");
            }
            String data = buff.toString().trim();
            JSONParser jsonParser = new JSONParser();
            JSONObject json = (JSONObject) jsonParser.parse(data);
            JSONArray jsonArray = (JSONArray) json.get("addresses");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject json2 = (JSONObject) jsonArray.get(i);
                if (null != json2.get("x")) {
                    x = json2.get("x").toString();
                }
                if (null != json2.get("y")) {
                    y = json2.get("y").toString();
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }


        return new String[]{x, y};
    }
}
