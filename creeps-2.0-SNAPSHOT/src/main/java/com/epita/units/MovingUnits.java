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

    public void moveUnitUp() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:up", 1);
        if (report.status == SUCCESS)
            SearchAndReplaceCoordinates(report.agentLocation);
    }

    public void moveUnitDown() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:down", 1);
        if (report.status == SUCCESS)
            SearchAndReplaceCoordinates(report.agentLocation);
    }

    public void moveUnitEast() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:east", 1);
        if (report.status == SUCCESS)
            SearchAndReplaceCoordinates(report.agentLocation);
    }

    public void moveUnitWest() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:west", 1);
        if (report.status == SUCCESS)
            SearchAndReplaceCoordinates(report.agentLocation);
    }

    public void moveUnitNorth() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:north", 1);
        if (report.status == SUCCESS)
            SearchAndReplaceCoordinates(report.agentLocation);
    }

    public void moveUnitSouth() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:south", 1);
        if (report.status == SUCCESS)
            SearchAndReplaceCoordinates(report.agentLocation);
    }

    public ScanReport sendCommandGetScanReport(String cmd, int waitTime) throws ExecutionException, InterruptedException {

        ScheduledFuture<ScanReport> report = this.game.getTpe().schedule(new CallableInspectReport(this, this.sendCommand(cmd).reportId)
                , waitTime, TimeUnit.SECONDS);
        return report.get();
    }

}
