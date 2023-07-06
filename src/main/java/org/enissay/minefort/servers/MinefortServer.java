package org.enissay.minefort.servers;

public class MinefortServer {

    private String serverName;
    private String serverID;
    private String userID;
    private String version;
    private int state;
    private String MOTD;
    private MinefortPlayers players;

    public MinefortServer(String serverName, String serverID, String userID, String version, int state, String MOTD, MinefortPlayers players) {
        this.serverName = serverName;
        this.serverID = serverID;
        this.userID = userID;
        this.version = version;
        this.state = state;
        this.MOTD = MOTD;
        this.players = players;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMOTD() {
        return MOTD;
    }

    public void setMOTD(String MOTD) {
        this.MOTD = MOTD;
    }

    public MinefortPlayers getPlayers() {
        return players;
    }

    public void setPlayers(MinefortPlayers players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "MinefortServer{" +
                "serverName='" + serverName + '\'' +
                ", serverID='" + serverID + '\'' +
                ", userID='" + userID + '\'' +
                ", version='" + version + '\'' +
                ", state=" + state +
                ", MOTD='" + MOTD + '\'' +
                ", players=" + players +
                '}';
    }
}
