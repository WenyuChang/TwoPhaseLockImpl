package Util;

import java.util.ArrayList;
import java.util.List;

import Model.DBModel;
import Model.OperationVO;

public class AllPermutation {
	private static int _opsNumber;
	private static List<List<OperationVO>> _transactions = new ArrayList<List<OperationVO>>();
	
	public AllPermutation(int opsNumber, List <List<OperationVO>> transactions)
    {
		_opsNumber = opsNumber;
		_transactions = transactions;
    }
	
    public void printPermutation(char[] buf,int start,int end)
    {
        if(start==end)
        {
        	System.out.print("Order of Transactions: ( ");
            for(int i=0;i<=end;i++)
            {     
                System.out.print("T" + ((int)buf[i]+1) + " " );  
                schedulingInTurn((int)buf[i]);
            }
            System.out.print(") -->  The Final State is£º");
            System.out.println("E1 = " + DBModel.getE1() + ", E2 = " + DBModel.getE2());      
        }     
        else
        {
            for(int i=start;i<=end;i++)
            {    
                char temp=buf[start];   
                buf[start]=buf[i];     
                buf[i]=temp;     
                     
                printPermutation(buf,start+1,end);  
    
                temp=buf[start];
                buf[start]=buf[i];     
                buf[i]=temp;     
            }     
        }     
    }     
    
	public static void schedulingInTurn(int serial)
	{
		int i = serial;
		for(int j=0; j<_opsNumber; j++)
		{
			if(_transactions.get(i).get(j).operation == "W")
			{
				if(_transactions.get(i).get(j).element == "E1")
				{
					DBModel.setE1(_transactions.get(i).get(j).value);
				}
				if(_transactions.get(i).get(j).element == "E2")
				{
					DBModel.setE2(_transactions.get(i).get(j).value);
				}
			}
		}
	}
}
