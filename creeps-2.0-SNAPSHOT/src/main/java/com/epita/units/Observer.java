package com.epita.units;

import com.epita.Game;

import com.epita.creeps.given.vo.geometry.Point;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-19 21:23
 */
public class Observer extends MovingUnits
{
    public Observer(Game game, Point coordinates, String agentId)
    {
        this.game = game;
        this.coordinates = coordinates;
        this.agentId = agentId;
        this.name = "observer";
    }
}
