package function;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;



import bean.ALARM_INFO;
import bean.AXIS_INFO;
import bean.SYSTEM_INFO;

public class Start {

	private boolean isConnect1 = false;//系统是否连接
	private boolean isConnect2 = false;
	private boolean isConnect3 = false;
	private boolean isConnect4 = false;
	private boolean isConnect5 = false;
	
	private CreateThread1 ct1 = null;//接受线程
	private CreateThread2 ct2 = null;
	private CreateThread3 ct3 = null;
	private CreateThread4 ct4 = null;
	private CreateThread5 ct5 = null;
	
	public Socket socket1 = null;//发送指令的socket
	public Socket socket2 = null;
	public Socket socket3 = null;
	public Socket socket4 = null;
	public Socket socket5 = null;
	
	public boolean flag1 = false;//接收数据是否完成的标志
	public boolean flag2 = false;
	public boolean flag3 = false;
	public boolean flag4 = false;
	public boolean flag5 = false;
	
	public AXIS_INFO ax1 = null;
	public AXIS_INFO ax2 = null;
	public AXIS_INFO ax3 = null;
	public AXIS_INFO ax4 = null;
	public AXIS_INFO ax5 = null;
	public ALARM_INFO al1 = null;
	public ALARM_INFO al2 = null;
	public ALARM_INFO al3 = null;
	public ALARM_INFO al4 = null;
	public ALARM_INFO al5 = null;
	public SYSTEM_INFO sy1 = null;
	public SYSTEM_INFO sy2 = null;
	public SYSTEM_INFO sy3 = null;
	public SYSTEM_INFO sy4 = null;
	public SYSTEM_INFO sy5 = null;
	
	public int isServerClose = 1;
	
	private Map<String, String> map= new HashMap<String, String>();//保存WARN报警文件
	private Map<Integer,String> ip_port = null;//保存IP  端口
	private List<Map> list_ip_port = new ArrayList<Map>();
	//单例模式 确保全局只有一个该类实例
	private static Start instance = null;
	private Start(){
		ax1 = new AXIS_INFO();
		ax2 = new AXIS_INFO();
		ax3 = new AXIS_INFO();
		ax4 = new AXIS_INFO();
		ax5 = new AXIS_INFO();
		al1 = new ALARM_INFO();
		al2 = new ALARM_INFO();
		al3 = new ALARM_INFO();
		al4 = new ALARM_INFO();
		al5 = new ALARM_INFO();
		sy1 = new SYSTEM_INFO();
		sy2 = new SYSTEM_INFO();
		sy3 = new SYSTEM_INFO();
		sy4 = new SYSTEM_INFO();
		sy5 = new SYSTEM_INFO();
		socket1 = new Socket();
	}
	public static Start getInstance(){
		if(instance == null){
			instance = new Start();
		}
		return instance;
	}
	
	/**
	 * 客户端连接服务器
	 * 输入1~5选择系统，输入IP，输入端口号
	 * 成功返回0，否则返回1或者socket error报警码
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws InterruptedException 
	 */
	public int ClientConnectServer(int robot_num,String ip_num,int port_num) throws UnknownHostException, IOException, InterruptedException {
		int isCon = 1;
		ip_port = new HashMap<Integer,String>();
		ip_port.put(port_num, ip_num);
		list_ip_port.add(ip_port);
		
		init_socket(robot_num,ip_num,port_num);//初始化socket
		//保存连接状态；
		switch(robot_num){
		case 1 : isConnect1 = true;//改变系统连接状态
				 ct1 = new CreateThread1(socket1,ip_num,port_num);
				 ct1.start();//开启线程
				 Thread.sleep(500);
				 isCon = isServerClose;
				 break;
		case 2 : isConnect2 = true;
				 ct2 = new CreateThread2(socket2,ip_num,port_num);
				 ct2.start();
				 Thread.sleep(500);
				 isCon = isServerClose;
				 break;
		case 3 : isConnect3 = true; 
				 ct3 = new CreateThread3(socket3,ip_num,port_num);
				 ct3.start();
				 Thread.sleep(500);
				 isCon = isServerClose;
				 break;
		case 4 : isConnect4 = true; 
				 ct4 = new CreateThread4(socket4,ip_num,port_num);
				 ct4.start();
				 Thread.sleep(500);
				 isCon = isServerClose;
				 break;
		case 5 : isConnect5 = true; 
				 ct5 = new CreateThread5(socket5,ip_num,port_num);
				 ct5.start();
				 Thread.sleep(500);
				 isCon = isServerClose;
				 break;
		default:System.out.println("option rang from 1~5");
		break;
		}
		return isCon;
		
	}
	//初始化 发送指令的 socket
	private void init_socket(int tag,String ip_num,int port_num) throws UnknownHostException, IOException{
		InetSocketAddress endpoint = new InetSocketAddress(ip_num, port_num);
		switch(tag){
		case 1 : 
			socket1.connect(endpoint);
//			socket1=new Socket(ip_num,port_num);
			
//			socket1 = new Socket(ip_num,port_num); 
			break;
		case 2 : socket2 = new Socket(ip_num,port_num); break;
		case 3 : socket3 = new Socket(ip_num,port_num); break;
		case 4 : socket4 = new Socket(ip_num,port_num); break;
		case 5 : socket5 = new Socket(ip_num,port_num); break;
		}
	}
	/**
	 * 断开服务器连接
	 * 输入1~5选择系统
	 * @throws IOException 
	 */
	public void DeleteServer(int robot_num) throws IOException{
		System.out.println("断开"+robot_num+"号连接");
		//保存连接状态；
		switch(robot_num){
		case 1 : isConnect1 = false;  socket1.close(); ct1.stop(); break;
		case 2 : isConnect2 = false;  socket2.close(); ct2.stop(); break;
		case 3 : isConnect3 = false;  socket3.close(); ct3.stop(); break;
		case 4 : isConnect4 = false;  socket4.close(); ct4.stop(); break;
		case 5 : isConnect5 = false;  socket5.close(); ct5.stop(); break;
		default: System.out.println("option rang from 1~5"); break;
		}
	}
	
	/**
	 * 重新连接服务器
	 * 输入1~5选择系统
	 * 成功返回TRUE  失败返回FALSE
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public boolean ReConnectServer(int robot_num) throws UnknownHostException, IOException{
		
		String ip_num = null;//ip
		int port_num = 0;//端口
		boolean isConnect = false;//系统连接状态
		Map<Integer, String> m = list_ip_port.get(robot_num-1);//获取ip及端口
		
		//迭代器 遍历key   端口
		Iterator<Integer> key = m.keySet().iterator();
		while(key.hasNext()){
			port_num = key.next();
		}
		//迭代器 遍历values  IP
		Iterator<String> values = m.values().iterator();
		while(values.hasNext()){
			ip_num = values.next();
		}
		init_socket(robot_num,ip_num,port_num);//初始化socket
		//更改连接状态 并 开启线程；
		switch(robot_num){
			case 1 : isConnect1 = true; isConnect = isConnect1; 
					 ct1 = new CreateThread1(socket1,ip_num,port_num);
					 ct1.start();
			break;
			case 2 : isConnect2 = true; isConnect = isConnect2;
					 ct2 = new CreateThread2(socket2,ip_num,port_num);
					 ct2.start();
			break;
			case 3 : isConnect3 = true; isConnect = isConnect3;
					 ct3 = new CreateThread3(socket3,ip_num,port_num);
					 ct3.start();
			break;
			case 4 : isConnect4 = true; isConnect = isConnect4;
					 ct4 = new CreateThread4(socket4,ip_num,port_num);
					 ct4.start();
			break;
			case 5 : isConnect5 = true; isConnect = isConnect5; 
					 ct5 = new CreateThread5(socket5,ip_num,port_num);
					 ct5.start();
			break;
			default:System.out.println("option rang from 1~5");
			break;
		}
		return isConnect;
	}
	/**
	 * 判断是否连接服务器
	 * 输入1~5，表示选择1~5号系统
	 * 返回TRUE表示连接，返回FALSE表示断开
	 */
	public boolean SocketKeepalive(int tag){
		boolean isConnect = false;
		switch(tag){
			case 1 : isConnect = isConnect1;	break;
			case 2 : isConnect = isConnect2;	break;
			case 3 : isConnect = isConnect3;	break;
			case 4 : isConnect = isConnect4;	break;
			case 5 : isConnect = isConnect5;	break;
			default:System.out.println("option rang from 1~5"); break;
		}
		return isConnect;
	}
	/**
	 * 获取当前轴信息
	 * 输入1~5选择系统
	 * 返回轴信息对象 或 null
	 * @throws InterruptedException 
	 */
	public AXIS_INFO GetAxisinfo(int tag) throws InterruptedException{
		char[] a = {0x55,0xAA,0x01,0x00};
//		byte [] aa={0x55,(byte)0xAA,0x01,0x00};
		
		String s1 = String.valueOf(a[0]);		
		String s2 = String.valueOf(a[1]);
		String s3 = String.valueOf(a[2]);
		String s4 = String.valueOf(a[3]);
		String s = s1+s2+s3+s4;
		
		switch(tag){
		case 1 : Send(socket1, s);
				 while(!flag1){
					 Thread.sleep(500);
				 }
				 flag1 = false;
				 return ax1;
		case 2 : Send(socket2, s);
				 while(!flag2){
					 Thread.sleep(500);
				 }
				 flag2 = false;
				 return ax2;
		case 3 : Send(socket3, s);
				 while(!flag3){
					 Thread.sleep(500);
				 }
				 flag3 = false;
				 return ax3;
		case 4 : Send(socket4, s);
				 while(!flag4){
					 Thread.sleep(500);
				 }
				 flag4 = false;
				 return ax4;
		case 5 : Send(socket5, s);
				 while(!flag5){
					 Thread.sleep(500);
				 }
				 flag5 = false;
				 return ax5;
		default:System.out.println("option rang from 1~5");
			break;	
		}
		return null;
	}
	
	
	/**
	 * 获取当前系统信息
	 * 输入1~5选择系统
	 * 返回系统信息对象 或null
	 * @throws InterruptedException 
	 */
	public SYSTEM_INFO GetSysteminfo(int tag) throws InterruptedException{
		char[] a = {0x55,0xAA,0x03,0x00};
		String s1 = String.valueOf(a[0]);
		String s2 = String.valueOf(a[1]);
		String s3 = String.valueOf(a[2]);
		String s4 = String.valueOf(a[3]);
		String s = s1+s2+s3+s4;
		switch(tag){
		case 1 : Send(socket1, s);
				 while(!flag1){
					 Thread.sleep(500);
				 }
				 flag1 = false;
				 return sy1;
		case 2 : Send(socket2, s);
				 while(!flag2){
					 Thread.sleep(500);
				 }
				 flag2 = false;
				 return sy2;
		case 3 : Send(socket3, s);
				 while(!flag3){
					 Thread.sleep(500);
				 }
				 flag3 = false;
				 return sy3;
		case 4 : Send(socket4, s);
				 while(!flag4){
					 Thread.sleep(500);
				 }
				 flag4 = false;
				 return sy4;
		case 5 : Send(socket5, s);
				 while(!flag5){
					 Thread.sleep(500);
				 }
				 flag5 = false;
				 return sy5;
		default:System.out.println("option rang from 1~5");
			break;	
		}
		return null;		
	}
	/**
	 * 获取当前系统报警信息
	 * 输入1~5选择系统
	 * 返回报警信息对象 或 null
	 * @throws InterruptedException 
	 */
	public ALARM_INFO GetAlarminfo(int tag) throws InterruptedException{
		char[] a = {0x55,0xAA,0x02,0x00};
		String s1 = String.valueOf(a[0]);
		String s2 = String.valueOf(a[1]);
		String s3 = String.valueOf(a[2]);
		String s4 = String.valueOf(a[3]);
		String s = s1+s2+s3+s4;
		switch(tag){
		case 1 : Send(socket1, s);
				while(!flag1){
					 Thread.sleep(500);
				 }
				 flag1 = false;
				 return al1;
		case 2 : Send(socket2, s);
				 while(!flag2){
					 Thread.sleep(500);
				 }
				 flag2 = false;
				 return al2;
		case 3 : Send(socket3, s);
				 while(!flag3){
					 Thread.sleep(500);
				 }
				 flag3 = false;
				 return al3;
		case 4 : Send(socket4, s);
				 while(!flag4){
					 Thread.sleep(500);
				 }
				 flag4 = false;
				 return al4;
		case 5 : Send(socket5, s);
				 while(!flag5){
					 Thread.sleep(500);
				 }
				 flag5 = false;
				 return al5;
		default:System.out.println("option rang from 1~5");
			break;	
		}
		return null;		
	}

	/**
	 * 发送指令
	 */
	public boolean Send(Socket socket,String str){
		OutputStream outt=null;
		OutputStreamWriter write=null;
		try {
		/*	PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			out.print(str);*/
			//add by wei 2018/5/4 -->			
			outt=socket.getOutputStream();
			write=new OutputStreamWriter(outt,"gbk");

			write.write(str);
			write.flush();
			//<---add by wei 2018/5/4	
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{			
			/*if(write!=null){
				try {
					write.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(outt!=null){
				try {
					outt.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}*/			
		}
	}

	/**
	 * 发送指令
	 */
	/*public boolean Send(Socket socket,char[] commands){
		OutputStream outt=null;
		OutputStreamWriter write=null;
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			out.print(commands);
//			out.flush();
			
			outt=socket.getOutputStream();
			write=new OutputStreamWriter(outt,"gbk");

			write.write(commands);
			write.flush();
						
			return true;
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
	}*/
	

	/**
	 * 读取WARN报警文件  并put到HashMap
	 */
	@Test
	public void LoadWarn(){
		File f = new File("c:/3100MC/WARN");
		try{
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String str = null;
			String[] strs = null;
			while((str = br.readLine()) != null){//整行读取文件
				strs = str.split(",");//切成两个字符串  ，放入String数组
				map.put(strs[0].toString(), strs[1].toString());
			}
			if(!map.isEmpty()){
//				System.out.println("报警文件读取成功");
			}else{
				System.out.println("报警文件读取失败，确认文件路径：c:/3100MC/WARN");
			}
			
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	//根据序号  读取报警文件
	public String WarnInfo(String num){
		map.clear();
		LoadWarn();//加载报警文件
		return map.get(num);
	}
}
