package com.epita.units;

import com.epita.Game;
import com.epita.creeps.given.vo.report.MoveReport;
import com.epita.creeps.given.vo.report.Report;
import com.epita.creeps.given.vo.report.SpawnReport;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.tools.CallableReport;
import com.epita.tools.GenericRequest;
import com.mashape.unirest.http.exceptions.UnirestException;

import com.epita.creeps.given.vo.geometry.Point;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
    public SpawnReport sendCommandGetReport2(String cmd, int waitTime) throws ExecutionException, InterruptedException {

        ScheduledFuture<SpawnReport> report = this.game.getTpe().schedule(new CallableReport(this, this.sendCommand(cmd).reportId)
                , waitTime, TimeUnit.SECONDS);
        return report.get();
    }

    public SpawnReport initNexus() throws UnirestException, ExecutionException, InterruptedException {
        return sendCommandGetReport2("/spawn:nexus", 10);
    }

    public SpawnReport initPhotonCanon() throws UnirestException, ExecutionException, InterruptedException {
        return sendCommandGetReport2("spawn:photoncannon", 5);

    }



}
