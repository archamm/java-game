package com.epita.units;

import com.epita.Game;
import com.epita.callables.CallableSpawnReport;
import com.epita.creeps.given.vo.report.MoveReport;
import com.epita.creeps.given.vo.report.Report;
import com.epita.creeps.given.vo.report.ScanReport;
import com.epita.creeps.given.vo.report.SpawnReport;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.callables.CallableReport;
import com.epita.tools.GenericRequest;
import com.mashape.unirest.http.exceptions.UnirestException;

import com.epita.creeps.given.vo.geometry.Point;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.epita.creeps.given.vo.report.Report.Status.SUCCESS;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 21:22
 */
public class Probe extends MovingUnits
{
    public Probe(Game game, Point coordinates, String agentId)
    {
        this.game = game;
        this.coordinates = coordinates;
        this.agentId = agentId;
        this.name = "probe";
    }


    public void initNexus() throws UnirestException, ExecutionException, InterruptedException {
        SpawnReport report =  sendCommandGetSpawnReport("/spawn:nexus", 10);
        if (report.status == SUCCESS)
            this.game.getUnitList().add(new Observer(this.game, report.agentLocation, report.spawnedAgentId));
    }

    public void initPhotonCanon() throws UnirestException, ExecutionException, InterruptedException {
        SpawnReport report = sendCommandGetSpawnReport("spawn:photoncannon", 5);
        if (report.status == SUCCESS)
            this.game.getUnitList().add(new Observer(this.game, report.agentLocation, report.spawnedAgentId));
    }



}
