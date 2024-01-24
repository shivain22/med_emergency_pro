package io.aurigraph.healthcare.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.aurigraph.healthcare.domain.Comorbidity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ComorbidityDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComorbidityDTO)) {
            return false;
        }

        ComorbidityDTO comorbidityDTO = (ComorbidityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, comorbidityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComorbidityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
