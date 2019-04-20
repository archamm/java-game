package com.epita.units;

import com.epita.callables.CallableInspectReport;
import com.epita.callables.CallableMoveReport;
import com.epita.creeps.given.vo.report.MoveReport;
import com.epita.creeps.given.vo.report.ScanReport;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.epita.creeps.given.vo.report.Report.Status.SUCCESS;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 21:22
 */
public class MovingUnits extends Unit
{
    public MoveReport sendCommandGetMoveReport(String cmd, int waitTime) throws ExecutionException, InterruptedException {
        
        ScheduledFuture<MoveReport> report = this.game.getTpe().schedule(new CallableMoveReport(this, this.sendCommand(cmd).reportId)
                , 1000 * waitTime / this.game.getTickrate(), TimeUnit.MILLISECONDS);
        return report.get();
    }

    public boolean moveUnitUp() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:up", 1);
        if (report.status == SUCCESS)
        {
            SearchAndReplaceCoordinates(report.agentLocation);
            return true;
        }
        return false;
    }

    public boolean moveUnitDown() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:down", 1);
        if (report.status == SUCCESS)
        {
            SearchAndReplaceCoordinates(report.agentLocation);
            return true;
        }
        return false;
    }

    public boolean moveUnitEast() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:east", 1);
        if (report.status == SUCCESS)
        {
            SearchAndReplaceCoordinates(report.agentLocation);
            return true;
        }
        return false;
    }

    public boolean moveUnitWest() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:west", 1);
        if (report.status == SUCCESS)
        {
            SearchAndReplaceCoordinates(report.agentLocation);
            return true;
        }
        return false;
    }

    public boolean moveUnitNorth() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:north", 1);
        if (report.status == SUCCESS)
        {
            SearchAndReplaceCoordinates(report.agentLocation);
            return true;
        }
        return false;
    }

    public boolean moveUnitSouth() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:south", 1);
        if (report.status == SUCCESS)
        {
            SearchAndReplaceCoordinates(report.agentLocation);
            return true;
        }
        return false;
    }

    public ScanReport sendCommandGetScanReport(String cmd, int waitTime) throws ExecutionException, InterruptedException {

        ScheduledFuture<ScanReport> report = this.game.getTpe().schedule(new CallableInspectReport(this, this.sendCommand(cmd).reportId)
                , waitTime, TimeUnit.SECONDS);
        return report.get();
    }

}
