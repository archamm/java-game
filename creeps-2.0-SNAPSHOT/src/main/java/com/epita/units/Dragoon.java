package com.epita.units;

import com.epita.Game;
import com.epita.creeps.given.vo.geometry.Point;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-20 09:24
 */
public class Dragoon extends MovingUnits
{
    public Dragoon(Game game, Point coordinates, String agentId)
    {
        this.game = game;
        this.coordinates = coordinates;
        this.agentId = agentId;
        this.name = "dragoon";
    }



}
