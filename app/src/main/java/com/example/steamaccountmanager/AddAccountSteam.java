package com.example.steamaccountmanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */

public class AddAccountSteam extends Fragment {

    Button steam_id;
    TextView steam_id_set;
    EditText steam_id_get;
    ImageView imageView;

    String steam;

    String steam_id_64;
    String SteamId = null;
    String background_url = null;

    public RequestQueue mQueue, idQueue;

    public AddAccountSteam() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_account_steam, container, false);

        steam_id = view.findViewById(R.id.steam_id);
        steam_id_set = view.findViewById(R.id.steam_id_set);
        steam_id_get = view.findViewById(R.id.steam_id_get);
        imageView = view.findViewById(R.id.imageView);

        steam = null;
        steam_id_64 = null;

        mQueue = Volley.newRequestQueue(getActivity());
        idQueue = Volley.newRequestQueue(getActivity());

        steam_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
//                steam = steam_id_get.getText().toString();
//                steam_id_set.setText(steam);
                steam_id_64 = steam_id_get.getText().toString();
                getBanInfo(steam_id_64)
                try {
                    getBackgroundURL();
                } catch (IOException e){
                    Toast.makeText(getActivity(), "da", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void getBanInfo(String steam_id){

//        String url = "https://api.myjson.com/bins/kp9wz";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray jsonArray = response.getJSONArray("employees");
//
//                            for (int i = 0; i < jsonArray.length(); i++){
//                                JSONObject employee = jsonArray.getJSONObject(i);
//
//                                String firstName = employee.getString("firstname");
//                                int age = employee.getInt("age");
//                                String mail = employee.getString("mail");
//
//                                steam_id_set.append(firstName + ", " + String.valueOf(age) + ", "+mail+"\n\n");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        mQueue.add(request);
            if(!steam_id.matches("[0-9]{17}")){
                steam_id = getSteamID64(steam_id)
            }
            String url = "https://api.steampowered.com/ISteamUser/GetPlayerBans/v1/?key=0997EAA83C80BF503C794A872FD94064&steamids=" + steam_id;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("players");

                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject player = jsonArray.getJSONObject(i);

                                    String SteamId = player.getString("SteamId");
                                    boolean CommunityBanned = player.getBoolean("CommunityBanned");
                                    boolean VACBanned = player.getBoolean("VACBanned");
                                    int NumberOfVACBans = player.getInt("NumberOfVACBans");
                                    int DaysSinceLastBan = player.getInt("DaysSinceLastBan");
                                    int NumberOfGameBans = player.getInt("NumberOfGameBans");
                                    String EconomyBan = player.getString("EconomyBan");

                                    steam_id_set.append(
                                            "SteamID - "+String.valueOf(SteamId) + ", \n"+
                                                    "CommunityBanned - "+String.valueOf(CommunityBanned)+ ", \n"+
                                                    "VACBanned - "+String.valueOf(VACBanned)+ ", \n"+
                                                    "NumberOfVACBans - "+String.valueOf(NumberOfVACBans)+ ", \n"+
                                                    "DaysSinceLastBan - "+String.valueOf(DaysSinceLastBan)+ ", \n"+
                                                    "NumberOfGameBans - "+String.valueOf(NumberOfGameBans)+ ", \n"+
                                                    "EconomyBan - "+String.valueOf(EconomyBan)+ ", \n");

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            mQueue.add(request);
    }

    private String getSteamID64(String steam_id){
        String temp_url = "https://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=0997EAA83C80BF503C794A872FD94064&vanityurl="+steam_id_64;

        JsonObjectRequest temp_request = new JsonObjectRequest(Request.Method.GET, temp_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject response_id = response.getJSONObject("response");
                            String id64 = response_id.getString("steamid");
                            return id64;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(temp_request);

    }

    public String getText(String url) throws IOException {
        URL url2 = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) url2.openConnection();
        //add headers to the connection, or check the status if desired..
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10);
        connection.setUseCaches(true);
        // handle error response code it occurs
        int responseCode = connection.getResponseCode();
        InputStream inputStream;
        if (200 <= responseCode && responseCode <= 299) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        inputStream));

        StringBuilder response = new StringBuilder();
        String currentLine;

        while ((currentLine = in.readLine()) != null)
            response.append(currentLine);

        in.close();

        return response.toString();
    }


    public void getBackgroundURL() throws IOException {
        String profile_response = getText("https://steamcommunity.com/id/kojimaone/");

        Pattern p = Pattern.compile("https://steamcdn-a[.]akamaihd[.]net/steamcommunity/public/images/items/[0-9]{6}/[0-9a-z]{40}[.]jpg");
        // create matcher for pattern p and given string
        Matcher m = p.matcher(profile_response);

        // if an occurrence if a pattern was found in a given string...
        if (m.find()) {
            background_url = m.group(0);
            Glide.with(getActivity())
                    .load(background_url)
                    .into(imageView);
        }
    }


}
