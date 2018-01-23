package rick.expensestrackerv2.Domain.Model;

/**
 * Created by Rick on 1/21/2018.
 */

public class BillModel {

    String billName;
    boolean isPaid;

    public BillModel(String billName, boolean isPaid) {
        this.billName = billName;
        this.isPaid = isPaid;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "BillModel{" +
                "billName='" + billName + '\'' +
                ", isPaid=" + isPaid +
                '}';
    }
}
