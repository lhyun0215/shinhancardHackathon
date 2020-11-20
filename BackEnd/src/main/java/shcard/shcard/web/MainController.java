package shcard.shcard.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shcard.shcard.model.domain.RestAreas;
import shcard.shcard.model.domain.RestaurantLists;
import shcard.shcard.model.service.RestAreaService;
import shcard.shcard.model.service.RestaurantService;
import shcard.shcard.model.util.NaverKey;
import shcard.shcard.model.util.SearchLocation;

import java.io.*;
import java.net.*;
import java.util.*;


@RestController
@CrossOrigin(origins = {"*"}, maxAge = 6000)
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private static RestAreaService restAreaService;
    private static RestaurantService restaurantService;
    private static SearchLocation searchLocation;
    private static NaverKey naver = new NaverKey();
    private static String keyId = naver.getKeyId();
    private static String key = naver.getKey();


    public MainController(RestAreaService restAreaService,  RestaurantService restaurantService) {
        this.restAreaService = restAreaService;
        this.restaurantService = restaurantService;
    }


    @GetMapping("trafast/map")
    public static Map<String, Object> trafastMap(@RequestParam("start") String start, @RequestParam("end") String end) {
        String[] startPoint = null;
        String[] destination = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            startPoint = SearchLocation.searchXY(start);
            destination = SearchLocation.searchXY(end);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String apiURL = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?" +
                "start=" + startPoint[0] + "," + startPoint[1] +
                "&goal=" + destination[0] + "," + destination[1] +
                "&option=trafast";
        URL url = null;
        HttpURLConnection conn = null;
        JSONObject json = null;
        try {
            url = new URL(apiURL);
            conn = (HttpURLConnection) url.openConnection();
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

            JSONObject jsonObject = (JSONObject) jsonParser.parse(data);
            List<RestAreas> restList = restAreaService.findAll();
            Collections.sort(restList);

            json = (JSONObject) jsonParser.parse(String.valueOf(jsonObject.get("route")));
            JSONArray array = new JSONArray();
            array.add(json);
            JSONObject tmp = (JSONObject)array.get(0);
            JSONArray array1 = (JSONArray) tmp.get("trafast");
            JSONObject tmp2 = (JSONObject)array1.get(0);
            JSONArray array2 = (JSONArray) tmp2.get("path");
            double[][]naverArray = new double[array2.size()][2];
            for(int i=0;i<array2.size();i++){
                String js = array2.get(i).toString();
                js = js.replace("[","").replace("]","");
                int idx = js.indexOf(",");
                double lng = Double.parseDouble(js.substring(0,idx));
                double lat = Double.parseDouble(js.substring(idx+1));
                naverArray[i][0] = lat;
                naverArray[i][1]= lng;
            }

            ArrayList<RestAreas> arr = new ArrayList<>();
            TreeSet<String> set = new TreeSet<>();
            for(int i =0; i<naverArray.length;i++){
                double dist =0;
                for(int j =0; j<restList.size();j++){
                    RestAreas rest = restList.get(j);
                    dist= searchLocation.distance(naverArray[i][0],naverArray[i][1],rest.getLat(),rest.getLng());
                    if(dist<=3.0){
                        if(!set.contains(rest.getName())){
                            if(set.size()==5)
                                break;
                            set.add(rest.getName());
                            arr.add(rest);
                        }
                    }
                }
            }
            set.clear();


            result.put("nevi",jsonObject);
            result.put("list",arr);
            result.put("start",startPoint);


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    @GetMapping("traavoidtoll/map")
    public static Map<String, Object> traavoidtollMap(@RequestParam("start") String start, @RequestParam("end") String end) {
        String[] startPoint = null;
        String[] destination = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            startPoint = searchLocation.searchXY(start);
            destination = searchLocation.searchXY(end);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String apiURL = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?" +
                "start=" + startPoint[0] + "," + startPoint[1] +
                "&goal=" + destination[0] + "," + destination[1] +
                "&option=traavoidtoll";
        URL url = null;
        HttpURLConnection conn = null;
        JSONObject json = null;
        try {
            url = new URL(apiURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", keyId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY",  key);
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

            JSONObject jsonObject = (JSONObject) jsonParser.parse(data);
            List<RestaurantLists> restList = restaurantService.findAll();
            json = (JSONObject) jsonParser.parse(String.valueOf(jsonObject.get("route")));
            JSONArray array = new JSONArray();
            array.add(json);
            JSONObject tmp = (JSONObject)array.get(0);
            JSONArray array1 = (JSONArray) tmp.get("traavoidtoll");
            JSONObject tmp2 = (JSONObject)array1.get(0);
            JSONArray array2 = (JSONArray) tmp2.get("path");
            double[][]naverArray = new double[array2.size()][2];
            for(int i=0;i<array2.size();i++){
                String js = array2.get(i).toString();
                js = js.replace("[","").replace("]","");
                int idx = js.indexOf(",");
                double lng = Double.parseDouble(js.substring(0,idx));
                double lat = Double.parseDouble(js.substring(idx+1));
                naverArray[i][0] = lat;
                naverArray[i][1]= lng;
            }

            ArrayList<RestaurantLists> arr = new ArrayList<>();
            TreeSet<String> set = new TreeSet<>();
            for(int i =0; i<naverArray.length;i++){
                double dist =0;
                for(int j =0; j<restList.size();j++){
                    RestaurantLists rest = restList.get(j);
                    dist= searchLocation.distance(naverArray[i][0],naverArray[i][1],rest.getLat(),rest.getLng());
                    if(dist<=3.0){
                        if(!set.contains(rest.getPlcNm())){
                            set.add(rest.getPlcNm());
                            arr.add(rest);
                        }
                    }
                }
            }
            set.clear();


            result.put("nevi",jsonObject);
            result.put("list",arr);
            result.put("start",startPoint);


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return result;
    }




}