package com.epita.units;

import com.epita.Game;
import com.epita.creeps.given.vo.report.MoveReport;
import com.epita.creeps.given.vo.report.Report;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.tools.CallableReport;
import com.epita.tools.GenericRequest;
import com.epita.tools.CallableCommand;
import com.mashape.unirest.http.exceptions.UnirestException;

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


    public CommandResponse sendCommand(String cmd) throws ExecutionException, InterruptedException {
        Callable<CommandResponse> callableRes = null;
            Future<CommandResponse> res = this.game.getTpe().submit(new CallableCommand(this, cmd));
            return res.get();
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
}


