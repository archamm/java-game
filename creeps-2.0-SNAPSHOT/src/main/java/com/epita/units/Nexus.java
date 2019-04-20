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

    public SpawnReport sendCommandGetReport(String cmd, int waitTime) throws ExecutionException, InterruptedException {

        ScheduledFuture<SpawnReport> report = this.game.getTpe().schedule(new CallableReport(this, this.sendCommand(cmd).reportId)
                , waitTime, TimeUnit.SECONDS);

        SpawnReport report1 =  report.get();
        return report1;
    }

    public SpawnReport initProbe() throws UnirestException, ExecutionException, InterruptedException {
        return sendCommandGetReport("/spawn:probe", 3);

    }

    public SpawnReport initDragoon() throws UnirestException, ExecutionException, InterruptedException {
        return sendCommandGetReport("/spawn:dragoon", 2);

    }

    public SpawnReport initObserver() throws UnirestException, ExecutionException, InterruptedException {
        return sendCommandGetReport("/spawn:observer", 5);

    }




}
