package com.lidennis.plat.config.model;
import com.lidennis.plat.common.model.AbstractBaseModel;

public class ConfigProcMapping extends AbstractBaseModel
{
    private static final long serialVersionUID = 1L;
    
    protected String  typeCode;       
    protected String  beanName; 
    
    public String getTypeCode()
    {
        return typeCode;
    }
    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }
    public String getBeanName()
    {
        return beanName;
    }
    public void setBeanName(String beanName)
    {
        this.beanName = beanName;
    }
}
