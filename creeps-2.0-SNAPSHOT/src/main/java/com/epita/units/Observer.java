package com.epita.units;

import com.epita.Game;

import com.epita.callables.CallableInspectReport;
import com.epita.callables.CallableSpawnReport;
import com.epita.creeps.given.extra.Cartographer;
import com.epita.creeps.given.vo.Block;
import com.epita.creeps.given.vo.geometry.Point;
import com.epita.creeps.given.vo.report.SpawnReport;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 21:23
 */
public class Observer extends Farmers
{
    public Observer(Game game, Point coordinates, String agentId)
    {
        this.action = false;
        this.game = game;
        this.coordinates = coordinates;
        this.agentId = agentId;
        this.name = "observer";
    }


    public void ScanTwo() throws ExecutionException, InterruptedException {
        sendCommandGetScanReport("2", 1);
    }


    public void ScanFour() throws ExecutionException, InterruptedException {
        sendCommandGetScanReport("4", 1);
    }
}
