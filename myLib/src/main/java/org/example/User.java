package org.example;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author Oksiuta Andrii
 * 30.12.2022
 * 10:52
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

  private String name;
  private String eMail;
  private String password;
  private LocalDate birthDay;
  private List<String> interestTags;

}
