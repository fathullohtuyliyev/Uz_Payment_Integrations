package integrations.integrations.Models.Payme.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    // it should be something you wrote in the accept type in payme business account
    private String account;
}