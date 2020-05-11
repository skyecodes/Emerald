package com.github.franckyi.emerald.data;

public class Configuration implements Cloneable {
    private int version;
    private boolean darkTheme;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isDarkTheme() {
        return darkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        if (version != that.version) return false;
        return darkTheme == that.darkTheme;
    }

    @Override
    public int hashCode() {
        int result = version;
        result = 31 * result + (darkTheme ? 1 : 0);
        return result;
    }

    @Override
    public Object clone() {
        Configuration c = new Configuration();
        c.setVersion(version);
        c.setDarkTheme(darkTheme);
        return c;
    }
}
