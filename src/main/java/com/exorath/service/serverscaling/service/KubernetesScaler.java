/*
 * Copyright 2017 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.service.serverscaling.service;

import com.exorath.service.connector.res.ServerInfo;
import com.exorath.service.serverscaling.res.Bungee;
import com.exorath.service.serverscaling.res.GameType;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by toonsev on 4/11/2017.
 */
public class KubernetesScaler implements Runnable {
    private com.exorath.service.serverscaling.Service service;
    private KubernetesClient kubernetesClient;
    private ConnectorServiceProvider connectorServiceProvider;

    public KubernetesScaler(com.exorath.service.serverscaling.Service service, KubernetesClient kubernetesClient, ConnectorServiceProvider connectorServiceProvider) {
        this.service = service;
        this.kubernetesClient = kubernetesClient;
        this.connectorServiceProvider = connectorServiceProvider;
    }

    @Override
    public void run() {
        try {
            scaleBungee();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String game : service.getGameTypes()) {
            try {
                scaleGame(game);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void scaleGame(String game) {
        GameType gameType = service.getGameType(game);
        if (gameType == null)
            throw new NullPointerException(game + " not found in service");
        scaleGameType(gameType);
    }

    private void scaleGameType(GameType gameType) {
        ServerInfo serverInfo = connectorServiceProvider.getInfo(gameType);
        if (serverInfo.getOpenServerCount() <= 0)
            if (getScheduledAndShutdownOutdated(gameType, serverInfo.getOpenServerCount()) <= 0)
                scheduleServer(gameType);
    }

    private int getScheduledAndShutdownOutdated(GameType gameType, int total) {
        PodList pods = kubernetesClient.pods().withLabel("app", getFullName(gameType.getName())).list();
        if (pods.getItems().size() > 100)
            throw new RuntimeException("HARDCODED POD COUNT REACHED FOR " + getFullName(gameType.getName()) + ". Won't scale until this is lower then 100 instances.");

        int runningAndScheduled = 0;
        for (Pod pod : pods.getItems()) {
            if (pod.getStatus().getPhase().equalsIgnoreCase("running") || pod.getStatus().getPhase().equalsIgnoreCase("pending")) {
                if (pod.getSpec().getContainers().get(0).getImage().equals(gameType.getImage())) {//No update required
                    runningAndScheduled++;
                } else
                    terminate(pod);
            }
        }
        return runningAndScheduled - total;
    }

    private void terminate(Pod pod) {
        System.out.println("Terminating pod " + pod.getMetadata().getName() + ": " + kubernetesClient.pods().delete(pod));
    }

    private void scheduleServer(GameType gameType) {
        kubernetesClient.pods().create(getGamePod(gameType));
    }

    private String getFullName(String gameTypeName){
        return "spigot-" + gameTypeName;
    }
    private Pod getGamePod(GameType gameType) {
        Set<EnvVar> envVars = gameType.getEnv().entrySet().stream().map(entry -> new EnvVarBuilder().withName(entry.getKey()).withValue(entry.getValue()).build()).collect(Collectors.toSet());
        String appName = getFullName(gameType.getName());

        PodBuilder podBuilder = new PodBuilder().editOrNewSpec().addNewContainer()
                .withName(appName)
                .withImage(gameType.getImage())
                .addNewPort().withContainerPort(25565).withProtocol("TCP").and()
                .addAllToEnv(envVars)
                .withStdin(true).withTty(true).and().and()
                .editOrNewMetadata().withGenerateName(gameType.getName()).addToLabels(gameType.getLabels()).withDeletionGracePeriodSeconds((long) gameType.getTerminationGracePeriodSeconds())
                .addToLabels("app", appName).and();
        return podBuilder.build();
    }

    //UNIMPLEMENTED FOR NOW!
    //if average cpu or ram load is higher then 60%, launch another instance, this is currently left unimplemented!
    private void scaleBungee() {

    }

    private Service getBungeeService(Bungee bungee) {
        return new ServiceBuilder()
                .editMetadata().addToLabels("app", "server-bc").addToLabels("type", "bc").withNamespace("servers").withName("server-bc").and()
                .editSpec().addToSelector("app", "server-bc").addNewPortLike(new ServicePortBuilder().withPort(25565).withNewTargetPort(25565).build()).and()
                .withType("NodePort").and().build();
    }
}
