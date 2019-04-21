package com.epita.units;

import com.epita.Game;
import com.epita.callables.CallableMineReport;
import com.epita.callables.CallableUnloadReport;
import com.epita.creeps.given.extra.Cartographer;
import com.epita.creeps.given.vo.AgentStatus;
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
    boolean hasMined;

    public Probe(Game game, Point coordinates, String agentId)
    {

        this.hasMined = false;
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
            sendCommandGetSpawnReport("nexus", 10);
        }

    public void initPylon() throws UnirestException, ExecutionException, InterruptedException {
        sendCommandGetSpawnReport("pylon", 4);
    }

    public void initPhotonCanon() throws UnirestException, ExecutionException, InterruptedException {
            sendCommandGetSpawnReport("photon-cannon", 5);
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
                        if (resourceLeftMinerals <= 0)
                            Cartographer.INSTANCE.register(res.agentLocation, Block.valueOf(this.game.getInitResponse().blockType));
                        resourceLeftMinerals = res.resourcesLeft.minerals;
                        resourceLeftBiomass = res.resourcesLeft.biomass;
                        System.out.println("ressourceLeftMineral:" + resourceLeftMinerals + " on" + res.agentId.toString());
                        System.out.println("ressourceLeftBiomass:" + resourceLeftBiomass + " on" + res.agentId.toString());

                        System.out.println(this.game.toString());
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

                        this.payloadBiomass = 0;
                        this.payloadMinerals = 0;
                        this.resourceLeftMinerals = 50;
                        this.resourceLeftBiomass = 50;
                        this.hasMined = false;

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

    public void mineBiomass() throws ExecutionException, InterruptedException {
        sendCommandGetMineReport("biomass", 1);
    }


    public void unload() throws ExecutionException, InterruptedException {
        sendCommandGetUnloadReport(5);
    }

    public boolean isFull()
    {
        return maxPayloadMineral <= payloadMinerals ||
                maxPayloadBiomass <= payloadBiomass;
    }

    public boolean isEmpty()
    {
        return resourceLeftMinerals == 0 || resourceLeftBiomass == 0;
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

    public boolean getHasMined() {
        return hasMined;
    }

    public void setHasMined(boolean hasMined) {
        this.hasMined = hasMined;
    }
}
