package com.lidennis.plat.config.web.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.constant.LogLevelEnum;
import com.lidennis.plat.common.constant.LogTypeEnum;
import com.lidennis.plat.common.constant.OperateFlagEnum;
import com.lidennis.plat.common.constant.OperateTypeEnum;
import com.lidennis.plat.common.model.AbstractBaseModel;
import com.lidennis.plat.common.model.OrderByModel;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.util.DataTypeUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.constant.ConfigConstants;
import com.lidennis.plat.config.model.ConfigSMSGateway;
import com.lidennis.plat.config.service.IConfigGatewayMainService;
import com.lidennis.plat.config.web.form.ConfigSMSGatewayForm;
import com.lidennis.plat.system.constant.SystemConstants;
import com.lidennis.plat.system.model.PlatCztmMain;
import com.lidennis.plat.system.service.ICommonDataUtils;
import com.lidennis.plat.system.service.IDataDetectionProcess;
import com.lidennis.plat.common.web.base.AbstractBaseAction;
import com.opensymphony.xwork2.ModelDriven;

public class ConfigGatewayMainAction extends AbstractBaseAction implements ModelDriven<ConfigSMSGatewayForm>
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger( ConfigGatewayMainAction.class );
    private ConfigSMSGatewayForm form = new ConfigSMSGatewayForm();
    protected ICommonDataUtils commonDataUtils;
    protected IConfigGatewayMainService configGatewayMainService;
    protected IDataDetectionProcess dataDetectionProcess;
    
    public Map<String,Object> findRequestParamsMap() throws Exception
    {
        Map<String,Object> paramMap = super.findRequestParamsMap( form );
        if( StringUtils.checkNotEmpty( form.getAppMode() ) )
            paramMap.put( "appMode", form.getAppMode() );
        if( StringUtils.checkNotEmpty( form.getTypeCode() ) )
            paramMap.put( Constants.COMMON_MODEL_CODE_TYPE_CODE, form.getTypeCode() );
        if( form.getClassifyId() != null )
            paramMap.put( "classifyId", form.getClassifyId() );
        return paramMap;
    }
    
    @SuppressWarnings("unchecked")
    public String findmain()
    {
        try
        {
            UserContext ucontext = this.findUserContext();
            if( ucontext != null )
            {
                OrderByModel ordering = new OrderByModel( "CZID|ORDERNUM" );
                Map<String,Object> paramMap = this.findRequestParamsMap();
                if( limit > 0 )
                {
                    this.checkUserOperatePower( ConfigGatewayMainAction.class, "find" );
                    if( returnMsg.isSuccess() ) 
                    {
                        totalRows = configGatewayMainService.findCountByParams( ucontext, paramMap );
                        if( totalRows > 0 )
                            resultList = configGatewayMainService.findByParams( ucontext, start, limit, paramMap, ordering );
                    }
                    if( resultList == null )
                        resultList = new ArrayList();
                    if( start == 0 )
                    {
                        if( !this.checkFindOperateRecord( ConfigConstants.MODULE_NAME_SMS_GATEWAY ) )
                            this.registerLog( "查询网关配置信息, 总数: "+totalRows, "Query gateway config info, total: "+totalRows, LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_SMS_GATEWAY, OperateTypeEnum.QUERY, OperateFlagEnum.SUCCESS, resultList.size(), form.toString() );
                    }
                }
                else
                {
                    if( simpleFindFlag.intValue() == Constants.BOOLEAN_VALUE_TRUE )
                        resultList = configGatewayMainService.findSimpleByParams( ucontext, paramMap, ordering );
                    else
                        resultList = configGatewayMainService.findByParams( ucontext, paramMap, ordering );
                    if( resultList == null )
                        resultList = new ArrayList();
                }
                
                this.matchObjRelateData( ucontext, resultList );
                if( start <= 0 && SystemConstants.LOG_SWITCH_SEARCH_DEBUG_ON )
                {
                    if( StringUtils.checkEquals( ucontext.getUserAcct(), Constants.COMMON_MODEL_CODE_SENIOR ) )
                        log.info( "--> find result number: " + resultList.size() + ", start: " + start + ", limit: " + limit + ", totalRows: " + totalRows + ", req ip: " + this.findRequestIpAddr() + ", params: " + JSONObject.fromObject( paramMap ).toString() );
                    else
                        log.info( "--> find result number: " + resultList.size() + ", by: " + ucontext.getUserAcct() + ", " + ucontext.getUserInfo() + ", start: " + start + ", limit: " + limit + ", totalRows: " + totalRows  + ", req ip: " + this.findRequestIpAddr() + ", params: " + JSONObject.fromObject( paramMap ).toString() );
                }
            }
        }
        catch( Exception err )
        {
            log.error( "findmain() error, " + err.toString() + ", " + form.toString() );
        }
        
        return SUCCESS;
    }
    
    @SuppressWarnings("unchecked")
    public String findById()
    {
        resultList = new ArrayList();
        this.checkUserOperatePower( ConfigGatewayMainAction.class, "find" );
        if( returnMsg.isSuccess() ) 
        {
            if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                UserContext ucontext = this.findUserContext();
                ConfigSMSGateway dataObj = configGatewayMainService.findById( ucontext, form.getItemId() );
                if( dataObj != null )
                {
                    if( DataTypeUtils.checkEquals( ucontext.getCzId(), dataObj.getCzId() ) )
                    {
                        resultList.add( dataObj );
                    }
                }
            }
        }
        return SUCCESS;
    }
    
    public String save() throws Exception
    { 
        UserContext ucontext = this.findUserContext();
        AbstractBaseModel baseModel = this.checkDataBySave( ucontext, ConfigSMSGateway.class, form );
        if( ucontext != null && baseModel != null )
        {
            try
            {
                ConfigSMSGateway dataObj = (ConfigSMSGateway)baseModel;
                if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                {
                    if( dataObj.getItemCode() != null && !dataObj.getItemCode().trim().equals("") )
                        form.setItemCode( dataObj.getItemCode() );
                    form.setAppMode( dataObj.getAppMode() );  
                    form.setStatDateStr( dataObj.getStatDateStr() );
                    form.setStatCount( dataObj.getStatCount() );
                    BeanUtils.copyProperties( dataObj, form );
                    returnMsg = configGatewayMainService.update( ucontext, dataObj, null );
                    if( returnMsg.isSuccess() )
                    {
                        this.registerLog( "网关配置["+dataObj.getItemName()+"]修改成功", "Gateway config["+dataObj.getItemName()+"] update success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_SMS_GATEWAY, OperateTypeEnum.UPDATE, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName() );
                        try {
                            dataDetectionProcess.updateNotify( ucontext, ConfigSMSGateway.class.getSimpleName(), "configGatewayMainService" );
                        }
                        catch( Exception e ){
                        }
                    }
                }
                else
                {
                    if( ucontext.getUserAcct().equals(Constants.COMMON_MODEL_CODE_SENIOR) )
                    {
                        if( dataObj.getCzId() == null || dataObj.getCzId().longValue() < 0 )
                            dataObj.setCzId( 0L );    
                    }
                    
                    returnMsg = configGatewayMainService.insert( ucontext, dataObj, null );
                    if( returnMsg.isSuccess() )
                    {
                        this.registerLog( "网关配置["+dataObj.getItemName()+"]新增成功", "Gateway config["+dataObj.getItemName()+"] create success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_SMS_GATEWAY, OperateTypeEnum.INSERT, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName() );
                        try {
                            dataDetectionProcess.updateNotify( ucontext, ConfigSMSGateway.class.getSimpleName(), "configGatewayMainService" );
                        }
                        catch( Exception e ){
                        }
                    }
                }
            }
            catch( Exception err )
            {
                log.error( "save() error, " + err.toString() + ", " + form.toString() );
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
                returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
            }
        }
        
        return SUCCESS;
    }
    
    public String delete() throws Exception
    {
        this.checkUserOperatePower( ConfigGatewayMainAction.class, "delete" );
        if( returnMsg.isSuccess() )
        {
            try
            {
                UserContext ucontext = this.findUserContext();
                returnMsg = configGatewayMainService.deleteBatch( ucontext, selIdStr, null );
                if( returnMsg.isSuccess() )
                {
                    this.registerLog( "网关配置删除成功, 数量: "+returnMsg.getResultCount(), "Gateway config delete success, total: "+returnMsg.getResultCount(), LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_SMS_GATEWAY, OperateTypeEnum.DELETE, OperateFlagEnum.SUCCESS, returnMsg.getResultCount(), "delete id: "+selIdStr+"  detail: "+returnMsg.getResultDesc() );
                    try {
                        dataDetectionProcess.updateNotify( ucontext, ConfigSMSGateway.class.getSimpleName(), "configGatewayMainService" );
                    }
                    catch( Exception e ){
                    }
                } 
            }
            catch( Exception err )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
                returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
                log.error( "delete() error, " + err.toString() + ", " + form.toString() );
            }
        }
        return SUCCESS;
    }
    
    public String receiveData()
    {
        returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        try
        {
            UserContext ucontext = this.findUserContext();
            if( form.getItemId() != null && form.getItemId() > 0 )
            {
                ConfigSMSGateway dataObj = configGatewayMainService.findById( ucontext, form.getItemId() );
                if( dataObj != null )
                {
                    returnMsg = configGatewayMainService.execReceive( ucontext, dataObj, null );
                    if( returnMsg.isSuccess() )
                    {
                        String content = returnMsg.getResultDesc();      
                        if( content != null && content.length() > 500 )
                            content = content.substring( 0, 500 ) + " ......";
                        this.registerLog( "网关("+dataObj.getItemName()+")接收数据成功, 数量: "+returnMsg.getResultCount(), "Gateway("+dataObj.getItemName()+") receive data success, total: "+returnMsg.getResultCount(), LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_SMS_GATEWAY, OperateTypeEnum.EXECUTE, OperateFlagEnum.SUCCESS, returnMsg.getResultCount(), dataObj.getItemId() + ", "+dataObj.getItemCode()+", content: "+content );
                    }
                }
            }
             
        }
        catch( Exception err )
        {
            returnMsg.setSuccess( false );
            returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
            returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
            log.error( "receiveData() error, " + err.toString() + ", " + form.toString() );
        }
        
        return SUCCESS;
    }
    
    public String connTest()
    {
        returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        UserContext ucontext = this.findUserContext();
        String requestIp = this.findRequestIpAddr();
        if( form.getItemId() != null && form.getItemId() > 0 )
        {
            ConfigSMSGateway dataObj = configGatewayMainService.findById( ucontext, form.getItemId() );
            if( dataObj != null )
            {
                try
                {
                    Map<String,Object> paramMap = new HashMap<String,Object>();
                    paramMap.put( "testType",    form.getTestType() );
                    paramMap.put( "sendMobile",  form.getSendMobile() );
                    paramMap.put( "sendContent", form.getSendContent() );
                    paramMap.put( "sendTimeStr", form.getSendTimeStr() );
                    returnMsg = configGatewayMainService.execExtend( ucontext, dataObj, paramMap );
                    log.info( "connTest, requestIp: " + requestIp + ", operator: " + ucontext.getUserAcct() + ", code: "+dataObj.getItemCode()+", result: " + returnMsg.getResultDesc() );
                }
                catch( Exception err )
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
                    returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
                    log.error( "connTest() error, " + err.toString() + ", by: " + ucontext.getUserAcct() + ", gateway: " + dataObj.getItemId() + ", " + dataObj.getItemCode() + ", " + dataObj.getItemName() + ", requestIp: " + requestIp + this.findRequestBaseInfo() );
                }
            }
        }
        return SUCCESS;
    }
    
    @SuppressWarnings("unchecked")
    private void matchObjRelateData( UserContext ucontext, List dataList )
    {
        if( dataList != null && dataList.size() > 0 )
        {
            for( int i=0; i<dataList.size(); i++ )
            {
                ConfigSMSGateway dobj = (ConfigSMSGateway)dataList.get(i);
                this.matchObjRelateData( ucontext, dobj );
            }
        }
    }
    
    private void matchObjRelateData( UserContext ucontext, ConfigSMSGateway dobj )
    {
        if( dobj != null )
        {
            if( dobj.getCzId() != null && dobj.getCzId().longValue() > 0 && ucontext != null && ucontext.getUserAcct().equals(Constants.COMMON_MODEL_CODE_SENIOR) )
            {
                PlatCztmMain cobj = commonDataUtils.findCztmAndSync( ucontext, dobj.getCzId(), null );
                if( cobj != null )
                {
                    dobj.setCompyInfo( cobj.getItemName() );
                }
            }
            else 
            {
                dobj.setCzCode( "" );
                dobj.setCzId( null );
            }
        }
    }
    
    
    public ConfigSMSGatewayForm getModel()
    {
        return form;
    }
    public void setCommonDataUtils(ICommonDataUtils commonDataUtils)
    {
        this.commonDataUtils = commonDataUtils;
    }
    public void setConfigGatewayMainService(IConfigGatewayMainService configGatewayMainService)
    {
        this.configGatewayMainService = configGatewayMainService;
    }
    public void setDataDetectionProcess(IDataDetectionProcess dataDetectionProcess)
    {
        this.dataDetectionProcess = dataDetectionProcess;
    }
    
}
