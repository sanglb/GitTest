package com.lidennis.plat.config.model;
import com.lidennis.plat.common.model.AbstractBaseModel;

public class ConfigCronJob extends AbstractBaseModel
{
    private static final long serialVersionUID = 1L;
    
    protected String  uuidCode;
    protected String  classifyCode;        
    protected String  serverCode;          
    protected String  serverName;          
    protected String  serviceCode;         
    protected String  procCode;            
    protected String  procName;
    protected String  procBeanName;
    protected String  procParam;           
    protected String  cronExpress;         
    protected String  procStatus;          
    protected Integer execCount;           
    protected String  execStatus;
    protected Integer errorCount;          
    protected Long    errorLastTime;       
    protected Long    errorLogTime;
    protected Long    runLogTime;
    protected Long    intervalError;       
    protected Long    intervalNormal;      
    protected String  noticeParam;         
    protected String  triggerStatus;       
    protected Long    startExecTime;       
    protected Long    endExecTime;         
    protected Long    lastExecTime;        
    protected Long    nextExecTime;        
    
    public String getUuidCode()
    {
        return uuidCode;
    }
    public void setUuidCode(String uuidCode)
    {
        this.uuidCode = uuidCode;
    }
    public String getClassifyCode()
    {
        return classifyCode;
    }
    public void setClassifyCode(String classifyCode)
    {
        this.classifyCode = classifyCode;
    }
    public String getServerCode()
    {
        return serverCode;
    }
    public void setServerCode(String serverCode)
    {
        this.serverCode = serverCode;
    }
    public String getServerName()
    {
        return serverName;
    }
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }
    public String getServiceCode()
    {
        return serviceCode;
    }
    public void setServiceCode(String serviceCode)
    {
        this.serviceCode = serviceCode;
    }
    public String getProcCode()
    {
        return procCode;
    }
    public void setProcCode(String procCode)
    {
        this.procCode = procCode;
    }
    public String getProcName()
    {
        return procName;
    }
    public void setProcName(String procName)
    {
        this.procName = procName;
    }
    public String getProcBeanName()
    {
        return procBeanName;
    }
    public void setProcBeanName(String procBeanName)
    {
        this.procBeanName = procBeanName;
    }
    public String getProcParam()
    {
        return procParam;
    }
    public void setProcParam(String procParam)
    {
        this.procParam = procParam;
    }
    public String getCronExpress()
    {
        return cronExpress;
    }
    public void setCronExpress(String cronExpress)
    {
        this.cronExpress = cronExpress;
    }
    public String getProcStatus()
    {
        return procStatus;
    }
    public void setProcStatus(String procStatus)
    {
        this.procStatus = procStatus;
    }
    public Integer getExecCount()
    {
        return execCount;
    }
    public void setExecCount(Integer execCount)
    {
        this.execCount = execCount;
    }
    public String getExecStatus()
    {
        return execStatus;
    }
    public void setExecStatus(String execStatus)
    {
        this.execStatus = execStatus;
    }
    public Long getErrorLastTime()
    {
        return errorLastTime;
    }
    public void setErrorLastTime(Long errorLastTime)
    {
        this.errorLastTime = errorLastTime;
    }
    public Integer getErrorCount()
    {
        return errorCount;
    }
    public void setErrorCount(Integer errorCount)
    {
        this.errorCount = errorCount;
    }
    public Long getRunLogTime()
    {
        return runLogTime;
    }
    public void setRunLogTime(Long runLogTime)
    {
        this.runLogTime = runLogTime;
    }
    public Long getErrorLogTime()
    {
        return errorLogTime;
    }
    public void setErrorLogTime(Long errorLogTime)
    {
        this.errorLogTime = errorLogTime;
    }
    public Long getIntervalError()
    {
        return intervalError;
    }
    public void setIntervalError(Long intervalError)
    {
        this.intervalError = intervalError;
    }
    public Long getIntervalNormal()
    {
        return intervalNormal;
    }
    public void setIntervalNormal(Long intervalNormal)
    {
        this.intervalNormal = intervalNormal;
    }
    public String getNoticeParam()
    {
        return noticeParam;
    }
    public void setNoticeParam(String noticeParam)
    {
        this.noticeParam = noticeParam;
    }
    public String getTriggerStatus()
    {
        return triggerStatus;
    }
    public void setTriggerStatus(String triggerStatus)
    {
        this.triggerStatus = triggerStatus;
    }
    public Long getStartExecTime()
    {
        return startExecTime;
    }
    public void setStartExecTime(Long startExecTime)
    {
        this.startExecTime = startExecTime;
    }
    public Long getEndExecTime()
    {
        return endExecTime;
    }
    public void setEndExecTime(Long endExecTime)
    {
        this.endExecTime = endExecTime;
    }
    public Long getLastExecTime()
    {
        return lastExecTime;
    }
    public void setLastExecTime(Long lastExecTime)
    {
        this.lastExecTime = lastExecTime;
    }
    public Long getNextExecTime()
    {
        return nextExecTime;
    }
    public void setNextExecTime(Long nextExecTime)
    {
        this.nextExecTime = nextExecTime;
    }
    
}
