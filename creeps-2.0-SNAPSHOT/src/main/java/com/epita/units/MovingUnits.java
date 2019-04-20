package com.epita.units;

import com.epita.callables.CallableInspectReport;
import com.epita.callables.CallableMoveReport;
import com.epita.callables.CallableScanReport;
import com.epita.creeps.given.vo.Block;
import com.epita.creeps.given.vo.geometry.Hexahedron;
import com.epita.creeps.given.vo.geometry.Point;
import com.epita.creeps.given.vo.report.MoveReport;
import com.epita.creeps.given.vo.report.ScanReport;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
            searchAndReplaceCoordinates(report.agentLocation);
            return true;
        }
        return false;
    }

    public boolean moveUnitDown() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:down", 1);
        if (report.status == SUCCESS)
        {
            searchAndReplaceCoordinates(report.agentLocation);
            return true;
        }
        return false;
    }

    public boolean moveUnitEast() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:east", 1);
        if (report.status == SUCCESS)
        {
            searchAndReplaceCoordinates(report.agentLocation);
            return true;
        }
        return false;
    }

    public boolean moveUnitWest() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:west", 1);
        if (report.status == SUCCESS)
        {
            searchAndReplaceCoordinates(report.agentLocation);
            return true;
        }
        return false;
    }

    public boolean moveUnitNorth() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:north", 1);
        if (report.status == SUCCESS)
        {
            searchAndReplaceCoordinates(report.agentLocation);
            return true;
        }
        return false;
    }

    public boolean moveUnitSouth() throws UnirestException, ExecutionException, InterruptedException {
        MoveReport report = sendCommandGetMoveReport( "/move:south", 1);
        if (report.status == SUCCESS)
        {
            searchAndReplaceCoordinates(report.agentLocation);
            return true;
        }
        return false;
    }

    public ScanReport sendCommandGetScanReport(String cmd, int waitTime) throws ExecutionException, InterruptedException {

        ScheduledFuture<ScanReport> report = this.game.getTpe().schedule(new CallableScanReport(this, this.sendCommand(cmd).reportId)
                , waitTime, TimeUnit.SECONDS);
            return report.get();
    }

    public boolean findAnyMineralMine()
    {
        return this.game.getLblock().stream().anyMatch(b -> b.element != null);
    }

    public void goToBlock(Point location) throws InterruptedException, ExecutionException, UnirestException {
        System.out.println(location.toString());
        System.out.println(this.coordinates.toString());
        while (!this.coordinates.equals(location))
        {
            System.out.println(location.toString());
            System.out.println(this.coordinates.toString());
            int disx = location.x - this.coordinates.x;
            int disy = location.y - this.coordinates.y;
            int disz = location.z - this.coordinates.z;
            if (Math.abs(disx) >= Math.abs(disy) && Math.abs(disx) >= Math.abs(disz))
            {
                if (disx > 0)
                    this.moveUnitEast();
                else
                    this.moveUnitWest();
            }
            if (Math.abs(disy) >= Math.abs(disx) && Math.abs(disy) >= Math.abs(disz))
            {
                if (disy < 0)
                    this.moveUnitDown();
                else
                    this.moveUnitUp();
            }
            else
            {
                if (disz < 0)
                    this.moveUnitSouth();
                else
                    this.moveUnitNorth();
            }

        }
    }

    public double distanceTo(Point location)
    {
        return Math.abs(location.x - this.coordinates.x) + Math.abs(location.y - this.coordinates.y)
                + Math.abs(location.z - this.coordinates.z);
    }
    public Hexahedron.HexahedronElement <Block> findestClosestNullBlock()
    {
        return game.getLblock().stream()
                .filter(b -> b.element == null || b.element == Block.UNKNOWN)
                .min(Comparator.comparingDouble(b -> distanceTo(b.location))).get();
    }

    public Nexus findClosestNexus()
    {
        return (Nexus) this.game.getUnitList().stream().filter(u -> u instanceof Nexus).min(Comparator.comparingDouble(b -> distanceTo(b.coordinates))).get();
    }

    public Hexahedron.HexahedronElement <Block> findClosest(List <Hexahedron.HexahedronElement <Block>> list)
    {
        return list.stream().min(Comparator.comparingDouble(b -> distanceTo(b.location))).get();
    }

}
