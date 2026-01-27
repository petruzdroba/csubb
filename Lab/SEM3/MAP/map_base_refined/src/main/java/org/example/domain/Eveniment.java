package org.example.domain;

import javax.swing.plaf.metal.MetalCheckBoxIcon;

public class Eveniment {
    private Long id;
    private Long matchId;
    private Team team;
    private int rata;
    private Action action;

    public Eveniment(Long matchId, Team team, int rata, Action action) {
        id=null;
        this.matchId = matchId;
        this.team = team;
        this.rata = rata;
        this.action = action;
    }

    public Eveniment(Long id, Long matchId, Team team, int rata, Action action) {
        this.id = id;
        this.matchId = matchId;
        this.team = team;
        this.rata = rata;
        this.action = action;
    }

    public Eveniment() {
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getRata() {
        return rata;
    }

    public void setRata(int rata) {
        this.rata = rata;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return String.format("Rata %d a facut %s pentru echipa %s", rata, action.toString(), team.toString());
    }
}
