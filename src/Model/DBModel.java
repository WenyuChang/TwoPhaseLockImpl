package Model;

public class DBModel {
	public static int E1=0;
	public static int E2=0;
	
	private static char E1IfLock = 'U';
	private static char E2IfLock = 'U';
	
	public static int getE1()
	{
		return E1;
	}
	public static int getE2()
	{
		return E2;
	}
	public static void setE1(int _e1)
	{
		E1 = _e1;
	}
	public static void setE2(int _e2)
	{
		E2 = _e2;
	}
	public static char getIfLockE1()
	{
		return E1IfLock;
	}
	public static char getIfLockE2()
	{
		return E2IfLock;
	}
	public static void setIfLockE1(char lockType)
	{
		E1IfLock = lockType;
	}
	public static void setIfLockE2(char lockType)
	{
		E2IfLock = lockType;
	}
	
	
	public static void clear()
	{
		DBModel.setE1(0);
		DBModel.setE2(0);
	}
	
}
