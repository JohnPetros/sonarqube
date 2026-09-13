package br.edu.fatecsjc.lgnspringapi.converter;

import java.util.Objects;

public class SimpleDTO {
    private String value;

    public SimpleDTO() {
    }

    public SimpleDTO(String value) {
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
        if (!(o instanceof SimpleDTO))
            return false;
        SimpleDTO dto = (SimpleDTO) o;
        return Objects.equals(getValue(), dto.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
