package org.enissay.minefort.servers;

import java.util.Arrays;

public class MinefortPlayers {

    private int online;
    private String[] uuids;
    private int max;

    public MinefortPlayers(int online, String[] uuids, int max) {
        this.online = online;
        this.uuids = uuids;
        this.max = max;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String[] getUuids() {
        return uuids;
    }

    public void setUuids(String[] uuids) {
        this.uuids = uuids;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "MinefortPlayers{" +
                "online=" + online +
                ", uuids=" + Arrays.toString(uuids) +
                ", max=" + max +
                '}';
    }
}
