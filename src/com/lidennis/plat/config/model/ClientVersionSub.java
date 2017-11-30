package com.lidennis.plat.config.model;
import com.lidennis.plat.common.model.AbstractBaseModel;

public class ClientVersionSub extends AbstractBaseModel
{
    private static final long serialVersionUID = 1L;
    
    protected Long    mainId;
    protected String  mainCode;              
    protected Integer versionNum;            
    protected Integer minSupportNum;         
    protected String  uuidCode;
    protected String  versionType;           
    protected String  clientType;            
    protected String  clientAdapter;         
    protected String  runEnv;                
    protected Long    mergeId;               
    protected Long    branchId;              
    protected Integer forceFlag;             
    protected String  devInfo;               
    protected Long    devStartTime;          
    protected Long    devEndTime;
    protected String  svnInfo;               
    protected String  reqDesc;               
    protected Long    releaseTime;           
    protected String  releaseInfo;           
    protected String  releaseDesc;           
    protected String  fullFilePath;          
    protected String  fullFileName;
    protected Integer fullFileSize;
    protected String  fullFileMd5;
    protected String  incFilePath;
    protected String  incFileName;
    protected Integer incFileSize;
    protected String  incFileMd5;
    protected Integer downCount;             
    protected Long    downLastTime;          
    
    
    public Long getMainId()
    {
        return mainId;
    }
    public void setMainId(Long mainId)
    {
        this.mainId = mainId;
    }
    public String getMainCode()
    {
        return mainCode;
    }
    public void setMainCode(String mainCode)
    {
        this.mainCode = mainCode;
    }
    public Integer getVersionNum()
    {
        return versionNum;
    }
    public void setVersionNum(Integer versionNum)
    {
        this.versionNum = versionNum;
    }
    public Integer getMinSupportNum()
    {
        return minSupportNum;
    }
    public void setMinSupportNum(Integer minSupportNum)
    {
        this.minSupportNum = minSupportNum;
    }
    public String getUuidCode()
    {
        return uuidCode;
    }
    public void setUuidCode(String uuidCode)
    {
        this.uuidCode = uuidCode;
    }
    public String getVersionType()
    {
        return versionType;
    }
    public void setVersionType(String versionType)
    {
        this.versionType = versionType;
    }
    public String getClientType()
    {
        return clientType;
    }
    public void setClientType(String clientType)
    {
        this.clientType = clientType;
    }
    public String getClientAdapter()
    {
        return clientAdapter;
    }
    public void setClientAdapter(String clientAdapter)
    {
        this.clientAdapter = clientAdapter;
    }
    public String getRunEnv()
    {
        return runEnv;
    }
    public void setRunEnv(String runEnv)
    {
        this.runEnv = runEnv;
    }
    public Long getMergeId()
    {
        return mergeId;
    }
    public void setMergeId(Long mergeId)
    {
        this.mergeId = mergeId;
    }
    public Long getBranchId()
    {
        return branchId;
    }
    public void setBranchId(Long branchId)
    {
        this.branchId = branchId;
    }
    public Integer getForceFlag()
    {
        return forceFlag;
    }
    public void setForceFlag(Integer forceFlag)
    {
        this.forceFlag = forceFlag;
    }
    public String getDevInfo()
    {
        return devInfo;
    }
    public void setDevInfo(String devInfo)
    {
        this.devInfo = devInfo;
    }
    public Long getDevStartTime()
    {
        return devStartTime;
    }
    public void setDevStartTime(Long devStartTime)
    {
        this.devStartTime = devStartTime;
    }
    public Long getDevEndTime()
    {
        return devEndTime;
    }
    public void setDevEndTime(Long devEndTime)
    {
        this.devEndTime = devEndTime;
    }
    public String getReqDesc()
    {
        return reqDesc;
    }
    public void setReqDesc(String reqDesc)
    {
        this.reqDesc = reqDesc;
    }
    public Long getReleaseTime()
    {
        return releaseTime;
    }
    public void setReleaseTime(Long releaseTime)
    {
        this.releaseTime = releaseTime;
    }
    public String getReleaseInfo()
    {
        return releaseInfo;
    }
    public void setReleaseInfo(String releaseInfo)
    {
        this.releaseInfo = releaseInfo;
    }
    public String getReleaseDesc()
    {
        return releaseDesc;
    }
    public void setReleaseDesc(String releaseDesc)
    {
        this.releaseDesc = releaseDesc;
    }
    public Integer getDownCount()
    {
        return downCount;
    }
    public void setDownCount(Integer downCount)
    {
        this.downCount = downCount;
    }
    public Long getDownLastTime()
    {
        return downLastTime;
    }
    public void setDownLastTime(Long downLastTime)
    {
        this.downLastTime = downLastTime;
    }
    public String getFullFilePath()
    {
        return fullFilePath;
    }
    public void setFullFilePath(String fullFilePath)
    {
        this.fullFilePath = fullFilePath;
    }
    public String getFullFileName()
    {
        return fullFileName;
    }
    public void setFullFileName(String fullFileName)
    {
        this.fullFileName = fullFileName;
    }
    public Integer getFullFileSize()
    {
        return fullFileSize;
    }
    public void setFullFileSize(Integer fullFileSize)
    {
        this.fullFileSize = fullFileSize;
    }
    public String getFullFileMd5()
    {
        return fullFileMd5;
    }
    public void setFullFileMd5(String fullFileMd5)
    {
        this.fullFileMd5 = fullFileMd5;
    }
    public String getIncFilePath()
    {
        return incFilePath;
    }
    public void setIncFilePath(String incFilePath)
    {
        this.incFilePath = incFilePath;
    }
    public String getIncFileName()
    {
        return incFileName;
    }
    public void setIncFileName(String incFileName)
    {
        this.incFileName = incFileName;
    }
    public Integer getIncFileSize()
    {
        return incFileSize;
    }
    public void setIncFileSize(Integer incFileSize)
    {
        this.incFileSize = incFileSize;
    }
    public String getIncFileMd5()
    {
        return incFileMd5;
    }
    public void setIncFileMd5(String incFileMd5)
    {
        this.incFileMd5 = incFileMd5;
    }
    public String getSvnInfo()
    {
        return svnInfo;
    }
    public void setSvnInfo(String svnInfo)
    {
        this.svnInfo = svnInfo;
    }
    
}
