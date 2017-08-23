package sigit.task_4_aplikasi.model;

import java.io.Serializable;

/**
 * Created by sigit on 24/07/17.
 */

public class Income implements Serializable {
    private String description;
    public int amount;

    public Income() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
