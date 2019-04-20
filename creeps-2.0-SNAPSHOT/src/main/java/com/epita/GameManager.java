package com.epita;

import com.epita.creeps.given.vo.Block;
import com.epita.creeps.given.vo.geometry.Hexahedron;
import com.epita.units.Probe;
import com.epita.units.*;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.concurrent.ExecutionException;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-21 00:29
 */

public class GameManager
{
    Game game;
    int numberOfProbes;
    int numberOfDragoons;
    int numberOfObservers;
    int numberOfNexus;
    int numberOfPhotons;



    public GameManager(Game game) {
        this.game = game;
    }

    public void getNumberOfInstance()
    {
        numberOfProbes = 0;
        numberOfDragoons = 0;
        numberOfObservers = 0;
        numberOfNexus = 0;
        numberOfPhotons = 0;
        this.game.getUnitList().forEach(u -> {
            if (u instanceof Probe)
                numberOfProbes++;
            if (u instanceof Dragoon)
                numberOfDragoons++;
            if (u instanceof Observer)
                numberOfObservers++;
            if (u instanceof Nexus)
                numberOfNexus++;
            if (u instanceof PhotonCannon)
                numberOfPhotons++;
        });
    }
    public void manageNexus(Nexus nexus) throws InterruptedException, ExecutionException, UnirestException {

        if (numberOfObservers < 3)
        {
            nexus.initObserver();
            if (nexus.isAction())
                System.out.println("Spawn Observer");
        }
        if (numberOfProbes < 10 && this.game.getMinerals() > this.game.initResponse.costs.spawnProbe.minerals)
        {
            nexus.initProbe();
            if (nexus.isAction())
                System.out.println("Spawn Probe");
        }
    }

    public void goExplore(MovingUnits u) throws InterruptedException, ExecutionException, UnirestException {
            Hexahedron.HexahedronElement<Block> b = u.findClosestNullBlock();
            u.goToBlock(b.location);
            if (u instanceof Observer)
                ((Observer) u).ScanFour();
            else
                ((Probe) u).ScanOne();
            if (u.isAction())
                System.out.println("Scan");
            this.game.updateBlockList();
    }

    public void goMine(Probe u) throws InterruptedException, ExecutionException, UnirestException {
        Hexahedron.HexahedronElement<Block> b = u.findClosest(this.game.getMineralMines());
        u.goToBlock(b.location);
        if (u.canCarry())
        {
            u.mineMinerals();
        }
        if (!u.isAction()) {
            System.out.println("Location of the Mine:" + b.location.toString());
            System.out.println("Location of the probe:" + u.getCoordinates().toString());
            System.out.println(u.toString());
        }
        else {
            u.goToBlock(u.findClosestNexus().getCoordinates());
            u.unload();
        }
    }

    public void manageProbe(Probe u) throws InterruptedException, ExecutionException, UnirestException {
       if (this.game.getMineralMines().size() != 0)
       {
            goMine(u);
       }
       else
       {
           goExplore(u);
       }
    }
    public void manageObservers(Observer o) throws InterruptedException, ExecutionException, UnirestException {
        goExplore(o);
    }

}
