package Util;

import java.util.ArrayList;
import java.util.List;

import Model.OperationVO;

public class LockingOperation {
	
	public int transNumber;
	public int opsNumber;
	public final int ACTS_NUMBER = 10;
	public List<List<OperationVO>> transactions = new ArrayList<List<OperationVO>>();
	
	public LockingOperation(int transNumber, int opsNumber, List<List<OperationVO>> transactions)
	{
		this.transNumber = transNumber;
		this.opsNumber = opsNumber;
		this.transactions = transactions;
	}
	
	public int firstElementIndex(String element,int transIndex)
    {
    	int index = 0;
    	for(int j = 0; j < opsNumber; j++)
		{
	    	if(transactions.get(transIndex).get(j).element == element)
	    		return j;
		}
    	return index;
    }
    
    public int lastElementIndex(String element,int transIndex)
    {
    	int index = 0;
    	for(int j = 0; j < opsNumber; j++)
		{
	    	if(transactions.get(transIndex).get(j).element == element)
	    		index = j;
		}
    	return index;
    }
	
	public List<List<Model.OperationVO>> LockingTable()
    {
    	for(int i = 0; i < transNumber; i++)
		{
    		int firstE1 = firstElementIndex("E1",i);
    	    int lastE1  = lastElementIndex ("E1",i)+1;
    	    int firstE2 = firstElementIndex("E2",i);
    	    int lastE2  = lastElementIndex ("E2",i)+1;
    	    if(firstE1 <= firstE2)
    	    	firstE2++;
    	    if(lastE1 <= lastE2)
    	    	lastE2++;
    	    	
    	    //Lock E1
	    	OperationVO action = new OperationVO();
	    	action.operation = "L";
            action.element = "E1";
            for(int k = opsNumber; k > firstE1; k--)
	    	{
	    		OperationVO preaction = new OperationVO();
	    		preaction = transactions.get(i).get(k-1);
	    		if(k == opsNumber)
	    		{
	    			transactions.get(i).add(preaction);
	    			opsNumber++;
	    		}
	    		else
	    		{
	    			transactions.get(i).set(k, preaction);
	    		}
	    	}
            transactions.get(i).set(firstE1, action);
                
            //unLock E1
            OperationVO unlockA = new OperationVO();
            unlockA.operation = "ul";
            unlockA.element = "E1";
               
            if(lastE1 == ACTS_NUMBER)
            {
               	transactions.get(i).add(unlockA);
    			opsNumber++;
            }
            else
            {
                for(int k = opsNumber; k > lastE1; k--)
		    	{
		    		OperationVO laction = new OperationVO();
		    		laction = transactions.get(i).get(k-1);
		    		if(k == opsNumber)
		    		{
		    			transactions.get(i).add(laction);
		    			opsNumber++;
		    		}
		    		else
		    		{
		    			transactions.get(i).set(k,laction);
		    		}
		    	}
                transactions.get(i).set(lastE1+1, unlockA);
            }
                
            //Lock E2
            OperationVO action2 = new OperationVO();
	    	action2.operation = "L";
            action2.element = "E2";
	    	for(int k = opsNumber; k > firstE2; k--)
    	    {
	    		OperationVO lastaction = new OperationVO();
	    		lastaction = transactions.get(i).get(k-1);
	    		
	    		if(k == opsNumber)
	    		{
	    			transactions.get(i).add(lastaction);
	    			opsNumber++;
	    		}
	    		else
	    		{
	    			transactions.get(i).set(k, lastaction);
	    		}
	    	}
	    	transactions.get(i).set(firstE2, action2);
	    		
	        //unLock E2
            OperationVO unlockB = new OperationVO();
            unlockB.operation = "ul";
            unlockB.element = "B";
                
            if(lastE2 == ACTS_NUMBER)
            {
               	transactions.get(i).add(unlockB);
    			opsNumber++;
            }
            else
            {
                lastE2++;
                for(int k = opsNumber; k > lastE2; k--)
		    	{
		    		OperationVO paction = new OperationVO();
		    		paction = transactions.get(i).get(k-1);
		    		if(k == opsNumber)
		    		{
		    			transactions.get(i).add(paction);
		    			opsNumber++;
		    		}
		    		else
		    		{
		    			transactions.get(i).set(k,paction);
		    		}
		    	}
                transactions.get(i).set(lastE2+1, unlockB);
            }
		}
    	return transactions;
    }
}
