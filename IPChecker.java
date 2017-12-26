import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Properties;

public class IPChecker {
	private static IPChecker check = null;
	private String ipFile = "ip.txt";
	Properties prop = new Properties();
	private String IPURL[] = {
		"http://checkip.amazonaws.com", 
		"http://icanhazip.com/", 
		"http://www.trackip.net/ip", 
		"http://myexternalip.com/raw", 
		"http://ipecho.net/plain", 
		"http://bot.whatismyipaddress.com"};
	
	public static IPChecker getInstance() {
		if (check == null) {
			check = new IPChecker();
		}
		return check;
	}

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
    //public boolean hasIPChanged() {
    //}
    
    public void PersistIPAddress(String ip) {
    	File f = null;
	    FileWriter fw = null;
    	try {
    		f = new File(ipFile);
    		fw = new FileWriter(f);
	    	fw.write(ip);
	    	fw.close();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
    }
    
    public String readExistingIP() {
    	try {
    		File f = new File(ipFile);
    		if (f.exists()) {
	    		FileReader fr = new FileReader(ipFile);
				char[] ipp = new char[20];
				int x = fr.read(ipp);
				String ips = new String(ipp);
				System.out.println("old ip: " + ips);
				return ips;
			} else {
				System.out.println("File does not exist. Return empty string");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getWebIP() {
		String webip = "";
		try {
        	InetAddress address = InetAddress.getByName("mohanpraveen.dlinkddns.com");
        	webip = address.getHostAddress();
        } catch (Exception e) {
        	e.printStackTrace();
        } 
        return webip;
    }
		
    public static void main (String args[]) {
    	IPChecker cc = IPChecker.getInstance();
    	try {
    		InputStream input = new FileInputStream("email.properties");
    		cc.prop.load(input);
    	} catch (Exception e) {
    		System.out.println("Error reading properties file");
    		e.printStackTrace();
    		System.exit(1);
    	}
    	Date dd = new Date(System.currentTimeMillis());
    	System.out.println("Checked at: " + dd);
    	String ip = cc.getPublicIP();
    	if (ip == null) {
    		System.out.println("Some problem with getting IP");
    		return;
    	} else {
    		System.out.println("Public IP: " + ip);
    	}
    	String oldIP = cc.getWebIP();
    	System.out.println("Web IP: " + oldIP);
    	String persistedIP = cc.readExistingIP();
    	
		if (!oldIP.trim().equals(ip.trim())) {
	    	if (!persistedIP.trim().equals(ip.trim())) {
	    		System.out.println("New IP is different from Old IP, send an email");
	    		String toAddress = cc.prop.getProperty("senders");
	    		String[] toAddr = toAddress.split(",");
	    		String header = cc.prop.getProperty("body");
	    		String body = header + "\n" + 
	    		"Checked at: " + dd + "\n" +
	    		"Current IP: " + oldIP + "\n" +
	    		"New IP: " + ip + "\n" +
	    		"DNS weblink: " + cc.prop.getProperty("weblink") + "\n"+
	    		"username: " + cc.prop.getProperty("dnsusername") + "\n";
	    		MailClient.sendFromGMail(cc.prop.getProperty("username"), cc.prop.getProperty("password"), toAddr, "Mannai Router IP Changed", body);
	    		System.out.println("Persist the new IP");
	    		cc.PersistIPAddress(ip);
	    	} else {
	    		System.out.println("Public IP and DNS IP are different but it has been notified already. ");
	    	}
    	} else {
    		System.out.println("DNS IP and New Public IP are same, just persist the new DNS IP");
    		cc.PersistIPAddress(ip);
    	}
    }
}