package com.lidennis.plat.config.model;
import com.lidennis.plat.common.model.AbstractBaseModel;

public class ConfigDataTemp extends AbstractBaseModel
{
    private static final long serialVersionUID = 1L;
    
    protected String  appMode;             
    protected String  bizCode;
    protected String  typeCode;            
    protected String  uuidCode;
    protected Long    clfyId;              
    protected String  clfyName;
    protected String  clfyValue;
    protected String  tempMainPath;        
    protected String  tempRelaPath;        
    protected String  tempFileName;        
    protected String  tempTitle;           
    protected String  tempContent;         
    protected String  tempParam;
    protected String  charEncode;          
    protected String  relateType;
    protected Long    relateId;
    protected Integer dynamicFlag;         
    protected Integer editFlag;            
    protected Integer fileFlag;            
    
    
    public String getAppMode()
    {
        return appMode;
    }
    public void setAppMode(String appMode)
    {
        this.appMode = appMode;
    }
    public String getBizCode()
    {
        return bizCode;
    }
    public void setBizCode(String bizCode)
    {
        this.bizCode = bizCode;
    }
    public String getTypeCode()
    {
        return typeCode;
    }
    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }
    public String getUuidCode()
    {
        return uuidCode;
    }
    public void setUuidCode(String uuidCode)
    {
        this.uuidCode = uuidCode;
    }
    public Long getClfyId()
    {
        return clfyId;
    }
    public void setClfyId(Long clfyId)
    {
        this.clfyId = clfyId;
    }
    public String getClfyName()
    {
        return clfyName;
    }
    public void setClfyName(String clfyName)
    {
        this.clfyName = clfyName;
    }
    public String getClfyValue()
    {
        return clfyValue;
    }
    public void setClfyValue(String clfyValue)
    {
        this.clfyValue = clfyValue;
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
    public String getTempFileName()
    {
        return tempFileName;
    }
    public void setTempFileName(String tempFileName)
    {
        this.tempFileName = tempFileName;
    }
    public String getTempTitle()
    {
        return tempTitle;
    }
    public void setTempTitle(String tempTitle)
    {
        this.tempTitle = tempTitle;
    }
    public String getTempContent()
    {
        return tempContent;
    }
    public void setTempContent(String tempContent)
    {
        this.tempContent = tempContent;
    }
    public String getTempParam()
    {
        return tempParam;
    }
    public void setTempParam(String tempParam)
    {
        this.tempParam = tempParam;
    }
    public String getCharEncode()
    {
        return charEncode;
    }
    public void setCharEncode(String charEncode)
    {
        this.charEncode = charEncode;
    }
    public String getRelateType()
    {
        return relateType;
    }
    public void setRelateType(String relateType)
    {
        this.relateType = relateType;
    }
    public Long getRelateId()
    {
        return relateId;
    }
    public void setRelateId(Long relateId)
    {
        this.relateId = relateId;
    }
    public Integer getDynamicFlag()
    {
        return dynamicFlag;
    }
    public void setDynamicFlag(Integer dynamicFlag)
    {
        this.dynamicFlag = dynamicFlag;
    }
    public Integer getEditFlag()
    {
        return editFlag;
    }
    public void setEditFlag(Integer editFlag)
    {
        this.editFlag = editFlag;
    }
    public Integer getFileFlag()
    {
        return fileFlag;
    }
    public void setFileFlag(Integer fileFlag)
    {
        this.fileFlag = fileFlag;
    }
    
}
