package DataTransferTest;

import ilog.concert.IloException;

import java.text.ParseException;

public class Test {

    public static void main(String[] args) throws ParseException, IloException {
        Data data = new Data(10,20,60,30);
        Model model = new Model(data);
        model.createModel();
        model.solve();
    }
}
