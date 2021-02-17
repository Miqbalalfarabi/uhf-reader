package uhf;
import java.lang.reflect.Array;

import com.rfid.uhf.Device.*;

import java.util.Arrays;
import java.io.IOException;
import javax.comm.UnsupportedCommOperationException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class rfid1 {
static String nilai ;
static String nilaiadd="0" ;
static String nilaiGadd="0" ;
int ite;
public static String getrfid() {
       
        return nilai;
    }static String content;
	/**
	 * @param args
	 */
	public static void main(String[] args) throws MqttException, IOException, UnsupportedCommOperationException {
            {
        String topic        = "MQTTExJohan";
        content             ="0";
        int qos             = 2;
        String broker       = "tcp://test.mosquitto.org:1883";
        String clientId     = "JavaSampleHanjohan";
        MemoryPersistence persistence = new MemoryPersistence();
        MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
         MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            MqttMessage message = new MqttMessage();
            message.setQos(qos);
          
                System.load("D:/Kuliah/NetBeansProjects/New Folder/testdemo/com_rfid_uhf_Devices.dll");
             
		 com.rfid.uhf.Device reader = new com.rfid.uhf.Device();
		 int Port = 37011;//com1
	     byte[]comAddr=new byte[1];
	     comAddr[0]=(byte)255;
	     byte baud=5;//57600bps
	     int[] PortHandle= new int[1];          
             String IpAddress="158.140.167.173";
             
             int results = reader.OpenNetPort(Port,IpAddress,comAddr,PortHandle);
             int results2=results;
             int jml=0;
	     System.out.println("Connect to TCP/IP ports:"+ results);
             message.setPayload(IpAddress.getBytes());
             sampleClient.publish("IpaddressUHFR", message);
	     while(true)
	     {
            
            	 byte[]versionInfo=new byte[2];
	    	 byte[]readerType=new byte[1];
	    	 byte[]trType=new byte[1];
	    	 byte[]dmaxfre=new byte[1];
	    	 byte[]dminfre=new byte[1];
	    	 byte[]powerdBm=new byte[1];
	    	 byte[]InventoryScanTime=new byte[1];
	    	 byte[]Ant=new byte[1];
	    	 byte[]BeepEn=new byte[1];
	    	 byte[]OutputRep=new byte[1];
	    	 byte[]CheckAnt=new byte[1];
	    	 results = reader.GetReaderInformation(comAddr, versionInfo, readerType, trType, dmaxfre, dminfre, powerdBm, InventoryScanTime,
	    			 Ant, BeepEn, OutputRep, CheckAnt, PortHandle[0]);
                 int PortHandles=PortHandle[0];
                 byte power1=30;
                 reader.SetRfPower(comAddr, power1,PortHandles);
	    	 System.out.println("Get reader information:"+results);
	    	 byte ComAdrData=0;
	    	 results = reader.SetAddress(comAddr, ComAdrData, PortHandle[0]);
	    	 System.out.println("Set the address of the reader:"+results);
	    	 byte QValue=4;
	    	 byte Session=0;
	    	 byte MaskMem=2;
	    	 byte[]MaskAdr=new byte[2];
	    	 byte MaskLen=0;
			 byte[]MaskData=new byte[256];
			 byte MaskFlag=0;
			 byte AdrTID=0;
			 byte LenTID=6;
			 byte TIDFlag=1;
			 byte Target=0;
			 byte InAnt=(byte)0x80;
			 byte Scantime=10;
			 byte FastFlag=0;
			 byte[]pEPCList=new byte[20000];
			 int[]Totallen=new int[1];
			 int[]CardNum=new int[1];
	    	 results = reader.Inventory_G2(comAddr,QValue,Session,MaskMem,MaskAdr,MaskLen,MaskData,MaskFlag,
	    			  AdrTID,LenTID,TIDFlag,Target,InAnt,Scantime,FastFlag,pEPCList, Ant,Totallen,
					   CardNum,PortHandle[0]);  
	    	 System.out.println("Inquiry command:"+results);
                 
                 
                 if(CardNum[0]>0)
	    	 {
	    		 System.out.println("Number of labels: "+CardNum[0]);
                         if(CardNum[0]>jml)jml=CardNum[0];
                         
	    		 int m=0;
                         
                         //nomor ID
	    		 for(int index=0;index<CardNum[0];index++)
	    		 {
	    			 int epclen = pEPCList[m++]&255;
	    			 String EPCstr="";
	    			 byte[]epc = new byte[epclen];
	    			 for(int n=0;n<epclen;n++)
	    			 {
	    				 byte bbt = pEPCList[m++];
	    				 epc[n] = bbt;
	    				 String hex= Integer.toHexString(bbt& 255);
			    		 if(hex.length()==1)
			    		 {
			    			hex="0"+hex;
			    		 }
			    		 EPCstr+=hex;
	    			 }
	    			 int rssi = pEPCList[m++];
	    			 System.out.println(EPCstr.toUpperCase());
                                 nilai=String.valueOf(IpAddress+" "+EPCstr.toUpperCase());
                                 System.out.println(IpAddress+" "+EPCstr.toUpperCase());
                                 message.setPayload(nilai.getBytes());
                                 sampleClient.publish(topic, message);
	    			 //Write data according to TID number
	    			 byte ENum=(byte)255;
	    			 byte Mem=1;
	    			 byte WordPtr=2;
	    			 byte Num=6;
	    			 byte[]Password=new byte[4];
                     MaskMem=2;
                     MaskAdr[0]=0;
                     MaskAdr[1]=0;
                     MaskLen=96;
                     int p=0;
                     System.arraycopy(epc,0,MaskData,0,96/8);
                     byte[]Data=new byte[Num*2];
                     int[]Errorcode=new int[1];
                     byte WNum=7;
                     byte[]Wdt=new byte[WNum*2];
                     Wdt[0]=0x30;
                     Wdt[1]=0x00;
                     Wdt[2]=(byte)0xE2;
                     Wdt[3]=0x00;
                     Wdt[4]=0x12;
                     Wdt[5]=0x34;
                     Wdt[6]=0x56;
                     Wdt[7]=0x78;
                     Wdt[8]=0x12;
                     Wdt[9]=0x34;
                     Wdt[10]=0x56;
                     Wdt[11]=0x78;
                     Wdt[12]=0x12;
                     Wdt[13]=0x34;
                     WordPtr=1;
                     results = reader.WriteData_G2(comAddr, epc, WNum, ENum, Mem, WordPtr, Wdt, Password, 
                    		 MaskMem,MaskAdr, MaskLen, MaskData, Errorcode, PortHandle[0]);
                     System.out.println("Write EPC number:"+results);
                     WordPtr=2;
	    			 results = reader.ReadData_G2(comAddr,epc,ENum,Mem,WordPtr,Num,Password,
		                       MaskMem,MaskAdr, MaskLen,MaskData,Data,Errorcode,PortHandle[0]); 
	    			 System.out.println("Read data:"+results);
	    			 if(results==0)
	    			 {
	    				 String Memdata="";
	    				 for( p=0;p<Num*2;p++)
		    			 {
		    				 byte bbt = Data[p];
		    				 String hex= Integer.toHexString(bbt& 255);
				    		 if(hex.length()==1)
				    		 {
				    			hex="0"+hex;
				    		 }
				    		 Memdata+=hex;
		    			 }
		    			 System.out.println(Memdata+"\n");
                          
                         
	    			 }
	    		 }
                
	    	 }

             }

	}

        }

}
