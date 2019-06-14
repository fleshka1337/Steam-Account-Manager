package com.example.steamaccountmanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */

public class AddAccountSteam extends Fragment {

    Button steam_id;
    TextView steam_id_set;
    EditText steam_id_get;

    String steam;

    String steam_id_64;
    String SteamId = null;

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

        steam = null;
        steam_id_64 = null;

        mQueue = Volley.newRequestQueue(getActivity());
        idQueue = Volley.newRequestQueue(getActivity());

        steam_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                steam = steam_id_get.getText().toString();
//                steam_id_set.setText(steam);
                steam_id_64 = steam_id_get.getText().toString();
                jsonParse();
            }
        });

        return view;
    }

    private void jsonParse(){

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

            String url = "https://api.steampowered.com/ISteamUser/GetPlayerBans/v1/?key=0997EAA83C80BF503C794A872FD94064&steamids=" + steam_id_64;

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

    private void parse_steaid_64(){
        String temp_url = "http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=0997EAA83C80BF503C794A872FD94064&vanityurl="+steam_id_64;

        JsonObjectRequest temp_request = new JsonObjectRequest(Request.Method.GET, temp_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("response");

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject response_id = jsonArray.getJSONObject(i);

                                SteamId = response_id.getString("steamid");
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

        mQueue.add(temp_request);

        String url = "https://api.steampowered.com/ISteamUser/GetPlayerBans/v1/?key=0997EAA83C80BF503C794A872FD94064&steamids=" + SteamId;

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

}
