package com.epita;

import com.epita.creeps.given.vo.response.InitResponse;
import com.epita.tools.GenericRequest;
import com.epita.units.Nexus;
import com.epita.units.Probe;
import com.epita.units.Unit;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 21:25
 */


public class Game
{
    String url;
    String login;
    String commitHash;
    ScheduledThreadPoolExecutor tpe;
    List <Unit> unitList;



    public Game(String url, String login, String commitHash) throws UnirestException, ExecutionException, InterruptedException {
        this.url = url;
        this.login = login;
        this.commitHash = commitHash;
        this.tpe = new ScheduledThreadPoolExecutor(8);
        this.unitList = new ArrayList<>();



        InitResponse b = GenericRequest.genericPost(url + "/init/" + this.login + "-" + this.commitHash
                , InitResponse.class);

        unitList.add(new Nexus(this, b.coordinates, b.baseId));
        unitList.add(new Probe(this, b.coordinates, b.probeId));

        for (Unit u : unitList)
        {
            if (u instanceof Nexus)
            {
                System.out.println(((Nexus) u).initProbe().toString());
            }
            if (u instanceof Probe)
            {
                System.out.println(((Probe) u).moveUnitWest().toString());
            }
        }


    }

    public List<Unit> getUnitList() {
        return unitList;
    }

    public String getUrl() {
        return url;
    }

    public String getLogin() {
        return login;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public ScheduledThreadPoolExecutor getTpe() {
        return tpe;
    }
}
