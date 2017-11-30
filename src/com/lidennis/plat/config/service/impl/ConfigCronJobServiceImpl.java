package com.lidennis.plat.config.service.impl;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.AbstractBaseService;
import com.lidennis.plat.common.util.DatetimeUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.constant.ConfigConstants;
import com.lidennis.plat.config.model.ConfigConnServer;
import com.lidennis.plat.config.model.ConfigCronJob;
import com.lidennis.plat.config.service.IConfigCronJobService;

public class ConfigCronJobServiceImpl extends AbstractBaseService implements IConfigCronJobService
{
    private static final Logger log = Logger.getLogger( ConfigCronJobServiceImpl.class );
    
    protected <T> ReturnMsg checkByDelete( UserContext userContext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkByDelete( userContext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            ConfigCronJob dataObj = (ConfigCronJob)model;
            if( !StringUtils.checkEquals( dataObj.getProcStatus(), ConfigConstants.PROC_RUN_STATUS_INIT ) && !StringUtils.checkEquals( dataObj.getProcStatus(), ConfigConstants.PROC_RUN_STATUS_STOP ) )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( "只有状态为初始或停止的任务配置可以删除." );
                returnMsg.setEnMessage( "the only state to init or stop the task configuration can be deleted." );
                return returnMsg;
            }
            if( StringUtils.checkEquals( dataObj.getProcStatus(), ConfigConstants.PROC_RUN_STATUS_STOP ) )
            {
                this.execJobRequest( userContext, dataObj, ConfigConstants.JOB_EXEC_OPERATE_DELETE );
            }
            /*
            Map<String,Object> pmap = new HashMap<String,Object>();
            pmap.put( Constants.COMMON_MODEL_CODE_TYPE_CODE, "cronjob" );
            pmap.put( "jobId", dataObj.getItemId() );
            int num = baseIbatisDao.findCountByParams( RunLog.class.getSimpleName(), pmap );
            if( num > 0 )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( "["+dataObj.getItemName()+"]已经存在"+num+"条运行日志, 不能删除." );
                returnMsg.setEnMessage( "["+dataObj.getItemName()+"] already exist "+num+" running log, can not be deleted." );
                return returnMsg;
            }
            */
        }
        
        return returnMsg;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public ReturnMsg execJobRequest( UserContext userContext, ConfigCronJob dataObj, String operateType ) throws Exception
    {
        ReturnMsg returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        if( dataObj != null && dataObj.getServerCode() != null && !dataObj.getServerCode().trim().equals("") )
        {
            if( operateType.equals( ConfigConstants.JOB_EXEC_OPERATE_STOP ) )
            {
                if( !dataObj.getProcStatus().equals( ConfigConstants.PROC_RUN_STATUS_RUN ) && !dataObj.getProcStatus().equals( ConfigConstants.PROC_RUN_STATUS_ERROR ) )
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( Constants.MSG_OPERATE_FAILED_PREFIX + "只有状态为运行或错误的任务实例可以停止." );
                    returnMsg.setEnMessage( Constants.MSG_OPERATE_FAILED_PREFIX_EN + "the only state to run or error the task instance can be stopped." );
                    return returnMsg;
                }
            }
            else if( operateType.equals( ConfigConstants.JOB_EXEC_OPERATE_DELETE ) )
            {
                if( !dataObj.getProcStatus().equals( ConfigConstants.PROC_RUN_STATUS_RUN ) && !dataObj.getProcStatus().equals( ConfigConstants.PROC_RUN_STATUS_STOP ) )
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( Constants.MSG_OPERATE_FAILED_PREFIX + "只有状态为运行或停止的任务实例可以删除." );
                    returnMsg.setEnMessage( Constants.MSG_OPERATE_FAILED_PREFIX_EN + "the only state to run or stop the task instance can be deleted." );
                    return returnMsg;
                }
            }
            ConfigConnServer serverObj = baseIbatisDao.findByCode( ConfigConnServer.class.getSimpleName(), dataObj.getServerCode().trim() );
            if( serverObj != null && serverObj.getConnectUrl() != null )
            {
                try
                {
                  //http://127.0.0.1:8080/dlinmkty_batch/services/EsbService
                    String wserviceUrl = serverObj.getConnectUrl().trim();
                    String wsdlUrl = wserviceUrl + "?WSDL";
                    WSDLFactory factory = WSDLFactory.newInstance();
                    WSDLReader reader = factory.newWSDLReader();
                    reader.setFeature( "javax.wsdl.verbose", true );
                    reader.setFeature( "javax.wsdl.importDocuments", true );
                    Definition def = reader.readWSDL( wsdlUrl );
                    if( def != null && def.getTargetNamespace() != null && !def.getTargetNamespace().trim().equals( "" ) )
                    {
                        String targetNamespace = def.getTargetNamespace().trim();
                        String eventId = DatetimeUtils.convertToDatetimeStr( System.currentTimeMillis(), "yyyyMMddHHmmssSSS" ) +String.format("%03d", 1);
                        String serviceCode = dataObj.getServiceCode();
                        if( serviceCode == null || serviceCode.trim().equals("") )
                            serviceCode = "OD.ESB.CRONJOB";
                        Map<String,String> paramMap = new HashMap<String,String>();
                        paramMap.put( "code", dataObj.getUuidCode() );
                        paramMap.put( "operateType", operateType );
                        paramMap.put( "cz", "platform" );
                        MessageFactory messageFactory = MessageFactory.newInstance();
                        SOAPMessage message = messageFactory.createMessage();
                        SOAPPart soapPart = message.getSOAPPart();
                        SOAPEnvelope envelope = soapPart.getEnvelope();
                        SOAPBody body = envelope.getBody();
                        Name bodyName = envelope.createName( "request", "", targetNamespace );
                        SOAPElement bodyElement = body.addChildElement( bodyName );
                        bodyElement.addChildElement("serviceCode").addTextNode( serviceCode );
                        bodyElement.addChildElement("eventId").addTextNode( eventId );
                        bodyElement.addChildElement("userAccount").addTextNode( serverObj.getItemAcct() );
                        bodyElement.addChildElement("authCode").addTextNode( serverObj.getItemPwd() );
                        bodyElement.addChildElement("serviceParams").addTextNode( "<![CDATA["+JSONObject.fromObject( paramMap ).toString()+"]]>" );
                        message.saveChanges(); 
                        String xmlString = "";
                        SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance(); 
                        SOAPConnection connection = soapConnFactory.createConnection();
                        SOAPMessage replyMsg = connection.call( message, wserviceUrl );
                        Source replyContent = replyMsg.getSOAPPart().getContent();
                        ByteArrayOutputStream responseOuts = new ByteArrayOutputStream();
                        StreamResult streamResult = new StreamResult( responseOuts );
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        transformer.transform( replyContent, streamResult );
                        xmlString = responseOuts.toString("UTF-8");
                        log.info( "job("+dataObj.getItemName()+"|"+serverObj.getConnectUrl()+") exec response content(xml): " + xmlString );
                        responseOuts.close();
                        connection.close();
                        Document document = DocumentHelper.parseText( xmlString );
                        Element root = document.getRootElement(); 
                        Element resultMessageElement = findResultMessageNode( root );
                        if( resultMessageElement != null )
                        {
                            String  resultCode = "";
                            String  resultMessage = "";
                            Iterator iterator = resultMessageElement.elementIterator();
                            while( iterator != null && iterator.hasNext() )
                            {
                                Element subElement = (Element)iterator.next();
                                if( subElement.getName() != null )
                                {
                                    String elementName = subElement.getName().trim().toLowerCase();
                                    if( elementName.indexOf( "resultcode" ) >= 0 )
                                        resultCode = subElement.getText();
                                    if( elementName.indexOf( "resultmessage" ) >= 0 )
                                        resultMessage = subElement.getText();
                                }
                            }
                            if( resultCode.trim().equals( "1" ) )
                            {
                                dataObj.setProcStatus( operateType );
                                baseIbatisDao.update( ConfigCronJob.class.getSimpleName(), dataObj );
                                returnMsg.setSuccess( true );
                                returnMsg.setMessage( Constants.MSG_OPERATE_SUCCESS );
                                returnMsg.setEnMessage( Constants.MSG_OPERATE_SUCCESS_EN );
                                returnMsg.setResultDesc( xmlString );
                            }
                            else
                            {
                                returnMsg.setSuccess( false );
                                returnMsg.setMessage( resultMessage );
                                returnMsg.setEnMessage( resultMessage );
                                returnMsg.setResultDesc( xmlString );
                            }
                        }
                        else
                        {
                            returnMsg.setSuccess( false );
                            returnMsg.setMessage( Constants.MSG_OPERATE_FAILED_PREFIX + "执行结果解析错误, " + xmlString );
                            returnMsg.setEnMessage( Constants.MSG_OPERATE_FAILED_PREFIX_EN + "result message parsing error, " + xmlString );
                            returnMsg.setResultDesc( xmlString );
                        }
                    }
                    else
                    {
                        returnMsg.setSuccess( false );
                        returnMsg.setMessage( Constants.MSG_OPERATE_FAILED_PREFIX + "服务器对应的URL不存在或不正确, " + serverObj.getConnectUrl() );
                        returnMsg.setEnMessage( Constants.MSG_OPERATE_FAILED_PREFIX_EN + "the server URL does not exist or incorrect, " + serverObj.getConnectUrl() );
                    }
                }
                catch( Exception err )
                {
                    log.error( "job("+dataObj.getItemName()+") exec request error, " + err.getMessage() );
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
                    returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
                }
            }
            else
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_OPERATE_FAILED_PREFIX + "找不到定时任务关联的服务器配置." );
                returnMsg.setEnMessage( Constants.MSG_OPERATE_FAILED_PREFIX_EN + "can not find the timing tasks associated server configuration." );
            }
        }
        return returnMsg;
    }
    
    @SuppressWarnings("unchecked")
    private Element findResultMessageNode( Element element )
    {
        Element resultMessageElement = null;
        Iterator iterator = element.elementIterator();
        while( iterator != null && iterator.hasNext() )
        {
            Element subElement = (Element)iterator.next();
            if( subElement.getName() != null )
            {
                String elementName = subElement.getName().trim().toLowerCase();
                if( elementName.indexOf( "responsemessage" ) >= 0 )
                {
                    resultMessageElement = subElement;
                    break;
                }
                else
                {
                    Element findElement = findResultMessageNode( subElement );
                    if( findElement != null )
                    {
                        resultMessageElement = findElement;
                        break;
                    }
                }
            }
        }
        return resultMessageElement;
    }
}
