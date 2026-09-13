package br.edu.fatecsjc.lgnspringapi.converter;

import java.util.Objects;

public class SimpleEntity {
    private String value;

    public SimpleEntity() {
    }

    public SimpleEntity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SimpleEntity))
            return false;
        SimpleEntity that = (SimpleEntity) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
