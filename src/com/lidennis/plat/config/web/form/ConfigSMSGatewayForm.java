package com.lidennis.plat.config.web.form;
import com.lidennis.plat.config.model.ConfigSMSGateway;

public class ConfigSMSGatewayForm extends ConfigSMSGateway
{
    private static final long serialVersionUID = 1L;
    
    protected String  testType;
    protected String  sendMobile;
    protected String  sendContent;
    protected String  sendTimeStr;

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
    
    
    public String getTestType()
    {
        return testType;
    }
    public void setTestType(String testType)
    {
        this.testType = testType;
    }
    public String getSendMobile()
    {
        return sendMobile;
    }
    public void setSendMobile(String sendMobile)
    {
        this.sendMobile = sendMobile;
    }
    public String getSendContent()
    {
        return sendContent;
    }
    public void setSendContent(String sendContent)
    {
        this.sendContent = sendContent;
    }
    public String getSendTimeStr()
    {
        return sendTimeStr;
    }
    public void setSendTimeStr(String sendTimeStr)
    {
        this.sendTimeStr = sendTimeStr;
    }
    
}
