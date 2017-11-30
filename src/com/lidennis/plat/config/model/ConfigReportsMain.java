package com.lidennis.plat.config.model;
import com.lidennis.plat.common.model.AbstractLevelModel;

public class ConfigReportsMain extends AbstractLevelModel
{
    private static final long serialVersionUID = 5183942559346989070L;
    
    protected Long    classifyId;            
    protected String  classifyName;
    protected String  classifyValue;
    protected String  uuidCode;
    protected Long    appId;                 
    protected String  bizMode;               
    protected String  bizLabel;              
    protected String  typeCode;              
    protected Integer subitemCount;
    
    protected Integer exint01;
    protected Integer exint02;
    protected Long    exlong01;
    protected Long    exlong02;
    
    
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
    public String getUuidCode()
    {
        return uuidCode;
    }
    public void setUuidCode(String uuidCode)
    {
        this.uuidCode = uuidCode;
    }
    public String getBizMode()
    {
        return bizMode;
    }
    public void setBizMode(String bizMode)
    {
        this.bizMode = bizMode;
    }
    public String getBizLabel()
    {
        return bizLabel;
    }
    public void setBizLabel(String bizLabel)
    {
        this.bizLabel = bizLabel;
    }
    public String getTypeCode()
    {
        return typeCode;
    }
    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }
    public Long getAppId()
    {
        return appId;
    }
    public void setAppId(Long appId)
    {
        this.appId = appId;
    }
    public Integer getSubitemCount()
    {
        return subitemCount;
    }
    public void setSubitemCount(Integer subitemCount)
    {
        this.subitemCount = subitemCount;
    }
    public Integer getExint01()
    {
        return exint01;
    }
    public void setExint01(Integer exint01)
    {
        this.exint01 = exint01;
    }
    public Integer getExint02()
    {
        return exint02;
    }
    public void setExint02(Integer exint02)
    {
        this.exint02 = exint02;
    }
    public Long getExlong01()
    {
        return exlong01;
    }
    public void setExlong01(Long exlong01)
    {
        this.exlong01 = exlong01;
    }
    public Long getExlong02()
    {
        return exlong02;
    }
    public void setExlong02(Long exlong02)
    {
        this.exlong02 = exlong02;
    }
}
