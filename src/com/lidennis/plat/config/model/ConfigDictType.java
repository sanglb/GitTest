package com.lidennis.plat.config.model;
import com.lidennis.plat.common.model.AbstractBaseModel;

public class ConfigDictType extends AbstractBaseModel
{
    private static final long serialVersionUID = 1L;
    
    protected String  bizCode;
    protected Integer levelFlag;
    protected Long    rootNodeId;
    protected Integer itemCount;
    
    
    public String getBizCode()
    {
        return bizCode;
    }
    public void setBizCode(String bizCode)
    {
        this.bizCode = bizCode;
    }
    public Integer getLevelFlag()
    {
        return levelFlag;
    }
    public void setLevelFlag(Integer levelFlag)
    {
        this.levelFlag = levelFlag;
    }
    public Long getRootNodeId()
    {
        return rootNodeId;
    }
    public void setRootNodeId(Long rootNodeId)
    {
        this.rootNodeId = rootNodeId;
    }
    public Integer getItemCount()
    {
        return itemCount;
    }
    public void setItemCount(Integer itemCount)
    {
        this.itemCount = itemCount;
    }
}
