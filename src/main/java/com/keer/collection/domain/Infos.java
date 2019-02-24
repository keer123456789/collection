package com.keer.collection.domain;

import java.util.ArrayList;
import java.util.List;

public class Infos {
    private String id ;
    private List<Info> infos=new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Info> getInfos() {
        return infos;
    }

    public void setInfos(List<Info> infos) {
        this.infos = infos;
    }

    public void addInfo(Info info){
        this.infos.add(info);
    }

    public Info getInfo(int i){
        return this.infos.get(i);
    }
}
