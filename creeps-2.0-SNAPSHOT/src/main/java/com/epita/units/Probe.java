package com.epita.units;

import com.epita.Game;
import com.epita.callables.CallableMineReport;
import com.epita.creeps.given.vo.report.*;
import com.mashape.unirest.http.exceptions.UnirestException;

import com.epita.creeps.given.vo.geometry.Point;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.epita.creeps.given.vo.report.Report.Status.SUCCESS;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 21:22
 */

public class Probe extends MovingUnits
{
    int maxPayloadMineral;
    int maxPayloadBiomass;
    int payloadBiomass;
    int payloadMinerals;

    public Probe(Game game, Point coordinates, String agentId)
    {
        this.game = game;
        this.coordinates = coordinates;
        this.agentId = agentId;
        this.name = "probe";
        maxPayloadMineral = this.game.getInitResponse().setup.maxProbeMineralsLoad;
        maxPayloadBiomass = this.game.getInitResponse().setup.maxProbeBiomassLoad;
        payloadBiomass = 0;
        payloadMinerals = 0;
    }


    public boolean initNexus() throws UnirestException, ExecutionException, InterruptedException {
        SpawnReport report =  sendCommandGetSpawnReport("/spawn:nexus", 10);
        if (report.status == SUCCESS) {
            this.game.getUnitList().add(new Observer(this.game, report.agentLocation, report.spawnedAgentId));
            return true;
        }
        return false;
    }

    public boolean initPhotonCanon() throws UnirestException, ExecutionException, InterruptedException {
        SpawnReport report = sendCommandGetSpawnReport("spawn:photoncannon", 5);
        if (report.status == SUCCESS) {
            this.game.getUnitList().add(new Observer(this.game, report.agentLocation, report.spawnedAgentId));
            return true;
        }
        return false;
    }

    public MineReport sendCommandGetMineReport(String cmd, int waitTime) throws ExecutionException, InterruptedException {

        ScheduledFuture<MineReport> report = this.game.getTpe().schedule(new CallableMineReport(this, this.sendCommand(cmd).reportId)
                , 1000 * waitTime / this.game.getTickrate(), TimeUnit.MILLISECONDS);
        return report.get();
    }

    public boolean genericMine() throws ExecutionException, InterruptedException {
        MineReport report = sendCommandGetMineReport("spawn:photoncannon", 5);
        if (report.status == SUCCESS) {
            payloadMinerals = report.payload.minerals;
            payloadBiomass = report.payload.biomass;
            return true;
        }
        return false;
    }



}
