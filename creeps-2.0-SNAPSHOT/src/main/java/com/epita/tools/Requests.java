package com.epita.tools;

import com.epita.creeps.given.vo.report.Report;
import com.epita.creeps.given.vo.response.StatusResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.concurrent.CompletableFuture;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 19:10
 */
public class Requests {

    static public boolean isRunning(String url) throws UnirestException {
        StatusResponse res =  GenericRequest.genericGet(url + "/status", StatusResponse.class);
        if (res != null)
            return res.running;
        return false;

    }


}
