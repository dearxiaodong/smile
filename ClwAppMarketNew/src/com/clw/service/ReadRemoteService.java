package com.clw.service;

import java.util.List;  

import org.ksoap2.SoapEnvelope;  
import org.ksoap2.SoapFault;  
import org.ksoap2.serialization.SoapObject;  
import org.ksoap2.serialization.SoapSerializationEnvelope;  
import org.ksoap2.transport.HttpTransportSE;  
import org.ksoap2.transport.AndroidHttpTransport;  
  
public class ReadRemoteService {  
      
    public String NAMESPACE;  
    public String METHODNAME;  
    public String WEBSERVICEURL;  
    public Boolean ISDOTNETSERVICE;  
    public List<String> LISTPARAMS;  
       
    public ReadRemoteService(String nameSpace,String methodName,String WebServiceUrl,Boolean IsDotNetService,List<String> lstParams)  
    {  
         this.NAMESPACE = nameSpace;  
         this.METHODNAME = methodName;  
         this.WEBSERVICEURL=WebServiceUrl;  
         this.ISDOTNETSERVICE = IsDotNetService;  
         this.LISTPARAMS=lstParams;  
    }  
      
    public Object Get()  
    {  
        try {  
            return ReadService();  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
            return "ERROR:" + e.getMessage();  
        }  
    }  
      
    private Object ReadService() throws Exception {  
        // 命名空间  
        String nameSpace = this.NAMESPACE;  
            //"http://WebXml.com.cn/";  
        // 调用的方法名称  
        String methodName = this.METHODNAME;  
            //"getMobileCodeInfo";  
        // EndPoint  
        String endPoint = this.WEBSERVICEURL;  
            //"http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx";  
        // SOAP Action  
        String soapAction = this.NAMESPACE + this.METHODNAME;  
            //"http://WebXml.com.cn/getMobileCodeInfo";  
  
        // 指定WebService的命名空间和调用的方法名  
        SoapObject rpc = new SoapObject(nameSpace, methodName);  
  
        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId  
        /*rpc.addProperty("mobileCode", phoneSec); 
        rpc.addProperty("userId", "");*/  
          
         if(this.LISTPARAMS !=null)  
         {  
             if(this.LISTPARAMS.size() % 2 !=0 || this.LISTPARAMS.size()<2)  
             {  
                  throw new Exception("params should be even!");  
             }  
               
            for(int i=0;i<this.LISTPARAMS.size()-1;i=i+2)  
            {  
                 //System.out.println(this.LISTPARAMS.get(i));  
                rpc.addProperty(this.LISTPARAMS.get(i), this.LISTPARAMS.get(i+1));  
                //throw new Exception("params:" + this.LISTPARAMS.get(i) + " " + this.LISTPARAMS.get(i+1));  
            }  
         }  
  
        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本  
        SoapSerializationEnvelope envelope   
        = new SoapSerializationEnvelope(SoapEnvelope.VER10);  
        //(SoapEnvelope.VER10);  VER11 有错误
  
        envelope.bodyOut = rpc;  
        // 设置是否调用的是dotNet开发的WebService  
        envelope.dotNet = this.ISDOTNETSERVICE;  
        envelope.encodingStyle="UTF-8";  
          
        if(!envelope.dotNet)  
        {  
            endPoint = endPoint + "?wsdl";   
        }  
          
        // 等价于envelope.bodyOut = rpc;  
        envelope.setOutputSoapObject(rpc);  
  
        try {  
            // 调用WebService  
            HttpTransportSE transport = new HttpTransportSE(endPoint);  
            //AndroidHttpTransport transport =new AndroidHttpTransport(endPoint);  
            //transport.debug=false;  
            /*transport.call(soapAction, envelope);*/  
              
        /*  if(!envelope.dotNet) 
            { 
                transport.call(null, envelope); 
            }else*/  
            {  
                transport.call(soapAction,envelope);  
            }  
              
        } catch (Exception e) {  
            e.printStackTrace();  
            return "ERROR:" + e.getMessage();  
        }  
  
        // 获取返回的数据  
        SoapObject object=null;  
        SoapFault error=null;  
        try  
        {  
            object = (SoapObject) envelope.bodyIn;  
        }catch(Exception e)  
        {  
            error = (SoapFault)envelope.bodyIn;  
            // detail =(SoapObject) envelope.getResponse();  
            System.out.println("Error message : "+error.toString());  
        }  
        // 获取返回的结果  
        //String result = object.getProperty(0).toString();  
  
        // 将WebService返回的结果显示在TextView中  
        // lbl1.setText(result);  
        if(error!=null)  
            return error;  
          
        return object;  
    }  
      
}  