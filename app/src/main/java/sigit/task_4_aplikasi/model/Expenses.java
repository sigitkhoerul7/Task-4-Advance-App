package sigit.task_4_aplikasi.model;

/**
 * Created by sigit on 24/07/17.
 */

public class Expenses {

    public String description;
    public int amount;


    public Expenses() {

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


