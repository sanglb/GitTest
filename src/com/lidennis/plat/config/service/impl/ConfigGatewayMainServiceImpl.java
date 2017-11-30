package com.lidennis.plat.config.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javapns.back.PushNotificationManager;
import javapns.back.SSLConnectionHelper;

import net.sf.ehcache.Cache;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.model.AbstractBaseModel;
import com.lidennis.plat.common.model.OrderByModel;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.AbstractBaseService;
import com.lidennis.plat.common.service.IEncacheService;
import com.lidennis.plat.common.util.DataOperateUtils;
import com.lidennis.plat.common.util.DataTypeUtils;
import com.lidennis.plat.common.util.DatetimeUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.model.ConfigSMSGateway;
import com.lidennis.plat.config.service.IConfigGatewayMainService;
import com.lidennis.plat.system.constant.SystemConstants;
import com.lidennis.plat.system.iface.ISyncDataService;
import com.lidennis.plat.system.model.InstantReplyData;
import com.lidennis.plat.system.util.SMSUtils;

public class ConfigGatewayMainServiceImpl extends AbstractBaseService implements IConfigGatewayMainService, ISyncDataService
{
    private static final Logger log = Logger.getLogger( ConfigGatewayMainServiceImpl.class );
    protected IEncacheService encacheService;
    
    @Override
    public ReturnMsg syncDataProc( UserContext ucontext, HttpClient httpClient, String syncType, AbstractBaseModel bizObj, Map<String, Object> dataMap ) throws Exception 
    {
        ReturnMsg rmsg = new ReturnMsg( false );
        if( ucontext != null && syncType != null && syncType.length() > 2 )
        {
            if( syncType.indexOf( "sync_cfg_params_by_web" ) == 0 )
            {
                String cfgcode1 = DataOperateUtils.findStringByMapKey( dataMap, "cfgcode1" );
                if( DataTypeUtils.checkEquals( ucontext.getManageFlag(), Constants.BOOLEAN_VALUE_TRUE ) && cfgcode1.length() >= 5 && cfgcode1.length() < 50 )
                {
                        /*
                        int num1 = 0;
                        String name1  = DataOperateUtils.findStringByMapKey( paramMap, "cfginfo1" );  
                        String flag1  = DataOperateUtils.findStringByMapKey( paramMap, "cfgflag1" );  
                        Map<String,Object> pmap2 = new HashMap<String,Object>();
                        pmap2.put( Constants.COMMON_MODEL_CODE_CODE, dataCode );
                        pmap2.put( Constants.COMMON_MODEL_CODE_CZ_ID, ucontext.getCzId() );
                        ConfigSMSGateway dobj1 = null;
                        List<ConfigSMSGateway> dlist2 = baseIbatisDao.findByParams( ConfigSMSGateway.class.getSimpleName(), 0, 1, pmap2, new OrderByModel( "ITEMID" ) );
                        if( dlist2 != null && dlist2.size() == 1 )
                        {
                            ConfigSMSGateway tmp2 = dlist2.get(0);
                            if( DataTypeUtils.checkEquals( tmp2.getCzId(), ucontext.getCzId() ) )
                            {
                                dobj1 = tmp2;
                                if( name1 != null && name1.length() > 1 && name1.length() < 50 )
                                    dobj1.setItemName( name1 );
                                log.info( "ready to update params: " + dataCode + "" );
                            }
                        }
                        
                        if( dobj1 == null ) 
                        {
                            dobj1 = new ConfigAppParam();
                            dobj1.setAppMode( "app" );
                            dobj1.setItemCode( dataCode );
                            if( name1 != null && name1.length() > 1 && name1.length() < 50 )
                                dobj1.setItemName( name1 );
                            else
                                dobj1.setItemName( "配置数据 " + DatetimeUtils.convertToDateStr( System.currentTimeMillis() ) );
                            dobj1.setCzId( ucontext.getCzId() );
                            dobj1.setEnableFlag( Constants.BOOLEAN_VALUE_TRUE );
                            dobj1.setCreateTime( System.currentTimeMillis() );
                            dobj1.setCreateUser( ucontext.getUserAcct() );
                        }
                        dataMap.put( "syncTime1", DatetimeUtils.convertToDatetimeStr( System.currentTimeMillis() ) );
                        dataMap.put( "syncUser1", ucontext.getUserAcct() );
                        dobj1.setItemType( "json" );
                        dobj1.setItemValue( JSONObject.fromObject( dataMap ).toString() );
                        if( DataTypeUtils.checkStrIsNumber( flag1 ) && flag1.length() < 5 )
                            dobj1.setEnableFlag( Integer.valueOf( flag1 ) );
                        dobj1.setUpdateTime( System.currentTimeMillis() );
                        dobj1.setUpdateUser( ucontext.getUserAcct() );
                        num1 = 0;
                        if( dobj1.getItemId() == null || dobj1.getItemId().longValue() <= 0 ) 
                            num1 = baseIbatisDao.insert( ConfigAppParam.class.getSimpleName(), dobj1 );
                        else
                            num1 = baseIbatisDao.update( ConfigAppParam.class.getSimpleName(), dobj1 );
                        if( num1 > 0 && dobj1.getItemId() != null )
                        {
                            rmsg.setSuccess( true );
                            rmsg.setResultValue( dobj1.getItemId().toString() );
                        }
                        */
                }
            }
            else if( syncType.indexOf( "xxxxxx" ) >= 0 )
            {
                
            }
        }
        
        return rmsg;
    }
    
    
    @Override
    @SuppressWarnings("unchecked")
    public List findDataByBizCode( UserContext ucontext, String bizCode, String acctCode, String dataCode, Map<String,Object> paramMap )
    {
        List dlist1 = null;
        if( ucontext != null && bizCode != null && bizCode.length() > 2 )
        {
            if( bizCode.indexOf( "sync_cfg_params_by_web" ) == 0 )
            {
                dlist1 = new ArrayList<Map<String,Object>>();
                if( DataTypeUtils.checkEquals( ucontext.getManageFlag(), Constants.BOOLEAN_VALUE_TRUE ) && dataCode != null && dataCode.length() >= 5 && dataCode.length() < 50 )
                {
                    dataCode = dataCode.toLowerCase().trim();
                    Map<String,Object> pmap2 = new HashMap<String,Object>();
                    pmap2.put( Constants.COMMON_MODEL_CODE_CODE, dataCode );
                    pmap2.put( Constants.COMMON_MODEL_CODE_CZ_ID, ucontext.getCzId() );
                    List<ConfigSMSGateway> dlist2 = baseIbatisDao.findByParams( ConfigSMSGateway.class.getSimpleName(), 0, 1, pmap2, new OrderByModel( "ITEMID" ) );
                    if( dlist2 != null && dlist2.size() == 1 )
                    {
                        ConfigSMSGateway dobj2 = dlist2.get(0);
                        if( DataTypeUtils.checkEquals( dobj2.getCzId(), ucontext.getCzId() ) )
                        {
                            Map<String,Object> dmap2 = DataOperateUtils.convertJsonStrToMap( dobj2.getItemParam() );
                            if( dmap2 == null ) 
                            {
                                dmap2 = new HashMap<String,Object>();
                            }
                            dmap2.put( "cfginfo1", dobj2.getItemName() );
                            dmap2.put( "cfgflag1", dobj2.getEnableFlag() );
                            dmap2.put( "createTime", dobj2.getCreateTime() );
                            dlist1.add( dmap2 );
                        }
                    }
                }
            }
            else if( bizCode.indexOf( "xxxxxx" ) >= 0 )
            {
                
            }
        }
        
        return dlist1;
    }
    
    @Override
    public <T> ReturnMsg execExtend(UserContext ucontext, T dataObj, Map<String, Object> paramMap) throws Exception
    {
        ReturnMsg returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        try
        {
            if( ucontext != null && dataObj != null )
            {
                ConfigSMSGateway dobj = (ConfigSMSGateway)dataObj;
                if( DataTypeUtils.checkEquals( dobj.getCzId(), ucontext.getCzId() ) || ucontext.getUserAcct().equals(Constants.COMMON_MODEL_CODE_SENIOR) )
                {
                    String testType = "";
                    if( StringUtils.checkEquals( dobj.getAppMode(), SystemConstants.INACTIVE_MODE_CODE_SMS ) )
                    {
                        if( paramMap != null && paramMap.containsKey("testType") )
                            testType    = paramMap.get( "testType" ).toString();
                        if( testType.indexOf("remain_sum") >= 0 )
                        {
                            returnMsg = SMSUtils.findRemainSum( dobj );
                        }
                        else if( testType.indexOf("receive") >= 0 )
                        {
                        }
                        else
                        {
                            String sendMobile  = "";
                            String sendContent = "";
                            String sendTimeStr = "";
                            Long   sendTime = null;
                            if( paramMap != null )
                            {
                                if( paramMap.containsKey("sendMobile") )
                                    sendMobile  = paramMap.get( "sendMobile" ).toString();
                                if( paramMap.containsKey("sendContent") )
                                    sendContent  = paramMap.get( "sendContent" ).toString();
                                if( paramMap.containsKey("sendTimeStr") )
                                    sendTimeStr  = paramMap.get( "sendTimeStr" ).toString();
                                if( sendTimeStr != null && !sendTimeStr.trim().equals("") )
                                    sendTime = DatetimeUtils.convertStrToLongTime( sendTimeStr );
                            }
                            
                            if( sendContent == null || sendContent.trim().equals("") )
                                sendContent = "Test send SMS, "+DatetimeUtils.convertToDatetimeStr( System.currentTimeMillis() );
                            returnMsg = SMSUtils.sendRealtime( null, dobj, sendMobile, sendContent, sendTime, null );
                        }
                    }
                    else if( StringUtils.checkEquals( dobj.getAppMode(), SystemConstants.INACTIVE_MODE_CODE_APNS ) )
                    {
                        Map<String,Object> cpmap = DataOperateUtils.convertJsonStrToMap( dobj.getItemParam() );
                        String clientNum  = DataOperateUtils.findStringByMapKey( cpmap, "clientNum" );
                        String apnsHost   = DataOperateUtils.findStringByMapKey( cpmap, "apnsHost" );
                        int    apnsPort   = DataOperateUtils.findIntByMapKey( cpmap, "apnsPort", 0 );
                        String certPath   = DataOperateUtils.findStringByMapKey( cpmap, "certPath" );
                        String certPwd    = DataOperateUtils.findStringByMapKey( cpmap, "certPwd" );
                        if( apnsPort <= 0 )
                            apnsPort = 2195;
                        if( clientNum.length() > 0 && apnsHost.length() > 0 && apnsPort < 65536 && certPath.length() > 0 && certPwd.length() > 0 )
                        {
                            try
                            {
                                PushNotificationManager pushManager = PushNotificationManager.getInstance();
                                pushManager.initializeConnection( apnsHost, apnsPort, certPath, certPwd, SSLConnectionHelper.KEYSTORE_TYPE_PKCS12 );
                                Thread.sleep( 500 );
                                pushManager.stopConnection();
                                returnMsg.setSuccess( true );
                                returnMsg.setMessage( "连接成功." );
                                returnMsg.setEnMessage( "Connection successful." );
                            }
                            catch( Exception err )
                            {
                                returnMsg.setSuccess( false );
                                returnMsg.setMessage( "连接失败, 请检查参数, " + err.toString() );
                                returnMsg.setEnMessage( "Connection failed, check the parameter, " + err.toString() );
                            }
                        }
                        else
                        {
                            returnMsg.setSuccess( false );
                            returnMsg.setMessage( "参数不完整, 请检查." );
                            returnMsg.setEnMessage( "Parameter is not complete, please check." );
                        }
                    }
                    else
                    {
                        returnMsg.setSuccess( false );
                        returnMsg.setMessage( "正在开发中, 请等待." );
                        returnMsg.setEnMessage( "In development, please wait." );
                    }
                }
            }
        }
        catch( Exception err )
        {
            returnMsg.setSuccess( false );
            returnMsg.setMessage( "连接出错, "+err.getMessage() );
            returnMsg.setEnMessage( "Connection error, "+err.getMessage() );
            log.error( "connectTest error, "+err.getMessage() );
        }
        
        return returnMsg;
    }
    
    @Override
    public ReturnMsg execReceive( UserContext ucontext, ConfigSMSGateway smsGateway, Map<String, Object> paramMap ) throws Exception 
    {
        ReturnMsg returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        if( ucontext != null && smsGateway != null )
        {
            List<InstantReplyData> dataList = new ArrayList<InstantReplyData>();
            returnMsg = SMSUtils.receiveRealtime( smsGateway, dataList );
            if( dataList != null && dataList.size() > 0 )
            {
                for( int i=0; i<dataList.size(); i++ )
                {
                    InstantReplyData dataObj = dataList.get(i);
                    dataObj.setCzId( ucontext.getCzId() );
                    dataObj.setCreateTime( System.currentTimeMillis() );
                    dataObj.setCreateUser( ucontext.getUserAcct() );
                    dataObj.setUpdateTime( System.currentTimeMillis() );
                    dataObj.setUpdateUser( ucontext.getUserAcct() );
                    int num = baseIbatisDao.insert( InstantReplyData.class.getSimpleName(), dataObj );
                    if( num <= 0 )
                    {
                        if( ucontext != null && StringUtils.checkEquals( Constants.LANG_TYPE_EN, ucontext.getLangType(), false ) )
                            throw new Exception( "failed to save the receive data, " + returnMsg.getResultDesc() );
                        else
                            throw new Exception( "接收数据保存失败, " + returnMsg.getResultDesc() );
                    }
                }
                
                returnMsg.setSuccess( true );
                returnMsg.setMessage( "接收数据保存成功, 数量: "+dataList.size() );
                returnMsg.setEnMessage( "Data saved success, total: "+dataList.size() );
                returnMsg.setResultCount( dataList.size() );
            }
        }
        
        return returnMsg;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reloadCacheData()
    {
        String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
        String okey = ConfigSMSGateway.class.getSimpleName();
        Cache cache = encacheService.findCache( ckey );
        if( cache != null )
        {
            Object obj = encacheService.findData( cache, okey );
            Map<String,Object> dataMap = null;
            if( obj == null )
            {
                dataMap = new ConcurrentHashMap<String,Object>();
                encacheService.putData( cache, okey, dataMap );
            }
            else
            {
                dataMap = (Map<String,Object>)obj;
                dataMap.clear();
            }
            
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put( Constants.COMMON_MODEL_CODE_ENABLE_FLAG, Constants.BOOLEAN_VALUE_TRUE );
            OrderByModel ordering = new OrderByModel( "ORDERNUM" );
            List<ConfigSMSGateway> dlist = baseIbatisDao.findByParams( ConfigSMSGateway.class.getSimpleName(), paramMap, ordering );
            if( dlist != null && dlist.size() > 0 )
            {
                for( int i=0; i<dlist.size(); i++ )
                {
                    ConfigSMSGateway dobj = dlist.get(i);
                    dobj.setParamObj( DataOperateUtils.convertJsonStrToMap( dobj.getItemParam() ) );
                    if( dobj.getCzId() != null && dobj.getCzId().longValue() > 0 )
                    {
                        if( StringUtils.checkNotEmpty( dobj.getItemCode() ) )
                            dataMap.put( dobj.getCzId() + "_" + dobj.getItemCode(), dobj );
                        dataMap.put( dobj.getCzId() + "_" + dobj.getItemId().toString(), dobj );
                    }
                    else
                    {
                        if( StringUtils.checkNotEmpty( dobj.getItemCode() ) )
                        {
                            dataMap.put( dobj.getItemCode().toString(), dobj );
                            if( StringUtils.checkNotEmpty( dobj.getAppMode() ) )
                                dataMap.put( dobj.getAppMode() + "_" + dobj.getItemCode(), dobj );  
                        }
                        dataMap.put( dobj.getItemId().toString(), dobj );
                    }
                }
                
                dataMap.put( SystemConstants.CACHE_DATA_KEY_ALL_LIST, dlist );
                if( SystemConstants.LOG_SWITCH_PROC_DEBUG_ON && SystemConstants.LOG_SWITCH_PACKAGE_INFO.length() >= 15 && SystemConstants.LOG_SWITCH_PACKAGE_INFO.indexOf( this.getClass().getPackage().getName() ) >= 0 )
                    log.info( "--> load gateway config to cache, total: "+dataMap.size() );
            }
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findCacheById( UserContext ucontext, Long itemId )
    {
        ConfigSMSGateway dataObj = null;
        if( ucontext != null && itemId != null )
        {
            String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
            String okey = ConfigSMSGateway.class.getSimpleName();
            Cache cache = encacheService.findCache( ckey );
            if( cache != null )
            {
                Object obj = encacheService.findData( cache, okey );
                if( obj == null )
                {
                    reloadCacheData();
                    obj = encacheService.findData( cache, okey );
                }
                if( obj != null )
                {
                    Map<String,Object> dataMap = (Map<String,Object>)obj;
                    if( ucontext.getCzId() != null && ucontext.getCzId().longValue() > 0 )
                    {
                        if( dataMap.containsKey( ucontext.getCzId() + "_" + itemId.toString() ) )
                            dataObj = (ConfigSMSGateway)dataMap.get( ucontext.getCzId() + "_" + itemId.toString() );
                    }
                    else
                    {
                        if( dataMap.containsKey( itemId.toString() ) )
                            dataObj = (ConfigSMSGateway)dataMap.get( itemId.toString() );
                    }
                }
            }
        }
        
        if( dataObj != null )
            return (T)dataObj;
        else
            return null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findCacheByCode( UserContext ucontext, String itemCode )
    {
        ConfigSMSGateway dataObj = null;
        if( ucontext != null && StringUtils.checkNotEmpty( itemCode ) )
        {
            String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
            String okey = ConfigSMSGateway.class.getSimpleName();
            Cache cache = encacheService.findCache( ckey );
            if( cache != null )
            {
                Object obj = encacheService.findData( cache, okey );
                if( obj == null )
                {
                    reloadCacheData();
                    obj = encacheService.findData( cache, okey );
                }
                if( obj != null )
                {
                    Map<String,Object> dataMap = (Map<String,Object>)obj;
                    if( ucontext.getCzId() != null && ucontext.getCzId().longValue() > 0 )
                    {
                        if( dataMap.containsKey( ucontext.getCzId() + "_" + itemCode ) )
                            dataObj = (ConfigSMSGateway)dataMap.get( ucontext.getCzId() + "_" + itemCode );
                    }
                    else
                    {
                        if( dataMap.containsKey( itemCode ) )
                            dataObj = (ConfigSMSGateway)dataMap.get( itemCode );
                    }
                }
            }
        }
        
        if( dataObj != null )
            return (T)dataObj;
        else
            return null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findCacheListByParams( UserContext ucontext, Map<String, Object> paramMap )
    {
        List resultList = null;
        if( ucontext != null )
        {
            String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
            String okey = ConfigSMSGateway.class.getSimpleName();
            Cache cache = encacheService.findCache( ckey );
            if( cache != null )
            {
                Object obj = encacheService.findData( cache, okey );
                if( obj == null )
                {
                    reloadCacheData();
                    obj = encacheService.findData( cache, okey );
                }
                if( obj != null )
                {
                    Map<String,Object> dataMap = (Map<String,Object>)obj;
                    if( dataMap.containsKey( SystemConstants.CACHE_DATA_KEY_ALL_LIST ) )
                    {
                        List<ConfigSMSGateway> dlist = (List<ConfigSMSGateway>)dataMap.get( SystemConstants.CACHE_DATA_KEY_ALL_LIST );
                        if( dlist != null && dlist.size() > 0 )
                        {
                            boolean flag = false;
                            String appMode  = DataOperateUtils.findStringByMapKey( paramMap, "appMode" );
                            String itemCode = DataOperateUtils.findStringByMapKey( paramMap, "itemCode" );
                            if( ucontext.getCzId() != null && ucontext.getCzId().longValue() > 0 )
                                flag  = true;
                            if( !flag && StringUtils.checkNotEmpty( appMode ) )
                                flag  = true;
                            if( !flag && StringUtils.checkNotEmpty( itemCode ) )
                                flag  = true;
                            if( flag )  
                            {
                                resultList = new ArrayList<ConfigSMSGateway>();
                                for( int m=0; m<dlist.size(); m=m+1 )
                                {
                                    flag = true;
                                    ConfigSMSGateway dobj = (ConfigSMSGateway)dlist.get(m);
                                    if( flag && StringUtils.checkNotEmpty( appMode ) )
                                    {
                                        if( !StringUtils.checkEquals( dobj.getAppMode(), appMode ) )
                                        {
                                            flag = false;
                                        }
                                    }
                                    if( flag && StringUtils.checkNotEmpty( itemCode ) )
                                    {
                                        if( !StringUtils.checkEquals( dobj.getItemCode(), itemCode ) )
                                        {
                                            flag = false;
                                        }
                                    }
                                    
                                    if( flag && dobj.getCzId() != null && dobj.getCzId().longValue() > 0 && ucontext.getCzId() != null && ucontext.getCzId().longValue() > 0 )
                                    {
                                        if( !DataTypeUtils.checkEquals( ucontext.getCzId(), dobj.getCzId() ) )
                                            flag = false;
                                    }
                                    
                                    if( flag )
                                    {
                                        resultList.add( dobj );
                                    }
                                }
                            }
                            else
                            {
                                resultList = dlist;   
                            }
                        }
                    }
                }
            }
        }
        
        return resultList;
    }
    
    
    public void setEncacheService(IEncacheService encacheService)
    {
        this.encacheService = encacheService;
    }
}
