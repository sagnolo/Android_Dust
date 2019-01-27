package com.example.dustapplication;

import java.util.List;

public class Dust {

    List<DustDetail> list;

    public List<DustDetail> getList() {
        return list;
    }

    public void setList(List<DustDetail> list) {
        this.list = list;
    }

    class DustDetail {
        String _returnType;
    }
}
