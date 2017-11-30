package com.lidennis.plat.config.web.form;
import com.lidennis.plat.config.model.ConfigRptMainType;

public class ConfigRptMainTypeForm extends ConfigRptMainType
{
    private static final long serialVersionUID = 1L;
    protected String  operateType;
    protected Long    appId;

    public String toString()
    {
        StringBuffer str = new StringBuffer("");
        str.append( "Form [id=" ).append( getItemId() );
        str.append( ", code=" ).append( getItemCode() );
        str.append( ", name=" ).append( getItemName() );
        str.append( ", enabled=" ).append( getEnableFlag() );
        str.append( "]" );
        return str.toString();
    }
    
    
    public String getOperateType()
    {
        return operateType;
    }
    public void setOperateType(String operateType)
    {
        this.operateType = operateType;
    }
    public Long getAppId()
    {
        return appId;
    }
    public void setAppId(Long appId)
    {
        this.appId = appId;
    }
}
