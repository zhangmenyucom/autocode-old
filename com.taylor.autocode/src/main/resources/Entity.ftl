package ${OBJ_ENTITY_PACKAGE};

import java.io.Serializable;
<#list OBJ_IMPORTS as imp>  
import ${imp};  
</#list>

/**
 * @notes:${OBJ_NOTES}
 *
 * @author ${OBJ_AUTHOR}
 *
 * @date  ${OBJ_CDATE}	Email:${OBJ_AUTHOR_EMAIL}
 */
public class ${OBJ_NAME} implements Serializable {

	private static final long serialVersionUID = ${OBJ_NO}L;
	
	<#list OBJ_PROPERTYS as pro>
    private ${pro.propertyType} ${pro.propertyName};
    
    </#list>

	<#list OBJ_PROPERTYS as pro>
    public ${pro.propertyType} ${pro.propertyGetMethod}() {
        return ${pro.propertyName};
    }

    public void ${pro.propertySetMethod}(${pro.propertyType} ${pro.propertyName}) {
        this.${pro.propertyName} = ${pro.propertyName};
    }
    
    </#list>
}