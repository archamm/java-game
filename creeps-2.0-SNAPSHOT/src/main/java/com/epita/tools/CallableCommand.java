package com.epita.tools;

import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.units.Unit;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.concurrent.Callable;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-20 01:16
 */
public class CallableCommand implements Callable
{
    Unit unit;
    String cmd;

    public CallableCommand(Unit unit, String cmd) {
        this.unit = unit;
        this.cmd = cmd;

    }

    @Override
    public CommandResponse call() throws Exception
    {
        CommandResponse response = null;
        try {
             response = GenericRequest.genericPost(this.unit.getGame().getUrl()+ "/command/" +
                            this.unit.getGame().getLogin() + "-"
                             + this.unit.getGame().getCommitHash() + "/" + this.unit.getAgentId() + "/" + cmd,
                    CommandResponse.class);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return response;
    }
}
