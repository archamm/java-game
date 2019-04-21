package com.epita.creeps;
import com.epita.Game;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.concurrent.*;


public class Program {

    public static void main(String[] args) throws UnirestException, ExecutionException, InterruptedException {

        String[] loginandhash =  args[3].split("-");
        Game game = new Game(args[2] + ":" + args[2]  , loginandhash[0], loginandhash[2]);
    }
}
