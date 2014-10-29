import java.util.ArrayList;
import java.util.List;

import Model.OperationVO;
import Util.LockingOperation;
import Util.TransactionsOperator;


public class MainClass {
	
	public static final int TRANSATION_NUMBER = 3;
	public static final int OPS_NUMBER = 8;
	public static final int REPEAT_TIMES = 3;
	
	public static void main(String[] args) {
		TransactionsOperator TranOp = new TransactionsOperator(TRANSATION_NUMBER, OPS_NUMBER);
		
		TranOp.createAllTransaction();

		TranOp.executeInTurn();
		
		//TranOp.executeInParallel(false, REPEAT_TIMES);
		TranOp.executeInParallel(true, REPEAT_TIMES);
		
//		System.out.println("\n****** Start to Imitation Locking the Transaction");
//		LockingOperation lockOp = new LockingOperation(TRANSATION_NUMBER, OPS_NUMBER, TranOp.transactions);
//		TranOp.printTransaction(lockOp.LockingTable());
//		System.out.println("\n****** End to Imitation Locking the Transaction");
	}

}
