package org.myweb.services;

import org.jetbrains.annotations.NotNull;
import org.myweb.db.Dao;

public class Service {
    @NotNull
    protected Dao db;

    @NotNull
    public Dao getDb() {
        return db;
    }

    public void setDb(@NotNull Dao db) {
        this.db = db;
    }
}
