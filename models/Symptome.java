package ma.ensa.lis.models;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@ToString

public class Symptome {

    private String name;

    @Override
    public String toString() {
        return "Symptome{name='" + name + "'}";
    }
}

