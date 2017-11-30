package com.lidennis.plat.config.web.action;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.IBaseService;
import com.lidennis.plat.common.service.ICacheService;
import com.lidennis.plat.common.util.DataOperateUtils;
import com.lidennis.plat.common.util.DataTypeUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.constant.ConfigConstants;
import com.lidennis.plat.config.model.ConfigDictDetail;
import com.lidennis.plat.config.model.ConfigReportsMain;
import com.lidennis.plat.config.web.form.ConfigReportsMainForm;
import com.lidennis.plat.common.web.base.AbstractBaseAction;
import com.lidennis.plat.system.constant.SystemConstants;
import com.opensymphony.xwork2.ModelDriven;

public class ConfigReportsMainAction extends AbstractBaseAction implements ModelDriven<ConfigReportsMainForm>
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger( ConfigReportsMainAction.class );
    private ConfigReportsMainForm form = new ConfigReportsMainForm();
    protected IBaseService  configReportsMainService;
    protected ICacheService configDictDetailService;
    
    public Map<String,Object> findRequestParamsMap() throws Exception
    {
        UserContext userContext = this.findUserContext();
        Map<String,Object> paramMap = super.findRequestParamsMap( form );
        if( form.getAppId() != null )
            paramMap.put( "appId", form.getAppId() );
        else
            paramMap.put( "appId", "-1" );
        if( StringUtils.checkNotEmpty( form.getUuidCode() ) )
            paramMap.put( Constants.COMMON_MODEL_CODE_UUID, form.getUuidCode().trim() );
        if( StringUtils.checkNotEmpty( form.getBizMode() ) )
            paramMap.put( "bizMode", form.getBizMode().trim() );
        if( StringUtils.checkNotEmpty( form.getBizLabel() ) )
            paramMap.put( "bizLabel", "%" + form.getBizLabel().trim() + "%" );
        if( StringUtils.checkNotEmpty( form.getTypeCode() ) )
            paramMap.put( Constants.COMMON_MODEL_CODE_TYPE_CODE, form.getTypeCode().trim() );
        if( form.getParentId() != null )
        {
            if( cascadeFlag != null && cascadeFlag.intValue() == 1 )
            {
                if( form.getParentId().longValue() != 0 )
                {
                    ConfigReportsMain dataObj = configReportsMainService.findById( userContext, form.getParentId() );
                    if( dataObj != null )                    
                        paramMap.put( "levelPath", dataObj.getLevelPath()+"%" );
                    else
                        paramMap.put( "levelPath", "-1" );
                    paramMap.put( "itemId_notexist", form.getParentId() );
                }
            }
            else
            {
                paramMap.put( "parentId", form.getParentId() );
            }
        }
        if( form.getLevelNum() != null )
            paramMap.put( "levelNum", form.getLevelNum() );
        if( form.getLeafFlag() != null )
            paramMap.put( "leafFlag", form.getLeafFlag() );
        paramMap.put( "createUser", userContext.getUserAcct() );   
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
                OrderByModel ordering = this.findRequestOrderByModel( "" );
                Map<String,Object> paramMap = this.findRequestParamsMap();
                if( limit > 0 )
                {
                    this.checkUserOperatePower( ConfigReportsMainAction.class, "find" );
                    if( returnMsg.isSuccess() ) 
                    {
                        totalRows = configReportsMainService.findCountByParams( ucontext, paramMap );
                        if( totalRows > 0 )
                            resultList = configReportsMainService.findByParams( ucontext, start, limit, paramMap, ordering );
                    }
                    if( resultList == null )
                        resultList = new ArrayList();
                    if( start == 0 )
                    {
                        if( !this.checkFindOperateRecord( ConfigConstants.MODULE_NAME_REPORTS_MAIN ) )
                            this.registerLog( "查询报告信息, 总数: "+totalRows, "Query reports info, total: "+totalRows, LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_REPORTS_MAIN, OperateTypeEnum.QUERY, OperateFlagEnum.SUCCESS, resultList.size(), form.toString() );
                    }
                }
                else
                {
                    if( simpleFindFlag.intValue() == Constants.BOOLEAN_VALUE_TRUE )
                        resultList = configReportsMainService.findSimpleByParams( ucontext, paramMap, ordering );
                    else
                        resultList = configReportsMainService.findByParams( ucontext, paramMap, ordering );
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
        this.checkUserOperatePower( ConfigReportsMainAction.class, "find" );
        if( returnMsg.isSuccess() ) 
        {
            if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                UserContext ucontext = this.findUserContext();
                ConfigReportsMain dataObj = configReportsMainService.findById( ucontext, form.getItemId() );
                if( dataObj != null )
                {
                    if( StringUtils.checkEquals( dataObj.getCzCode(), ucontext.getCzCode() ) || DataTypeUtils.checkEquals( ucontext.getCzId(), dataObj.getCzId() ) )
                    {
                        this.matchObjRelateData( ucontext, dataObj );
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
        AbstractBaseModel baseModel = this.checkDataBySave( ucontext, ConfigReportsMain.class, form );
        if( ucontext != null && baseModel != null )
        {
            try
            {
                ConfigReportsMain dataObj = (ConfigReportsMain)baseModel;
                if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                {
                    if( dataObj.getItemCode() != null && !dataObj.getItemCode().trim().equals("") )
                        form.setItemCode( dataObj.getItemCode() );
                    form.setAppId( dataObj.getAppId() );
                    form.setUuidCode( dataObj.getUuidCode() );
                    form.setSubitemCount( dataObj.getSubitemCount() );
                    if( dataObj.getTypeCode() != null && dataObj.getTypeCode().trim().equals("dashboard") )
                    {
                        form.setTypeCode( dataObj.getTypeCode() );
                        form.setOrderNum( 1 );
                    }
                    
                    BeanUtils.copyProperties( dataObj, form );
                    returnMsg = configReportsMainService.update( ucontext, dataObj, null );
                    if( returnMsg.isSuccess() )
                    {
                        operateId = dataObj.getItemId();
                        this.registerLog( "报告["+dataObj.getItemName()+"]修改成功", "Reports["+dataObj.getItemName()+"] update success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_REPORTS_MAIN, OperateTypeEnum.UPDATE, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName() );
                    }
                }
                else
                {
                    dataObj.setUuidCode( DataOperateUtils.buildUuidCodeStr( true ) );
                    dataObj.setSubitemCount( 0 );
                    returnMsg = configReportsMainService.insert( ucontext, dataObj, null );
                    if( returnMsg.isSuccess() )
                    {
                        operateId = dataObj.getItemId();
                        this.registerLog( "报告["+dataObj.getItemName()+"]新增成功", "Reports["+dataObj.getItemName()+"] create success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_REPORTS_MAIN, OperateTypeEnum.INSERT, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName() );
                    }
                }
            }
            catch( Exception err )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
                returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
                log.error( "save() error, " + err.toString() + ", " + form.toString() );
            }
        }
        
        return SUCCESS;
    }
    
    public String delete() throws Exception
    {
        this.checkUserOperatePower( ConfigReportsMainAction.class, "delete" );
        if( returnMsg.isSuccess() )
        {
            try
            {
                UserContext ucontext = this.findUserContext();
                returnMsg = configReportsMainService.deleteBatch( ucontext, selIdStr, null );
                if( returnMsg.isSuccess() )
                {
                    this.registerLog( "报告删除成功, 数量: "+returnMsg.getResultCount(), "Reports delete success, total: "+returnMsg.getResultCount(), LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_REPORTS_MAIN, OperateTypeEnum.DELETE, OperateFlagEnum.SUCCESS, returnMsg.getResultCount(), "delete id: "+selIdStr+"  detail: "+returnMsg.getResultDesc() );
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
    
    @SuppressWarnings("unchecked")
    private void matchObjRelateData( UserContext ucontext, List dataList )
    {
        if( dataList != null && dataList.size() > 0 )
        {
            for( int i=0; i<dataList.size(); i++ )
            {
                ConfigReportsMain dataObj = (ConfigReportsMain)dataList.get(i);
                this.matchObjRelateData( ucontext, dataObj );
            }
        }
    }
    
    private void matchObjRelateData( UserContext ucontext, ConfigReportsMain dataObj )
    {
        if( dataObj != null )
        {
            dataObj.setCzCode( "" );
            dataObj.setLevelPath( "" );
            if( dataObj.getClassifyId() != null )
            {
                ConfigDictDetail dictObj = configDictDetailService.findCacheById( ucontext, dataObj.getClassifyId() ); 
                if( dictObj != null )
                {
                    dataObj.setClassifyName( dictObj.getItemName() );
                }
            }
        }
    }
    
    
    public ConfigReportsMainForm getModel()
    {
        return form;
    }
    public void setConfigReportsMainService(IBaseService configReportsMainService)
    {
        this.configReportsMainService = configReportsMainService;
    }
    public void setConfigDictDetailService(ICacheService configDictDetailService)
    {
        this.configDictDetailService = configDictDetailService;
    }
    
}
