package com.github.franckyi.emerald.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.Instant;

public class Instance implements Cloneable {
    private final StringProperty name;
    private String id;
    private Instant creationDate;
    private String minecraftVersion;

    public Instance() {
        name = new SimpleStringProperty();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public void setMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Instance instance = (Instance) o;

        if (!id.equals(instance.id)) return false;
        if (!name.getValue().equals(instance.name.getValue())) return false;
        if (!creationDate.equals(instance.creationDate)) return false;
        return minecraftVersion.equals(instance.minecraftVersion);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + creationDate.hashCode();
        result = 31 * result + minecraftVersion.hashCode();
        return result;
    }

    @Override
    public Instance clone() {
        Instance i = new Instance();
        i.setId(id);
        i.setName(name.getValue());
        i.setCreationDate(creationDate);
        i.setMinecraftVersion(minecraftVersion);
        return i;
    }
}
