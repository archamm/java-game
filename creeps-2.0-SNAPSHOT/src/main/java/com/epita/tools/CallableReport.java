package com.epita.tools;

import com.epita.creeps.given.vo.report.MoveReport;
import com.epita.creeps.given.vo.report.Report;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.units.Unit;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.concurrent.Callable;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-20 01:23
 */
public class CallableReport implements Callable
{
    Unit unit;
    String reportId;

    public CallableReport(Unit unit, String reportId) {
        this.unit = unit;
        this.reportId = reportId;
    }

    @Override
    public Report call() throws Exception
    {
          Report res = GenericRequest.genericGet(this.unit.getGame().getUrl()
                + "/report/" + this.reportId, Report.class);
        return res;
    }
}
