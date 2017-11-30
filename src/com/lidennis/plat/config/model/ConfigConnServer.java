package com.lidennis.plat.config.model;
import com.lidennis.plat.common.model.AbstractBaseModel;

public class ConfigConnServer extends AbstractBaseModel
{
    private static final long serialVersionUID = 1L;
    
    protected String  classifyId;          
    protected String  classifyName;
    protected String  classifyValue;
    protected String  typeCode;            
    protected String  connectType;         
    protected String  connectUrl;
    protected String  serverAddr;
    protected Integer serverPort;
    protected String  itemAcct;            
    protected String  itemPwd;
    protected String  authCode;            
    protected String  itemParam;           
    
    
    public String getClassifyId()
    {
        return classifyId;
    }
    public void setClassifyId(String classifyId)
    {
        this.classifyId = classifyId;
    }
    public String getClassifyName()
    {
        return classifyName;
    }
    public void setClassifyName(String classifyName)
    {
        this.classifyName = classifyName;
    }
    public String getClassifyValue()
    {
        return classifyValue;
    }
    public void setClassifyValue(String classifyValue)
    {
        this.classifyValue = classifyValue;
    }
    public String getTypeCode()
    {
        return typeCode;
    }
    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }
    public String getConnectType()
    {
        return connectType;
    }
    public void setConnectType(String connectType)
    {
        this.connectType = connectType;
    }
    public String getConnectUrl()
    {
        return connectUrl;
    }
    public void setConnectUrl(String connectUrl)
    {
        this.connectUrl = connectUrl;
    }
    public String getServerAddr()
    {
        return serverAddr;
    }
    public void setServerAddr(String serverAddr)
    {
        this.serverAddr = serverAddr;
    }
    public Integer getServerPort()
    {
        return serverPort;
    }
    public void setServerPort(Integer serverPort)
    {
        this.serverPort = serverPort;
    }
    public String getItemAcct()
    {
        return itemAcct;
    }
    public void setItemAcct(String itemAcct)
    {
        this.itemAcct = itemAcct;
    }
    public String getItemPwd()
    {
        return itemPwd;
    }
    public void setItemPwd(String itemPwd)
    {
        this.itemPwd = itemPwd;
    }
    public String getAuthCode()
    {
        return authCode;
    }
    public void setAuthCode(String authCode)
    {
        this.authCode = authCode;
    }
    public String getItemParam()
    {
        return itemParam;
    }
    public void setItemParam(String itemParam)
    {
        this.itemParam = itemParam;
    }
    
}
