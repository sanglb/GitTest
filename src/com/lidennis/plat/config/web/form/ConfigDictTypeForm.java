package com.lidennis.plat.config.web.form;
import com.lidennis.plat.config.model.ConfigDictType;

public class ConfigDictTypeForm extends ConfigDictType
{
    private static final long serialVersionUID = 344151551586129088L;

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
}
