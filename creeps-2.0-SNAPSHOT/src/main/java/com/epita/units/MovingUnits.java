package com.epita.units;

import com.epita.callables.CallableInspectReport;
import com.epita.callables.CallableMoveReport;
import com.epita.callables.CallableScanReport;
import com.epita.creeps.given.vo.Block;
import com.epita.creeps.given.vo.geometry.Hexahedron;
import com.epita.creeps.given.vo.geometry.Point;
import com.epita.creeps.given.vo.report.MoveReport;
import com.epita.creeps.given.vo.report.ScanReport;
import com.epita.creeps.given.vo.report.SpawnReport;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.tools.GenericRequest;
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
    public void sendCommandGetMoveReport(String move, int waitTime) throws ExecutionException, InterruptedException {
        if (action)
            return;
        action = true;
        CommandResponse response = sendCommand("/move:" + move);
        this.game.getTpe().schedule(() -> {
                    try {
                        MoveReport res = GenericRequest.genericGet(this.getGame().getUrl()
                                + "/report/" + response.reportId, MoveReport.class);
                        if (res.status != SUCCESS) {
                            this.action = false;
                            return;
                        }
                        if (move.equals("up"))
                            coordinates = coordinates.plus(0, 1, 0);
                        if (move.equals("down"))
                            coordinates = coordinates.plus(0, -1, 0);
                        if (move.equals("east"))
                            coordinates = coordinates.plus(1, 0, 0);
                        if (move.equals("west"))
                            coordinates = coordinates.plus(-1, 0, 0);
                        if (move.equals("north"))
                            coordinates = coordinates.plus(0, 0, 1);
                        if (move.equals("south"))
                            coordinates = coordinates.plus(0, 0, -1);

                        System.out.println(coordinates.toString());

                        this.action = false;
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }
                }
                , 1000 * waitTime / this.game.getTickrate(), TimeUnit.MILLISECONDS);
    }



    public void moveUnitUp() throws UnirestException, ExecutionException, InterruptedException {
        sendCommandGetMoveReport("up", 1);
    }

    public void moveUnitDown() throws UnirestException, ExecutionException, InterruptedException {
        sendCommandGetMoveReport("down", 1);

    }

    public void moveUnitEast() throws UnirestException, ExecutionException, InterruptedException {
        sendCommandGetMoveReport("east", 1);

    }

    public void moveUnitWest() throws UnirestException, ExecutionException, InterruptedException {
        sendCommandGetMoveReport("west", 1);

    }

    public void moveUnitNorth() throws UnirestException, ExecutionException, InterruptedException {
        sendCommandGetMoveReport("north", 1);

    }

    public void moveUnitSouth() throws UnirestException, ExecutionException, InterruptedException {
        sendCommandGetMoveReport("south", 1);

    }


    public boolean findAnyMineralMine()
    {
        return this.game.getLblock().stream().anyMatch(b -> b.element != null);
    }

    public void goToBlock(Point location) throws InterruptedException, ExecutionException, UnirestException {
        while (!this.coordinates.equals(location))
        {
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
    public Hexahedron.HexahedronElement <Block> findClosestNullBlock()
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
