package com.epita.units;

import com.epita.Game;
import com.epita.creeps.given.vo.geometry.Point;

/**
 * Created by: Matthieu Archambault
 * On 2019-04-20 13:02
 */
public class PhotonCannon extends MovingUnits{

    public PhotonCannon(Game game, Point coordinates, String agentId)
    {
        this.action = false;
        this.game = game;
        this.coordinates = coordinates;
        this.agentId = agentId;
        this.name = "dragoon";
    }

}
