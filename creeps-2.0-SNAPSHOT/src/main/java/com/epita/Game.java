package com.epita;

import com.epita.creeps.given.extra.Cartographer;
import com.epita.creeps.given.vo.response.InitResponse;
import com.epita.creeps.given.vo.response.StatisticsResponse;
import com.epita.creeps.given.vo.response.StatusResponse;
import com.epita.tools.GenericRequest;
import com.epita.units.Nexus;
import com.epita.units.Probe;
import com.epita.units.Unit;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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
    int tickrate;
    int ticks;
    int minerals;
    int biomass;
    int warmUpTime;

    List <Unit> unitList;

    public void initGame() throws UnirestException {
        InitResponse b = GenericRequest.genericPost(url + "/init/" + this.login + "-" + this.commitHash
                , InitResponse.class);
        this.tickrate = (int) b.setup.ticksPerSeconds;
        System.out.println(this.tickrate);

        StatisticsResponse statisticsResponse = getStatistics();
        this.warmUpTime = statisticsResponse.scheduledGameStartTick;
        Cartographer.initialize(statisticsResponse.dimension);

        unitList.add(new Nexus(this, b.coordinates, b.baseId));
        unitList.add(new Probe(this, b.coordinates, b.probeId));
    }

    public boolean isRunning() throws UnirestException {
        StatusResponse res = GenericRequest.genericGet(this.url + "/status", StatusResponse.class);
        if (res != null)
            return res.running;
        return false;
    }

    public StatisticsResponse getStatistics() throws UnirestException {
        StatisticsResponse res =  GenericRequest.genericGet(url + "/statistics", StatisticsResponse.class);
        return res;
    }

    public StatisticsResponse.PlayerStatsResponse getPlayerResponse() throws UnirestException {
        StatisticsResponse statisticsResponse = getStatistics();
        for (StatisticsResponse.PlayerStatsResponse e : statisticsResponse.players)
        {
            String name = this.login + "-" + commitHash;
            if (name.equals(e.name))
            {
                return e;
            }
        }
        return null;
    }

    public void gameLoop() throws InterruptedException, ExecutionException, UnirestException {
        while (isRunning())
        {
            StatisticsResponse.PlayerStatsResponse playerStatsResponse = getPlayerResponse();
            this.minerals = playerStatsResponse.minerals;
            this.biomass = playerStatsResponse.biomass;

            for (int i = 0; i < unitList.size(); i++) {
                Unit u = unitList.get(i);
                if (u instanceof Nexus) {
                    ((Nexus) u).initProbe();
                }
                if (u instanceof Probe) {
                    ((Probe) u).moveUnitWest();
                }
            }
        }

    }
    public Game(String url, String login, String commitHash) throws UnirestException, ExecutionException, InterruptedException {
        this.url = url;
        this.login = login;
        this.commitHash = commitHash;
        this.tpe = new ScheduledThreadPoolExecutor(8);
        this.unitList = new ArrayList<>();

        this.initGame();

        if(!isRunning()) {
            Thread.sleep(this.warmUpTime * 1000 / this.tickrate);
        }

        gameLoop();

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

    public int getTickrate() {
        return tickrate;
    }

    public int getMinerals() {
        return minerals;
    }

    public int getBiomass() {
        return biomass;
    }
}
