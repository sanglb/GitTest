package com.lidennis.plat.config.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.ehcache.Cache;

import org.apache.log4j.Logger;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.model.OrderByModel;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.AbstractBaseService;
import com.lidennis.plat.common.service.IEncacheService;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.model.ConfigDictDetail;
import com.lidennis.plat.config.model.ConfigDictType;
import com.lidennis.plat.config.service.IConfigDictDetailService;
import com.lidennis.plat.system.constant.SystemConstants;
import com.lidennis.plat.system.external.ClientSearchForm;

public class ConfigDictDetailServiceImpl extends AbstractBaseService implements IConfigDictDetailService
{
    private static final Logger log = Logger.getLogger( ConfigDictDetailServiceImpl.class );
    protected IEncacheService encacheService;
    
    @Override
    @SuppressWarnings("unchecked")
    public List search( UserContext ucontext, String type, int start, int limit, ClientSearchForm form, Map<String, Object> exparamMap ) 
    {
        List resultList = new ArrayList();
        if( type != null && type.indexOf( "dict_" ) == 0 )
        {
            String code = type.replaceAll( "dict_", "" ).trim();
            if( code.length() > 0 )
            {
                Map<String,Object> pmap = new HashMap<String,Object>();
                pmap.put( "dictTypeCode", code );
                pmap.put( Constants.COMMON_MODEL_CODE_ENABLE_FLAG, Constants.BOOLEAN_VALUE_TRUE );
                List<ConfigDictDetail> dlist = baseIbatisDao.findByParams( ConfigDictDetail.class.getSimpleName(), pmap, new OrderByModel( "ORDERNUM" ) );
                if( dlist != null && dlist.size() > 0 )
                {
                    for( int i=0; i<dlist.size(); i=i+1 )
                    {
                        ConfigDictDetail tmpobj = dlist.get(i);
                        Map<String,Object> dmap = new HashMap<String,Object>();
                        dmap.put( "dataClfy", "" );
                        dmap.put( "dataId", "" );
                        if( StringUtils.checkNotEmpty( tmpobj.getItemEnName() ) )
                            dmap.put( "dataName", tmpobj.getItemEnName().trim() + "(" + tmpobj.getItemName() + ")" );
                        else
                            dmap.put( "dataName", tmpobj.getItemName() );
                        dmap.put( "dataEnName", tmpobj.getItemEnName() );
                        dmap.put( "bizCode", "" );
                        dmap.put( "dataValue", tmpobj.getItemValue() );
                        if( tmpobj.getExval02() != null )
                            dmap.put( "dataInfo1", tmpobj.getExval02() );  
                        else
                            dmap.put( "dataInfo1", "" );
                        if( tmpobj.getExval01() != null )
                            dmap.put( "dataInfo2", tmpobj.getExval01() );  
                        else
                            dmap.put( "dataInfo2", "" );
                        resultList.add( dmap );
                    }
                }
                
            }
        }
        
        return resultList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ReturnMsg syncDataByClient( UserContext ucontext, String dataModel, String opType, Map<String, Object> dataMap, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = new ReturnMsg( false );
        return returnMsg;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void reloadCacheData()
    {
        String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
        String okey = ConfigDictDetail.class.getSimpleName();
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
            
            Map<String,Object> pmap = new HashMap<String,Object>();
            List<ConfigDictDetail> dlist = baseIbatisDao.findByParams( ConfigDictDetail.class.getSimpleName(), pmap, null );
            if( dlist != null && dlist.size() > 0 )
            {
                for( int i=0; i<dlist.size(); i=i+1 )
                {
                    ConfigDictDetail dobj = dlist.get(i);
                    dataMap.put( dobj.getItemId().toString(), dobj );
                    if( dobj.getCzId() != null && dobj.getCzId().longValue() > 0 )
                    {
                        if( StringUtils.checkNotEmpty( dobj.getDictTypeCode() ) ) 
                        {
                            if( dobj.getItemValue() != null && dobj.getItemValue().length() < 30 && dobj.getItemValue().indexOf( "{" ) < 0 )
                                dataMap.put( dobj.getCzId() + "__" + dobj.getDictTypeCode() + "__" + dobj.getItemValue().trim(), dobj ); 
                            if( StringUtils.checkNotEmpty( dobj.getBizCode() ) )
                                dataMap.put( dobj.getCzId() + "__" + dobj.getDictTypeCode() + "__" + dobj.getBizCode(), dobj );
                        }
                    }
                    else
                    {
                        if( StringUtils.checkNotEmpty( dobj.getDictTypeCode() ) )
                        {
                            if( dobj.getItemValue() != null && dobj.getItemValue().length() < 30 && dobj.getItemValue().indexOf( "{" ) < 0 )
                                dataMap.put( dobj.getDictTypeCode() + "__" + dobj.getItemValue().trim(), dobj ); 
                            if( StringUtils.checkNotEmpty( dobj.getBizCode() ) )
                                dataMap.put( dobj.getDictTypeCode() + "__" + dobj.getBizCode(), dobj );
                        }
                    }
                }
                
                dataMap.put( SystemConstants.CACHE_DATA_KEY_ALL_LIST, dlist );
            }
            
            if( SystemConstants.LOG_SWITCH_PROC_DEBUG_ON && SystemConstants.LOG_SWITCH_PACKAGE_INFO.length() >= 15 && SystemConstants.LOG_SWITCH_PACKAGE_INFO.indexOf( this.getClass().getPackage().getName() ) >= 0 )
                log.info( "--> load data dict to cache, total: "+dataMap.size() );
        }
        
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findCacheById( UserContext ucontext, Long itemId )
    {
        ConfigDictDetail dataObj = null;
        if( ucontext != null && itemId != null )
        {
            String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
            String okey = ConfigDictDetail.class.getSimpleName();
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
                    if( dataMap.containsKey( itemId.toString() ) )
                        dataObj = (ConfigDictDetail)dataMap.get( itemId.toString() );
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
        ConfigDictDetail dataObj = null;
        if( ucontext != null && StringUtils.checkNotEmpty( itemCode ) )
        {
            String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
            String okey = ConfigDictDetail.class.getSimpleName();
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
                        if( dataMap.containsKey( ucontext.getCzId() + "__" + itemCode ) )
                            dataObj = (ConfigDictDetail)dataMap.get( ucontext.getCzId() + "__" + itemCode );
                    }
                    if( dataObj == null )
                    {
                        if( dataMap.containsKey( itemCode ) )
                            dataObj = (ConfigDictDetail)dataMap.get( itemCode );
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
        String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
        String okey = ConfigDictDetail.class.getSimpleName();
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
                    resultList = (List<ConfigDictDetail>)dataMap.get( SystemConstants.CACHE_DATA_KEY_ALL_LIST );
                }
            }
        }
        
        return resultList;
    }
    
    @Override
    protected <T> ReturnMsg checkBySave( UserContext ucontext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkBySave( ucontext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            if( model instanceof ConfigDictDetail )
            {
                ConfigDictDetail dataObj = (ConfigDictDetail)model;
                if( dataObj.getDictTypeId() == null || dataObj.getDictTypeId().longValue() <= 0 )
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( "必须关联一个字典类型." );
                    returnMsg.setEnMessage( "must be associated with a dictionary type." );
                    return returnMsg;
                }
                else
                {
                    ConfigDictType dictType = baseIbatisDao.findById( ConfigDictType.class.getSimpleName(), dataObj.getDictTypeId() );
                    if( dictType != null )
                    {
                        dataObj.setDictTypeCode( dictType.getItemCode() );
                        dataObj.setCzId( dictType.getCzId() );
                    }
                    else
                    {
                        returnMsg.setSuccess( false );
                        returnMsg.setMessage( "找不到关联的字典类型." );
                        returnMsg.setEnMessage( "can not find the type of associated dictionary." );
                        return returnMsg;
                    }
                }
                if( dataObj.getItemValue() == null || dataObj.getItemValue().trim().equals("") )
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( "字典值不能为空." );
                    returnMsg.setEnMessage( "dictionary value can not be empty." );
                    return returnMsg;
                }
                else
                {
                    dataObj.setItemValue( dataObj.getItemValue().trim() );
                }
                
                Map<String,Object> pmap = new HashMap<String,Object>();
                if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                    pmap.put( "itemId_notexist", dataObj.getItemId() );
                pmap.put( "dictTypeId", dataObj.getDictTypeId() );
                pmap.put( "itemValue", dataObj.getItemValue().trim() );
                pmap.put( Constants.COMMON_MODEL_CODE_CZ_ID, dataObj.getCzId() );
                int num = this.findCountByParams( ucontext, pmap );
                if( num > 0 )
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( "该值在同一类型下已经存在." );
                    returnMsg.setEnMessage( "the value in the same type already exists" );
                    return returnMsg;
                }
            }
        }
        
        return returnMsg;
    }
    
    @SuppressWarnings("unchecked")
    public <T> ReturnMsg insert( UserContext ucontext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = checkBySave( ucontext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            returnMsg.setSuccess( false );
            returnMsg.setMessage( Constants.MSG_OPERATE_FAILED );
            returnMsg.setEnMessage( Constants.MSG_OPERATE_FAILED_EN );
            if( model instanceof ConfigDictDetail )
            {
                ConfigDictDetail dataObj = (ConfigDictDetail)model;
                int num = 0;
                if( extendedDao == null )
                    num = baseIbatisDao.insert( modelName, model );
                else
                    num = extendedDao.insert( modelName, model );
                if( num > 0 )
                {
                    ConfigDictType dictType = baseIbatisDao.findById( ConfigDictType.class.getSimpleName(), dataObj.getDictTypeId() );
                    if( dictType != null )
                    {
                        Map<String,Object> paramMap = new HashMap<String,Object>();
                        paramMap.put( "dictTypeId", dataObj.getDictTypeId() );
                        num = baseIbatisDao.findCountByParams( ConfigDictDetail.class.getSimpleName(), paramMap );
                        dictType.setItemCount( num );
                        baseIbatisDao.update( ConfigDictType.class.getSimpleName(), dictType );
                    }
                    
                    returnMsg.setSuccess( true );
                    returnMsg.setResultCount( 1 );
                    returnMsg.setMessage( Constants.MSG_OPERATE_SUCCESS );
                    returnMsg.setEnMessage( Constants.MSG_OPERATE_SUCCESS_EN );
                }
            }
        }
        else
        {
            if( ucontext != null && StringUtils.checkEquals( Constants.LANG_TYPE_EN, ucontext.getLangType(), false ) )
                throw new Exception( returnMsg.getEnMessage() );
            else
                throw new Exception( returnMsg.getMessage() );
        }
        return returnMsg;
    }
    

    @SuppressWarnings("unchecked")
    public ReturnMsg deleteBatch( UserContext ucontext, final String selIdStr, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        if( selIdStr != null && !selIdStr.equals("") )
        {
            StringBuffer deleteInfoStr = new StringBuffer("");
            String[] idArray = selIdStr.trim().split(",");
            for( int i=0; i<idArray.length; i++ )
            {
                Object obj = this.findById( ucontext, Long.parseLong(idArray[i]) );
                returnMsg = checkByDelete( ucontext, obj, exparamMap );
                if( returnMsg.isSuccess() )
                {
                    ConfigDictDetail dataObj = (ConfigDictDetail)obj;
                    int num = 0;
                    if( extendedDao == null )
                        num = baseIbatisDao.delete( modelName, obj );
                    else
                        num = extendedDao.delete( modelName, obj );
                    if( num > 0 )
                    {
                        ConfigDictType dictType = baseIbatisDao.findById( ConfigDictType.class.getSimpleName(), dataObj.getDictTypeId() );
                        if( dictType != null )
                        {
                            Map<String,Object> paramMap = new HashMap<String,Object>();
                            paramMap.put( "dictTypeId", dataObj.getDictTypeId() );
                            num = baseIbatisDao.findCountByParams( ConfigDictDetail.class.getSimpleName(), paramMap );
                            dictType.setItemCount( num );
                            baseIbatisDao.update( ConfigDictType.class.getSimpleName(), dictType );
                        }
                        
                        if( i > 0 )
                            deleteInfoStr.append(", ");
                        deleteInfoStr.append("[").append( dataObj.getItemId()).append("|").append(dataObj.getItemCode()).append("]").append(dataObj.getItemName());
                    }
                    else
                    {
                        String desc = "";
                        if( dataObj.getItemName() != null && !dataObj.getItemName().trim().equals("") )
                            desc = "["+dataObj.getItemName()+"]";
                        if( ucontext != null && StringUtils.checkEquals( Constants.LANG_TYPE_EN, ucontext.getLangType(), false ) )
                            throw new Exception( desc + "deletion is unsuccessful." );
                        else
                            throw new Exception( desc + "删除不成功." );
                    }
                }
                else
                {
                    if( ucontext != null && StringUtils.checkEquals( Constants.LANG_TYPE_EN, ucontext.getLangType(), false ) )
                        throw new Exception( returnMsg.getEnMessage() );
                    else
                        throw new Exception( returnMsg.getMessage() );
                }
            }
            
            returnMsg.setSuccess( true );
            returnMsg.setResultCount( idArray.length );
            returnMsg.setResultDesc( deleteInfoStr.toString() );
            returnMsg.setMessage( Constants.MSG_OPERATE_SUCCESS );
            returnMsg.setEnMessage( Constants.MSG_OPERATE_SUCCESS_EN );
            if( log.isInfoEnabled() )
                log.info( "user["+ucontext.getUserAcct()+"] delete the data from ["+modelName+"], detail: "+deleteInfoStr.toString() );
        }
        
        return returnMsg;
    }
    
    
    public void setEncacheService(IEncacheService encacheService)
    {
        this.encacheService = encacheService;
    }
}
