package com.epita.units;

import com.epita.Game;
import com.epita.callables.CallableCommand;
import com.epita.callables.CallableSpawnReport;
import com.epita.creeps.given.vo.report.SpawnReport;
import com.epita.creeps.given.vo.response.CommandResponse;


import com.epita.creeps.given.vo.geometry.Point;
;
import java.util.concurrent.*;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 20:30
 */


public class Unit {
    Game game;
    Point coordinates;
    String name;
    String agentId;

    public SpawnReport sendCommandGetSpawnReport(String cmd, int waitTime) throws ExecutionException, InterruptedException {

        ScheduledFuture<SpawnReport> report = this.game.getTpe().schedule(new CallableSpawnReport(this, this.sendCommand(cmd).reportId)
                , 1000 * waitTime / this.game.getTickrate(), TimeUnit.MILLISECONDS);
        return report.get();
    }

    public CommandResponse sendCommand(String cmd) throws ExecutionException, InterruptedException {
        Callable<CommandResponse> callableRes = null;
            Future<CommandResponse> res = this.game.getTpe().submit(new CallableCommand(this, cmd));
            return res.get();
        }

    public void SearchAndReplaceCoordinates(Point point)
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


