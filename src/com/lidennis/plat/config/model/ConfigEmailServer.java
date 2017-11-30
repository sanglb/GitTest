package com.lidennis.plat.config.model;
import com.lidennis.plat.common.model.AbstractBaseModel;

public class ConfigEmailServer extends AbstractBaseModel
{
    private static final long serialVersionUID = 1L;
    
    protected Long    classifyId;          
    protected String  classifyName;
    protected String  classifyValue;
    protected String  connectType;         
    protected String  connectUrl;
    protected String  serverAddr;          
    protected Integer serverPort;          
    protected String  itemAcct;            
    protected String  itemPwd;
    protected String  authCode;            
    protected String  itemParam;
    protected String  senderName;          
    protected String  senderMail;
    protected String  managerEmail;
    protected String  seniorEmail;
    protected Integer dailyMaxNum;         
    protected String  matchRule;           
    protected String  statDateStr;         
    protected Integer statCount;           
    
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
    public String getClassifyValue()
    {
        return classifyValue;
    }
    public void setClassifyValue(String classifyValue)
    {
        this.classifyValue = classifyValue;
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
    public String getSenderName()
    {
        return senderName;
    }
    public void setSenderName(String senderName)
    {
        this.senderName = senderName;
    }
    public String getSenderMail()
    {
        return senderMail;
    }
    public void setSenderMail(String senderMail)
    {
        this.senderMail = senderMail;
    }
    public String getManagerEmail()
    {
        return managerEmail;
    }
    public void setManagerEmail(String managerEmail)
    {
        this.managerEmail = managerEmail;
    }
    public String getSeniorEmail()
    {
        return seniorEmail;
    }
    public void setSeniorEmail(String seniorEmail)
    {
        this.seniorEmail = seniorEmail;
    }
    public Integer getDailyMaxNum()
    {
        return dailyMaxNum;
    }
    public void setDailyMaxNum(Integer dailyMaxNum)
    {
        this.dailyMaxNum = dailyMaxNum;
    }
    public String getMatchRule()
    {
        return matchRule;
    }
    public void setMatchRule(String matchRule)
    {
        this.matchRule = matchRule;
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
