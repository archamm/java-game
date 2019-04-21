package com.epita;

import com.epita.creeps.given.extra.Cartographer;
import com.epita.creeps.given.vo.Block;
import com.epita.creeps.given.vo.geometry.Hexahedron;
import com.epita.creeps.given.vo.geometry.Point;
import com.epita.creeps.given.vo.response.InitResponse;
import com.epita.creeps.given.vo.response.StatisticsResponse;
import com.epita.creeps.given.vo.response.StatusResponse;
import com.epita.tools.GenericRequest;
import com.epita.units.*;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 21:25
 */


public class Game
{
    String url;
    String login;
    String commitHash;
    ScheduledThreadPoolExecutor tpe;
    int tickrate;
    int ticks;
    int minerals;
    int biomass;
    int warmUpTime;
    InitResponse initResponse;
    List <Hexahedron.HexahedronElement<Block>> lblock;
    StatisticsResponse statisticsResponse;
    int numberOfObservers;
    int numberOfProbes;
    int numberOfNexus;
    int numberOfPhoton;
    int numberOfDragoon;

    List <Point> assignedMined;
    List <Unit> unitList;

    public void initGame() throws UnirestException {
        this.initResponse = GenericRequest.genericPost(url + "/init/" + this.login + "-" + this.commitHash
                , InitResponse.class);

        this.tickrate = (int) initResponse.setup.ticksPerSeconds;
        System.out.println(this.tickrate);



        statisticsResponse = getStatistics();

        for (StatisticsResponse.PlayerStatsResponse e : statisticsResponse.players)
        {
            String name = this.login + "-" + commitHash;
            if (name.equals(e.name))
            {
                this.minerals = e.minerals;
                this.biomass = e.biomass;
                break;
            }
        }
        this.warmUpTime = statisticsResponse.scheduledGameStartTick;
        Cartographer.initialize(statisticsResponse.dimension);

        unitList.add(new Nexus(this, initResponse.coordinates, initResponse.baseId));
        unitList.add(new Probe(this, initResponse.coordinates, initResponse.probeId));
    }

    public boolean isRunning() throws UnirestException {
        StatusResponse res = GenericRequest.genericGet(this.url + "/status", StatusResponse.class);
        if (res != null)
            return res.running;
        return false;
    }

    public StatisticsResponse getStatistics() throws UnirestException {
        StatisticsResponse res =  GenericRequest.genericGet(url + "/statistics", StatisticsResponse.class);
        return res;
    }

    public StatisticsResponse.PlayerStatsResponse getPlayerResponse() throws UnirestException {
        for (StatisticsResponse.PlayerStatsResponse e : statisticsResponse.players)
        {
            String name = this.login + "-" + commitHash;
            if (name.equals(e.name))
            {
                return e;
            }
        }
        return null;
    }

    public void updateBlockList()
    {
        this.lblock = Cartographer.INSTANCE.stream().collect(Collectors.toList());
    }

    public List <Hexahedron.HexahedronElement <Block>> getMineralMines()
    {
          return lblock.stream()
                        .filter(bl -> bl.element != null)
                        .collect(Collectors.toList())
                        .stream().
                        filter(b -> b.element == Block.MINERALS_LOW || b.element == Block.MINERALS_MEDIUM || b.element == Block.MINERALS_HIGH)
                        .collect(Collectors.toList());
    }




    public void gameLoop() throws InterruptedException, ExecutionException, UnirestException {


        while (isRunning())
        {
                this.getNumberofInstance();
                for (int i = 0; i < unitList.size(); i++) {
                Unit u = unitList.get(i);
                updateBlockList();
                if (u instanceof Nexus) {

                    this.manageNexus((Nexus) u);
                }
                if (u instanceof Probe) {
                    this.manageProbe((Probe) u);
                }
                if (u instanceof Observer) {
                    this.manageObservers((Observer) u);
                }
            }
        }

    }
    public Game(String url, String login, String commitHash) throws UnirestException, ExecutionException, InterruptedException {
        this.url = url;
        this.login = login;
        this.commitHash = commitHash;
        this.tpe = new ScheduledThreadPoolExecutor(50);
        this.unitList = new ArrayList<>();

        this.initGame();

        if(!isRunning()) {
            this.tpe.schedule(() -> {
                try {
                    gameLoop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            }, this.warmUpTime * 1000 / this.tickrate, TimeUnit.MILLISECONDS);
        }
            else
                gameLoop();

        }





    public List<Unit> getUnitList() {
        return unitList;
    }


    public String getUrl() {
        return url;
    }

    public String getLogin() {
        return login;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public ScheduledThreadPoolExecutor getTpe() {
        return tpe;
    }

    public int getTickrate() {
        return tickrate;
    }

    public int getMinerals() {
        return minerals;
    }

    public void setMinerals(int minerals) {
        this.minerals = minerals;
    }

    public void setBiomass(int biomass) {
        this.biomass = biomass;
    }

    public int getBiomass() {
        return biomass;
    }

    public int getTicks() {
        return ticks;
    }

    public int getWarmUpTime() {
        return warmUpTime;
    }

    public InitResponse getInitResponse() {
        return initResponse;
    }

    public List<Hexahedron.HexahedronElement<Block>> getLblock() {
        return lblock;
    }

    @Override
    public String toString() {
        return "Game{" +
                "minerals=" + minerals +
                ", biomass=" + biomass +
                '}';
    }

    public void goExplore (MovingUnits u) throws InterruptedException, ExecutionException, UnirestException {
        Hexahedron.HexahedronElement<Block> b = u.findClosestNullBlock();
        if (!u.isOnBlock(b.location))
            u.goToBlock(b.location);
        else {
            if (u instanceof Observer)
                ((Observer) u).ScanFour();
            else
                ((Probe) u).convert();
        }

        this.updateBlockList();
    }

    public void goMine (Probe u) throws InterruptedException, ExecutionException, UnirestException {
        if (u.getPayloadMinerals() < this.initResponse.setup.maxProbeMineralsLoad){
            Hexahedron.HexahedronElement<Block> b = u.findClosest(this.getMineralMines());
            if (!u.isOnBlock(b.location))
                u.goToBlock(b.location);
            else {
                u.mineMinerals();
            }
        }
        else {
            if (!u.isOnBlock(u.findClosestNexus().getCoordinates()))
                u.goToBlock(u.findClosestNexus().getCoordinates());
            else
                u.unload();
        }

    }


    public void manageProbe (Probe u) throws InterruptedException, ExecutionException, UnirestException {
        if (this.getMineralMines().size() != 0 && getTotalNumberofInstance() < 40) {
            goMine(u);
        } else {
            goExplore(u);
        }
    }



    public void manageObservers (Observer o) throws InterruptedException, ExecutionException, UnirestException {
        goExplore(o);
    }

    public void getNumberofInstance()
    {
        numberOfObservers = 0;
        numberOfProbes = 0;
        numberOfNexus = 0;
        numberOfDragoon = 0;
        numberOfPhoton = 0;

        for (Unit e : unitList)
        {
            if (e instanceof Probe)
                numberOfProbes++;
            if (e instanceof Nexus)
                numberOfNexus++;
            if (e instanceof Dragoon)
                numberOfDragoon++;
            if (e instanceof Observer)
                numberOfObservers++;
            if (e instanceof PhotonCannon)
                numberOfPhoton++;
        }

    }

    public int getTotalNumberofInstance()
    {
        this.getNumberofInstance();
        return numberOfObservers + numberOfPhoton + numberOfDragoon + numberOfProbes + numberOfObservers;

    }

    public void manageNexus (Nexus nexus) throws InterruptedException, ExecutionException, UnirestException {

        if (numberOfObservers < 3) {
            numberOfObservers++;
            nexus.initObserver();
            if (!nexus.isAction()) {
                numberOfObservers++;
                System.out.println("Spawn Observer");
            }
        }
        if (numberOfProbes < 40 && this.getMinerals() > this.initResponse.costs.spawnProbe.minerals) {
            nexus.initProbe();
            if (!nexus.isAction()) {
                System.out.println("Spawn Probe");

            }
        }

        if (this.getMinerals() > this.initResponse.costs.spawnDragoon.minerals){
            nexus.initDragoon();
            if (!nexus.isAction()) {
                System.out.println("Spawn Probe");

            }
        }
    }

}
