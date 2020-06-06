package bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
//entity 2
public class BankTransaction {
    @Id
private long id;
private int accountId;
private Date transDate;
@Transient
private  String strTransacionDate;
private String transType;
private double amount;
}
