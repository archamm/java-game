package com.epita.callables;

import com.epita.creeps.given.vo.report.FetchReport;
import com.epita.creeps.given.vo.report.Report;
import com.epita.creeps.given.vo.report.UnloadReport;
import com.epita.tools.GenericRequest;
import com.epita.units.Unit;

import java.util.concurrent.Callable;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-20 01:23
 */
public class CallableUnloadReport implements Callable
{
    Unit unit;
    String reportId;

    public CallableUnloadReport(Unit unit, String reportId) {
        this.unit = unit;
        this.reportId = reportId;
    }

    @Override
    public Report call() throws Exception
    {
          Report res = GenericRequest.genericGet(this.unit.getGame().getUrl()
                + "/report/" + this.reportId, UnloadReport.class);
        return res;
    }
}
