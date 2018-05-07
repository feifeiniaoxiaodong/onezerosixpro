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

	private boolean isConnect1 = false;//ϵͳ�Ƿ�����
	private boolean isConnect2 = false;
	private boolean isConnect3 = false;
	private boolean isConnect4 = false;
	private boolean isConnect5 = false;
	
	private CreateThread1 ct1 = null;//�����߳�
	private CreateThread2 ct2 = null;
	private CreateThread3 ct3 = null;
	private CreateThread4 ct4 = null;
	private CreateThread5 ct5 = null;
	
	public Socket socket1 = null;//����ָ���socket
	public Socket socket2 = null;
	public Socket socket3 = null;
	public Socket socket4 = null;
	public Socket socket5 = null;
	
	public boolean flag1 = false;//���������Ƿ���ɵı�־
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
	
	private Map<String, String> map= new HashMap<String, String>();//����WARN�����ļ�
	private Map<Integer,String> ip_port = null;//����IP  �˿�
	private List<Map> list_ip_port = new ArrayList<Map>();
	//����ģʽ ȷ��ȫ��ֻ��һ������ʵ��
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
	 * �ͻ������ӷ�����
	 * ����1~5ѡ��ϵͳ������IP������˿ں�
	 * �ɹ�����0�����򷵻�1����socket error������
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws InterruptedException 
	 */
	public int ClientConnectServer(int robot_num,String ip_num,int port_num) throws UnknownHostException, IOException, InterruptedException {
		int isCon = 1;
		ip_port = new HashMap<Integer,String>();
		ip_port.put(port_num, ip_num);
		list_ip_port.add(ip_port);
		
		init_socket(robot_num,ip_num,port_num);//��ʼ��socket
		//��������״̬��
		switch(robot_num){
		case 1 : isConnect1 = true;//�ı�ϵͳ����״̬
				 ct1 = new CreateThread1(socket1,ip_num,port_num);
				 ct1.start();//�����߳�
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
	//��ʼ�� ����ָ��� socket
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
	 * �Ͽ�����������
	 * ����1~5ѡ��ϵͳ
	 * @throws IOException 
	 */
	public void DeleteServer(int robot_num) throws IOException{
		System.out.println("�Ͽ�"+robot_num+"������");
		//��������״̬��
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
	 * �������ӷ�����
	 * ����1~5ѡ��ϵͳ
	 * �ɹ�����TRUE  ʧ�ܷ���FALSE
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public boolean ReConnectServer(int robot_num) throws UnknownHostException, IOException{
		
		String ip_num = null;//ip
		int port_num = 0;//�˿�
		boolean isConnect = false;//ϵͳ����״̬
		Map<Integer, String> m = list_ip_port.get(robot_num-1);//��ȡip���˿�
		
		//������ ����key   �˿�
		Iterator<Integer> key = m.keySet().iterator();
		while(key.hasNext()){
			port_num = key.next();
		}
		//������ ����values  IP
		Iterator<String> values = m.values().iterator();
		while(values.hasNext()){
			ip_num = values.next();
		}
		init_socket(robot_num,ip_num,port_num);//��ʼ��socket
		//��������״̬ �� �����̣߳�
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
	 * �ж��Ƿ����ӷ�����
	 * ����1~5����ʾѡ��1~5��ϵͳ
	 * ����TRUE��ʾ���ӣ�����FALSE��ʾ�Ͽ�
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
	 * ��ȡ��ǰ����Ϣ
	 * ����1~5ѡ��ϵͳ
	 * ��������Ϣ���� �� null
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
	 * ��ȡ��ǰϵͳ��Ϣ
	 * ����1~5ѡ��ϵͳ
	 * ����ϵͳ��Ϣ���� ��null
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
	 * ��ȡ��ǰϵͳ������Ϣ
	 * ����1~5ѡ��ϵͳ
	 * ���ر�����Ϣ���� �� null
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
	 * ����ָ��
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
	 * ����ָ��
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
	 * ��ȡWARN�����ļ�  ��put��HashMap
	 */
	@Test
	public void LoadWarn(){
		File f = new File("c:/3100MC/WARN");
		try{
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String str = null;
			String[] strs = null;
			while((str = br.readLine()) != null){//���ж�ȡ�ļ�
				strs = str.split(",");//�г������ַ���  ������String����
				map.put(strs[0].toString(), strs[1].toString());
			}
			if(!map.isEmpty()){
//				System.out.println("�����ļ���ȡ�ɹ�");
			}else{
				System.out.println("�����ļ���ȡʧ�ܣ�ȷ���ļ�·����c:/3100MC/WARN");
			}
			
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	//�������  ��ȡ�����ļ�
	public String WarnInfo(String num){
		map.clear();
		LoadWarn();//���ر����ļ�
		return map.get(num);
	}
}
