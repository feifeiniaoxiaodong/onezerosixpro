package function;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;

public class CreateThread3 extends Thread{
	public String ip = null;//IP
	public Integer port = null;//端口
	private Socket socket = null;//套接字
	private boolean close = false;//关闭连接 标志位
	private String time = null;
	
	public CreateThread3(){		init();		}
	
	public CreateThread3(Socket socket,String ip,Integer port){
		this.socket = socket;
		this.ip = ip;
		this.port = port;
		init();
	}
	//初始化socket
	public void init(){
		try {
			socket.setKeepAlive(true);//开启保持活动状态的套接字
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while(true){
			close = isServerClose(socket);//判断是否断开
			if(!close){
				String readtext = R(socket);
				if(readtext != null && readtext.trim().length()>0){
//					System.out.println("3号读取数据："+readtext.trim());
					string_dispose(readtext);//数据处理
				}
			}else{
			}
		}
	}
	//倒置字符串
	private int Dao_string(String s){
		String ss  = "";
		String s1 = s.substring(6, 8);
		String s2 = s.substring(4, 6);
		String s3 = s.substring(2, 4);
		String s4 = s.substring(0, 2);
		ss = s1+s2+s3+s4;
		//有符号十六进制转成有符号十进制
		BigInteger i = new BigInteger(ss,16);
		int j = i.intValue();
//		int i = (int)Long.parseLong(ss, 16);
		return j;
	}
	//数据处理  1:轴  2：报警  3：系统
	private void string_dispose(String str){
		Start s = Start.getInstance();
		//switch的值：p1
		BigInteger p2 = new BigInteger(str.substring(0, 2),16);
		int p1 = p2.intValue();
		switch(p1){
		case 1://轴信息
			s.ax3.setType(String.valueOf(p1));
			float []c_axis = new float[3];
			int q1 = Dao_string(str.substring(8, 16));
			int q2 = Dao_string(str.substring(16, 24));
			int q3 = Dao_string(str.substring(24, 32));
			c_axis[0] = (float)(q1/10000F);
			c_axis[1] = (float)(q2/10000F);
			c_axis[2] = (float)(q3/10000F);
			s.ax3.setC_axis(c_axis);
			
			int q4 = Dao_string(str.substring(32, 40));
			int q5 = Dao_string(str.substring(40, 48));
			int q6 = Dao_string(str.substring(48, 56));
			float []a_axis_work = new float[3];
			a_axis_work[0] = (float)(q4/10000F);
			a_axis_work[1] = (float)(q5/10000F);
			a_axis_work[2] = (float)(q6/10000F);
			s.ax3.setA_axis_work(a_axis_work);
			
			int q7 = Dao_string(str.substring(56, 64));
			int q8 = Dao_string(str.substring(64, 72));
			int q9 = Dao_string(str.substring(72, 80));
			float []a_axis_relative = new float[3];
			a_axis_relative[0] = (float)(q7/10000F);
			a_axis_relative[1] = (float)(q8/10000F);
			a_axis_relative[2] = (float)(q9/10000F);
			s.ax3.setA_axis_relative(a_axis_relative);
			
			int q10 = Dao_string(str.substring(80, 88));
			int q11 = Dao_string(str.substring(88, 96));
			int q12 = Dao_string(str.substring(96, 104));
			float []a_axis_machine = new float[3];
			a_axis_machine[0] = (float)(q10/10000F);
			a_axis_machine[1] = (float)(q11/10000F);
			a_axis_machine[2] = (float)(q12/10000F);
			s.ax3.setA_axis_machine(a_axis_machine);
			
			int q13 = Dao_string(str.substring(104, 112));
			int q14 = Dao_string(str.substring(112, 120));
			int q15 = Dao_string(str.substring(120, 128));
			float []a_axis_remainder = new float[3];
			a_axis_remainder[0] = (float)(q13/10000F);
			a_axis_remainder[1] = (float)(q14/10000F);
			a_axis_remainder[2] = (float)(q15/10000F);
			s.ax3.setA_axis_remainder(a_axis_remainder);
			
			int q16 = Dao_string(str.substring(128, 136));
			int q17 = Dao_string(str.substring(136, 144));
			int q18 = Dao_string(str.substring(144, 152));
			long []a_f_value = new long[3];	
			a_f_value[0] = Math.abs(q16);
			a_f_value[1] = Math.abs(q17);
			a_f_value[2] = Math.abs(q18);
			s.ax3.setA_f_value(a_f_value);
			
			int q19 = Dao_string(str.substring(152, 160));
			int q20 = Dao_string(str.substring(160, 168));
			s.ax3.setC_s_value(q19);
			s.ax3.setA_s_value(q20);
			
			String source = To_ASCII(str.substring(168, 215));
			s.ax3.setTimestamp(source);
			s.flag3 = true;
			break;
		case 2://报警
			s.al3.setType(String.valueOf(p1));
			s.al3.setAlarmcode(Dao_string(str.substring(8, 16)));
			s.al3.setAlarmtime_occur(To_ASCII(str.substring(16, 64)));
			s.al3.setAlarmtime_remove(Dao_string(str.substring(64, 72)));
			s.al3.setAlarmnum(To_ASCII(str.substring(72, 74)));
			s.al3.setTimestamp(To_ASCII(str.substring(74, 128)));
			
			String s1 = str.substring(128, 384);
			int []alarmcode_array = new int[32];
			int j1 = 0;
			for(int i = 0;i<alarmcode_array.length;i++){
				alarmcode_array[i] = Dao_string(s1.substring(j1, j1+8));
				j1 = j1+8;
			}
			s.al3.setAlarmcode_array(alarmcode_array);
			
			String s2 = str.substring(384, 1920);
			String []alarmtime_occur_array = new String[32];	
			int p = 0;
			for(int i = 0;i<alarmtime_occur_array.length;i++){
				alarmtime_occur_array[i] = To_ASCII(s2.substring(p, p+48));
				p  = p+48;
			}
			s.al3.setAlarmtime_occur_array(alarmtime_occur_array);
			
			s.flag3 = true;
			break;
		case 3://系统
			s.sy3.setType(String.valueOf(p1));
			s.sy3.setSystemid(To_ASCII(str.substring(2, 18)));
			s.sy3.setSystemtype(To_ASCII(str.substring(18, 50)));
			s.sy3.setSystemver(To_ASCII(str.substring(50, 82)));
			s.sy3.setGgroup(To_ASCII(str.substring(82, 162)));
			s.sy3.setPn(To_ASCII(str.substring(162, 192)));
			
			s.sy3.setPd(Integer.valueOf(Dao_string(str.substring(192, 200))));
			s.sy3.setPs(Integer.valueOf(Dao_string(str.substring(200, 208))));
			s.sy3.setPl(Integer.valueOf(Dao_string(str.substring(208, 216))));
			
			s.sy3.setS_loadcurrent(Dao_string(str.substring(216, 224)));
			long []axis_loadcurrent = new long[3];
			axis_loadcurrent[0] = Dao_string(str.substring(224, 232));
			axis_loadcurrent[1] = Dao_string(str.substring(232, 240));
			axis_loadcurrent[2] = Dao_string(str.substring(240, 248));
			s.sy3.setAxis_loadcurrent(axis_loadcurrent);
			
			s.sy3.setOntime(Dao_string(str.substring(248, 256)));
			s.sy3.setRuntime(Dao_string(str.substring(256, 264)));
			s.sy3.setTimestamp(To_ASCII(str.substring(264, 312)));
			
			s.flag3 = true;
			break;
		}
	}
	//转换ASCII
	private String To_ASCII(String str){
		int j = 0;
		String ss = "";
		for(int i = 0;i<(str.length())/2;i++){
			String sa = str.substring(j, j+2);
			j+=2;
			int code ;  
		    code = Integer.parseInt(sa, 16);
		    char result = (char) code;  
		    ss  = ss+result;
		}
		return ss;
	}
	//读数据 返回字符串
	public String R(Socket socket){
		try {
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			String serverString = null;
			String hex = null;
			while(true){
				byte[] buf = new byte[1024];
				int size = 0;
				if((size = in.read(buf))!=-1){
					for(int i = 0;i<buf.length;i++){
						hex = Integer.toHexString(buf[i] & 0xFF);
						if(hex.length() == 1){
							hex = '0' + hex;
						}
						serverString += hex.toUpperCase();
					}
					time = new String(buf);
//					System.out.println(time);
					if(serverString != null){
						return serverString.substring(4, serverString.length()).toUpperCase();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ip;
	}
	/**
	 * 判断是否断开  断开返回true 没有返回false
	 * @param socket
	 * @return
	 */
	public boolean isServerClose(Socket socket){
		try {
			socket.sendUrgentData(0);//发送1个字节的紧急数据
			return false;
		} catch (Exception e) {
			return true;
		}
	}
}

