import java.net.*;
import java.io.*;
import java.util.*;
 
class Test {
	private String IPURL[] = {
		"http://checkip.amazonaws.com", 
		"http://icanhazip.com/", 
		"http://www.trackip.net/ip", 
		"http://myexternalip.com/raw", 
		"http://ipecho.net/plain", 
		"http://bot.whatismyipaddress.com"};
	
	public String getPublicIP() {
        BufferedReader in = null;
        String publicIP = null;
        for (int i = 0; i < IPURL.length; i ++) {
        	System.out.println("Third Party: " + IPURL[i]);
        	try {
        		URL whatismyip = new URL(IPURL[i]);
        		in = new BufferedReader(new InputStreamReader(
                	whatismyip.openStream()));
        		publicIP = in.readLine();
        		if (publicIP != null || publicIP.trim().length() > 0) 
        			return publicIP;
        	} catch (Exception e) {
        		System.out.println("Not working, trying next");
        	} finally {
            	if (in != null) {
                	try {
                    	in.close();
                	} catch (IOException e) {
                    	e.printStackTrace();
                	}
            	}
        	}
        }
        return publicIP;
    }
    
    public static void main(String[] arguments) {
    	Test t = new Test();
    	String ip = t.getPublicIP();
    	System.out.println(ip);
} 
}