package com.epita.creeps;
import com.epita.Game;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.concurrent.*;


public class Program {

    public static void main(String[] args) throws UnirestException, ExecutionException, InterruptedException {


        String urlepita = "http://rush.assistants.epita.fr:1685";
        String urllocal = "http://localhost:1664";
        Game game = new Game(urllocal, "archam_m", "d6ec8f5");
        /*
        try
        {

                InitResponse b = GenericRequest.genericPost(url + "/init/archam_m-v4", InitResponse.class);
                System.out.println(b.toString());
                if (b.login != null)
                {

                    System.out.println("success");
                    CommandResponse res = GenericRequest.genericPost(
                            url + "/command/" + b.login + "/" +b.baseId + "/spawn:probe",
                            CommandResponse.class);
                    System.out.println(res.toString());

                    Future <CommandResponse> response = tpe.submit(new Callable<CommandResponse>() {
                        @Override
                        public CommandResponse call() {
                            CommandResponse response = null;
                            try {
                                response = GenericRequest.genericPost(
                                        url + "/command/" + b.login + "/" + b.probeId + "/move:up",
                                        CommandResponse.class);
                                return response;
                            } catch (UnirestException e) {
                                e.printStackTrace();
                            }
                            System.out.println(response.toString());
                            return response;
                        }
                    });
                    CommandResponse r = response.get();

                    ScheduledFuture <MoveReport> report = tpe.schedule(new Callable<MoveReport>() {
                        @Override
                        public MoveReport call() throws UnirestException {
                            MoveReport res =  GenericRequest.genericGet(url + "/report/" + r.reportId, MoveReport.class);
                            return res;

                        }
                    }, 3, TimeUnit.SECONDS);
                    System.out.println(report.get().toString());




                    Nexus nexus = new Nexus()


                }
                else
                {
                    System.out.print("fail");

                }


        }  catch (UnirestException e) {
            e.printStackTrace();
        } */

    }
}
