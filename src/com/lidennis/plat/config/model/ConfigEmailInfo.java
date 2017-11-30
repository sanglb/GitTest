package com.lidennis.plat.config.model;
import java.util.Date;

import com.lidennis.plat.common.model.AbstractBaseModel;

public class ConfigEmailInfo extends AbstractBaseModel
{
    private static final long serialVersionUID = -5537475598705859729L;
    
    protected String serverCode;
    protected String senderEmail;
    protected String toEmail;
    protected String ccEmail;
    protected String itemSubject;
    protected String itemContent;
    protected String charEncode;           
    protected Date   sendTime;
    protected Date   receiveTime;
    
    
    public String getServerCode()
    {
        return serverCode;
    }
    public void setServerCode(String serverCode)
    {
        this.serverCode = serverCode;
    }
    public String getSenderEmail()
    {
        return senderEmail;
    }
    public void setSenderEmail(String senderEmail)
    {
        this.senderEmail = senderEmail;
    }
    public String getToEmail()
    {
        return toEmail;
    }
    public void setToEmail(String toEmail)
    {
        this.toEmail = toEmail;
    }
    public String getCcEmail()
    {
        return ccEmail;
    }
    public void setCcEmail(String ccEmail)
    {
        this.ccEmail = ccEmail;
    }
    public String getItemSubject()
    {
        return itemSubject;
    }
    public void setItemSubject(String itemSubject)
    {
        this.itemSubject = itemSubject;
    }
    public String getItemContent()
    {
        return itemContent;
    }
    public void setItemContent(String itemContent)
    {
        this.itemContent = itemContent;
    }
    public String getCharEncode()
    {
        return charEncode;
    }
    public void setCharEncode(String charEncode)
    {
        this.charEncode = charEncode;
    }
    public Date getSendTime()
    {
        return sendTime;
    }
    public void setSendTime(Date sendTime)
    {
        this.sendTime = sendTime;
    }
    public Date getReceiveTime()
    {
        return receiveTime;
    }
    public void setReceiveTime(Date receiveTime)
    {
        this.receiveTime = receiveTime;
    }
}
