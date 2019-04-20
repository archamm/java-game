package com.epita.units;

import com.epita.Game;
import com.epita.callables.CallableCommand;
import com.epita.callables.CallableSpawnReport;
import com.epita.creeps.given.extra.Cartographer;
import com.epita.creeps.given.vo.Block;
import com.epita.creeps.given.vo.geometry.Hexahedron;
import com.epita.creeps.given.vo.report.InspectReport;
import com.epita.creeps.given.vo.report.Report;
import com.epita.creeps.given.vo.report.SpawnReport;
import com.epita.creeps.given.vo.response.CommandResponse;


import com.epita.creeps.given.vo.geometry.Point;
import com.epita.tools.GenericRequest;
import com.mashape.unirest.http.exceptions.UnirestException;
;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.epita.creeps.given.vo.report.Report.Status.SUCCESS;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 20:30
 */


public class Unit {
    Game game;
    Point coordinates;
    String name;
    String agentId;
    boolean action;



    public void sendCommandGetSpawnReport(String unit, int waitTime) throws ExecutionException, InterruptedException {
        if (action)
            return;
        action = true;
        CommandResponse response = sendCommand("/spawn:" + unit);
        this.game.getTpe().schedule(() -> {
                    try {
                        SpawnReport res = GenericRequest.genericGet(this.getGame().getUrl()
                                + "/report/" + response.reportId, SpawnReport.class);
                        if (res.status != SUCCESS) {
                            this.action = false;
                            return;
                        }
                        if (unit.equals("probe"))
                            this.game.getUnitList().add(new Probe(this.game, res.spawnedAgentLocation, res.spawnedAgentId));
                        if (unit.equals("nexus"))
                            this.game.getUnitList().add(new Nexus(this.game, res.spawnedAgentLocation, res.spawnedAgentId));
                        if (unit.equals("photoncannon"))
                            this.game.getUnitList().add(new PhotonCannon(this.game, res.spawnedAgentLocation, res.spawnedAgentId));
                        if (unit.equals("observer"))
                            this.game.getUnitList().add(new Observer(this.game, res.spawnedAgentLocation, res.spawnedAgentId));
                        if (unit.equals("dragoon"))
                            this.game.getUnitList().add(new Dragoon(this.game, res.spawnedAgentLocation, res.spawnedAgentId));

                        this.action = false;
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }
                }
                , 1000 * waitTime / this.game.getTickrate(), TimeUnit.MILLISECONDS);
    }


    public CommandResponse sendCommand(String cmd) throws ExecutionException, InterruptedException {
            Future<CommandResponse> res = this.game.getTpe().submit(new CallableCommand(this, cmd));
            return res.get();
        }

    public void searchAndReplaceCoordinates(Point point)
    {
        for (Unit unit : this.game.getUnitList())
        {
            if(unit.agentId.equals(this.agentId)) {
                unit.setCoordinates(point);
            }
        }
    }




    public Game getGame() {
        return game;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public String getName() {
        return name;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isAction() {
        return action;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "game=" + game +
                ", coordinates=" + coordinates +
                ", name='" + name + '\'' +
                ", agentId='" + agentId + '\'' +
                '}';
    }
}


