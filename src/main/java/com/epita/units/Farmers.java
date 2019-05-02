package com.epita.units;

import com.epita.callables.CallableInspectReport;
import com.epita.creeps.given.extra.Cartographer;
import com.epita.creeps.given.vo.report.MoveReport;
import com.epita.creeps.given.vo.report.ScanReport;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.tools.GenericRequest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.epita.creeps.given.vo.report.Report.Status.SUCCESS;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-20 09:26
 */
public class Farmers extends MovingUnits
{

    public void sendCommandGetScanReport(String cmd, int waitTime) throws ExecutionException, InterruptedException {

        if (action)
            return;
        action = true;
        CommandResponse response = sendCommand("/scan:" + cmd);
        this.game.getTpe().schedule(() -> {
                    try {
                        ScanReport res = GenericRequest.genericGet(this.getGame().getUrl()
                                + "/report/" + response.reportId, ScanReport.class);
                        if (res.status != SUCCESS) {
                            this.action = false;
                            return;
                        }
                       Cartographer.INSTANCE.register(res);

                        this.action = false;
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }
                }
                , 1000 * waitTime / this.game.getTickrate(), TimeUnit.MILLISECONDS);
    }


    public void ScanOne() throws ExecutionException, InterruptedException {
        sendCommandGetScanReport("1", 1);
    }
}
