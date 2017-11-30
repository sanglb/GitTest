package com.lidennis.plat.config.model;
import com.lidennis.plat.common.model.AbstractBaseModel;

public class ClientVersionMain extends AbstractBaseModel
{
    private static final long serialVersionUID = 1L;
    
    protected Long    appId;                 
    protected String  appName;
    protected String  appMode;               
    protected String  typeCode;              
    protected String  compyInfo; 
    protected String  clientType;            
    protected String  bizCode;               
    protected String  itemParams;            
    protected String  uuidCode;
    protected String  imageInfo;             
    protected String  itemUrl;               
    protected String  twodimInfo;            
    protected Integer startVerNum;           
    protected Integer lastVerNum;            
    protected Long    startRlsTime;          
    protected Long    lastRlsTime;
    protected String  fullFileName;          
    protected String  incFileName;           
    protected String  devInfo;               
    protected String  reqDesc;               
    protected String  svnInfo;               
    protected Integer subCount;              
    protected Integer downCount;             
    
    
    public Long getAppId()
    {
        return appId;
    }
    public void setAppId(Long appId)
    {
        this.appId = appId;
    }
    public String getAppName()
    {
        return appName;
    }
    public void setAppName(String appName)
    {
        this.appName = appName;
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
    public String getCompyInfo()
    {
        return compyInfo;
    }
    public void setCompyInfo(String compyInfo)
    {
        this.compyInfo = compyInfo;
    }
    public String getClientType()
    {
        return clientType;
    }
    public void setClientType(String clientType)
    {
        this.clientType = clientType;
    }
    public String getBizCode()
    {
        return bizCode;
    }
    public void setBizCode(String bizCode)
    {
        this.bizCode = bizCode;
    }
    public String getItemParams()
    {
        return itemParams;
    }
    public void setItemParams(String itemParams)
    {
        this.itemParams = itemParams;
    }
    public String getUuidCode()
    {
        return uuidCode;
    }
    public void setUuidCode(String uuidCode)
    {
        this.uuidCode = uuidCode;
    }
    public String getImageInfo()
    {
        return imageInfo;
    }
    public void setImageInfo(String imageInfo)
    {
        this.imageInfo = imageInfo;
    }
    public String getItemUrl()
    {
        return itemUrl;
    }
    public void setItemUrl(String itemUrl)
    {
        this.itemUrl = itemUrl;
    }
    public String getTwodimInfo()
    {
        return twodimInfo;
    }
    public void setTwodimInfo(String twodimInfo)
    {
        this.twodimInfo = twodimInfo;
    }
    public String getSvnInfo()
    {
        return svnInfo;
    }
    public void setSvnInfo(String svnInfo)
    {
        this.svnInfo = svnInfo;
    }
    public Integer getStartVerNum()
    {
        return startVerNum;
    }
    public void setStartVerNum(Integer startVerNum)
    {
        this.startVerNum = startVerNum;
    }
    public Integer getLastVerNum()
    {
        return lastVerNum;
    }
    public void setLastVerNum(Integer lastVerNum)
    {
        this.lastVerNum = lastVerNum;
    }
    public Long getStartRlsTime()
    {
        return startRlsTime;
    }
    public void setStartRlsTime(Long startRlsTime)
    {
        this.startRlsTime = startRlsTime;
    }
    public Long getLastRlsTime()
    {
        return lastRlsTime;
    }
    public void setLastRlsTime(Long lastRlsTime)
    {
        this.lastRlsTime = lastRlsTime;
    }
    public String getFullFileName()
    {
        return fullFileName;
    }
    public void setFullFileName(String fullFileName)
    {
        this.fullFileName = fullFileName;
    }
    public String getIncFileName()
    {
        return incFileName;
    }
    public void setIncFileName(String incFileName)
    {
        this.incFileName = incFileName;
    }
    public String getDevInfo()
    {
        return devInfo;
    }
    public void setDevInfo(String devInfo)
    {
        this.devInfo = devInfo;
    }
    public String getReqDesc()
    {
        return reqDesc;
    }
    public void setReqDesc(String reqDesc)
    {
        this.reqDesc = reqDesc;
    }
    public Integer getSubCount()
    {
        return subCount;
    }
    public void setSubCount(Integer subCount)
    {
        this.subCount = subCount;
    }
    public Integer getDownCount()
    {
        return downCount;
    }
    public void setDownCount(Integer downCount)
    {
        this.downCount = downCount;
    }
    
}
