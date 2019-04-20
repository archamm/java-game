package com.epita.units;

import com.epita.creeps.given.vo.report.MoveReport;
import com.epita.creeps.given.vo.report.Report;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.tools.CallableReport;
import com.epita.tools.GenericRequest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 21:22
 */
public class MovingUnits extends Unit
{
    public MoveReport sendCommandGetReport(String cmd, int waitTime) throws ExecutionException, InterruptedException {
        
        ScheduledFuture<MoveReport> report = this.game.getTpe().schedule(new CallableReport(this, this.sendCommand(cmd).reportId)
                , waitTime, TimeUnit.SECONDS);
        return report.get();
    }
    public MoveReport moveUnitUp() throws UnirestException, ExecutionException, InterruptedException {
        return sendCommandGetReport( "/move:up", 1);

    }

    public MoveReport moveUnitDown() throws UnirestException, ExecutionException, InterruptedException {
        return sendCommandGetReport("/move:down", 1);

    }

    public MoveReport moveUnitEast() throws UnirestException, ExecutionException, InterruptedException {
        return sendCommandGetReport("/move:east", 1);

    }

    public MoveReport moveUnitWest() throws UnirestException, ExecutionException, InterruptedException {
        return sendCommandGetReport("/move:east", 1);

    }

    public MoveReport moveUnitNort() throws UnirestException, ExecutionException, InterruptedException {
        return sendCommandGetReport("/move:east", 1);

    }

    public MoveReport moveUnitSouth() throws UnirestException, ExecutionException, InterruptedException {
        return sendCommandGetReport("/move:east", 1);

    }

}
