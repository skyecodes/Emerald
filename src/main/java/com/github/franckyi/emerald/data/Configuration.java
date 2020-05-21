package com.github.franckyi.emerald.data;

import java.util.Objects;

public class Configuration implements Cloneable {
    private int version;
    private Theme theme;
    private String customTheme;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public String getCustomTheme() {
        return customTheme;
    }

    public void setCustomTheme(String customTheme) {
        this.customTheme = customTheme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        if (version != that.version) return false;
        if (theme != that.theme) return false;
        return Objects.equals(customTheme, that.customTheme);
    }

    @Override
    public int hashCode() {
        int result = version;
        result = 31 * result + (theme != null ? theme.hashCode() : 0);
        result = 31 * result + (customTheme != null ? customTheme.hashCode() : 0);
        return result;
    }

    @Override
    public Configuration clone() {
        Configuration c = new Configuration();
        c.setVersion(version);
        c.setTheme(theme);
        c.setCustomTheme(customTheme);
        return c;
    }

    public void set(Configuration config) {
        this.setVersion(config.getVersion());
        this.setTheme(config.getTheme());
        this.setCustomTheme(config.getCustomTheme());
    }

    public enum Theme {
        LIGHT, DARK, CUSTOM
    }
}
