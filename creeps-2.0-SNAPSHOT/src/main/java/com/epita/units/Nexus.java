package com.epita.units;

import com.epita.Game;

import com.mashape.unirest.http.exceptions.UnirestException;

import com.epita.creeps.given.vo.geometry.Point;

import java.util.concurrent.ExecutionException;


/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 22:07
 */
public class Nexus extends Unit
{
    public Nexus(Game game, Point coordinates, String agentId)
    {
        this.action = false;
        this.game = game;
        this.coordinates = coordinates;
        this.agentId = agentId;
        this.name = "Nexus";
    }


    public void initProbe() throws UnirestException, ExecutionException, InterruptedException {
        sendCommandGetSpawnReport("probe", 3);
    }

    public void initDragoon() throws UnirestException, ExecutionException, InterruptedException {
        sendCommandGetSpawnReport("dragoon", 5);
    }

    public void initObserver() throws UnirestException, ExecutionException, InterruptedException {
        sendCommandGetSpawnReport("observer", 2);
    }

}
