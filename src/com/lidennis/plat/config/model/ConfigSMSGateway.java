package com.lidennis.plat.config.model;
import java.util.Map;

import com.lidennis.plat.common.model.AbstractBaseModel;

public class ConfigSMSGateway extends AbstractBaseModel
{
    private static final long serialVersionUID = 1L;
    
    protected String  compyInfo;           
    protected String  appMode;             
    protected String  typeCode;
    protected Long    classifyId;          
    protected String  classifyName;
    protected String  tempMainPath;        
    protected String  tempRelaPath;        
    protected String  connectType;         
    protected String  connectUrl;
    protected String  serverAddr;
    protected Integer serverPort;
    protected String  itemAcct;            
    protected String  itemPwd;
    protected String  authCode;            
    protected String  itemParam;           
    protected Map<String,Object> paramObj; 
    protected Integer dailyMaxNum;         
    protected String  statDateStr;         
    protected Integer statCount;           
    
    
    public String getCompyInfo()
    {
        return compyInfo;
    }
    public void setCompyInfo(String compyInfo)
    {
        this.compyInfo = compyInfo;
    }
    public String getAppMode()
    {
        return appMode;
    }
    public void setAppMode(String appMode)
    {
        this.appMode = appMode;
    }
    public String getTypeCode()
    {
        return typeCode;
    }
    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }
    public Long getClassifyId()
    {
        return classifyId;
    }
    public void setClassifyId(Long classifyId)
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
    public String getTempMainPath()
    {
        return tempMainPath;
    }
    public void setTempMainPath(String tempMainPath)
    {
        this.tempMainPath = tempMainPath;
    }
    public String getTempRelaPath()
    {
        return tempRelaPath;
    }
    public void setTempRelaPath(String tempRelaPath)
    {
        this.tempRelaPath = tempRelaPath;
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
    public Map<String, Object> getParamObj()
    {
        return paramObj;
    }
    public void setParamObj(Map<String, Object> paramObj)
    {
        this.paramObj = paramObj;
    }
    public Integer getDailyMaxNum()
    {
        return dailyMaxNum;
    }
    public void setDailyMaxNum(Integer dailyMaxNum)
    {
        this.dailyMaxNum = dailyMaxNum;
    }
    public String getStatDateStr()
    {
        return statDateStr;
    }
    public void setStatDateStr(String statDateStr)
    {
        this.statDateStr = statDateStr;
    }
    public Integer getStatCount()
    {
        return statCount;
    }
    public void setStatCount(Integer statCount)
    {
        this.statCount = statCount;
    }
    
}
