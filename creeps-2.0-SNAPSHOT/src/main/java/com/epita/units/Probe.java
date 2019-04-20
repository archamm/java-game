package com.epita.units;

import com.epita.Game;
import com.epita.callables.CallableMineReport;
import com.epita.callables.CallableUnloadReport;
import com.epita.creeps.given.vo.Block;
import com.epita.creeps.given.vo.geometry.Hexahedron;
import com.epita.creeps.given.vo.report.*;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.tools.GenericRequest;
import com.mashape.unirest.http.exceptions.UnirestException;

import com.epita.creeps.given.vo.geometry.Point;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.epita.creeps.given.vo.report.Report.Status.SUCCESS;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 21:22
 */

public class Probe extends Farmers
{
    int maxPayloadMineral;
    int maxPayloadBiomass;
    int payloadBiomass;
    int payloadMinerals;
    int resourceLeftMinerals;
    int resourceLeftBiomass;

    public Probe(Game game, Point coordinates, String agentId)
    {


        this.action = false;
        this.game = game;
        this.coordinates = coordinates;
        this.agentId = agentId;
        this.name = "probe";
        maxPayloadMineral = this.game.getInitResponse().setup.maxProbeMineralsLoad;
        maxPayloadBiomass = this.game.getInitResponse().setup.maxProbeBiomassLoad;
        payloadBiomass = 0;
        payloadMinerals = 0;
        resourceLeftBiomass = 50;
        resourceLeftMinerals = 50;
    }


    public void initNexus() throws UnirestException, ExecutionException, InterruptedException {
            sendCommandGetSpawnReport("/spawn:nexus", 10);
        }

    public void initPhotonCanon() throws UnirestException, ExecutionException, InterruptedException {
            sendCommandGetSpawnReport("spawn:photoncannon", 5);
    }

    public void sendCommandGetMineReport(String minerals, int waitTime) throws ExecutionException, InterruptedException {
        if (action)
            return;
        action = true;
        CommandResponse response = sendCommand("/mine:" + minerals);
        this.game.getTpe().schedule(() -> {
                    try {
                        MineReport res = GenericRequest.genericGet(this.getGame().getUrl()
                                + "/report/" + response.reportId, MineReport.class);
                        if (res.status != SUCCESS) {
                            this.action = false;
                            return;
                        }
                        payloadMinerals = res.payload.minerals;
                        payloadBiomass = res.payload.biomass;
                        resourceLeftMinerals = res.resourcesLeft.minerals;
                        resourceLeftBiomass = res.resourcesLeft.biomass;

                        this.action = false;
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }
                }
                , 1000 * waitTime / this.game.getTickrate(), TimeUnit.MILLISECONDS);
    }

    public void sendCommandGetUnloadReport(int waitTime) throws ExecutionException, InterruptedException {
        if (action)
            return;
        action = true;
        CommandResponse response = sendCommand("unload");
        this.game.getTpe().schedule(() -> {
                    try {
                        UnloadReport res = GenericRequest.genericGet(this.getGame().getUrl()
                                + "/report/" + response.reportId, UnloadReport.class);
                        if (res.status != SUCCESS) {
                            this.action = false;
                            return;
                        }
                        this.game.setMinerals(this.game.getMinerals() + res.creditedMinerals);
                        this.game.setBiomass(this.game.getBiomass() + res.creditedBiomass);

                        this.resourceLeftMinerals = 50;
                        this.resourceLeftBiomass = 50;

                        this.action = false;
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }
                }
                , 1000 * waitTime / this.game.getTickrate(), TimeUnit.MILLISECONDS);
    }


    public void mineMinerals() throws ExecutionException, InterruptedException {
        sendCommandGetMineReport("minerals", 1);
    }
    public void setMineralsOrBiomass() throws ExecutionException, InterruptedException {
        sendCommandGetMineReport("biomass", 1);
    }

    public void unload() throws ExecutionException, InterruptedException {
        sendCommandGetUnloadReport(5);
    }
    public boolean canCarry()
    {
        return payloadBiomass < maxPayloadBiomass
                && payloadMinerals < maxPayloadMineral
                && resourceLeftBiomass != 0
                && resourceLeftMinerals != 0;
    }

    public int getMaxPayloadMineral() {
        return maxPayloadMineral;
    }

    public int getMaxPayloadBiomass() {
        return maxPayloadBiomass;
    }

    public int getPayloadBiomass() {
        return payloadBiomass;
    }

    public int getPayloadMinerals() {
        return payloadMinerals;
    }

    @Override
    public String toString() {
        return "Probe{" +
                ", name='" + name + '\'' +
                "maxPayloadMineral=" + maxPayloadMineral +
                ", maxPayloadBiomass=" + maxPayloadBiomass +
                ", payloadBiomass=" + payloadBiomass +
                ", payloadMinerals=" + payloadMinerals +
                '}';
    }
}
