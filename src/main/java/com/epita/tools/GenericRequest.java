package com.epita.tools;

import com.epita.creeps.given.json.Json;
import com.epita.creeps.given.vo.report.SpawnReport;
import com.epita.creeps.given.vo.response.InitResponse;
import com.epita.creeps.given.vo.response.StatisticsResponse;
import com.epita.creeps.given.vo.response.StatusResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 18:04
 */
public class GenericRequest
{
    static public <OBJECT_TYPE> OBJECT_TYPE genericGet (String url, Class<OBJECT_TYPE> objectType) throws UnirestException {
        try {
            HttpResponse<JsonNode> response = Unirest.get(url   ).asJson();
            String str = response.getBody().toString();
            return Json.parse(str , objectType);
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }



    static public <OBJECT_TYPE> OBJECT_TYPE genericPost (String url, Class<OBJECT_TYPE> objectType) throws UnirestException {
        try {
            HttpResponse<JsonNode> response = Unirest.post(url).asJson();
            String str = response.getBody().toString();
            return Json.parse(str , objectType);
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }

    }
}
