package com.lidennis.plat.config.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.ehcache.Cache;

import org.apache.log4j.Logger;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.AbstractBaseService;
import com.lidennis.plat.common.service.IEncacheService;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.model.ClientVersionMain;
import com.lidennis.plat.config.model.ClientVersionSub;
import com.lidennis.plat.config.service.IClientVersionMainService;
import com.lidennis.plat.system.constant.SystemConstants;

public class ClientVersionMainServiceImpl extends AbstractBaseService implements IClientVersionMainService
{
    private static final Logger log = Logger.getLogger(ClientVersionMainServiceImpl.class);
    protected IEncacheService encacheService;
    
    @Override
    protected <T> ReturnMsg checkBySave( UserContext ucontext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkBySave( ucontext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            ClientVersionMain dataObj = (ClientVersionMain)model;
            if( dataObj.getItemName() == null || dataObj.getItemName().trim().equals("") )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_NAME_NO_EMPTY );
                returnMsg.setEnMessage( Constants.MSG_NAME_NO_EMPTY_EN );
                return returnMsg;
            }
            if( StringUtils.checkIsEmpty( dataObj.getClientType() ) )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( "客户端类型不能为空." );
                returnMsg.setEnMessage( "client type can not be empty." );
                return returnMsg;
            }
        }
        
        return returnMsg;
    }
    
    @Override
    protected <T> ReturnMsg checkByDelete( UserContext ucontext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkByDelete( ucontext, model, null );
        if( returnMsg.isSuccess() )
        {
            ClientVersionMain dataObj = (ClientVersionMain)model;
            Map<String,Object> pmap1 = new HashMap<String,Object>();
            pmap1.put( "mainId", dataObj.getItemId() );
            int num = baseIbatisDao.findCountByParams( ClientVersionSub.class.getSimpleName(), pmap1 );
            if( num > 0 )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( "该数据关联了子版本, 不能删除." );
                returnMsg.setEnMessage( "the data associated with sub versions, can not be deleted." );
                return returnMsg;
            }
        }
        
        return returnMsg;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void reloadCacheData()
    {
        String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
        String okey = ClientVersionMain.class.getSimpleName();
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
            List<ClientVersionMain> dlist = baseIbatisDao.findByParams( ClientVersionMain.class.getSimpleName(), pmap, null );
            if( dlist != null && dlist.size() > 0 )
            {
                for( int i=0; i<dlist.size(); i=i+1 )
                {
                    ClientVersionMain dobj = dlist.get(i);
                    dataMap.put( dobj.getItemId().toString(), dobj );
                    if( StringUtils.checkNotEmpty( dobj.getItemCode() ) )
                    {
                        if( StringUtils.checkEquals( dobj.getAppMode(), Constants.COMMON_CODE_SYSTEM ) )
                        {
                            dataMap.put( dobj.getItemCode(), dobj ); 
                        }
                        else if( dobj.getCzId() != null && dobj.getCzId().longValue() > 0 )
                        {
                            dataMap.put( dobj.getCzId() + "__" + dobj.getItemCode(), dobj );
                        }
                    }
                    if( StringUtils.checkNotEmpty( dobj.getUuidCode() ) )
                    {
                        dataMap.put( dobj.getUuidCode(), dobj ); 
                    }
                }
                
                dataMap.put( SystemConstants.CACHE_DATA_KEY_ALL_LIST, dlist );
            }
            
            if( SystemConstants.LOG_SWITCH_PROC_DEBUG_ON && SystemConstants.LOG_SWITCH_PACKAGE_INFO.length() >= 15 && SystemConstants.LOG_SWITCH_PACKAGE_INFO.indexOf( this.getClass().getPackage().getName() ) >= 0 )
                log.info( "--> load client version main to cache, total: " + dataMap.size() );
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findCacheById( UserContext ucontext, Long itemId )
    {
        ClientVersionMain dataObj = null;
        if( ucontext != null && itemId != null )
        {
            String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
            String okey = ClientVersionMain.class.getSimpleName();
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
                        dataObj = (ClientVersionMain)dataMap.get( itemId.toString() );
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
        ClientVersionMain dataObj = null;
        if( ucontext != null && StringUtils.checkNotEmpty( itemCode ) )
        {
            String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
            String okey = ClientVersionMain.class.getSimpleName();
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
                    if( dataMap.containsKey( itemCode ) )
                    {
                        dataObj = (ClientVersionMain)dataMap.get( itemCode );
                    }
                    else if( dataObj == null )
                    {
                        if( ucontext.getCzId() != null && ucontext.getCzId().longValue() > 0 )
                        {
                            if( dataMap.containsKey( ucontext.getCzId() + "__" + itemCode ) )
                                dataObj = (ClientVersionMain)dataMap.get( ucontext.getCzId() + "__" + itemCode );
                        }
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
        String okey = ClientVersionMain.class.getSimpleName();
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
                    resultList = (List<ClientVersionMain>)dataMap.get( SystemConstants.CACHE_DATA_KEY_ALL_LIST );
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
