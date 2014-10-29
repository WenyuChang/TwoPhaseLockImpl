package Util;

import java.util.ArrayList;
import java.util.List;

import Model.DBModel;
import Model.OperationVO;

public class TransactionsOperator {
	
	static int transNumber;
	static int opsNumber;
	
	public List<OperationVO> transaction = new ArrayList<OperationVO>();
	public List<List<OperationVO>> transactions = new ArrayList<List<OperationVO>>();
	
	public TransactionsOperator(int transNumber, int opsNumber)
	{
		this.transNumber = transNumber;
		this.opsNumber = opsNumber;
	}
	
	public void createAllTransaction()
	{	
		for(int j = 0; j < transNumber; j++)
		{
			List<OperationVO> transaction = new ArrayList<OperationVO>();
		    for(int i = 0; i < opsNumber; i++)
		    {
		    	OperationVO action = new OperationVO();
		    	action.id = (j+1)*10 + i;
		    	int random = (int)(Math.random() * 2) + 1;
		    	if(random == 1)
		    	{
		    		action.operation = "W";
		    		action.value = (int)(Math.random() * 10) + 1;
		    	}
		    	else
		    	{
		    		action.operation = "R";
		    	}
		    	int random2 = (int)(Math.random() * 2) + 1;
		    	if(random2 == 1)
		    		action.element = "E1";
		    	else
		    		action.element = "E2";
		    	transaction.add(action);
		    }
		    transactions.add(transaction);
		}
		printTransaction();
	}
	
	public void printTransaction(List<List<OperationVO>> _transactions)
	{
		int _transNumber = _transactions.size();
		
		for(int j = 0; j < _transNumber; j++)
		{
			System.out.println("****** Start of Transaction " + (j+1) + " ******");
		    for(int i = 0; i < _transactions.get(j).size(); i++)
		    {
		    	String operation = _transactions.get(j).get(i).operation;
		    	String element = _transactions.get(j).get(i).element;
		    	if(operation == "W")
		    	{
		    		int value = _transactions.get(j).get(i).value;
		    		System.out.print(operation + "(" + element + "," + value + ");");
		    	}
		    	else
		    	{
		    		System.out.print(operation + "(" + element + ");");
		    	}
		    }
		    System.out.println("\n****** End of Transaction " + (j+1) + " ******\n");
		}
	}
	
	public void printTransaction()
	{
		for(int j = 0; j < transNumber; j++)
		{
			System.out.println("****** Start of Transaction " + (j+1) + " ******");
		    for(int i = 0; i < opsNumber; i++)
		    {
		    	String operation = transactions.get(j).get(i).operation;
		    	String element = transactions.get(j).get(i).element;
		    	if(operation == "W")
		    	{
		    		int value = transactions.get(j).get(i).value;
		    		System.out.print(operation + "(" + element + "," + value + ");");
		    	}
		    	else
		    	{
		    		System.out.print(operation + "(" + element + ");");
		    	}
		    }
		    System.out.println("\n****** End of Transaction " + (j+1) + " ******\n");
		}
	}
	
	public void executeInTurn()
	{
		System.out.println("****** Start to Execute Transaction in Turn ******");
		DBModel.clear();
		System.out.println("The Initial DB State is: E1 = " + DBModel.getE1() + ", E2 = " + DBModel.getE2());  
		char[] buf = new char[transNumber];
		for(int i=0;i<transactions.size();i++)
		{
			buf[i] = (char)i;
		}
		AllPermutation allPermutation = new AllPermutation(opsNumber, transactions);
		allPermutation.printPermutation(buf,0,buf.length-1);
		System.out.println("****** End to Execute Transaction in Turn ******");
	}
	
	public void executeInParallel(boolean ifThread, int repeatTimes)
	{
		System.out.println("\n****** Start to Execute Transaction in Parallel(No Lock) ******");
		DBModel.clear();
		System.out.println("The Initial DB State is: E1 = " + DBModel.getE1() + ", E2 = " + DBModel.getE2());  
		
		if(!ifThread)
		{
			executeInParallel_NoThread(repeatTimes);
		}
		else
		{
			executeInParallel_Thread(false, repeatTimes);
		}
		
		System.out.print("\nThe Final State is");
        System.out.println(" E1 = " + DBModel.getE1() + ", E2 = " + DBModel.getE2());
		System.out.println("****** End to Execute Transaction in Parallel(No Lock) ******");
		
		DBModel.clear();
		executeInParallel_Thread(false, repeatTimes);
		
		System.out.print("\nThe Final State is");
        System.out.println(" E1 = " + DBModel.getE1() + ", E2 = " + DBModel.getE2());
		System.out.println("****** End to Execute Transaction in Parallel(No Lock) ******");
	}
	
	private boolean testThreadAlive(Thread[] t)
	{
		for(int i=0;i<transNumber;i++)
		{
			if(t[i].isAlive())
				return false;
		}
		return true;
	}
	
	public void executeInParallel_Thread(boolean ifSynchronized, int repeatTimes)
	{
		Thread[] t = new Thread[transNumber];
		System.out.print("The Order of Operations is: \n    ");
		for(int i=0;i<transNumber;i++)
		{
			if(!ifSynchronized)
			{
				ExecuteThread exThread = new ExecuteThread();
				exThread.setNum(i);
				t[i] = new Thread(exThread);
			}
			else
			{
				ExecuteThreadSynchronized exThread = new ExecuteThreadSynchronized();
				exThread.setNum(i);
				t[i] = new Thread(exThread);
			}
			t[i].start();
		}
		while(!testThreadAlive(t))
		{
			try{
				this.wait();
			}catch(Exception e){
			}
		}
	}
	
	public void executeInParallel_NoThread(int repeatTimes)
	{
		for(int rep=0;rep<repeatTimes;rep++)
		{
			List<Integer> hasExec = new ArrayList<Integer>();
			List<List<OperationVO>> tmpTrans = new ArrayList<List<OperationVO>>();
			int opsCount = 0;
			for(int i=0;i<transactions.size();i++)
		 	{
				List<OperationVO> actionList = new ArrayList<OperationVO>();
		 		for(int j=0;j<transactions.get(i).size();j++)
		 		{
		 		    OperationVO action = new OperationVO();	
		 		    action.id = transactions.get(i).get(j).id;
		 		    action.element = transactions.get(i).get(j).element;
		 		    action.operation = transactions.get(i).get(j).operation;
		 			action.value = transactions.get(i).get(j).value;
		 			actionList.add(action);	
		 			opsCount++;
		 		}
		 		tmpTrans.add(actionList);
		 	}
			
			System.out.print("The Order of Operations is: \n    ");
	 		
			while(opsCount--!=0)
	 		{
	 			int transRandom = (int)(Math.random() * transNumber);
	 			
	 			if(tmpTrans.get(transRandom).size()!=0)
	 			{
	 				if(null != tmpTrans.get(transRandom).get(0))
	 				{
	 					if(tmpTrans.get(transRandom).get(0).operation == "W")
	 					{
	 						System.out.print(tmpTrans.get(transRandom).get(0).operation +
	 								(transRandom+1) + "(" + tmpTrans.get(transRandom).get(0).element + "," + tmpTrans.get(transRandom).get(0).value + ");");
	 						if(tmpTrans.get(transRandom).get(0).element == "E1")
	 							DBModel.setE1(tmpTrans.get(transRandom).get(0).value);
	 						if(tmpTrans.get(transRandom).get(0).element == "E2")
	 							DBModel.setE2(tmpTrans.get(transRandom).get(0).value);
	 					}	
	 					else
	 					{
	 						System.out.print(tmpTrans.get(transRandom).get(0).operation +
	 						           (transRandom+1) + "(" + tmpTrans.get(transRandom).get(0).element +");");
	 					}
	 					
	 					if(tmpTrans.get(transRandom).get(0) != null)	
	 						tmpTrans.get(transRandom).remove(0);
	 				}
	 			}
	 		}
		}
	}
	
	class ExecuteThread extends Thread
	{
		private int _transactionNum = 0;
		public void setNum(int transactionNum)
		{
			_transactionNum = transactionNum;
		}
		public void run()
		{
			List<List<OperationVO>> tmpTrans = new ArrayList<List<OperationVO>>();
			int opsCount = 0;
			List<OperationVO> actionList = new ArrayList<OperationVO>();

			for(int i=0;i<transactions.get(_transactionNum).size();i++)
			{
				OperationVO opVO = transactions.get(_transactionNum).get(i);
				if(null != opVO)
 				{
 					if(opVO.operation == "W")
 					{
 						System.out.print(opVO.operation + (_transactionNum+1) + "(" + opVO.element + "," + opVO.value + ");");
 						if(opVO.element == "E1")
 							DBModel.setE1(opVO.value);
 						if(opVO.element == "E2")
 							DBModel.setE2(opVO.value);
 					}	
 					else
 					{
 						System.out.print(opVO.operation + (_transactionNum+1) + "(" + opVO.element +");");
 					}
 				}
 			}
		}
	}
	
	
	class ExecuteThreadSynchronized extends Thread
	{
		private int _transactionNum = 0;
		public void setNum(int transactionNum)
		{
			_transactionNum = transactionNum;
		}
		public void run()
		{
			synchronized(this)
			{
				List<List<OperationVO>> tmpTrans = new ArrayList<List<OperationVO>>();
				int opsCount = 0;
				List<OperationVO> actionList = new ArrayList<OperationVO>();
				
				
				for(int i=0;i<transactions.get(_transactionNum).size();i++)
				{
					OperationVO opVO = transactions.get(_transactionNum).get(i);
					if(null != opVO)
	 				{
	 					if(opVO.operation == "W")
	 					{
	 						System.out.print(opVO.operation + (_transactionNum+1) + "(" + opVO.element + "," + opVO.value + ");");
	 						if(opVO.element == "E1")
	 							DBModel.setE1(opVO.value);
	 						if(opVO.element == "E2")
	 							DBModel.setE2(opVO.value);
	 					}	
	 					else
	 					{
	 						System.out.print(opVO.operation + (_transactionNum+1) + "(" + opVO.element +");");
	 					}
	 				}
	 			}
			}
		}
	}
}
