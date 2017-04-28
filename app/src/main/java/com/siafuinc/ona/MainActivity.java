package com.siafuinc.ona;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Test;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    public String TAG =MainActivity.class.getSimpleName();
    public static String WATER_POINT_FUNCTIONING="yes";
    public static String WATER_POINT_CONDITION_BROKEN="broken";
    public static int TOTAL_NUMBER_OF_BROKEN_WATER_POINTS=0;
    public int NUMBER_OF_WATER_POINT_FUNCTIONING=0;
    public int NUMBER_OF_WATER_POINT_CONDITION_BROKEN=1;

    @BindView(R2.id.textView_message) TextView tvMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tvMessage.setText(" Welcome It's prefarable if you could log from the android monitor. \n Tap to Request for refined infor ");

        tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processJson("https://raw.githubusercontent.com/onaio/ona-tech/master/data/water_points.json");
            }
        });


    }
    public void processJson(final String jsonUrl){



        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {

                    try {
                        String json = IOUtils.toString(new URL(jsonUrl), Charset.forName("UTF-8"));

                        if (isValidJSON(json)){
                            Log.e("THE JSON IS",json);
                            final ObjectMapper objectMapper = new ObjectMapper();
                            List<InfraResources> infraResourcesList = objectMapper.readValue(json, new TypeReference<List<InfraResources>>(){});

                            final HashMap<String, Integer> hashMapCommunityWaterPoints = new HashMap<String, Integer>();
                            final HashMap<String, Integer> hashMapPercCommunityBrokenWaterPoints = new HashMap<String, Integer>();

                            for(InfraResources infraResources:infraResourcesList){
                                if(infraResources.getWater_functioning().equals(WATER_POINT_FUNCTIONING)){
                                    NUMBER_OF_WATER_POINT_FUNCTIONING+=1;
                                }
                                //TODO HERE WE WANT TO GET THE TOTAL NUMBER OF WATERPOINTS per communities
                                if(hashMapCommunityWaterPoints.containsKey(infraResources.getCommunities_villages())) {
                                    hashMapCommunityWaterPoints.put(infraResources.getCommunities_villages(), hashMapCommunityWaterPoints.get(infraResources.getCommunities_villages())+1);
                                }else{ hashMapCommunityWaterPoints.put(infraResources.getCommunities_villages(), 1); }

                                //TODO HERE WE GET THE BROKEN WATERPOINTS SO NOW WE NEED TO GET THE TWO HARSHMAPS AND DO SOME ARITHMETICS


                                if (infraResources.getWater_point_condition().equals(WATER_POINT_CONDITION_BROKEN)){
                                    TOTAL_NUMBER_OF_BROKEN_WATER_POINTS+=1;
                                    Log.e(TAG,"areas broken:"+infraResources.getCommunities_villages());
                                    if (hashMapPercCommunityBrokenWaterPoints.containsKey(infraResources.getCommunities_villages())){
                                        hashMapPercCommunityBrokenWaterPoints.put(infraResources.getCommunities_villages(),hashMapPercCommunityBrokenWaterPoints.get(infraResources.getCommunities_villages())+1);
                                    }else{
                                        hashMapPercCommunityBrokenWaterPoints.put(infraResources.getCommunities_villages(), 1);
                                    }
                                }else {
                                    if (hashMapPercCommunityBrokenWaterPoints.containsKey(infraResources.getCommunities_villages())){
                                    }else{
                                        NUMBER_OF_WATER_POINT_CONDITION_BROKEN=0 ;
                                        hashMapPercCommunityBrokenWaterPoints.put(infraResources.getCommunities_villages(), NUMBER_OF_WATER_POINT_CONDITION_BROKEN);
                                    }
                                }
                                Log.e(TAG,infraResources.getCommunities_villages());

                            }



                            //TODO TO MERGE AND MULTIPLY
                            final Map<String, Double> result = mergeMaps(hashMapCommunityWaterPoints, hashMapPercCommunityBrokenWaterPoints);
                            //TODO SORT THE OUTPUTS

                            //TODO HERE CREATE THE JSON OBJECT
                            String jsonMap= new ObjectMapper().writeValueAsString(sortByValue(hashMapCommunityWaterPoints));
                            String jsonMapBroken= new ObjectMapper().writeValueAsString(sortByValue(hashMapPercCommunityBrokenWaterPoints));
                            String jsonPercentageBrokenWater=new ObjectMapper().writeValueAsString(sortByValue(result));

                            Log.d(TAG,"number_functional:"+NUMBER_OF_WATER_POINT_FUNCTIONING);
                            Log.d(TAG,"total broken points:"+TOTAL_NUMBER_OF_BROKEN_WATER_POINTS);
                            Log.d(TAG,"number_water_points:"+jsonMap);
                            Log.d(TAG,"number_of_water_points_broken"+jsonMapBroken);

                            Log.d(TAG,"{number_of_functional_water_points:"+NUMBER_OF_WATER_POINT_FUNCTIONING+"," +
                                    " number_of_water_points:"+jsonMap+","+
                                    "number_of_broken_water_points:"+jsonMapBroken+
                                    "}");

                            Log.e(TAG,"really?:"+jsonPercentageBrokenWater);

                            //TODO THE REQUIRED FINAL JSON
                            Log.i(TAG+"FINAL_JSON","{number_of_functional_water_points:"+NUMBER_OF_WATER_POINT_FUNCTIONING+"," +
                                    " number_of_water_points:"+jsonMap+","+
                                    "number_of_broken_water_points:"+jsonMapBroken+","+
                                    "community_ranking_in_percentage:"+jsonPercentageBrokenWater+
                                    "}");

                            updateTextView("Successfully calculated please log tag FINAL_JSON");

                        }else{

                            Log.wtf(TAG,"OOPS! Something wrong with your Url or endpoint Please Check internet");
                            updateTextView("OOPS! Something is wrong with your Url or endpoint Please Check internet connection");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        updateTextView("OOPS! Something is wrong with your Url or endpoint Please Check internet connection");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    updateTextView("OOPS! Something is wrong with your Url or endpoint Please Check internet connection");

                }
            }
        });

        thread.start();

    }
    public void updateTextView(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvMessage.setText(message);
            }
        });

    }
    public static boolean isValidJSON(final String json) throws IOException {
        boolean valid = true;
        try{
            ObjectMapper objectMapper=new ObjectMapper();
            objectMapper.readTree(json);
        } catch(JsonProcessingException e){
            valid = false;
        }
        return valid;
    }
    public Map sortByValue(Map map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o2, Object o1) {
                return ((Comparable) ((Map.Entry) (o1)).getValue()) .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next(); result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public Map<String, Double> mergeMaps(Map<String, Integer>... maps) {
        final Map<String, Double> resultMap = new HashMap<>();
        for (final Map<String, Integer> map : maps) {
            for (final String key : map.keySet()) {
                final double value;
                if (resultMap.containsKey(key)) {
                    final double existingValue = resultMap.get(key);
                    Log.e(TAG,"1st array value :"+existingValue+" 2nd :"+map.get(key));
                    if (map.get(key)==0){
                        value=0;
                    }else{
                        value = ( map.get(key)/existingValue)*100;
                    }
                    Log.e(TAG,"percentage is:"+value);
                }
                else {
                    value = map.get(key);
                }
                resultMap.put(key, value);
            }
        }
        return resultMap;
    }
}
