package com.epita.units;

import com.epita.Game;
import com.epita.callables.CallableSpawnReport;
import com.epita.creeps.given.vo.report.MoveReport;
import com.epita.creeps.given.vo.report.Report;
import com.epita.creeps.given.vo.report.SpawnReport;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.callables.CallableReport;
import com.epita.tools.GenericRequest;
import com.mashape.unirest.http.exceptions.UnirestException;

import com.epita.creeps.given.vo.geometry.Point;

import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.epita.creeps.given.vo.report.Report.Status.SUCCESS;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 22:07
 */
public class Nexus extends Unit
{
    public Nexus(Game game, Point coordinates, String agentId)
    {
        this.game = game;
        this.coordinates = coordinates;
        this.agentId = agentId;
        this.name = "Nexus";
    }


    public boolean initProbe() throws UnirestException, ExecutionException, InterruptedException {
        SpawnReport report = sendCommandGetSpawnReport("/spawn:probe", 3);
        if (report.status == SUCCESS) {
            this.game.getUnitList().add(new Probe(this.game, report.agentLocation, report.spawnedAgentId));
            return true;
        }
        return false;


    }

    public boolean initDragoon() throws UnirestException, ExecutionException, InterruptedException {
        SpawnReport report = sendCommandGetSpawnReport("/spawn:dragoon", 2);
        if (report.status == SUCCESS) {
            this.game.getUnitList().add(new Dragoon(this.game, report.agentLocation, report.spawnedAgentId));
            return true;
        }
        return false;
    }

    public boolean initObserver() throws UnirestException, ExecutionException, InterruptedException {
        SpawnReport report =  sendCommandGetSpawnReport("/spawn:observer", 5);
        if (report.status == SUCCESS) {
            this.game.getUnitList().add(new Observer(this.game, report.agentLocation, report.spawnedAgentId));
            return true;
        }
        return false;
    }






}
