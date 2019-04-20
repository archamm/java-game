package com.epita.units;

import com.epita.callables.CallableInspectReport;
import com.epita.creeps.given.extra.Cartographer;
import com.epita.creeps.given.vo.report.ScanReport;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-20 09:26
 */
public class Farmers extends MovingUnits
{

    public ScanReport sendCommandGetScanReport(String cmd, int waitTime) throws ExecutionException, InterruptedException {

        ScheduledFuture<ScanReport> report = this.game.getTpe().schedule(new CallableInspectReport(this, this.sendCommand(cmd).reportId)
                , 1000 * waitTime / this.game.getTickrate(), TimeUnit.MILLISECONDS);
        return report.get();
    }

    public void ScanOne() throws ExecutionException, InterruptedException {
        Cartographer.INSTANCE.register(sendCommandGetScanReport("/scan:1", 1));
    }
}
